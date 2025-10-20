
package com.safetymarcus.mygroceries.server.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway

import java.sql.DriverManager
import org.postgresql.util.PSQLException

object DatabaseFactory {

    fun init() {
        createDatabaseIfNotExists()
        val dataSource = hikari()
        Database.connect(dataSource)
        runFlyway(dataSource)
    }

    private fun createDatabaseIfNotExists() {
        val jdbcUrl = System.getenv("DB_JDBC_URL") ?: "jdbc:postgresql://localhost:5432/"
        val username = System.getenv("DB_USERNAME") ?: "marcushooper"
        val password = System.getenv("DB_PASSWORD") ?: ""

        try {
            val connection = DriverManager.getConnection(jdbcUrl, username, password)
            val statement = connection.createStatement()
            statement.executeUpdate("CREATE DATABASE mygroceries")
            statement.close()
            connection.close()
        } catch (e: PSQLException) {
            if (e.getSQLState() == "42P04") {
                println("Database 'mygroceries' already exists.")
            } else {
                System.err.println("Error creating database: ${e.message}")
                throw e
            }
        } catch (e: Exception) {
            System.err.println("Unexpected error creating database: ${e.message}")
            throw e
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = System.getenv("DB_JDBC_URL_WITH_DB") ?: "jdbc:postgresql://localhost:5432/mygroceries"
        config.username = System.getenv("DB_USERNAME") ?: "postgres"
        config.password = System.getenv("DB_PASSWORD") ?: "postgres"
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
