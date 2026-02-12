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

package com.d4rk.android.libs.apptoolkit.app.settings.utils.providers

import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks

interface PrivacySettingsProvider {

    /**
     * URL for the privacy policy.
     */
    val privacyPolicyUrl: String
        get() = AppLinks.PRIVACY_POLICY

    /**
     * URL for the terms of service.
     */
    val termsOfServiceUrl: String
        get() = AppLinks.TERMS_OF_SERVICE

    /**
     * URL for the code of conduct.
     */
    val codeOfConductUrl: String
        get() = AppLinks.CODE_OF_CONDUCT

    /**
     * URL for the legal notices.
     */
    val legalNoticesUrl: String
        get() = AppLinks.LEGAL_NOTICES

    /**
     * URL for the license (GPLv3).
     */
    val licenseUrl: String
        get() = AppLinks.GPL_V3

    fun openPermissionsScreen()
    fun openAdsScreen()
    fun openUsageAndDiagnosticsScreen()
}