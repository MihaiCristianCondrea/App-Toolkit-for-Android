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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dropdown

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event

/**
 * A dropdown menu row with the toolkit's click feedback, bounce, and GA4 logging.
 *
 * [icon] is optional so that value pickers, where every row would carry the same glyph, look the
 * same as action menus without one.
 *
 * @param text Row label.
 * @param onClick Invoked after feedback and analytics, on every click.
 * @param modifier The [Modifier] applied to the row.
 * @param icon Optional leading icon.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun CommonDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    DropdownMenuItem(
        text = { Text(text = text) },
        leadingIcon = icon?.let { { Icon(imageVector = it, contentDescription = null) } },
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            firebaseController.logGa4Event(ga4Event)
            onClick()
        },
        modifier = modifier
            .clip(CircleShape)
            .bounceClick()
    )
}

/** Overload for rows whose label is a string resource. */
@Composable
fun CommonDropdownMenuItem(
    textResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    CommonDropdownMenuItem(
        text = stringResource(id = textResId),
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        firebaseController = firebaseController,
        ga4Event = ga4Event,
    )
}
