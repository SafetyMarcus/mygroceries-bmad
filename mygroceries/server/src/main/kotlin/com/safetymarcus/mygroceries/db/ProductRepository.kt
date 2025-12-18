package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Product
import com.safetymarcus.mygroceries.db.Categories
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.uuid.toJavaUuid

object ProductRepository {
    private fun toProduct(row: ResultRow) = Product(
        stringId = row[Products.id].toString(),
        name = row[Products.name],
        categoryId = row[Products.categoryId].toString()
    )

    fun create(name: String, categoryId: UUID) = transaction {
        val id = UUID.randomUUID()
        Products.insert {
            it[Products.id] = id
            it[Products.name] = name
            it[Products.categoryId] = categoryId
        }
        
        readById(id)!!
    }

    fun readAll() = transaction { Products.selectAll().map { toProduct(it) } }

    fun readById(id: UUID): Product? = transaction {
        Products.selectAll()
            .where { Products.id eq id }
            .map { toProduct(it) }
            .singleOrNull()
    }

    fun update(product: Product) = transaction {
        Products.update({ Products.id eq product.id!!.toJavaUuid() }) {
            it[name] = product.name
            it[categoryId] = UUID.fromString(product.categoryId.toString())
        } > 0
    }

    fun delete(id: UUID) = transaction { Products.deleteWhere { Products.id eq id } > 0 }

    fun deleteAll() = transaction { Products.deleteAll() }
}