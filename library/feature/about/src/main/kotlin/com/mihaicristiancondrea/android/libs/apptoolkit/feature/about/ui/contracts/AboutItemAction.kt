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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText

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
     * [UiText] rather than `String` so a row whose value is a string resource, such as the
     * application name, is as copyable as one holding a formatted value.
     *
     * @property label Clipboard entry label, also used as the copied item's name.
     * @property text The exact text written to the clipboard.
     * @property successMessage Confirmation shown once the copy succeeds. When `null` the screen
     * falls back to the generic copied-to-clipboard message.
     */
    data class CopyToClipboard(
        val label: UiText,
        val text: UiText,
        val successMessage: UiText? = null,
    ) : AboutItemAction
}
