package dev.yuanzix.jettodo.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yuanzix.jettodo.data.model.Priority
import dev.yuanzix.jettodo.data.model.ToDoTask
import dev.yuanzix.jettodo.data.repository.DataStoreRepository
import dev.yuanzix.jettodo.data.repository.ToDoRepository
import dev.yuanzix.jettodo.util.Action
import dev.yuanzix.jettodo.util.Constants.MAX_TITLE_LENGTH
import dev.yuanzix.jettodo.util.RequestState
import dev.yuanzix.jettodo.util.SearchAppBarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NoAction)

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchedTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    private val _sortOrder = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortOrder: StateFlow<RequestState<Priority>> = _sortOrder

    val lowPriorityTasks: StateFlow<List<ToDoTask>> = repository.sortByLowPriority.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList()
    )

    val highPriorityTasks: StateFlow<List<ToDoTask>> = repository.sortByHighPriority.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList()
    )

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(
        SearchAppBarState.Closed
    )
    val searchTextState: MutableState<String> = mutableStateOf("")

    val id: MutableState<Int> = mutableIntStateOf(-1)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.Low)

    init {
        getAllTasks()
        readSortOrder()
    }

    private fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    private fun readSortOrder() {
        _sortOrder.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortOrder.collect {
                    _sortOrder.value = RequestState.Success(Priority.valueOf(it))
                }
            }
        } catch (e: Exception) {
            _sortOrder.value = RequestState.Error(e)
        }
    }

    fun searchTasks(query: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase("%$query%").collect {
                    _searchedTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.Triggered
    }

    fun persistSortOrder(priority: Priority) {
        viewModelScope.launch {
            dataStoreRepository.persistSortOrder(priority)
        }
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect {
                _selectedTask.value = it
            }
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value, description = description.value, priority = priority.value
            )

            repository.insert(toDoTask)
        }
        searchAppBarState.value = SearchAppBarState.Closed
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(
                ToDoTask(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(
                ToDoTask(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.Add -> addTask()
            Action.Update -> updateTask()
            Action.Delete -> deleteTask()
            Action.DeleteAll -> deleteAllTasks()
            Action.Undo -> {
                addTask()
            }

            else -> return
        }
        this.action.value = Action.NoAction
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = -1
            title.value = ""
            description.value = ""
            priority.value = Priority.Low
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }
}