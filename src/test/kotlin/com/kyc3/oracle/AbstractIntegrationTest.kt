package com.kyc3.oracle

import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest
abstract class AbstractIntegrationTest {

  companion object {

    val postgresImage = DockerImageName.parse("postgres:12")
    val postges = KPostgreSQLContainer(postgresImage)

    init {
      postges.start()

      System.setProperty("spring.datasource.url", postges.jdbcUrl)
      System.setProperty("spring.datasource.username", postges.username)
      System.setProperty("spring.datasource.password", postges.password)
    }
  }

  class KPostgreSQLContainer(dockerImageName: DockerImageName) :
    PostgreSQLContainer<KPostgreSQLContainer>(dockerImageName)
}