package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

private const val IconRequirementMessage: String = "Either icon or painter must be provided"

@Composable
internal fun IconContent(
    icon: ImageVector?,
    painter: Painter?,
    contentDescription: String?,
) {
    require(icon != null || painter != null) { IconRequirementMessage }

    val iconModifier: Modifier = Modifier.size(size = SizeConstants.ButtonIconSize)
    when {
        icon != null -> Icon(
            modifier = iconModifier,
            imageVector = icon,
            contentDescription = contentDescription
        )

        painter != null -> Icon(
            modifier = iconModifier,
            painter = painter,
            contentDescription = contentDescription
        )
    }
}
