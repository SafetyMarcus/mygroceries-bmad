package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Product
import com.safetymarcus.mygroceries.db.Categories
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.core.*
import java.util.UUID
import kotlin.uuid.toJavaUuid

object ProductRepository {
    private fun toProduct(row: ResultRow) = Product(
        stringId = row[Products.id].toString(),
        name = row[Products.name],
        categoryId = row[Products.categoryId].toString()
    )

    suspend fun create(name: String, categoryId: UUID) = dbQuery {
        val id = UUID.randomUUID()
        Products.insert {
            it[Products.id] = id
            it[Products.name] = name
            it[Products.categoryId] = categoryId
        }
        
        readById(id)!!
    }

    suspend fun readAll() = dbQuery { Products.selectAll().map { toProduct(it) } }

    suspend fun readById(id: UUID): Product? = dbQuery {
        Products.selectAll()
            .where { Products.id eq id }
            .map { toProduct(it) }
            .singleOrNull()
    }

    suspend fun update(product: Product) = dbQuery {
        Products.update({ Products.id eq product.id!!.toJavaUuid() }) {
            it[Products.name] = product.name
            it[Products.categoryId] = UUID.fromString(product.categoryId.toString())
        } > 0
    }

    suspend fun delete(id: UUID) = dbQuery { Products.deleteWhere { Products.id eq id } > 0 }

    suspend fun deleteAll() = dbQuery { Products.deleteAll() }
}