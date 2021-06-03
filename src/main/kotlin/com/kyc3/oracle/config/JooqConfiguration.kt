package com.kyc3.oracle.config

import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class JooqConfiguration {

  @Bean
  fun connectionProvider(dataSource: DataSource): DataSourceConnectionProvider =
      DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))

  @Bean
  fun configuration(connectionProvider: DataSourceConnectionProvider): DefaultConfiguration =
      DefaultConfiguration()
          .also {
            it.set(connectionProvider)
            it.setSQLDialect(SQLDialect.POSTGRES)
          }

  @Bean
  fun dsl(configuration: DefaultConfiguration): DefaultDSLContext =
      DefaultDSLContext(configuration)
}