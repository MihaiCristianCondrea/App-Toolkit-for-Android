package com.d4rk.android.libs.apptoolkit.app.support.ui.state

data class SupportScreenUiState(
    val error: String? = null,
    val donationOptions: List<DonationOptionUiState> = emptyList(),
    val isBillingInProgress: Boolean = false,
)

data class DonationOptionUiState(
    val productId: String,
    val formattedPrice: String?,
    val isEligible: Boolean,
)
