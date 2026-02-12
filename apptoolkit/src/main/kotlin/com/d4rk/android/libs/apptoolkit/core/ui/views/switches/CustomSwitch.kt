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

package com.d4rk.android.libs.apptoolkit.core.ui.views.switches

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun CustomSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    checkIcon: ImageVector = Icons.Filled.Check,
    uncheckIcon: ImageVector = Icons.Filled.Close
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    Switch(
        modifier = modifier,
        checked = checked,
        enabled = enabled,
        onCheckedChange = { isChecked ->
            if (!enabled) return@Switch
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(
                hapticFeedbackType = if (checked) {
                    HapticFeedbackType.ToggleOn
                } else {
                    HapticFeedbackType.ToggleOff
                }
            )
            onCheckedChange(isChecked)
        },
        thumbContent = {
            AnimatedContent(
                targetState = checked,
                transitionSpec = {
                    if (targetState) {
                        slideInVertically { height: Int -> height } + fadeIn() togetherWith
                                slideOutVertically { height: Int -> -height } + fadeOut()
                    } else {
                        slideInVertically { height: Int -> -height } + fadeIn() togetherWith
                                slideOutVertically { height: Int -> height } + fadeOut()
                    } using SizeTransform(clip = false)
                },
                label = "SwitchIconAnimation"
            ) { targetChecked: Boolean ->
                Icon(
                    imageVector = if (targetChecked) checkIcon else uncheckIcon,
                    contentDescription = null,
                    modifier = Modifier.size(size = SizeConstants.SwitchIconSize),
                )
            }
        }
    )
}
