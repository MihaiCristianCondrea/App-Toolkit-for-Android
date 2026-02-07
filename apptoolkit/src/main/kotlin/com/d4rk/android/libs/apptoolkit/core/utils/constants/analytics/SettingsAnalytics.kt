package com.d4rk.android.libs.apptoolkit.core.utils.constants.analytics

/**
 * Shared GA4 naming used by settings surfaces so dashboards stay consistent across screens.
 */
object SettingsAnalytics {
    object Events {
        const val PREFERENCE_TOGGLE: String = "settings_preference_toggle"
        const val PREFERENCE_VIEW: String = "settings_preference_view"
        const val THEME_SWITCH: String = "settings_theme_switch"
    }

    object Params {
        const val SCREEN: String = "screen"
        const val PREFERENCE_KEY: String = "preference_key"
        const val ENABLED: String = "enabled"
        const val THEME_MODE: String = "theme_mode"
    }
}

