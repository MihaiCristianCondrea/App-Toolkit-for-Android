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

package com.d4rk.android.libs.apptoolkit.core.ui.views.cards

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Displays a selectable theme choice card with optional preview content and description.
 *
 * This composable is shared across onboarding and settings screens to keep theme selection
 * interactions consistent.
 */
@Composable
fun ThemeChoicePreviewCard(
    title: String,
    description: String?,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showTopIcon: Boolean = true,
    showPreview: Boolean = true,
    preview: @Composable BoxScope.() -> Unit,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    val cardContainerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

    val cardElevation = if (isSelected) {
        SizeConstants.ExtraSmallSize
    } else {
        SizeConstants.ExtraTinySize / 2
    }

    val cardShape = RoundedCornerShape(SizeConstants.LargeSize)
    val previewShape = RoundedCornerShape(
        topStart = SizeConstants.MediumSize,
        topEnd = SizeConstants.MediumSize,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    Column(
        modifier = modifier
            .bounceClick()
            .semantics {
                role = Role.RadioButton
                selected = isSelected
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
            },
            shape = cardShape,
            colors = CardDefaults.cardColors(containerColor = cardContainerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.SmallSize),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
            ) {
                ExtraSmallVerticalSpacer()

                if (showTopIcon) {
                    val selectedBg = MaterialTheme.colorScheme.primary
                    val selectedFg = MaterialTheme.colorScheme.onPrimary

                    val contrastOk =
                        kotlin.math.abs(selectedBg.luminance() - selectedFg.luminance()) >= 0.35f
                    val showSelectedIcon = isSelected && contrastOk

                    val iconScale by animateFloatAsState(
                        targetValue = if (showSelectedIcon) 1.10f else 1.00f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "themeIconScale"
                    )

                    val iconTint by animateColorAsState(
                        targetValue = if (showSelectedIcon) selectedFg else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy),
                        label = "themeIconTint"
                    )

                    val iconBg by animateColorAsState(
                        targetValue = if (showSelectedIcon) selectedBg else Color.Transparent,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy),
                        label = "themeIconBg"
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(iconBg)
                            .padding(SizeConstants.ExtraSmallSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = iconTint,
                            modifier = Modifier
                                .size(SizeConstants.LargeSize + SizeConstants.SmallSize)
                                .graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SizeConstants.NinetySixSize)
                        .clip(previewShape)
                        .padding(
                            start = SizeConstants.ExtraTinySize * 2,
                            end = SizeConstants.ExtraTinySize * 2,
                            top = SizeConstants.ExtraTinySize * 2,
                            bottom = SizeConstants.ZeroSize
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (showPreview) preview()
                }
            }
        }

        Spacer(modifier = Modifier.height(SizeConstants.SmallSize))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        if (!description.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
