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
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.constants.BillingRetryConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.extensions.isValidForBilling
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.models.PurchaseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.milliseconds

/**
 * Process-scoped Play Billing implementation.
 *
 * The repository owns one [BillingClient], reconnects with bounded retries, replays the latest
 * product query, and emits purchase outcomes as one-off events. One-time donations are consumed and
 * subscriptions are acknowledged. Unsettled purchases of both types are recovered on initial
 * connection and later reconnects.
 *
 * Use [getInstance] rather than constructing a second callback/listener graph.
 */
class DefaultBillingRepository internal constructor(
    context: Context,
    private val dispatchers: DispatcherProvider,
    private val diagnostics: DiagnosticsReporter,
    externalScope: CoroutineScope,
    billingClientFactory: (Context, PurchasesUpdatedListener) -> BillingClient = ::defaultBillingClient,
) : PurchasesUpdatedListener, BillingRepository {

    private val scope = CoroutineScope(
        context = externalScope.coroutineContext + SupervisorJob(parent = externalScope.coroutineContext[Job]),
    )

    private val connectionMutex = Mutex()

    private val _productDetails = MutableSharedFlow<Map<String, ProductDetails>>(replay = 1)
    override val productDetails: Flow<Map<String, ProductDetails>> =
        _productDetails.asSharedFlow()

    private val _purchaseResult = MutableSharedFlow<PurchaseResult>(
        extraBufferCapacity = BillingRetryConstants.PURCHASE_RESULT_BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val purchaseResult: Flow<PurchaseResult> =
        _purchaseResult.asSharedFlow()

    /**
     * Purchase tokens whose consumption or acknowledgement is in flight or has succeeded.
     *
     * One unsettled purchase can reach [handlePurchases] from several paths at once: the purchase
     * callback, and [processPastPurchases] on connection and whenever the app resumes, which
     * returning from Play's purchase screen can trigger. Consuming the same token twice fails the
     * second call with `ITEM_NOT_OWNED`, which would reach the UI as a failed purchase right after
     * the successful one. A token leaves the set only when settling it fails, so a later pass can
     * retry it.
     */
    private val settlingTokens: MutableSet<String> = ConcurrentHashMap.newKeySet()

    /**
     * Product type of every product launched through [launchBillingFlow], keyed by product id.
     *
     * [Purchase] does not carry its product type, yet a purchase reported through
     * [onPurchasesUpdated] must be settled the way its type requires: consuming a subscription
     * token fails, and Play refunds a subscription that is not acknowledged within three days.
     */
    private val launchedProductTypes: MutableMap<String, String> = ConcurrentHashMap()

    private val billingClient: BillingClient = billingClientFactory(context, this)

    companion object {
        @Volatile
        private var INSTANCE: DefaultBillingRepository? = null

        /** Returns the singleton backed by the application [context]. */
        fun getInstance(
            context: Context,
            dispatchers: DispatcherProvider,
            diagnostics: DiagnosticsReporter,
            externalScope: CoroutineScope,
        ): DefaultBillingRepository {
            return INSTANCE ?: synchronized(lock = this) {
                INSTANCE ?: DefaultBillingRepository(
                    context = context.applicationContext,
                    dispatchers = dispatchers,
                    diagnostics = diagnostics,
                    externalScope = externalScope,
                ).also { INSTANCE = it }
            }
        }

        private fun clearInstance(closed: DefaultBillingRepository) {
            synchronized(lock = this) {
                if (INSTANCE === closed) INSTANCE = null
            }
        }

        private fun defaultBillingClient(
            context: Context,
            listener: PurchasesUpdatedListener,
        ): BillingClient = BillingClient.newBuilder(context)
            .setListener(listener)
            .enablePendingPurchases(
                PendingPurchasesParams
                    .newBuilder()
                    .enableOneTimeProducts()
                    .build(),
            )
            .enableAutoServiceReconnection()
            .build()
    }

    init {
        billingClient.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    when (billingResult.responseCode) {
                        BillingClient.BillingResponseCode.OK -> scope.launch { processPastPurchases() }
                        else -> if (billingResult.isRetryable()) {
                            scope.launch { retryBillingConnection() }
                        }
                    }
                }

                override fun onBillingServiceDisconnected() {
                    scope.launch { retryBillingConnection() }
                }
            },
        )
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?,
    ) {
        if ((billingResult.responseCode == BillingClient.BillingResponseCode.OK) && (purchases != null)) {
            handlePurchases(purchases = purchases)
            return
        }

        scope.launch {
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.USER_CANCELED ->
                    emitPurchaseResult(result = PurchaseResult.UserCancelled)

                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
                BillingClient.BillingResponseCode.ITEM_NOT_OWNED,
                -> {
                    processPastPurchases()
                    emitPurchaseResult(result = billingResult.toFailureResult())
                }

                else -> {
                    if (billingResult.isRetryable()) {
                        retryPurchaseUpdate(billingResult = billingResult, purchases = purchases)
                    } else {
                        emitPurchaseResult(result = billingResult.toFailureResult())
                    }
                }
            }
        }
    }

    /**
     * Settles each completed purchase and reports pending ones.
     *
     * @param productType The type of every purchase in [purchases], when the caller knows it from
     * its query. When null the type is looked up in [launchedProductTypes]. A product never launched
     * in this process, such as a pending purchase completing after a restart or one bought in the
     * Play Store, is settled by [processPastPurchases], whose queries carry each purchase's type,
     * rather than guessed: consuming a subscription fails, and an unacknowledged one is refunded.
     * @return `true` when at least one purchase was queued for settlement or reported pending.
     */
    private fun handlePurchases(purchases: List<Purchase>, productType: String? = null): Boolean {
        var handledAny = false
        var hasUnknownType = false
        purchases.forEach { purchase ->
            when (purchase.purchaseState) {
                Purchase.PurchaseState.PURCHASED -> {
                    if (!purchase.isAcknowledged) {
                        handledAny = true
                        when (productType ?: purchase.launchedProductType()) {
                            BillingClient.ProductType.SUBS -> acknowledgePurchase(purchase = purchase)
                            BillingClient.ProductType.INAPP -> consumePurchase(purchase = purchase)
                            else -> hasUnknownType = true
                        }
                    }
                }

                Purchase.PurchaseState.PENDING -> {
                    handledAny = true
                    emitPurchaseResult(result = PurchaseResult.Pending)
                }

                else -> Unit
            }
        }
        if (hasUnknownType) {
            scope.launch { processPastPurchases() }
        }
        return handledAny
    }

    private fun Purchase.launchedProductType(): String? =
        products.firstNotNullOfOrNull { productId -> launchedProductTypes[productId] }

    private fun consumePurchase(purchase: Purchase) {
        val params: ConsumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        settlePurchase(purchase = purchase) { complete ->
            billingClient.consumeAsync(params) { billingResult: BillingResult, _ ->
                complete(BillingCallResult(billingResult = billingResult, data = Unit))
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val params: AcknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        settlePurchase(purchase = purchase) { complete ->
            billingClient.acknowledgePurchase(params) { billingResult: BillingResult ->
                complete(BillingCallResult(billingResult = billingResult, data = Unit))
            }
        }
    }

    /**
     * Runs [settle], a consume or acknowledge call, at most once per purchase token at a time, with
     * retries, and reports the outcome through [purchaseResult].
     */
    private fun settlePurchase(
        purchase: Purchase,
        settle: (complete: (BillingCallResult<Unit>) -> Unit) -> Unit,
    ) {
        val purchaseToken: String = purchase.purchaseToken
        if (!settlingTokens.add(purchaseToken)) return

        scope.launch {
            val (billingResult) = try {
                retryBillingCall(strategy = RetryStrategy.Exponential()) {
                    awaitBillingCallback(register = settle)
                }
            } catch (throwable: Throwable) {
                settlingTokens.remove(purchaseToken)
                throw throwable
            }
            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                settlingTokens.remove(purchaseToken)
            }
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK ->
                    emitPurchaseResult(result = PurchaseResult.Success)

                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                    processPastPurchases()
                    emitPurchaseResult(result = billingResult.toFailureResult())
                }

                else -> emitPurchaseResult(result = billingResult.toFailureResult())
            }
        }
    }

    /** Play Billing answers through its own callbacks, so the queries need no dispatcher switch. */
    override suspend fun processPastPurchases() {
        processPastPurchasesAndReportHandled()
    }

    private suspend fun processPastPurchasesAndReportHandled(): Boolean {
        val handledInApp: Boolean = processPastPurchases(productType = BillingClient.ProductType.INAPP)
        val handledSubs: Boolean = processPastPurchases(productType = BillingClient.ProductType.SUBS)
        return handledInApp || handledSubs
    }

    private suspend fun processPastPurchases(productType: String): Boolean {
        val params: QueryPurchasesParams = QueryPurchasesParams.newBuilder()
            .setProductType(productType)
            .build()
        val (billingResult, purchasesList) =
            retryBillingCall(strategy = RetryStrategy.Exponential()) {
                awaitBillingCallback { complete ->
                    billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
                        complete(BillingCallResult(billingResult = billingResult, data = purchasesList))
                    }
                }
            }
        val responseCode: Int = billingResult.responseCode
        return when {
            responseCode == BillingClient.BillingResponseCode.OK ->
                purchasesList?.let { purchases ->
                    handlePurchases(purchases = purchases, productType = productType)
                } ?: false

            // A device without subscription support has no subscriptions to recover, so this is
            // not a failure to show the user.
            (productType == BillingClient.ProductType.SUBS) &&
                (responseCode == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED) -> false

            !billingResult.isRetryable() -> {
                diagnostics.logBreadcrumb(
                    message = "Past purchases query failed",
                    attributes = mapOf(
                        "productType" to productType,
                        "responseCode" to responseCode.toString(),
                    ),
                )
                emitPurchaseResult(result = billingResult.toFailureResult())
                false
            }

            else -> false
        }
    }

    /** Play Billing answers through its own callbacks, so the query needs no dispatcher switch. */
    override suspend fun queryProductDetails(productIds: List<String>) {
        if (productIds.isEmpty()) {
            _productDetails.emit(value = emptyMap())
            return
        }

        val products: List<QueryProductDetailsParams.Product> = productIds.map { productId ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params: QueryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(products)
            .build()
        val callResult = retryBillingCall(strategy = RetryStrategy.Simple()) {
            awaitBillingCallback { complete ->
                billingClient.queryProductDetailsAsync(params) { billingResult, result ->
                    complete(BillingCallResult(billingResult = billingResult, data = result))
                }
            }
        }

        if (callResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            val map: Map<String, ProductDetails> =
                callResult.data?.productDetailsList?.associateBy { it.productId }.orEmpty()
            _productDetails.emit(value = map)
        } else {
            emitPurchaseResult(result = callResult.billingResult.toFailureResult())
        }
    }

    override fun launchInAppDonationFlow(
        activity: Activity,
        details: ProductDetails,
    ) {
        launchBillingFlow(
            activity = activity,
            details = details,
            productType = BillingClient.ProductType.INAPP,
            offerToken = details.primaryOfferToken(productType = BillingClient.ProductType.INAPP),
        )
    }

    override fun launchSubscriptionFlow(
        activity: Activity,
        details: ProductDetails,
        offerToken: String?,
    ) {
        launchBillingFlow(
            activity = activity,
            details = details,
            productType = BillingClient.ProductType.SUBS,
            offerToken = offerToken?.takeIf { it.isNotBlank() }
                ?: details.primaryOfferToken(productType = BillingClient.ProductType.SUBS),
        )
    }

    private fun launchBillingFlow(
        activity: Activity,
        details: ProductDetails,
        productType: String,
        offerToken: String?,
        allowReconnect: Boolean = true,
    ) {
        if (!activity.isValidForBilling()) {
            diagnostics.logBreadcrumb(
                message = "Billing flow skipped: invalid activity",
                attributes = mapOf(
                    "productId" to details.productId,
                    "productType" to productType,
                    "isFinishing" to activity.isFinishing.toString(),
                    "isDestroyed" to activity.isDestroyed.toString(),
                    "isChangingConfigurations" to activity.isChangingConfigurations.toString(),
                ),
            )
            emitPurchaseResult(result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE))
            return
        }

        if ((productType == BillingClient.ProductType.SUBS) && offerToken.isNullOrBlank()) {
            diagnostics.logBreadcrumb(
                message = "Billing flow skipped: missing subscription offer token",
                attributes = mapOf("productId" to details.productId),
            )
            emitPurchaseResult(result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE))
            return
        }

        if (!billingClient.isReady) {
            if (!allowReconnect) {
                emitPurchaseResult(result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE))
                return
            }
            val activityRef = WeakReference(activity)
            val handled = AtomicBoolean(false)
            billingClient.startConnection(
                object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (!handled.compareAndSet(false, true)) return
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            scope.launch(context = dispatchers.main) {
                                val hostActivity: Activity? = activityRef.get()
                                if (hostActivity == null) {
                                    diagnostics.logBreadcrumb(
                                        message = "Billing flow skipped after reconnect: activity cleared",
                                        attributes = mapOf("productId" to details.productId),
                                    )
                                    emitPurchaseResult(
                                        result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE),
                                    )
                                    return@launch
                                }
                                launchBillingFlow(
                                    activity = hostActivity,
                                    details = details,
                                    productType = productType,
                                    offerToken = offerToken,
                                    allowReconnect = false,
                                )
                            }
                        } else {
                            emitPurchaseResult(result = billingResult.toFailureResult())
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        // Handled by auto reconnection and retryBillingConnection.
                    }
                },
            )
            return
        }

        launchedProductTypes[details.productId] = productType

        diagnostics.logBreadcrumb(
            message = "Billing flow launch",
            attributes = mapOf(
                "productId" to details.productId,
                "productType" to productType,
                "billingReady" to billingClient.isReady.toString(),
                "activity" to activity::class.java.simpleName,
                "isFinishing" to activity.isFinishing.toString(),
                "isDestroyed" to activity.isDestroyed.toString(),
                "offerTokenProvided" to (!offerToken.isNullOrBlank()).toString(),
                "offerTokenLength" to (offerToken?.length?.toString() ?: "0"),
            ),
        )

        runCatching {
            val paramsBuilder: BillingFlowParams.ProductDetailsParams.Builder =
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(details)
            if (!offerToken.isNullOrBlank()) {
                paramsBuilder.setOfferToken(offerToken)
            }

            val params: BillingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(paramsBuilder.build()))
                .build()

            billingClient.launchBillingFlow(activity, params)
        }.onSuccess { billingResult ->
            diagnostics.logBreadcrumb(
                message = "Billing flow result",
                attributes = mapOf(
                    "responseCode" to billingResult.responseCode.toString(),
                    "debugMessage" to billingResult.debugMessage,
                ),
            )

            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                val result: PurchaseResult = when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.USER_CANCELED -> PurchaseResult.UserCancelled
                    else -> billingResult.toFailureResult()
                }
                emitPurchaseResult(result = result)
            }
        }.onFailure { throwable ->
            diagnostics.recordNonFatal(
                throwable = throwable,
                attributes = mapOf(
                    "operation" to "launchBillingFlow",
                    "product_id" to details.productId,
                    "product_type" to productType,
                ),
            )
            emitPurchaseResult(result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE))
        }
    }

    override fun close() {
        scope.cancel()
        billingClient.endConnection()
        // A closed client cannot reconnect, so the next getInstance must build a new repository
        // rather than hand back this one.
        clearInstance(closed = this)
    }

    private fun emitPurchaseResult(result: PurchaseResult) {
        _purchaseResult.tryEmit(value = result)
    }

    private suspend fun retryBillingConnection(maxAttempts: Int = 3) {
        connectionMutex.withLock {
            var attempt = 0
            while (!billingClient.isReady && (attempt < maxAttempts)) {
                attempt++
                val billingResult: BillingResult? = awaitBillingCallback { complete ->
                    billingClient.startConnection(
                        object : BillingClientStateListener {
                            override fun onBillingSetupFinished(billingResult: BillingResult) {
                                complete(billingResult)
                            }

                            override fun onBillingServiceDisconnected() {
                                complete(null)
                            }
                        },
                    )
                }
                when {
                    billingResult == null -> Unit

                    billingResult.responseCode == BillingClient.BillingResponseCode.OK -> {
                        scope.launch { processPastPurchases() }
                        return
                    }

                    !billingResult.isRetryable() -> {
                        diagnostics.logBreadcrumb(
                            message = "Billing connection failed without retry",
                            attributes = mapOf("responseCode" to billingResult.responseCode.toString()),
                        )
                        emitPurchaseResult(result = billingResult.toFailureResult())
                        return
                    }
                }
                if (!billingClient.isReady && (attempt < maxAttempts)) {
                    delay(duration = BillingRetryConstants.RETRY_DELAY_SIMPLE_MS.milliseconds)
                }
            }
        }
    }

    private suspend fun retryPurchaseUpdate(
        billingResult: BillingResult,
        purchases: List<Purchase>?,
    ) {
        delay(duration = BillingRetryConstants.RETRY_DELAY_SIMPLE_MS.milliseconds)
        if (purchases.isNullOrEmpty()) {
            val handledAny: Boolean = processPastPurchasesAndReportHandled()
            if (!handledAny) {
                emitPurchaseResult(result = billingResult.toFailureResult())
            }
        } else {
            handlePurchases(purchases = purchases)
        }
    }

    private data class BillingCallResult<T>(val billingResult: BillingResult, val data: T?)

    private sealed interface RetryStrategy {
        val maxAttempts: Int

        data class Simple(
            override val maxAttempts: Int = 3,
            val delayMillis: Long = BillingRetryConstants.RETRY_DELAY_SIMPLE_MS,
        ) : RetryStrategy

        data class Exponential(
            override val maxAttempts: Int = 3,
            val initialDelayMillis: Long = BillingRetryConstants.RETRY_DELAY_EXPONENTIAL_MS,
            val factor: Double = 2.0,
            val maxDelayMillis: Long = BillingRetryConstants.RETRY_MAX_DELAY_MS,
        ) : RetryStrategy
    }

    private suspend fun <T> retryBillingCall(
        strategy: RetryStrategy,
        block: suspend () -> BillingCallResult<T>,
    ): BillingCallResult<T> {
        var attempt = 1
        var delayMillis: Long = when (strategy) {
            is RetryStrategy.Simple -> strategy.delayMillis
            is RetryStrategy.Exponential -> strategy.initialDelayMillis
        }

        while (true) {
            val result: BillingCallResult<T> = block()
            val shouldRetry: Boolean = result.billingResult.isRetryable()
            if (!shouldRetry || attempt >= strategy.maxAttempts) {
                return result
            }
            when (strategy) {
                is RetryStrategy.Simple -> delay(duration = strategy.delayMillis.milliseconds)
                is RetryStrategy.Exponential -> {
                    delay(duration = delayMillis.milliseconds)
                    delayMillis = (delayMillis * strategy.factor).toLong()
                        .coerceAtMost(maximumValue = strategy.maxDelayMillis)
                }
            }
            attempt++
        }
    }

    private fun BillingResult.isRetryable(): Boolean = responseCode in RETRYABLE_RESPONSE_CODES

    /** Groups Play's response code by what the person can do; Play's message is kept for diagnostics. */
    private fun BillingResult.toFailureResult(): PurchaseResult.Failed {
        val reason: PurchaseResult.Failed.Reason = when (responseCode) {
            BillingClient.BillingResponseCode.NETWORK_ERROR,
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
            -> PurchaseResult.Failed.Reason.NETWORK

            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
            BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
            -> PurchaseResult.Failed.Reason.BILLING_UNAVAILABLE

            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED,
            -> PurchaseResult.Failed.Reason.ITEM_UNAVAILABLE

            else -> PurchaseResult.Failed.Reason.UNKNOWN
        }
        return PurchaseResult.Failed(reason = reason, debugMessage = debugMessage)
    }

    /**
     * Resolves the default offer token for [productType]. Subscriptions always require an offer
     * token; Play Billing 8+ one-time purchase offers may also supply one via
     * [ProductDetails.oneTimePurchaseOfferDetails] or [ProductDetails.oneTimePurchaseOfferDetailsList].
     */
    private fun ProductDetails.primaryOfferToken(productType: String): String? =
        when (productType) {
            BillingClient.ProductType.SUBS ->
                subscriptionOfferDetails?.firstOrNull()?.offerToken?.takeIf { it.isNotBlank() }

            else ->
                oneTimePurchaseOfferDetails?.offerToken?.takeIf { it.isNotBlank() }
                    ?: oneTimePurchaseOfferDetailsList
                        ?.firstNotNullOfOrNull { offer -> offer.offerToken?.takeIf { it.isNotBlank() } }
        }
}

private val RETRYABLE_RESPONSE_CODES: Set<Int> = setOf(
    BillingClient.BillingResponseCode.NETWORK_ERROR,
    BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
    BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
    BillingClient.BillingResponseCode.ERROR,
)
