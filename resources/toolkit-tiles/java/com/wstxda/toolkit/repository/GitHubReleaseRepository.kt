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

package com.wstxda.toolkit.repository

import com.wstxda.toolkit.data.ReleaseInfo
import com.wstxda.toolkit.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

object GitHubReleaseRepository {

    suspend fun fetchLatestRelease(): ReleaseInfo = withContext(Dispatchers.IO) {
        val json = JSONObject(URL(Constants.GITHUB_API_URL).readText())
        val asset = json.getJSONArray("assets").getJSONObject(0)

        val tagName = json.optString("tag_name")
        val releaseName = json.optString("name").takeIf { it.isNotBlank() } ?: tagName

        ReleaseInfo(
            title = releaseName.trimStart { !it.isDigit() },
            version = tagName.trimStart { !it.isDigit() },
            changelog = json.optString("body"),
            downloadUrl = asset.optString("browser_download_url"),
            pageUrl = json.optString("html_url").takeIf { it.isNotBlank() }
                ?: Constants.GITHUB_RELEASE_URL)
    }
}