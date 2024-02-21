package com.example.androidappproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.androidappproject.entities.Animal
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {
    @Upsert
    suspend fun upsertAnimal(animal: Animal)

    @Delete
    suspend fun deleteAnimal(animal: Animal)

    @Query("SELECT * FROM Animal")
    fun getAllAnimals(): Flow<List<Animal>>
}