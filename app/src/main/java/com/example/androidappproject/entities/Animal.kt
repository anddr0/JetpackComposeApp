package com.example.androidappproject.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Animal(
    var name: String? = "Brak",
    var age: Int? = 0,
    var health: Int? = 0,
    var vaccinated: Boolean? = false,
    var animalType: String? = "inny",
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)