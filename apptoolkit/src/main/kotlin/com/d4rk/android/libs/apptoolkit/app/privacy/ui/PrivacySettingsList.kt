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

package com.d4rk.android.libs.apptoolkit.app.privacy.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.PrivacySettingsProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.analytics.SettingsAnalytics
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl
import org.koin.compose.koinInject

private const val PRIVACY_SCREEN_NAME = "Privacy"
private const val PRIVACY_SCREEN_CLASS = "PrivacySettingsList"

private object PrivacyPreferenceKeys {
    const val PRIVACY_POLICY: String = "privacy_policy"
    const val TERMS_OF_SERVICE: String = "terms_of_service"
    const val CODE_OF_CONDUCT: String = "code_of_conduct"
    const val PERMISSIONS: String = "permissions"
    const val ADS: String = "ads"
    const val USAGE_AND_DIAGNOSTICS: String = "usage_and_diagnostics"
    const val LEGAL_NOTICES: String = "legal_notices"
    const val LICENSE: String = "license"
}

@Composable
fun PrivacySettingsList(
    paddingValues: PaddingValues = PaddingValues(),
) {
    val provider: PrivacySettingsProvider = koinInject()
    val context: Context = LocalContext.current

    val firebaseController: FirebaseController = koinInject()
    TrackScreenView(
        firebaseController = firebaseController,
        screenName = PRIVACY_SCREEN_NAME,
        screenClass = PRIVACY_SCREEN_CLASS,
    )
    TrackScreenState(
        firebaseController = firebaseController,
        screenName = PRIVACY_SCREEN_NAME,
        screenState = ScreenState.Success(),
    )

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxHeight(),
    ) {
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.privacy))
            SmallVerticalSpacer()

            Column(
                modifier = Modifier
                    .padding(horizontal = SizeConstants.LargeSize)
                    .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize)),
            ) {
                SettingsPreferenceItem(
                    title = stringResource(id = R.string.privacy_policy),
                    summary = stringResource(id = R.string.summary_preference_settings_privacy_policy),
                    onClick = { context.openUrl(provider.privacyPolicyUrl) },
                    firebaseController = firebaseController,
                    ga4Event = privacyPreferenceTapEvent(preferenceKey = PrivacyPreferenceKeys.PRIVACY_POLICY),
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.terms_of_service),
                    summary = stringResource(id = R.string.summary_preference_settings_terms_of_service),
                    onClick = { context.openUrl(provider.termsOfServiceUrl) },
                    firebaseController = firebaseController,
                    ga4Event = privacyPreferenceTapEvent(preferenceKey = PrivacyPreferenceKeys.TERMS_OF_SERVICE),
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.code_of_conduct),
                    summary = stringResource(id = R.string.summary_preference_settings_code_of_conduct),
                    onClick = { context.openUrl(provider.codeOfConductUrl) },
                    firebaseController = firebaseController,
                    ga4Event = privacyPreferenceTapEvent(preferenceKey = PrivacyPreferenceKeys.CODE_OF_CONDUCT),
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.permissions),
                    summary = stringResource(id = R.string.summary_preference_settings_permissions),
                    onClick = { provider.openPermissionsScreen() },
                    firebaseController = firebaseController,
                    ga4Event = privacyPreferenceTapEvent(preferenceKey = PrivacyPreferenceKeys.PERMISSIONS),
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.ads),
                    summary = stringResource(id = R.string.summary_preference_settings_ads),
                    onClick = { provider.openAdsScreen() },
                    firebaseController = firebaseController,
                    ga4Event = privacyPreferenceTapEvent(preferenceKey = PrivacyPreferenceKeys.ADS),
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.usage_and_diagnostics),
                    summary = stringResource(id = R.string.summary_preference_settings_usage_and_diagnostics),
                    onClick = { provider.openUsageAndDiagnosticsScreen() },
                    firebaseController = firebaseController,
                    ga4Event = privacyPreferenceTapEvent(preferenceKey = PrivacyPreferenceKeys.USAGE_AND_DIAGNOSTICS),
                )
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.legal))
            SmallVerticalSpacer()

            Column(
                modifier = Modifier
                    .padding(horizontal = SizeConstants.LargeSize)
                    .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize)),
            ) {
                SettingsPreferenceItem(
                    title = stringResource(id = R.string.legal_notices),
                    summary = stringResource(id = R.string.summary_preference_settings_legal_notices),
                    onClick = { context.openUrl(provider.legalNoticesUrl) },
                    firebaseController = firebaseController,
                    ga4Event = privacyPreferenceTapEvent(preferenceKey = PrivacyPreferenceKeys.LEGAL_NOTICES),
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.license),
                    summary = stringResource(id = R.string.summary_preference_settings_license),
                    onClick = { context.openUrl(provider.licenseUrl) },
                    firebaseController = firebaseController,
                    ga4Event = privacyPreferenceTapEvent(preferenceKey = PrivacyPreferenceKeys.LICENSE),
                )
            }
        }
    }
}

private fun privacyPreferenceTapEvent(preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(PRIVACY_SCREEN_NAME),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}
