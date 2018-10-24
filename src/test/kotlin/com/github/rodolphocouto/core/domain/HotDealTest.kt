package com.github.rodolphocouto.core.domain

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import org.valiktor.ConstraintViolationException
import org.valiktor.constraints.Between
import org.valiktor.constraints.Greater
import org.valiktor.constraints.NotBlank
import org.valiktor.constraints.Size
import kotlin.test.assertFailsWith

class HotDealTest : BehaviorSpec({
    given("a valid hotDeal") {
        val hotDeal = HotDealFixture.validHotDeal

        `when`("cashback is less than 3") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(cashback = 2) }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "cashback"
                    this.value shouldBe 2
                    this.constraint shouldBe Between(3, 100)
                }
            }
        }
        `when`("cashback is greater than 100") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(cashback = 101) }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "cashback"
                    this.value shouldBe 101
                    this.constraint shouldBe Between(3, 100)
                }
            }
        }

        `when`("endTime is less than startTime") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(endTime = hotDeal.startTime.minusNanos(1L))
                }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "endTime"
                    this.value shouldBe hotDeal.startTime.minusNanos(1L)
                    this.constraint shouldBe Greater(hotDeal.startTime)
                }
            }
        }
        `when`("endTime is equal to startTime") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(endTime = hotDeal.startTime)
                }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "endTime"
                    this.value shouldBe hotDeal.startTime
                    this.constraint shouldBe Greater(hotDeal.startTime)
                }
            }
        }

        `when`("merchant.name is empty") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(name = ""))
                }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "merchant.name"
                    this.value shouldBe ""
                    this.constraint shouldBe NotBlank
                }
            }
        }
        `when`("merchant.name size is greater than 50") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(name = 1.rangeTo(51).joinToString(separator = "") { "@" }))
                }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "merchant.name"
                    this.value shouldBe "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                    this.constraint shouldBe Size(max = 50)
                }
            }
        }

        `when`("merchant.address is empty") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(address = ""))
                }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "merchant.address"
                    this.value shouldBe ""
                    this.constraint shouldBe NotBlank
                }
            }
        }
        `when`("merchant.address size is greater than 100") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(address = 1.rangeTo(101).joinToString(separator = "") { "@" }))
                }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "merchant.address"
                    this.value shouldBe "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                    this.constraint shouldBe Size(max = 100)
                }
            }
        }

        `when`("merchant.rating is less than 0.0") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(rating = -1.0))
                }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "merchant.rating"
                    this.value shouldBe -1.0
                    this.constraint shouldBe Between(0.0, 5.0)
                }
            }
        }
        `when`("merchant.rating is greater than 5.0") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(rating = 5.1))
                }
                ex.constraintViolations.size shouldBe 1
                with(ex.constraintViolations.first()) {
                    this.property shouldBe "merchant.rating"
                    this.value shouldBe 5.1
                    this.constraint shouldBe Between(0.0, 5.0)
                }
            }
        }
    }
})