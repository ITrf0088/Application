package org.rasulov.application.model.accounts.impl.room.entities

import androidx.room.ColumnInfo

data class AccountSignInTuple(
    val id: Long,
    @ColumnInfo(name = "password") val passw: String
)

data class AccountUpdateUsernameTuple(
    val id: Long,
    val username: String
)