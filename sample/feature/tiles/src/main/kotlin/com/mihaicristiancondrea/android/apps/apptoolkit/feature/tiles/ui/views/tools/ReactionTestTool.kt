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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionRating
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestPhase
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestToolState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReactionTestTool(
    state: ReactionTestToolState,
    onStart: () -> Unit,
    onTap: () -> Unit,
    onReset: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(state.phase) {
        when (state.phase) {
            ReactionTestPhase.Signal -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            ReactionTestPhase.FalseStart -> haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            else -> {}
        }
    }

    val containerColor by animateColorAsState(
        targetValue = when (state.phase) {
            ReactionTestPhase.Idle -> MaterialTheme.colorScheme.surfaceContainerHigh
            ReactionTestPhase.Waiting -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            ReactionTestPhase.Signal -> Color(0xFF2E7D32)
            ReactionTestPhase.Result -> MaterialTheme.colorScheme.primaryContainer
            ReactionTestPhase.FalseStart -> MaterialTheme.colorScheme.errorContainer
        },
        label = "reaction_test_container_color",
    )

    val contentColor = when (state.phase) {
        ReactionTestPhase.Signal -> Color.White
        ReactionTestPhase.FalseStart -> MaterialTheme.colorScheme.onErrorContainer
        ReactionTestPhase.Result -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(SizeConstants.LargeSize))
                .background(containerColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = state.phase == ReactionTestPhase.Waiting || state.phase == ReactionTestPhase.Signal,
                    onClick = onTap,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
            ) {
                when (state.phase) {
                    ReactionTestPhase.Idle -> {
                        Text(
                            text = stringResource(id = R.string.tool_reaction_test_idle_prompt),
                            style = MaterialTheme.typography.titleMedium,
                            color = contentColor,
                        )
                    }

                    ReactionTestPhase.Waiting -> {
                        Text(
                            text = stringResource(id = R.string.tool_reaction_test_waiting_prompt),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = contentColor,
                        )
                    }

                    ReactionTestPhase.Signal -> {
                        Text(
                            text = stringResource(id = R.string.tool_reaction_test_signal_prompt),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = contentColor,
                        )
                    }

                    ReactionTestPhase.FalseStart -> {
                        Text(
                            text = stringResource(id = R.string.tool_reaction_test_too_early),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = contentColor,
                        )
                    }

                    ReactionTestPhase.Result -> {
                        state.lastReactionTimeMs?.let { time ->
                            Text(
                                text = stringResource(id = R.string.tool_reaction_test_time_format, time),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = contentColor,
                            )
                        }
                        state.rating?.let { rating ->
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = stringResource(
                                            id = when (rating) {
                                                ReactionRating.Lightning -> R.string.tool_reaction_rating_lightning
                                                ReactionRating.Fast -> R.string.tool_reaction_rating_fast
                                                ReactionRating.Average -> R.string.tool_reaction_rating_average
                                                ReactionRating.Slow -> R.string.tool_reaction_rating_slow
                                            }
                                        )
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                ),
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
        ) {
            GeneralButton(
                modifier = Modifier.weight(1f),
                measurements = ButtonMeasurements.Medium,
                onClick = onStart,
                enabled = state.phase != ReactionTestPhase.Waiting && state.phase != ReactionTestPhase.Signal,
                icon = ToolkitIcon.Vector(Icons.Outlined.PlayArrow),
                label = stringResource(
                    id = when (state.phase) {
                        ReactionTestPhase.Idle -> R.string.tool_reaction_test_start
                        ReactionTestPhase.Waiting, ReactionTestPhase.Signal -> R.string.tool_reaction_test_in_progress
                        else -> R.string.tool_reaction_test_try_again
                    }
                ),
            )
            if (state.history.isNotEmpty()) {
                GeneralButton(
                    style = GeneralButtonStyle.Outlined,
                    measurements = ButtonMeasurements.Medium,
                    onClick = onReset,
                    icon = ToolkitIcon.Vector(Icons.Outlined.Refresh),
                    label = stringResource(id = R.string.tool_counter_reset),
                )
            }
        }

        if (state.history.isNotEmpty() || state.bestTimeMs != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(SizeConstants.MediumSize),
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        state.bestTimeMs?.let { best ->
                            Text(
                                text = stringResource(id = R.string.tool_reaction_test_best, best),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        state.averageTimeMs?.let { avg ->
                            Text(
                                text = stringResource(id = R.string.tool_reaction_test_average, avg),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    if (state.history.isNotEmpty()) {
                        Text(
                            text = stringResource(
                                id = R.string.tool_reaction_test_round_progress,
                                state.roundCount,
                                state.totalRounds,
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
                            verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
                        ) {
                            state.history.forEach { score ->
                                AssistChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            text = stringResource(id = R.string.tool_reaction_test_time_format, score),
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
