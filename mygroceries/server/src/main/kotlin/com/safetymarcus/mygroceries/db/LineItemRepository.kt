package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewLineItem
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.uuid.toJavaUuid

object LineItemRepository {
    private fun toLineItem(row: ResultRow) = LineItem(
        stringId = row[LineItems.id].toString(),
        orderId = row[LineItems.orderId].toString(),
        productId = row[LineItems.productId].toString(),
        quantity = row[LineItems.quantity],
        cost = row[LineItems.cost].toDouble()
    )

    fun create(orderId: UUID, lineItem: NewLineItem) = transaction {
        val id = UUID.randomUUID()
        LineItems.insert {
            it[LineItems.id] = id
            it[LineItems.orderId] = orderId
            it[LineItems.productId] = lineItem.productId!!.toJavaUuid()
            it[LineItems.quantity] = lineItem.quantity
            it[LineItems.cost] = lineItem.cost.toBigDecimal()
        }
        readById(id)!!
    }

    fun readAll() = transaction { 
        LineItems.selectAll().map { toLineItem(it) } 
    }

    fun readById(lineItemId: UUID) = transaction {
        LineItems.selectAll()
            .where { LineItems.id eq lineItemId }
            .map { toLineItem(it) }
            .singleOrNull()
    }

    fun findByOrderId(orderId: UUID) = transaction {
        LineItems.selectAll().where { LineItems.orderId eq orderId }.map { toLineItem(it) }
    }

    fun update(lineItem: LineItem) = transaction {
        LineItems.update({ LineItems.id eq lineItem.id!!.toJavaUuid() }) {
            it[LineItems.orderId] = lineItem.orderId!!.toJavaUuid()
            it[LineItems.productId] = lineItem.productId!!.toJavaUuid()
            it[LineItems.quantity] = lineItem.quantity
            it[LineItems.cost] = lineItem.cost.toBigDecimal()
        } > 0
    }

    fun delete(lineItemId: UUID) = transaction { 
        LineItems.deleteWhere { LineItems.id eq id } > 0 
    }
}