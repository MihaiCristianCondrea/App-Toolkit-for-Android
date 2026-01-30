package com.d4rk.android.libs.apptoolkit.app.support.ui

import android.app.Activity
import app.cash.turbine.test
import com.android.billingclient.api.ProductDetails
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository
import com.d4rk.android.libs.apptoolkit.app.support.billing.PurchaseResult
import com.d4rk.android.libs.apptoolkit.app.support.utils.constants.DonationProductIds
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class SupportViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val productDetailsFlow = MutableStateFlow<Map<String, ProductDetails>>(emptyMap())
    private val purchaseResultFlow = MutableSharedFlow<PurchaseResult>()
    private val billingRepository = mockk<BillingRepository>(relaxed = true) {
        every { productDetails } returns productDetailsFlow
        every { purchaseResult } returns purchaseResultFlow
        coEvery { queryProductDetails(any()) } returns Unit
        every { launchInAppDonationFlow(any(), any()) } returns Unit
    }
    private val firebaseController = FakeFirebaseController()

    private fun createViewModel(
        initialProducts: Map<String, ProductDetails> = emptyMap(),
    ): SupportViewModel {
        productDetailsFlow.value = initialProducts
        return SupportViewModel(billingRepository, firebaseController)
    }

    private fun mockOneTimeOfferDetails(
        formattedPrice: String = "€0.99",
        currencyCode: String = "EUR",
        amountMicros: Long = 990_000,
    ): ProductDetails.OneTimePurchaseOfferDetails =
        mockk(relaxed = true) {
            every { this@mockk.formattedPrice } returns formattedPrice
            every { this@mockk.priceCurrencyCode } returns currencyCode
            every { this@mockk.priceAmountMicros } returns amountMicros
        }

    private fun mockProductDetails(
        productId: String,
        formattedPrice: String = "€0.99",
    ): ProductDetails {
        val offer = mockOneTimeOfferDetails(formattedPrice = formattedPrice)
        return mockk(relaxed = true) {
            every { this@mockk.productId } returns productId
            every { this@mockk.oneTimePurchaseOfferDetails } returns offer
        }
    }

    @Test
    fun `products update screenState to success with mapped list`() =
        runTest(dispatcherExtension.testDispatcher) {
            val p1 = mockProductDetails(DonationProductIds.LOW_DONATION, "€0.99")
            val p2 = mockProductDetails(DonationProductIds.NORMAL_DONATION, "€1.99")

            val viewModel = createViewModel(
                linkedMapOf(
                    DonationProductIds.LOW_DONATION to p1,
                    DonationProductIds.NORMAL_DONATION to p2,
                )
            )

            viewModel.uiState.test {
                var state = awaitItem()
                while (state.screenState !is ScreenState.Success) {
                    state = awaitItem()
                }

                assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)

                val donationOptions = requireNotNull(state.data).donationOptions
                assertThat(donationOptions.map { it.productId })
                    .containsAtLeast(
                        DonationProductIds.LOW_DONATION,
                        DonationProductIds.NORMAL_DONATION
                    )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `pending purchase shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial state
            purchaseResultFlow.emit(PurchaseResult.Pending)

            var stateWithSnackbar = awaitItem()
            while (stateWithSnackbar.snackbar == null) {
                stateWithSnackbar = awaitItem()
            }

            val snackbar = stateWithSnackbar.snackbar
            assertThat(snackbar.isError).isFalse()
            val msg = snackbar.message as UiTextHelper.StringResource
            assertThat(msg.resourceId).isEqualTo(R.string.purchase_pending)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `failed purchase shows error state and snackbar`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            val error = "boom"

            viewModel.uiState.test {
                awaitItem() // initial state
                purchaseResultFlow.emit(PurchaseResult.Failed(error))

                var stateWithError = awaitItem()
                while (stateWithError.screenState !is ScreenState.Error || stateWithError.snackbar == null) {
                    stateWithError = awaitItem()
                }

                assertThat(stateWithError.screenState).isInstanceOf(ScreenState.Error::class.java)

                val errorData = requireNotNull(stateWithError.data)
                assertThat(errorData.error).isEqualTo(error)

                val snackbar = stateWithError.snackbar
                assertThat(snackbar.isError).isTrue()
                val msg = snackbar.message as UiTextHelper.DynamicString
                assertThat(msg.content).isEqualTo(error)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `user cancelled purchase shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial state
            purchaseResultFlow.emit(PurchaseResult.UserCancelled)

            var stateWithSnackbar = awaitItem()
            while (stateWithSnackbar.snackbar == null) {
                stateWithSnackbar = awaitItem()
            }

            val snackbar = stateWithSnackbar.snackbar
            assertThat(snackbar.isError).isFalse()
            val msg = snackbar.message as UiTextHelper.StringResource
            assertThat(msg.resourceId).isEqualTo(R.string.purchase_cancelled)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDonateClicked delegates to billingRepository`() =
        runTest(dispatcherExtension.testDispatcher) {
            val activity = mockk<Activity>(relaxed = true)
            every { activity.isFinishing } returns false
            every { activity.isDestroyed } returns false

            val product = mockProductDetails(DonationProductIds.LOW_DONATION, "€0.99")

            val viewModel = createViewModel(
                linkedMapOf(DonationProductIds.LOW_DONATION to product)
            )

            // Ensure VM reached a usable state before clicking.
            viewModel.uiState.test {
                var state = awaitItem()
                while (state.data?.donationOptions?.any { it.productId == DonationProductIds.LOW_DONATION } != true) {
                    state = awaitItem()
                }
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.onDonateClicked(activity, DonationProductIds.LOW_DONATION)

            verify { billingRepository.launchInAppDonationFlow(activity, product) }
        }

    @Test
    fun `onDonateClicked without eligible offer shows error snackbar`() =
        runTest(dispatcherExtension.testDispatcher) {
            val activity = mockk<Activity>(relaxed = true)
            every { activity.isFinishing } returns false
            every { activity.isDestroyed } returns false

            val product = mockk<ProductDetails>(relaxed = true) {
                every { productId } returns DonationProductIds.LOW_DONATION
                every { oneTimePurchaseOfferDetails } returns null
            }

            val viewModel = createViewModel(
                linkedMapOf(DonationProductIds.LOW_DONATION to product)
            )

            viewModel.uiState.test {
                awaitItem() // initial state

                viewModel.onDonateClicked(activity, DonationProductIds.LOW_DONATION)

                verify(exactly = 0) { billingRepository.launchInAppDonationFlow(any(), any()) }

                var stateWithSnackbar = awaitItem()
                while (stateWithSnackbar.snackbar == null) {
                    stateWithSnackbar = awaitItem()
                }

                val snackbar = stateWithSnackbar.snackbar
                assertThat(snackbar.isError).isTrue()
                val message = snackbar.message as UiTextHelper.StringResource
                assertThat(message.resourceId).isEqualTo(R.string.support_offer_unavailable)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
