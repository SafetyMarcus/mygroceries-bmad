package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.service.SpendingService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.spendingRoutes(spendingService: SpendingService) {
    route("/spending") {
        get("/categories") {
            val spending = spendingService.getCategorySpending()
            call.respond(spending)
        }
    }
}
