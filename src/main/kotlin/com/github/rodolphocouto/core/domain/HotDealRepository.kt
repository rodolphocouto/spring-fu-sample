package com.github.rodolphocouto.core.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface HotDealRepository {

    fun findAll(): Flux<HotDeal>

    fun findStream(): Flux<HotDeal>

    fun findById(hotDealId: HotDealId): Mono<HotDeal>

    fun findByMerchantNameAndCategory(merchantName: String, merchantCategory: MerchantCategory): Mono<HotDeal>

    fun create(hotDeal: HotDeal): Mono<HotDeal>

    fun update(hotDeal: HotDeal): Mono<HotDeal>
}