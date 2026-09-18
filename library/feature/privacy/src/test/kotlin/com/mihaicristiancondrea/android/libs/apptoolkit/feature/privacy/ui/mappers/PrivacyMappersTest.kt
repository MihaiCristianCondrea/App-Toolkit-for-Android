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

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import org.junit.jupiter.api.Test

class PrivacyMappersTest {

    private val provider = object : PrivacySettingsProvider {
        override val privacyPolicyUrl: String = "https://example.test/privacy"
        override val termsOfServiceUrl: String = "https://example.test/terms"
        override val codeOfConductUrl: String = "https://example.test/conduct"
        override val legalNoticesUrl: String = "https://example.test/legal"
        override val licenseUrl: String = "https://example.test/license"

        override fun openPermissionsScreen() = Unit
        override fun openAdsScreen() = Unit
        override fun openUsageAndDiagnosticsScreen() = Unit
    }

    @Test
    fun `maps the provider into two ordered groups`() {
        val items = provider.toUiState().items

        assertThat(items.map { it.key }).containsExactly(
            PrivacyItemKey.HEADER_PRIVACY,
            PrivacyItemKey.PRIVACY_POLICY,
            PrivacyItemKey.TERMS_OF_SERVICE,
            PrivacyItemKey.CODE_OF_CONDUCT,
            PrivacyItemKey.PERMISSIONS,
            PrivacyItemKey.ADS,
            PrivacyItemKey.USAGE_AND_DIAGNOSTICS,
            PrivacyItemKey.HEADER_LEGAL,
            PrivacyItemKey.LEGAL_NOTICES,
            PrivacyItemKey.LICENSE,
        ).inOrder()
    }

    @Test
    fun `assigns card positions per group rather than across the whole list`() {
        val items = provider.toUiState().items

        assertThat(items.preference(PrivacyItemKey.PRIVACY_POLICY).position)
            .isEqualTo(GroupedItemPosition.FIRST)
        assertThat(items.preference(PrivacyItemKey.ADS).position)
            .isEqualTo(GroupedItemPosition.MIDDLE)
        assertThat(items.preference(PrivacyItemKey.USAGE_AND_DIAGNOSTICS).position)
            .isEqualTo(GroupedItemPosition.LAST)
        assertThat(items.preference(PrivacyItemKey.LEGAL_NOTICES).position)
            .isEqualTo(GroupedItemPosition.FIRST)
        assertThat(items.preference(PrivacyItemKey.LICENSE).position)
            .isEqualTo(GroupedItemPosition.LAST)
    }

    @Test
    fun `link rows carry the provider's urls and host rows carry their destination`() {
        val items = provider.toUiState().items

        assertThat(items.preference(PrivacyItemKey.PRIVACY_POLICY).action)
            .isEqualTo(PrivacyItemAction.OpenUrl(url = "https://example.test/privacy"))
        assertThat(items.preference(PrivacyItemKey.LICENSE).action)
            .isEqualTo(PrivacyItemAction.OpenUrl(url = "https://example.test/license"))
        assertThat(items.preference(PrivacyItemKey.PERMISSIONS).action)
            .isEqualTo(PrivacyItemAction.OpenPermissions)
        assertThat(items.preference(PrivacyItemKey.ADS).action)
            .isEqualTo(PrivacyItemAction.OpenAds)
        assertThat(items.preference(PrivacyItemKey.USAGE_AND_DIAGNOSTICS).action)
            .isEqualTo(PrivacyItemAction.OpenUsageAndDiagnostics)
    }

    private fun List<PrivacyItem>.preference(key: String): PrivacyItem.Preference =
        first { it.key == key } as PrivacyItem.Preference
}
