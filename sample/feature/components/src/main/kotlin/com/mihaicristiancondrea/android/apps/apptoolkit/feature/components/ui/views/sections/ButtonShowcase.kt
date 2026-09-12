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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.sections

import android.graphics.Bitmap
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSurface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonIconPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ButtonShowcase(
    firebaseController: FirebaseController,
    onLogEvent: (String, String?) -> Ga4EventData,
) {
    val iconContentDescription = stringResource(id = R.string.components_icon_content_description)
    val runtimeBitmapIcon = remember {
        Bitmap.createBitmap(
            intArrayOf(
                android.graphics.Color.rgb(66, 133, 244),
                android.graphics.Color.rgb(52, 168, 83),
                android.graphics.Color.rgb(251, 188, 4),
                android.graphics.Color.rgb(234, 67, 53),
            ),
            2,
            2,
            Bitmap.Config.ARGB_8888,
        ).asImageBitmap()
    }

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
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.StarOutline),
                    contentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "primary_icon"),
                )
                GeneralButton(
                    label = stringResource(id = R.string.components_button_primary),
                    icon = ToolkitIcon.Bitmap(imageBitmap = runtimeBitmapIcon),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "primary_bitmap_icon"),
                )
                GeneralButton(
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.StarOutline),
                    contentDescription = iconContentDescription,
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
                GeneralButton(
                    style = GeneralButtonStyle.Tonal,
                    label = stringResource(id = R.string.components_button_tonal),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "tonal"),
                )
                GeneralButton(
                    style = GeneralButtonStyle.Tonal,
                    label = stringResource(id = R.string.components_button_tonal),
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Favorite),
                    contentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "tonal_icon"),
                )
                GeneralButton(
                    style = GeneralButtonStyle.Tonal,
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Favorite),
                    contentDescription = iconContentDescription,
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
                GeneralButton(
                    style = GeneralButtonStyle.Outlined,
                    label = stringResource(id = R.string.components_button_outlined),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "outlined"),
                )
                GeneralButton(
                    style = GeneralButtonStyle.Outlined,
                    label = stringResource(id = R.string.components_button_outlined),
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.StarOutline),
                    contentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "outlined_icon"),
                )
                GeneralButton(
                    style = GeneralButtonStyle.Outlined,
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.StarOutline),
                    contentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "outlined_icon_only"),
                )
            }
        }
        ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
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
                GeneralButton(
                    style = GeneralButtonStyle.Text,
                    label = stringResource(id = R.string.components_button_text),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "text"),
                )
                GeneralButton(
                    style = GeneralButtonStyle.Text,
                    label = stringResource(id = R.string.components_button_text),
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Favorite),
                    contentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "text_icon"),
                )
                GeneralButton(
                    style = GeneralButtonStyle.Text,
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Favorite),
                    contentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "text_icon_only"),
                )
                AnimatedIconButtonDirection(
                    icon = ToolkitIcon.Vector(imageVector = Icons.Filled.MoreVert),
                    contentDescription = iconContentDescription,
                    onClick = {},
                    fromRight = true,
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("button", "animated_direction"),
                )
            }
        }
        // Each shape gets the whole size range, smallest first, so the scale is comparable across
        // rows rather than demonstrated once on a single shape.
        ButtonSizeGroup(
            titleResId = R.string.components_button_group_icon_sizes,
            titleColor = MaterialTheme.colorScheme.primary,
            position = GroupedItemPosition.MIDDLE,
        ) { measurements, sizeLabel ->
            GeneralButton(
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.StarOutline),
                contentDescription = sizeLabel,
                measurements = measurements,
                onClick = {},
                firebaseController = firebaseController,
                ga4Event = onLogEvent("button", "icon_size_${measurements.sizeVariant()}"),
            )
        }
        ButtonSizeGroup(
            titleResId = R.string.components_button_group_text_sizes,
            titleColor = MaterialTheme.colorScheme.secondary,
            position = GroupedItemPosition.MIDDLE,
        ) { measurements, sizeLabel ->
            GeneralButton(
                style = GeneralButtonStyle.Tonal,
                label = sizeLabel,
                measurements = measurements,
                onClick = {},
                firebaseController = firebaseController,
                ga4Event = onLogEvent("button", "text_size_${measurements.sizeVariant()}"),
            )
        }
        ButtonSizeGroup(
            titleResId = R.string.components_button_group_text_icon_sizes,
            titleColor = MaterialTheme.colorScheme.tertiary,
            position = GroupedItemPosition.MIDDLE,
        ) { measurements, sizeLabel ->
            GeneralButton(
                style = GeneralButtonStyle.Outlined,
                label = sizeLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Favorite),
                measurements = measurements,
                onClick = {},
                firebaseController = firebaseController,
                ga4Event = onLogEvent("button", "text_icon_size_${measurements.sizeVariant()}"),
            )
        }
        ButtonSizeGroup(
            titleResId = R.string.components_button_group_trailing_icon_sizes,
            titleColor = MaterialTheme.colorScheme.primary,
            position = GroupedItemPosition.LAST,
        ) { measurements, sizeLabel ->
            GeneralButton(
                style = GeneralButtonStyle.Elevated,
                label = sizeLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.StarOutline),
                iconPosition = ButtonIconPosition.End,
                measurements = measurements,
                onClick = {},
                firebaseController = firebaseController,
                ga4Event = onLogEvent("button", "trailing_icon_size_${measurements.sizeVariant()}"),
            )
        }
    }
}

/**
 * One showcase card rendering [button] once per expressive size class, smallest first.
 *
 * @param button Receives the size class and its resolved name, which doubles as the button's label
 *   or content description so each button says which size it is.
 */
@Composable
private fun ButtonSizeGroup(
    titleResId: Int,
    titleColor: Color,
    position: GroupedItemPosition,
    button: @Composable (ButtonMeasurements, String) -> Unit,
) {
    ShowcaseSurface(position = position) {
        Text(
            text = stringResource(id = titleResId),
            style = MaterialTheme.typography.labelLarge,
            color = titleColor,
            fontWeight = FontWeight.Bold,
        )
        SmallVerticalSpacer()
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            ButtonSizes.forEach { (measurements, labelResId) ->
                button(measurements, stringResource(id = labelResId))
            }
        }
    }
}

/** GA4 variant suffix for a size class. */
private fun ButtonMeasurements.sizeVariant(): String = name.lowercase()

/** Every expressive size class, in ascending order, paired with its label. */
private val ButtonSizes: ImmutableList<Pair<ButtonMeasurements, Int>> = persistentListOf(
    ButtonMeasurements.ExtraSmall to R.string.components_button_size_extra_small,
    ButtonMeasurements.Small to R.string.components_button_size_small,
    ButtonMeasurements.Medium to R.string.components_button_size_medium,
    ButtonMeasurements.Large to R.string.components_button_size_large,
    ButtonMeasurements.ExtraLarge to R.string.components_button_size_extra_large,
)
