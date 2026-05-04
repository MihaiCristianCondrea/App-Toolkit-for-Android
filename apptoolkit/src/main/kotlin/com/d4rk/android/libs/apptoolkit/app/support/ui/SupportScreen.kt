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

package com.d4rk.android.libs.apptoolkit.app.support.ui

import android.app.Activity
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.support.ui.contract.SupportEvent
import com.d4rk.android.libs.apptoolkit.app.support.ui.state.DonationOptionUiState
import com.d4rk.android.libs.apptoolkit.app.support.ui.state.SupportScreenUiState
import com.d4rk.android.libs.apptoolkit.app.support.utils.constants.DonationProductIds
import com.d4rk.android.libs.apptoolkit.app.support.utils.constants.ShortenLinkConstants
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.SupportNativeAdCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralTonalButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHandler
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ads.AdsQualifiers
import com.d4rk.android.libs.apptoolkit.core.utils.constants.analytics.SettingsAnalytics
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl
import kotlinx.collections.immutable.ImmutableMap
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

private const val SUPPORT_SCREEN_NAME = "Support"
private const val SUPPORT_SCREEN_CLASS = "SupportComposable"

private object SupportPreferenceKeys {
    const val DONATE_LOW: String = "donate_low"
    const val DONATE_NORMAL: String = "donate_normal"
    const val DONATE_HIGH: String = "donate_high"
    const val DONATE_EXTREME: String = "donate_extreme"
    const val WEB_AD: String = "web_ad"
    const val PRODUCT_ID: String = "product_id"
    const val DESTINATION: String = "destination"
}

