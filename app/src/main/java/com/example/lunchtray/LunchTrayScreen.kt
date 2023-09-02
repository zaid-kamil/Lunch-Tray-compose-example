package com.example.lunchtray

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

// TODO: Screen enum
enum class NavScreen() {
    Home,
    Entree,
    SideDish,
    Accompaniment,
    Checkout
}

// TODO: AppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchAppBar() {
    CenterAlignedTopAppBar(title = {
        Text(stringResource(R.string.app_name))
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp() {
    // TODO: Create Controller and initialization
    val navController = rememberNavController()
    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = { LunchAppBar() }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        // navigation host
        NavHost(
            navController = navController,
            startDestination = NavScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavScreen.Home.name) {
                StartOrderScreen(onStartOrderButtonClicked = {
                    navController.navigate(NavScreen.Entree.name)
                })
            }
            composable(NavScreen.Entree.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = {
                        navController.navigateUp()
                    },
                    onNextButtonClicked = {
                        navController.navigate(NavScreen.SideDish.name)
                    },
                    onSelectionChanged = {
                        viewModel.updateEntree(it)
                    },
                )
            }
            composable(NavScreen.SideDish.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = { navController.navigateUp() },
                    onNextButtonClicked = {
                        navController.navigate(NavScreen.Accompaniment.name)
                    },
                    onSelectionChanged = {
                        viewModel.updateSideDish(it)
                    },
                )
            }
            composable(NavScreen.Accompaniment.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = { navController.navigateUp() },
                    onNextButtonClicked = {
                        navController.navigate(NavScreen.Checkout.name)
                    },
                    onSelectionChanged = {
                        viewModel.updateAccompaniment(it)
                    },
                )
            }
            composable(NavScreen.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = {
                        // todo navigate to confirmation screen
                    },
                    onCancelButtonClicked = {
                        navController.navigateUp()
                    })
            }
        }

    }
}