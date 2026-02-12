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

package com.d4rk.android.libs.apptoolkit.app.consent.domain.model

/**
 * Aggregated consent flags used to configure analytics and ad-related collection.
 *
 * @property usageAndDiagnostics Whether analytics/crash/performance collection is enabled.
 * @property analyticsConsent Consent for analytics storage in Firebase.
 * @property adStorageConsent Consent for ad storage in Firebase.
 * @property adUserDataConsent Consent for ad user data usage in Firebase.
 * @property adPersonalizationConsent Consent for ad personalization in Firebase.
 */
data class ConsentSettings(
    val usageAndDiagnostics: Boolean,
    val analyticsConsent: Boolean,
    val adStorageConsent: Boolean,
    val adUserDataConsent: Boolean,
    val adPersonalizationConsent: Boolean,
)
