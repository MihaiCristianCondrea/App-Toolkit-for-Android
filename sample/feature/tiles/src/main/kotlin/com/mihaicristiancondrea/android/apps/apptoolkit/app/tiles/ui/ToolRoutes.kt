/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.BatteryTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.BreathingTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.CaffeineTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.CoinFlipTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.CompassTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.CounterTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.DiceRollTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.FlashDimmerTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.LevelTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.LuxMeterTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.MusicSearchTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.MorseTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.SosTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.SoundModeTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools.TemperatureTool
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CoinFlipToolRoute(viewModel: CoinFlipToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool(viewModel::dismiss)
    CoinFlipTool(state.isHeads, state.request, viewModel::flip)
}

@Composable
internal fun DiceRollToolRoute(viewModel: DiceRollToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool(viewModel::dismiss)
    DiceRollTool(state.result, state.request, viewModel::roll)
}

@Composable
internal fun CounterToolRoute(viewModel: CounterToolViewModel = koinViewModel()) {
    val count by viewModel.count.collectAsStateWithLifecycle()
    DisposeTool(viewModel::dismiss)
    CounterTool(count, viewModel::increment, viewModel::reset)
}

@Composable
internal fun BatteryToolRoute(viewModel: BatteryToolViewModel = koinViewModel()) {
    val percent by viewModel.percent.collectAsStateWithLifecycle()
    BatteryTool(percent, viewModel::refresh)
}

@Composable
internal fun CompassToolRoute(viewModel: CompassToolViewModel = koinViewModel()) {
    val value by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(viewModel::open, viewModel::dismiss)
    CompassTool(value)
}

@Composable
internal fun LevelToolRoute(viewModel: LevelToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(viewModel::open, viewModel::dismiss)
    LevelTool(state.pitch, state.roll)
}

@Composable
internal fun LuxMeterToolRoute(viewModel: LuxMeterToolViewModel = koinViewModel()) {
    val value by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(viewModel::open, viewModel::dismiss)
    LuxMeterTool(value)
}

@Composable
internal fun TemperatureToolRoute(viewModel: TemperatureToolViewModel = koinViewModel()) {
    val value by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(viewModel::open, viewModel::dismiss)
    TemperatureTool(value)
}

@Composable
internal fun BreathingToolRoute(viewModel: BreathingToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(viewModel::open, viewModel::close)
    BreathingTool(state)
}

@Composable
internal fun CaffeineToolRoute(viewModel: CaffeineToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CaffeineTool(state, viewModel::cycle)
}

@Composable
internal fun SoundModeToolRoute(viewModel: SoundModeToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StartStopTool(viewModel::open, viewModel::dismiss)
    SoundModeTool(state, viewModel::cycle)
}

@Composable
internal fun MusicSearchToolRoute(viewModel: MusicSearchToolViewModel = koinViewModel()) {
    MusicSearchTool(viewModel::launch)
}

@Composable
internal fun SosToolRoute(viewModel: SosToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool(viewModel::dismiss)
    SosTool(state.isActive && state.message == "SOS", viewModel::toggle)
}

@Composable
internal fun MorseToolRoute(viewModel: MorseToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool(viewModel::dismiss)
    MorseTool(state, viewModel::updateInput, viewModel::toggle)
}

@Composable
internal fun FlashDimmerToolRoute(viewModel: FlashDimmerToolViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DisposeTool(viewModel::dismiss)
    FlashDimmerTool(state, viewModel::setLevel, viewModel::applyPreset)
}

@Composable
private fun StartStopTool(start: () -> Unit, stop: () -> Unit) {
    LaunchedEffect(Unit) { start() }
    DisposeTool(stop)
}

@Composable
private fun DisposeTool(dispose: () -> Unit) {
    DisposableEffect(Unit) { onDispose(dispose) }
}
