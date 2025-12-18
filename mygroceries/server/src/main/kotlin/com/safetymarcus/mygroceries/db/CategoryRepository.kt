package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.core.*
import java.util.*

object CategoryRepository {
    private fun toCategory(row: ResultRow) = Category(
        stringId = row[Categories.id].toString(),
        name = row[Categories.name]
    )
    
    suspend fun create(category: NewCategory) = dbQuery {
        val id = UUID.randomUUID()
        Categories.insert {
            it[Categories.id] = id
            it[Categories.name] = category.name
        }
        
        readById(id.toString())!!
    }

    suspend fun readAll() = dbQuery { Categories.selectAll().map { toCategory(it) } }

    suspend fun readById(id: String) = dbQuery {
        val uuid = UUID.fromString(id)
        Categories.selectAll()
            .where { Categories.id eq uuid }
            .map { toCategory(it) }
            .singleOrNull()
    }

    suspend fun update(category: Category) = dbQuery {
        Categories.update({ Categories.id eq UUID.fromString(category.id?.toString()) }) {
            it[Categories.name] = category.name
        } > 0
    }

    suspend fun delete(id: String) = dbQuery {
        val uuid = UUID.fromString(id)
        Categories.deleteWhere { Categories.id eq uuid } > 0
    }

    suspend fun deleteAll() = dbQuery { Categories.deleteAll() }
}
