package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.HotDeal
import com.github.rodolphocouto.core.domain.HotDealId
import com.github.rodolphocouto.core.domain.Merchant
import com.github.rodolphocouto.core.domain.MerchantCategory
import java.time.LocalDateTime

data class HotDealQuery(
    val hotDealId: HotDealId,
    val merchant: MerchantQuery,
    val cashback: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)

data class MerchantQuery(
    val name: String,
    val category: MerchantCategory,
    val address: String,
    val rating: Double
)

fun HotDeal.toQuery() = HotDealQuery(
    hotDealId = this.hotDealId,
    merchant = this.merchant.toQuery(),
    cashback = this.cashback,
    startTime = this.startTime,
    endTime = this.endTime
)

fun Merchant.toQuery() = MerchantQuery(
    name = this.name,
    category = this.category,
    address = this.address,
    rating = this.rating
)