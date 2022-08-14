package org.rasulov.application.model.accounts.impl.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.rasulov.application.model.accounts.core.entities.Account
import org.rasulov.application.model.accounts.core.entities.SignUpData

@Entity(
    tableName = "accounts",
    indices = [Index("email", unique = true)]
)

data class AccountDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val email: String,
    val username: String,
    val password: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = Account.UNKNOWN_CREATED_AT
) {

    fun toAccount() = Account(
        id,
        username,
        email,
        createdAt
    )

    companion object {
        fun fromSignUpData(signUpData: SignUpData) = AccountDBEntity(
            id = AUTO_GENERATE,
            email = signUpData.email,
            username = signUpData.username,
            password = signUpData.password,
            createdAt = System.currentTimeMillis()
        )

        private const val AUTO_GENERATE = 0L
    }
}
