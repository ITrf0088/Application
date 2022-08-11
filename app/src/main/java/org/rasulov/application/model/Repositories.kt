package org.rasulov.application.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.rasulov.application.model.accounts.AccountsRepository
import org.rasulov.application.model.accounts.SQLiteAccountsRepository
import org.rasulov.application.model.boxes.BoxesRepository
import org.rasulov.application.model.boxes.SQLiteBoxesRepository
import org.rasulov.application.model.settings.AppSettings
import org.rasulov.application.model.settings.SharedPreferencesAppSettings
import org.rasulov.application.model.sqlite.AppSQLiteHelper

object Repositories {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    // -- stuffs

    private val database: SQLiteDatabase by lazy<SQLiteDatabase> {
        AppSQLiteHelper(applicationContext).writableDatabase
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        SQLiteAccountsRepository(database, appSettings, ioDispatcher)
    }

    val boxesRepository: BoxesRepository by lazy {
        SQLiteBoxesRepository(database, accountsRepository, ioDispatcher)
    }


}
