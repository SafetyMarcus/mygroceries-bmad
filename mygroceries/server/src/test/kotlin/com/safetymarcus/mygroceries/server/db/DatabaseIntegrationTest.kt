
package com.safetymarcus.mygroceries.server.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals

object TestTable : Table("test_table") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

class DatabaseIntegrationTest {

    private lateinit var dataSource: HikariDataSource

    @Before
    fun setup() {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = System.getProperty("db.jdbc.url.with.db")
        config.username = System.getProperty("db.username")
        config.password = System.getProperty("db.password")
        config.maximumPoolSize = 10
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        dataSource = HikariDataSource(config)

        // Manually run Flyway to ensure schema is up-to-date for tests
        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()

        Database.connect(dataSource)
    }

    @After
    fun tearDown() {
        // Clean up the database after each test if necessary, or rely on Flyway clean/migrate for next test
        // For now, we'll just close the data source.
        dataSource.close()
    }

    @Test
    fun `flyway migrations create expected schema`() {
        transaction {
            // Verify that a table created by Flyway exists
            val tables = org.jetbrains.exposed.sql.transactions.transaction {
                val schema = object : Table("information_schema.tables") {
                    val name = varchar("table_name", 255)
                    val tableSchema = varchar("table_schema", 255)
                }
                schema.selectAll()
                    .where { schema.tableSchema eq "public" }
                    .map { it[schema.name].lowercase() }
            }
            assertTrue("categories" in tables)
            assertTrue("products" in tables)
            assertTrue("orders" in tables)
            assertTrue("line_items" in tables)
        }
    }

    @Test
    fun `basic data persistence works`() {
        transaction {
            SchemaUtils.create(TestTable) // Create a test table for this specific test

            val insertedId = TestTable.insert {
                it[name] = "Test Item"
            } get TestTable.id

            val result = TestTable.selectAll().where { TestTable.id eq insertedId }.single()

            assertEquals("Test Item", result[TestTable.name])
        }
    }
}
