package dev.yuanzix.jettodo.ui.screen.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import dev.yuanzix.jettodo.R
import dev.yuanzix.jettodo.data.model.Priority
import dev.yuanzix.jettodo.data.model.ToDoTask
import dev.yuanzix.jettodo.ui.theme.LARGE_PADDING
import dev.yuanzix.jettodo.ui.theme.MEDIUM_PADDING
import dev.yuanzix.jettodo.ui.theme.PRIORITY_INDICATOR_SIZE
import dev.yuanzix.jettodo.ui.theme.SMALL_PADDING
import dev.yuanzix.jettodo.ui.theme.VERY_LARGE_PADDING
import dev.yuanzix.jettodo.ui.theme.VERY_SMALL_PADDING
import dev.yuanzix.jettodo.util.Action
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
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
) {
    if (sortOrder is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.Triggered -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen,
                    )
                }
            }

            sortOrder.data == Priority.None -> {
                if (allTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = allTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen,
                    )
                }
            }

            sortOrder.data == Priority.Low -> {
                HandleListContent(
                    tasks = lowPriorityTasks,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen,
                )
            }

            sortOrder.data == Priority.High -> {
                HandleListContent(
                    tasks = highPriorityTasks,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen,
                )
            }

        }
    }
}

@Composable
fun HandleListContent(
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        ListToDos(
            tasks = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTaskScreen = navigateToTaskScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListToDos(
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
) {
    LazyColumn {
        items(items = tasks, key = { task -> task.id }) {
            ToDoItem(
                toDoTask = it,
                navigateToTaskScreen = navigateToTaskScreen,
                onSwipeToDelete = onSwipeToDelete,
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = {
        if (it == SwipeToDismissBoxValue.EndToStart) {
            onSwipeToDelete(Action.Delete, toDoTask)
        }
        true
    })

    SwipeToDismissBox(state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> Color.Red
                else -> Color.Transparent
            }

            val angle = animateFloatAsState(
                targetValue = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) -30f else 0f,
                label = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = SMALL_PADDING, horizontal = LARGE_PADDING),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    shape = RoundedCornerShape(MEDIUM_PADDING),
                    color = color,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = VERY_LARGE_PADDING),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.rotate(angle.value),
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete_button),
                            tint = Color.White,
                        )
                    }
                }
            }
        },
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = SMALL_PADDING, horizontal = LARGE_PADDING)
                    .clickable { navigateToTaskScreen(toDoTask.id) },
                shape = RoundedCornerShape(MEDIUM_PADDING),
                color = MaterialTheme.colorScheme.secondaryContainer,
            ) {
                Column(
                    modifier = Modifier
                        .padding(VERY_LARGE_PADDING)
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
        })
}
