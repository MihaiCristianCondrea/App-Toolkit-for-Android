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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier

/**
 * How tall a dialog's body should be.
 *
 * Dialogs that list a handful of options were sized as a fixed fraction of the screen, so a dialog
 * with three choices reserved the same height as one with ten and left most of it empty. This makes
 * that a choice rather than a constant.
 */
@Immutable
sealed interface DialogContentSizing {

    /**
     * Height follows the content, up to whatever the dialog itself allows.
     *
     * Scrollable bodies still scroll: they measure to their content and are clamped by the dialog's
     * own maximum height, so a long list stays scrollable while a short one stops being padded out.
     */
    @Immutable
    data object WrapContent : DialogContentSizing

    /**
     * Height is [fraction] of the available screen height, regardless of content.
     *
     * This is the behaviour dialogs had before [WrapContent] existed. Pass it to keep a dialog
     * looking exactly as it did.
     *
     * @param fraction Portion of the available height to occupy, in `0f..1f`.
     */
    @Immutable
    data class FractionOfScreen(val fraction: Float) : DialogContentSizing
}

/** Applies [sizing] to a dialog body. */
fun Modifier.dialogContentHeight(sizing: DialogContentSizing): Modifier = when (sizing) {
    is DialogContentSizing.WrapContent -> this
    is DialogContentSizing.FractionOfScreen -> this.fillMaxHeight(fraction = sizing.fraction)
}
