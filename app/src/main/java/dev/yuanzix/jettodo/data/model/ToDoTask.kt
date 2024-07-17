package dev.yuanzix.jettodo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.yuanzix.jettodo.util.Constants

@Entity(Constants.DATABASE_TABLE)
data class ToDoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority,
)
