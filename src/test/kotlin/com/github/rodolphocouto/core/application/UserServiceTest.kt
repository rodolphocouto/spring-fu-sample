package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.application.UserServiceTestFixture.createUserCommand
import com.github.rodolphocouto.core.application.UserServiceTestFixture.removeUserCommand
import com.github.rodolphocouto.core.application.UserServiceTestFixture.updateUserCommand
import com.github.rodolphocouto.core.application.UserServiceTestFixture.user
import com.github.rodolphocouto.core.application.UserServiceTestFixture.userCreated
import com.github.rodolphocouto.core.application.UserServiceTestFixture.userQueries
import com.github.rodolphocouto.core.application.UserServiceTestFixture.userQuery
import com.github.rodolphocouto.core.application.UserServiceTestFixture.userRemoved
import com.github.rodolphocouto.core.application.UserServiceTestFixture.userUpdated
import com.github.rodolphocouto.core.application.UserServiceTestFixture.users
import com.github.rodolphocouto.core.domain.UserAlreadyExistsException
import com.github.rodolphocouto.core.domain.UserNotFoundException
import com.github.rodolphocouto.core.domain.UserRepository
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.DescribeSpec
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList

class UserServiceTest : DescribeSpec() {

    private val repository = mockk<UserRepository>()
    private val service = UserService(repository)

    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearMocks(repository)
    }

    init {
        describe("Find all users") {

            it("Should find all users") {
                coEvery { repository.findAll() } returns users().asFlow()

                service.findAll().toList() shouldBe userQueries()

                coVerify { repository.findAll() }
                confirmVerified(repository)
            }
        }

        describe("Find user by id") {

            context("User does not exist") {

                it("Should return null when user does not exist") {
                    coEvery { repository.findById(any()) } returns null

                    val ex = shouldThrow<UserNotFoundException> {
                        service.findById(user().id)
                    }
                    ex.id shouldBe user().id

                    coVerify { repository.findById(eq(user().id)) }
                    confirmVerified(repository)
                }
            }

            it("Should find user by id") {
                coEvery { repository.findById(any()) } returns user()

                service.findById(userQuery().id) shouldBe userQuery()

                coVerify { repository.findById(eq(userQuery().id)) }
                confirmVerified(repository)
            }
        }

        describe("Create user") {

            context("User already exists") {

                it("Should fail when user already exists") {
                    coEvery { repository.findByName(any()) } returns userCreated()

                    val ex = shouldThrow<UserAlreadyExistsException> {
                        service.create(createUserCommand())
                    }
                    ex.id shouldBe userCreated().id

                    coVerify { repository.findByName(eq(createUserCommand().name)) }
                    confirmVerified(repository)
                }
            }

            it("Should create user") {
                coEvery { repository.findByName(any()) } returns null
                coEvery { repository.create(any()) } returns Unit

                service.create(createUserCommand())

                coVerify { repository.findByName(eq(createUserCommand().name)) }
                coVerify { repository.create(match { it.name == userCreated().name && it.email == userCreated().email }) }
                confirmVerified(repository)
            }
        }

        describe("Update user") {

            context("User does not exist") {

                it("Should fail when user does not exist") {
                    coEvery { repository.findById(any()) } returns null

                    val ex = shouldThrow<UserNotFoundException> {
                        service.update(updateUserCommand())
                    }
                    ex.id shouldBe updateUserCommand().id

                    coVerify { repository.findById(eq(updateUserCommand().id)) }
                    confirmVerified(repository)
                }
            }

            it("Should update user") {
                coEvery { repository.findById(any()) } returns user()
                coEvery { repository.update(any()) } returns Unit

                service.update(updateUserCommand())

                coVerify { repository.findById(eq(updateUserCommand().id)) }
                coVerify { repository.update(eq(userUpdated())) }
                confirmVerified(repository)
            }
        }

        describe("Remove user") {

            context("User does not exist") {

                it("Should fail when user does not exist") {
                    coEvery { repository.findById(any()) } returns null

                    val ex = shouldThrow<UserNotFoundException> {
                        service.remove(removeUserCommand())
                    }
                    ex.id shouldBe removeUserCommand().id

                    coVerify { repository.findById(eq(removeUserCommand().id)) }
                    confirmVerified(repository)
                }
            }

            it("Should remove user") {
                coEvery { repository.findById(any()) } returns user()
                coEvery { repository.remove(any()) } returns Unit

                service.remove(removeUserCommand())

                coVerify { repository.findById(eq(removeUserCommand().id)) }
                coVerify { repository.remove(eq(userRemoved())) }
                confirmVerified(repository)
            }
        }
    }
}
