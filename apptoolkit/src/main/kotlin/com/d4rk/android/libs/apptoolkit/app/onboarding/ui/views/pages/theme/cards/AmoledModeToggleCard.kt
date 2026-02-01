package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.cards

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tonality
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun AmoledModeToggleCard(
    isAmoledMode: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    val elevation =
        if (isAmoledMode) SizeConstants.ExtraSmallSize else SizeConstants.ExtraTinySize / 2
    val shape = RoundedCornerShape(SizeConstants.LargeSize)

    Card(
        onClick = {
            if (!enabled) return@Card
            view.playSoundEffect(SoundEffectConstants.CLICK)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
            onCheckedChange(!isAmoledMode)
        },
        enabled = enabled,
        modifier = (if (enabled) Modifier.bounceClick() else Modifier)
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.55f),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SizeConstants.MediumSize,
                    vertical = SizeConstants.LargeSize
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.amoled_mode),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(id = R.string.onboarding_amoled_mode_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LargeHorizontalSpacer()

            CustomSwitch(
                checked = isAmoledMode,
                onCheckedChange = { if (enabled) onCheckedChange(it) },
                checkIcon = Icons.Outlined.Contrast,
                uncheckIcon = Icons.Filled.Tonality
            )
        }
    }
}
