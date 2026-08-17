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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.SmallFloatingActionButton
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseSection

@Composable
fun FabShowcase(
    firebaseController: FirebaseController,
    onLogEvent: (String, String?) -> Ga4EventData,
) {
    val iconContentDescription = stringResource(id = R.string.components_icon_content_description)

    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_fabs),
        icon = Icons.Filled.AutoAwesome,
    )
    ShowcaseSection {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(SizeConstants.LargeSize),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedExtendedFloatingActionButton(
                    onClick = {},
                    visible = true,
                    expanded = true,
                    icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.components_fab_extended)) },
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("fab", "extended"),
                )
                AnimatedFloatingActionButton(
                    isVisible = true,
                    icon = Icons.Filled.Add,
                    contentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("fab", "animated"),
                )
                SmallFloatingActionButton(
                    isVisible = true,
                    isExtended = true,
                    icon = Icons.Filled.Add,
                    contentDescription = iconContentDescription,
                    onClick = {},
                    onLogClick = {
                        firebaseController.logGa4Event(
                            onLogEvent("fab", "small"),
                        )
                    },
                )
            }
        }
    }
}
