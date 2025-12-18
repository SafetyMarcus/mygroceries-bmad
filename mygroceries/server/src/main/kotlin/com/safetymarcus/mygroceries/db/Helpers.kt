package com.safetymarcus.mygroceries.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.transactions.inTopLevelSuspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

suspend fun <T> dbQuery(block: suspend () -> T): T = withContext(Dispatchers.IO) {
    inTopLevelSuspendTransaction { block() }
}
