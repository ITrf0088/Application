package org.rasulov.application.model.boxes.impl.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.rasulov.application.model.accounts.impl.room.entities.AccountDBEntity

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
    @ColumnInfo(name = "is_active") val isActive: Boolean
)
