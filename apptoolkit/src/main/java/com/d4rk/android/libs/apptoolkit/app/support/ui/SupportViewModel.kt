package com.d4rk.android.libs.apptoolkit.app.support.ui

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository
import com.d4rk.android.libs.apptoolkit.app.support.billing.PurchaseResult
import com.d4rk.android.libs.apptoolkit.app.support.ui.contract.SupportAction
import com.d4rk.android.libs.apptoolkit.app.support.ui.contract.SupportEvent
import com.d4rk.android.libs.apptoolkit.app.support.ui.state.SupportScreenUiState
import com.d4rk.android.libs.apptoolkit.app.support.utils.constants.DonationProductIds
import com.d4rk.android.libs.apptoolkit.app.support.utils.extensions.primaryOfferToken
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SupportViewModel(
    private val billingRepository: BillingRepository,
) : ScreenViewModel<SupportScreenUiState, SupportEvent, SupportAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = SupportScreenUiState()
    )
) {

    init {
        billingRepository.productDetails
            .onStart {
                if (screenData?.products?.isNotEmpty() == true) {
                    screenState.updateState(ScreenState.Success())
                } else {
                    screenState.updateState(ScreenState.IsLoading())
                }
            }
            .map { it.values.toList() }
            .onEach { products ->
                if (products.isEmpty()) {
                    screenState.updateData(newState = ScreenState.NoData()) { current ->
                        current.copy(error = null, products = emptyList())
                    }
                } else {
                    screenState.updateData(newState = ScreenState.Success()) { current ->
                        current.copy(error = null, products = products)
                    }
                }
            }
            .onCompletion { cause ->
                when (cause) {
                    null -> {
                        if (screenData?.products?.isNotEmpty() == true) {
                            screenState.updateState(ScreenState.Success())
                        } else {
                            screenState.updateData(newState = ScreenState.NoData()) { current ->
                                current.copy(error = null, products = emptyList())
                            }
                        }
                    }

                    is CancellationException -> return@onCompletion
                    else -> {
                        val errorMessage = cause.message.orEmpty()
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(error = errorMessage)
                        }
                        screenState.showSnackbar(
                            UiSnackbar(
                                message = UiTextHelper.DynamicString(errorMessage),
                                isError = true,
                                timeStamp = System.currentTimeMillis(),
                                type = ScreenMessageType.SNACKBAR
                            )
                        )
                    }
                }
            }
            .catch { cause ->
                if (cause is CancellationException) throw cause
            }
            .launchIn(viewModelScope)

        billingRepository.purchaseResult
            .onEach { result ->
                when (result) {
                    PurchaseResult.Pending -> screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.purchase_pending),
                            isError = false,
                            timeStamp = System.currentTimeMillis(),
                            type = ScreenMessageType.SNACKBAR
                        )
                    )

                    PurchaseResult.Success -> screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.purchase_thank_you),
                            isError = false,
                            timeStamp = System.currentTimeMillis(),
                            type = ScreenMessageType.SNACKBAR
                        )
                    )

                    is PurchaseResult.Failed -> {
                        screenState.updateData(newState = ScreenState.Error()) { current ->
                            current.copy(error = result.error)
                        }
                        screenState.showSnackbar(
                            UiSnackbar(
                                message = UiTextHelper.DynamicString(result.error),
                                isError = true,
                                timeStamp = System.currentTimeMillis(),
                                type = ScreenMessageType.SNACKBAR
                            )
                        )
                    }

                    PurchaseResult.UserCancelled -> screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.purchase_cancelled),
                            isError = false,
                            timeStamp = System.currentTimeMillis(),
                            type = ScreenMessageType.SNACKBAR
                        )
                    )
                }
            }
            .onCompletion { cause ->
                if (cause == null || cause is CancellationException) {
                    return@onCompletion
                }

                val errorMessage = cause.message.orEmpty()
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.DynamicString(errorMessage),
                        isError = true,
                        timeStamp = System.currentTimeMillis(),
                        type = ScreenMessageType.SNACKBAR
                    )
                )
            }
            .catch { cause ->
                if (cause is CancellationException) throw cause
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            screenState.updateState(ScreenState.IsLoading())
            runCatching {
                billingRepository.queryProductDetails(
                    listOf(
                        DonationProductIds.LOW_DONATION,
                        DonationProductIds.NORMAL_DONATION,
                        DonationProductIds.HIGH_DONATION,
                        DonationProductIds.EXTREME_DONATION
                    )
                )
            }.onSuccess {
                // When product details are already cached, querying again won't emit
                // a new value. Ensure the UI exits the loading state in that case.
                if (screenData?.products?.isNotEmpty() == true) {
                    screenState.updateState(ScreenState.Success())
                }
            }.onFailure { e ->
                screenState.updateData(newState = ScreenState.Error()) { current ->
                    current.copy(error = e.message)
                }
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.DynamicString(e.message ?: ""),
                        isError = true,
                        timeStamp = System.currentTimeMillis(),
                        type = ScreenMessageType.SNACKBAR
                    )
                )
            }
        }
    }

    override fun onEvent(event: SupportEvent) {
        when (event) {
            is SupportEvent.QueryProductDetails -> viewModelScope.launch {
                screenState.updateState(ScreenState.IsLoading())
                runCatching {
                    billingRepository.queryProductDetails(
                        listOf(
                            DonationProductIds.LOW_DONATION,
                            DonationProductIds.NORMAL_DONATION,
                            DonationProductIds.HIGH_DONATION,
                            DonationProductIds.EXTREME_DONATION
                        )
                    )
                }.onSuccess {
                    if (screenData?.products?.isNotEmpty() == true) {
                        screenState.updateState(ScreenState.Success())
                    }
                }.onFailure { e ->
                    screenState.updateData(newState = ScreenState.Error()) { current ->
                        current.copy(error = e.message)
                    }
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.DynamicString(e.message ?: ""),
                            isError = true,
                            timeStamp = System.currentTimeMillis(),
                            type = ScreenMessageType.SNACKBAR
                        )
                    )
                }
            }

            SupportEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    fun onDonateClicked(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.primaryOfferToken()
        if (offerToken.isNullOrBlank()) {
            screenState.showSnackbar(
                UiSnackbar(
                    message = UiTextHelper.StringResource(R.string.support_offer_unavailable),
                    isError = true,
                    timeStamp = System.currentTimeMillis(),
                    type = ScreenMessageType.SNACKBAR
                )
            )
            return
        }

        billingRepository.launchPurchaseFlow(activity, productDetails, offerToken)
    }
}

