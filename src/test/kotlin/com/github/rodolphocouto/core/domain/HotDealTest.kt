package com.github.rodolphocouto.core.domain

import io.kotlintest.Matcher
import io.kotlintest.Result
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec
import org.valiktor.Constraint
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
                ex should containViolation("cashback", 2, Between(3, 100))
            }
        }
        `when`("cashback is greater than 100") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(cashback = 101) }
                ex should containViolation("cashback", 101, Between(3, 100))
            }
        }

        `when`("endTime is less than startTime") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(endTime = hotDeal.startTime.minusNanos(1L))
                }
                ex should containViolation("endTime", hotDeal.startTime.minusNanos(1L), Greater(hotDeal.startTime))
            }
        }
        `when`("endTime is equal to startTime") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(endTime = hotDeal.startTime) }
                ex should containViolation("endTime", hotDeal.startTime, Greater(hotDeal.startTime))
            }
        }

        `when`("merchant.name is empty") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(name = ""))
                }
                ex should containViolation("merchant.name", "", NotBlank)
            }
        }
        `when`("merchant.name size is greater than 50") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(name = 1.rangeTo(51).joinToString(separator = "") { "@" }))
                }
                ex should containViolation("merchant.name", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", Size(max = 50))
            }
        }

        `when`("merchant.address is empty") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(address = ""))
                }
                ex should containViolation("merchant.address", "", NotBlank)
            }
        }
        `when`("merchant.address size is greater than 100") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(address = 1.rangeTo(101).joinToString(separator = "") { "@" }))
                }
                ex should containViolation("merchant.address", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", Size(max = 100))
            }
        }

        `when`("merchant.rating is less than 0.0") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(rating = -1.0))
                }
                ex should containViolation("merchant.rating", -1.0, Between(0.0, 5.0))
            }
        }
        `when`("merchant.rating is greater than 5.0") {
            then("should throw a ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> {
                    hotDeal.copy(merchant = hotDeal.merchant.copy(rating = 5.1))
                }
                ex should containViolation("merchant.rating", 5.1, Between(0.0, 5.0))
            }
        }
    }
})

private fun containViolation(property: String, invalidValue: Any?, constraint: Constraint) = object : Matcher<ConstraintViolationException> {

    override fun test(value: ConstraintViolationException) = Result(
        passed = value.constraintViolations.size == 1 && value.constraintViolations.any { it.property == property && it.value == invalidValue && it.constraint == constraint },
        failureMessage = "ConstraintViolationException should contain 1 violation with property $property, value $invalidValue and constraint $constraint",
        negatedFailureMessage = "ConstraintViolationException should not contain 1 violation with property $property, value $invalidValue and constraint $constraint")
}