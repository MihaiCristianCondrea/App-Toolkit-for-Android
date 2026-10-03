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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.domain.models.DonationProductIds
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.constants.ShortenLinkConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.contracts.SupportEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.models.DonationOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.states.SupportUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.views.ads.SupportNativeAdCard
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
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

@Composable
fun SupportComposable() {
    SupportScreen()
}

/**
 * The support page: donation tiers bought through Google Play, a web ad link and a native ad. It
 * is the body of the page `supportPage()` registers, which the overflow menu's `supportUs()` entry
 * opens.
 *
 * Owns [SupportViewModel], tracking and messages. A purchase needs the host activity, so a tap
 * while there is none does nothing.
 */
@Composable
fun SupportScreen() {
    val viewModel: SupportViewModel = koinViewModel()
    val state: SupportUiState by viewModel.state.collectAsStateWithLifecycle()
    val context: Context = LocalContext.current
    val activity: Activity? = LocalActivity.current
    val adsConfig: AdsConfig = koinInject(qualifier = named(name = AdsQualifiers.SUPPORT_NATIVE_AD))

    TrackScreenView(
        screenName = SUPPORT_SCREEN_NAME,
        screenClass = SUPPORT_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = SUPPORT_SCREEN_NAME,
        state = state.donationOptions,
    )

    SupportScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onDonate = { productId ->
            activity?.let { host -> viewModel.onEvent(SupportEvent.Donate(productId = productId, activity = host)) }
        },
        onOpenWebAd = { context.openUrl(ShortenLinkConstants.LINKVERTISE_APP_DIRECT_LINK) },
        contentPadding = contentPadding(),
        adUnitId = adsConfig.bannerAdUnitId,
    )

    MessageHost(viewModel = viewModel)
}

/**
 * Renders the donation tiers, the web ad and the native ad for [state], or the loading, empty or
 * failure state. Each button logs its own tap through its `ga4Event`. The failure state keeps the
 * money icon and offers a retry when the failure is retryable.
 *
 * @param onEvent Receives the events [SupportViewModel] handles.
 * @param onDonate A donation tier was tapped. Launching the purchase needs the activity, so it
 * belongs to the caller.
 * @param onOpenWebAd The web ad button was tapped. Opening the link needs a `Context`.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 * @param adUnitId The native ad slot's unit, or null when no ad should show.
 */
@Composable
internal fun SupportScreenContent(
    state: SupportUiState,
    onEvent: (SupportEvent) -> Unit,
    onDonate: (productId: String) -> Unit,
    onOpenWebAd: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    adUnitId: String? = null,
) {
    ScreenStateHandler(
        state = state.donationOptions,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(SupportEvent.QueryProductDetails) },
        onError = { failed ->
            NoDataScreen(
                message = failed.message.asString(),
                icon = Icons.Outlined.MoneyOff,
                isError = true,
                showRetry = failed.retryable,
                onRetry = { onEvent(SupportEvent.QueryProductDetails) },
                paddingValues = contentPadding,
            )
        },
    ) { ready ->
        SupportList(
            donationOptions = ready.value,
            isBillingInProgress = state.isBillingInProgress,
            onDonate = onDonate,
            onOpenWebAd = onOpenWebAd,
            contentPadding = contentPadding,
            adUnitId = adUnitId,
        )
    }
}

