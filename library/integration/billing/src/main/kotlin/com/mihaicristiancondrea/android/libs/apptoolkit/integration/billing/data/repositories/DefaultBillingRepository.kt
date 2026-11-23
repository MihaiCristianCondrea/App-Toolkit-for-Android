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
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.models.PurchaseEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.milliseconds

/**
 * Shares one Play Billing client per process and settles outstanding purchases on connection.
 *
 * Use [getInstance] to share the client and its purchase listener.
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

    private val _purchaseResult = MutableSharedFlow<PurchaseEvent>(
        extraBufferCapacity = BillingRetryConstants.PURCHASE_RESULT_BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val purchaseEvents: Flow<PurchaseEvent> = _purchaseResult.asSharedFlow()
    override val purchaseResult: Flow<PurchaseResult> = purchaseEvents.map { it.result }

    @Volatile
    private var launchedProductId: String? = null

    /** Keeps a token claimed during and after settlement so callbacks and recovery cannot settle it twice. */
    private val settlingTokens: MutableSet<String> = ConcurrentHashMap.newKeySet()

    /** Remembers the type at launch because Play's purchase callback omits it. */
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

        val productId = launchedProductId
        val productIds = listOfNotNull(productId)
        scope.launch {
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.USER_CANCELED ->
                    emitPurchaseResult(result = PurchaseResult.UserCancelled, productIds = productIds)

                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
                BillingClient.BillingResponseCode.ITEM_NOT_OWNED,
                -> {
                    if (!processPastPurchasesAndReportHandled(productId)) {
                        emitPurchaseResult(result = billingResult.toFailureResult(), productIds = productIds)
                    }
                }

                else -> {
                    if (billingResult.isRetryable()) {
                        retryPurchaseUpdate(billingResult = billingResult, purchases = purchases, productId = productId)
                    } else {
                        emitPurchaseResult(result = billingResult.toFailureResult(), productIds = productIds)
                    }
                }
            }
        }
    }

    /**
     * Reports pending purchases and settles completed ones. A typed query resolves purchases whose
     * type is unknown.
     *
     * @param productType Type supplied by the purchase query, if available.
     * @param expectedProductId Limits the result to a specific product when supplied.
     * @return Whether a matching purchase was pending or needed settlement.
     */
    private fun handlePurchases(purchases: List<Purchase>, productType: String? = null, expectedProductId: String? = null): Boolean {
        var handledAny = false
        var hasUnknownType = false
        purchases.forEach { purchase ->
            when (purchase.purchaseState) {
                Purchase.PurchaseState.PURCHASED -> {
                    if (!purchase.isAcknowledged) {
                        handledAny = handledAny || expectedProductId == null || expectedProductId in purchase.products
                        when (productType ?: purchase.launchedProductType()) {
                            BillingClient.ProductType.SUBS -> acknowledgePurchase(purchase = purchase)
                            BillingClient.ProductType.INAPP -> consumePurchase(purchase = purchase)
                            else -> hasUnknownType = true
                        }
                    }
                }

                Purchase.PurchaseState.PENDING -> {
                    handledAny = handledAny || expectedProductId == null || expectedProductId in purchase.products
                    emitPurchaseResult(result = PurchaseResult.Pending, productIds = purchase.products)
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

    /** Claims the purchase token before settlement and releases the claim on failure for later recovery. */
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
                    emitPurchaseResult(result = PurchaseResult.Success, productIds = purchase.products)

                else -> emitPurchaseResult(result = billingResult.toFailureResult(), productIds = purchase.products)
            }
        }
    }

    override suspend fun processPastPurchases() {
        processPastPurchasesAndReportHandled()
    }

    private suspend fun processPastPurchasesAndReportHandled(expectedProductId: String? = null): Boolean {
        val handledInApp: Boolean = processPastPurchases(productType = BillingClient.ProductType.INAPP, expectedProductId = expectedProductId)
        val handledSubs: Boolean = processPastPurchases(productType = BillingClient.ProductType.SUBS, expectedProductId = expectedProductId)
        return handledInApp || handledSubs
    }

    private suspend fun processPastPurchases(productType: String, expectedProductId: String?): Boolean {
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
                    handlePurchases(purchases = purchases, productType = productType, expectedProductId = expectedProductId)
                } ?: false

            // Play can omit subscription support while still returning one-time purchases.
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
            emitPurchaseResult(result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE), productIds = listOf(details.productId))
            return
        }

        if ((productType == BillingClient.ProductType.SUBS) && offerToken.isNullOrBlank()) {
            diagnostics.logBreadcrumb(
                message = "Billing flow skipped: missing subscription offer token",
                attributes = mapOf("productId" to details.productId),
            )
            emitPurchaseResult(result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE), productIds = listOf(details.productId))
            return
        }

        if (!billingClient.isReady) {
            if (!allowReconnect) {
                emitPurchaseResult(result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE), productIds = listOf(details.productId))
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
                                        productIds = listOf(details.productId),
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
                            emitPurchaseResult(result = billingResult.toFailureResult(), productIds = listOf(details.productId))
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                    }
                },
            )
            return
        }

        launchedProductId = details.productId
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
                if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    scope.launch {
                        if (!processPastPurchasesAndReportHandled(details.productId)) {
                            emitPurchaseResult(result, listOf(details.productId))
                        }
                    }
                } else {
                    emitPurchaseResult(result, listOf(details.productId))
                }
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
            emitPurchaseResult(result = PurchaseResult.Failed(reason = PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE), productIds = listOf(details.productId))
        }
    }

    override fun close() {
        scope.cancel()
        billingClient.endConnection()
        clearInstance(closed = this)
    }

    private fun emitPurchaseResult(result: PurchaseResult, productIds: List<String> = emptyList()) {
        _purchaseResult.tryEmit(value = PurchaseEvent(result, productIds))
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
        productId: String?,
    ) {
        delay(duration = BillingRetryConstants.RETRY_DELAY_SIMPLE_MS.milliseconds)
        if (purchases.isNullOrEmpty()) {
            val handledAny: Boolean = processPastPurchasesAndReportHandled(productId)
            if (!handledAny) {
                emitPurchaseResult(result = billingResult.toFailureResult(), productIds = listOfNotNull(productId))
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

    /** Maps Play's response to a user-facing reason and preserves its debug message for diagnostics. */
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

    /** Returns the default offer token for the product type when one is available. */
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
