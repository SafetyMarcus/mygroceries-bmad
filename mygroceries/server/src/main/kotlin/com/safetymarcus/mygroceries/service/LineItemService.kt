package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.LineItemRepository
import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewLineItem
import kotlin.uuid.*

class LineItemService(
    private val lineItemRepository: LineItemRepository,
) {
    suspend fun createLineItem(orderId: Uuid, lineItem: NewLineItem) = lineItemRepository.create(orderId.toJavaUuid(), lineItem)
    
    suspend fun getLineItem(id: Uuid) = lineItemRepository.readById(id.toJavaUuid())
    
    suspend fun getLineItemsByOrder(orderId: Uuid) = lineItemRepository.findByOrderId(orderId.toJavaUuid())
    
    suspend fun updateLineItem(lineItem: LineItem) = lineItemRepository
        .update(lineItem)
        .takeIf { it }
        ?.let { lineItemRepository.readById(lineItem.id!!.toJavaUuid()) }
    
    suspend fun deleteLineItem(id: Uuid) = lineItemRepository.delete(id.toJavaUuid())
}
