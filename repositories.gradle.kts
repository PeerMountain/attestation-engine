@file:Suppress("PropertyName")

val PRIVATE_TOKEN = extra.properties["kyc3.com.apiToken"] as? String

gradle.projectsLoaded {
    allprojects {
        repositories {
            mavenCentral()
            mavenLocal()
            maven {
                url = uri("https://gitlab.amlapi.com/api/v4/projects/57/packages/maven")
                name = "GitLab"
                credentials(HttpHeaderCredentials::class.java) {
                    name = "Private-Token"
                    value = PRIVATE_TOKEN
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
            maven {
                url = uri("https://gitlab.amlapi.com/api/v4/projects/57/packages/maven")
                name = "GitLabCI"
                credentials(HttpHeaderCredentials::class.java) {
                    name = "Job-Token"
                    value = System.getenv("CI_JOB_TOKEN")
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
            maven {
                url = uri("https://www.igniterealtime.org/archiva/repository/maven")
                name = "Nightly Apache"
            }
        }
    }
}
