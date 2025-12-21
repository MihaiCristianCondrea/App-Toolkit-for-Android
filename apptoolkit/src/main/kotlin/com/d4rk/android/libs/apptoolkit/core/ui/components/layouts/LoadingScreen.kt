package com.d4rk.android.libs.apptoolkit.core.ui.components.layouts

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.MediumVerticalSpacer

/**
 * Fullscreen wrapper that displays an animated [LoadingIndicator].
 *
 * This composable is typically used while data is being loaded. Optional
 * text can be shown beneath the indicator, and the indicator size or padding
 * can be customized through the provided parameters.
 *
 * @param modifier Modifier applied to the root [Column].
 * @param paddingValues Additional padding around the content.
 * @param indicatorSize Size of the circular loading indicator.
 * @param showText When true a localized "loading" label is rendered.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    indicatorSize: Dp = 96.dp,
    showText: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingIndicator(modifier = Modifier.size(size = indicatorSize))
        if (showText) {
            MediumVerticalSpacer()
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}