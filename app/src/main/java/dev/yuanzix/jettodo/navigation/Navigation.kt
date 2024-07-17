package dev.yuanzix.jettodo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.yuanzix.jettodo.navigation.destination.listComposable
import dev.yuanzix.jettodo.navigation.destination.taskComposable
import dev.yuanzix.jettodo.ui.viewmodel.SharedViewModel
import dev.yuanzix.jettodo.util.Action

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
) {
    val screen = remember(navController) {
        Screens(navController)
    }

    NavHost(
        navController = navController,
        startDestination = ListScreen(name = Action.NoAction.name)
    ) {
        listComposable(
            navigateToTaskScreen = screen.goToTask,
            sharedViewModel = sharedViewModel,
        )
        taskComposable(
            sharedViewModel = sharedViewModel,
            navigateToListScreen = screen.goToList,
        )
    }
}