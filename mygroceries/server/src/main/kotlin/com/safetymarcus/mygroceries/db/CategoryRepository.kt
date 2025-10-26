package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import java.util.UUID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Categories : Table() {
    val id = uuid("id")
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

object CategoryRepository {
    fun create(category: NewCategory) = transaction {
        val ID = UUID.randomUUID()
        Categories.insert {
            it[id] = ID
            it[name] = category.name
        }
        
        readById(ID)!!
    }

    fun readAll(): List<Category> = transaction {
        Categories.selectAll().map { toCategory(it) }
    }

    fun readById(id: UUID): Category? = transaction {
        Categories.select { Categories.id eq id }
            .map { toCategory(it) }
            .singleOrNull()
    }

    fun update(category: Category) = transaction {
        Categories.update({ Categories.id eq category.id!! }) {
            it[name] = category.name
        }
    }

    fun delete(id: UUID) = transaction {
        Categories.deleteWhere { Categories.id eq id }
    }

    fun deleteAll() = transaction {
        Categories.deleteAll()
    }

    private fun toCategory(row: ResultRow) = Category(
        id = row[Categories.id],
        name = row[Categories.name]
    )
}
