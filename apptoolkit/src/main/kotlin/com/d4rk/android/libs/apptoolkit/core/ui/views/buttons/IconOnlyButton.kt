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

package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons

import android.view.View
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick

internal enum class IconOnlyButtonStyle {
    Filled,
    FilledTonal,
    Outlined,
    Standard,
}

/**
 * Change rationale: icon-only button APIs were consolidated into the General* buttons to reduce
 * duplicate public composables while preserving the same visual styles and feedback behavior.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun IconOnlyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    iconContentDescription: String? = null,
    vectorIcon: ImageVector? = null,
    painterIcon: Painter? = null,
    feedback: ButtonFeedback,
    firebaseController: FirebaseController?,
    ga4Event: Ga4EventData?,
    style: IconOnlyButtonStyle,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    val onClickWithFeedback = {
        feedback.performClick(view = view, hapticFeedback = hapticFeedback)
        firebaseController.logGa4Event(ga4Event)
        onClick()
    }

    when (style) {
        IconOnlyButtonStyle.Filled -> androidx.compose.material3.FilledIconButton(
            onClick = onClickWithFeedback,
            enabled = enabled,
            modifier = modifier.bounceClick(),
            shapes = IconButtonDefaults.shapes(),
        ) {
            IconContent(
                icon = vectorIcon,
                painter = painterIcon,
                contentDescription = iconContentDescription
            )
        }
        IconOnlyButtonStyle.FilledTonal -> androidx.compose.material3.FilledTonalIconButton(
            onClick = onClickWithFeedback,
            enabled = enabled,
            modifier = modifier.bounceClick(),
            shapes = IconButtonDefaults.shapes(),
        ) {
            IconContent(
                icon = vectorIcon,
                painter = painterIcon,
                contentDescription = iconContentDescription
            )
        }
        IconOnlyButtonStyle.Outlined -> androidx.compose.material3.OutlinedIconButton(
            onClick = onClickWithFeedback,
            enabled = enabled,
            modifier = modifier.bounceClick(),
            shapes = IconButtonDefaults.shapes(),
        ) {
            IconContent(
                icon = vectorIcon,
                painter = painterIcon,
                contentDescription = iconContentDescription
            )
        }
        IconOnlyButtonStyle.Standard -> androidx.compose.material3.IconButton(
            onClick = onClickWithFeedback,
            enabled = enabled,
            modifier = modifier.bounceClick(),
            shapes = IconButtonDefaults.shapes(),
        ) {
            IconContent(
                icon = vectorIcon,
                painter = painterIcon,
                contentDescription = iconContentDescription
            )
        }
    }
}
