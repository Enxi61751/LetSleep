package com.letsleep.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.letsleep.app.data.LetSleepViewModel
import com.letsleep.app.ui.screens.home.HomeScreen
import com.letsleep.app.ui.screens.review.ReviewScreen
import com.letsleep.app.ui.screens.settings.SettingsScreen
import com.letsleep.app.ui.screens.todos.TodoScreen

@Composable
fun LetSleepNavHost(
    navController: NavHostController,
    viewModel: LetSleepViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home,
        modifier = modifier,
    ) {
        composable(Routes.Home) {
            HomeScreen(
                viewModel = viewModel,
                onOpenTodos = { navController.navigate(Routes.Todos) },
                onOpenReview = { navController.navigate(Routes.Review) },
            )
        }
        composable(Routes.Todos) {
            TodoScreen(viewModel = viewModel)
        }
        composable(Routes.Review) {
            ReviewScreen(viewModel = viewModel)
        }
        composable(Routes.Settings) {
            SettingsScreen(viewModel = viewModel)
        }
    }
}
