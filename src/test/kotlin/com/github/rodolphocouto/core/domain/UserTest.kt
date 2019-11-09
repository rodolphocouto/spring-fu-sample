package com.github.rodolphocouto.core.domain

import com.github.rodolphocouto.core.domain.UserTestFixture.user
import io.kotlintest.Spec
import io.kotlintest.specs.DescribeSpec
import io.mockk.clearStaticMockk
import io.mockk.every
import io.mockk.mockkStatic
import org.valiktor.constraints.Email
import org.valiktor.constraints.NotBlank
import org.valiktor.constraints.Size
import org.valiktor.test.shouldFailValidation

class UserTest : DescribeSpec() {

    override fun beforeSpec(spec: Spec) {
        val userId = UserId.randomUUID()
        mockkStatic(UserId::class)
        every { UserId.randomUUID() } returns userId
    }

    override fun afterSpec(spec: Spec) {
        clearStaticMockk(UserId::class)
    }

    init {
        describe("Validate user") {
            val user = user()

            context("Invalid name") {

                it("Should fail when name is empty") {
                    shouldFailValidation<User> {
                        user.copy(name = "")
                    }.verify {
                        expect(User::name, "", NotBlank)
                    }
                }

                it("Should fail when name is blank") {
                    shouldFailValidation<User> {
                        user.copy(name = "   ")
                    }.verify {
                        expect(User::name, "   ", NotBlank)
                    }
                }

                it("Should fail when name size is greater than 50") {
                    shouldFailValidation<User> {
                        user.copy(name = (1..51).joinToString(separator = "") { "!" })
                    }.verify {
                        expect(User::name, (1..51).joinToString(separator = "") { "!" }, Size(max = 50))
                    }
                }
            }

            context("Invalid email") {

                it("Should fail when email is empty") {
                    shouldFailValidation<User> {
                        user.copy(email = "")
                    }.verify {
                        expect(User::email, "", NotBlank)
                    }
                }

                it("Should fail when email is blank") {
                    shouldFailValidation<User> {
                        user.copy(email = "   ")
                    }.verify {
                        expect(User::email, "   ", NotBlank)
                    }
                }

                it("Should fail when email size is greater than 50") {
                    shouldFailValidation<User> {
                        user.copy(email = (1..51).joinToString(separator = "") { "!" })
                    }.verify {
                        expect(User::email, (1..51).joinToString(separator = "") { "!" }, Size(max = 50))
                    }
                }

                it("Should fail when email is invalid") {
                    shouldFailValidation<User> {
                        user.copy(email = "invalid_email")
                    }.verify {
                        expect(User::email, "invalid_email", Email)
                    }
                }
            }
        }
    }
}
