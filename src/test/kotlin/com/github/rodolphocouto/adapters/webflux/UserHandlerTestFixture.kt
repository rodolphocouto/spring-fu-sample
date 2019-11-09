package com.github.rodolphocouto.adapters.webflux

import com.github.rodolphocouto.core.application.CreateUserCommand
import com.github.rodolphocouto.core.application.RemoveUserCommand
import com.github.rodolphocouto.core.application.UpdateUserCommand
import com.github.rodolphocouto.core.application.UserQuery
import com.github.rodolphocouto.core.domain.UserId

object UserHandlerTestFixture {

    private val userId = UserId.randomUUID()
    private val userIds = (1..10).map { UserId.randomUUID() }

    fun user() = UserQuery(
        id = userId,
        name = "User 1",
        email = "user1@company.com"
    )

    fun users() = userIds.mapIndexed { index, userId ->
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
}
