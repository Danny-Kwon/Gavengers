package com.example.gavengers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DeviceData::class], version = 2)
abstract class DeviceDatabase : RoomDatabase(){
    abstract fun deviceDao() : DeviceDao

    companion object{
        private var database: DeviceDatabase? = null
        private const val ROOM_DB = "room.db"

        fun getDatabase(context: Context): DeviceDatabase{
            if (database == null){
                database = Room.databaseBuilder(
                    context.applicationContext, DeviceDatabase::class.java, ROOM_DB)
                    .allowMainThreadQueries()
                    .build()
            }
            return database!!
        }
    }
}