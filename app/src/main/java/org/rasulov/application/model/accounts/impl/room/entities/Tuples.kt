package org.rasulov.application.model.accounts.impl.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.rasulov.application.model.boxes.impl.room.entities.AccountBoxSettingsEntity
import org.rasulov.application.model.boxes.impl.room.entities.BoxDBEntity

data class AccountSignInTuple(
    val id: Long,
    val hashPassword: String,
    val salt: String
)

data class AccountUpdateUsernameTuple(
    val id: Long,
    val username: String
)

data class AccountAndEditedBoxesTuple(

    @Embedded val accountDBEntity: AccountDBEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",

        associateBy = Junction(
            value = AccountBoxSettingsEntity::class,
            parentColumn = "account_id",
            entityColumn = "box_id"
        )
    )
    val boxes: List<BoxDBEntity>

)