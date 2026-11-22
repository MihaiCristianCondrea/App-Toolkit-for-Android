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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repositories

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsResult
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.models.PurchaseResult
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultBillingRepositoryTest {

    @Test
    fun `queryProductDetails with an empty list emits an empty map without querying Play`() = runTest {
        val harness = createHarness()
        try {
            harness.repository.queryProductDetails(productIds = emptyList())

            assertEquals(emptyMap<String, ProductDetails>(), harness.repository.productDetails.first())
            verify(exactly = 0) { harness.client.queryProductDetailsAsync(any(), any()) }
        } finally {
            harness.repository.close()
        }
    }

    @Test
    fun `queryProductDetails publishes products keyed by id after a transient retry`() = runTest {
        val harness = createHarness()
        val product: ProductDetails = mockk(relaxed = true) {
            every { productId } returns "donation_low"
        }
        val queryResult: QueryProductDetailsResult = mockk {
            every { productDetailsList } returns listOf(product)
        }
        var attempts = 0
        every { harness.client.queryProductDetailsAsync(any(), any()) } answers {
            val listener = secondArg<ProductDetailsResponseListener>()
            if (attempts++ == 0) {
                listener.onProductDetailsResponse(
                    billingResult(code = BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE),
                    mockk(relaxed = true),
                )
            } else {
                listener.onProductDetailsResponse(
                    billingResult(code = BillingClient.BillingResponseCode.OK),
                    queryResult,
                )
            }
        }
        try {
            harness.repository.queryProductDetails(productIds = listOf("donation_low"))

            assertEquals(mapOf("donation_low" to product), harness.repository.productDetails.first())
            assertEquals(2, attempts)
        } finally {
            harness.repository.close()
        }
    }

    @Test
    fun `queryProductDetails emits failure without retrying non retryable codes`() = runTest {
        val harness = createHarness()
        var attempts = 0
        every { harness.client.queryProductDetailsAsync(any(), any()) } answers {
            attempts++
            secondArg<ProductDetailsResponseListener>().onProductDetailsResponse(
                billingResult(
                    code = BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
                    debugMessage = "Play Store disabled",
                ),
                mockk(relaxed = true),
            )
        }
        val results = mutableListOf<PurchaseResult>()
        val collector = launch { harness.repository.purchaseResult.collect(results::add) }
        advanceUntilIdle()
        try {
            harness.repository.queryProductDetails(productIds = listOf("donation_low"))
            advanceUntilIdle()

            assertEquals(1, attempts)
            val failure = assertIs<PurchaseResult.Failed>(results.single())
            assertEquals("Play Store disabled", failure.error)
        } finally {
            collector.cancel()
            harness.repository.close()
        }
    }

    @Test
    fun `one time donation purchase is consumed once even when reported twice`() = runTest {
        val harness = createHarness()
        val purchase: Purchase = mockPurchase(
            token = "token-inapp-1",
            products = listOf("donation_low"),
        )
        every { harness.client.consumeAsync(any(), any()) } answers {
            secondArg<ConsumeResponseListener>().onConsumeResponse(
                billingResult(code = BillingClient.BillingResponseCode.OK),
                "token-inapp-1",
            )
        }
        val results = mutableListOf<PurchaseResult>()
        val collector = launch { harness.repository.purchaseResult.collect(results::add) }
        advanceUntilIdle()
        try {
            harness.listener.onPurchasesUpdated(
                billingResult(code = BillingClient.BillingResponseCode.OK),
                mutableListOf(purchase),
            )
            harness.listener.onPurchasesUpdated(
                billingResult(code = BillingClient.BillingResponseCode.OK),
                mutableListOf(purchase),
            )
            advanceUntilIdle()

            verify(exactly = 1) { harness.client.consumeAsync(any(), any()) }
            verify(exactly = 0) { harness.client.acknowledgePurchase(any(), any()) }
            assertEquals(listOf(PurchaseResult.Success), results)
        } finally {
            collector.cancel()
            harness.repository.close()
        }
    }

    @Test
    fun `subscription purchase launched through repository is acknowledged instead of consumed`() =
        runTest {
            val harness = createHarness(isReady = true)
            val subDetails: ProductDetails = mockSubProduct()
            every { harness.client.launchBillingFlow(any(), any()) } returns
                billingResult(code = BillingClient.BillingResponseCode.OK)
            every { harness.client.acknowledgePurchase(any(), any()) } answers {
                secondArg<AcknowledgePurchaseResponseListener>().onAcknowledgePurchaseResponse(
                    billingResult(code = BillingClient.BillingResponseCode.OK),
                )
            }
            val results = mutableListOf<PurchaseResult>()
            val collector = launch { harness.repository.purchaseResult.collect(results::add) }
            advanceUntilIdle()
            try {
                harness.repository.launchSubscriptionFlow(
                    activity = validActivity(),
                    details = subDetails,
                )
                val purchase: Purchase = mockPurchase(
                    token = "token-sub-1",
                    products = listOf("sub_monthly"),
                )
                harness.listener.onPurchasesUpdated(
                    billingResult(code = BillingClient.BillingResponseCode.OK),
                    mutableListOf(purchase),
                )
                advanceUntilIdle()

                verify(exactly = 1) { harness.client.acknowledgePurchase(any(), any()) }
                verify(exactly = 0) { harness.client.consumeAsync(any(), any()) }
                assertEquals(listOf(PurchaseResult.Success), results)
            } finally {
                collector.cancel()
                harness.repository.close()
            }
        }

    @Test
    fun `launchInAppDonationFlow rejects an invalid activity without calling Play`() = runTest {
        val harness = createHarness(isReady = true)
        val finishingActivity: Activity = validActivity(isFinishing = true)
        val details: ProductDetails = mockInAppProduct()
        val results = mutableListOf<PurchaseResult>()
        val collector = launch { harness.repository.purchaseResult.collect(results::add) }
        advanceUntilIdle()
        try {
            harness.repository.launchInAppDonationFlow(activity = finishingActivity, details = details)
            advanceUntilIdle()

            verify(exactly = 0) { harness.client.launchBillingFlow(any(), any()) }
            assertIs<PurchaseResult.Failed>(results.single())
            assertTrue(
                harness.diagnostics.breadcrumbs.any { (message, _) ->
                    message == "Billing flow skipped: invalid activity"
                },
            )
        } finally {
            collector.cancel()
            harness.repository.close()
        }
    }

    @Test
    fun `launchInAppDonationFlow resolves offer token from oneTimePurchaseOfferDetailsList`() =
        runTest {
            val harness = createHarness(isReady = true)
            val offer: ProductDetails.OneTimePurchaseOfferDetails = mockk(relaxed = true) {
                every { offerToken } returns "one-time-list-token"
            }
            val details: ProductDetails = mockk(relaxed = true) {
                every { productId } returns "donation_list"
                every { oneTimePurchaseOfferDetails } returns null
                every { oneTimePurchaseOfferDetailsList } returns listOf(offer)
            }
            every { harness.client.launchBillingFlow(any(), any()) } returns
                billingResult(code = BillingClient.BillingResponseCode.OK)
            try {
                harness.repository.launchInAppDonationFlow(activity = validActivity(), details = details)

                verify(exactly = 1) { harness.client.launchBillingFlow(any(), any()) }
                val launchCrumb = harness.diagnostics.breadcrumbs.single { (message, _) ->
                    message == "Billing flow launch"
                }
                assertEquals("true", launchCrumb.second["offerTokenProvided"])
                assertEquals("one-time-list-token".length.toString(), launchCrumb.second["offerTokenLength"])
            } finally {
                harness.repository.close()
            }
        }

    @Test
    fun `launchSubscriptionFlow rejects a missing offer token before calling Play`() = runTest {
        val harness = createHarness(isReady = true)
        val details: ProductDetails = mockk(relaxed = true) {
            every { productId } returns "sub_no_offer"
            every { subscriptionOfferDetails } returns emptyList()
        }
        val results = mutableListOf<PurchaseResult>()
        val collector = launch { harness.repository.purchaseResult.collect(results::add) }
        advanceUntilIdle()
        try {
            harness.repository.launchSubscriptionFlow(activity = validActivity(), details = details)
            advanceUntilIdle()

            verify(exactly = 0) { harness.client.launchBillingFlow(any(), any()) }
            assertIs<PurchaseResult.Failed>(results.single())
        } finally {
            collector.cancel()
            harness.repository.close()
        }
    }

    @Test
    fun `launchInAppDonationFlow skips launch when activity becomes invalid during reconnect`() =
        runTest {
            val harness = createHarness(isReady = false)
            var isFinishingFlag = false
            val activity: Activity = mockk(relaxed = true) {
                every { isFinishing } answers { isFinishingFlag }
                every { isDestroyed } returns false
                every { isChangingConfigurations } returns false
            }
            val stateListenerSlot = slot<BillingClientStateListener>()
            every { harness.client.startConnection(capture(stateListenerSlot)) } returns Unit
            val results = mutableListOf<PurchaseResult>()
            val collector = launch { harness.repository.purchaseResult.collect(results::add) }
            advanceUntilIdle()
            try {
                harness.repository.launchInAppDonationFlow(
                    activity = activity,
                    details = mockInAppProduct(),
                )
                isFinishingFlag = true
                every { harness.client.isReady } returns true
                stateListenerSlot.captured.onBillingSetupFinished(
                    billingResult(code = BillingClient.BillingResponseCode.OK),
                )
                advanceUntilIdle()

                verify(exactly = 0) { harness.client.launchBillingFlow(any(), any()) }
                assertIs<PurchaseResult.Failed>(results.single())
            } finally {
                collector.cancel()
                harness.repository.close()
            }
        }

    @Test
    fun `retryable purchase update error emits failure when no unsettled purchase is found`() =
        runTest {
            val harness = createHarness()
            every { harness.client.queryPurchasesAsync(any(), any()) } answers {
                secondArg<PurchasesResponseListener>().onQueryPurchasesResponse(
                    billingResult(code = BillingClient.BillingResponseCode.OK),
                    emptyList(),
                )
            }
            val results = mutableListOf<PurchaseResult>()
            val collector = launch { harness.repository.purchaseResult.collect(results::add) }
            advanceUntilIdle()
            try {
                harness.listener.onPurchasesUpdated(
                    billingResult(
                        code = BillingClient.BillingResponseCode.NETWORK_ERROR,
                        debugMessage = "Offline",
                    ),
                    null,
                )
                advanceUntilIdle()

                val failure = assertIs<PurchaseResult.Failed>(results.single())
                assertEquals("Offline", failure.error)
            } finally {
                collector.cancel()
                harness.repository.close()
            }
        }

    @Test
    fun `processPastPurchases ignores FEATURE_NOT_SUPPORTED for subscriptions`() = runTest {
        val harness = createHarness()
        var call = 0
        every { harness.client.queryPurchasesAsync(any(), any()) } answers {
            val listener = secondArg<PurchasesResponseListener>()
            if (call++ == 0) {
                listener.onQueryPurchasesResponse(
                    billingResult(code = BillingClient.BillingResponseCode.OK),
                    emptyList(),
                )
            } else {
                listener.onQueryPurchasesResponse(
                    billingResult(code = BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED),
                    emptyList(),
                )
            }
        }
        val results = mutableListOf<PurchaseResult>()
        val collector = launch { harness.repository.purchaseResult.collect(results::add) }
        advanceUntilIdle()
        try {
            harness.repository.processPastPurchases()
            advanceUntilIdle()

            assertTrue(results.isEmpty())
        } finally {
            collector.cancel()
            harness.repository.close()
        }
    }

    @Test
    fun `close ends BillingClient connection`() = runTest {
        val harness = createHarness()

        harness.repository.close()

        verify(exactly = 1) { harness.client.endConnection() }
    }

    private fun TestScope.createHarness(isReady: Boolean = true): Harness {
        val dispatcher = StandardTestDispatcher(scheduler = testScheduler)
        val dispatchers = object : DispatcherProvider {
            override val io: CoroutineDispatcher = dispatcher
            override val default: CoroutineDispatcher = dispatcher
            override val main: CoroutineDispatcher = dispatcher
            override val unconfined: CoroutineDispatcher = dispatcher
        }
        val diagnostics = RecordingDiagnosticsReporter()
        val client: BillingClient = mockk(relaxed = true) {
            every { this@mockk.isReady } returns isReady
            justRun { startConnection(any()) }
            justRun { endConnection() }
        }
        val context: Context = mockk(relaxed = true) {
            every { applicationContext } returns this
        }
        lateinit var capturedListener: PurchasesUpdatedListener
        val repository = DefaultBillingRepository(
            context = context,
            dispatchers = dispatchers,
            diagnostics = diagnostics,
            externalScope = CoroutineScope(context = SupervisorJob() + dispatcher),
            billingClientFactory = { _, listener ->
                capturedListener = listener
                client
            },
        )
        return Harness(
            repository = repository,
            client = client,
            listener = capturedListener,
            diagnostics = diagnostics,
        )
    }

    private fun validActivity(
        isFinishing: Boolean = false,
        isDestroyed: Boolean = false,
        isChangingConfigurations: Boolean = false,
    ): Activity = mockk(relaxed = true) {
        every { this@mockk.isFinishing } returns isFinishing
        every { this@mockk.isDestroyed } returns isDestroyed
        every { this@mockk.isChangingConfigurations } returns isChangingConfigurations
    }

    private fun mockInAppProduct(): ProductDetails = mockk(relaxed = true) {
        every { productId } returns "donation_low"
        every { oneTimePurchaseOfferDetails } returns null
        every { oneTimePurchaseOfferDetailsList } returns emptyList()
    }

    private fun mockSubProduct(): ProductDetails {
        val offer: ProductDetails.SubscriptionOfferDetails = mockk(relaxed = true) {
            every { offerToken } returns "sub-offer-token"
        }
        return mockk(relaxed = true) {
            every { productId } returns "sub_monthly"
            every { subscriptionOfferDetails } returns listOf(offer)
        }
    }

    private fun mockPurchase(
        token: String,
        products: List<String>,
        state: Int = Purchase.PurchaseState.PURCHASED,
        acknowledged: Boolean = false,
    ): Purchase = mockk(relaxed = true) {
        every { purchaseToken } returns token
        every { this@mockk.products } returns products
        every { purchaseState } returns state
        every { isAcknowledged } returns acknowledged
    }

    private fun billingResult(code: Int, debugMessage: String = ""): BillingResult =
        BillingResult.newBuilder()
            .setResponseCode(code)
            .setDebugMessage(debugMessage)
            .build()

    private class Harness(
        val repository: DefaultBillingRepository,
        val client: BillingClient,
        val listener: PurchasesUpdatedListener,
        val diagnostics: RecordingDiagnosticsReporter,
    )

    private class RecordingDiagnosticsReporter : DiagnosticsReporter {
        val breadcrumbs: MutableList<Pair<String, Map<String, String>>> = mutableListOf()
        val nonFatals: MutableList<Throwable> = mutableListOf()

        override fun logBreadcrumb(message: String, attributes: Map<String, String>) {
            breadcrumbs += message to attributes
        }

        override fun reportViewModelError(
            viewModelName: String,
            action: String,
            throwable: Throwable,
            extraKeys: Map<String, String>,
        ) = Unit

        override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {
            nonFatals += throwable
        }
    }
}
