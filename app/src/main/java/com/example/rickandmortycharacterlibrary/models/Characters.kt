package com.example.rickandmortycharacterlibrary.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Characters")
data class Characters(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "species")
    val species: String,
    @ColumnInfo(name = "alive")
    val alive: String,
    @ColumnInfo(name = "location")
    val location: String,
)
