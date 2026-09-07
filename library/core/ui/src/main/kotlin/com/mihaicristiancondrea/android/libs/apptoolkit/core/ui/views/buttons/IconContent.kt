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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon

/**
 * Icon renderer shared by every button in this package.
 *
 * It sizes the icon and hands it to [AnimatedToolkitIcon], so a [ToolkitIcon.AnimatedVector] plays
 * whenever [clickCount] grows while a [ToolkitIcon.Vector] or [ToolkitIcon.Resource] simply draws.
 *
 * @param icon The icon to draw.
 * @param clickCount Clicks the owning button received while composed.
 * @param contentDescription Accessibility description of the icon.
 * @param size Icon size.
 */
@Composable
internal fun IconContent(
    icon: ToolkitIcon,
    clickCount: Int,
    contentDescription: String?,
    size: Dp = SizeConstants.ButtonIconSize,
    tint: Color? = null,
) {
    AnimatedToolkitIcon(
        icon = icon,
        tint = tint ?: LocalContentColor.current,
        clickCount = clickCount,
        contentDescription = contentDescription,
        modifier = Modifier.size(size = size),
    )
}
