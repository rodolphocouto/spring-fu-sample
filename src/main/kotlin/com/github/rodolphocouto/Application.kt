package com.github.rodolphocouto

import com.github.rodolphocouto.adapters.mongo.HotDealRepositoryMongoAdapter
import com.github.rodolphocouto.adapters.rest.hotDealRouter
import com.github.rodolphocouto.core.application.HotDealService
import org.springframework.boot.logging.LogLevel
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.mongo.mongodb
import org.springframework.fu.kofu.web.jackson
import org.springframework.fu.kofu.web.server
import org.valiktor.springframework.config.ValiktorConfiguration
import org.valiktor.springframework.web.reactive.ValiktorJacksonReactiveExceptionHandler
import org.valiktor.springframework.web.reactive.ValiktorReactiveExceptionHandler

private const val PORT = 8080

val app = application {
    beans {
        bean<HotDealService>()
        bean<HotDealRepositoryMongoAdapter>()
        bean<ValiktorConfiguration>()
        bean<ValiktorJacksonReactiveExceptionHandler>()
        bean<ValiktorReactiveExceptionHandler>()
    }

    logging {
        level = LogLevel.INFO
    }

    mongodb {
        uri = "mongodb:27017//localhost/test"
    }

    server {
        port = PORT

        codecs {
            jackson()
        }

        import(::hotDealRouter)
    }
}

fun main(args: Array<String>) = app.run(args)