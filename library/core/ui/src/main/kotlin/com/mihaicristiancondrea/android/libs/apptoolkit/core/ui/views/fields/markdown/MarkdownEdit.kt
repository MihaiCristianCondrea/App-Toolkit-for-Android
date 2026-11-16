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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown

/**
 * Result of a formatting action: the new text and where the caret or the selection ends up.
 *
 * Selection is part of the contract because every action has to keep typing flowing: wrapping an
 * empty selection has to leave the caret between the markers, and wrapping a selection has to keep
 * the same words selected so the action can be toggled off again.
 */
internal data class MarkdownEdit(
    val text: String,
    val selectionStart: Int,
    val selectionEnd: Int,
)
