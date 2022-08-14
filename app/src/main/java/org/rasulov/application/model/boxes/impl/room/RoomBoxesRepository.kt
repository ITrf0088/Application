package org.rasulov.application.model.boxes.impl.room

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.rasulov.application.model.AuthException
import org.rasulov.application.model.accounts.core.AccountsRepository
import org.rasulov.application.model.boxes.core.BoxesRepository
import org.rasulov.application.model.boxes.core.entities.Box
import org.rasulov.application.model.boxes.core.entities.BoxSetting
import org.rasulov.application.model.boxes.impl.room.entities.AccountBoxSettingsEntity
import org.rasulov.application.model.persistentHelper.sqlite_api.wrapSQLiteException

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalCoroutinesApi::class)
class RoomBoxesRepository(
    private val accountsRepository: AccountsRepository,
    private val boxesDao: BoxesDao,
    private val ioDispatcher: CoroutineDispatcher
) : BoxesRepository {

    override suspend fun getBoxesAndSettings(onlyActive: Boolean): Flow<List<BoxSetting>> {
        return accountsRepository.getAccount()
            .flatMapLatest { account ->
                if (account == null) return@flatMapLatest flowOf(emptyList())
                queryBoxesAndSettings(account.id)
            }
            .mapLatest { boxSettings ->
                if (onlyActive) {
                    boxSettings.filter { it.isActive }
                } else {
                    boxSettings
                }
            }
    }

    override suspend fun activateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, true)
    }

    override suspend fun deactivateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, false)
    }

    private fun queryBoxesAndSettings(accountId: Long): Flow<List<BoxSetting>> {
        return boxesDao.getBoxesAndSettings(accountId)
            .map { boxAndSettings ->
                boxAndSettings.map { boxSettingEntry ->
                    BoxSetting(
                        box = boxSettingEntry.key.toBox(),
                        isActive = boxSettingEntry.value?.isActive ?: true
                    )
                }
            }

    }

    private suspend fun setActiveFlagForBox(box: Box, isActive: Boolean) {
        val accountId = accountsRepository.getAccount().first()?.id ?: throw AuthException()
        boxesDao.setActiveFlagForBox(
            AccountBoxSettingsEntity(
                accountId,
                box.id,
                isActive
            )
        )
    }
}