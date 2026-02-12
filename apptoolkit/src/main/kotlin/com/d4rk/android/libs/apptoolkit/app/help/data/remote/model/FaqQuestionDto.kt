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

package com.d4rk.android.libs.apptoolkit.app.help.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing a frequently asked question (FAQ) item.
 *
 * @property id The unique identifier for the FAQ question.
 * @property icon The optional URL or identifier for an icon associated with the question.
 * @property question The text of the question.
 * @property answer The detailed response or answer to the question.
 * @property featured Whether this question should be highlighted or prioritized in the UI.
 * @property tags A list of keywords or categories associated with this question for filtering purposes.
 */
@Serializable
data class FaqQuestionDto(
    val id: String,
    val icon: String? = null,
    val question: String,
    val answer: String,
    val featured: Boolean = false,
    val tags: List<String> = emptyList(),
)
