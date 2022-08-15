package org.rasulov.application.model.boxes.impl.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.rasulov.application.model.boxes.impl.room.entities.AccountBoxSettingsEntity
import org.rasulov.application.model.boxes.impl.room.entities.BoxAndSettingsTuple
import org.rasulov.application.model.boxes.impl.room.entities.BoxDBEntity
import org.rasulov.application.model.boxes.impl.room.entities.SettingWithEntitiesTuple

@Dao
interface BoxesDao {

    @Transaction
    @Query("SELECT * FROM accounts_boxes_settings WHERE account_id = :accountId")
    fun getBoxesAndSettings(accountId: Long): Flow<List<SettingWithEntitiesTuple>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActiveFlagForBox(settingsEntity: AccountBoxSettingsEntity)



}