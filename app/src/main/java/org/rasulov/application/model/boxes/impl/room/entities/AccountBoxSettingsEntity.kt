package org.rasulov.application.model.boxes.impl.room.entities

import androidx.room.*
import org.rasulov.application.model.accounts.impl.room.entities.AccountDBEntity
import org.rasulov.application.model.boxes.core.entities.BoxSetting

@Entity(
    tableName = "accounts_boxes_settings",
    primaryKeys = ["account_id", "box_id"],
    indices = [Index("box_id")],
    foreignKeys = [
        ForeignKey(
            entity = AccountDBEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BoxDBEntity::class,
            parentColumns = ["id"],
            childColumns = ["box_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
)
data class AccountBoxSettingsEntity(
    @ColumnInfo(name = "account_id") val accountId: Long,
    @ColumnInfo(name = "box_id") val boxId: Long,
    @Embedded val setting: SettingsTuple
)
