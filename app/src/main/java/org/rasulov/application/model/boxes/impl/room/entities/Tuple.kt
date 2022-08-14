package org.rasulov.application.model.boxes.impl.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded


data class SettingsTuple(
    @ColumnInfo(name = "is_active") val isActive: Boolean
)

data class BoxAndSettingsTuple(
    @Embedded val boxDBEntity: BoxDBEntity,
    @Embedded val settingDBEntity: AccountBoxSettingsEntity?
)