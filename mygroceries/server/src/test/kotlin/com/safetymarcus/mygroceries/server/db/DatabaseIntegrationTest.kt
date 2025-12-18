
package com.safetymarcus.mygroceries.server.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.util.UUID

object TestTable : Table("test_table") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

object Categories : Table("categories") {
    val id = uuid("id")
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

class DatabaseIntegrationTest {

    private lateinit var dataSource: HikariDataSource

    @BeforeEach
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

    @AfterEach
    fun tearDown() {
        transaction {
            // List of known tables that need to be cleaned up
            val tablesToClean = listOf(
                "test_table",  // Test table used only in this test
                "categories",
                "products",
                "orders",
                "line_items"
            )
            
            // Truncate each table with CASCADE to handle foreign key constraints
            tablesToClean.forEach { table ->
                try {
                    exec("TRUNCATE TABLE $table CASCADE;")
                } catch (e: Exception) {}
            }
        }
        dataSource.close()
    }

    @Test
    fun `flyway migrations create expected schema`() {
        transaction {
            // Verify that a table created by Flyway exists
            val tables = org.jetbrains.exposed.v1.jdbc.transactions.transaction {
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

    @Test
    fun `category table handles UUID primary keys correctly`() {
        transaction {
            SchemaUtils.create(Categories) // Ensure the Categories table is created for this test

            val testUuid = UUID.randomUUID()
            val categoryName = "Test Category UUID"

            // Insert a new category with a UUID
            Categories.insert {
                it[Categories.id] = testUuid
                it[Categories.name] = categoryName
            }

            // Retrieve the category by UUID
            val retrievedCategory = Categories.selectAll().where { Categories.id eq testUuid }.singleOrNull()

            assertTrue(retrievedCategory != null, "Retrieved category should not be null")
            assertEquals(testUuid, retrievedCategory?.get(Categories.id))
            assertEquals(categoryName, retrievedCategory?.get(Categories.name))
        }
    }
}
