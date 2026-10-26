/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools

import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.compose.LifecycleStartEffect
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ScreenLightToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ScreenLightToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton

/** Brightness and keep-awake apply only to this dialog window while the app is started. */
@Composable
fun ScreenLightTool(state: ScreenLightToolUiState, onEvent: (ScreenLightToolEvent) -> Unit, onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
    ) {
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        val previousBrightness = remember(window) { window?.attributes?.screenBrightness ?: -1f }
        val keptAwake = remember(window) { window?.attributes?.flags?.and(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0 }
        var foreground by remember { mutableStateOf(false) }
        LifecycleStartEffect(window) {
            foreground = true
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            onStopOrDispose {
                foreground = false
                window?.let {
                    it.attributes = it.attributes.apply { screenBrightness = previousBrightness }
                    if (!keptAwake) it.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
        }
        SideEffect {
            window?.let { it.attributes = it.attributes.apply { screenBrightness = if (foreground) state.brightness else previousBrightness } }
        }
        Box(Modifier.fillMaxSize().background(lerp(Color.White, Color(0xFFFFB56B), state.warmth))) {
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().safeDrawingPadding()
                    .padding(SizeConstants.LargeSize),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                val brightnessLabel = stringResource(R.string.tool_light_brightness)
                val warmthLabel = stringResource(R.string.tool_light_warmth)
                Column(
                    Modifier.verticalScroll(rememberScrollState()).padding(SizeConstants.LargeSize),
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
                ) {
                    Text(stringResource(R.string.tile_screen_light_title), style = MaterialTheme.typography.titleLarge)
                    Text(brightnessLabel)
                    val brightnessSliderState = rememberSliderState(value = state.brightness, trackRange = 0.01f..1f)
                    brightnessSliderState.value = state.brightness
                    Slider(
                        state = brightnessSliderState,
                        onValueChange = { onEvent(ScreenLightToolEvent.BrightnessChanged(it)) },
                        modifier = Modifier.semantics { contentDescription = brightnessLabel },
                    )
                    Text(warmthLabel)
                    val warmthSliderState = rememberSliderState(value = state.warmth)
                    warmthSliderState.value = state.warmth
                    Slider(
                        state = warmthSliderState,
                        onValueChange = { onEvent(ScreenLightToolEvent.WarmthChanged(it)) },
                        modifier = Modifier.semantics { contentDescription = warmthLabel },
                    )
                    GeneralButton(onClick = onClose, label = stringResource(R.string.tool_dialog_close_content_description))
                }
            }
        }
    }
}
