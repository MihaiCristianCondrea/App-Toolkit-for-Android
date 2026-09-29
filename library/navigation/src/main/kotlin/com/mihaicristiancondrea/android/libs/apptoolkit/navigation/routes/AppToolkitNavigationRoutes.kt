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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDestinationType
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Shared AppToolkit navigation destinations that can be embedded in a host app shell.
 *
 * Every key is `@Parcelize` for the current back stacks and `@Serializable` for the shell's,
 * whose `rememberNavBackStack` saves keys through kotlinx.serialization. A key added here needs
 * both until the old stacks are removed.
 */
@Immutable
sealed interface AppToolkitNavKey : StableNavKey {
    override val destinationType: NavigationDestinationType
        get() = NavigationDestinationType.ActivityLike
}

/** Library extras destination. */
@Parcelize
@Serializable
data object LibraryExtrasRoute : AppToolkitNavKey

/** Root settings destination. */
@Parcelize
@Serializable
data object SettingsRoute : AppToolkitNavKey

/** Destination for a settings category identified by [contentKey]. */
@Parcelize
@Serializable
data class GeneralSettingsRoute(val title: String, val contentKey: String) : AppToolkitNavKey {
    override val destinationType: NavigationDestinationType
        get() = NavigationDestinationType.Nested
}

/** Help and feedback FAQ destination. */
@Parcelize
@Serializable
data object HelpRoute : AppToolkitNavKey

/** Support and donations destination. */
@Parcelize
@Serializable
data object SupportRoute : AppToolkitNavKey

/** Advertising settings destination. */
@Parcelize
@Serializable
data object AdsSettingsRoute : AppToolkitNavKey

/** Runtime permissions destination. */
@Parcelize
@Serializable
data object PermissionsRoute : AppToolkitNavKey

/** Open-source licenses destination. */
@Parcelize
@Serializable
data object LicensesRoute : AppToolkitNavKey

// One key per page that `GeneralSettingsRoute` reaches today through a content key, and per
// activity that becomes a page. Nothing registers them yet: each feature registers its own when it
// moves to the shell graph, which replaces `GeneralSettingsRoute` and the string content keys.

/** About the app, registered by `:library:feature:about`. */
@Parcelize
@Serializable
data object AboutRoute : AppToolkitNavKey

/** Theme settings, registered by `:library:feature:theme`. */
@Parcelize
@Serializable
data object ThemeSettingsRoute : AppToolkitNavKey

/** Display and language settings, registered by `:library:feature:display`. */
@Parcelize
@Serializable
data object DisplaySettingsRoute : AppToolkitNavKey

/** Privacy settings, registered by `:library:feature:privacy`. */
@Parcelize
@Serializable
data object PrivacySettingsRoute : AppToolkitNavKey

/** Advanced settings, registered by `:library:feature:advanced`. */
@Parcelize
@Serializable
data object AdvancedSettingsRoute : AppToolkitNavKey

/** Usage and diagnostics settings, registered by `:library:feature:diagnostics`. */
@Parcelize
@Serializable
data object DiagnosticsSettingsRoute : AppToolkitNavKey

/** The shell's developer options, registered by the developer feature. */
@Parcelize
@Serializable
data object DeveloperOptionsRoute : AppToolkitNavKey

/** The first-launch start screen (consent and permissions), registered by `:library:feature:onboarding`. */
@Parcelize
@Serializable
data object StartupRoute : AppToolkitNavKey

/** The onboarding start screen that follows [StartupRoute], registered by `:library:feature:onboarding`. */
@Parcelize
@Serializable
data object OnboardingRoute : AppToolkitNavKey
