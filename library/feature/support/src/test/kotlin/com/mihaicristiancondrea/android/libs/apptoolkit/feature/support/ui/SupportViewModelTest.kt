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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.GenericErrorText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.domain.models.DonationProductIds
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.contracts.SupportEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.models.DonationOption
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repositories.BillingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.domain.models.PurchaseResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SupportViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(
        billingRepository: FakeBillingRepository = FakeBillingRepository(),
    ): SupportViewModel = SupportViewModel(
        billingRepository = billingRepository,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun SupportViewModel.readyOptions(): ImmutableMap<String, DonationOption> =
        assertIs<Loadable.Ready<ImmutableMap<String, DonationOption>>>(state.value.donationOptions).value

    private fun SupportViewModel.onlyMessage(): UiMessage = messages.value.single()

    private fun SupportViewModel.donateLow(activity: Activity = readyActivity()) =
        onEvent(SupportEvent.Donate(productId = DonationProductIds.LOW_DONATION, activity = activity))

    private val UiMessage.resourceId: Int
        get() = (text as UiTextHelper.StringResource).resourceId

    private fun events(name: String): List<AnalyticsEvent> =
        telemetryRepository.loggedEvents.filter { it.name == name }

    @Test
    fun `the first load shows every tier with the price Play returned`() =
        runTest(dispatcherExtension.testDispatcher) {
            val billing = FakeBillingRepository(
                products = mapOf(
                    DonationProductIds.LOW_DONATION to donationProduct(DonationProductIds.LOW_DONATION, "€0.99"),
                    DonationProductIds.NORMAL_DONATION to donationProduct(DonationProductIds.NORMAL_DONATION, "€1.99"),
                ),
            )
            val viewModel = createViewModel(billingRepository = billing)
            advance()

            val options = viewModel.readyOptions()
            assertEquals(
                listOf(
                    DonationProductIds.LOW_DONATION,
                    DonationProductIds.NORMAL_DONATION,
                    DonationProductIds.HIGH_DONATION,
                    DonationProductIds.EXTREME_DONATION,
                ),
                options.keys.toList(),
            )
            assertEquals(
                DonationOption(
                    productId = DonationProductIds.LOW_DONATION,
                    formattedPrice = "€0.99",
                    isEligible = true,
                ),
                options.getValue(DonationProductIds.LOW_DONATION),
            )
            assertEquals("€1.99", options.getValue(DonationProductIds.NORMAL_DONATION).formattedPrice)
            assertFalse(options.getValue(DonationProductIds.HIGH_DONATION).isEligible)
            assertEquals(1, billing.queries)
            assertFalse(viewModel.state.value.isBillingInProgress)
            assertTrue(viewModel.messages.value.isEmpty())
        }

    @Test
    fun `the page stays loading until the product query publishes a result`() =
        runTest(dispatcherExtension.testDispatcher) {
            val billing = FakeBillingRepository(products = null)
            val viewModel = createViewModel(billingRepository = billing)
            advance()

            assertEquals(Loadable.Loading, viewModel.state.value.donationOptions)

            billing.productDetailsFlow.emit(emptyMap())

            assertIs<Loadable.Empty>(viewModel.state.value.donationOptions)
        }

    @Test
    fun `no products from Play is the empty state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(billingRepository = FakeBillingRepository(products = emptyMap()))
        advance()

        assertIs<Loadable.Empty>(viewModel.state.value.donationOptions)
    }

    @Test
    fun `a thrown query failure shows the page's failure text with a retry`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(
                billingRepository = FakeBillingRepository(queryFailure = IllegalStateException("boom")),
            )
            advance()

            val failed = assertIs<Loadable.Failed>(viewModel.state.value.donationOptions)
            assertEquals(UiTextHelper.StringResource(R.string.error_failed_to_load_sku_details), failed.message)
            assertTrue(failed.retryable)
            assertTrue(viewModel.messages.value.isEmpty())
            assertTrue(events("vm_op_error").any { it.params["action"] == AnalyticsValue.Str("queryProductDetails") })
        }

    @Test
    fun `an offline query failure shows its own text`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            billingRepository = FakeBillingRepository(
                queryFailure = NetworkException(NetworkException.Reason.NO_INTERNET),
            ),
        )
        advance()

        val failed = assertIs<Loadable.Failed>(viewModel.state.value.donationOptions)
        assertEquals(UiTextHelper.StringResource(CoreUiR.string.screen_error_no_internet), failed.message)
    }

    @Test
    fun `a query failure billing reports as a purchase result shows the failure state`() =
        runTest(dispatcherExtension.testDispatcher) {
            val billing = FakeBillingRepository(products = null)
            val viewModel = createViewModel(billingRepository = billing)
            advance()

            billing.purchaseResults.emit(PurchaseResult.Failed("Billing service unavailable"))

            val failed = assertIs<Loadable.Failed>(viewModel.state.value.donationOptions)
            assertEquals(UiTextHelper.StringResource(R.string.error_failed_to_load_sku_details), failed.message)
            assertTrue(failed.retryable)
            assertTrue(viewModel.messages.value.isEmpty())
        }

    @Test
    fun `retrying shows loading while the query runs, then the options`() =
        runTest(dispatcherExtension.testDispatcher) {
            val billing = FakeBillingRepository(queryFailure = IllegalStateException("boom"))
            val viewModel = createViewModel(billingRepository = billing)
            advance()
            assertIs<Loadable.Failed>(viewModel.state.value.donationOptions)

            val gate = CompletableDeferred<Unit>()
            billing.queryFailure = null
            billing.queryGate = gate
            viewModel.onEvent(SupportEvent.QueryProductDetails)
            advance()
            assertEquals(Loadable.Loading, viewModel.state.value.donationOptions)

            gate.complete(Unit)
            advance()

            assertTrue(viewModel.readyOptions().getValue(DonationProductIds.LOW_DONATION).isEligible)
            assertEquals(2, billing.queries)
        }

    @Test
    fun `a failed purchase keeps the options and shows Play's reason`() =
        runTest(dispatcherExtension.testDispatcher) {
            val billing = FakeBillingRepository()
            val viewModel = createViewModel(billingRepository = billing)
            advance()

            billing.purchaseResults.emit(PurchaseResult.Failed("Card declined"))

            assertTrue(viewModel.readyOptions().isNotEmpty())
            val message = viewModel.onlyMessage()
            assertEquals(UiTextHelper.DynamicString("Card declined"), message.text)
            assertTrue(message.isError)
        }

    @Test
    fun `a failed purchase after an empty result is a message`() = runTest(dispatcherExtension.testDispatcher) {
        val billing = FakeBillingRepository(products = emptyMap())
        val viewModel = createViewModel(billingRepository = billing)
        advance()

        billing.purchaseResults.emit(PurchaseResult.Failed("Item is unavailable"))

        assertIs<Loadable.Empty>(viewModel.state.value.donationOptions)
        assertTrue(viewModel.onlyMessage().isError)
    }

    @Test
    fun `a pending purchase shows the processing message`() = runTest(dispatcherExtension.testDispatcher) {
        assertPurchaseMessage(result = PurchaseResult.Pending, resourceId = R.string.purchase_pending)
    }

    @Test
    fun `a successful purchase thanks the user`() = runTest(dispatcherExtension.testDispatcher) {
        assertPurchaseMessage(result = PurchaseResult.Success, resourceId = R.string.purchase_thank_you)
    }

    @Test
    fun `a cancelled purchase says so`() = runTest(dispatcherExtension.testDispatcher) {
        assertPurchaseMessage(result = PurchaseResult.UserCancelled, resourceId = R.string.purchase_cancelled)
    }

    private suspend fun assertPurchaseMessage(result: PurchaseResult, resourceId: Int) {
        val billing = FakeBillingRepository()
        val viewModel = createViewModel(billingRepository = billing)
        advance()

        billing.purchaseResults.emit(result)

        val message = viewModel.onlyMessage()
        assertEquals(resourceId, message.resourceId)
        assertFalse(message.isError)
    }

    @Test
    fun `donating launches the purchase and disables the buttons until a result arrives`() =
        runTest(dispatcherExtension.testDispatcher) {
            val billing = FakeBillingRepository()
            val viewModel = createViewModel(billingRepository = billing)
            advance()
            val activity = readyActivity()

            viewModel.donateLow(activity = activity)

            assertEquals(listOf(activity to billing.lowProduct), billing.launches)
            assertTrue(viewModel.state.value.isBillingInProgress)

            billing.purchaseResults.emit(PurchaseResult.Success)

            assertFalse(viewModel.state.value.isBillingInProgress)
        }

    @Test
    fun `a second tap while a purchase is launching is ignored`() = runTest(dispatcherExtension.testDispatcher) {
        val billing = FakeBillingRepository()
        val viewModel = createViewModel(billingRepository = billing)
        advance()

        viewModel.donateLow()
        viewModel.donateLow()

        assertEquals(1, billing.launches.size)
        assertEquals(1, events("begin_checkout").size)
    }

    @Test
    fun `the buttons are enabled again when Play never answers`() = runTest(dispatcherExtension.testDispatcher) {
        val billing = FakeBillingRepository()
        val viewModel = createViewModel(billingRepository = billing)
        advance()

        viewModel.donateLow()
        assertTrue(viewModel.state.value.isBillingInProgress)
        advance()

        assertFalse(viewModel.state.value.isBillingInProgress)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `a tier without a one-time offer says it is unavailable`() = runTest(dispatcherExtension.testDispatcher) {
        val billing = FakeBillingRepository(
            products = mapOf(DonationProductIds.LOW_DONATION to productWithoutOffer(DonationProductIds.LOW_DONATION)),
        )
        val viewModel = createViewModel(billingRepository = billing)
        advance()

        viewModel.donateLow()

        assertTrue(billing.launches.isEmpty())
        assertFalse(viewModel.state.value.isBillingInProgress)
        val message = viewModel.onlyMessage()
        assertEquals(R.string.support_offer_unavailable, message.resourceId)
        assertTrue(message.isError)
    }

    @Test
    fun `an activity that is finishing launches nothing`() = runTest(dispatcherExtension.testDispatcher) {
        val billing = FakeBillingRepository()
        val viewModel = createViewModel(billingRepository = billing)
        advance()
        val finishing: Activity = mockk(relaxed = true) {
            every { isFinishing } returns true
        }

        viewModel.donateLow(activity = finishing)

        assertTrue(billing.launches.isEmpty())
        assertTrue(viewModel.messages.value.isEmpty())
        assertTrue(events("begin_checkout").isEmpty())
    }

    @Test
    fun `a launch that throws shows a message and enables the buttons`() =
        runTest(dispatcherExtension.testDispatcher) {
            val billing = FakeBillingRepository(launchFailure = IllegalStateException("boom"))
            val viewModel = createViewModel(billingRepository = billing)
            advance()

            viewModel.donateLow()

            assertFalse(viewModel.state.value.isBillingInProgress)
            assertEquals(GenericErrorText, viewModel.onlyMessage().text)
            assertTrue(viewModel.readyOptions().isNotEmpty())
            val result = events("donation_result").single()
            assertEquals(AnalyticsValue.Str("failed"), result.params["outcome"])
            assertTrue(events("vm_op_error").any { it.params["action"] == AnalyticsValue.Str("donateClicked") })
        }

    @Test
    fun `starting a donation reports begin_checkout with its price`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            viewModel.donateLow()

            val checkout = events("begin_checkout").single()
            assertEquals(AnalyticsValue.Str(DonationProductIds.LOW_DONATION), checkout.params["product_id"])
            assertEquals(AnalyticsValue.DoubleVal(0.99), checkout.params["value"])
            assertEquals(AnalyticsValue.Str("EUR"), checkout.params["currency"])
            assertTrue(events("purchase").isEmpty())
        }

    @Test
    fun `a started donation reports how it ended, once`() = runTest(dispatcherExtension.testDispatcher) {
        val billing = FakeBillingRepository()
        val viewModel = createViewModel(billingRepository = billing)
        advance()
        viewModel.donateLow()

        billing.purchaseResults.emit(PurchaseResult.UserCancelled)
        billing.purchaseResults.emit(PurchaseResult.Success)

        val result = events("donation_result").single()
        assertEquals(AnalyticsValue.Str("cancelled"), result.params["outcome"])
        assertEquals(AnalyticsValue.Str(DonationProductIds.LOW_DONATION), result.params["product_id"])
    }

    @Test
    fun `a purchase recovered in the background is not a donation outcome`() =
        runTest(dispatcherExtension.testDispatcher) {
            val billing = FakeBillingRepository()
            createViewModel(billingRepository = billing)
            advance()

            billing.purchaseResults.emit(PurchaseResult.Success)

            assertTrue(events("donation_result").isEmpty())
        }

    @Test
    fun `a shown message leaves the queue`() = runTest(dispatcherExtension.testDispatcher) {
        val billing = FakeBillingRepository()
        val viewModel = createViewModel(billingRepository = billing)
        advance()
        billing.purchaseResults.emit(PurchaseResult.Success)

        viewModel.messageShown(viewModel.onlyMessage().id)

        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `the page reports its loads under the support screen's names`() =
        runTest(dispatcherExtension.testDispatcher) {
            createViewModel()
            advance()

            val starts = events("vm_op_start")
            assertEquals(
                setOf("observeProductDetails", "observePurchaseResult", "queryProductDetails"),
                starts.map { (it.params["action"] as AnalyticsValue.Str).value }.toSet(),
            )
            assertTrue(starts.all { it.params["screen"] == AnalyticsValue.Str("Support") })
            assertTrue(starts.all { it.params["view_model"] == AnalyticsValue.Str("SupportViewModel") })
            assertNull(starts.firstOrNull { it.params["action"] == AnalyticsValue.Str("donateClicked") })
        }

    private fun readyActivity(): Activity = mockk(relaxed = true) {
        every { isFinishing } returns false
        every { isDestroyed } returns false
    }

    /**
     * Play's details for a one-time product priced at [formattedPrice]. `ProductDetails` has no
     * public constructor, so it is a mock.
     */
    private fun donationProduct(productId: String, formattedPrice: String = "€0.99"): ProductDetails {
        val offer: ProductDetails.OneTimePurchaseOfferDetails = mockk(relaxed = true) {
            every { this@mockk.formattedPrice } returns formattedPrice
            every { priceCurrencyCode } returns "EUR"
            every { priceAmountMicros } returns 990_000L
        }
        return mockk(relaxed = true) {
            every { this@mockk.productId } returns productId
            every { oneTimePurchaseOfferDetails } returns offer
            every { oneTimePurchaseOfferDetailsList } returns listOf(offer)
        }
    }

    private fun productWithoutOffer(productId: String): ProductDetails = mockk(relaxed = true) {
        every { this@mockk.productId } returns productId
        every { oneTimePurchaseOfferDetails } returns null
        every { oneTimePurchaseOfferDetailsList } returns emptyList()
    }

    /**
     * Publishes [products] for each query, like the real repository, or nothing when it is null.
     * [queryFailure] makes the query throw, [queryGate] holds it until completed, and
     * [launchFailure] makes a purchase launch throw.
     */
    private inner class FakeBillingRepository(
        val lowProduct: ProductDetails = donationProduct(DonationProductIds.LOW_DONATION),
        private val products: Map<String, ProductDetails>? = mapOf(DonationProductIds.LOW_DONATION to lowProduct),
        var queryFailure: Throwable? = null,
        var queryGate: CompletableDeferred<Unit>? = null,
        private val launchFailure: Throwable? = null,
    ) : BillingRepository {
        val productDetailsFlow = MutableSharedFlow<Map<String, ProductDetails>>(replay = 1)
        val purchaseResults = MutableSharedFlow<PurchaseResult>()
        val launches: MutableList<Pair<Activity, ProductDetails>> = mutableListOf()
        var queries: Int = 0

        override val productDetails: Flow<Map<String, ProductDetails>> = productDetailsFlow
        override val purchaseResult: Flow<PurchaseResult> = purchaseResults

        override suspend fun queryProductDetails(productIds: List<String>) {
            queries += 1
            queryGate?.await()
            queryFailure?.let { throw it }
            products?.let { productDetailsFlow.emit(it) }
        }

        override suspend fun processPastPurchases() = Unit

        override fun launchInAppDonationFlow(activity: Activity, details: ProductDetails) {
            launchFailure?.let { throw it }
            launches += activity to details
        }

        override fun launchSubscriptionFlow(activity: Activity, details: ProductDetails, offerToken: String?) = Unit

        override fun close() = Unit
    }
}
