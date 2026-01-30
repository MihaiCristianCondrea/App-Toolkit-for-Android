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

    private fun createViewModel(): SupportViewModel {
        productDetailsFlow.value = emptyMap()
        return SupportViewModel(billingRepository, firebaseController)
    }

    @Test
    fun `products update screenState to success with mapped list`() =
        runTest(dispatcherExtension.testDispatcher) {
            val p1 = mockk<ProductDetails>()
            val p2 = mockk<ProductDetails>()
            val viewModel = createViewModel()

            viewModel.uiState.test {
                awaitItem() // initial state
                every { p1.productId } returns DonationProductIds.LOW_DONATION
                every { p2.productId } returns DonationProductIds.NORMAL_DONATION
                every { p1.oneTimePurchaseOfferDetails } returns mockk()
                every { p2.oneTimePurchaseOfferDetails } returns mockk()
                productDetailsFlow.value = linkedMapOf(
                    DonationProductIds.LOW_DONATION to p1,
                    DonationProductIds.NORMAL_DONATION to p2
                )
                // It might take a couple of emissions for the screenState to update
                var successState = awaitItem()
                while (successState.screenState !is ScreenState.Success) {
                    successState = awaitItem()
                }
                assertThat(successState.data!!.donationOptions.map { it.productId })
                    .containsAtLeast(
                        DonationProductIds.LOW_DONATION,
                        DonationProductIds.NORMAL_DONATION
                    )
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
                assertThat(stateWithError.data!!.error).isEqualTo(error)
                val snackbar = stateWithError.snackbar
                assertThat(snackbar.isError).isTrue()
                val msg = snackbar.message as UiTextHelper.DynamicString
                assertThat(msg.content).isEqualTo(error)
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
        }
    }

    @Test
    fun `onDonateClicked delegates to billingRepository`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            val activity = mockk<Activity>(relaxed = true)
            val product = mockk<ProductDetails>()

            every { activity.isFinishing } returns false
            every { activity.isDestroyed } returns false
            every { product.productId } returns DonationProductIds.LOW_DONATION
            every { product.oneTimePurchaseOfferDetails } returns mockk()

            productDetailsFlow.value = linkedMapOf(DonationProductIds.LOW_DONATION to product)

            viewModel.onDonateClicked(activity, DonationProductIds.LOW_DONATION)
            verify { billingRepository.launchInAppDonationFlow(activity, product) }
        }

    @Test
    fun `onDonateClicked without eligible offer shows error snackbar`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            val activity = mockk<Activity>(relaxed = true)
            val product = mockk<ProductDetails>()

            every { activity.isFinishing } returns false
            every { activity.isDestroyed } returns false
            every { product.productId } returns DonationProductIds.LOW_DONATION
            every { product.oneTimePurchaseOfferDetails } returns null

            productDetailsFlow.value = linkedMapOf(DonationProductIds.LOW_DONATION to product)

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
            }
        }
}
