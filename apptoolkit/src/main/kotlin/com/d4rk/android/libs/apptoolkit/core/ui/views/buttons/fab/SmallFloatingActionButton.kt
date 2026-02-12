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

package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab

import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick

/**
 * A customizable small floating action button that animates its visibility.
 *
 * This composable provides a small floating action button with animated visibility, scaling in and out.
 * It also includes a click sound effect and a bounce click animation.
 *
 * @param modifier Modifier to be applied to the button.
 * @param isVisible Controls the visibility of the button. The button is only visible if this is true.
 * @param isExtended Controls if the button is extended. The button is only visible if this is true.
 * @param icon The icon to be displayed inside the button.
 * @param contentDescription Optional description of the icon for accessibility.
 * @param onClick The action to be performed when the button is clicked.
 * @param feedback The feedback configuration for sound and haptics.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@Composable
fun SmallFloatingActionButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isExtended: Boolean,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    feedback: ButtonFeedback = ButtonFeedback(),
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    AnimatedVisibility(
        visible = isVisible && isExtended,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        SmallFloatingActionButton(onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            firebaseController.logGa4Event(ga4Event)
            onClick()
        }, modifier = modifier.bounceClick()) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    }
}
