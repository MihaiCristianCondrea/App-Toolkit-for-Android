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
import com.android.billingclient.api.ProductDetails
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.GenericErrorText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.domain.models.DonationProductIds
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.contracts.SupportEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.mappers.toDonationOptions
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.models.DonationOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.states.SupportUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.utils.SupportAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.utils.beginCheckoutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.utils.donationResultEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repositories.BillingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.domain.models.PurchaseResult
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.utils.extensions.isValidForBilling
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for the support page: the donation options, and the purchase of one.
 *
 * [BillingRepository] is main-safe, so this needs no dispatcher. It reports a failed product query
 * through [BillingRepository.purchaseResult] instead of throwing, so a failure that arrives before
 * any options have loaded becomes the page's failure state with a retry. Once the options show, a
 * failed purchase is a message and the options stay.
 */
class SupportViewModel(
    private val billingRepository: BillingRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<SupportUiState, SupportEvent>(
    initialState = SupportUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "Support",
    viewModelName = "SupportViewModel",
) {
    /** Play's details for each loaded product, which launching its purchase needs. */
    private var productDetails: Map<String, ProductDetails> = emptyMap()

    /**
     * The donation this screen started and has not yet heard back about.
     *
     * Purchase results also arrive for purchases recovered in the background, such as one finished
     * while the app was closed. Only a result for a donation started here is a step in this
     * screen's funnel, so only those are reported.
     */
    private var checkoutProductId: String? = null

    private var productDetailsJob: Job? = null
    private var purchaseResultJob: Job? = null
    private var queryJob: Job? = null
    private var billingLaunchJob: Job? = null

    init {
        onEvent(SupportEvent.QueryProductDetails)
    }

    override fun handleEvent(event: SupportEvent) {
        when (event) {
            SupportEvent.QueryProductDetails -> load()
            is SupportEvent.Donate -> donate(activity = event.activity, productId = event.productId)
        }
    }

    /**
     * Observes the products and the purchase results where they are not observed already, so a
     * retry also restarts a stream that failed, then queries the products.
     */
    private fun load() {
        if (productDetailsJob?.isActive != true) observeProductDetails()
        if (purchaseResultJob?.isActive != true) observePurchaseResults()
        queryProductDetails()
    }

    /** The repository replays its last result, so a page opened again shows its options at once. */
    private fun observeProductDetails() {
        productDetailsJob = billingRepository.productDetails.collectReport(
            action = Actions.OBSERVE_PRODUCT_DETAILS,
            onError = { error -> showLoadFailure(error = error) },
        ) { details ->
            productDetails = details
            setState {
                copy(
                    donationOptions = if (details.isEmpty()) {
                        Loadable.Empty()
                    } else {
                        Loadable.Ready(details.toDonationOptions(productIds = DonationProducts))
                    },
                )
            }
        }
    }

    private fun observePurchaseResults() {
        purchaseResultJob = billingRepository.purchaseResult.collectReport(
            action = Actions.OBSERVE_PURCHASE_RESULT,
            onError = { error ->
                endBillingLaunch()
                showMessage(error.toErrorMessage(fallback = GenericErrorText))
            },
        ) { result ->
            onPurchaseResult(result = result)
        }
    }

    /**
     * Options already on screen stay while the query runs. The query publishes its result through
     * [BillingRepository.productDetails], which [observeProductDetails] renders.
     */
    private fun queryProductDetails() {
        queryJob = queryJob.restart {
            launchReport(
                action = Actions.QUERY_PRODUCT_DETAILS,
                onError = { error -> showLoadFailure(error = error) },
            ) {
                setState {
                    if (donationOptions is Loadable.Ready) this else copy(donationOptions = Loadable.Loading)
                }
                billingRepository.queryProductDetails(productIds = DonationProducts)
            }
        }
    }

    /**
     * Launches Play's purchase sheet and keeps the donation buttons disabled until a result arrives,
     * or for [BillingLaunchTimeout] when Play never answers. The [activity] reference is dropped
     * once Play has it, so the wait cannot keep a destroyed activity alive.
     */
    private fun donate(activity: Activity, productId: String) {
        if (!activity.isValidForBilling() || currentState.isBillingInProgress) return

        val option: DonationOption? = when (val options = currentState.donationOptions) {
            is Loadable.Ready -> options.value[productId]
            else -> null
        }
        val details: ProductDetails? = productDetails[productId]
        if (option?.isEligible != true || details == null) {
            showMessage(UiMessage(text = OfferUnavailableText, isError = true))
            return
        }

        val hostName: String = activity::class.java.name
        val host: AtomicReference<Activity?> = AtomicReference(activity)
        telemetryRepository.logEvent(beginCheckoutEvent(productId = productId, details = details))
        checkoutProductId = productId
        setState { copy(isBillingInProgress = true) }
        billingLaunchJob = billingLaunchJob.restart {
            launchReport(
                action = Actions.DONATE_CLICKED,
                extra = mapOf(
                    ExtraKeys.PRODUCT_ID to productId,
                    ExtraKeys.ACTIVITY to hostName,
                ),
                onError = { error ->
                    reportDonationResult(outcome = SupportAnalytics.Outcomes.FAILED)
                    setState { copy(isBillingInProgress = false) }
                    showMessage(error.toErrorMessage(fallback = GenericErrorText))
                },
            ) {
                billingRepository.launchInAppDonationFlow(
                    activity = checkNotNull(host.getAndSet(null)),
                    details = details,
                )
                delay(BillingLaunchTimeout)
                setState { copy(isBillingInProgress = false) }
            }
        }
    }

    /** Any result, a recovered background purchase's included, ends the wait for the sheet. */
    private fun onPurchaseResult(result: PurchaseResult) {
        reportDonationResult(outcome = result.toOutcome())
        endBillingLaunch()
        when (result) {
            PurchaseResult.Pending -> showMessage(UiMessage(UiTextHelper.StringResource(R.string.purchase_pending)))
            PurchaseResult.Success -> showMessage(UiMessage(UiTextHelper.StringResource(R.string.purchase_thank_you)))
            PurchaseResult.UserCancelled ->
                showMessage(UiMessage(UiTextHelper.StringResource(R.string.purchase_cancelled)))

            is PurchaseResult.Failed -> showPurchaseFailure(error = result.error)
        }
    }

    /**
     * Before any options have loaded, the failure is the product query's, so the page shows its
     * failure state with a retry. Otherwise the options stay, and a message gives Play's reason.
     */
    private fun showPurchaseFailure(error: String) {
        when (currentState.donationOptions) {
            Loadable.Loading -> setState { copy(donationOptions = Loadable.Failed(message = LoadFailedText)) }
            is Loadable.Failed -> Unit
            is Loadable.Empty, is Loadable.Ready ->
                showMessage(UiMessage(text = UiTextHelper.DynamicString(error), isError = true))
        }
    }

    /** Options already on screen stay, with a message; otherwise the failure replaces the page. */
    private fun showLoadFailure(error: Throwable) {
        if (currentState.donationOptions is Loadable.Ready) {
            showMessage(error.toErrorMessage(fallback = LoadFailedText))
        } else {
            setState { copy(donationOptions = error.toFailed(fallback = LoadFailedText)) }
        }
    }

    private fun endBillingLaunch() {
        billingLaunchJob?.cancel()
        billingLaunchJob = null
        setState { copy(isBillingInProgress = false) }
    }

    /** Reports how the donation this screen started ended, once, and forgets it. */
    private fun reportDonationResult(outcome: String) {
        val productId: String = checkoutProductId ?: return
        checkoutProductId = null
        telemetryRepository.logEvent(donationResultEvent(productId = productId, outcome = outcome))
    }

    private fun PurchaseResult.toOutcome(): String = when (this) {
        PurchaseResult.Pending -> SupportAnalytics.Outcomes.PENDING
        PurchaseResult.Success -> SupportAnalytics.Outcomes.SUCCESS
        is PurchaseResult.Failed -> SupportAnalytics.Outcomes.FAILED
        PurchaseResult.UserCancelled -> SupportAnalytics.Outcomes.CANCELLED
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

    private companion object {
        /** The donation tiers, in the order the page shows them. */
        val DonationProducts: List<String> = listOf(
            DonationProductIds.LOW_DONATION,
            DonationProductIds.NORMAL_DONATION,
            DonationProductIds.HIGH_DONATION,
            DonationProductIds.EXTREME_DONATION,
        )

        val BillingLaunchTimeout: Duration = 20.seconds

        /** Shown for a failure with no text of its own; see `toUiText` for the ones that have one. */
        val LoadFailedText = UiTextHelper.StringResource(R.string.error_failed_to_load_sku_details)

        val OfferUnavailableText = UiTextHelper.StringResource(R.string.support_offer_unavailable)
    }
}
