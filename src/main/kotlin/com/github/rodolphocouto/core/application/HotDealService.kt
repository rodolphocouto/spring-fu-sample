package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.HotDeal
import com.github.rodolphocouto.core.domain.HotDealAlreadyExistsException
import com.github.rodolphocouto.core.domain.HotDealId
import com.github.rodolphocouto.core.domain.HotDealRepository
import com.github.rodolphocouto.core.domain.Merchant
import org.springframework.stereotype.Service
import reactor.core.publisher.toMono
import java.time.LocalDateTime

@Service
class HotDealService(private val repository: HotDealRepository) {

    fun findHotDeals() = repository.findAll().map { it.toQuery() }

    fun findHotDealStream() = repository.findStream().map { it.toQuery() }

    fun findHotDealById(hotDealId: HotDealId) = repository.findById(hotDealId).map { it.toQuery() }

    fun createHotDeal(command: HotDealCreationCommand) =
        repository.findByMerchantNameAndCategory(command.merchant.name, command.merchant.category)
            .flatMap { HotDealAlreadyExistsException().toMono<HotDeal>() }
            .switchIfEmpty(
                repository.create(
                    HotDeal(
                        merchant = Merchant(
                            name = command.merchant.name,
                            category = command.merchant.category,
                            address = command.merchant.address,
                            rating = command.merchant.rating
                        ),
                        cashback = command.cashback,
                        endTime = command.endTime
                    )
                )
            )

    fun updateHotDeal(command: HotDealUpdateCommand) =
        repository.findById(command.hotDealId)
            .map {
                it.copy(
                    merchant = Merchant(
                        name = command.merchant?.name ?: it.merchant.name,
                        category = command.merchant?.category ?: it.merchant.category,
                        address = command.merchant?.address ?: it.merchant.address,
                        rating = command.merchant?.rating ?: it.merchant.rating
                    ),
                    cashback = command.cashback ?: it.cashback,
                    endTime = command.endTime ?: it.endTime
                )
            }
            .flatMap { repository.update(it) }

    fun removeHotDeal(command: HotDealRemovalCommand) =
        repository.findById(command.hotDealId)
            .map {
                it.copy(endTime = LocalDateTime.now())
            }
            .flatMap { repository.update(it) }
}