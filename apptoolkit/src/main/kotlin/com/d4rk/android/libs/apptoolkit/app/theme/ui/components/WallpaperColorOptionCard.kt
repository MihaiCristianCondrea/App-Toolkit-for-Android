package com.d4rk.android.libs.apptoolkit.app.theme.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

@Composable
fun WallpaperColorOptionCard(
    colors: WallpaperSwatchColors,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardSize: Dp = 72.dp,
    swatchSize: Dp = 44.dp,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
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
                indication = null, // set to rememberRipple() if you want ripple
                role = Role.RadioButton,
                onClick = onClick
            ),
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer, // looks like that light rounded card
        border = BorderStroke(2.dp, borderColor),
    ) {
        Box(contentAlignment = Alignment.Center) {
            MaterialYouCircleSwatch(
                primary = colors.primary,
                secondary = colors.secondary,
                tertiary = colors.tertiary,
                modifier = Modifier.size(swatchSize)
            )
        }
    }
}