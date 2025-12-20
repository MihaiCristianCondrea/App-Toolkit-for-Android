package com.d4rk.android.libs.apptoolkit.core.utils.constants.links

object AppLinks {

    // Play Store
    const val PLAY_STORE_MAIN : String = "https://play.google.com/"
    const val PLAY_STORE_APP : String = "${PLAY_STORE_MAIN}store/apps/details?id="
    const val PLAY_STORE_BETA : String = "${PLAY_STORE_MAIN}apps/testing/"
    const val MARKET_APP_PAGE : String = "market://details?id="
    const val PLAY_STORE_DEVELOPER: String = "${PLAY_STORE_MAIN}store/apps/dev?id="
    const val DEFAULT_DEVELOPER_ID: String = "5390214922640123642"

    const val DEVELOPER_PAGE = "${PLAY_STORE_DEVELOPER}${DEFAULT_DEVELOPER_ID}&hl=en"

    // Legal & Policy
    private const val AUTHOR_WEBSITE_BASE : String = "https://mihaicristiancondrea.github.io/profile/"
    const val ADS_HELP_CENTER : String = "${AUTHOR_WEBSITE_BASE}#ads-help-center"
    const val PRIVACY_POLICY : String = "${AUTHOR_WEBSITE_BASE}#privacy-policy-end-user-software"
    const val TERMS_OF_SERVICE : String = "${AUTHOR_WEBSITE_BASE}#terms-of-service-end-user-software"
    const val CODE_OF_CONDUCT : String = "${AUTHOR_WEBSITE_BASE}#code-of-conduct"
    const val LEGAL_NOTICES : String = "${AUTHOR_WEBSITE_BASE}#legal-notices"
    const val GPL_V3 : String = "https://www.gnu.org/licenses/gpl-3.0"

    const val CONTACT_EMAIL : String = "contact.mihaicristiancondrea@gmail.com"
}