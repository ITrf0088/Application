package org.rasulov.application.model.boxes.impl.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.rasulov.application.model.boxes.impl.room.entities.AccountBoxSettingsEntity
import org.rasulov.application.model.boxes.impl.room.entities.BoxAndSettingsTuple
import org.rasulov.application.model.boxes.impl.room.entities.BoxDBEntity

@Dao
interface BoxesDao {

    @Query(
        "SELECT * FROM boxes " +
                "LEFT JOIN accounts_boxes_settings " +
                "ON id = box_id AND account_id = :accountId"
    )
    fun getBoxesAndSettings(accountId: Long): Flow<List<BoxAndSettingsTuple>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActiveFlagForBox(settingsEntity: AccountBoxSettingsEntity)

}