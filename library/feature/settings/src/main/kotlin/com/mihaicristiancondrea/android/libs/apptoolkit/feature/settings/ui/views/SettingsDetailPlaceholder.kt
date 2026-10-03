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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalContentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR

/**
 * What the detail side of the settings list shows before a category is chosen: the settings
 * illustration, the app's name, a hint, and a way to Help and feedback.
 */
@Composable
fun SettingsDetailPlaceholder() {
    val navigator = LocalShellNavigator.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalContentPadding.current)
            .verticalScroll(rememberScrollState())
            .padding(SizeConstants.LargeSize),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.widthIn(max = PlaceholderMaxWidth),
            shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize),
        ) {
            Column(
                modifier = Modifier.padding(all = SizeConstants.MediumSize * 2),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.il_settings),
                    contentDescription = null,
                    modifier = Modifier.size(SizeConstants.TwoHundredFiftyEightSize),
                )
                LargeVerticalSpacer()
                Text(
                    text = stringResource(CommonR.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                SmallVerticalSpacer()
                Text(
                    text = stringResource(R.string.settings_placeholder_description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
            GeneralButton(
                style = GeneralButtonStyle.Outlined,
                modifier = Modifier
                    .padding(
                        start = SizeConstants.MediumSize * 2,
                        end = SizeConstants.MediumSize * 2,
                        bottom = SizeConstants.MediumSize * 2,
                    )
                    .align(Alignment.Start),
                onClick = { navigator.navigate(HelpRoute) },
                icon = ToolkitIcon.Vector(Icons.AutoMirrored.Outlined.ContactSupport),
                label = stringResource(R.string.get_help),
            )
        }
    }
}

private val PlaceholderMaxWidth = SizeConstants.TwoHundredFiftyEightSize * 2
