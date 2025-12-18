package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewLineItem
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object LineItemRepository {
    private fun toLineItem(row: ResultRow) = LineItem(
        id = row[LineItems.id],
        orderId = row[LineItems.orderId],
        productId = row[LineItems.productId],
        quantity = row[LineItems.quantity],
        cost = row[LineItems.cost].toDouble()
    )

    fun create(lineItem: NewLineItem) = transaction {
        val id = UUID.randomUUID()
        LineItems.insert {
            it[id] = id
            it[orderId] = lineItem.orderId
            it[productId] = lineItem.productId
            it[quantity] = lineItem.quantity
            it[cost] = lineItem.cost.toBigDecimal()
        }
        readById(id.toString())
    }

    fun readAll() = transaction { 
        LineItems.selectAll().map { toLineItem(it) } 
    }

    fun readById(lineItemId: String) = transaction {
        val id = UUID.fromString(lineItemId)
        LineItems.select { LineItems.id eq id }
            .map { toLineItem(it) }
            .singleOrNull()
    }

    fun findByOrderId(orderId: String) = transaction {
        val id = UUID.fromString(orderId)
        LineItems.select { LineItems.orderId eq id }.map { toLineItem(it) }
    }

    fun update(lineItem: LineItem) = transaction {
        LineItems.update({ LineItems.id eq lineItem.id }) {
            it[orderId] = lineItem.orderId
            it[productId] = lineItem.productId
            it[quantity] = lineItem.quantity
            it[cost] = lineItem.cost.toBigDecimal()
        } > 0
    }

    fun delete(lineItemId: String) = transaction { 
        val id = UUID.fromString(lineItemId)
        LineItems.deleteWhere { LineItems.id eq id } > 0 
    }
}