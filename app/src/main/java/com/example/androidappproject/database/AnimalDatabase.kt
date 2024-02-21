package com.example.androidappproject.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidappproject.entities.Animal

@Database(
    entities = [Animal::class],
    version = 1
)

abstract class AnimalDatabase: RoomDatabase() {
    abstract val dao: AnimalDao
}