package com.github.rodolphocouto.core.application

import com.github.rodolphocouto.core.domain.User
import com.github.rodolphocouto.core.domain.UserAlreadyExistsException
import com.github.rodolphocouto.core.domain.UserId
import com.github.rodolphocouto.core.domain.UserNotFoundException
import com.github.rodolphocouto.core.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserService(private val repository: UserRepository) {

    suspend fun findAll(): Flow<UserQuery> = repository.findAll().map { it.toQuery() }

    suspend fun findById(id: UserId): UserQuery = repository.findById(id)?.toQuery() ?: throw UserNotFoundException(id)

    suspend fun create(command: CreateUserCommand): UserId {
        repository.findByName(command.name)?.let { throw UserAlreadyExistsException(it.id) }

        val user = User(UserId.randomUUID(), command.name, command.email)
        repository.create(user)

        return user.id
    }

    suspend fun update(command: UpdateUserCommand) {
        val oldUser = repository.findById(command.id) ?: throw UserNotFoundException(command.id)
        val newUser = oldUser.copy(name = command.name, email = command.email)
        repository.update(newUser)
    }

    suspend fun remove(command: RemoveUserCommand) {
        val user = repository.findById(command.id) ?: throw UserNotFoundException(command.id)
        repository.remove(user)
    }
}
