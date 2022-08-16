package org.rasulov.application.model.accounts.impl.room.entities

data class AccountSignInTuple(
    val id: Long,
    val hashPassword: String,
    val salt: String
)

data class AccountUpdateUsernameTuple(
    val id: Long,
    val username: String
)

