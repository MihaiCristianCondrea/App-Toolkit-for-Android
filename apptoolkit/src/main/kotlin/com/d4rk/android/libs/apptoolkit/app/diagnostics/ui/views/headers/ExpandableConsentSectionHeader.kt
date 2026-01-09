package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.views.headers

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.IconButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * A Composable that displays a header for a collapsible section, typically used for consent management.
 * It shows a title and an expand/collapse icon. Tapping the header triggers an animation,
 * sound, haptic feedback, and invokes the `onToggle` callback.
 *
 * @param title The text to be displayed as the header's title.
 * @param expanded A boolean indicating whether the section is currently expanded or collapsed.
 *   This determines which icon (ExpandLess or ExpandMore) is shown.
 * @param onToggle A lambda function that will be invoked when the user taps on the header,
 *   to toggle the expanded state.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpandableConsentSectionHeader(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clip(CircleShape)
            .clickable(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                onToggle()
            })
            .padding(
                horizontal = SizeConstants.LargeSize,
                vertical = SizeConstants.MediumSize
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        IconButton(
            onClick = onToggle,
            icon = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            iconContentDescription = if (expanded) stringResource(id = R.string.icon_desc_expand_less) else stringResource(
                id = R.string.icon_desc_expand_more
            ),
        )
    }
}
