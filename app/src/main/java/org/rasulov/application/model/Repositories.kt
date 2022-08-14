package org.rasulov.application.model

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.rasulov.application.model.accounts.core.AccountsRepository
import org.rasulov.application.model.accounts.impl.room.RoomAccountsRepository
import org.rasulov.application.model.boxes.core.BoxesRepository
import org.rasulov.application.model.boxes.impl.room.RoomBoxesRepository
import org.rasulov.application.model.persistentHelper.room.AppDatabase
import org.rasulov.application.model.settings.AppSettings
import org.rasulov.application.model.settings.SharedPreferencesAppSettings

object Repositories {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    // -- stuffs

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database.db"
        )
            .createFromAsset("init.db")
            .build()
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        RoomAccountsRepository(database.getAccountsDao(), appSettings, ioDispatcher)
    }

    val boxesRepository: BoxesRepository by lazy {
        RoomBoxesRepository(accountsRepository, database.getBoxesDao(), ioDispatcher)
    }


}
