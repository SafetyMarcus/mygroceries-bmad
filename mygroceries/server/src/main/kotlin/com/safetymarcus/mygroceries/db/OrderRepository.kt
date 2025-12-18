package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.model.NewOrder
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.core.*
import java.util.*
import kotlin.uuid.toJavaUuid

object OrderRepository {
    private fun toOrder(row: ResultRow) = Order(
        stringId = row[Orders.id].toString(),
        date = row[Orders.date],
    )

    suspend fun create(order: NewOrder) = dbQuery {
        val id = UUID.randomUUID()
        Orders.insert {
            it[Orders.id] = id
            it[Orders.date] = order.date
        }
        readById(id)
    }

    suspend fun readAll() = dbQuery { Orders.selectAll().map { toOrder(it) } }

    suspend fun readById(id: UUID) = dbQuery {
        Orders.selectAll()
            .where { Orders.id eq id }
            .map { toOrder(it) }
            .singleOrNull()
    }

    suspend fun update(order: Order) = dbQuery {
        Orders.update({ Orders.id eq order.id!!.toJavaUuid() }) {
            it[Orders.date] = order.date
        } > 0
    }

    suspend fun delete(id: UUID) = dbQuery { Orders.deleteWhere { Orders.id eq id } > 0 }

    suspend fun deleteAll() = dbQuery { Orders.deleteAll() }
}