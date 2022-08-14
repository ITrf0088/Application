package org.rasulov.application.model.boxes.impl.room.entities

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.rasulov.application.model.boxes.core.entities.Box

@Entity(tableName = "boxes")
data class BoxDBEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "color_name") val colorName: String,
    @ColumnInfo(name = "color_value") val colorValue: String
) {
    fun toBox() = Box(id, colorName, Color.parseColor(colorValue))
}