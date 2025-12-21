package com.d4rk.android.libs.apptoolkit.app.support.billing

sealed class PurchaseResult {
    object Success : PurchaseResult()
    object Pending : PurchaseResult()
    data class Failed(val error: String) : PurchaseResult()
    object UserCancelled : PurchaseResult()
}
