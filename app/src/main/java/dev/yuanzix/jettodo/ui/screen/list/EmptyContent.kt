package dev.yuanzix.jettodo.ui.screen.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import dev.yuanzix.jettodo.R
import dev.yuanzix.jettodo.ui.theme.MEDIUM_PADDING

@Composable
fun EmptyContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.empty_box_white else R.drawable.empty_box_black),
            contentDescription = stringResource(id = R.string.empty_box),
        )
        Spacer(modifier = Modifier.height(MEDIUM_PADDING))
        Text(
            text = stringResource(id = R.string.empty_content),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center
        )
    }
}