package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.User
import com.github.rodolphocouto.core.domain.UserId

data class UserQuery(
    val id: UserId,
    val name: String,
    val email: String
)

fun User.toQuery() = UserQuery(
    id = this.id,
    name = this.name,
    email = this.email
)
