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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.utils

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics

/**
 * Analytics plumbing shared by the report screen and the views split out of it.
 *
 * These were private to `IssueReporterScreen.kt`, which is why every card had to stay in that one
 * file: a composable in `ui/views` could not name the event helper it needed.
 */
internal const val ISSUE_REPORTER_SCREEN_NAME: String = "IssueReporter"

internal object IssueReporterActionNames {
    const val BACK_CLICK: String = "back_click"
    const val OPEN_ISSUES_LIST: String = "open_issues_list"
    const val SEND_ISSUE: String = "send_issue"
    const val OPEN_CREATED_ISSUE: String = "open_created_issue"
    const val TOGGLE_DEVICE_INFO: String = "toggle_device_info"
    const val SET_ANONYMOUS_MODE: String = "set_anonymous_mode"
}

internal fun issueReporterActionEvent(
    actionName: String,
    params: Map<String, AnalyticsValue> = emptyMap(),
): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.ACTION,
    params = buildMap {
        put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(ISSUE_REPORTER_SCREEN_NAME))
        put(SettingsAnalytics.Params.ACTION_NAME, AnalyticsValue.Str(actionName))
        putAll(params)
    },
)
