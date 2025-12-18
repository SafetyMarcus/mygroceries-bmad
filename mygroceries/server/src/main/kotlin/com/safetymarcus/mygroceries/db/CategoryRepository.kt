package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import java.util.UUID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object CategoryRepository {
    private fun toCategory(row: ResultRow) = Category(
        stringId = row[Categories.id].toString(),
        name = row[Categories.name]
    )
    
    fun create(category: NewCategory) = transaction {
        val ID = UUID.randomUUID()
        Categories.insert {
            it[id] = ID
            it[name] = category.name
        }
        
        readById(ID.toString())!!
    }

    fun readAll(): List<Category> = transaction {
        Categories.selectAll().map { toCategory(it) }
    }

    fun readById(id: String): Category? = transaction {
        val uuid = UUID.fromString(id)
        Categories.select { Categories.id eq uuid }
            .map { toCategory(it) }
            .singleOrNull()
    }

    fun update(category: Category) = transaction {
        Categories.update({ Categories.id eq UUID.fromString(category.id?.toString()) }) {
            it[name] = category.name
        } > 0
    }

    fun delete(id: String) = transaction {
        val uuid = UUID.fromString(id)
        Categories.deleteWhere { Categories.id eq uuid } > 0
    }

    fun deleteAll() = transaction { Categories.deleteAll() }
}
