package com.github.rodolphocouto.adapters.webflux

import com.github.rodolphocouto.core.application.CreateUserCommand
import com.github.rodolphocouto.core.application.RemoveUserCommand
import com.github.rodolphocouto.core.application.UpdateUserCommand
import com.github.rodolphocouto.core.application.UserService
import com.github.rodolphocouto.core.domain.UserAlreadyExistsException
import com.github.rodolphocouto.core.domain.UserId
import com.github.rodolphocouto.core.domain.UserNotFoundException
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

class UserHandler(private val service: UserService) {

    suspend fun findAll(req: ServerRequest): ServerResponse = ok().bodyAndAwait(service.findAll())

    suspend fun findById(req: ServerRequest): ServerResponse {
        val id = UserId.fromString(req.pathVariable("id"))

        return try {
            ok().bodyValueAndAwait(service.findById(id))
        } catch (ex: UserNotFoundException) {
            notFound().buildAndAwait()
        }
    }

    suspend fun create(req: ServerRequest): ServerResponse {
        val command = req.awaitBody<CreateUserCommand>()

        return try {
            val id = service.create(command)
            created(req.uriBuilder().path("/{id}").build(id)).buildAndAwait()
        } catch (ex: UserAlreadyExistsException) {
            status(CONFLICT).bodyValueAndAwait(service.findById(ex.id))
        }
    }

    suspend fun update(req: ServerRequest): ServerResponse {
        val command = req.awaitBody<UpdateUserCommand>()

        return try {
            service.update(command)
            noContent().buildAndAwait()
        } catch (ex: UserNotFoundException) {
            notFound().buildAndAwait()
        }
    }

    suspend fun remove(req: ServerRequest): ServerResponse {
        val command = RemoveUserCommand(UserId.fromString(req.pathVariable("id")))

        return try {
            service.remove(command)
            noContent().buildAndAwait()
        } catch (ex: UserNotFoundException) {
            notFound().buildAndAwait()
        }
    }
}
