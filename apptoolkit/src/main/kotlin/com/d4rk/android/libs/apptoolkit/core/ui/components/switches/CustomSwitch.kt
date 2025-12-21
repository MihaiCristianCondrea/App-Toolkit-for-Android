package com.d4rk.android.libs.apptoolkit.core.ui.components.switches

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
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkIcon: ImageVector = Icons.Filled.Check,
    uncheckIcon: ImageVector = Icons.Filled.Close
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    Switch(modifier = modifier, checked = checked, onCheckedChange = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        hapticFeedback.performHapticFeedback(hapticFeedbackType = if (checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff)
        onCheckedChange(it)
    }, thumbContent = {
        AnimatedContent(
            targetState = checked, transitionSpec = {
                if (targetState) {
                    slideInVertically { height: Int -> height } + fadeIn() togetherWith slideOutVertically { height: Int -> -height } + fadeOut()
                } else {
                    slideInVertically { height: Int -> -height } + fadeIn() togetherWith slideOutVertically { height: Int -> height } + fadeOut()
                } using SizeTransform(clip = false)
            }, label = "SwitchIconAnimation"
        ) { targetChecked: Boolean ->
            Icon(
                imageVector = if (targetChecked) checkIcon else uncheckIcon,
                contentDescription = null,
                modifier = Modifier.size(size = SizeConstants.SwitchIconSize),
            )
        }
    })
}