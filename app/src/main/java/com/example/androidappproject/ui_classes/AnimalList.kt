package com.example.androidappproject.ui_classes

import android.widget.RatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.androidappproject.R
import com.example.androidappproject.database.AnimalEvent
import com.example.androidappproject.database.AnimalState
import com.example.androidappproject.entities.Animal
import kotlin.math.roundToInt


@Composable
fun AnimalList(state: AnimalState, onEvent: (AnimalEvent) -> Unit) {

    var selectedTripPosition by remember { mutableStateOf(-1) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(AnimalEvent.ShowDialog) })
            {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null)
            }
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        if (state.isAddingAnimal) {
            FullScreenDialog(state, onEvent, isEditing = false, onDismissRequest = { onEvent(AnimalEvent.HideDialog) })
        } else if (state.isEditingAnimal) {
            FullScreenDialog(state, onEvent, isEditing = true, id = selectedTripPosition, onDismissRequest = { onEvent(AnimalEvent.HideEditDialog) })
        }

        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "My Animals",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(state.animals) { animal ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedTripPosition = state.animals.indexOf(animal)
                            onEvent(AnimalEvent.ShowEditDialog)
                        }

                ){
                    Image(
                        painter = when(animal.animalType){
                            "cat" -> painterResource(id = R.drawable.cat)
                            "dog" -> painterResource(id = R.drawable.dog)
                            "crocodile" -> painterResource(id = R.drawable.crocodile)
                            "iguana" -> painterResource(id = R.drawable.iguana)
                            else -> painterResource(id = R.drawable.default_image)
                        },
                        contentDescription = "Transport type",
                        modifier = Modifier
                            .padding(end = 16.dp, bottom = 5.dp)
                            .size(50.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = "${animal.name}",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Id: ${animal.id}, Health: ${animal.health}",
                            fontSize = 12.sp,
                        )

                    }
                    IconButton(onClick = {
                        onEvent(AnimalEvent.DeleteAnimal(animal))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Animal"
                        )
                    }
                }
                Divider()
            }
        }
    }

}

@Composable
fun FullScreenDialog(
    state: AnimalState,
    onEvent: (AnimalEvent) -> Unit,
    isEditing: Boolean,
    id: Int? = null,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                if (isEditing) {
                    EditAnimal(state, onEvent, id)
                } else {
                    AddAnimal(state, onEvent)
                }
            }
        }
    }
}

