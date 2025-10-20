
package com.safetymarcus.mygroceries.server.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway

import java.sql.DriverManager
import org.postgresql.util.PSQLException

object DatabaseFactory {

    fun init() {
        val dataSource = hikari()
        Database.connect(dataSource)
        runFlyway(dataSource)
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = System.getProperty("db.jdbc.url.with.db")
        config.username = System.getProperty("db.username")
        config.password = System.getProperty("db.password")
        config.maximumPoolSize = 10
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    private fun runFlyway(dataSource: HikariDataSource) {
        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()
    }
}
