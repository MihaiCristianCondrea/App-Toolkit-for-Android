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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models

import kotlinx.serialization.Serializable

/**
 * Question as supplied by a remote FAQ source.
 *
 * @property id Catalog identifier used to identify duplicates across sources.
 * @property featured Editorial flag supplied by the source. The FAQ screen preserves source order.
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
