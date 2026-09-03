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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.settings.ui.views.dropdowns

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.ui.HelpActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dropdown.CommonDropdownMenuItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as AboutR

/**
 * Top-app-bar actions for the settings root.
 *
 * Help belongs to the screen the settings tree starts at, not to each page inside it. This first
 * hung off `GeneralSettingsScreen`, which is the host every standalone sub-page renders through,
 * Display, Security & privacy, Advanced, About, so the overflow appeared on all of them and on
 * none of the places a reader would look for it, while the root Settings list had no actions at all.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsMenuActions() {
    val context = LocalContext.current
    val showMenu = rememberSaveable { mutableStateOf(value = false) }
    val rotation by animateFloatAsState(
        targetValue = if (showMenu.value) 90f else 0f,
        label = "SettingsMenuRotation",
    )

    AnimatedIconButtonDirection(
        modifier = Modifier.graphicsLayer { rotationZ = rotation },
        fromRight = true,
        contentDescription = null,
        icon = Icons.Default.MoreVert,
        onClick = { showMenu.value = true },
    )

    DropdownMenu(
        expanded = showMenu.value,
        shape = MaterialTheme.shapes.largeIncreased,
        onDismissRequest = { showMenu.value = false },
    ) {
        CommonDropdownMenuItem(
            textResId = AboutR.string.help_and_feedback,
            icon = Icons.AutoMirrored.Outlined.HelpOutline,
            onClick = {
                showMenu.value = false
                context.openActivity(HelpActivity::class.java)
            },
        )
    }
}
