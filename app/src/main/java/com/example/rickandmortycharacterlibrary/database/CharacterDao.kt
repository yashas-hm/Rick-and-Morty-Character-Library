package com.example.rickandmortycharacterlibrary.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rickandmortycharacterlibrary.models.Characters

@Dao
interface CharacterDao {
    @Insert
    fun insertIntoDatabase(characters: Characters)

    @Query("SELECT * FROM Characters")
    fun getAll(): List<Characters>

    @Query("DELETE FROM Characters ")
    fun deleteAll()

    @Query("SELECT * FROM Characters")
    fun getLiveData(): LiveData<List<Characters>>
}