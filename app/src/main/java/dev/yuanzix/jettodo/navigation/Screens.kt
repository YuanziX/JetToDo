package dev.yuanzix.jettodo.navigation

import androidx.navigation.NavHostController
import dev.yuanzix.jettodo.util.Action
import kotlinx.serialization.Serializable

class Screens(navController: NavHostController) {
    val goToList: (Action) -> Unit = {
        navController.navigate(ListScreen(name = it.name)) {
            popUpTo(ListScreen(name = Action.NoAction.name)) { inclusive = true }
        }
    }

    val goToTask: (Int) -> Unit = {
        navController.navigate(TaskScreen(id = it))
    }
}

@Serializable
data class ListScreen(
    val name: String,
)

@Serializable
data class TaskScreen(
    val id: Int,
)