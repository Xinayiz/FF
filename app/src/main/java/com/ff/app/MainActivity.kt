package com.ff.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ff.app.ui.theme.FFTheme
import com.ff.app.ui.home.HomeScreen
import com.ff.app.ui.home.HomeViewModel
import com.ff.app.ui.subscriptions.SubscriptionsScreen
import com.ff.app.ui.settings.SettingsScreen
import com.ff.app.ui.navigation.BottomNavItem

class MainActivity : ComponentActivity() {
    private lateinit var homeViewModel: HomeViewModel

    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            homeViewModel.actuallyStartVpn()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = viewModel()
        setContent {
            FFTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    androidx.navigation.compose.NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(BottomNavItem.Home.route) { HomeScreen(homeViewModel) }
                        composable(BottomNavItem.Subscriptions.route) { SubscriptionsScreen() }
                        composable(BottomNavItem.Settings.route) { SettingsScreen() }
                    }
                }
            }
        }
        homeViewModel.vpnPermissionLauncher = vpnPermissionLauncher
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) { launchSingleTop = true } },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
