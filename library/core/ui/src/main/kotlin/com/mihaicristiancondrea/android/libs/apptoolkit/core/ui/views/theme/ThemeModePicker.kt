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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.cards.ThemeChoicePreviewCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.DarkModePreview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.LightModePreview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews.SystemModePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val ThemeModes: ImmutableList<String> = persistentListOf(
    DataStoreNamesConstants.THEME_MODE_LIGHT,
    DataStoreNamesConstants.THEME_MODE_DARK,
    DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
)

/**
 * The theme mode choice: light, dark and follow system, each a card with a preview of the mode.
 * The theme settings page and the onboarding theme page both show it. A stored mode that is
 * neither light nor dark, including a blank one, selects follow system.
 *
 * @param selectedMode The stored `DataStoreNamesConstants.THEME_MODE_*` key.
 * @param onSelected Called with the key of the card the user picked.
 */
@Composable
fun ThemeModePicker(
    selectedMode: String,
    onSelected: (mode: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selected: String = when (selectedMode) {
        DataStoreNamesConstants.THEME_MODE_LIGHT, DataStoreNamesConstants.THEME_MODE_DARK -> selectedMode
        else -> DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        ThemeModes.forEach { mode ->
            ThemeModeCard(
                mode = mode,
                selected = selected == mode,
                onClick = { onSelected(mode) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ThemeModeCard(
    mode: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title: Int
    val description: Int
    val icon: ImageVector
    when (mode) {
        DataStoreNamesConstants.THEME_MODE_LIGHT -> {
            title = R.string.light_mode
            description = R.string.onboarding_theme_light_desc
            icon = Icons.Filled.LightMode
        }

        DataStoreNamesConstants.THEME_MODE_DARK -> {
            title = R.string.dark_mode
            description = R.string.onboarding_theme_dark_desc
            icon = Icons.Filled.DarkMode
        }

        else -> {
            title = R.string.follow_system
            description = R.string.onboarding_theme_system_desc
            icon = Icons.Filled.BrightnessAuto
        }
    }
    ThemeChoicePreviewCard(
        title = stringResource(id = title),
        description = stringResource(id = description),
        icon = icon,
        isSelected = selected,
        onClick = onClick,
        modifier = modifier,
        preview = {
            when (mode) {
                DataStoreNamesConstants.THEME_MODE_LIGHT -> LightModePreview(Modifier.fillMaxWidth())
                DataStoreNamesConstants.THEME_MODE_DARK -> DarkModePreview(Modifier.fillMaxWidth())
                else -> SystemModePreview(Modifier.fillMaxWidth())
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ThemeModePickerPreview() {
    MaterialTheme {
        ThemeModePicker(
            selectedMode = DataStoreNamesConstants.THEME_MODE_DARK,
            onSelected = {},
        )
    }
}
