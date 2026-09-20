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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition

/**
 * Stable keys for items displayed on the About screen.
 */
object AboutItemKey {
    const val HEADER_APP_INFO: String = "header_app_info"
    const val APP_NAME: String = "app_name"
    const val APP_BUILD_VERSION: String = "app_build_version"
    const val APP_TOOLKIT_VERSION: String = "app_toolkit_version"
    const val GOOGLE_PLAY_SERVICES_VERSION: String = "google_play_services_version"
    const val OSS_LICENSES: String = "oss_licenses"
    const val HEADER_DEVICE_INFO: String = "header_device_info"
    const val DEVICE_INFO: String = "device_info"
}

/**
 * Click interactions supported by About screen preference items.
 */
sealed interface AboutItemAction {
    /** Opens the open-source software licenses screen. */
    data object OpenLicenses : AboutItemAction

    /**
     * Copies [text] to the system clipboard under [label].
     *
     * The text travels with the action so the clipboard receives exactly what the item displays,
     * instead of a second lookup that could resolve under a different configuration. Both are
     * [UiTextHelper] rather than `String` so a row whose value is a string resource, such as the
     * application name, is as copyable as one holding a formatted value.
     *
     * @property label Clipboard entry label, also used as the copied item's name.
     * @property text The exact text written to the clipboard.
     * @property successMessage Confirmation shown once the copy succeeds. When `null` the screen
     * falls back to the generic copied-to-clipboard message.
     */
    data class CopyToClipboard(
        val label: UiTextHelper,
        val text: UiTextHelper,
        val successMessage: UiTextHelper? = null,
    ) : AboutItemAction
}

/**
 * Represents an entry displayed on the About screen.
 */
sealed interface AboutItem {
    /** Unique, stable identifier used for LazyColumn item keys and analytics. */
    val key: String

    /** A category title separating groups of preference items. */
    data class Header(
        override val key: String,
        val title: UiTextHelper,
    ) : AboutItem

    /**
     * An individual preference entry within a category card.
     *
     * @property countsVersionTap Taps on this row advance the hidden version-tap counter that
     * launches konfetti on the fifth one. It is a gesture layered on top of the row, not something
     * the row does instead of its [action], so it is a flag rather than an [AboutItemAction].
     */
    data class Preference(
        override val key: String,
        val title: UiTextHelper,
        val summary: UiTextHelper,
        val position: GroupedItemPosition = GroupedItemPosition.MIDDLE,
        val action: AboutItemAction? = null,
        val countsVersionTap: Boolean = false,
    ) : AboutItem
}
