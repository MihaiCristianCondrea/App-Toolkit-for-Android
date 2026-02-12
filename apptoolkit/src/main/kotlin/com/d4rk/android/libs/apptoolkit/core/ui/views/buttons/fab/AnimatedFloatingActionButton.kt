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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick

/**
 * A Composable function that displays a Floating Action Button with an animated appearance.
 * The button animates in and out of view based on the `isVisible` parameter.
 * It also includes haptic feedback and a click sound effect on interaction.
 *
 * @param modifier The [Modifier] to be applied to the button.
 * @param isVisible A boolean that controls the visibility of the FAB. If true, the button animates in; if false, it animates out.
 * @param icon The [ImageVector] to be displayed inside the FAB.
 * @param contentDescription Text used by accessibility services to describe what the icon represents.
 * @param onClick A lambda function to be invoked when the button is clicked.
 * @param feedback The feedback configuration for sound and haptics.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedFloatingActionButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    feedback: ButtonFeedback = ButtonFeedback(),
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val haptics = LocalHapticFeedback.current
    val view = LocalView.current
    val checkedState = rememberSaveable { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        ToggleFloatingActionButton(
            checked = checkedState.value,
            onCheckedChange = { newChecked ->
                feedback.performClick(view = view, hapticFeedback = haptics)
                firebaseController.logGa4Event(ga4Event)
                checkedState.value = newChecked
                onClick()
            },
            modifier = modifier.bounceClick()
        ) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    }
}
