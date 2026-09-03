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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.ui.views.cards

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralTextButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.text.HtmlText

/**
 * Displays one expandable FAQ entry.
 *
 * @param groupedPosition Optional position in the Help content group. When supplied, the card
 * uses grouped corners and a compact layout; when omitted, the standalone shape is kept.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QuestionCard(
    title: String,
    summary: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
    groupedPosition: GroupedItemPosition? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val expandIconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "ExpandIconRotation"
    )
    val cardModifier = modifier
        .fillMaxWidth()
        .let { currentModifier ->
            groupedPosition?.let {
                currentModifier.groupedCorners(
                    position = it,
                    outerRadius = SizeConstants.ExtraLargeIncreasedSize,
                )
            } ?: currentModifier
        }
        .animateContentSize()

    Card(
        modifier = cardModifier,
        shape = groupedPosition?.let { RectangleShape }
            ?: RoundedCornerShape(size = SizeConstants.MediumSize),
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            onToggleExpand()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = SizeConstants.MediumSize)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.QuestionAnswer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(size = SizeConstants.LauncherIconSize)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                        .padding(all = SizeConstants.SmallSize)
                )

                LargeHorizontalSpacer()

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(weight = 1f)
                )

                GeneralTextButton(
                    onClick = { onToggleExpand() },
                    vectorIcon = Icons.Filled.ExpandMore,
                    modifier = Modifier.rotate(degrees = expandIconRotation),
                )
            }
            if (isExpanded) {
                SmallVerticalSpacer()
                HtmlText(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

