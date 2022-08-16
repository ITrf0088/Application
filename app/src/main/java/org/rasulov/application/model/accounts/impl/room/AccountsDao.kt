package org.rasulov.application.model.accounts.impl.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.rasulov.application.model.accounts.impl.room.entities.AccountDBEntity
import org.rasulov.application.model.accounts.impl.room.entities.AccountSignInTuple
import org.rasulov.application.model.accounts.impl.room.entities.AccountUpdateUsernameTuple

@Dao
interface AccountsDao {

    @Query("SELECT id,hashPassword,salt FROM accounts WHERE email = :email")
    suspend fun findByEmail(email: String): AccountSignInTuple?

    @Update(entity = AccountDBEntity::class)
    suspend fun updateUsername(updateUsernameTuple: AccountUpdateUsernameTuple)

    @Insert
    suspend fun createAccount(accountDBEntity: AccountDBEntity)

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getById(accountId: Long): Flow<AccountDBEntity?>




}