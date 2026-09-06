# Incident Report: Billing Duplicate Callback Continuation Crash

* **Status:** Resolved
* **Issue ID:** `05c41df9eee5b548ee99decabc40eb68`
* **Application:** `com.d4rk.cleaner` version `26.07.8` (`137309008`)
* **Date:** July 31, 2026 at 18:02:02 EEST

## Behavior and Impact

When an application queries past purchases (e.g. during app startup or navigating to the support/donation screen), Google Play Billing Client can under certain conditions (such as network timeouts or service reconnects) deliver duplicate callback responses.

In previous versions of the Toolkit billing integration, receiving a second callback (e.g., a delayed `SERVICE_UNAVAILABLE` timeout response after a first response had already resumed the coroutine continuation) resulted in a fatal `java.lang.IllegalStateException: Already resumed` crash. This crashed the application for users interacting with purchase recovery or the support screen.

## Failure Details

* **Exception:** `java.lang.IllegalStateException: Already resumed, but proposed with update BillingCallResult(billingResult=Response Code: SERVICE_UNAVAILABLE, Debug Message: Timeout communicating with service., data=[])`
* **Location:** `com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository$processPastPurchases$2$result$1$1$1.onQueryPurchasesResponse`
* **Stack Trace Summary:**
  ```text
  Fatal Exception: java.lang.IllegalStateException: Already resumed, but proposed with update BillingCallResult(billingResult=Response Code: SERVICE_UNAVAILABLE, Debug Message: Timeout communicating with service., data=[])
         at kotlinx.coroutines.CancellableContinuationImpl.alreadyResumedError(CancellableContinuationImpl.kt:556)
         at kotlinx.coroutines.CancellableContinuationImpl.resumeImpl$kotlinx_coroutines_core(CancellableContinuationImpl.kt:521)
         at kotlinx.coroutines.CancellableContinuationImpl.resumeImpl$kotlinx_coroutines_core$default(CancellableContinuationImpl.kt:493)
         at kotlinx.coroutines.CancellableContinuationImpl.resumeWith(CancellableContinuationImpl.kt:359)
         at com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository$processPastPurchases$2$result$1$1$1.onQueryPurchasesResponse(BillingRepository.java:208)
         at com.android.billingclient.api.BillingClientImpl.zzS(BillingClientImpl.java:3)
  ```

## Root Cause

`BillingRepository` invoked `Continuation.resumeWith` unconditionally inside `onQueryPurchasesResponse` (and related billing query callbacks).

Kotlin's [CancellableContinuation contract](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-cancellable-continuation/) strictly mandates that callers must prevent multiple completions/resumes on the same continuation, including concurrent callbacks. Because Google Play Billing Client can deliver duplicate callbacks or late timeout events, calling `resumeWith` a second time violated coroutine invariants and threw `IllegalStateException: Already resumed`.

## Affected Area

* **Module:** `:library:integration:billing`
* **Class:** `com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository` (`DefaultBillingRepository`)
* **Method:** `processPastPurchases` / `awaitBillingCallback` helper logic

## Fix

Created an atomic first-response gate mechanism (`awaitBillingCallback`) for purchase-query, product-query, and consumption attempts:

1. **Atomic First-Response Gate:** Ensures each request attempt accepts only its first response callback and atomically ignores any subsequent duplicate callbacks.
2. **Cancellation Protection:** Coroutine cancellation closes the gate so late callbacks from Play Billing cannot attempt to resume a cancelled continuation.
3. **Retry Isolation:** Retries generate a fresh gate so late responses from an earlier attempt cannot complete a subsequent retry attempt.
4. **Semantics Preserved:** Does not change retry counts, product IDs, purchase ownership, or consumption semantics.

## Verification

Automated unit test suite in `:library:integration:billing:testDebugUnitTest`:
* `duplicate timeout delivery`
* `concurrent responses`
* `callbacks after cancellation`
* `late callbacks across retry attempts`

All four regression tests passed with zero failures. Billing production Kotlin sources compiled cleanly.

## Timeline

* **2026-07-31 18:02:02 EEST:** Crash incident reported on `com.d4rk.cleaner` version `26.07.8`.
* **2026-08:** Root cause identified in `BillingRepository` continuation handling.
* **2026-08:** `awaitBillingCallback` fix implemented and verified with unit tests in `:library:integration:billing`.

## References

* [`library/integration/billing/README.md`](../../../../library/integration/billing/README.md)
* [Kotlin CancellableContinuation API Documentation](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-cancellable-continuation/)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [breadcrumbs.json](breadcrumbs.json)
* [device-info.md](device-info.md)
