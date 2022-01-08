package com.example.rickandmortycharacterlibrary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rickandmortycharacterlibrary.models.Characters

@Database(entities = [Characters::class], version = 1)
abstract  class CharacterDatabase: RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object{
        @Volatile
        private var INSTANCE: CharacterDatabase? = null

        fun getDatabase(context: Context): CharacterDatabase {
            return INSTANCE ?:synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CharacterDatabase::class.java,
                    "Character_Database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}