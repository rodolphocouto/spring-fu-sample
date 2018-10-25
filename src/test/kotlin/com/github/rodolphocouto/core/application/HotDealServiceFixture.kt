package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.HotDeal
import com.github.rodolphocouto.core.domain.HotDealId
import com.github.rodolphocouto.core.domain.Merchant
import com.github.rodolphocouto.core.domain.MerchantCategory
import java.time.LocalDate
import java.time.Month

object HotDealServiceFixture {

    val hotDeal = HotDeal(
        hotDealId = HotDealId.fromString("3985c51f-2c4a-4484-82ff-1f4a981b9348"),
        merchant = Merchant(
            name = "Coco Bambu",
            category = MerchantCategory.RESTAURANT,
            address = "Shopping Iguatemi",
            rating = 4.99
        ),
        cashback = 50,
        startTime = LocalDate.of(2018, Month.JANUARY, 1).atStartOfDay(),
        endTime = LocalDate.of(2019, Month.JANUARY, 1).atStartOfDay()
    )

    val hotDealQuery = HotDealQuery(
        hotDealId = HotDealId.fromString("3985c51f-2c4a-4484-82ff-1f4a981b9348"),
        merchant = MerchantQuery(
            name = "Coco Bambu",
            category = MerchantCategory.RESTAURANT,
            address = "Shopping Iguatemi",
            rating = 4.99
        ),
        cashback = 50,
        startTime = LocalDate.of(2018, Month.JANUARY, 1).atStartOfDay(),
        endTime = LocalDate.of(2019, Month.JANUARY, 1).atStartOfDay()
    )

    val hotDeals = listOf(hotDeal)

    val hotDealsQuery = listOf(hotDealQuery)

    val hotDealCreationCommand = HotDealCreationCommand(
        merchant = MerchantCreationCommand(
            name = "Coco Bambu",
            category = MerchantCategory.RESTAURANT,
            address = "Shopping Iguatemi",
            rating = 4.99
        ),
        cashback = 50,
        endTime = LocalDate.of(2019, Month.JANUARY, 1).atStartOfDay()
    )

    val hotDealUpdateCommand = HotDealUpdateCommand(
        hotDealId = HotDealId.fromString("3985c51f-2c4a-4484-82ff-1f4a981b9348"),
        merchant = MerchantUpdateCommand(
            name = "Coco Bambu",
            category = MerchantCategory.RESTAURANT,
            address = "Shopping Iguatemi",
            rating = 4.99
        ),
        cashback = 50,
        endTime = LocalDate.of(2019, Month.JANUARY, 1).atStartOfDay()
    )

    val hotDealRemovalCommand = HotDealRemovalCommand(
        hotDealId = HotDealId.fromString("3985c51f-2c4a-4484-82ff-1f4a981b9348")
    )
}