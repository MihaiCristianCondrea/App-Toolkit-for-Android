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

package com.d4rk.android.libs.apptoolkit.core.utils.constants.links

object AppLinks {

    // Play Store
    const val PLAY_STORE_MAIN: String = "https://play.google.com/"
    const val PLAY_STORE_APP: String = "${PLAY_STORE_MAIN}store/apps/details?id="
    const val PLAY_STORE_BETA: String = "${PLAY_STORE_MAIN}apps/testing/"
    const val MARKET_APP_PAGE: String = "market://details?id="
    const val PLAY_STORE_DEVELOPER: String = "${PLAY_STORE_MAIN}store/apps/dev?id="
    const val DEFAULT_DEVELOPER_ID: String = "5390214922640123642"

    const val DEVELOPER_PAGE = "${PLAY_STORE_DEVELOPER}${DEFAULT_DEVELOPER_ID}&hl=en"

    // Legal & Policy
    private const val AUTHOR_WEBSITE_BASE: String =
        "https://mihaicristiancondrea.github.io/profile/"
    const val ADS_HELP_CENTER: String = "${AUTHOR_WEBSITE_BASE}#ads-help-center"
    const val PRIVACY_POLICY: String = "${AUTHOR_WEBSITE_BASE}#privacy-policy-end-user-software"
    const val TERMS_OF_SERVICE: String = "${AUTHOR_WEBSITE_BASE}#terms-of-service-end-user-software"
    const val CODE_OF_CONDUCT: String = "${AUTHOR_WEBSITE_BASE}#code-of-conduct"
    const val LEGAL_NOTICES: String = "${AUTHOR_WEBSITE_BASE}#legal-notices"
    const val GPL_V3: String = "https://www.gnu.org/licenses/gpl-3.0"

    const val CONTACT_EMAIL: String = "contact.mihaicristiancondrea@gmail.com"
}