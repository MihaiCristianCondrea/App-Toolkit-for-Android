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

package com.wstxda.toolkit.utils

object Constants {

    // -------------------------------------------------------------------------
    // Dialog / fragment tags
    // -------------------------------------------------------------------------

    const val UPDATER_DIALOG = "UpdaterBottomSheet"
    const val ABOUT_DIALOG = "AboutAppBottomSheet"
    const val LIBRARY_DIALOG = "LibraryBottomSheet"
    const val WRITE_SECURE_SETTINGS_DIALOG = "WriteSecureSettingsBottomSheet"
    const val FREE_ANDROID_WARN_DIALOG = "FreeAndroidWarnDialog"

    // -------------------------------------------------------------------------
    // SharedPreferences
    // -------------------------------------------------------------------------

    const val IS_WARN_DISMISSED = "is_warn_dismissed"

    // -------------------------------------------------------------------------
    // Updater GitHub API
    // -------------------------------------------------------------------------

    const val GITHUB_TITLE = "title"
    const val GITHUB_VERSION = "version"
    const val GITHUB_CHANGELOG = "changelog"
    const val GITHUB_DOWNLOAD_URL = "download_url"
    const val GITHUB_PAGE_URL = "page_url"
    const val GITHUB_UPDATE_CHECKED = "update_checked"

    const val GITHUB_API_URL = "https://api.github.com/repos/WSTxda/Toolkit-Tiles/releases/latest"
    const val GITHUB_RELEASE_URL = "https://github.com/WSTxda/Toolkit-Tiles/releases/latest"
}
