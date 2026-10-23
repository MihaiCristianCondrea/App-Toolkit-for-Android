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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutItemAction

/**
 * Represents an entry displayed on the About screen.
 */
sealed interface AboutItem {
    /** Unique, stable identifier used for LazyColumn item keys and analytics. */
    val key: String

    /** A category title separating groups of preference items. */
    data class Header(
        override val key: String,
        val title: UiText,
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
        val title: UiText,
        val summary: UiText,
        val position: GroupedItemPosition = GroupedItemPosition.MIDDLE,
        val action: AboutItemAction? = null,
        val countsVersionTap: Boolean = false,
    ) : AboutItem
}
