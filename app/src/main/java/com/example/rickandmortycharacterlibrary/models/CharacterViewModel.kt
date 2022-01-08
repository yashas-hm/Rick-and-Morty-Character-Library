package com.example.rickandmortycharacterlibrary.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rickandmortycharacterlibrary.database.CharacterDatabase

class CharacterViewModel(application: Application) : AndroidViewModel(application) {

    private val database: CharacterDatabase = CharacterDatabase.getDatabase(application.applicationContext)

    fun getAllCharacters(): LiveData<List<Characters>>{
        return database.characterDao().getLiveData()
    }

    fun enterAllCharacters(list: ArrayList<Characters>){
        for( i in 0 until list.size){
            database.characterDao().insertIntoDatabase(list[i])
        }
    }

    fun deleteDatabase(){
        database.characterDao().deleteAll()
    }
}