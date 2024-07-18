package dev.yuanzix.jettodo.navigation.destination

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.yuanzix.jettodo.navigation.ListScreen
import dev.yuanzix.jettodo.ui.screen.list.ListScreen
import dev.yuanzix.jettodo.ui.viewmodel.SharedViewModel
import dev.yuanzix.jettodo.util.Action
import dev.yuanzix.jettodo.util.toAction

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    composable<ListScreen> {
        val action = it.toRoute<ListScreen>().name.toAction()
        var myAction by rememberSaveable {
            mutableStateOf(Action.NoAction)
        }

        LaunchedEffect(key1 = action) {
            if (action != myAction) {
                myAction = action
                sharedViewModel.action.value = action
            }
        }

        val databaseAction by sharedViewModel.action

        ListScreen(
            databaseAction = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )
    }
}
