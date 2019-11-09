package com.github.rodolphocouto.adapters.webflux

import org.springframework.web.reactive.function.server.coRouter

private const val UUID_REGEX = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}"

fun userRouter(handler: UserHandler) = coRouter {
    "/users".nest {
        GET("", handler::findAll)
        POST("", handler::create)
        GET("/{id:$UUID_REGEX}", handler::findById)
        PUT("/{id:$UUID_REGEX}", handler::update)
        DELETE("/{id:$UUID_REGEX}", handler::remove)
    }
}
