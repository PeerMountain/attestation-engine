import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.bmuschko.gradle.docker.tasks.container.*
import com.bmuschko.gradle.docker.tasks.image.*
import java.lang.Thread.sleep
import org.ajoberstar.grgit.Grgit

plugins {
  id("org.springframework.boot") version "2.5.0"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("org.flywaydb.flyway") version "7.9.1"
  id("nu.studer.jooq") version "5.2.1"
  kotlin("jvm") version "1.5.10"
  kotlin("plugin.spring") version "1.5.10"
  id("com.bmuschko.docker-remote-api") version "7.0.1"
  id("org.ajoberstar.grgit") version "4.0.1"
}

group = "com.kyc3"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val grgit = Grgit.open(mapOf("dir" to project.projectDir))
val commit = grgit.head().abbreviatedId


val testContainerVersion = "1.15.3"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.springframework.boot:spring-boot-starter-jooq")
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  implementation("org.springframework.boot:spring-boot-starter-jooq")
  jooqGenerator("org.postgresql:postgresql:42.2.14")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

  implementation("org.flywaydb:flyway-core")

  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

  implementation("org.igniterealtime.smack:smack-tcp:4.3.5")
  implementation("org.igniterealtime.smack:smack-im:4.3.5")
  implementation("org.igniterealtime.smack:smack-extensions:4.3.5")
  implementation("org.igniterealtime.smack:smack-java7:4.3.5")

  implementation("commons-io:commons-io:2.8.0")

  implementation("org.web3j:core:5.0.0")
  implementation("com.squareup.okhttp3:okhttp:4.9.1")

  implementation("com.kyc3:oracle-definitions:9fac369")

  runtimeOnly("org.postgresql:postgresql")

  implementation("com.muquit.libsodiumjna:libsodium-jna:1.0.4") {
    exclude ("org.slf4j", "slf4j-log4j12")
  }

  implementation("org.ehcache:ehcache:3.1.3")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")

  testImplementation("org.testcontainers:testcontainers:$testContainerVersion")
  testImplementation("org.testcontainers:postgresql:$testContainerVersion")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

flyway {
  url = "jdbc:postgresql://localhost:5433/oracle"
  user = "oracle"
  password = "oracle"
  schemas = arrayOf("public")
}

val pullPostgresImage by tasks.creating(DockerPullImage::class.java) {
  image.set("postgres:12")
}

val createPostgresContainer by tasks.creating(DockerCreateContainer::class) {
  dependsOn(pullPostgresImage)
  targetImageId("postgres:12")
  hostConfig.portBindings.set(listOf("5433:5432"))
  hostConfig.autoRemove.set(true)
  withEnvVar("POSTGRES_USER", "oracle")
  withEnvVar("POSTGRES_PASSWORD", "oracle")
}

val startPostgresContainer by tasks.creating(DockerStartContainer::class) {
  dependsOn(createPostgresContainer)
  targetContainerId(createPostgresContainer.getContainerId())
  doLast {
    sleep(20 * 1000)
  }
}

val stopPostgresContainer by tasks.creating(DockerStopContainer::class) {
  targetContainerId(createPostgresContainer.getContainerId())
}

val buildAppDockerImage by tasks.creating(DockerBuildImage::class) {
  inputDir.set(file("."))
  images.add("registry.amlapi.com/kyc3/pmo-oracle-be/oracle:$commit")
  images.add("oracle:$commit")
  images.add("oracle:latest")
  images.add("registry.amlapi.com/kyc3/pmo-oracle-be/oracle:latest")
}

val pushAppDockerImage by tasks.creating(DockerPushImage::class) {
  images.add("registry.amlapi.com/kyc3/pmo-oracle-be/oracle:latest")
  images.add("registry.amlapi.com/kyc3/pmo-oracle-be/oracle:$commit")
  registryCredentials.username.set(System.getenv("CI_REGISTRY_USER"))
  registryCredentials.password.set(System.getenv("CI_REGISTRY_PASSWORD"))
  registryCredentials.url.set("registry.amlapi.com")
}

jooq {
  version.set("3.14.7")
  edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

  configurations {
    create("main") {
      generateSchemaSourceOnCompilation.set(true)

      jooqConfiguration.apply {
        logging = org.jooq.meta.jaxb.Logging.WARN
        jdbc.apply {
          driver = "org.postgresql.Driver"
          url = "jdbc:postgresql://localhost:5433/oracle"
          user = "oracle"
          password = "oracle"
        }
        generator.apply {
          name = "org.jooq.codegen.DefaultGenerator"
          database.apply {
            name = "org.jooq.meta.postgres.PostgresDatabase"
            inputSchema = "public"
          }
          generate.apply {
            isDeprecated = false
            isRecords = true
            isImmutablePojos = true
            isFluentSetters = true
          }
          target.apply {
            packageName = "com.kyc3.oracle.types"
            directory = "build/generated-src/jooq/main"
          }
          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
        }
      }
    }
  }
}

tasks["flywayMigrate"].dependsOn("startPostgresContainer")
tasks["generateJooq"].dependsOn("flywayMigrate")
tasks["generateJooq"].finalizedBy("stopPostgresContainer")
