package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.cards

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.model.OnboardingThemeChoice
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ThemeChoicePreviewCard(
    choice: OnboardingThemeChoice,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showTopIcon: Boolean = true,
    showPreview: Boolean = true,
    preview: @Composable BoxScope.() -> Unit,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    val cardContainerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

    val cardElevation = if (isSelected) {
        SizeConstants.ExtraSmallSize
    } else {
        SizeConstants.ExtraTinySize / 2
    }

    val cardShape = RoundedCornerShape(SizeConstants.LargeSize)
    val previewShape = RoundedCornerShape(
        topStart = SizeConstants.MediumSize,
        topEnd = SizeConstants.MediumSize,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    Column(
        modifier = modifier
            .bounceClick()
            .semantics {
                role = Role.RadioButton
                selected = isSelected
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
            },
            shape = cardShape,
            colors = CardDefaults.cardColors(containerColor = cardContainerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.SmallSize),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
            ) {
                ExtraSmallVerticalSpacer()

                if (showTopIcon) {
                    Icon(
                        imageVector = choice.icon,
                        contentDescription = choice.displayName,
                        modifier = Modifier
                            .size(SizeConstants.LargeSize + SizeConstants.SmallSize),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SizeConstants.NinetySixSize)
                        .clip(previewShape)
                        .padding(
                            start = SizeConstants.ExtraTinySize * 2,
                            end = SizeConstants.ExtraTinySize * 2,
                            top = SizeConstants.ExtraTinySize * 2,
                            bottom = SizeConstants.ZeroSize
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (showPreview) preview()
                }
            }
        }

        Spacer(modifier = Modifier.height(SizeConstants.SmallSize))

        Text(
            text = choice.displayName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize))

        Text(
            text = choice.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
