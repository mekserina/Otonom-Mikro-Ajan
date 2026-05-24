package com.otonom.mikroajan

import android.content.Context
import androidx.room.Room
import com.otonom.mikroajan.data.AppDatabase

object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "otonom_ajan_db"
            ).build()
            instance = db
            db
        }
    }
}
