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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation

import androidx.compose.material3.SegmentedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.BillSplitterToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.BreathingToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.CoinFlipToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.CompassToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.CounterToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.DiceRollToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.FlashDimmerToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.LevelToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.MorseToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.ProtractorToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.RandomNumberToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.ReactionTestToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.RulerToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.ScreenLightToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.SosToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.UnitConverterToolViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BillSplitterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BreathingToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CoinFlipToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CompassToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CounterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.DiceRollToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.FlashDimmerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.LevelToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.MorseToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ProtractorToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RandomNumberToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ReactionTestToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ScreenLightToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.SosToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.UnitConverterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.BillSplitterTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.BreathingTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.CoinFlipTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.CompassTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.CounterTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.DiceRollTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.FlashDimmerTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.LevelTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.MorseTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.ProtractorTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.RandomNumberTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.ReactionTestTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.RulerTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.ScreenLightTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.SosTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.UnitConverterTool
import org.koin.compose.viewmodel.koinViewModel

/*
 * These routes take their ViewModel as a defaulted parameter, which Compose reports as unstable and
 * therefore unskippable. That is not worth chasing: a route is the content of one navigation
 * destination, composed once and never re-invoked with different arguments, so skippability buys
 * nothing. Removing the parameter to satisfy the inspection would only cost the tests their seam
 * for injecting a fake.
 */

@Composable
internal fun CoinFlipToolRoute(viewModel: CoinFlipToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool { viewModel.onEvent(CoinFlipToolEvent.Dismiss) }
    CoinFlipTool(
        side = state.side,
        flipRequest = state.request,
        onFlip = { viewModel.onEvent(CoinFlipToolEvent.Flip) },
    )
}

@Composable
internal fun DiceRollToolRoute(viewModel: DiceRollToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool { viewModel.onEvent(DiceRollToolEvent.Dismiss) }
    DiceRollTool(
        result = state.result,
        rollRequest = state.request,
        onRoll = { viewModel.onEvent(DiceRollToolEvent.Roll) },
    )
}

@Composable
internal fun CounterToolRoute(viewModel: CounterToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool { viewModel.onEvent(CounterToolEvent.Dismiss) }
    CounterTool(
        count = state.count,
        onIncrement = { viewModel.onEvent(CounterToolEvent.Increment) },
        onDecrement = { viewModel.onEvent(CounterToolEvent.Decrement) },
        onReset = { viewModel.onEvent(CounterToolEvent.Reset) },
    )
}

@Composable
internal fun CompassToolRoute(viewModel: CompassToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(
        start = { viewModel.onEvent(CompassToolEvent.Open) },
        stop = { viewModel.onEvent(CompassToolEvent.Dismiss) },
    )
    ForegroundSensor(
        start = { viewModel.onEvent(CompassToolEvent.StartSensor) },
        stop = { viewModel.onEvent(CompassToolEvent.StopSensor) },
    )
    CompassTool(azimuth = state.azimuth)
}

@Composable
internal fun LevelToolRoute(viewModel: LevelToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(
        start = { viewModel.onEvent(LevelToolEvent.Open) },
        stop = { viewModel.onEvent(LevelToolEvent.Dismiss) },
    )
    ForegroundSensor(
        start = { viewModel.onEvent(LevelToolEvent.StartSensor) },
        stop = { viewModel.onEvent(LevelToolEvent.StopSensor) },
    )
    LevelTool(pitch = state.pitch, roll = state.roll)
}

/**
 * Keeps a sensor registered only while the app is started, so a tool sheet left open does not
 * keep the sensor running in the background.
 */
@Composable
private fun ForegroundSensor(start: () -> Unit, stop: () -> Unit) {
    LifecycleStartEffect(Unit) {
        start()
        onStopOrDispose { stop() }
    }
}

@Composable
internal fun BreathingToolRoute(viewModel: BreathingToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(
        start = { viewModel.onEvent(BreathingToolEvent.Open) },
        stop = { viewModel.onEvent(BreathingToolEvent.Dismiss) },
    )
    BreathingTool(state = state.breathing)
}

@Composable
internal fun SosToolRoute(viewModel: SosToolViewModel = koinViewModel(), morse: MorseToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val message by morse.state.collectAsStateWithLifecycle()
    var messageMode by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf(false) }
    LifecycleStartEffect(viewModel, morse) {
        onStopOrDispose {
            viewModel.onEvent(SosToolEvent.Dismiss)
            morse.onEvent(MorseToolEvent.Dismiss)
        }
    }
    androidx.compose.foundation.layout.Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
        androidx.compose.material3.SingleChoiceSegmentedButtonRow(androidx.compose.ui.Modifier.fillMaxWidth()) {
            listOf(false,true).forEachIndexed { index, selected ->
                SegmentedButton(selected = messageMode == selected, enabled = !state.isActive && !message.playback.isActive,
                    onClick = { messageMode = selected }, shape = androidx.compose.material3.SegmentedButtonDefaults.itemShape(index,2)) {
                    androidx.compose.material3.Text(if (selected) androidx.compose.ui.res.stringResource(com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R.string.morse_mode) else "SOS")
                }
            }
        }
        if (messageMode) MorseTool(message, { morse.onEvent(MorseToolEvent.InputChanged(it)) }, { morse.onEvent(MorseToolEvent.Toggle) })
        else SosTool(state.isActive) { viewModel.onEvent(SosToolEvent.Toggle) }
    }
}

