package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewLineItem
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.core.*
import java.util.*
import kotlin.uuid.toJavaUuid

object LineItemRepository {
    private fun toLineItem(row: ResultRow) = LineItem(
        stringId = row[LineItems.id].toString(),
        orderId = row[LineItems.orderId].toString(),
        productId = row[LineItems.productId].toString(),
        quantity = row[LineItems.quantity],
        cost = row[LineItems.cost]
    )

    suspend fun create(orderId: UUID, lineItem: NewLineItem) = dbQuery {
        val id = UUID.randomUUID()
        LineItems.insert {
            it[LineItems.id] = id
            it[LineItems.orderId] = orderId
            it[LineItems.productId] = lineItem.productId!!.toJavaUuid()
            it[LineItems.quantity] = lineItem.quantity
            it[LineItems.cost] = lineItem.cost
        }
        readById(id)!!
    }

    suspend fun readAll() = dbQuery { 
        LineItems.selectAll().map { toLineItem(it) } 
    }

    suspend fun readById(lineItemId: UUID) = dbQuery {
        LineItems.selectAll()
            .where { LineItems.id eq lineItemId }
            .map { toLineItem(it) }
            .singleOrNull()
    }

    suspend fun findByOrderId(orderId: UUID) = dbQuery {
        LineItems.selectAll().where { LineItems.orderId eq orderId }.map { toLineItem(it) }
    }

    suspend fun update(lineItem: LineItem) = dbQuery {
        LineItems.update({ LineItems.id eq lineItem.id!!.toJavaUuid() }) {
            it[LineItems.orderId] = lineItem.orderId!!.toJavaUuid()
            it[LineItems.productId] = lineItem.productId!!.toJavaUuid()
            it[LineItems.quantity] = lineItem.quantity
            it[LineItems.cost] = lineItem.cost
        } > 0
    }

    suspend fun delete(lineItemId: UUID) = dbQuery { 
        LineItems.deleteWhere { LineItems.id eq id } > 0 
    }

    suspend fun deleteAll() = dbQuery { LineItems.deleteAll() }
}