//package com.example.androidappproject.ui_classes
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.androidappproject.database.TripEvent
//import com.example.androidappproject.database.TripState
//
//@Composable
//fun AddEditAnimal(
//    navController: NavController,
//    state: TripState,
//    onEvent: (TripEvent) -> Unit,
//    isEditing: Boolean,
//    id: Int? = null
//
//) {
//    val choices = listOf("inny", "samolot", "autobus", "pociąg", "statek")
//    val (selectedOption, setSelectedOption) = remember { mutableStateOf(choices[0]) }
//    val ratings = listOf("1","2","3","4","5")
//    val (selectedRating, setSelectedRating) = remember { mutableStateOf(ratings[0]) }
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize()
//    ) { padding ->
//        // Оберните содержимое в Box для позиционирования кнопки закрытия
//        Box {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//            ){
//                Text(
//                    text = "Destination",
//                )
//                TextField(
//                    value = state.destination,
//                    onValueChange = {
//                        onEvent(TripEvent.SetDestination(it))
//                    },
//                    placeholder = { Text(text = "Destination") },
//                )
//                Text(
//                    text = "Duration",
//                )
//                TextField(
//                    value = state.duration.toString(),
//                    onValueChange = {
//                        val duration = it.toIntOrNull() ?: 0
//                        onEvent(TripEvent.SetDuration(duration))
//                    },
//                    placeholder = { Text(text = "duration") },
//                )
//                Text(
//                    text = "Price",
//                )
//                TextField(
//                    value = state.price.toString(),
//                    onValueChange = {
//                        val price = it.toIntOrNull() ?: 0
//                        onEvent(TripEvent.SetPrice(price))
//                    },
//                    placeholder = { Text(text = "price") },
//                )
//                Text(
//                    text = "Rating",
//                )
//                mySpinner(label ="rating", choices = ratings, selectedOption = selectedRating, setSelected = setSelectedRating, onEvent = onEvent)
//                Text(
//                    text = "Transport type",
//                )
//                mySpinner(label = "transport type", choices =choices , selectedOption = selectedOption, setSelected = setSelectedOption, onEvent = onEvent)
//            }
//            IconButton(
//                onClick = { onEvent(if (isEditing) TripEvent.HideEditDialog else TripEvent.HideDialog) },
//                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
//            ) {
//                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
//            }
//        }
//    }
//}