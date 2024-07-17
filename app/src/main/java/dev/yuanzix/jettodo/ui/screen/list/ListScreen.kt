package dev.yuanzix.jettodo.ui.screen.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.yuanzix.jettodo.R
import dev.yuanzix.jettodo.ui.theme.MEDIUM_PADDING
import dev.yuanzix.jettodo.ui.viewmodel.SharedViewModel
import dev.yuanzix.jettodo.util.Action
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(
        snackbarHostState = snackbarHostState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(sharedViewModel.action.value) },
        onUndoClicked = { sharedViewModel.action.value = it },
        taskTitle = sharedViewModel.title.value,
        action = sharedViewModel.action.value
    )
    Scaffold(
        topBar = { ListAppBar(sharedViewModel) },
        floatingActionButton = { ListFab(navigateToTaskScreen) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column {
                Spacer(modifier = Modifier.height(MEDIUM_PADDING))
                ListContent(
                    allTasks = sharedViewModel.allTasks.collectAsState().value,
                    searchedTasks = sharedViewModel.searchedTasks.collectAsState().value,
                    lowPriorityTasks = sharedViewModel.lowPriorityTasks.collectAsState().value,
                    highPriorityTasks = sharedViewModel.highPriorityTasks.collectAsState().value,
                    sortOrder = sharedViewModel.sortOrder.collectAsState().value,
                    searchAppBarState = sharedViewModel.searchAppBarState.value,
                    navigateToTaskScreen = navigateToTaskScreen,
                )
            }
        }
    }
}

@Composable
private fun ListFab(
    onFabClicked: (taskId: Int) -> Unit,
) {
    FloatingActionButton(
        onClick = { onFabClicked(-1) },
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = stringResource(id = R.string.add_button),
        )
    }
}

@Composable
fun DisplaySnackBar(
    snackbarHostState: SnackbarHostState,
    handleDatabaseActions: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action,
) {
    handleDatabaseActions()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NoAction) {
            scope.launch {
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = setMessage(
                        action = action, taskTitle = taskTitle
                    ),
                    actionLabel = setActionLabel(action),
                    duration = SnackbarDuration.Short,
                )
                undoDeletedTask(
                    action = action,
                    snackbarResult = snackbarResult,
                    onUndoClicked = onUndoClicked,
                )
            }
        }
    }
}

private fun setMessage(
    action: Action,
    taskTitle: String,
): String {
    return when (action) {
        Action.Add -> "Added: $taskTitle"
        Action.Update -> "Updated: $taskTitle"
        Action.Delete -> "Deleted: $taskTitle"
        Action.DeleteAll -> "Deleted all tasks"
        else -> ""
    }
}

private fun setActionLabel(action: Action): String {
    return when (action) {
        Action.Delete -> "Undo"
        else -> "Ok"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackbarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit,
) {
    if (snackbarResult == SnackbarResult.ActionPerformed && action == Action.Delete) {
        onUndoClicked(Action.Undo)
    }
}