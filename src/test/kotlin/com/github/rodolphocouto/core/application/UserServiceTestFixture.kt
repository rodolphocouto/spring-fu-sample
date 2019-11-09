package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.User
import com.github.rodolphocouto.core.domain.UserId

object UserServiceTestFixture {

    private val userId = UserId.randomUUID()
    private val userIds = (1..10).map { UserId.randomUUID() }

    fun user() = User(
        id = userId,
        name = "User 1",
        email = "user1@company.com"
    )

    fun users() = userIds.mapIndexed { index, userId ->
        User(
            id = userId,
            name = "User $index",
            email = "user$index@company.com"
        )
    }

    fun userQuery() = UserQuery(
        id = userId,
        name = "User 1",
        email = "user1@company.com"
    )

    fun userQueries() = userIds.mapIndexed { index, userId ->
        UserQuery(
            id = userId,
            name = "User $index",
            email = "user$index@company.com"
        )
    }

    fun createUserCommand() = CreateUserCommand(
        name = "User 1",
        email = "user1@company.com"
    )

    fun updateUserCommand() = UpdateUserCommand(
        id = userId,
        name = "User 2",
        email = "user2@company.com"
    )

    fun removeUserCommand() = RemoveUserCommand(
        id = userId
    )

    fun userCreated() = User(
        id = userId,
        name = "User 1",
        email = "user1@company.com"
    )

    fun userUpdated() = User(
        id = userId,
        name = "User 2",
        email = "user2@company.com"
    )

    fun userRemoved() = User(
        id = userId,
        name = "User 1",
        email = "user1@company.com"
    )
}
