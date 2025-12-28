package com.d4rk.android.libs.apptoolkit.app.theme.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.d4rk.android.libs.apptoolkit.app.theme.domain.model.WallpaperSwatchColors
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun WallpaperColorOptionCard(
    colors: WallpaperSwatchColors,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardSize: Dp = SizeConstants.SeventyTwoSize,
    swatchSize: Dp = SizeConstants.FortyFourSize,
    shape: RoundedCornerShape = RoundedCornerShape(SizeConstants.LargeSize),
    showSeasonalBadge: Boolean = false,
) {
    val borderColor = animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "swatchBorderColor"
    ).value

    Surface(
        modifier = modifier
            .size(cardSize)
            .bounceClick()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.RadioButton,
                onClick = onClick
            ),
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(SizeConstants.ExtraTinySize, borderColor),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (showSeasonalBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(all = SizeConstants.ExtraTinySize)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape
                        )
                        .padding(all = SizeConstants.ExtraTinySize + SizeConstants.ExtraTinySize / 2)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AcUnit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(SizeConstants.LargeSize)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                MaterialYouCircleSwatch(
                    primary = colors.primary,
                    secondary = colors.secondary,
                    tertiary = colors.tertiary,
                    selected = selected,
                    modifier = Modifier.size(swatchSize)
                )
            }
        }
    }
}
