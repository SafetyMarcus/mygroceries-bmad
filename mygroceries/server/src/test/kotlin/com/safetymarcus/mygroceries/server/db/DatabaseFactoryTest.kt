
package com.safetymarcus.mygroceries.server.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Assert.assertNotNull
import org.junit.Test

class DatabaseFactoryTest {

    @Before
    fun setup() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    }

    @Test
    fun `database connection is initialized`() {
        transaction {
            assertNotNull(db)
        }
    }
}
