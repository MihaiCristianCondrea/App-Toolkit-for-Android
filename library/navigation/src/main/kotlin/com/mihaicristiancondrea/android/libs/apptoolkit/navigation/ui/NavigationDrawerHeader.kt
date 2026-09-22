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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

/**
 * The app's logo and name, for the top of a navigation drawer.
 *
 * Both are resource ids rather than rendered values so the header stays Compose-stable and can be
 * passed straight to [NavigationDrawerSheet].
 *
 * @property title String resource naming the app, usually its product name rather than its
 * launcher label.
 * @property logo Drawable resource holding the app's logo. It is drawn untinted, as an image, so a
 * multi-colour logo keeps its colours.
 */
@Immutable
data class NavigationDrawerBranding(
    @param:StringRes val title: Int,
    @param:DrawableRes val logo: Int,
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
 */
@Composable
fun NavigationDrawerHeader(
    branding: NavigationDrawerBranding,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    NavigationDrawerHeader(
        title = stringResource(id = branding.title),
        logo = painterResource(id = branding.logo),
        modifier = modifier,
        titleStyle = titleStyle,
    )
}

/**
 * Draws a logo beside a name, as the first row of a navigation drawer.
 *
 * Takes the already-resolved values, for a host whose logo or name is not a static resource.
 *
 * @param title Name shown beside the logo.
 * @param logo Logo drawn at the start of the row, untinted.
 * @param modifier The [Modifier] applied to the row.
 * @param titleStyle Typography of the name, which also sets the logo's size.
 */
@Composable
fun NavigationDrawerHeader(
    title: String,
    logo: Painter,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    val logoSize: Dp = with(LocalDensity.current) { titleStyle.lineHeight.toDp() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues = NavigationDrawerItemDefaults.ItemPadding)
            .padding(horizontal = SizeConstants.MediumSize),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = logo,
            contentDescription = null,
            modifier = Modifier.size(size = logoSize),
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
