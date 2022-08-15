package org.rasulov.application.model.persistentHelper.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import org.rasulov.application.model.accounts.impl.room.AccountsDao
import org.rasulov.application.model.accounts.impl.room.entities.AccountDBEntity
import org.rasulov.application.model.boxes.impl.room.BoxesDao
import org.rasulov.application.model.boxes.impl.room.entities.AccountBoxSettingsEntity
import org.rasulov.application.model.boxes.impl.room.entities.BoxDBEntity

@Database(
    version = 2,
    entities = [AccountDBEntity::class, BoxDBEntity::class, AccountBoxSettingsEntity::class],
    autoMigrations = [AutoMigration(from = 1, to = 2, spec = AutoMigrationSpec1to2::class)],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao(): AccountsDao

    abstract fun getBoxesDao(): BoxesDao


}