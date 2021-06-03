import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.5.0"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("org.flywaydb.flyway") version "7.9.1"
  id("nu.studer.jooq") version "5.2.1"
  kotlin("jvm") version "1.5.10"
  kotlin("plugin.spring") version "1.5.10"
}

group = "com.kyc3"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
  mavenCentral()
  mavenLocal()
}

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

  implementation("org.web3j:core:5.0.0")
  implementation("com.squareup.okhttp3:okhttp:4.9.1")

  implementation("com.kyc3:oracle-definitions:e7aefc9")

  runtimeOnly("org.postgresql:postgresql")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
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
  url = "jdbc:postgresql://localhost:5432/oracle"
  user = "oracle"
  password = "oracle"
  schemas = arrayOf("public")
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
          url = "jdbc:postgresql://localhost:5432/oracle"
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