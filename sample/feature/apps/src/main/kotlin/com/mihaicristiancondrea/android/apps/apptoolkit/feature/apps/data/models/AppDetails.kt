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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models

import androidx.compose.runtime.Immutable

/**
 * Package details loaded when an app is expanded. Lists are produced once by DTO mapping and
 * must remain immutable to satisfy the Compose [Immutable] contract.
 */
@Immutable
data class AppDetails(
    val name: String,
    val packageName: String,
    val iconUrl: String,
    val description: String,
    val shortDescription: String = "",
    val category: AppCategory? = null,
    val screenshots: List<AppScreenshot> = emptyList(),
    val links: List<AppLink> = emptyList(),
    val latestVersion: AppLatestVersion? = null,
)
