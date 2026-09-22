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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconContent

/**
 * The app's logo and name, for the top of a navigation drawer.
 *
 * The logo is a [ToolkitIcon], the same slot every other toolkit component takes, so an app can name
 * itself with a drawable, a Compose vector, a bitmap resolved at runtime, or an animated mark
 * without this component knowing which. The title stays a string resource so the pair is
 * Compose-stable and can be passed straight to [NavigationDrawerSheet].
 *
 * @property title String resource naming the app, usually its product name rather than its
 * launcher label.
 * @property logo The app's mark. Authored colours are kept unless the caller tints it, so a
 * multi-colour logo arrives intact. Prefer artwork cropped to the mark: a launcher foreground still
 * carries its adaptive-icon safe zone, which renders as padding here and leaves the logo looking
 * smaller than the title beside it.
 */
@Immutable
data class NavigationDrawerBranding(
    @param:StringRes val title: Int,
    val logo: ToolkitIcon,
)

/**
 * Draws an app's logo beside its name, as the first row of a navigation drawer.
 *
 * The logo is sized to the line height of [titleStyle] rather than to a fixed dimension, so the
 * pair stays optically balanced when the person scales their font size up.
 *
 * @param branding The logo and name to draw.
 * @param modifier The [Modifier] applied to the row.
 * @param titleStyle Typography of the name, which also sets the logo's size.
 * @param logoTint Tint applied to the logo. Unspecified by default, which keeps a brand mark's own
 * colours; pass a colour only for a monochrome logo that should follow the theme.
 */
@Composable
fun NavigationDrawerHeader(
    branding: NavigationDrawerBranding,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    logoTint: Color = Color.Unspecified,
) {
    NavigationDrawerHeader(
        title = stringResource(id = branding.title),
        logo = branding.logo,
        modifier = modifier,
        titleStyle = titleStyle,
        logoTint = logoTint,
    )
}

/**
 * Draws a logo beside a name, as the first row of a navigation drawer.
 *
 * Takes the already-resolved title, for a host whose name is not a static string resource.
 *
 * @param title Name shown beside the logo.
 * @param logo The app's mark, drawn at the start of the row.
 * @param modifier The [Modifier] applied to the row.
 * @param titleStyle Typography of the name, which also sets the logo's size.
 * @param logoTint Tint applied to the logo; unspecified keeps its own colours.
 */
@Composable
fun NavigationDrawerHeader(
    title: String,
    logo: ToolkitIcon,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    logoTint: Color = Color.Unspecified,
) {
    val logoSize: Dp = with(LocalDensity.current) { titleStyle.lineHeight.toDp() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues = NavigationDrawerItemDefaults.ItemPadding)
            .padding(horizontal = SizeConstants.MediumSize),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ToolkitIconContent(
            icon = logo,
            contentDescription = null,
            modifier = Modifier.size(size = logoSize),
            tint = logoTint,
        )

        Spacer(modifier = Modifier.width(width = SizeConstants.MediumSize))

        Text(
            text = title,
            style = titleStyle,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}
