package dev.yuanzix.jettodo.ui.screen.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import dev.yuanzix.jettodo.R
import dev.yuanzix.jettodo.component.DisplayAlertDialog
import dev.yuanzix.jettodo.data.model.ToDoTask
import dev.yuanzix.jettodo.ui.theme.customTopAppBarColors
import dev.yuanzix.jettodo.util.Action

@Composable
fun TaskAppBar(
    navigateToListScreen: (Action) -> Unit,
    selectedTask: ToDoTask?,
) {
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListScreen)
    } else {
        ExistingTaskAppBar(selectedTask, navigateToListScreen)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit,
) {
    TopAppBar(navigationIcon = {
        CloseAction(navigateToListScreen)
    }, title = {
        Text(text = stringResource(id = R.string.add_todo))
    }, colors = TopAppBarDefaults.customTopAppBarColors, actions = {
        AddAction(navigateToListScreen)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit,
) {
    TopAppBar(navigationIcon = {
        CloseAction(navigateToListScreen)
    }, title = {
        Text(
            text = selectedTask.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }, colors = TopAppBarDefaults.customTopAppBarColors, actions = {
        ExistingAppBarActions(
            selectedTask, navigateToListScreen
        )
    })
}

@Composable
private fun ExistingAppBarActions(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit,
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_task, selectedTask.title),
        message = stringResource(id = R.string.delete_task_confirmation, selectedTask.title),
        openDialog = openDialog,
        onDismiss = { openDialog = false },
        onConfirm = { navigateToListScreen(Action.Delete) }
    )

    DeleteAction { openDialog = true }
    UpdateAction(navigateToListScreen)
}

@Composable
fun DeleteAction(
    onDeleteClicked: (Action) -> Unit,
) {
    IconButton(onClick = { onDeleteClicked(Action.Delete) }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_button),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit,
) {
    IconButton(onClick = { onUpdateClicked(Action.Update) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.update_button),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit,
) {
    IconButton(onClick = { onCloseClicked(Action.NoAction) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.close_button),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit,
) {
    IconButton(onClick = { onAddClicked(Action.Add) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.add_todo),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
