package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Product
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Products : Table() {
    val id = uuid("id")
    val name = varchar("name", 255)
    val categoryId = uuid("category_id")
    
    override val primaryKey = PrimaryKey(id)
    
    init {
        foreignKey("fk_products_categories", categoryId, Categories.id)
    }
}

object ProductRepository {
    private fun toProduct(row: ResultRow): Product =
        Product(
            id = row[Products.id].toString(),
            name = row[Products.name],
            categoryId = row[Products.categoryId].toString()
        )

    fun create(name: String, categoryId: UUID): Product = transaction {
        val id = UUID.randomUUID()
        Products.insert {
            it[Products.id] = id
            it[Products.name] = name
            it[Products.categoryId] = categoryId
        }
        
        readById(id)!!
    }

    fun readAll(): List<Product> = transaction {
        Products.selectAll().map { toProduct(it) }
    }

    fun readById(id: UUID): Product? = transaction {
        Products.select { Products.id eq id }
            .map { toProduct(it) }
            .singleOrNull()
    }

    fun update(product: Product): Boolean = transaction {
        Products.update({ Products.id eq UUID.fromString(product.id) }) {
            it[name] = product.name
            it[categoryId] = UUID.fromString(product.categoryId)
        } > 0
    }

    fun delete(id: UUID): Boolean = transaction {
        Products.deleteWhere { Products.id eq id } > 0
    }

    fun deleteAll() = transaction {
        Products.deleteAll()
    }

    fun existsByCategoryId(categoryId: UUID): Boolean = transaction {
        Products.select { Products.categoryId eq categoryId }
            .limit(1)
            .count() > 0
    }
}
