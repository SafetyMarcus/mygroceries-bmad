package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Category
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Categories : Table() {
    val id = varchar("id", 36)
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

class CategoryRepository {
    fun create(category: Category): Category {
        transaction {
            Categories.insert {
                it[id] = category.id
                it[name] = category.name
            }
        }
        return category
    }

    fun readAll(): List<Category> {
        return transaction {
            Categories.selectAll().map { toCategory(it) }
        }
    }

    fun readById(id: String): Category? {
        return transaction {
            Categories.select { Categories.id eq id }
                .mapNotNull { toCategory(it) }
                .singleOrNull()
        }
    }

    fun update(id: String, category: Category): Int {
        return transaction {
            Categories.update({ Categories.id eq id }) {
                it[name] = category.name
            }
        }
    }

    fun delete(id: String): Int {
        return transaction {
            Categories.deleteWhere { Categories.id eq id }
        }
    }

    private fun toCategory(row: ResultRow): Category = 
        Category(
            id = row[Categories.id],
            name = row[Categories.name]
        )
}
