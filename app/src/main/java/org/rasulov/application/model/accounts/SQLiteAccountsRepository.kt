package org.rasulov.application.model.accounts

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.content.contentValuesOf
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.rasulov.application.model.AccountAlreadyExistsException
import org.rasulov.application.model.AuthException
import org.rasulov.application.model.EmptyFieldException
import org.rasulov.application.model.Field
import org.rasulov.application.model.accounts.entities.Account
import org.rasulov.application.model.accounts.entities.SignUpData
import org.rasulov.application.model.settings.AppSettings
import org.rasulov.application.model.sqlite.Contract
import org.rasulov.application.model.sqlite.Contract.AccountsTable
import org.rasulov.application.model.sqlite.wrapSQLiteException
import org.rasulov.application.utils.AsyncLoader

class SQLiteAccountsRepository(
    private val db: SQLiteDatabase,
    private val appSettings: AppSettings,
    private val ioDispatcher: CoroutineDispatcher
) : AccountsRepository {

    private val currentAccountIdFlow = AsyncLoader {
        MutableStateFlow(AccountId(appSettings.getCurrentAccountId()))
    }

    override suspend fun isSignedIn(): Boolean {
        delay(2000)
        return appSettings.getCurrentAccountId() != AppSettings.NO_ACCOUNT_ID
    }

    override suspend fun signIn(email: String, password: String) =
        wrapSQLiteException(ioDispatcher) {
            if (email.isBlank()) throw EmptyFieldException(Field.Email)
            if (password.isBlank()) throw EmptyFieldException(Field.Password)

            delay(1000)

            val accountId = findAccountIdByEmailAndPassword(email, password)
            appSettings.setCurrentAccountId(accountId)
            currentAccountIdFlow.get().value = AccountId(accountId)

            return@wrapSQLiteException
        }

    override suspend fun signUp(signUpData: SignUpData) = wrapSQLiteException(ioDispatcher) {
        signUpData.validate()
        delay(1000)
        createAccount(signUpData)
    }

    override suspend fun logout() {
        appSettings.setCurrentAccountId(AppSettings.NO_ACCOUNT_ID)
        currentAccountIdFlow.get().value = AccountId(AppSettings.NO_ACCOUNT_ID)
    }

    override suspend fun getAccount(): Flow<Account?> {
        return currentAccountIdFlow.get()
            .map { accountId ->
                getAccountById(accountId.value)
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateAccountUsername(newUsername: String) =
        wrapSQLiteException(ioDispatcher) {
            if (newUsername.isBlank()) throw EmptyFieldException(Field.Username)
            delay(3000)
            val accountId = appSettings.getCurrentAccountId()
            if (accountId == AppSettings.NO_ACCOUNT_ID) throw AuthException()

            updateUsernameForAccountId(accountId, newUsername)
            Log.d("itrr0088", "updateAccountUsername: ${accountId}")
            currentAccountIdFlow.get().value = AccountId(accountId)
            return@wrapSQLiteException
        }

    private fun findAccountIdByEmailAndPassword(email: String, password: String): Long {
        val cursor = db.query(
            AccountsTable.TABLE_NAME,
            arrayOf(AccountsTable.COLUMN_ID),
            "${AccountsTable.COLUMN_EMAIL} = ? AND " +
                    "${AccountsTable.COLUMN_PASSWORD} = ?",
            arrayOf(email, password),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.count == 0) throw AuthException()
            cursor.moveToFirst()
            it.getLong(cursor.getColumnIndexOrThrow(AccountsTable.COLUMN_ID))
        }

    }

    private fun createAccount(signUpData: SignUpData) {
        try {
            db.insertOrThrow(
                AccountsTable.TABLE_NAME,
                null,
                contentValuesOf(
                    AccountsTable.COLUMN_USERNAME to signUpData.username,
                    AccountsTable.COLUMN_EMAIL to signUpData.email,
                    AccountsTable.COLUMN_PASSWORD to signUpData.password,
                    AccountsTable.COLUMN_CREATED_AT to System.currentTimeMillis()

                )
            )
        } catch (ex: SQLiteConstraintException) {
            throw AccountAlreadyExistsException().apply { initCause(ex) }
        }
    }

    private fun getAccountById(accountId: Long): Account? {
        if (accountId == AppSettings.NO_ACCOUNT_ID) return null

        val cursor = db.query(
            AccountsTable.TABLE_NAME,
            arrayOf(
                AccountsTable.COLUMN_ID,
                AccountsTable.COLUMN_EMAIL,//todocha
                AccountsTable.COLUMN_USERNAME,
                AccountsTable.COLUMN_CREATED_AT
            ),
            "${AccountsTable.COLUMN_ID} = ?",
            arrayOf(accountId.toString()),
            null, null, null
        )

        return cursor.use {
            if (it.count == 0) null
            else {
                it.moveToFirst()
                Account(
                    id = it.getLong(it.getColumnIndexOrThrow(AccountsTable.COLUMN_ID)),
                    username = it.getString(it.getColumnIndexOrThrow(AccountsTable.COLUMN_USERNAME)),
                    email = it.getString(it.getColumnIndexOrThrow(AccountsTable.COLUMN_EMAIL)),
                    createdAt = it.getLong(it.getColumnIndexOrThrow(AccountsTable.COLUMN_CREATED_AT)),
                )
            }
        }
    }

    private fun updateUsernameForAccountId(accountId: Long, newUsername: String) {
        db.update(
            AccountsTable.TABLE_NAME,
            contentValuesOf(AccountsTable.COLUMN_USERNAME to newUsername),
            "${AccountsTable.COLUMN_ID} = ?",
            arrayOf(accountId.toString())
        )
    }

    private class AccountId(val value: Long)
}