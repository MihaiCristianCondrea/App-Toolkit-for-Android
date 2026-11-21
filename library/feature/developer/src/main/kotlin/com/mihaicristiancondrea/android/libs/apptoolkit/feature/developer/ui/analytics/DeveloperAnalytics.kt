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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption

internal fun developerOptionKey(option: DeveloperOption): String = when (option) {
    DeveloperOption.Layout -> "layout"
    DeveloperOption.TopBarStyle -> "top_bar_style"
    DeveloperOption.HideTopBarOnScroll -> "hide_top_bar_on_scroll"
    DeveloperOption.NavigationBarStyle -> "navigation_bar_style"
    DeveloperOption.HideBottomBarOnScroll -> "hide_bottom_bar_on_scroll"
    DeveloperOption.NavigationTint -> "navigation_tint"
    DeveloperOption.ContentWidth -> "content_width"
    DeveloperOption.StartOverride -> "start_override"
    DeveloperOption.TabTransition -> "tab_transition"
    DeveloperOption.BackEdge -> "back_edge"
    DeveloperOption.AnimationSpeed -> "animation_speed"
    DeveloperOption.EntranceStyle -> "entrance_style"
    DeveloperOption.MotionTempo -> "motion_tempo"
    DeveloperOption.EntranceReplay -> "entrance_replay"
    DeveloperOption.BottomAccessory -> "bottom_accessory"
}

internal fun developerOptionSavedEvent(option: DeveloperOption): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.ACTION,
    params = mapOf(
        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str("DeveloperOptions"),
        SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str("save_developer_option"),
        SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(developerOptionKey(option)),
    ),
)

internal fun developerOptionsResetEvent(): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.ACTION,
    params = mapOf(
        SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str("DeveloperOptions"),
        SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str("reset_developer_options"),
    ),
)
