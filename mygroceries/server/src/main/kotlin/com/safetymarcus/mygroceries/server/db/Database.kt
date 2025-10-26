
package com.safetymarcus.mygroceries.server.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway

import java.sql.DriverManager
import org.postgresql.util.PSQLException

object Database {

    fun init() = hikari().let {
        Database.connect(it)
        runFlyway(it)
    }

    private fun hikari() = HikariDataSource(HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = System.getProperty("db.jdbc.url.with.db")
        username = System.getProperty("db.username")
        password = System.getProperty("db.password")
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
        validate()
    })

    private fun runFlyway(dataSource: HikariDataSource) = Flyway
        .configure()
        .dataSource(dataSource)
        .load()
        .migrate()
}
