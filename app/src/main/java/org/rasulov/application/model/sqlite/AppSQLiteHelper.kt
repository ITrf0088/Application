package org.rasulov.application.model.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AppSQLiteHelper(
    private val applicationContext: Context
) : SQLiteOpenHelper(applicationContext, "app.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val sqlScript: String = applicationContext.assets
            .open("start.sql")
            .bufferedReader()
            .use { it.readText() }

        sqlScript
            .split(';')
            .filter { it.isNotBlank() }
            .forEach { db.execSQL(it) }
        Log.d("it0088", "onCreateSql: ")


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) = Unit


}