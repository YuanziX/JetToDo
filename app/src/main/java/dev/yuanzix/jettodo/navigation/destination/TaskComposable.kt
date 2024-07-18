package dev.yuanzix.jettodo.navigation.destination

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.yuanzix.jettodo.navigation.TaskScreen
import dev.yuanzix.jettodo.ui.screen.task.TaskScreen
import dev.yuanzix.jettodo.ui.viewmodel.SharedViewModel
import dev.yuanzix.jettodo.util.Action

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit,
) {
    composable<TaskScreen>(
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 400)
            )
        }
    ) {
        val taskId = it.toRoute<TaskScreen>().id

        LaunchedEffect(key1 = taskId) {
            sharedViewModel.getSelectedTask(taskId)
        }

        val selectedTask by sharedViewModel.selectedTask.collectAsState()

        LaunchedEffect(key1 = selectedTask) {
            if (selectedTask != null || taskId == -1) {
                sharedViewModel.updateTaskFields(selectedTask)
            }
        }

        TaskScreen(
            navigateToListScreen = navigateToListScreen,
            selectedTask = selectedTask,
            sharedViewModel = sharedViewModel
        )
    }
}