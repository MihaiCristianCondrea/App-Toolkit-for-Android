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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import android.os.SystemClock
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.logReactionScore
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ReactionTestToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionRating
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestPhase
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

/**
 * Runs Reaction Test rounds: a random wait, then a signal, then the time to the tap. Each finished
 * round is posted as a `post_score` event.
 *
 * @param timeProvider The clock reaction times are measured on, in milliseconds.
 * @param signalDelayMs How long a round waits before the signal, in milliseconds.
 */
class ReactionTestToolViewModel(
    telemetryRepository: TelemetryRepository,
    private val timeProvider: () -> Long = SystemClock::elapsedRealtime,
    private val signalDelayMs: () -> Long = { Random.nextLong(from = 1_500L, until = 5_000L) },
) : LoggedScreenViewModel<ReactionTestToolUiState, ReactionTestToolEvent>(
    initialState = ReactionTestToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "ReactionTestToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.REACTION_TEST)
    private var waitingJob: Job? = null
    private var signalTimeMs: Long = 0L

    override fun handleEvent(event: ReactionTestToolEvent) {
        when (event) {
            ReactionTestToolEvent.Start -> startRound()
            ReactionTestToolEvent.Tap -> tap()
            ReactionTestToolEvent.Reset -> reset()
            ReactionTestToolEvent.Dismiss -> dismiss()
        }
    }

    private fun startRound() {
        if (currentState.phase == ReactionTestPhase.Waiting || currentState.phase == ReactionTestPhase.Signal) return

        setState { copy(phase = ReactionTestPhase.Waiting) }
        val delayMs: Long = signalDelayMs()
        waitingJob = waitingJob.restart {
            launchReport(action = Actions.START_ROUND) {
                delay(delayMs.milliseconds)
                if (currentState.phase == ReactionTestPhase.Waiting) {
                    signalTimeMs = timeProvider()
                    setState { copy(phase = ReactionTestPhase.Signal) }
                }
            }
        }
    }

    private fun tap() {
        when (currentState.phase) {
            ReactionTestPhase.Waiting -> {
                waitingJob?.cancel()
                setState { copy(phase = ReactionTestPhase.FalseStart) }
            }

            ReactionTestPhase.Signal -> finishRound(reactionTimeMs = timeProvider() - signalTimeMs)
            ReactionTestPhase.Idle, ReactionTestPhase.Result, ReactionTestPhase.FalseStart -> Unit
        }
    }

    private fun finishRound(reactionTimeMs: Long) {
        launchReport(action = Actions.FINISH_ROUND) {
            setState { withResult(reactionTimeMs) }
            telemetryRepository.logReactionScore(reactionTimeMs = reactionTimeMs)
            usage.markUsed()
        }
    }

    private fun reset() {
        waitingJob?.cancel()
        setState { ReactionTestToolUiState() }
    }

    private fun dismiss() {
        waitingJob?.cancel()
        setState { copy(phase = ReactionTestPhase.Idle) }
        usage.endSession()
    }

    /** Adds a round to the results; the average covers the last [HISTORY_SIZE] rounds. */
    private fun ReactionTestToolUiState.withResult(reactionTimeMs: Long): ReactionTestToolUiState {
        val updatedHistory: ImmutableList<Long> =
            (history + reactionTimeMs).takeLast(HISTORY_SIZE).toImmutableList()
        return copy(
            phase = ReactionTestPhase.Result,
            lastReactionTimeMs = reactionTimeMs,
            bestTimeMs = bestTimeMs?.let { minOf(it, reactionTimeMs) } ?: reactionTimeMs,
            averageTimeMs = updatedHistory.average().toLong(),
            history = updatedHistory,
            roundCount = minOf(roundCount + 1, totalRounds),
            rating = reactionTimeMs.toRating(),
        )
    }

    private fun Long.toRating(): ReactionRating = when {
        this < 200 -> ReactionRating.Lightning
        this in 200..250 -> ReactionRating.Fast
        this in 251..350 -> ReactionRating.Average
        else -> ReactionRating.Slow
    }

    private object Actions {
        const val START_ROUND: String = "startReactionRound"
        const val FINISH_ROUND: String = "finishReactionRound"
    }

    private companion object {
        const val HISTORY_SIZE: Int = 5
    }
}
