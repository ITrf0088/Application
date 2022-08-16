package org.rasulov.application.model.accounts.impl.room.entities

import android.database.DatabaseUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.rasulov.application.model.accounts.core.entities.Account
import org.rasulov.application.model.accounts.core.entities.SignUpData
import org.rasulov.application.utils.security.Security

@Entity(
    tableName = "accounts",
    indices = [Index("email", unique = true)]
)

data class AccountDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val email: String,
    val username: String,
    val hashPassword: String,
    @ColumnInfo(defaultValue = "") val salt: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
) {

    fun toAccount() = Account(
        id,
        username,
        email,
        createdAt
    )

    companion object {
        fun fromSignUpData(signUpData: SignUpData, security: Security): AccountDBEntity {
            val salt = security.generateSalt()
            val hashP = security.passwordToHash(signUpData.password.toCharArray(), salt)
            return AccountDBEntity(
                id = AUTO_GENERATE,
                email = signUpData.email,
                username = signUpData.username,
                hashPassword = security.bytesToString(hashP),
                salt = security.bytesToString(salt),
                createdAt = System.currentTimeMillis()
            )
        }

        private const val AUTO_GENERATE = 0L
    }
}
