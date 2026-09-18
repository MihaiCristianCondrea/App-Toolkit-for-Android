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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition

/**
 * Stable keys for the entries rendered by the privacy screen.
 */
object PrivacyItemKey {
    const val HEADER_PRIVACY: String = "header_privacy"
    const val PRIVACY_POLICY: String = "privacy_policy"
    const val TERMS_OF_SERVICE: String = "terms_of_service"
    const val CODE_OF_CONDUCT: String = "code_of_conduct"
    const val PERMISSIONS: String = "permissions"
    const val ADS: String = "ads"
    const val USAGE_AND_DIAGNOSTICS: String = "usage_and_diagnostics"
    const val HEADER_LEGAL: String = "header_legal"
    const val LEGAL_NOTICES: String = "legal_notices"
    const val LICENSE: String = "license"
}

/**
 * Click interactions supported by privacy preference items.
 */
sealed interface PrivacyItemAction {
    /** Opens [url] in the device browser. */
    data class OpenUrl(val url: String) : PrivacyItemAction

    /** Opens the host's permissions screen. */
    data object OpenPermissions : PrivacyItemAction

    /** Opens the host's ads screen. */
    data object OpenAds : PrivacyItemAction

    /** Opens the host's usage and diagnostics screen. */
    data object OpenUsageAndDiagnostics : PrivacyItemAction
}

/**
 * Represents an entry displayed on the privacy screen.
 */
sealed interface PrivacyItem {
    /** Unique, stable identifier used for LazyColumn item keys and analytics. */
    val key: String

    /** A category title separating groups of preference items. */
    data class Header(
        override val key: String,
        val title: UiTextHelper,
    ) : PrivacyItem

    /** An individual preference entry within a category card. */
    data class Preference(
        override val key: String,
        val title: UiTextHelper,
        val summary: UiTextHelper,
        val position: GroupedItemPosition = GroupedItemPosition.MIDDLE,
        val action: PrivacyItemAction,
    ) : PrivacyItem
}
