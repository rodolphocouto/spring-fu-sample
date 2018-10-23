package com.github.rodolphocouto.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.valiktor.functions.hasDecimalDigits
import org.valiktor.functions.hasSize
import org.valiktor.functions.isBetween
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.validate
import org.valiktor.validate
import java.time.LocalDateTime
import java.util.UUID

typealias HotDealId = UUID

data class HotDealAlreadyExistsException(val hotDealId: HotDealId) : Exception()

@Document(collection = "hotDeals")
data class HotDeal(
    @Id val hotDealId: HotDealId,
    val merchant: Merchant,
    val cashback: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
) {
    constructor(merchant: Merchant, cashback: Int, endTime: LocalDateTime)
        : this(HotDealId.randomUUID(), merchant, cashback, LocalDateTime.now(), endTime)

    init {
        validate(this) {
            validate(HotDeal::cashback).isBetween(1, 100)
            validate(HotDeal::endTime).isGreaterThan(this@HotDeal.startTime)
            validate(HotDeal::merchant).validate {
                validate(Merchant::name).isNotBlank().hasSize(max = 50)
                validate(Merchant::address).isNotBlank().hasSize(max = 100)
                validate(Merchant::rating).isBetween(0.0, 5.0).hasDecimalDigits(max = 2)
            }
        }
    }
}

enum class MerchantCategory { RESTAURANT, FUEL, HEALTH }

data class Merchant(
    val name: String,
    val category: MerchantCategory,
    val address: String,
    val rating: Double
)