private object SupportActionNames {
    const val BACK_CLICK: String = "back_click"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportComposable() {
    SupportScreen()
}

/**
 * Renders the support and donation screen.
 *
 * @param isEmbedded When true, the screen omits its own top app bar so a host navigation shell can
 * provide app-level chrome.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(isEmbedded: Boolean = false) {
    val viewModel: SupportViewModel = koinViewModel()
    val activity = LocalActivity.current
    val screenState: UiStateScreen<SupportScreenUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val currentViewModel = rememberUpdatedState(newValue = viewModel)

    val onDonateClick: (Activity, String) -> Unit =
        remember(currentViewModel) {
            { hostActivity, productId ->
                currentViewModel.value.onDonateClicked(
                    activity = hostActivity,
                    productId = productId
                )
            }
        }

    val firebaseController: FirebaseController = koinInject()
    TrackScreenView(
        firebaseController = firebaseController,
        screenName = SUPPORT_SCREEN_NAME,
        screenClass = SUPPORT_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = SUPPORT_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    val content: @Composable (PaddingValues) -> Unit = { paddingValues ->
        ScreenStateHandler(
            screenState = screenState,
            onLoading = { LoadingScreen() },
            onEmpty = { NoDataScreen(paddingValues = paddingValues) },
            onError = {
                NoDataScreen(
                    icon = Icons.Outlined.MoneyOff,
                    isError = true,
                    textMessage = R.string.error_failed_to_load_sku_details,
                    paddingValues = paddingValues
                )
            },
            onSuccess = { data: SupportScreenUiState ->
                SupportScreenContent(
                    paddingValues = paddingValues,
                    donationOptions = data.donationOptions,
                    isBillingInProgress = data.isBillingInProgress,
                    firebaseController = firebaseController,
                    onDonateClick = { productId ->
                        activity?.let { hostActivity ->
                            onDonateClick(hostActivity, productId)
                        }
                    },
                )
            })
        DefaultSnackbarHandler(
            screenState = screenState,
            snackbarHostState = snackbarHostState,
            getDismissEvent = { SupportEvent.DismissSnackbar },
            onEvent = { viewModel.onEvent(it) }
        )
    }

    if (isEmbedded) {
        content(PaddingValues())
    } else {
        LargeTopAppBarWithScaffold(
            title = stringResource(id = R.string.support_us),
            onBackClicked = {
                firebaseController.logEvent(supportActionEvent(actionName = SupportActionNames.BACK_CLICK))
                activity?.finish()
            },
            snackbarHostState = snackbarHostState,
            content = content,
        )
    }
}

@Composable
fun SupportScreenContent(
    paddingValues: PaddingValues,
    donationOptions: ImmutableMap<String, DonationOptionUiState>,
    isBillingInProgress: Boolean,
    firebaseController: FirebaseController,
    onDonateClick: (String) -> Unit,
) {
    val context: Context = LocalContext.current
    val nativeAdsConfig: AdsConfig = koinInject(qualifier = named(name = AdsQualifiers.SUPPORT_NATIVE_AD))

    LazyColumn(
        modifier = Modifier.padding(paddingValues),
    ) {
        item {
            Text(
                text = stringResource(id = R.string.paid_support),
                modifier = Modifier.padding(
                    start = SizeConstants.LargeSize,
                    top = SizeConstants.LargeSize
                ),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.LargeSize),
                shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize)
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.summary_donations),
                        modifier = Modifier.padding(all = SizeConstants.LargeSize)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SizeConstants.LargeSize),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val lowDonation = donationOptions[DonationProductIds.LOW_DONATION]
                        val normalDonation = donationOptions[DonationProductIds.NORMAL_DONATION]
                        GeneralTonalButton(
                            modifier = Modifier.weight(1f),
                            onClick = { onDonateClick(DonationProductIds.LOW_DONATION) },
                            firebaseController = firebaseController,
                            ga4Event = supportPreferenceTapEvent(
                                preferenceKey = SupportPreferenceKeys.DONATE_LOW,
                                productId = DonationProductIds.LOW_DONATION,
                            ),
                            enabled = lowDonation?.isEligible == true && !isBillingInProgress,
                            vectorIcon = Icons.Outlined.Paid,
                            label = if (lowDonation?.isEligible == true) {
                                lowDonation.formattedPrice.orEmpty()
                            } else {
                                stringResource(id = R.string.support_offer_unavailable)
                            }
                        )
                        Spacer(modifier = Modifier.width(SizeConstants.MediumSize))
                        GeneralTonalButton(
                            modifier = Modifier.weight(1f),
                            onClick = { onDonateClick(DonationProductIds.NORMAL_DONATION) },
                            firebaseController = firebaseController,
                            ga4Event = supportPreferenceTapEvent(
                                preferenceKey = SupportPreferenceKeys.DONATE_NORMAL,
                                productId = DonationProductIds.NORMAL_DONATION,
                            ),
                            enabled = normalDonation?.isEligible == true && !isBillingInProgress,
                            vectorIcon = Icons.Outlined.Paid,
                            label = if (normalDonation?.isEligible == true) {
                                normalDonation.formattedPrice.orEmpty()
                            } else {
                                stringResource(id = R.string.support_offer_unavailable)
                            }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = SizeConstants.LargeSize),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val highDonation = donationOptions[DonationProductIds.HIGH_DONATION]
                        val extremeDonation = donationOptions[DonationProductIds.EXTREME_DONATION]
                        GeneralTonalButton(
                            modifier = Modifier.weight(1f),
                            onClick = { onDonateClick(DonationProductIds.HIGH_DONATION) },
                            firebaseController = firebaseController,
                            ga4Event = supportPreferenceTapEvent(
                                preferenceKey = SupportPreferenceKeys.DONATE_HIGH,
                                productId = DonationProductIds.HIGH_DONATION,
                            ),
                            enabled = highDonation?.isEligible == true && !isBillingInProgress,
                            vectorIcon = Icons.Outlined.Paid,
                            label = if (highDonation?.isEligible == true) {
                                highDonation.formattedPrice.orEmpty()
                            } else {
                                stringResource(id = R.string.support_offer_unavailable)
                            }
                        )
                        Spacer(modifier = Modifier.width(SizeConstants.MediumSize))
                        GeneralTonalButton(
                            modifier = Modifier.weight(1f),
                            onClick = { onDonateClick(DonationProductIds.EXTREME_DONATION) },
                            firebaseController = firebaseController,
                            ga4Event = supportPreferenceTapEvent(
                                preferenceKey = SupportPreferenceKeys.DONATE_EXTREME,
                                productId = DonationProductIds.EXTREME_DONATION,
                            ),
                            enabled = extremeDonation?.isEligible == true && !isBillingInProgress,
                            vectorIcon = Icons.Outlined.Paid,
                            label = if (extremeDonation?.isEligible == true) {
                                extremeDonation.formattedPrice.orEmpty()
                            } else {
                                stringResource(id = R.string.support_offer_unavailable)
                            }
                        )
                    }
                }
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.non_paid_support),
                modifier = Modifier.padding(start = SizeConstants.LargeSize),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            GeneralTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.LargeSize),
                onClick = {
                    context.openUrl(ShortenLinkConstants.LINKVERTISE_APP_DIRECT_LINK)
                },
                firebaseController = firebaseController,
                ga4Event = supportPreferenceTapEvent(
                    preferenceKey = SupportPreferenceKeys.WEB_AD,
                    destination = ShortenLinkConstants.LINKVERTISE_APP_DIRECT_LINK,
                ),
                vectorIcon = Icons.Outlined.Paid,
                label = stringResource(id = R.string.web_ad)
            )
        }
        item {
            SupportNativeAdCard(
                modifier = Modifier
                    .padding(all = SizeConstants.LargeSize)
                    .animateItem(),
                adUnitId = nativeAdsConfig.bannerAdUnitId
            )
        }
    }
}


private fun supportPreferenceTapEvent(
    preferenceKey: String,
    productId: String? = null,
    destination: String? = null,
): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = buildMap {
            put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(SUPPORT_SCREEN_NAME))
            put(SettingsAnalytics.Params.PREFERENCE_KEY, AnalyticsValue.Str(preferenceKey))
            productId?.let { put(SupportPreferenceKeys.PRODUCT_ID, AnalyticsValue.Str(it)) }
            destination?.let { put(SupportPreferenceKeys.DESTINATION, AnalyticsValue.Str(it)) }
        },
    )
}

private fun supportActionEvent(actionName: String): AnalyticsEvent {
    return AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(SUPPORT_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}
