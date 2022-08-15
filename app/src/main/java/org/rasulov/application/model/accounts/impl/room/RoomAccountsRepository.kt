package org.rasulov.application.model.accounts.impl.room

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.rasulov.application.model.AccountAlreadyExistsException
import org.rasulov.application.model.AuthException
import org.rasulov.application.model.EmptyFieldException
import org.rasulov.application.model.Field
import org.rasulov.application.model.accounts.core.AccountsRepository
import org.rasulov.application.model.accounts.core.entities.Account
import org.rasulov.application.model.accounts.core.entities.SignUpData
import org.rasulov.application.model.accounts.impl.room.entities.AccountDBEntity
import org.rasulov.application.model.accounts.impl.room.entities.AccountUpdateUsernameTuple
import org.rasulov.application.model.persistentHelper.sqlite_api.wrapSQLiteException
import org.rasulov.application.model.settings.AppSettings
import org.rasulov.application.utils.AsyncLoader
import org.rasulov.application.utils.security.Security

class RoomAccountsRepository(
    private val accountsDao: AccountsDao,
    private val appSettings: AppSettings,
    private val security: Security,
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
            .flatMapLatest { accountId ->
                if (accountId.value == AppSettings.NO_ACCOUNT_ID) {
                    flowOf(null)
                } else {
                    getAccountById(accountId.value)
                }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateAccountUsername(newUsername: String) =
        wrapSQLiteException(ioDispatcher) {
            if (newUsername.isBlank()) throw EmptyFieldException(Field.Username)
            delay(1000)
            val accountId = appSettings.getCurrentAccountId()
            if (accountId == AppSettings.NO_ACCOUNT_ID) throw AuthException()

            updateUsernameForAccountId(accountId, newUsername)

            currentAccountIdFlow.get().value = AccountId(accountId)
            return@wrapSQLiteException
        }

    private suspend fun findAccountIdByEmailAndPassword(email: String, password: String): Long {
        val accountsSignInTuple = accountsDao.findByEmail(email) ?: throw AuthException()

        val salt = security.stringToBytes(accountsSignInTuple.salt)
        val hash = security.passwordToHash(password.toCharArray(), salt)
        val hashStr = security.bytesToString(hash)
        if (accountsSignInTuple.hashPassword != hashStr) throw AuthException()
        return accountsSignInTuple.id

    }

    private suspend fun createAccount(signUpData: SignUpData) {
        try {
            accountsDao.createAccount(
                accountDBEntity = AccountDBEntity.fromSignUpData(signUpData, security)
            )
        } catch (e: SQLiteConstraintException) {
            val ex = AccountAlreadyExistsException()
            ex.initCause(e)
            throw ex
        }

    }

    private fun getAccountById(accountId: Long): Flow<Account?> {
        return accountsDao
            .getById(accountId)
            .map { it?.toAccount() }
    }

    private suspend fun updateUsernameForAccountId(accountId: Long, newUsername: String) {
        accountsDao.updateUsername(
            updateUsernameTuple = AccountUpdateUsernameTuple(accountId, newUsername)
        )
    }

    private class AccountId(val value: Long)

}