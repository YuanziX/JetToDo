package dev.yuanzix.jettodo.navigation.destination

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.yuanzix.jettodo.navigation.ListScreen
import dev.yuanzix.jettodo.ui.screen.list.ListScreen
import dev.yuanzix.jettodo.ui.viewmodel.SharedViewModel
import dev.yuanzix.jettodo.util.toAction

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    composable<ListScreen> {
        val action = it.toRoute<ListScreen>().name.toAction()

        LaunchedEffect(key1 = action) {
            sharedViewModel.action.value = action
        }

        val databaseAction by sharedViewModel.action

        ListScreen(
            databaseAction = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )
    }
}
