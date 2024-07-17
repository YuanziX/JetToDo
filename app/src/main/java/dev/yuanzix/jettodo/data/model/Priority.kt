package dev.yuanzix.jettodo.data.model

import androidx.compose.ui.graphics.Color
import dev.yuanzix.jettodo.ui.theme.HighPriorityColor
import dev.yuanzix.jettodo.ui.theme.LowPriorityColor
import dev.yuanzix.jettodo.ui.theme.MediumPriorityColor
import dev.yuanzix.jettodo.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    High(HighPriorityColor),
    Medium(MediumPriorityColor),
    Low(LowPriorityColor),
    None(NonePriorityColor),
}