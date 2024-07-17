package dev.yuanzix.jettodo.ui.screen.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import dev.yuanzix.jettodo.R
import dev.yuanzix.jettodo.component.DisplayAlertDialog
import dev.yuanzix.jettodo.component.PriorityItem
import dev.yuanzix.jettodo.data.model.Priority
import dev.yuanzix.jettodo.ui.theme.TOP_APP_BAR_HEIGHT
import dev.yuanzix.jettodo.ui.theme.customTopAppBarColors
import dev.yuanzix.jettodo.ui.viewmodel.SharedViewModel
import dev.yuanzix.jettodo.util.Action
import dev.yuanzix.jettodo.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
) {
    when (sharedViewModel.searchAppBarState.value) {
        SearchAppBarState.Closed -> DefaultListAppBar(
            onSearchClicked = {
                sharedViewModel.searchAppBarState.value = SearchAppBarState.Opened
            },
            onSortClicked = { sharedViewModel.persistSortOrder(it) },
            onDeleteAllConfirmed = { sharedViewModel.action.value = Action.DeleteAll },
        )

        else -> SearchAppBar(
            text = sharedViewModel.searchTextState.value,
            onTextChange = { sharedViewModel.searchTextState.value = it },
            onCloseClicked = {
                if (sharedViewModel.searchTextState.value.isEmpty()) {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.Closed
                } else {
                    sharedViewModel.searchTextState.value = ""
                }
            },
            onSearchClicked = { sharedViewModel.searchTasks(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.default_top_app_bar_title),
            )
        },
        actions = {
            ListAppBarActions(
                onSearchClicked, onSortClicked, onDeleteAllConfirmed
            )
        },
        colors = TopAppBarDefaults.customTopAppBarColors,
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks),
        message = stringResource(id = R.string.delete_all_tasks_confirmation),
        openDialog = openDialog,
        onDismiss = { openDialog = false },
        onConfirm = {
            onDeleteAllConfirmed()
            openDialog = false
        },
    )

    SearchAction(onSearchClicked)
    SortAction(onSortClicked)
    DeleteAllAction { openDialog = true }
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit,
) {
    IconButton(
        onClick = onSearchClicked
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_button),
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    IconButton(onClick = { isExpanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.sort_button),
        )
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            Priority.entries.filter { it != Priority.Medium }.forEach {
                DropdownMenuItem(text = { PriorityItem(priority = it) }, onClick = {
                    isExpanded = false
                    onSortClicked(it)
                })
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit,
) {
    IconButton(
        onClick = onDeleteAllConfirmed
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_all_button),
        )
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(TOP_APP_BAR_HEIGHT)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(0.75f),
                        text = stringResource(id = R.string.search_button),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_button),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.close_button),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = { onSearchClicked(text) }),
                colors = TextFieldDefaults.colors().copy(
                    cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
        }
    }
}
