package com.example.androidappproject

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object TravelList : Screen("animallist")
    object Settings : Screen("profile")
}
