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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.SmartButton
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseSurface
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralTextButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralTonalButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer

@Composable
fun ButtonShowcase(
    firebaseController: FirebaseController,
    onLogEvent: (String, String?) -> Ga4EventData,
) {
    val iconContentDescription = stringResource(id = R.string.components_icon_content_description)

    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_buttons),
        icon = Icons.Outlined.SmartButton,
    )
    ShowcaseSection {
        ShowcaseSurface(position = GroupedItemPosition.FIRST) {
            Text(
                text = stringResource(id = R.string.components_button_group_standard),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
            SmallVerticalSpacer()
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                GeneralButton(
                    label = stringResource(id = R.string.components_button_primary),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "primary"),
                )
                GeneralButton(
                    label = stringResource(id = R.string.components_button_primary),
                    vectorIcon = Icons.Outlined.StarOutline,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "primary_icon"),
                )
                GeneralButton(
                    vectorIcon = Icons.Outlined.StarOutline,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "primary_icon_only"),
                )
            }
        }
        ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
            Text(
                text = stringResource(id = R.string.components_button_group_tonal),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
            )
            SmallVerticalSpacer()
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                GeneralTonalButton(
                    label = stringResource(id = R.string.components_button_tonal),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "tonal"),
                )
                GeneralTonalButton(
                    label = stringResource(id = R.string.components_button_tonal),
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "tonal_icon"),
                )
                GeneralTonalButton(
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "tonal_icon_only"),
                )
            }
        }
        ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
            Text(
                text = stringResource(id = R.string.components_button_group_outlined),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
            )
            SmallVerticalSpacer()
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                GeneralOutlinedButton(
                    label = stringResource(id = R.string.components_button_outlined),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "outlined"),
                )
                GeneralOutlinedButton(
                    label = stringResource(id = R.string.components_button_outlined),
                    vectorIcon = Icons.Outlined.StarOutline,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "outlined_icon"),
                )
                GeneralOutlinedButton(
                    vectorIcon = Icons.Outlined.StarOutline,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "outlined_icon_only"),
                )
            }
        }
        ShowcaseSurface(position = GroupedItemPosition.LAST) {
            Text(
                text = stringResource(id = R.string.components_button_group_text_and_icon),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
            )
            SmallVerticalSpacer()
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                GeneralTextButton(
                    label = stringResource(id = R.string.components_button_text),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "text"),
                )
                GeneralTextButton(
                    label = stringResource(id = R.string.components_button_text),
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "text_icon"),
                )
                GeneralTextButton(
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "text_icon_only"),
                )
                AnimatedIconButtonDirection(
                    icon = Icons.Filled.MoreVert,
                    contentDescription = iconContentDescription,
                    onClick = {},
                    fromRight = true,
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "animated_direction"),
                )
            }
        }
    }
}