@Composable
private fun SupportList(
    donationOptions: ImmutableMap<String, DonationOption>,
    isBillingInProgress: Boolean,
    onDonate: (productId: String) -> Unit,
    onOpenWebAd: () -> Unit,
    contentPadding: PaddingValues,
    adUnitId: String?,
) {
    LazyColumn(contentPadding = contentPadding) {
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
                        DonationButton(
                            productId = DonationProductIds.LOW_DONATION,
                            preferenceKey = SupportPreferenceKeys.DONATE_LOW,
                            option = donationOptions[DonationProductIds.LOW_DONATION],
                            isBillingInProgress = isBillingInProgress,
                            onDonate = onDonate,
                            modifier = Modifier.weight(1f),
                        )
                        Spacer(modifier = Modifier.width(SizeConstants.MediumSize))
                        DonationButton(
                            productId = DonationProductIds.NORMAL_DONATION,
                            preferenceKey = SupportPreferenceKeys.DONATE_NORMAL,
                            option = donationOptions[DonationProductIds.NORMAL_DONATION],
                            isBillingInProgress = isBillingInProgress,
                            onDonate = onDonate,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = SizeConstants.LargeSize),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        DonationButton(
                            productId = DonationProductIds.HIGH_DONATION,
                            preferenceKey = SupportPreferenceKeys.DONATE_HIGH,
                            option = donationOptions[DonationProductIds.HIGH_DONATION],
                            isBillingInProgress = isBillingInProgress,
                            onDonate = onDonate,
                            modifier = Modifier.weight(1f),
                        )
                        Spacer(modifier = Modifier.width(SizeConstants.MediumSize))
                        DonationButton(
                            productId = DonationProductIds.EXTREME_DONATION,
                            preferenceKey = SupportPreferenceKeys.DONATE_EXTREME,
                            option = donationOptions[DonationProductIds.EXTREME_DONATION],
                            isBillingInProgress = isBillingInProgress,
                            onDonate = onDonate,
                            modifier = Modifier.weight(1f),
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
            GeneralButton(
                style = GeneralButtonStyle.Tonal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.LargeSize),
                onClick = onOpenWebAd,
                ga4Event = supportPreferenceTapEvent(
                    preferenceKey = SupportPreferenceKeys.WEB_AD,
                    destination = ShortenLinkConstants.LINKVERTISE_APP_DIRECT_LINK,
                ),
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Paid),
                label = stringResource(id = R.string.web_ad)
            )
        }
        if (adUnitId != null) {
            item {
                SupportNativeAdCard(
                    modifier = Modifier
                        .padding(all = SizeConstants.LargeSize)
                        .animateItem(),
                    adUnitId = adUnitId,
                )
            }
        }
    }
}

/** A tier without a one-time offer from Play shows as unavailable and cannot be tapped. */
@Composable
private fun DonationButton(
    productId: String,
    preferenceKey: String,
    option: DonationOption?,
    isBillingInProgress: Boolean,
    onDonate: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val eligibleOption: DonationOption? = option?.takeIf { it.isEligible }
    GeneralButton(
        style = GeneralButtonStyle.Tonal,
        modifier = modifier,
        onClick = { onDonate(productId) },
        ga4Event = supportPreferenceTapEvent(
            preferenceKey = preferenceKey,
            productId = productId,
        ),
        enabled = eligibleOption != null && !isBillingInProgress,
        icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Paid),
        label = if (eligibleOption != null) {
            eligibleOption.formattedPrice.orEmpty()
        } else {
            stringResource(id = R.string.support_offer_unavailable)
        }
    )
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

@Preview(showBackground = true)
@Composable
private fun SupportScreenContentPreview() {
    MaterialTheme {
        SupportScreenContent(
            state = SupportUiState(
                donationOptions = Loadable.Ready(
                    persistentMapOf(
                        DonationProductIds.LOW_DONATION to DonationOption(
                            productId = DonationProductIds.LOW_DONATION,
                            formattedPrice = "€0.99",
                            isEligible = true,
                        ),
                        DonationProductIds.NORMAL_DONATION to DonationOption(
                            productId = DonationProductIds.NORMAL_DONATION,
                            formattedPrice = "€1.99",
                            isEligible = true,
                        ),
                        DonationProductIds.HIGH_DONATION to DonationOption(
                            productId = DonationProductIds.HIGH_DONATION,
                            formattedPrice = "€4.99",
                            isEligible = true,
                        ),
                        DonationProductIds.EXTREME_DONATION to DonationOption(
                            productId = DonationProductIds.EXTREME_DONATION,
                            formattedPrice = null,
                            isEligible = false,
                        ),
                    )
                ),
            ),
            onEvent = {},
            onDonate = {},
            onOpenWebAd = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SupportScreenContentLoadingPreview() {
    MaterialTheme {
        SupportScreenContent(
            state = SupportUiState(donationOptions = Loadable.Loading),
            onEvent = {},
            onDonate = {},
            onOpenWebAd = {},
        )
    }
}
