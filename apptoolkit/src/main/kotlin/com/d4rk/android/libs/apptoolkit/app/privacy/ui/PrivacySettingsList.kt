package com.d4rk.android.libs.apptoolkit.app.privacy.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.PrivacySettingsProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val PRIVACY_SCREEN_NAME = "Privacy"
private const val PRIVACY_SCREEN_CLASS = "PrivacySettingsScreen"

@Composable
fun PrivacySettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
) {
    val viewModel: PrivacySettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<*> by viewModel.uiState.collectAsStateWithLifecycle()
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
        screenState = screenState.screenState,
    )

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = { NoDataScreen(paddingValues = paddingValues) },
        onError = { NoDataScreen(isError = true, paddingValues = paddingValues) },
        onSuccess = {
            PrivacySettingsContent(
                paddingValues = paddingValues,
                provider = provider,
                context = context,
            )
        }
    )
}

@Composable
private fun PrivacySettingsContent(
    paddingValues: PaddingValues,
    provider: PrivacySettingsProvider,
    context: Context,
) {
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
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.terms_of_service),
                    summary = stringResource(id = R.string.summary_preference_settings_terms_of_service),
                    onClick = { context.openUrl(provider.termsOfServiceUrl) },
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.code_of_conduct),
                    summary = stringResource(id = R.string.summary_preference_settings_code_of_conduct),
                    onClick = { context.openUrl(provider.codeOfConductUrl) },
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.permissions),
                    summary = stringResource(id = R.string.summary_preference_settings_permissions),
                    onClick = { provider.openPermissionsScreen() },
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.ads),
                    summary = stringResource(id = R.string.summary_preference_settings_ads),
                    onClick = { provider.openAdsScreen() },
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.usage_and_diagnostics),
                    summary = stringResource(id = R.string.summary_preference_settings_usage_and_diagnostics),
                    onClick = { provider.openUsageAndDiagnosticsScreen() },
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
                )
                ExtraTinyVerticalSpacer()

                SettingsPreferenceItem(
                    title = stringResource(id = R.string.license),
                    summary = stringResource(id = R.string.summary_preference_settings_license),
                    onClick = { context.openUrl(provider.licenseUrl) },
                )
            }
        }
    }
}
