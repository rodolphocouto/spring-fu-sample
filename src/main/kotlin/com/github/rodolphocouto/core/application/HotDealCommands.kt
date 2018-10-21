package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.HotDealId
import com.github.rodolphocouto.core.domain.MerchantCategory
import java.time.LocalDateTime

data class HotDealCreationCommand(
    val merchant: MerchantCreationCommand,
    val cashback: Int,
    val endTime: LocalDateTime
)

data class MerchantCreationCommand(
    val name: String,
    val category: MerchantCategory,
    val address: String,
    val rating: Double
)

data class HotDealUpdateCommand(
    val hotDealId: HotDealId,
    val merchant: MerchantUpdateCommand?,
    val cashback: Int?,
    val endTime: LocalDateTime?
)

data class MerchantUpdateCommand(
    val name: String?,
    val category: MerchantCategory?,
    val address: String?,
    val rating: Double?
)

data class HotDealRemovalCommand(
    val hotDealId: HotDealId
)