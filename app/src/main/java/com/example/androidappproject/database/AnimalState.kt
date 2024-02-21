package com.example.androidappproject.database

import com.example.androidappproject.entities.Animal

data class AnimalState(
    val animals: List<Animal> = emptyList(),

    val name: String = "",
    val age: Int = 0,
    val health: Int = 0,
    var vaccinated: Boolean? = false,
    var animalType: String? = "inny",
    val isAddingAnimal: Boolean = false,
    val isEditingAnimal: Boolean = false,
)