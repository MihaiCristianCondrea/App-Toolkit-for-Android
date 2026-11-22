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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body posted to the GitHub REST API to create a new issue.
 *
 * @property title Issue title.
 * @property body Formatted Markdown issue body.
 * @property labels Optional labels attached to the created issue.
 * @property assignees Optional GitHub usernames assigned to the created issue.
 */
@Serializable
data class CreateIssueRequest(
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
    @SerialName("labels") val labels: List<String>? = null,
    @SerialName("assignees") val assignees: List<String>? = null,
)