@Composable
internal fun MorseToolRoute(viewModel: MorseToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LifecycleStartEffect(viewModel) { onStopOrDispose { viewModel.onEvent(MorseToolEvent.Dismiss) } }
    MorseTool(
        state = state,
        onInputChanged = { input -> viewModel.onEvent(MorseToolEvent.InputChanged(input)) },
        onToggle = { viewModel.onEvent(MorseToolEvent.Toggle) },
    )
}

@Composable
internal fun FlashDimmerToolRoute(viewModel: FlashDimmerToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LifecycleStartEffect(viewModel) { onStopOrDispose { viewModel.onEvent(FlashDimmerToolEvent.Dismiss) } }
    FlashDimmerTool(
        state = state.torch,
        onLevelChanged = { level -> viewModel.onEvent(FlashDimmerToolEvent.LevelChanged(level)) },
        onPresetSelected = { preset -> viewModel.onEvent(FlashDimmerToolEvent.PresetSelected(preset)) },
    )
}

@Composable
internal fun ReactionTestToolRoute(autoStart: Boolean = false, viewModel: ReactionTestToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(autoStart) { if (autoStart) viewModel.onEvent(ReactionTestToolEvent.Start) }
    LifecycleStartEffect(viewModel) {
        onStopOrDispose { viewModel.onEvent(ReactionTestToolEvent.Dismiss) }
    }
    ReactionTestTool(
        state = state,
        onStart = { viewModel.onEvent(ReactionTestToolEvent.Start) },
        onTap = { viewModel.onEvent(ReactionTestToolEvent.Tap) },
        onReset = { viewModel.onEvent(ReactionTestToolEvent.Reset) },
    )
}

@Composable
private fun StartStopTool(start: () -> Unit, stop: () -> Unit) {
    LifecycleStartEffect(Unit) { start(); onStopOrDispose { stop() } }
}

@Composable
private fun DisposeTool(dispose: () -> Unit) {
    DisposableEffect(Unit) { onDispose(dispose) }
}

@Composable
internal fun UnitConverterToolRoute(viewModel: UnitConverterToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool { viewModel.onEvent(UnitConverterToolEvent.Dismiss) }
    UnitConverterTool(state = state, onEvent = viewModel::onEvent)
}

@Composable
internal fun RulerToolRoute(onClose: () -> Unit, viewModel: RulerToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool { viewModel.onEvent(RulerToolEvent.Dismiss) }
    RulerTool(state = state, onEvent = viewModel::onEvent, onClose = onClose)
}

@Composable
internal fun ScreenLightToolRoute(onClose: () -> Unit, viewModel: ScreenLightToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(
        start = { viewModel.onEvent(ScreenLightToolEvent.Open) },
        stop = { viewModel.onEvent(ScreenLightToolEvent.Dismiss) },
    )
    ScreenLightTool(state = state, onEvent = viewModel::onEvent, onClose = onClose)
}

@Composable
internal fun ProtractorToolRoute(onClose: () -> Unit, viewModel: ProtractorToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool { viewModel.onEvent(ProtractorToolEvent.Dismiss) }
    ProtractorTool(state = state, onEvent = viewModel::onEvent, onClose = onClose)
}

@Composable
internal fun BillSplitterToolRoute(viewModel: BillSplitterToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool { viewModel.onEvent(BillSplitterToolEvent.Dismiss) }
    BillSplitterTool(state = state, onEvent = viewModel::onEvent)
}

@Composable
internal fun RandomNumberToolRoute(viewModel: RandomNumberToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool { viewModel.onEvent(RandomNumberToolEvent.Dismiss) }
    RandomNumberTool(state = state, onEvent = viewModel::onEvent)
}

@Composable
internal fun WaterEjectToolRoute(viewModel: com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.WaterEjectToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LifecycleStartEffect(viewModel) {
        onStopOrDispose { viewModel.onEvent(com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WaterEjectToolEvent.Stop) }
    }
    com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.WaterEjectTool(state, viewModel::onEvent)
}

@Composable
internal fun DecisionWheelToolRoute(viewModel: com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.WheelToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LifecycleStartEffect(viewModel) {
        onStopOrDispose { viewModel.onEvent(com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WheelToolEvent.Dismiss) }
    }
    com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.DecisionWheelTool(state, viewModel::onEvent)
}
