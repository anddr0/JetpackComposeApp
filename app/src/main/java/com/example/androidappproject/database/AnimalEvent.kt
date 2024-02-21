package com.example.androidappproject.database

import com.example.androidappproject.entities.Animal

sealed interface AnimalEvent{
    data class SetName(val name: String): AnimalEvent
    data class SetAge(val age: Int): AnimalEvent
    data class SetHealth(val health: Int): AnimalEvent
    data class SetVaccinated(val vaccinated: Boolean): AnimalEvent
    data class SetAnimalType(val animalType: String): AnimalEvent

    object saveAnimal: AnimalEvent

    data class EditAnimal(val animal: Animal): AnimalEvent
    data class DeleteAnimal(val animal: Animal): AnimalEvent

    object ShowDialog: AnimalEvent
    object HideDialog: AnimalEvent
    object HideEditDialog: AnimalEvent
    object ShowEditDialog: AnimalEvent
}