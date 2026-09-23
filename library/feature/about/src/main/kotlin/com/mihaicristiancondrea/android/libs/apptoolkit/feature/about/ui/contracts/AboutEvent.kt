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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/**
 * User driven events from the About screen UI.
 */
sealed interface AboutEvent : UiEvent {
    data object Load : AboutEvent

    /**
     * Requests a clipboard write of [text] under [label].
     *
     * @param label Clipboard entry label, resolved by the screen that raised the event.
     * @param text The exact text to place on the clipboard.
     * @param successMessage Confirmation to show once the write succeeds. When `null` the generic
     * copied-to-clipboard message is used.
     */
    data class CopyToClipboard(
        val label: String,
        val text: String,
        val successMessage: UiTextHelper? = null,
    ) : AboutEvent

    data object DismissSnackbar : AboutEvent

    /** The version was tapped enough times to set off the easter egg. */
    data object EasterEggFound : AboutEvent
}
