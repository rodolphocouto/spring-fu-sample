package com.github.rodolphocouto.core.domain

import java.time.LocalDateTime

object HotDealFixture {

    val validHotDeal = HotDeal(
        hotDealId = HotDealId.randomUUID(),
        merchant = Merchant(
            name = "Coco Bambu",
            category = MerchantCategory.RESTAURANT,
            address = "Shopping Iguatemi",
            rating = 4.99
        ),
        cashback = 50,
        startTime = LocalDateTime.now(),
        endTime = LocalDateTime.now().plusDays(1L)
    )
}