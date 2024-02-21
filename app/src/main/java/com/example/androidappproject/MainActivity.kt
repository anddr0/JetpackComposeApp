package com.example.androidappproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidappproject.ui.theme.AppTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.androidappproject.database.AnimalDatabase
import com.example.androidappproject.ui_classes.Home
import com.example.androidappproject.ui_classes.Settings
import com.example.androidappproject.ui_classes.AnimalList
//import com.example.androidappproject.ui_classes.AddEditAnimal
import com.example.androidappproject.viewmodels.MainViewModel
import com.example.androidappproject.viewmodels.AnimalViewModel
import dagger.hilt.android.AndroidEntryPoint


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
)


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AnimalDatabase::class.java,
            "trips.db"
        ).build()
    }

    private val dbViewModel by viewModels<AnimalViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AnimalViewModel(db.dao) as T
                }
            }
        }
    )
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val state by dbViewModel.state.collectAsState()
                val navController = rememberNavController()
                val items = listOf<NavigationItem>(
                    NavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Filled.Home,
                        route = "home",
                    ),
                    NavigationItem(
                        title = "My Lovely Animals",
                        selectedIcon = Icons.Filled.List,
                        unselectedIcon = Icons.Filled.List,
                        route = "animallist",
                    ),
                    NavigationItem(
                        title = "Profile",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Filled.Settings,
                        route = "profile",
                    ),
                )
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var selectedIdemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Spacer(modifier = Modifier.height(20.dp))
                            Row (verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.animal_ins),
                                    contentDescription = "",
                                    modifier = Modifier.size(150.dp)
                                )

                                Text("made by\nDrobitko Andrii", fontSize = 20.sp, modifier = Modifier
                                    .padding(top = 20.dp))
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            items.forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    label = { Text(item.title) },
                                    selected = index == selectedIdemIndex,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo("home") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        selectedIdemIndex = index
                                        scope.launch { drawerState.close() }
                                    },
                                    icon = {
                                        Icon(imageVector = if (index == selectedIdemIndex) item.selectedIcon else item.unselectedIcon,
                                            contentDescription =  item.title)
                                    },
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }
                    },
                    drawerState = drawerState
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text(
                                        "Animal inspector",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                },
                            )
                        },
                    )
                    {
                        NavHost(navController = navController, startDestination = "home") {
                            composable(Screen.Home.route) { Home(navController, mainViewModel) }
                            composable(Screen.TravelList.route) { AnimalList(state = state, onEvent= dbViewModel::onEvent) }
                            composable(Screen.Settings.route) { Settings(navController, mainViewModel) }
                        }
                    }
                }
            }

        }
    }
}


