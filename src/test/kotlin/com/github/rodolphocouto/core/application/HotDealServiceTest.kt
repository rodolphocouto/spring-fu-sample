package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.HotDealAlreadyExistsException
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.specs.BehaviorSpec
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.test.StepVerifier
import reactor.test.expectError

class HotDealServiceTest : BehaviorSpec({

    given("a hot deal creation command") {
        val command = HotDealServiceFixture.hotDealCreationCommand

        `when`("hot deal does not exist") {
            val service = HotDealService(mock {
                on { findByMerchantNameAndCategory(any(), any()) } doReturn Mono.empty()
                on { create(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            then("should create hot deal and return it's query") {
                StepVerifier.create(service.createHotDeal(command))
                    .expectNext(HotDealServiceFixture.hotDealQuery)
                    .expectComplete()
                    .verify()
            }
        }

        `when`("hot deal exists") {
            val service = HotDealService(mock {
                on { findByMerchantNameAndCategory(any(), any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
                on { create(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            then("should not create hot deal and throw HotDealAlreadyExistsException") {
                StepVerifier.create(service.createHotDeal(command))
                    .expectError<HotDealAlreadyExistsException>()
                    .verify()
            }
        }
    }

    given("a hot deal update command") {
        val command = HotDealServiceFixture.hotDealUpdateCommand

        `when`("hot deal exists") {
            val service = HotDealService(mock {
                on { findById(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
                on { update(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            then("should update hot deal and return it's query") {
                StepVerifier.create(service.updateHotDeal(command))
                    .expectNext(HotDealServiceFixture.hotDealQuery)
                    .expectComplete()
                    .verify()
            }
        }

        `when`("hot deal does not exist") {
            val service = HotDealService(mock {
                on { findById(any()) } doReturn Mono.empty()
                on { update(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            then("should return a empty Mono") {
                StepVerifier.create(service.updateHotDeal(command))
                    .expectComplete()
                    .verify()
            }
        }
    }

    given("a hot deal removal command") {
        val command = HotDealServiceFixture.hotDealRemovalCommand

        `when`("hot deal exists") {
            val service = HotDealService(mock {
                on { findById(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
                on { update(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            then("should remove hot deal and return it's query") {
                StepVerifier.create(service.removeHotDeal(command))
                    .expectNext(HotDealServiceFixture.hotDealQuery)
                    .expectComplete()
                    .verify()
            }
        }

        `when`("hot deal does not exist") {
            val service = HotDealService(mock {
                on { findById(any()) } doReturn Mono.empty()
                on { update(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            then("should return a empty Mono") {
                StepVerifier.create(service.removeHotDeal(command))
                    .expectComplete()
                    .verify()
            }
        }
    }
})