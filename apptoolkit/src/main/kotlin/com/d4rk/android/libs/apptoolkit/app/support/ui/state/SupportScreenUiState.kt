package com.d4rk.android.libs.apptoolkit.app.support.ui.state

import com.android.billingclient.api.ProductDetails

data class SupportScreenUiState(
    val error: String? = null,
    val products: List<ProductDetails> = emptyList()
)