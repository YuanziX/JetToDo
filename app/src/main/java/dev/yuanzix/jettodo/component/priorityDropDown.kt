package dev.yuanzix.jettodo.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.yuanzix.jettodo.R
import dev.yuanzix.jettodo.data.model.Priority
import dev.yuanzix.jettodo.ui.theme.PRIORITY_DROPDOWN_HEIGHT
import dev.yuanzix.jettodo.ui.theme.PRIORITY_INDICATOR_SIZE
import dev.yuanzix.jettodo.ui.theme.SMALL_PADDING

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val angle: Float by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(PRIORITY_DROPDOWN_HEIGHT)
            .clickable { isExpanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(SMALL_PADDING)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .weight(1f)
        ) {
            drawCircle(
                color = priority.color,
            )
        }
        Text(
            modifier = Modifier.weight(8f),
            text = priority.name,
            style = MaterialTheme.typography.bodyLarge,
        )
        IconButton(
            modifier = Modifier
                .alpha(0.75f)
                .weight(1.5f)
                .rotate(angle),
            onClick = { isExpanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.drop_down_button),
            )
        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(0.94f),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            Priority.entries.filter { it != Priority.None }.forEach {
                DropdownMenuItem(
                    text = { PriorityItem(priority = it) },
                    onClick = {
                        isExpanded = false
                        onPrioritySelected(it)
                    },
                )
            }
        }
    }
}
