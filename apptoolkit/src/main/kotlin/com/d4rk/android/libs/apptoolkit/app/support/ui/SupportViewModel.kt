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
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState.Error
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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
                    screenState.setLoading()
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
                        screenState.updateData(newState = Error()) { current ->
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
                        screenState.updateData(newState = Error()) { current ->
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

        queryProductDetails()
    }

    override fun onEvent(event: SupportEvent) {
        when (event) {
            is SupportEvent.QueryProductDetails -> queryProductDetails()

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

    private fun queryProductDetails() {
        viewModelScope.launch {
            flow<DataState<Unit, BillingError>> { // FIXME: Type argument is not within its bounds: must be subtype of 'Error'.
                billingRepository.queryProductDetails(
                    productIds = listOf(
                        DonationProductIds.LOW_DONATION,
                        DonationProductIds.NORMAL_DONATION,
                        DonationProductIds.HIGH_DONATION,
                        DonationProductIds.EXTREME_DONATION
                    )
                )
                emit(DataState.Success(Unit))
            }
                .onStart { screenState.setLoading() }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    emit(
                        DataState.Error(
                            error = BillingError(message = throwable.message) // FIXME: Argument type mismatch: actual type is 'SupportViewModel.BillingError', but 'Error' was expected.
                        )
                    )
                }
                .onEach { result ->
                    result
                        .onSuccess {
                            if (screenData?.products?.isNotEmpty() == true) {
                                screenState.updateState(ScreenState.Success())
                            }
                        }
                        .onFailure { error -> // TODO: should use thesae correctly across entire project onFailure and onSuccess
                            val errorMessage = error.message.orEmpty()
                            val snackbarMessage = if (errorMessage.isNotBlank()) {
                                UiTextHelper.DynamicString(errorMessage)
                            } else {
                                UiTextHelper.StringResource(R.string.error_failed_to_load_sku_details)
                            }

                            screenState.updateData(newState = Error()) { current ->
                                current.copy(error = errorMessage.ifBlank { null })
                            }
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = snackbarMessage,
                                    isError = true,
                                    timeStamp = System.currentTimeMillis(),
                                    type = ScreenMessageType.SNACKBAR
                                )
                            )
                        }
                }
                .launchIn(viewModelScope)
        }
    }

    data class BillingError(val message: String?) :
        Error(message) // TODO: move somehwere else FIXME: Function 'fun component1(): String?' generated for the data class conflicts with the supertype member 'fun component1(): String' defined in 'com/d4rk/android/libs/apptoolkit/core/ui/state/ScreenState.Error'.
}
