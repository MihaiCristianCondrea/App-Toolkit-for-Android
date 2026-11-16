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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields

/** How much a field knows about the Markdown its text is written in. */
enum class GeneralTextFieldMarkdown {

    /** Plain text. The field's own `visualTransformation` is used as given. */
    None,

    /** Markdown syntax is highlighted as it is typed. The text itself is never rewritten. */
    Highlight,

    /** Highlighting, plus the formatting bar below the field. */
    Editor,
}
