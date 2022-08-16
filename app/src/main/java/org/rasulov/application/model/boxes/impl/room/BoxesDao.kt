package org.rasulov.application.model.boxes.impl.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.rasulov.application.model.boxes.impl.room.entities.*

@Dao
interface BoxesDao {

    @Transaction
    @Query("SELECT * FROM accounts_boxes_settings WHERE account_id = :accountId")
    fun getBoxesAndSettings(accountId: Long): Flow<List<SettingWithEntitiesTuple>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActiveFlagForBox(settingsEntity: AccountBoxSettingsEntity)


    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getAccountAndEditedBoxes(accountId: Long): AccountAndEditedBoxesTuple
}