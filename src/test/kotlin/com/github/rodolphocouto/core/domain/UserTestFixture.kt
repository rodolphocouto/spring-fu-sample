package com.github.rodolphocouto.core.domain

object UserTestFixture {

    fun user() = User(
        id = UserId.randomUUID(),
        name = "User 1",
        email = "user1@company.com"
    )
}
