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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PrivacyMappersTest {

    private val provider = object : PrivacySettingsProvider {
        override val privacyPolicyUrl: String = "https://example.test/privacy"
        override val termsOfServiceUrl: String = "https://example.test/terms"
        override val codeOfConductUrl: String = "https://example.test/conduct"
        override val legalNoticesUrl: String = "https://example.test/legal"
        override val licenseUrl: String = "https://example.test/license"
    }

    @Test
    fun `maps the provider into two ordered groups`() {
        val items = provider.toPrivacyItems()

        assertEquals(
            listOf(
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
            ),
            items.map { it.key },
        )
    }

    @Test
    fun `link rows carry the provider's urls and host rows carry their destination`() {
        val items = provider.toPrivacyItems()

        assertEquals(
            PrivacyItemAction.OpenUrl(url = "https://example.test/privacy"),
            items.preference(PrivacyItemKey.PRIVACY_POLICY).action,
        )
        assertEquals(
            PrivacyItemAction.OpenUrl(url = "https://example.test/license"),
            items.preference(PrivacyItemKey.LICENSE).action,
        )
        assertEquals(PrivacyItemAction.OpenPermissions, items.preference(PrivacyItemKey.PERMISSIONS).action)
        assertEquals(PrivacyItemAction.OpenAds, items.preference(PrivacyItemKey.ADS).action)
        assertEquals(
            PrivacyItemAction.OpenUsageAndDiagnostics,
            items.preference(PrivacyItemKey.USAGE_AND_DIAGNOSTICS).action,
        )
    }

    @Test
    fun `each row knows its place within its own group`() {
        val items = provider.toPrivacyItems()

        val policy = items.preference(PrivacyItemKey.PRIVACY_POLICY)
        assertEquals(0 to 6, policy.index to policy.count)
        val diagnostics = items.preference(PrivacyItemKey.USAGE_AND_DIAGNOSTICS)
        assertEquals(5 to 6, diagnostics.index to diagnostics.count)
        val notices = items.preference(PrivacyItemKey.LEGAL_NOTICES)
        assertEquals(0 to 2, notices.index to notices.count)
    }

    private fun List<PrivacyItem>.preference(key: String): PrivacyItem.Preference =
        first { it.key == key } as PrivacyItem.Preference
}
