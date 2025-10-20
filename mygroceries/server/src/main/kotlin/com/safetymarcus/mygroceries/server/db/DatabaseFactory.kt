
package com.safetymarcus.mygroceries.server.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway

import java.sql.DriverManager

object DatabaseFactory {

    fun init() {
        createDatabaseIfNotExists()
        val dataSource = hikari()
        Database.connect(dataSource)
        runFlyway(dataSource)
    }

    private fun createDatabaseIfNotExists() {
        try {
            val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "postgres")
            val statement = connection.createStatement()
            statement.executeUpdate("CREATE DATABASE mygroceries")
            statement.close()
            connection.close()
        } catch (e: Exception) {
            // Database already exists
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/mygroceries"
        config.username = "postgres"
        config.password = "postgres"
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
