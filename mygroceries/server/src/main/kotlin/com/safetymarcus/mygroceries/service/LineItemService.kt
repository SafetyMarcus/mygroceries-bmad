package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.LineItemRepository
import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewLineItem
import kotlin.uuid.*

class LineItemService(
    private val lineItemRepository: LineItemRepository,
) {
    suspend fun createLineItem(lineItem: NewLineItem) = lineItemRepository.create(lineItem)
    
    suspend fun getLineItem(id: Uuid) = lineItemRepository.readById(id.toString())
    
    suspend fun getLineItemsByOrder(orderId: Uuid) = lineItemRepository.findByOrderId(orderId.toString())
    
    suspend fun updateLineItem(lineItem: LineItem) = lineItemRepository
        .update(lineItem)
        .takeIf { it }
        ?.let { lineItemRepository.readById(lineItem.id.toString()!!) }
    
    suspend fun deleteLineItem(id: String) = lineItemRepository.delete(id)
}
