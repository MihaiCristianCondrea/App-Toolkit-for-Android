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

import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Link

/**
 * One action of the Markdown formatting bar.
 *
 * [analyticsName] is the identity a host reports the action under. It is part of the contract, so
 * an event logged today keeps meaning the same thing after the enum is reordered or renamed.
 */
enum class MarkdownFormatAction(val analyticsName: String) {
    Bold(analyticsName = "bold"),
    Italic(analyticsName = "italic"),
    Code(analyticsName = "code"),
    CodeBlock(analyticsName = "code_block"),
    BulletList(analyticsName = "bullet_list"),
    NumberedList(analyticsName = "numbered_list"),
    Quote(analyticsName = "quote"),
    Link(analyticsName = "link"),
}
