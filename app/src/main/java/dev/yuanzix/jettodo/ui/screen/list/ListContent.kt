package dev.yuanzix.jettodo.ui.screen.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import dev.yuanzix.jettodo.data.model.Priority
import dev.yuanzix.jettodo.data.model.ToDoTask
import dev.yuanzix.jettodo.ui.theme.LARGE_PADDING
import dev.yuanzix.jettodo.ui.theme.PRIORITY_INDICATOR_SIZE
import dev.yuanzix.jettodo.ui.theme.SMALL_PADDING
import dev.yuanzix.jettodo.ui.theme.VERY_SMALL_PADDING
import dev.yuanzix.jettodo.util.RequestState
import dev.yuanzix.jettodo.util.SearchAppBarState

@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortOrder: RequestState<Priority>,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen: (taskId: Int) -> Unit,
) {
    if (sortOrder is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.Triggered -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        searchedTasks.data,
                        navigateToTaskScreen,
                    )
                }
            }

            sortOrder.data == Priority.None -> {
                if (allTasks is RequestState.Success) {
                    HandleListContent(
                        allTasks.data, navigateToTaskScreen
                    )
                }
            }

            sortOrder.data == Priority.Low -> {
                HandleListContent(
                    lowPriorityTasks,
                    navigateToTaskScreen,
                )
            }

            sortOrder.data == Priority.High -> {
                HandleListContent(
                    highPriorityTasks,
                    navigateToTaskScreen,
                )
            }

        }
    }
}

@Composable
fun HandleListContent(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        ListToDos(tasks, navigateToTaskScreen)
    }
}

@Composable
private fun ListToDos(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
) {
    LazyColumn {
        items(items = tasks, key = { task -> task.id }) {
            ToDoItem(
                toDoTask = it,
                navigateToTaskScreen = navigateToTaskScreen,
            )
        }
    }
}

@Composable
fun ToDoItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SMALL_PADDING, horizontal = LARGE_PADDING)
            .clickable { navigateToTaskScreen(toDoTask.id) },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Column(
            modifier = Modifier
                .padding(2 * LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = toDoTask.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
                    drawCircle(color = toDoTask.priority.color)
                }
            }
            Spacer(modifier = Modifier.height(VERY_SMALL_PADDING))
            Text(
                text = toDoTask.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
