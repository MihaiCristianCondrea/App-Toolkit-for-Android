# Purchase recovery: duplicate Billing callback

Issue: `05c41df9eee5b548ee99decabc40eb68`, reported by `com.d4rk.cleaner` version
`26.07.8` (`137309008`) on July 31, 2026 at 18:02:02 EEST. Original trace and breadcrumbs:
`docs/crashes/BillingRepository$processPastPurchases/`.

## Diagnosis

The fatal error is `IllegalStateException: Already resumed`, proposing a second
`BillingCallResult` with `SERVICE_UNAVAILABLE`, `Timeout communicating with service.`, and an
empty purchase list. The failing frame is Toolkit's purchase-query response callback, in the
historical `com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository` package.
The first response is not identified by this trace.

This establishes repeated continuation completion. It is not the separate `ProxyBillingActivity`
null-pending-intent failure and has no demonstrated relationship to activity `configChanges`.
Kotlin's [continuation contract](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-cancellable-continuation/)
requires callers to prevent multiple resumes, including concurrent callbacks.

## Existing-version audit

At inspection, both current Toolkit source and the cached published `billing:3.0.0-pre12` AAR
contain an unconditional resume in the purchase-query callback. `javap -c -p` on the published
`DefaultBillingRepository$processPastPurchases$2$result$1$1$1` class confirms that
`onQueryPurchasesResponse` invokes `Continuation.resumeWith` directly.

Smart Cleaner's local version catalog declares Toolkit `3.0.0-pre12`. This does not establish
which dependencies each newer Play release shipped. No historical or current production APK
was audited, and no live Firebase recurrence data was available. A newer Cleaner version or a
renamed Toolkit package is insufficient evidence that this issue was fixed.

## Local fix and rollout

`awaitBillingCallback` gives each purchase-query, product-query, and consumption attempt an atomic
first-response gate. Duplicate callbacks cannot resume the continuation twice. Cancellation closes
the gate; cancellation racing with the first resume retains coroutine prompt-cancellation behavior.
Late callbacks from one attempt cannot complete a subsequent retry, which owns a fresh gate.
The SDK exposes no cancellation handle for these requests, so their late results are ignored.

The fix does not change retry counts, product IDs, purchase ownership, or consumption semantics.
It does not suppress unrelated exceptions. The connection listener is outside this patch's scope.

Publish a new immutable Toolkit version containing this patch, then update consuming apps and
verify their resolved billing artifact. Do not mark old installed Cleaner builds as repaired.
Purchase-flow device testing and confirmation of production rollout remain separate checks.

## Validation

Passed `:library:integration:billing:testDebugUnitTest --offline --no-daemon --console=plain`:
four tests, zero failures, errors, or skips. They exercise duplicate timeout delivery, concurrent
responses, callbacks after cancellation, and late callbacks across retry attempts. Billing's
production Kotlin sources compiled as part of this task. `git diff --check` also passed.
No device purchase or live Play release verification was performed.
