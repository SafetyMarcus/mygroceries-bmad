package com.safetymarcus.mygroceries.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabaseFactory {
    private var database: Database? = null

    fun init() {
        if (database != null) {
            return
        }

        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;"
        database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(Categories)
        }
    }

    fun close() {
        
    }
}