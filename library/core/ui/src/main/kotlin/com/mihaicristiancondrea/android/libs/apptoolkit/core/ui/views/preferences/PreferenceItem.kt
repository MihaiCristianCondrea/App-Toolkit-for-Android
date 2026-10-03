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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.colorscheme.darken
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry

/**
 * Stateless preference row with Toolkit click feedback and analytics. Supply display-ready
 * text, with [title] meaningful on its own and [summary] adding context.
 *
 * @param ga4EventProvider Resolves analytics at click time, taking precedence over [ga4Event]
 * when it returns an event.
 */
@Composable
fun PreferenceItem(
    icon: ImageVector? = null,
    title: String? = null,
    summary: String? = null,
    useIconContainer: Boolean = false,
    iconColor: Color? = null,
    iconContainerColor: Color? = null,
    enabled: Boolean = true,
    rippleEffectDp: Dp = SizeConstants.LargeSize,
    onClick: () -> Unit = {},
    ga4Event: Ga4EventData? = null,
    ga4EventProvider: (() -> Ga4EventData?)? = null,
) {
    val telemetryRepository = LocalTelemetry.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = rippleEffectDp))
            .clickable(enabled = enabled, onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                telemetryRepository.logGa4Event(ga4EventProvider?.invoke() ?: ga4Event)
                onClick()
            }), verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            LargeHorizontalSpacer()
            if (useIconContainer) {
                val containerColor =
                    iconContainerColor ?: MaterialTheme.colorScheme.secondaryContainer
                val tint = iconColor ?: containerColor.darken(factor = 0.6f)

                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = containerColor
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = if (iconColor == null && iconContainerColor == null) MaterialTheme.colorScheme.onSecondaryContainer else tint
                        )
                    }
                }
            } else {
                Icon(imageVector = it, contentDescription = null)
            }
        }
        Column(
            modifier = Modifier.padding(all = SizeConstants.LargeSize)
        ) {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (!enabled) LocalContentColor.current.copy(alpha = 0.38f) else LocalContentColor.current,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            summary?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (!enabled) LocalContentColor.current.copy(alpha = 0.38f) else LocalContentColor.current
                )
            }
        }
    }
}

/**
 * Card container for [PreferenceItem], with the same click feedback and analytics contract.
 */
@Composable
fun SettingsPreferenceItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String? = null,
    summary: String? = null,
    useIconContainer: Boolean = false,
    iconColor: Color? = null,
    iconContainerColor: Color? = null,
    rippleEffectDp: Dp = SizeConstants.ExtraTinySize,
    onClick: () -> Unit = {},
    ga4Event: Ga4EventData? = null,
    ga4EventProvider: (() -> Ga4EventData?)? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RectangleShape,
    ) {
        PreferenceItem(
            rippleEffectDp = rippleEffectDp,
            icon = icon,
            title = title,
            summary = summary,
            useIconContainer = useIconContainer,
            iconColor = iconColor,
            iconContainerColor = iconContainerColor,
            onClick = {
                onClick()
            },
            ga4Event = ga4Event,
            ga4EventProvider = ga4EventProvider,
        )
    }
}
