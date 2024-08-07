package dev.yuanzix.jettodo.ui.screen.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.yuanzix.jettodo.data.model.ToDoTask
import dev.yuanzix.jettodo.ui.viewmodel.SharedViewModel
import dev.yuanzix.jettodo.util.Action

@Composable
fun TaskScreen(
    navigateToListScreen: (Action) -> Unit,
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
) {
    val context = LocalContext.current

    BackHandler {
        navigateToListScreen(Action.NoAction)
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                navigateToListScreen = {
                    if (it == Action.NoAction) {
                        navigateToListScreen(it)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(it)
                        } else {
                            displayToast(context = context)
                        }
                    }
                },
                selectedTask = selectedTask,
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            TaskContent(
                title = sharedViewModel.title.value,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = sharedViewModel.description.value,
                onDescriptionChange = {
                    sharedViewModel.description.value = it
                },
                priority = sharedViewModel.priority.value,
                onPriorityChange = {
                    sharedViewModel.priority.value = it
                },
            )
        }
    }
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Fields cannot be empty",
        Toast.LENGTH_SHORT
    ).show()
}
