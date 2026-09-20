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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.states.PrivacyUiState

/**
 * Builds the ordered privacy entries from the host-supplied [PrivacySettingsProvider].
 *
 * Card positions are assigned after the list is assembled, so they always describe the grouping the
 * user actually sees.
 */
internal fun PrivacySettingsProvider.toUiState(): PrivacyUiState =
    PrivacyUiState(items = toPrivacyItems())

private fun PrivacySettingsProvider.toPrivacyItems(): List<PrivacyItem> {
    val privacyPreferences = listOf(
        PrivacyItem.Preference(
            key = PrivacyItemKey.PRIVACY_POLICY,
            title = UiTextHelper.StringResource(R.string.privacy_policy),
            summary = UiTextHelper.StringResource(R.string.summary_preference_settings_privacy_policy),
            action = PrivacyItemAction.OpenUrl(url = privacyPolicyUrl),
        ),
        PrivacyItem.Preference(
            key = PrivacyItemKey.TERMS_OF_SERVICE,
            title = UiTextHelper.StringResource(R.string.terms_of_service),
            summary = UiTextHelper.StringResource(R.string.summary_preference_settings_terms_of_service),
            action = PrivacyItemAction.OpenUrl(url = termsOfServiceUrl),
        ),
        PrivacyItem.Preference(
            key = PrivacyItemKey.CODE_OF_CONDUCT,
            title = UiTextHelper.StringResource(R.string.code_of_conduct),
            summary = UiTextHelper.StringResource(R.string.summary_preference_settings_code_of_conduct),
            action = PrivacyItemAction.OpenUrl(url = codeOfConductUrl),
        ),
        PrivacyItem.Preference(
            key = PrivacyItemKey.PERMISSIONS,
            title = UiTextHelper.StringResource(R.string.permissions),
            summary = UiTextHelper.StringResource(R.string.summary_preference_settings_permissions),
            action = PrivacyItemAction.OpenPermissions,
        ),
        PrivacyItem.Preference(
            key = PrivacyItemKey.ADS,
            title = UiTextHelper.StringResource(R.string.ads),
            summary = UiTextHelper.StringResource(R.string.summary_preference_settings_ads),
            action = PrivacyItemAction.OpenAds,
        ),
        PrivacyItem.Preference(
            key = PrivacyItemKey.USAGE_AND_DIAGNOSTICS,
            title = UiTextHelper.StringResource(R.string.usage_and_diagnostics),
            summary = UiTextHelper.StringResource(R.string.summary_preference_settings_usage_and_diagnostics),
            action = PrivacyItemAction.OpenUsageAndDiagnostics,
        ),
    ).assignPositions()

    val legalPreferences = listOf(
        PrivacyItem.Preference(
            key = PrivacyItemKey.LEGAL_NOTICES,
            title = UiTextHelper.StringResource(R.string.legal_notices),
            summary = UiTextHelper.StringResource(R.string.summary_preference_settings_legal_notices),
            action = PrivacyItemAction.OpenUrl(url = legalNoticesUrl),
        ),
        PrivacyItem.Preference(
            key = PrivacyItemKey.LICENSE,
            title = UiTextHelper.StringResource(R.string.license),
            summary = UiTextHelper.StringResource(R.string.summary_preference_settings_license),
            action = PrivacyItemAction.OpenUrl(url = licenseUrl),
        ),
    ).assignPositions()

    return buildList {
        add(
            PrivacyItem.Header(
                key = PrivacyItemKey.HEADER_PRIVACY,
                title = UiTextHelper.StringResource(R.string.privacy),
            )
        )
        addAll(privacyPreferences)
        add(
            PrivacyItem.Header(
                key = PrivacyItemKey.HEADER_LEGAL,
                title = UiTextHelper.StringResource(R.string.legal),
            )
        )
        addAll(legalPreferences)
    }
}

private fun List<PrivacyItem.Preference>.assignPositions(): List<PrivacyItem.Preference> =
    mapIndexed { index, item ->
        val position = when {
            size == 1 -> GroupedItemPosition.SINGLE
            index == 0 -> GroupedItemPosition.FIRST
            index == lastIndex -> GroupedItemPosition.LAST
            else -> GroupedItemPosition.MIDDLE
        }
        item.copy(position = position)
    }
