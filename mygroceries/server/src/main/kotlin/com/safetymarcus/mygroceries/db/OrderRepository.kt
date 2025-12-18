package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Order
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object OrderRepository {
    private fun toOrder(row: ResultRow) = Order(
        id = row[Orders.id],
        orderDate = row[Orders.orderDate],
    )

    fun create(order: NewOrder) = transaction {
        val id = UUID.randomUUID()
        Orders.insert {
            it[id] = id
            it[orderDate] = order.orderDate
        }
        readById(id)
    }

    fun readAll() = transaction { 
        Orders.selectAll().map { toOrder(it) } 
    }

    fun readById(id: String) = transaction {
        Orders.select { Orders.id eq id }
            .map { toOrder(it) }
            .singleOrNull()
    }

    fun update(order: Order) = transaction {
        Orders.update({ Orders.id eq order.id }) {
            it[orderDate] = order.orderDate
        } > 0
    }

    fun delete(id: String) = transaction { 
        Orders.deleteWhere { Orders.id eq id } > 0 
    }

    fun deleteAll() = transaction { 
        Orders.deleteAll() 
    }
}