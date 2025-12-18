package com.safetymarcus.mygroceries.db

import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.datetime.timestamp
import java.util.UUID

object Categories : Table() {
    val id = uuid("id")
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

object Products : Table() {
    val id = uuid("id")
    val name = varchar("name", 255)
    val categoryId = optReference("category_id", Categories.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(id)
}

object Orders : Table("orders") {
    val id = uuid("id")
    val date = timestamp("date")
    
    override val primaryKey = PrimaryKey(id)
}

object LineItems : Table("line_items") {
    val id = uuid("id").autoGenerate()
    val orderId = reference("order_id", Orders.id, onDelete = ReferenceOption.CASCADE)
    val productId = reference("product_id", Products.id) //Intentionally no cascading deletes as orders should have immutable line item references
    val quantity = double("quantity")
    val cost = decimal("cost", 10, 2)
    
    override val primaryKey = PrimaryKey(id)
}
