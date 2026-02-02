package com.d4rk.android.libs.apptoolkit.app.support.ui

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository
import com.d4rk.android.libs.apptoolkit.app.support.billing.PurchaseResult
import com.d4rk.android.libs.apptoolkit.app.support.ui.contract.SupportAction
import com.d4rk.android.libs.apptoolkit.app.support.ui.contract.SupportEvent
import com.d4rk.android.libs.apptoolkit.app.support.ui.state.DonationOptionUiState
import com.d4rk.android.libs.apptoolkit.app.support.ui.state.SupportScreenUiState
import com.d4rk.android.libs.apptoolkit.app.support.utils.constants.DonationProductIds
import com.d4rk.android.libs.apptoolkit.app.support.utils.extensions.hasOneTimePurchaseOffer
import com.d4rk.android.libs.apptoolkit.app.support.utils.extensions.primaryFormattedPrice
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.setNoData
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.activity.isValidForBilling
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val BILLING_LAUNCH_TIMEOUT_MS = 20_000L

/**
 * ViewModel responsible for the support/donation flow.
 *
 * Handles billing setup, product detail observation, and purchase result UI updates.
 */
class SupportViewModel(
    private val billingRepository: BillingRepository,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<SupportScreenUiState, SupportEvent, SupportAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = SupportScreenUiState(),
    ),
    firebaseController = firebaseController,
    screenName = "Support",
) {

    private val donationProductIds = listOf(
        DonationProductIds.LOW_DONATION,
        DonationProductIds.NORMAL_DONATION,
        DonationProductIds.HIGH_DONATION,
        DonationProductIds.EXTREME_DONATION,
    )

    private var currentProductDetails: Map<String, ProductDetails> = emptyMap()

    private var productDetailsJob: Job? = null
    private var purchaseResultJob: Job? = null
    private var queryJob: Job? = null
    private var billingTimeoutJob: Job? = null

    init {
        handleEvent(SupportEvent.SetUpBilling)
    }

    override fun handleEvent(event: SupportEvent) {
        when (event) {
            is SupportEvent.SetUpBilling -> setupBilling()
            is SupportEvent.QueryProductDetails -> queryProductDetails()
            is SupportEvent.DismissSnackbar -> dismissSnackbar()
        }
    }

    fun setupBilling() {
        observeProductDetails()
        observePurchaseResults()
        queryProductDetails()
    }

    fun onDonateClicked(activity: Activity, productId: String) {
        startOperation(
            action = Actions.DONATE_CLICKED,
            extra = mapOf(
                ExtraKeys.PRODUCT_ID to productId,
                ExtraKeys.ACTIVITY to activity::class.java.name
            )
        )

        if (!activity.isValidForBilling()) return
        if (screenData?.isBillingInProgress == true) return

        val option = screenData?.donationOptions?.firstOrNull { it.productId == productId }
        if (option?.isEligible != true) {
            showOfferUnavailable()
            return
        }

        val details = currentProductDetails[productId]
        if (details == null) {
            showOfferUnavailable()
            return
        }
        viewModelScope.launch {
            updateStateThreadSafe {
                setBillingInProgress(inProgress = true)
                startBillingTimeout()
            }

            runCatching {
                billingRepository.launchInAppDonationFlow(activity, details)
            }.onFailure { throwable ->
                updateStateThreadSafe {
                    setBillingInProgress(inProgress = false)
                    screenState.setError(
                        message = UiTextHelper.DynamicString(
                            throwable.message ?: "Billing launch failed"
                        )
                    )
                }
            }
        }
    }

    private fun observeProductDetails() {
        productDetailsJob = productDetailsJob.restart {
            startOperation(action = Actions.OBSERVE_PRODUCT_DETAILS)

            billingRepository.productDetails
                .onStart {
                    updateStateThreadSafe {
                        if (screenData?.donationOptions.isNullOrEmpty()) {
                            screenState.setLoading()
                        } else {
                            screenState.updateState(ScreenState.Success())
                        }
                    }
                }
                .onEach { detailsMap ->
                    currentProductDetails = detailsMap

                    val options = buildDonationOptions(detailsMap)
                    val base = screenData ?: SupportScreenUiState()
                    val updated = base.copy(error = null, donationOptions = options)

                    updateStateThreadSafe {
                        if (detailsMap.isEmpty()) {
                            screenState.setNoData(data = updated)
                        } else {
                            screenState.setSuccess(data = updated)
                        }
                    }
                }
                .catchReport(action = Actions.OBSERVE_PRODUCT_DETAILS) { throwable ->
                    val message = UiTextHelper.DynamicString(throwable.message.orEmpty())

                    updateStateThreadSafe {
                        screenState.copyData { copy(error = throwable.message.orEmpty()) }
                        screenState.setError(message = message)
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun observePurchaseResults() {
        startOperation(action = Actions.OBSERVE_PURCHASE_RESULT)
        purchaseResultJob = purchaseResultJob.restart {
            billingRepository.purchaseResult
                .onEach { result ->
                    when (result) {
                        PurchaseResult.Pending -> updateStateThreadSafe {
                            setBillingInProgress(inProgress = false)
                            clearError()
                            restoreScreenStateFromData()
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = UiTextHelper.StringResource(
                                        R.string.purchase_pending
                                    ),
                                    isError = false,
                                    timeStamp = System.nanoTime(),
                                    type = ScreenMessageType.SNACKBAR
                                )
                            )
                        }

                        PurchaseResult.Success -> updateStateThreadSafe {
                            setBillingInProgress(inProgress = false)
                            clearError()
                            restoreScreenStateFromData()
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = UiTextHelper.StringResource(
                                        R.string.purchase_thank_you
                                    ),
                                    isError = false,
                                    timeStamp = System.nanoTime(),
                                    type = ScreenMessageType.SNACKBAR
                                )
                            )
                        }

                        is PurchaseResult.Failed -> updateStateThreadSafe {
                            setBillingInProgress(inProgress = false)
                            screenState.copyData { copy(error = result.error) }
                            screenState.setError(message = UiTextHelper.DynamicString(result.error))
                        }

                        PurchaseResult.UserCancelled -> updateStateThreadSafe {
                            setBillingInProgress(inProgress = false)
                            clearError()
                            restoreScreenStateFromData()
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = UiTextHelper.StringResource(
                                        R.string.purchase_cancelled
                                    ),
                                    isError = false,
                                    timeStamp = System.nanoTime(),
                                    type = ScreenMessageType.SNACKBAR
                                )
                            )
                        }
                    }
                }
                .catchReport(action = Actions.OBSERVE_PURCHASE_RESULT) { throwable ->
                    updateStateThreadSafe {
                        setBillingInProgress(inProgress = false)
                        screenState.setError(message = UiTextHelper.DynamicString(throwable.message.orEmpty()))
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun queryProductDetails() {
        queryJob = queryJob.restart {
            launchReport(
                action = Actions.QUERY_PRODUCT_DETAILS,
                block = {
                    updateStateThreadSafe {
                        if (screenData?.donationOptions.isNullOrEmpty()) {
                            screenState.setLoading()
                        }
                    }

                    billingRepository.queryProductDetails(productIds = donationProductIds)
                },
                onError = { throwable ->
                    updateStateThreadSafe {
                        clearError()
                        screenState.showSnackbar(
                            UiSnackbar(
                                message = Errors.UseCase.FAILED_TO_LOAD_SKU_DETAILS.asUiText(),
                                isError = true,
                                timeStamp = System.nanoTime(),
                                type = ScreenMessageType.SNACKBAR
                            )
                        )
                        screenState.updateState(ScreenState.Error())
                        screenState.copyData { copy(error = throwable.message.orEmpty()) }
                    }
                },
            )
        }
    }

    private fun buildDonationOptions(detailsMap: Map<String, ProductDetails>): List<DonationOptionUiState> {
        return donationProductIds.map { productId ->
            val details = detailsMap[productId]
            DonationOptionUiState(
                productId = productId,
                formattedPrice = details?.primaryFormattedPrice(),
                isEligible = details?.hasOneTimePurchaseOffer() == true,
            )
        }
    }

    private fun dismissSnackbar() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.dismissSnackbar()
            }
        }
    }

    private fun showOfferUnavailable() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.StringResource(R.string.support_offer_unavailable),
                        isError = true,
                        timeStamp = System.nanoTime(),
                        type = ScreenMessageType.SNACKBAR
                    )
                )
            }
        }
    }

    private fun setBillingInProgress(inProgress: Boolean) {
        if (!inProgress) {
            billingTimeoutJob?.cancel()
            billingTimeoutJob = null
        }
        screenState.copyData { copy(isBillingInProgress = inProgress) }
    }

    private fun startBillingTimeout() {
        billingTimeoutJob?.cancel()
        billingTimeoutJob = viewModelScope.launch {
            delay(BILLING_LAUNCH_TIMEOUT_MS)
            updateStateThreadSafe { setBillingInProgress(inProgress = false) }
        }
    }

    private fun clearError() {
        screenState.copyData { copy(error = null) }
    }

    private fun restoreScreenStateFromData() {
        val hasOptions = screenData?.donationOptions?.isNotEmpty() == true
        screenState.updateState(if (hasOptions) ScreenState.Success() else ScreenState.NoData())
    }

    private object Actions {
        const val OBSERVE_PRODUCT_DETAILS: String = "observeProductDetails"
        const val OBSERVE_PURCHASE_RESULT: String = "observePurchaseResult"
        const val QUERY_PRODUCT_DETAILS: String = "queryProductDetails"
        const val DONATE_CLICKED: String = "donateClicked"
    }

    private object ExtraKeys {
        const val PRODUCT_ID: String = "productId"
        const val ACTIVITY: String = "activity"
    }
}
