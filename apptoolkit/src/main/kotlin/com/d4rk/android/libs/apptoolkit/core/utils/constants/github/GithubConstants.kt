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

package com.d4rk.android.libs.apptoolkit.core.utils.constants.github

object GithubConstants {
    const val GITHUB_USER: String = "MihaiCristianCondrea"
    const val GITHUB_BASE: String = "https://github.com/$GITHUB_USER/"
    const val GITHUB_ISSUES_SUFFIX: String = "/issues/new"
    const val GITHUB_RAW: String = "https://raw.githubusercontent.com/$GITHUB_USER"
    const val GITHUB_PAGES: String = "https://mihaicristiancondrea.github.io"
    fun githubChangelog(repository: String): String =
        "$GITHUB_RAW/$repository/refs/heads/master/CHANGELOG.md"
}
