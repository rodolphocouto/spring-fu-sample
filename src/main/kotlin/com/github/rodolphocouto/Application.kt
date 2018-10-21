package com.github.rodolphocouto

import com.github.rodolphocouto.adapters.mongo.HotDealRepositoryMongoAdapter
import com.github.rodolphocouto.adapters.rest.hotDealRouter
import com.github.rodolphocouto.core.application.HotDealService
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.mongo.embedded
import org.springframework.fu.kofu.mongo.mongodb
import org.springframework.fu.kofu.web.jackson
import org.springframework.fu.kofu.web.server
import org.valiktor.springframework.config.ValiktorConfiguration
import org.valiktor.springframework.web.reactive.ValiktorJacksonReactiveExceptionHandler
import org.valiktor.springframework.web.reactive.ValiktorReactiveExceptionHandler

val app = application {
    beans {
        bean<HotDealService>()
        bean<HotDealRepositoryMongoAdapter>()
        bean<ValiktorConfiguration>()
        bean<ValiktorJacksonReactiveExceptionHandler>()
        bean<ValiktorReactiveExceptionHandler>()
    }

    mongodb {
        embedded()
    }

    server {
        port = 8080

        codecs {
            jackson()
        }

        import(::hotDealRouter)
    }
}

fun main(args: Array<String>) = app.run(args)