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

package com.d4rk.android.libs.apptoolkit.app.support.billing

import android.app.Activity
import android.content.Context
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
import com.d4rk.android.libs.apptoolkit.app.support.utils.extensions.primaryOfferToken
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

private const val RETRY_DELAY_SIMPLE_MS = 1_000L
private const val RETRY_DELAY_EXPONENTIAL_MS = 2_000L
private const val RETRY_MAX_DELAY_MS = 16_000L

class BillingRepository private constructor(
    context: Context,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
    externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatchers.io),
) : PurchasesUpdatedListener {

    private val scope =
        CoroutineScope(externalScope.coroutineContext + SupervisorJob() + dispatchers.io)

    private val _productDetails = MutableStateFlow<Map<String, ProductDetails>>(emptyMap())
    val productDetails: Flow<Map<String, ProductDetails>> =
        _productDetails.asStateFlow()

    private val _purchaseResult = MutableSharedFlow<PurchaseResult>()
    val purchaseResult: Flow<PurchaseResult> =
        _purchaseResult.asSharedFlow()

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams
                .newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .enableAutoServiceReconnection()
        .build()

    companion object {
        @Volatile
        private var INSTANCE: BillingRepository? = null

        fun getInstance(
            context: Context,
            dispatchers: DispatcherProvider,
            firebaseController: FirebaseController,
            externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatchers.io),
        ): BillingRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BillingRepository(
                    context.applicationContext,
                    dispatchers,
                    firebaseController,
                    externalScope
                )
                    .also { INSTANCE = it }
            }
        }
    }

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> scope.launch { processPastPurchases() }
                    else -> if (billingResult.shouldRetrySimple()) {
                        scope.launch { retryBillingConnection() }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                scope.launch { retryBillingConnection() }
            }
        })
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
            return
        }

        scope.launch {
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.USER_CANCELED -> _purchaseResult.emit(
                    PurchaseResult.UserCancelled
                )

                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    processPastPurchases()
                    _purchaseResult.emit(billingResult.toFailureResult())
                }

                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                    processPastPurchases()
                    _purchaseResult.emit(billingResult.toFailureResult())
                }

                else -> {
                    if (billingResult.shouldRetrySimple()) {
                        retryPurchaseUpdate(purchases)
                    } else {
                        _purchaseResult.emit(billingResult.toFailureResult())
                    }
                }
            }
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        purchases.forEach { purchase ->
            when (purchase.purchaseState) {
                Purchase.PurchaseState.PURCHASED -> {
                    if (!purchase.isAcknowledged) {
                        consumePurchase(purchase)
                    }
                }

                Purchase.PurchaseState.PENDING -> {
                    scope.launch { _purchaseResult.emit(PurchaseResult.Pending) }
                }

                else -> {}
            }
        }
    }

    private fun consumePurchase(purchase: Purchase) {
        scope.launch {
            val params = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            val result = retryBillingCall(RetryStrategy.Exponential()) {
                suspendCancellableCoroutine { continuation ->
                    billingClient.consumeAsync(params) { billingResult: BillingResult, _: String? ->
                        continuation.resume(BillingCallResult(billingResult, Unit))
                    }
                }
            }
            when (result.billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> _purchaseResult.emit(PurchaseResult.Success)
                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                    processPastPurchases()
                    _purchaseResult.emit(result.billingResult.toFailureResult())
                }

                else -> _purchaseResult.emit(result.billingResult.toFailureResult())
            }
        }
    }

    suspend fun processPastPurchases() {
        withContext(dispatchers.io) {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
            val result = retryBillingCall(RetryStrategy.Exponential()) {
                suspendCancellableCoroutine { continuation ->
                    billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
                        continuation.resume(BillingCallResult(billingResult, purchasesList))
                    }
                }
            }
            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                result.data?.let { handlePurchases(it) }
            } else if (!result.billingResult.shouldRetryExponential()) {
                scope.launch { _purchaseResult.emit(result.billingResult.toFailureResult()) }
            }
        }
    }

    suspend fun queryProductDetails(productIds: List<String>) {
        withContext(dispatchers.io) {
            val products = productIds.map {
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            }
            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(products)
                .build()
            val callResult = retryBillingCall(RetryStrategy.Simple()) {
                suspendCancellableCoroutine { continuation ->
                    billingClient.queryProductDetailsAsync(params) { billingResult, result ->
                        continuation.resume(BillingCallResult(billingResult, result))
                    }
                }
            }

            if (callResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val map =
                    callResult.data?.productDetailsList?.associateBy { it.productId }.orEmpty()
                scope.launch { _productDetails.emit(map) }
            } else {
                scope.launch { _purchaseResult.emit(callResult.billingResult.toFailureResult()) }
            }
        }
    }

    fun launchInAppDonationFlow(
        activity: Activity,
        details: ProductDetails
    ) {
        launchBillingFlow(
            activity = activity,
            details = details,
            productType = BillingClient.ProductType.INAPP,
            offerToken = details.primaryOfferToken(BillingClient.ProductType.INAPP),
        )
    }

    fun launchSubscriptionFlow(
        activity: Activity,
        details: ProductDetails,
        offerToken: String? = null,
    ) {
        launchBillingFlow(
            activity = activity,
            details = details,
            productType = BillingClient.ProductType.SUBS,
            offerToken = offerToken?.takeIf { it.isNotBlank() }
                ?: details.primaryOfferToken(BillingClient.ProductType.SUBS),
        )
    }

    private fun launchBillingFlow(
        activity: Activity,
        details: ProductDetails,
        productType: String,
        offerToken: String?,
    ) {
        if (!billingClient.isReady) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        launchBillingFlow(activity, details, productType, offerToken)
                    } else {
                        scope.launch { _purchaseResult.emit(billingResult.toFailureResult()) }
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // handled by auto reconnection
                }
            })
            return
        }

        firebaseController.logBreadcrumb(
            message = "Billing flow launch",
            attributes = mapOf(
                "productId" to details.productId,
                "productType" to productType,
                "billingReady" to billingClient.isReady.toString(),
                "activity" to activity::class.java.name,
                "isFinishing" to activity.isFinishing.toString(),
                "isDestroyed" to activity.isDestroyed.toString(),
                "offerTokenProvided" to (!offerToken.isNullOrBlank()).toString(),
                "offerTokenLength" to (offerToken?.length?.toString() ?: "0"),
            ),
        )

        runCatching {
            val paramsBuilder =
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(details)
            if (!offerToken.isNullOrBlank()) {
                paramsBuilder.setOfferToken(offerToken)
            }

            val params = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(paramsBuilder.build()))
                .build()

            billingClient.launchBillingFlow(activity, params)
        }.onSuccess { billingResult ->
            firebaseController.logBreadcrumb(
                message = "Billing flow result",
                attributes = mapOf(
                    "responseCode" to billingResult.responseCode.toString(),
                    "debugMessage" to billingResult.debugMessage,
                ),
            )

            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                val result = when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.USER_CANCELED -> PurchaseResult.UserCancelled
                    else -> billingResult.toFailureResult()
                }
                scope.launch { _purchaseResult.emit(result) }
            }
        }.onFailure { throwable ->
            firebaseController.reportViewModelError(
                viewModelName = "SupportViewModel",
                action = "launchBillingFlow",
                throwable = throwable,
                extraKeys = mapOf(
                    "product_id" to details.productId,
                    "product_type" to productType,
                ),
            )
            scope.launch {
                _purchaseResult.emit(
                    PurchaseResult.Failed("Unable to start the billing flow. Please try again.")
                )
            }
        }
    }

    fun close() {
        scope.cancel()
        billingClient.endConnection()
    }

    private suspend fun retryBillingConnection(maxAttempts: Int = 3) {
        var attempt = 0
        while (!billingClient.isReady && attempt < maxAttempts) {
            attempt++
            suspendCancellableCoroutine { continuation ->
                billingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        when (billingResult.responseCode) {
                            BillingClient.BillingResponseCode.OK -> scope.launch { processPastPurchases() }
                            else -> if (!billingResult.shouldRetrySimple()) {
                                scope.launch { _purchaseResult.emit(billingResult.toFailureResult()) }
                            }
                        }
                        if (continuation.isActive) {
                            continuation.resume(Unit)
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        if (continuation.isActive) {
                            continuation.resume(Unit)
                        }
                    }
                })
            }
            if (!billingClient.isReady && attempt < maxAttempts) {
                delay(RETRY_DELAY_SIMPLE_MS)
            }
        }
    }

    private suspend fun retryPurchaseUpdate(purchases: MutableList<Purchase>?) {
        delay(RETRY_DELAY_SIMPLE_MS)
        if (purchases.isNullOrEmpty()) {
            processPastPurchases()
        } else {
            handlePurchases(purchases)
        }
    }

    private data class BillingCallResult<T>(val billingResult: BillingResult, val data: T?)

    private sealed interface RetryStrategy {
        val maxAttempts: Int

        data class Simple(
            override val maxAttempts: Int = 3,
            val delayMillis: Long = RETRY_DELAY_SIMPLE_MS,
        ) : RetryStrategy

        data class Exponential(
            override val maxAttempts: Int = 3,
            val initialDelayMillis: Long = RETRY_DELAY_EXPONENTIAL_MS,
            val factor: Double = 2.0,
            val maxDelayMillis: Long = RETRY_MAX_DELAY_MS,
        ) : RetryStrategy
    }

    private suspend fun <T> retryBillingCall(
        strategy: RetryStrategy,
        block: suspend () -> BillingCallResult<T>,
    ): BillingCallResult<T> {
        var attempt = 1
        var delayMillis = when (strategy) {
            is RetryStrategy.Simple -> strategy.delayMillis
            is RetryStrategy.Exponential -> strategy.initialDelayMillis
        }

        while (true) {
            val result = block()
            val responseCode = result.billingResult.responseCode
            val shouldRetry = shouldRetry(responseCode, strategy)
            if (!shouldRetry || attempt >= strategy.maxAttempts) {
                return result
            }
            when (strategy) {
                is RetryStrategy.Simple -> delay(strategy.delayMillis)
                is RetryStrategy.Exponential -> {
                    delay(delayMillis)
                    delayMillis = (delayMillis * strategy.factor).toLong()
                        .coerceAtMost(strategy.maxDelayMillis)
                }
            }
            attempt++
        }
    }

    private fun shouldRetry(responseCode: Int, strategy: RetryStrategy): Boolean {
        val simpleRetryCodes = setOf(
            BillingClient.BillingResponseCode.NETWORK_ERROR,
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
            BillingClient.BillingResponseCode.ERROR,
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED,
        )

        val exponentialRetryCodes = setOf(
            BillingClient.BillingResponseCode.NETWORK_ERROR,
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
            BillingClient.BillingResponseCode.ERROR,
        )

        return when (strategy) {
            is RetryStrategy.Simple -> responseCode in simpleRetryCodes
            is RetryStrategy.Exponential -> responseCode in exponentialRetryCodes
        }
    }

    private fun BillingResult.shouldRetrySimple(): Boolean = shouldRetry(
        responseCode,
        RetryStrategy.Simple(),
    )

    private fun BillingResult.shouldRetryExponential(): Boolean = shouldRetry(
        responseCode,
        RetryStrategy.Exponential(),
    )

    private fun BillingResult.toFailureResult(): PurchaseResult.Failed {
        val message = debugMessage.takeIf { it.isNotBlank() }
            ?: when (responseCode) {
                BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> "Billing is unavailable"
                BillingClient.BillingResponseCode.DEVELOPER_ERROR -> "Billing API misused"
                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> "Item is already owned"
                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> "Item is not owned"
                BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> "Item is unavailable"
                BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> "Feature not supported"
                BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> "Billing service unavailable"
                BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> "Billing service disconnected"
                BillingClient.BillingResponseCode.NETWORK_ERROR -> "Network error"
                BillingClient.BillingResponseCode.ERROR -> "Billing error"
                else -> "Billing operation failed"
            }
        return PurchaseResult.Failed(message)
    }
}
