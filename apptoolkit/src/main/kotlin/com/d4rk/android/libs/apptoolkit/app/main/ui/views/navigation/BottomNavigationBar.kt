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

package com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : StableNavKey> BottomNavigationBar(
    currentRoute: StableNavKey?,
    items: ImmutableList<BottomBarItem<T>>,
    onNavigate: (T) -> Unit,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    LocalContext.current

    val dataStore = rememberCommonDataStore()
    val showLabels: Boolean by dataStore
        .getShowBottomBarLabels()
        .collectAsStateWithLifecycle(initialValue = true)

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                alwaysShowLabel = showLabels,
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.icon,
                        contentDescription = stringResource(id = item.title),
                        modifier = Modifier.bounceClick()
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.title),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.basicMarquee()
                    )
                },
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    hapticFeedback.performHapticFeedback(
                        hapticFeedbackType = HapticFeedbackType.ContextClick
                    )

                    if (!selected) onNavigate(item.route)
                }
            )
        }
    }
}
