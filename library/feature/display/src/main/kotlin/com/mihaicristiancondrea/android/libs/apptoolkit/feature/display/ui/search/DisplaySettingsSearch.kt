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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.search

import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DisplaySettingsRoute

/**
 * The display settings rows the settings search finds, under the same conditions the page shows
 * them: nothing about a bottom bar without tabs, no banner style without a banner. The content
 * width row depends on the window's layout policy, which the search cannot see, so it is left out.
 */
internal val displaySettingsSearch = SettingsSearchProvider { graph ->
    val hasTabs = graph.tabs.isNotEmpty()
    fun entry(title: Int, summary: Int? = null) =
        SettingsSearchEntry(title = title, section = R.string.display, destination = DisplaySettingsRoute, summary = summary)
    listOfNotNull(
        entry(R.string.dark_theme),
        entry(R.string.dynamic_colors, R.string.summary_preference_settings_dynamic_colors)
            .takeIf { Build.VERSION.SDK_INT >= Build.VERSION_CODES.S },
        entry(R.string.shell_top_bar),
        entry(R.string.shell_hide_top_bar, R.string.shell_hide_top_bar_summary),
        entry(R.string.shell_navigation_tint).takeIf { hasTabs },
        entry(R.string.shell_banner_style).takeIf { graph.banner != null },
        entry(R.string.shell_navigation_bar).takeIf { hasTabs },
        entry(R.string.show_labels_on_bottom_bar, R.string.summary_preference_settings_show_labels_on_bottom_bar)
            .takeIf { hasTabs },
        entry(R.string.shell_hide_bottom_bar, R.string.shell_hide_bottom_bar_summary).takeIf { hasTabs },
        entry(R.string.shell_tab_transition).takeIf { graph.tabs.size > 1 },
        entry(R.string.shell_back_edge),
        entry(R.string.bounce_buttons, R.string.summary_preference_settings_bounce_buttons),
        entry(R.string.language, R.string.summary_preference_settings_language),
    )
}
