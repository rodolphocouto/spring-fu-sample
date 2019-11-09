package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.UserId

data class CreateUserCommand(
    val name: String,
    val email: String
)

data class UpdateUserCommand(
    val id: UserId,
    val name: String,
    val email: String
)

data class RemoveUserCommand(
    val id: UserId
)
