package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.model.NewOrder
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.uuid.toJavaUuid
import kotlinx.datetime.*

object OrderRepository {
    private fun toOrder(row: ResultRow) = Order(
        stringId = row[Orders.id].toString(),
        date = row[Orders.date].toKotlinInstant(),
    )

    fun create(order: NewOrder) = transaction {
        val id = UUID.randomUUID()
        Orders.insert {
            it[Orders.id] = id
            it[Orders.date] = order.date
        }
        readById(id)
    }

    fun readAll() = transaction { 
        Orders.selectAll().map { toOrder(it) } 
    }

    fun readById(id: UUID) = transaction {
        Orders.selectAll()
            .where { Orders.id eq id }
            .map { toOrder(it) }
            .singleOrNull()
    }

    fun update(order: Order) = transaction {
        Orders.update({ Orders.id eq order.id!!.toJavaUuid() }) {
            it[Orders.date] = order.date
        } > 0
    }

    fun delete(id: UUID) = transaction { 
        Orders.deleteWhere { Orders.id eq id } > 0 
    }

    fun deleteAll() = transaction { 
        Orders.deleteAll() 
    }
}