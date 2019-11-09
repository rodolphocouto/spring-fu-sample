package com.github.rodolphocouto.adapters.r2dbc

import com.github.rodolphocouto.core.domain.User
import com.github.rodolphocouto.core.domain.UserId
import com.github.rodolphocouto.core.domain.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.asType
import org.springframework.data.r2dbc.core.await
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.data.r2dbc.core.flow

class UserRepositoryR2dbc(private val db: DatabaseClient) : UserRepository {

    override suspend fun findAll() =
        db.execute("select * from user").asType<User>().fetch().flow()

    override suspend fun findById(id: UserId) =
        db.execute("select * from user where id = :id").bind("id", id).asType<User>().fetch().awaitOneOrNull()

    override suspend fun findByName(name: String) =
        db.execute("select * from user where name = :name").bind("name", name).asType<User>().fetch().awaitOneOrNull()

    override suspend fun create(user: User) =
        db.execute("insert into user values (:id, :name, :email)")
            .bind("id", user.id)
            .bind("name", user.name)
            .bind("email", user.email)
            .await()

    override suspend fun update(user: User) =
        db.execute("update user set id = :id, name = :name, email = :email where id = :id")
            .bind("id", user.id)
            .bind("name", user.name)
            .bind("email", user.email)
            .await()

    override suspend fun remove(user: User) =
        db.execute("delete from user where id = :id").bind("id", user.id).await()

    suspend fun init() = coroutineScope {
        db.execute("create table if not exists user (id uuid primary key, name varchar, email varchar)").await()
        db.execute("delete from user")

        val user1 = async { create(User(id = UserId.randomUUID(), name = "User 1", email = "user1@company.com")) }
        val user2 = async { create(User(id = UserId.randomUUID(), name = "User 2", email = "user2@company.com")) }
        val user3 = async { create(User(id = UserId.randomUUID(), name = "User 2", email = "user3@company.com")) }

        user1.await()
        user2.await()
        user3.await()
    }
}
