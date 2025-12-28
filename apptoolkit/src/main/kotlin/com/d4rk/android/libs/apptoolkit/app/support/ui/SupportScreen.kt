package com.d4rk.android.libs.apptoolkit.app.support.ui

import android.app.Activity
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.d4rk.android.libs.apptoolkit.app.support.ui.state.SupportScreenUiState
import com.d4rk.android.libs.apptoolkit.app.support.utils.constants.DonationProductIds
import com.d4rk.android.libs.apptoolkit.app.support.utils.constants.ShortenLinkConstants
import com.d4rk.android.libs.apptoolkit.app.support.utils.extensions.primaryFormattedPrice
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.SupportNativeAdCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.TonalIconButtonWithText
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHandler
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.platform.IntentsHelper
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportComposable() {
    val viewModel: SupportViewModel = koinViewModel()
    val activity = LocalActivity.current
    val screenState: UiStateScreen<SupportScreenUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val currentViewModel = rememberUpdatedState(newValue = viewModel)

    val onDonateClick: (Activity, com.android.billingclient.api.ProductDetails) -> Unit =
        remember(currentViewModel) {
            { hostActivity, productDetails ->
                currentViewModel.value.onDonateClicked(
                    activity = hostActivity,
                    productDetails = productDetails
                )
            }
        }

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.support_us),
        onBackClicked = { activity?.finish() },
        snackbarHostState = snackbarHostState
    ) { paddingValues ->
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
                val productDetailsMap = data.products.associateBy { it.productId }
                val currentProducts = rememberUpdatedState(newValue = productDetailsMap)
                SupportScreenContent(
                    paddingValues = paddingValues,
                    getPriceLabel = { productId ->
                        currentProducts.value[productId]?.primaryFormattedPrice().orEmpty()
                    },
                    onDonateClick = { productId ->
                        val productDetails =
                            currentProducts.value[productId] ?: return@SupportScreenContent
                        activity?.let { hostActivity ->
                            onDonateClick(hostActivity, productDetails)
                        }
                    }
                )
            })
        DefaultSnackbarHandler(
            screenState = screenState,
            snackbarHostState = snackbarHostState,
            getDismissEvent = { SupportEvent.DismissSnackbar },
            onEvent = { viewModel.onEvent(it) }
        )
    }
}

@Composable
fun SupportScreenContent(
    paddingValues: PaddingValues,
    getPriceLabel: (String) -> String,
    onDonateClick: (String) -> Unit,
) {
    val context: Context = LocalContext.current
    val nativeAdsConfig: AdsConfig = koinInject(qualifier = named(name = "support_native_ad"))

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
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SizeConstants.LargeSize),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        item {
                            TonalIconButtonWithText(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onDonateClick(DonationProductIds.LOW_DONATION)
                                },
                                icon = Icons.Outlined.Paid,
                                label = getPriceLabel(DonationProductIds.LOW_DONATION)
                            )
                        }
                        item {
                            TonalIconButtonWithText(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onDonateClick(DonationProductIds.NORMAL_DONATION)
                                },
                                icon = Icons.Outlined.Paid,
                                label = getPriceLabel(DonationProductIds.NORMAL_DONATION)
                            )
                        }
                    }
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = SizeConstants.LargeSize),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        item {
                            TonalIconButtonWithText(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onDonateClick(DonationProductIds.HIGH_DONATION)
                                },
                                icon = Icons.Outlined.Paid,
                                label = getPriceLabel(DonationProductIds.HIGH_DONATION)
                            )
                        }
                        item {
                            TonalIconButtonWithText(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onDonateClick(DonationProductIds.EXTREME_DONATION)
                                },
                                icon = Icons.Outlined.Paid,
                                label = getPriceLabel(DonationProductIds.EXTREME_DONATION)
                            )
                        }
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
            TonalIconButtonWithText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.LargeSize),
                onClick = {
                    IntentsHelper.openUrl(
                        context = context,
                        url = ShortenLinkConstants.LINKVERTISE_APP_DIRECT_LINK
                    )
                },
                icon = Icons.Outlined.Paid,
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