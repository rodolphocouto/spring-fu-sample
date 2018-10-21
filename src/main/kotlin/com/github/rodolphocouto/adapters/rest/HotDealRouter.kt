package com.github.rodolphocouto.adapters.rest

import com.github.rodolphocouto.core.application.HotDealCreationCommand
import com.github.rodolphocouto.core.application.HotDealQuery
import com.github.rodolphocouto.core.application.HotDealRemovalCommand
import com.github.rodolphocouto.core.application.HotDealService
import com.github.rodolphocouto.core.application.HotDealUpdateCommand
import com.github.rodolphocouto.core.domain.HotDealAlreadyExistsException
import com.github.rodolphocouto.core.domain.HotDealId
import com.github.rodolphocouto.core.domain.HotDealNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.json
import org.springframework.web.reactive.function.server.router

fun hotDealRouter(service: HotDealService) = router {
    (accept(MediaType.APPLICATION_JSON) and "/hot-deals").nest {

        GET("/") {
            ok().json().body<HotDealQuery>(service.findHotDeals())
        }

        GET("/stream") {
            ok().jsonStream().body<HotDealQuery>(service.findHotDealStream())
        }

        POST("/") { req ->
            req.bodyToMono<HotDealCreationCommand>()
                .flatMap { service.createHotDeal(it) }
                .flatMap { created(req.uriBuilder().path("/{id}").build(it.hotDealId)).json().build() }
                .onErrorResume(HotDealAlreadyExistsException::class.java) { conflict().build() }
        }

        "/{id}".nest {

            GET("/") { req ->
                ok().json()
                    .body<HotDealQuery>(service.findHotDealById(HotDealId.fromString(req.pathVariable("id"))))
                    .onErrorResume(HotDealNotFoundException::class.java) { notFound().build() }
            }

            PATCH("/") { req ->
                req.bodyToMono<HotDealUpdateCommand>()
                    .flatMap { service.updateHotDeal(it) }
                    .flatMap { noContent().build() }
                    .onErrorResume(HotDealNotFoundException::class.java) { notFound().build() }
            }

            DELETE("/") { req ->
                req.bodyToMono<HotDealRemovalCommand>()
                    .flatMap { service.removeHotDeal(it) }
                    .flatMap { noContent().build() }
                    .onErrorResume(HotDealNotFoundException::class.java) { notFound().build() }
            }
        }
    }
}

private fun conflict() = status(HttpStatus.CONFLICT)
private fun ServerResponse.BodyBuilder.jsonStream() = contentType(MediaType.APPLICATION_STREAM_JSON)