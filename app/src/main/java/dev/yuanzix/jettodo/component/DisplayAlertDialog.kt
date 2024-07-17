package dev.yuanzix.jettodo.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.yuanzix.jettodo.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.W500
                )
            },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                ElevatedButton(onClick = onConfirm) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                ElevatedButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            }
        )
    }
}