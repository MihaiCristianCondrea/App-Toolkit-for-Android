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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperStateLine
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.navigation.developerOptionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.DeveloperOptionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun DeveloperOptionsScreenContentPreview() {
    val graph = remember {
        ShellGraphBuilder(appTitle = R.string.shell_developer_options).apply {
            developerOptionsScreen()
            start(key = DeveloperOptionsScreen)
        }.build()
    }
    MaterialTheme {
        DeveloperOptionsScreenContent(
            settings = ShellSettings(),
            graph = graph,
            capabilities = ShellCapabilities(
                tabCount = 0,
                startOptionCount = 1,
                declaresBanner = false,
                declaresPlayer = false,
                hasFramedPages = true,
                hasBackNavigation = false,
                layoutReachesBottomBar = false,
                layoutReachesWideNavigation = false,
                hasContentWidthLimit = false,
            ),
            liveState = persistentListOf(
                DeveloperStateLine(title = "Window", summary = "Phone"),
            ),
            onChange = {},
            onReset = {},
        )
    }
}
