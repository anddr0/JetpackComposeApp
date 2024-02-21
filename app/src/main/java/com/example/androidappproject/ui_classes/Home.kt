package com.example.androidappproject.ui_classes


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidappproject.viewmodels.MainViewModel
import com.example.androidappproject.R

@Composable
fun Home(navController: NavController, mainViewModel: MainViewModel){
    val username by mainViewModel.username.collectAsState(initial = "Andrii Drobitko :)")
    val description by mainViewModel.description.collectAsState(initial = "Android animal inspector project")
    val profilePicture by mainViewModel.profilePicture.collectAsState(initial = "1")

    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()

    ){
        Text(text = username)
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = when (profilePicture.toInt() + 1) {
                1 -> painterResource(id = R.drawable.p1)
                2 -> painterResource(id = R.drawable.p2)
                3 -> painterResource(id = R.drawable.p3)
                4 -> painterResource(id = R.drawable.p4)
                5 -> painterResource(id = R.drawable.p5)
                else -> painterResource(id = R.drawable.p1)
            },
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(300.dp)
                .width(500.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = description)
        }

}
