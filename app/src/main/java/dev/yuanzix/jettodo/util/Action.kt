package dev.yuanzix.jettodo.util

enum class Action {
    Add,
    Update,
    Delete,
    DeleteAll,
    Undo,
    NoAction,
}

fun String?.toAction(): Action {
    return when (this) {
        "Add" -> Action.Add
        "Update" -> Action.Update
        "Delete" -> Action.Delete
        "DeleteAll" -> Action.DeleteAll
        "Undo" -> Action.Undo
        else -> Action.NoAction
    }
}