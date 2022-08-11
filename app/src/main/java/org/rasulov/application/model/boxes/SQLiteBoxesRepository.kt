package org.rasulov.application.model.boxes

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.core.content.contentValuesOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import org.rasulov.application.model.AuthException
import org.rasulov.application.model.accounts.AccountsRepository
import org.rasulov.application.model.boxes.entities.Box
import org.rasulov.application.model.sqlite.Contract.AccountsBoxesSettingsTable
import org.rasulov.application.model.sqlite.Contract.BoxesTable
import org.rasulov.application.model.sqlite.wrapSQLiteException

class SQLiteBoxesRepository(
    private val db: SQLiteDatabase,
    private val accountsRepository: AccountsRepository,
    private val ioDispatcher: CoroutineDispatcher
) : BoxesRepository {

    private val reconstructFlow = MutableSharedFlow<Unit>(replay = 1).also { it.tryEmit(Unit) }

    override suspend fun getBoxes(onlyActive: Boolean): Flow<List<Box>> {
        return combine(accountsRepository.getAccount(), reconstructFlow) { account, _ ->
            queryBoxes(onlyActive,account?.id)
        }.flowOn(ioDispatcher)
    }

    override suspend fun activateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, true)
    }

    override suspend fun deactivateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, false)
    }

    private suspend fun setActiveFlagForBox(box: Box, isActive: Boolean) {
        val account = accountsRepository.getAccount().first() ?: throw AuthException()
        saveActiveFlag(account.id, box.id, isActive)
        reconstructFlow.tryEmit(Unit)
    }

    private fun queryBoxes(onlyActive: Boolean, accountId: Long?): List<Box> {
        if (accountId == null) return emptyList()

        val cursor = queryBoxes(onlyActive, accountId)
        return cursor.use {
            val list = mutableListOf<Box>()
            while (cursor.moveToNext()) {
                list.add(parseBox(cursor))
            }
            return@use list
        }
    }

    private fun parseBox(cursor: Cursor) = Box(
        id = cursor.getLong(cursor.getColumnIndexOrThrow(BoxesTable.COLUMN_ID)),
        colorName = cursor.getString(cursor.getColumnIndexOrThrow(BoxesTable.COLUMN_COLOR_NAME)),
        colorValue = Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(BoxesTable.COLUMN_COLOR_VALUE)))
    )


    private fun saveActiveFlag(accountId: Long, boxId: Long, isActive: Boolean) {
        db.insertWithOnConflict(
            AccountsBoxesSettingsTable.TABLE_NAME,
            null,
            contentValuesOf(
                AccountsBoxesSettingsTable.COLUMN_ACCOUNT_ID to accountId,
                AccountsBoxesSettingsTable.COLUMN_BOX_ID to boxId,
                AccountsBoxesSettingsTable.COLUMN_IS_ACTIVE to isActive
            ),
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    private fun queryBoxes(onlyActive: Boolean, accountId: Long): Cursor {
        return if (onlyActive) {
            db.rawQuery(
                " SELECT ${BoxesTable.TABLE_NAME}.* \n" +

                        "FROM ${BoxesTable.TABLE_NAME} LEFT JOIN ${AccountsBoxesSettingsTable.TABLE_NAME}\n" +

                        "ON ${BoxesTable.COLUMN_ID} = ${AccountsBoxesSettingsTable.COLUMN_BOX_ID}\n" +

                        "AND ${AccountsBoxesSettingsTable.COLUMN_ACCOUNT_ID} = ? " +

                        "WHERE ${AccountsBoxesSettingsTable.COLUMN_IS_ACTIVE} = 1 \n" +

                        "OR ${AccountsBoxesSettingsTable.COLUMN_IS_ACTIVE} IS NULL",

                arrayOf(accountId.toString())
            )

        } else {
            db.rawQuery("SELECT * FROM ${BoxesTable.TABLE_NAME}", null)
        }
    }
}