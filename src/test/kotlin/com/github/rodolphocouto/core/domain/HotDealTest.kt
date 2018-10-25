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

    Given("A valid hot deal") {
        val hotDeal = HotDealFixture.validHotDeal

        When("Cashback is less than 3") {
            val cashback = 2

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(cashback = cashback) }
                ex should containViolation("cashback", cashback, Between(3, 100))
            }
        }

        When("Cashback is greater than 100") {
            val cashback = 101

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(cashback = cashback) }
                ex should containViolation("cashback", cashback, Between(3, 100))
            }
        }

        When("End time is less than start time") {
            val endTime = hotDeal.startTime.minusNanos(1L)

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(endTime = endTime) }
                ex should containViolation("endTime", endTime, Greater(hotDeal.startTime))
            }
        }

        When("End time is equal to start time") {
            val endTime = hotDeal.startTime

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(endTime = endTime) }
                ex should containViolation("endTime", endTime, Greater(hotDeal.startTime))
            }
        }

        When("Merchant name is empty") {
            val merchantName = ""

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(merchant = hotDeal.merchant.copy(name = merchantName)) }
                ex should containViolation("merchant.name", merchantName, NotBlank)
            }
        }

        When("Merchant name size is greater than 50") {
            val merchantName = 1.rangeTo(51).joinToString(separator = "") { "@" }

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(merchant = hotDeal.merchant.copy(name = merchantName)) }
                ex should containViolation("merchant.name", merchantName, Size(max = 50))
            }
        }

        When("Merchant address is empty") {
            val merchantAddress = ""

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(merchant = hotDeal.merchant.copy(address = merchantAddress)) }
                ex should containViolation("merchant.address", merchantAddress, NotBlank)
            }
        }

        When("Merchant address size is greater than 100") {
            val merchantAddress = 1.rangeTo(101).joinToString(separator = "") { "@" }

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(merchant = hotDeal.merchant.copy(address = merchantAddress)) }
                ex should containViolation("merchant.address", merchantAddress, Size(max = 100))
            }
        }

        When("Merchant rating is less than 0.0") {
            val merchantRating = -1.0

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(merchant = hotDeal.merchant.copy(rating = merchantRating)) }
                ex should containViolation("merchant.rating", merchantRating, Between(0.0, 5.0))
            }
        }

        When("Merchant rating is greater than 5.0") {
            val merchantRating = 5.1

            Then("Should throw ConstraintViolationException") {
                val ex = assertFailsWith<ConstraintViolationException> { hotDeal.copy(merchant = hotDeal.merchant.copy(rating = merchantRating)) }
                ex should containViolation("merchant.rating", merchantRating, Between(0.0, 5.0))
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