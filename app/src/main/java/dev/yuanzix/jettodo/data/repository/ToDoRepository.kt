package dev.yuanzix.jettodo.data.repository

import dagger.hilt.android.scopes.ViewModelScoped
import dev.yuanzix.jettodo.data.ToDoDao
import dev.yuanzix.jettodo.data.model.ToDoTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {
    val getAllTasks: Flow<List<ToDoTask>> = toDoDao.getAllTasts()
    val sortByLowPriority: Flow<List<ToDoTask>> = toDoDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<ToDoTask>> = toDoDao.sortByHighPriority()

    fun getTaskById(id: Int): Flow<ToDoTask> = toDoDao.getSelectedTask(id)

    suspend fun insert(todo: ToDoTask) = toDoDao.insertTask(todo)

    suspend fun update(todo: ToDoTask) = toDoDao.updateTask(todo)

    suspend fun delete(todo: ToDoTask) = toDoDao.deleteTask(todo)

    suspend fun deleteAll() = toDoDao.deleteAllTasks()

    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> =
        toDoDao.searchDatabase(searchQuery)
}