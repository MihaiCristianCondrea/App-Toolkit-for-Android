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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.BreathingPhase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.BreathingState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.time.Duration.Companion.milliseconds

/** Owns the breathing-session state machine and its haptic feedback. */
class BreathingRepository(
    context: Context,
    private val dispatchers: DispatcherProvider,
) {

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private val repositoryScope = CoroutineScope(SupervisorJob() + dispatchers.default)
    private val _breathingState = MutableStateFlow(BreathingState())
    val breathingState: StateFlow<BreathingState> = _breathingState.asStateFlow()
    private var job: Job? = null

    companion object {
        private const val DURATION_PREPARING = 1000L
        private const val DURATION_INHALE = 4000L
        private const val DURATION_HOLD_FULL = 2000L
        private const val DURATION_EXHALE = 4000L
        private const val DURATION_HOLD_EMPTY = 1000L
        private const val FRAME_RATE_MS = 32L
        private const val INHALE_TICK_INTERVAL = 150L
    }

    fun start() {
        job?.cancel()
        job = repositoryScope.launch {
            try {
                runPhase(BreathingPhase.PREPARING, DURATION_PREPARING, 1f, 1f)
                while (isActive) {
                    runCycle()
                }
            } finally {
                _breathingState.value = BreathingState()
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        _breathingState.value = BreathingState()
    }

    private suspend fun runCycle() {
        runPhase(BreathingPhase.INHALE, DURATION_INHALE, 0.4f, 1f, useHaptics = true)
        runPhase(BreathingPhase.HOLD_FULL, DURATION_HOLD_FULL, 1f, 1f)
        vibrate(VibrationEffect.EFFECT_HEAVY_CLICK) /*FIXME: Field requires API level 29 (current min is 26): android.os.VibrationEffect#EFFECT_HEAVY_CLICK*/
        runPhase(BreathingPhase.EXHALE, DURATION_EXHALE, 1f, 0.4f)
        runPhase(BreathingPhase.HOLD_EMPTY, DURATION_HOLD_EMPTY, 0.4f, 0.4f)
    }

    private suspend fun runPhase(
        phase: BreathingPhase,
        duration: Long,
        startVal: Float,
        endVal: Float,
        useHaptics: Boolean = false
    ) {
        val startTime = System.currentTimeMillis()
        var elapsedTime = 0L
        var nextHapticTrigger = 0L

        while (elapsedTime < duration && currentCoroutineContext().isActive) {
            elapsedTime = System.currentTimeMillis() - startTime

            if (useHaptics && elapsedTime >= nextHapticTrigger) {
                vibrate(VibrationEffect.EFFECT_TICK) /*FIXME: Field requires API level 29 (current min is 26): android.os.VibrationEffect#EFFECT_TICK*/
                nextHapticTrigger += INHALE_TICK_INTERVAL
            }

            val fraction = (elapsedTime.toFloat() / duration).coerceIn(0f, 1f)
            val currentProgress = startVal + (endVal - startVal) * fraction
            val secondsLeft = ceil((duration - elapsedTime) / 1000.0).toInt().coerceAtLeast(1)

            _breathingState.value = BreathingState(phase, currentProgress, secondsLeft)
            delay(FRAME_RATE_MS.milliseconds)
        }
    }

    private fun vibrate(effectId: Int) {
        if (!vibrator.hasVibrator()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(effectId)) /*FIXME: Missing permissions required by Vibrator.vibrate: android.permission.VIBRATE*/
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }
}
