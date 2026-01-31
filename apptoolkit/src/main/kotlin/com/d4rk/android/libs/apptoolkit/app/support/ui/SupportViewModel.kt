package com.d4rk.android.libs.apptoolkit.app.support.ui

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
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
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState.Error
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

private const val BILLING_LAUNCH_TIMEOUT_MS = 20_000L

class SupportViewModel(
    private val billingRepository: BillingRepository,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<SupportScreenUiState, SupportEvent, SupportAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = SupportScreenUiState()
    )
) {

    private val donationProductIds = listOf(
        DonationProductIds.LOW_DONATION,
        DonationProductIds.NORMAL_DONATION,
        DonationProductIds.HIGH_DONATION,
        DonationProductIds.EXTREME_DONATION,
    )

    private var currentProductDetails: Map<String, ProductDetails> = emptyMap()
    private var billingTimeoutJob: Job? = null

    init {
        firebaseController.logBreadcrumb(
            message = "SupportViewModel initialized",
            attributes = mapOf("screen" to "Support"),
        )
        billingRepository.productDetails
            .onStart {
                if (screenData?.donationOptions?.isNotEmpty() == true) {
                    screenState.updateState(ScreenState.Success())
                } else {
                    screenState.setLoading()
                }
            }
            .onEach { detailsMap ->
                currentProductDetails = detailsMap
                val options = buildDonationOptions(detailsMap)
                if (detailsMap.isEmpty()) {
                    screenState.updateData(newState = ScreenState.NoData()) { current ->
                        current.copy(error = null, donationOptions = emptyList())
                    }
                } else {
                    screenState.updateData(newState = ScreenState.Success()) { current ->
                        current.copy(error = null, donationOptions = options)
                    }
                }
            }
            .onCompletion { cause ->
                when (cause) {
                    null -> {
                        if (screenData?.donationOptions?.isNotEmpty() == true) {
                            screenState.updateState(ScreenState.Success())
                        } else {
                            screenState.updateData(newState = ScreenState.NoData()) { current ->
                                current.copy(error = null, donationOptions = emptyList())
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
                firebaseController.reportViewModelError(
                    viewModelName = "SupportViewModel",
                    action = "observeProductDetails",
                    throwable = cause,
                )
            }
            .launchIn(viewModelScope)

        billingRepository.purchaseResult
            .onEach { result ->
                setBillingInProgress(false)
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
                firebaseController.reportViewModelError(
                    viewModelName = "SupportViewModel",
                    action = "observePurchaseResult",
                    throwable = cause,
                )
            }
            .launchIn(viewModelScope)

        queryProductDetails()
    }

    override fun onEvent(event: SupportEvent) {
        firebaseController.logBreadcrumb(
            message = "SupportViewModel event",
            attributes = mapOf("event" to event::class.java.simpleName),
        )
        when (event) {
            is SupportEvent.QueryProductDetails -> queryProductDetails()

            SupportEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    fun onDonateClicked(activity: Activity, productId: String) {
        firebaseController.logBreadcrumb(
            message = "Support donation clicked",
            attributes = mapOf(
                "productId" to productId,
                "activity" to activity::class.java.name,
            ),
        )
        if (!activity.isValidForBilling()) {
            return
        }

        if (screenData?.isBillingInProgress == true) {
            return
        }

        val option = screenData?.donationOptions?.firstOrNull { it.productId == productId }
        if (option?.isEligible != true) {
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

        val details = currentProductDetails[productId]
        if (details == null) {
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

        setBillingInProgress(true)
        startBillingTimeout()
        billingRepository.launchInAppDonationFlow(activity, details)
    }

    private fun queryProductDetails() {
        viewModelScope.launch {
            flow<DataState<Unit, Errors>> {
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
                    firebaseController.reportViewModelError(
                        viewModelName = "SupportViewModel",
                        action = "queryProductDetails",
                        throwable = throwable,
                    )
                    emit(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_SKU_DETAILS))
                }
                .onEach { result ->
                    result
                        .onSuccess {
                            if (screenData?.donationOptions?.isNotEmpty() == true) {
                                screenState.updateState(ScreenState.Success())
                            }
                        }
                        .onFailure { error ->
                            val snackbarMessage = error.asUiText()
                            screenState.updateData(newState = Error()) { current ->
                                current.copy(error = null)
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

    private fun buildDonationOptions(
        detailsMap: Map<String, ProductDetails>,
    ): List<DonationOptionUiState> =
        donationProductIds.map { productId ->
            val details = detailsMap[productId]
            DonationOptionUiState(
                productId = productId,
                formattedPrice = details?.primaryFormattedPrice(),
                isEligible = details?.hasOneTimePurchaseOffer() == true,
            )
        }

    private fun setBillingInProgress(inProgress: Boolean) {
        if (!inProgress) {
            billingTimeoutJob?.cancel()
            billingTimeoutJob = null
        }

        viewModelScope.launch {
            updateSuccessState(screenState) {
                it.copy(isBillingInProgress = inProgress)
            }
        }
    }

    private fun startBillingTimeout() {
        billingTimeoutJob?.cancel()
        billingTimeoutJob = viewModelScope.launch {
            delay(BILLING_LAUNCH_TIMEOUT_MS)
            setBillingInProgress(false)
        }
    }

    private fun Activity.isValidForBilling(): Boolean {
        if (isFinishing || isDestroyed) {
            return false
        }

        val lifecycleOwner = this as? LifecycleOwner ?: return true
        return lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }
}
