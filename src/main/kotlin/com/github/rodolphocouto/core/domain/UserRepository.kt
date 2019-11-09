package com.github.rodolphocouto.core.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun findAll(): Flow<User>

    suspend fun findById(id: UserId): User?

    suspend fun findByName(name: String): User?

    suspend fun create(user: User)

    suspend fun update(user: User)

    suspend fun remove(user: User)
}
