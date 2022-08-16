package org.rasulov.application.model.persistentHelper.room

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.content.contentValuesOf
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import org.rasulov.application.utils.security.SecurityImpl

@RenameColumn(tableName = "accounts", fromColumnName = "password", toColumnName = "hashPassword")
class AutoMigrationSpec1to2 : AutoMigrationSpec {

    private val security = SecurityImpl()

    override fun onPostMigrate(db: SupportSQLiteDatabase) {
        super.onPostMigrate(db)
        Log.d("it0088", "onPostMigrate: ")
        db.query("SELECT * FROM accounts").use { cursor ->
            val passwordHashIndex = cursor.getColumnIndex("hashPassword")
            val idIndex = cursor.getColumnIndex("id")

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val password = cursor.getString(passwordHashIndex)

                val salt = security.generateSalt()
                val hashPassword = security.passwordToHash(password.toCharArray(), salt)

                db.update(
                    "accounts",
                    SQLiteDatabase.CONFLICT_NONE,
                    contentValuesOf(
                        "salt" to security.bytesToString(salt),
                        "hashPassword" to security.bytesToString(hashPassword)
                    ),
                    "id = ?",
                    arrayOf(id)

                )
            }
        }
    }
}