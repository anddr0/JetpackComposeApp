package com.example.androidappproject.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidappproject.database.AnimalDao
import com.example.androidappproject.database.AnimalEvent
import com.example.androidappproject.database.AnimalState
import com.example.androidappproject.entities.Animal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimalViewModel(private val dao: AnimalDao): ViewModel() {

    private val animals_state = MutableStateFlow(AnimalState())
    private val animals_list = dao.getAllAnimals().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(animals_state, animals_list) { state, animals ->
        state.copy(animals = animals)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnimalState())

    fun onEvent(event: AnimalEvent) {
        when(event) {
            is AnimalEvent.DeleteAnimal -> {
                viewModelScope.launch {
                    dao.deleteAnimal(event.animal)
                }
            }
            AnimalEvent.HideDialog -> {
                animals_state.update {
                    it.copy(isAddingAnimal = false)
                }
            }
            is AnimalEvent.SetName -> {
                animals_state.update {
                    it.copy(name = event.name)
                }
            }
            is AnimalEvent.SetAge -> {
                animals_state.update {
                    it.copy(age = event.age)
                }
            }
            is AnimalEvent.SetHealth -> {
                animals_state.update {
                    it.copy(health = event.health)
                }

            }
            is AnimalEvent.SetVaccinated -> {
                animals_state.update {
                    it.copy(vaccinated = event.vaccinated)
                }

            }
            is AnimalEvent.SetAnimalType -> {
                animals_state.update {
                    it.copy(animalType = event.animalType)
                }
            }
            AnimalEvent.ShowDialog -> {
                animals_state.update {
                    it.copy(isAddingAnimal = true)
                }
            }
            AnimalEvent.ShowEditDialog -> {
                animals_state.update {
                    it.copy(isEditingAnimal = true)
                }
            }
            is AnimalEvent.EditAnimal -> {
                val selectedAnimal = event.animal

                val existingAnimal = animals_list.value.find { it == selectedAnimal }

                if (existingAnimal != null) {
                    if(state.value.name != "")
                        existingAnimal.name = state.value.name
                    if(state.value.age != 0)
                        existingAnimal.age = state.value.age
                    if(state.value.health != 0)
                        existingAnimal.health = state.value.health
                    if(state.value.vaccinated != false)
                        existingAnimal.vaccinated = state.value.vaccinated
                    if(state.value.animalType != "inny")
                        existingAnimal.animalType = state.value.animalType

                    viewModelScope.launch {
                        dao.upsertAnimal(existingAnimal)
                    }
                }
                animals_state.update {
                    it.copy(
                        name = "",
                        age = 0,
                        health = 0,
                        vaccinated = false,
                        animalType = "inny",
                        isAddingAnimal = false,
                        isEditingAnimal = false,
                    )
                }
            }

            AnimalEvent.HideEditDialog -> {
                animals_state.update {
                    it.copy(isEditingAnimal = false)
                }
            }
            AnimalEvent.saveAnimal -> {
                val name = state.value.name
                val age = state.value.age
                val health = state.value.health
                val vaccinated = state.value.vaccinated
                val animalType = state.value.animalType

                val animal = Animal(name, age, health, vaccinated, animalType)
                viewModelScope.launch {
                    dao.upsertAnimal(animal)
                }
                animals_state.update {
                    it.copy(
                        name = "",
                        age = 0,
                        health = 0,
                        vaccinated = false,
                        animalType = "inny",
                        isAddingAnimal = false,
                        isEditingAnimal = false,
                    )
                }
            }

        }
    }
}