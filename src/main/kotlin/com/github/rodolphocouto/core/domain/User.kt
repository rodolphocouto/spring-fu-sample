package com.github.rodolphocouto.core.domain

import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import java.util.UUID

typealias UserId = UUID

data class UserNotFoundException(val id: UserId) : Exception()
data class UserAlreadyExistsException(val id: UserId) : Exception()

data class User(
    val id: UserId,
    val name: String,
    val email: String
) {
    init {
        validate(this) {
            validate(User::name).isNotBlank().hasSize(max = 50)
            validate(User::email).isNotBlank().hasSize(max = 50).isEmail()
        }
    }
}