@Composable
fun AddAnimal(state: AnimalState, onEvent: (AnimalEvent) -> Unit) {
    var selectedAnimal by remember { mutableStateOf(animalOptions.first()) }
    var ageSliderValue by remember { mutableStateOf(state.age.toFloat()) }
    var healthRating by remember { mutableStateOf(state.health.toFloat()) }
    val (vaccinated, setVaccinated) = remember { mutableStateOf(state.vaccinated) }

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        //--------------------------------Animal name--------------------------------
        Divider()
        Text(text = "Animal name", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        TextField(
            value = state.name,
            onValueChange = {
                onEvent(AnimalEvent.SetName(it))
            },
            placeholder = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth()
        )

        //--------------------------------AGE--------------------------------
        Divider()
        Text(text = "Age", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        Row {
            val ageRange = 0..20
            TextField(
                value = ageSliderValue.toInt().toString(),
                onValueChange = { newValue ->
                    val newAge = newValue.toIntOrNull()?.coerceIn(ageRange) ?: ageSliderValue.toInt()
                    ageSliderValue = newAge.toFloat()
                    onEvent(AnimalEvent.SetAge(newAge))
                },
                singleLine = true,
                modifier = Modifier.width(60.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Slider(
                value = ageSliderValue,
                onValueChange = { newValue ->
                    ageSliderValue = newValue
                    onEvent(AnimalEvent.SetAge(newValue.toInt()))
                },
                valueRange = ageRange.first.toFloat()..ageRange.last.toFloat()
            )
        }

        //--------------------------------Health--------------------------------
        Divider()
        Text(text = "Health", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        Row {
            Text(text = "${healthRating}",
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .padding(16.dp)
            )
            Slider(
                value = healthRating,
                onValueChange = { newRating ->
                    healthRating = (newRating * 2).roundToInt() / 2f
                    onEvent(AnimalEvent.SetHealth(healthRating.toInt()))
                },
                valueRange = 0f..5f,
                modifier = Modifier.padding(start = 10.dp),
                steps = 9
            )
        }
        //--------------------------------Vaccinated--------------------------------
        Divider()
        Text(text = "Vaccinated", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        Row {
            val isVaccinated = vaccinated ?: false
            RadioButton(
                selected = isVaccinated,
                onClick = { setVaccinated(true)
                    onEvent(AnimalEvent.SetVaccinated(true)) }
            )
            Text(text = "Yes")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(
                selected = !isVaccinated,
                onClick = { setVaccinated(false)
                    onEvent(AnimalEvent.SetVaccinated(false))}
            )
            Text(text = "No")
        }
        //--------------------------------Animal Type--------------------------------
        Divider()
        Text(text = "Animal Type", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold)
        )
        AnimalDropdownMenu(
            options = animalOptions,
            selectedOption = selectedAnimal,
            onOptionSelected = { option ->
                selectedAnimal = option
                onEvent(AnimalEvent.SetAnimalType(option.name))
            }
        )
        //--------------------------------FAB--------------------------------
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd)
        {
            FloatingActionButton(
                onClick = { onEvent(AnimalEvent.saveAnimal) },
                modifier = Modifier.padding(16.dp)) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Confirm")
            }
        }
    }
}



@Composable
fun EditAnimal(state: AnimalState, onEvent: (AnimalEvent) -> Unit, id: Int?) {
    if (id == null || id !in state.animals.indices) {
        Text("Invalid Animal ID")
        return
    }
    val animalToEdit = state.animals[id]

    var name by remember { mutableStateOf(animalToEdit.name ?: "") }
    var age by remember { mutableStateOf(animalToEdit.age ?: 0) }
    var health by remember { mutableStateOf(animalToEdit.health?.toFloat() ?: 0.0) }
    var vaccinated by remember { mutableStateOf(animalToEdit.vaccinated ?: false) }
    var selectedAnimal by remember { mutableStateOf(animalOptions.find { it.name == animalToEdit.animalType } ?: animalOptions.first()) }

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        //--------------------------------Animal name--------------------------------
        Divider()
        Text(text = "Animal name", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        TextField(
            value = name,
            onValueChange = {
                name = it
                onEvent(AnimalEvent.SetName(it))
            },
            placeholder = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth()
        )

        //--------------------------------AGE--------------------------------
        Divider()
        Text(text = "Age", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        Row {
            val ageRange = 0..20
            TextField(
                value = age.toString(),
                onValueChange = { newValue ->
                    age = newValue.toIntOrNull()?.coerceIn(ageRange) ?: age
                    onEvent(AnimalEvent.SetAge(age))
                },
                singleLine = true,
                modifier = Modifier.width(60.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Slider(
                value = age.toFloat(),
                onValueChange = { newValue ->
                    age = newValue.toInt()
                    onEvent(AnimalEvent.SetAge(age))
                },
                valueRange = ageRange.first.toFloat()..ageRange.last.toFloat()
            )
        }

        //--------------------------------Health--------------------------------
        Divider()
        Text(text = "Health", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        Row {
            Text(text = "${health}",
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .padding(16.dp)
            )
            Slider(
                value = health.toFloat(),
                onValueChange = { newRating ->
                    health = (newRating * 2).roundToInt() / 2f
                    onEvent(AnimalEvent.SetHealth(health.toInt()))
                },
                valueRange = 0f..5f,
                modifier = Modifier.padding(start = 10.dp),
                steps = 9
            )
        }

        //--------------------------------Vaccinated--------------------------------
        Divider()
        Text(text = "Vaccinated", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        Row {
            RadioButton(
                selected = vaccinated,
                onClick = {
                    vaccinated = true
                    onEvent(AnimalEvent.SetVaccinated(vaccinated))
                }
            )
            Text(text = "Yes")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(
                selected = !vaccinated,
                onClick = {
                    vaccinated = false
                    onEvent(AnimalEvent.SetVaccinated(vaccinated))
                }
            )
            Text(text = "No")
        }

        //--------------------------------Animal Type--------------------------------
        Divider()
        Text(text = "Animal Type", style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold))
        AnimalDropdownMenu(
            options = animalOptions,
            selectedOption = selectedAnimal,
            onOptionSelected = { option ->
                selectedAnimal = option
                onEvent(AnimalEvent.SetAnimalType(option.name))
            }
        )

        //--------------------------------FAB--------------------------------
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { onEvent(AnimalEvent.EditAnimal(state.animals[id])) },
                modifier = Modifier.padding(16.dp))
            { Icon(imageVector = Icons.Default.Check, contentDescription = "Confirm") }
        }
    }
}



@Composable
fun AnimalDropdownMenu(
    options: List<AnimalOption>,
    selectedOption: AnimalOption,
    onOptionSelected: (AnimalOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = selectedOption.imageResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = selectedOption.name, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.width(150.dp)
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = option.imageResId),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option.name)
                    }
                },
                onClick = {
                    onOptionSelected(option)
                    expanded = false
                }
            )
        }
    }
}

val animalOptions = listOf(
    AnimalOption("inny", R.drawable.default_image),
    AnimalOption("cat", R.drawable.cat),
    AnimalOption("dog", R.drawable.dog),
    AnimalOption("crocodile", R.drawable.crocodile),
    AnimalOption("iguana", R.drawable.iguana)
)

data class AnimalOption(
    val name: String,
    val imageResId: Int
)




