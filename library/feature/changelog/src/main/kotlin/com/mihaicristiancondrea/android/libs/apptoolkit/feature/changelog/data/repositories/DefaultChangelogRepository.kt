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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.mappers.toNetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.models.ChangelogRelease
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.remote.ChangelogRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.remote.mappers.toChangelogReleases
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.remote.models.MarkdownResponse
import io.ktor.http.HttpStatusCode

/**
 * Reads the package-specific changelog from the Android App Metadata API and maps it into releases.
 *
 * @param legacyChangelogUrl Compatibility URL used for a blank package or HTTP 404.
 */
class DefaultChangelogRepository(
    private val remoteDataSource: ChangelogRemoteDataSource,
    private val apiBaseUrl: String,
    private val legacyChangelogUrl: String,
    private val diagnostics: DiagnosticsReporter,
) : ChangelogRepository {

    override suspend fun getChangelog(
        packageName: String,
        currentVersion: String,
    ): List<ChangelogRelease> {
        val markdown = fetchChangelogMarkdown(packageName = packageName)
        return markdown.toChangelogReleases(currentVersion = currentVersion)
    }

    private suspend fun fetchChangelogMarkdown(packageName: String): String {
        if (packageName.isBlank()) {
            return getLegacyChangelog(
                reason = LegacyReason.BLANK_PACKAGE,
                packageName = packageName,
            )
        }

        val primary = remoteDataSource.fetchMarkdown(
            url = ApiHost.appChangelogUrl(
                packageName = packageName,
                baseUrl = apiBaseUrl,
            ),
        )
        if (primary.status == HttpStatusCode.NotFound) {
            return getLegacyChangelog(reason = LegacyReason.NOT_FOUND, packageName = packageName)
        }
        return primary.bodyOrThrow()
    }

    private suspend fun getLegacyChangelog(reason: String, packageName: String): String {
        diagnostics.logBreadcrumb(
            message = "Changelog legacy fallback",
            attributes = mapOf(
                "packageName" to packageName,
                "reason" to reason,
            ),
        )
        return remoteDataSource.fetchMarkdown(url = legacyChangelogUrl).bodyOrThrow()
    }

    private fun MarkdownResponse.bodyOrThrow(): String {
        status.toNetworkException()?.let { failure -> throw failure }
        return body
    }

    private object LegacyReason {
        const val BLANK_PACKAGE: String = "blank_package"
        const val NOT_FOUND: String = "not_found"
    }
}
