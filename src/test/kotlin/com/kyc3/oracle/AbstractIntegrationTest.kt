package com.kyc3.oracle

import com.kyc3.oracle.config.CashierContractV2TestConfig
import com.kyc3.oracle.config.XMPPTestConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.io.File

@SpringBootTest
@Import(CashierContractV2TestConfig::class, XMPPTestConfiguration::class)
@ActiveProfiles("test")
abstract class AbstractIntegrationTest {

    companion object {

        val postgresImage = DockerImageName.parse("postgres:12")
        val postges = KPostgreSQLContainer(postgresImage)

        var environment = KDockerComposeContainer(File("src/test/resources/docker-compose.yml"))
            .withExposedService("testnet", 8545)

        init {
            postges.start()
            environment.start()

            System.setProperty("spring.datasource.url", postges.jdbcUrl)
            System.setProperty("spring.datasource.username", postges.username)
            System.setProperty("spring.datasource.password", postges.password)

            System.setProperty(
                "web3.host",
                "http://localhost:${environment.getServicePort("testnet", 8545)}"
            )
        }
    }

    class KPostgreSQLContainer(dockerImageName: DockerImageName) :
        PostgreSQLContainer<KPostgreSQLContainer>(dockerImageName)

    class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)
}
