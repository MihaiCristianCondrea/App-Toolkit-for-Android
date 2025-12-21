package com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab

import android.view.SoundEffectConstants
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

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
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedFloatingActionButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
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
                view.playSoundEffect(SoundEffectConstants.CLICK)
                haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                checkedState.value = newChecked
                onClick()
            },
            modifier = modifier.bounceClick()
        ) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    }
}