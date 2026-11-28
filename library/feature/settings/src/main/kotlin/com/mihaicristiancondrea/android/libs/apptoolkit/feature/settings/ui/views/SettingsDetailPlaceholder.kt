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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.HelpScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR

/**
 * What the detail side of the settings list shows before a category is chosen: the settings
 * illustration, the app's name, a hint, and a way to Help and feedback.
 */
@Composable
fun SettingsDetailPlaceholder(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalShellNavigator.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(
                paddingValues = contentPadding(
                    extra = PaddingValues(all = MaterialTheme.spacing.large),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.widthIn(max = PlaceholderMaxWidth),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Column(
                modifier = Modifier.padding(all = MaterialTheme.spacing.extraLargeCompact),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.il_settings),
                    contentDescription = null,
                    modifier = Modifier.size(size = 258.dp),
                )
                LargeVerticalSpacer()
                Text(
                    text = stringResource(id = CommonR.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                SmallVerticalSpacer()
                Text(
                    text = stringResource(id = R.string.settings_placeholder_description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
            GeneralButton(
                style = GeneralButtonStyle.Outlined,
                modifier = Modifier
                    .padding(
                        start = MaterialTheme.spacing.extraLargeCompact,
                        end = MaterialTheme.spacing.extraLargeCompact,
                        bottom = MaterialTheme.spacing.extraLargeCompact,
                    )
                    .align(alignment = Alignment.Start),
                onClick = { navigator.navigate(key = HelpScreen) },
                icon = ToolkitIcon.Vector(imageVector = Icons.AutoMirrored.Outlined.ContactSupport),
                label = stringResource(id = R.string.get_help),
            )
        }
    }
}

private val PlaceholderMaxWidth = 516.dp
