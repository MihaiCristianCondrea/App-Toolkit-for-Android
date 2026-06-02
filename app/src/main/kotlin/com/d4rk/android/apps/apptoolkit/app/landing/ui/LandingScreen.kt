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

package com.d4rk.android.apps.apptoolkit.app.landing.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import java.util.Calendar

/** Landing page that highlights recent tools, favorite apps, quick actions, and updates. */
@Composable
fun LandingRoute(
    paddingValues: PaddingValues,
    onAppsClick: () -> Unit,
    onQuickToolsClick: () -> Unit,
) {
    LandingScreen(
        paddingValues = paddingValues,
        greeting = rememberGreeting(),
        onAppsClick = onAppsClick,
        onQuickToolsClick = onQuickToolsClick,
    )
}

/** Stateless Material 3 landing content rendered by the top-level app shell. */
@Composable
fun LandingScreen(
    paddingValues: PaddingValues,
    greeting: String,
    onAppsClick: () -> Unit,
    onQuickToolsClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = SizeConstants.LargeSize,
            top = paddingValues.calculateTopPadding() + SizeConstants.LargeSize,
            end = SizeConstants.LargeSize,
            bottom = paddingValues.calculateBottomPadding() + SizeConstants.LargeSize,
        ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        item {
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        item {
            LandingSectionCard(
                title = stringResource(id = R.string.landing_recent_tool),
                primaryText = stringResource(id = R.string.landing_recent_tool_bubble_level),
                supportingText = stringResource(id = R.string.landing_recent_tool_summary),
                icon = Icons.Outlined.Straighten,
                onClick = onQuickToolsClick,
            )
        }
        item {
            LandingSectionCard(
                title = stringResource(id = R.string.landing_favorite_app),
                primaryText = stringResource(id = R.string.landing_favorite_app_cleaner),
                supportingText = stringResource(id = R.string.landing_favorite_app_summary),
                icon = Icons.Outlined.Apps,
                onClick = onAppsClick,
            )
        }
        item {
            QuickActionsCard(onQuickToolsClick = onQuickToolsClick)
        }
        item {
            LandingSectionCard(
                title = stringResource(id = R.string.landing_latest_update),
                primaryText = stringResource(id = R.string.landing_latest_update_toolkit_2),
                supportingText = stringResource(id = R.string.landing_latest_update_summary),
                icon = Icons.Outlined.Update,
                onClick = onQuickToolsClick,
            )
        }
        item {
            NavigationBarSpacer()
        }
    }
}

@Composable
private fun QuickActionsCard(onQuickToolsClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onQuickToolsClick,
    ) {
        Column(
            modifier = Modifier.padding(SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        ) {
            Text(
                text = stringResource(id = R.string.landing_quick_actions),
                style = MaterialTheme.typography.titleMedium,
            )
            LandingActionRow(
                icon = Icons.Outlined.Casino,
                text = stringResource(id = R.string.tile_coin_flip_title),
            )
            LandingActionRow(
                icon = Icons.Outlined.Explore,
                text = stringResource(id = R.string.tile_compass_title),
            )
            LandingActionRow(
                icon = Icons.Outlined.WbSunny,
                text = stringResource(id = R.string.landing_quick_action_flashlight),
            )
        }
    }
}

@Composable
private fun LandingSectionCard(
    title: String,
    primaryText: String,
    supportingText: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(SizeConstants.LargeSize),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = primaryText,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun LandingActionRow(
    icon: ImageVector,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun rememberGreeting(): String {
    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greetingResId = when (hour) {
        in 5..11 -> R.string.landing_good_morning
        in 12..16 -> R.string.landing_good_afternoon
        in 17..21 -> R.string.landing_good_evening
        else -> R.string.landing_good_night
    }
    return stringResource(id = greetingResId)
}
