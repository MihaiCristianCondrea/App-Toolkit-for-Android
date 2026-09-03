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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.settings

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.github.GithubConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.utils.providers.AdvancedSettingsProvider

/** Supplies the sample repository's GitHub issue URL to advanced settings. */
class AppAdvancedSettingsProvider(val context: Context) : AdvancedSettingsProvider {
    override val bugReportUrl: String
        get() = "${GithubConstants.GITHUB_BASE}AppToolkit${GithubConstants.GITHUB_ISSUES_SUFFIX}"
}
