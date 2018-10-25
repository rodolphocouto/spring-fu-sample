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

    Given("A hot deal creation command") {
        val command = HotDealServiceFixture.hotDealCreationCommand

        When("Hot deal does not exist") {
            val service = HotDealService(mock {
                on { findByMerchantNameAndCategory(any(), any()) } doReturn Mono.empty()
                on { create(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            Then("Should create hot deal and return it's query") {
                StepVerifier.create(service.createHotDeal(command))
                    .expectNext(HotDealServiceFixture.hotDealQuery)
                    .expectComplete()
                    .verify()
            }
        }

        When("Hot deal exists") {
            val service = HotDealService(mock {
                on { findByMerchantNameAndCategory(any(), any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
                on { create(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            Then("Should throw HotDealAlreadyExistsException") {
                StepVerifier.create(service.createHotDeal(command))
                    .expectError<HotDealAlreadyExistsException>()
                    .verify()
            }
        }
    }

    Given("A hot deal update command") {
        val command = HotDealServiceFixture.hotDealUpdateCommand

        When("Hot deal exists") {
            val service = HotDealService(mock {
                on { findById(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
                on { update(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            Then("Should update hot deal and return it's query") {
                StepVerifier.create(service.updateHotDeal(command))
                    .expectNext(HotDealServiceFixture.hotDealQuery)
                    .expectComplete()
                    .verify()
            }
        }

        When("Hot deal does not exist") {
            val service = HotDealService(mock {
                on { findById(any()) } doReturn Mono.empty()
                on { update(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            Then("Should return a empty Mono") {
                StepVerifier.create(service.updateHotDeal(command))
                    .expectComplete()
                    .verify()
            }
        }
    }

    Given("A hot deal removal command") {
        val command = HotDealServiceFixture.hotDealRemovalCommand

        When("Hot deal exists") {
            val service = HotDealService(mock {
                on { findById(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
                on { update(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            Then("Should remove hot deal and return it's query") {
                StepVerifier.create(service.removeHotDeal(command))
                    .expectNext(HotDealServiceFixture.hotDealQuery)
                    .expectComplete()
                    .verify()
            }
        }

        When("Hot deal does not exist") {
            val service = HotDealService(mock {
                on { findById(any()) } doReturn Mono.empty()
                on { update(any()) } doReturn HotDealServiceFixture.hotDeal.toMono()
            })

            Then("Should return a empty Mono") {
                StepVerifier.create(service.removeHotDeal(command))
                    .expectComplete()
                    .verify()
            }
        }
    }
})