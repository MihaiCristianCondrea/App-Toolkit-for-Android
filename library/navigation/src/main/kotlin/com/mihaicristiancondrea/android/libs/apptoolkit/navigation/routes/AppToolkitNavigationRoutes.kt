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
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * The Toolkit's own destinations. `toolkitGraph { }` in `:library:apptoolkit` registers a page for
 * each, which an app can replace by registering the same key itself.
 *
 * Every key is `@Serializable`: the shell's back stacks save their keys through
 * kotlinx.serialization, so they survive rotation and process death.
 */
@Immutable
sealed interface AppToolkitNavKey : NavKey

/** The library extras page, registered by `:library:feature:about`. */
@Serializable
data object LibraryExtrasRoute : AppToolkitNavKey

/** The settings list, a list page registered by `:library:feature:settings`. */
@Serializable
data object SettingsRoute : AppToolkitNavKey

/** Help and feedback, registered by `:library:feature:faq`. */
@Serializable
data object HelpRoute : AppToolkitNavKey

/** Support and donations, registered by `:library:feature:support`. */
@Serializable
data object SupportRoute : AppToolkitNavKey

/** Advertising settings, registered by `:library:integration:ads`. */
@Serializable
data object AdsSettingsRoute : AppToolkitNavKey

/** The app's permissions, registered by `:library:feature:permissions`. */
@Serializable
data object PermissionsRoute : AppToolkitNavKey

/** Open-source licenses, registered by `:library:feature:licenses`. */
@Serializable
data object LicensesRoute : AppToolkitNavKey

/** About the app, registered by `:library:feature:about`. */
@Serializable
data object AboutRoute : AppToolkitNavKey

/** Theme settings, registered by `:library:feature:theme`. */
@Serializable
data object ThemeSettingsRoute : AppToolkitNavKey

/** Display and language settings, registered by `:library:feature:display`. */
@Serializable
data object DisplaySettingsRoute : AppToolkitNavKey

/** Privacy settings, registered by `:library:feature:privacy`. */
@Serializable
data object PrivacySettingsRoute : AppToolkitNavKey

/** Advanced settings, registered by `:library:feature:advanced`. */
@Serializable
data object AdvancedSettingsRoute : AppToolkitNavKey

/** Usage and diagnostics settings, registered by `:library:feature:diagnostics`. */
@Serializable
data object DiagnosticsSettingsRoute : AppToolkitNavKey

/** The shell's developer options, registered by `:library:feature:developer`. */
@Serializable
data object DeveloperOptionsRoute : AppToolkitNavKey

/** The first-launch start screen (consent and permissions), registered by `:library:feature:startup`. */
@Serializable
data object StartupRoute : AppToolkitNavKey

/** The onboarding start screen that follows [StartupRoute], registered by `:library:feature:onboarding`. */
@Serializable
data object OnboardingRoute : AppToolkitNavKey
