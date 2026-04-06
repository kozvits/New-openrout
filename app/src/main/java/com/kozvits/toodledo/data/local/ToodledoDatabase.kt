package com.kozvits.toodledo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kozvits.toodledo.data.local.dao.ContextDao
import com.kozvits.toodledo.data.local.dao.FolderDao
import com.kozvits.toodledo.data.local.dao.TaskDao
import com.kozvits.toodledo.data.local.entities.ContextEntity
import com.kozvits.toodledo.data.local.entities.FolderEntity
import com.kozvits.toodledo.data.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class, FolderEntity::class, ContextEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ToodledoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun folderDao(): FolderDao
    abstract fun contextDao(): ContextDao

    companion object {
        @Volatile private var INSTANCE: ToodledoDatabase? = null

        fun getInstance(context: android.content.Context): ToodledoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    ToodledoDatabase::class.java,
                    "toodledo.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
