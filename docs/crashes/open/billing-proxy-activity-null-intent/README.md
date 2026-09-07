# Incident Report: ProxyBillingActivity Null PendingIntent Crash

* **Status:** Open (Unresolved)
* **Issue ID:** `ab47e167269f091f3ffe0938df7bf5e9`
* **Application:** `com.d4rk.netprobe` version `26.09.05` (`137260905`)
* **Date:** September 5, 2026 at 14:59:25 EEST

## Behavior and Impact

When launching the Play Billing flow or support screen UI, `ProxyBillingActivity.onCreate` throws a fatal `NullPointerException` when trying to invoke `PendingIntent.getIntentSender()` on a null object reference.

This crashes the host app when launching billing activities provided by the Google Play Billing Library.

## Failure Details

* **Exception:** `java.lang.RuntimeException: Unable to start activity ComponentInfo{com.d4rk.netprobe/com.android.billingclient.api.ProxyBillingActivity}: java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.IntentSender android.app.PendingIntent.getIntentSender()' on a null object reference`
* **Caused By:** `java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.IntentSender android.app.PendingIntent.getIntentSender()' on a null object reference`
* **Location:** `com.android.billingclient.api.ProxyBillingActivity.onCreate` (line 416)
* **Stack Trace Summary:**
  ```text
  Fatal Exception: java.lang.RuntimeException: Unable to start activity ComponentInfo{com.d4rk.netprobe/com.android.billingclient.api.ProxyBillingActivity}: java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.IntentSender android.app.PendingIntent.getIntentSender()' on a null object reference
         at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3433)
         at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3607)
         ...
  Caused by java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.IntentSender android.app.PendingIntent.getIntentSender()' on a null object reference
         at com.android.billingclient.api.ProxyBillingActivity.onCreate(ProxyBillingActivity.java:416)
  ```

## Affected Area

* **Module:** `:library:integration:billing` (Google Play Billing Library dependency `com.android.billingclient:billing`)
* **Component:** `com.android.billingclient.api.ProxyBillingActivity`

## Assessment of Proposed Workarounds

Do **not** propagate expanded `android:configChanges` overrides (`smallestScreenSize|uiMode|fontScale|layoutDirection|locale|density`) with `tools:replace` as a proven crash fix.

Inspection of resolved Google Play Billing `9.1.0` `ProxyBillingActivity` bytecode (`javap -c -p`) reveals:
1. `onCreate` branches on `savedInstanceState`.
2. The null-bundle launch branch reads `BUY_INTENT` or `IN_APP_MESSAGE_INTENT`. If neither extra is present, the pending intent remains null prior to calling `getIntentSender()`.
3. The saved-state restoration branch restores callback/flow state rather than re-executing the pending-intent launch path.

This bytecode evidence indicates that `ProxyBillingActivity` expects specific intent extras on initial launch. Disabling activity recreation for selected configuration changes does not prevent process death or invalid initial launch intents.

## Existing Safeguards and Limits

Toolkit's `BillingRepository` checks client readiness, validates host lifecycle, and catches synchronous exceptions around `BillingClient.launchBillingFlow`. However, these safeguards cannot catch asynchronous framework crashes occurring inside Google's `ProxyBillingActivity.onCreate`.

## Guidance & Open Questions

1. Keep this issue classified as **Open / Unresolved** until a reproducible flow is established or an upstream Play Billing release resolves the signature.
2. Verify purchase flows on physical devices with licensed test accounts and Play-installed test builds, testing cancel, success, backgrounding, and configuration changes while the purchase UI is visible.
3. Check whether crash reports originate from Play pre-launch crawlers or automated intent probes.

## References

* [`library/integration/billing/README.md`](../../../../library/integration/billing/README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
