package com.github.rodolphocouto.adapters.webflux

import com.github.rodolphocouto.adapters.webflux.UserHandlerTestFixture.createUserCommand
import com.github.rodolphocouto.adapters.webflux.UserHandlerTestFixture.removeUserCommand
import com.github.rodolphocouto.adapters.webflux.UserHandlerTestFixture.updateUserCommand
import com.github.rodolphocouto.adapters.webflux.UserHandlerTestFixture.user
import com.github.rodolphocouto.adapters.webflux.UserHandlerTestFixture.users
import com.github.rodolphocouto.core.application.UserQuery
import com.github.rodolphocouto.core.application.UserService
import com.github.rodolphocouto.core.domain.UserAlreadyExistsException
import com.github.rodolphocouto.core.domain.UserNotFoundException
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.specs.DescribeSpec
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.asFlow
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList

class UserHandlerTest : DescribeSpec() {

    private val service = mockk<UserService>()
    private val handler = UserHandler(service)
    private val webClient = WebTestClient.bindToRouterFunction(userRouter(handler)).build()

    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearMocks(service)
    }

    init {
        describe("Find all users") {

            it("Should find all users and return 200") {
                coEvery { service.findAll() } returns users().asFlow()

                webClient
                    .get()
                    .uri("/users")
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBodyList<UserQuery>()
                    .contains(*users().toTypedArray())

                coVerify { service.findAll() }
                confirmVerified(service)
            }
        }

        describe("Find user by id") {

            context("Invalid user id") {

                it("Should return 404 when user id is invalid") {
                    webClient
                        .get()
                        .uri { it.path("/users").path("/{id}").build("INVALID_ID") }
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound
                }
            }

            context("User does not exist") {

                it("Should return 404 when user does not exist") {
                    coEvery { service.findById(any()) } throws UserNotFoundException(user().id)

                    webClient
                        .get()
                        .uri { it.path("/users").path("/{id}").build(user().id) }
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound
                        .expectBody().isEmpty

                    coVerify { service.findById(eq(user().id)) }
                    confirmVerified(service)
                }
            }

            it("Should find user and return 200") {
                coEvery { service.findById(any()) } returns user()

                webClient
                    .get()
                    .uri { it.path("/users").path("/{id}").build(user().id) }
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk
                    .expectHeader().contentType(APPLICATION_JSON)
                    .expectBody<UserQuery>()
                    .isEqualTo(user())

                coVerify { service.findById(eq(user().id)) }
                confirmVerified(service)
            }
        }

        describe("Create user") {

            context("User already exists") {

                it("Should return 409 when user already exists") {
                    coEvery { service.create(any()) } throws UserAlreadyExistsException(user().id)
                    coEvery { service.findById(any()) } returns user()

                    webClient
                        .post()
                        .uri("/users")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(createUserCommand())
                        .exchange()
                        .expectStatus().isEqualTo(CONFLICT)
                        .expectHeader().contentType(APPLICATION_JSON)
                        .expectBody<UserQuery>()
                        .isEqualTo(user())

                    coVerify { service.create(eq(createUserCommand())) }
                    coVerify { service.findById(eq(user().id)) }
                    confirmVerified(service)
                }
            }

            it("Should create user and return 201") {
                coEvery { service.create(any()) } returns user().id

                webClient
                    .post()
                    .uri("/users")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(createUserCommand())
                    .exchange()
                    .expectStatus().isCreated
                    .expectHeader().valueEquals(LOCATION, "/users/${user().id}")
                    .expectBody().isEmpty

                coVerify { service.create(eq(createUserCommand())) }
                confirmVerified(service)
            }
        }

        describe("Update user") {

            context("Invalid user id") {

                it("Should return 404 when user id is invalid") {
                    webClient
                        .put()
                        .uri { it.path("/users").path("/{id}").build("INVALID_ID") }
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updateUserCommand())
                        .exchange()
                        .expectStatus().isNotFound
                }
            }

            context("User does not exist") {

                it("Should return 404 when user does not exist") {
                    coEvery { service.update(any()) } throws UserNotFoundException(user().id)

                    webClient
                        .put()
                        .uri { it.path("/users").path("/{id}").build(user().id) }
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updateUserCommand())
                        .exchange()
                        .expectStatus().isNotFound
                        .expectBody().isEmpty

                    coVerify { service.update(eq(updateUserCommand())) }
                    confirmVerified(service)
                }
            }

            it("Should update user and return 204") {
                coEvery { service.update(any()) } returns Unit

                webClient
                    .put()
                    .uri { it.path("/users").path("/{id}").build(user().id) }
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(updateUserCommand())
                    .exchange()
                    .expectStatus().isNoContent
                    .expectBody().isEmpty

                coVerify { service.update(eq(updateUserCommand())) }
                confirmVerified(service)
            }
        }

        describe("Remove user") {

            context("Invalid user id") {

                it("Should return 404 when user id is invalid") {
                    webClient
                        .delete()
                        .uri { it.path("/users").path("/{id}").build("INVALID_ID") }
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound
                }
            }

            context("User does not exist") {

                it("Should return 404 when user does not exist") {
                    coEvery { service.remove(any()) } throws UserNotFoundException(user().id)

                    webClient
                        .delete()
                        .uri { it.path("/users").path("/{id}").build(user().id) }
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound
                        .expectBody().isEmpty

                    coVerify { service.remove(eq(removeUserCommand())) }
                    confirmVerified(service)
                }
            }

            it("Should remove user and return 204") {
                coEvery { service.remove(any()) } returns Unit

                webClient
                    .delete()
                    .uri { it.path("/users").path("/{id}").build(user().id) }
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNoContent
                    .expectBody().isEmpty

                coVerify { service.remove(eq(removeUserCommand())) }
                confirmVerified(service)
            }
        }
    }
}
