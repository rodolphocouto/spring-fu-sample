package com.github.rodolphocouto

import com.github.rodolphocouto.adapters.r2dbc.UserRepositoryR2dbc
import com.github.rodolphocouto.adapters.webflux.UserHandler
import com.github.rodolphocouto.adapters.webflux.userRouter
import com.github.rodolphocouto.core.application.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.boot.WebApplicationType.REACTIVE
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.logging.LogLevel.INFO
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.r2dbc.r2dbcH2
import org.springframework.fu.kofu.webflux.webFlux
import org.valiktor.springframework.config.ValiktorConfiguration
import org.valiktor.springframework.web.reactive.ReactiveConstraintViolationExceptionHandler
import org.valiktor.springframework.web.reactive.ReactiveInvalidFormatExceptionHandler
import org.valiktor.springframework.web.reactive.ReactiveMissingKotlinParameterExceptionHandler

const val PORT = 8080

val app = application(REACTIVE) {
    beans {
        // user beans
        bean(::userRouter)
        bean<UserHandler>()
        bean<UserService>()
        bean<UserRepositoryR2dbc>()

        // valiktor beans
        bean<ValiktorConfiguration>()
        bean<ReactiveInvalidFormatExceptionHandler>()
        bean<ReactiveMissingKotlinParameterExceptionHandler>()
        bean<ReactiveConstraintViolationExceptionHandler>()
    }

    listener<ApplicationReadyEvent> {
        runBlocking {
            ref<UserRepositoryR2dbc>().init()
        }
    }

    logging {
        level = INFO
    }

    r2dbcH2()

    webFlux {
        port = PORT

        codecs {
            string()
            jackson()
        }
    }
}

fun main(args: Array<String>) {
    app.run(args)
}
