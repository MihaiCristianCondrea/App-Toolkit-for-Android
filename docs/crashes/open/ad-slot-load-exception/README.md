# Incident Report: AdSlotLoadException Network Error Non-Fatal

* **Status:** Open (Unresolved)
* **Issue ID:** `f7ff488bb8b207d12e438303b7d2c9e6`
* **Application:** `com.d4rk.android.apps.apptoolkit` version `26.10.31` (`137261031`)
* **Date:** October 14, 2026 at 05:56:31 EEST

## Behavior and Impact

When requesting ad units (native ad slots), AdMob fails to retrieve SDK core HTML assets due to a network connection failure (`net::ERR_CONNECTION_REFUSED` on `https://googleads.g.doubleclick.net/mads/static/sdk/native/sdk-core-android.html`).

`AdLoadReporter` catches the ad failure and logs a non-fatal `AdSlotLoadException` to Firebase Crashlytics.

## Failure Details

* **Exception:** `AdSlotLoadException` (Non-fatal)
* **Error Code:** `code=NETWORK_ERROR` (`WebView HTTP error -6: net::ERR_CONNECTION_REFUSED`)
* **Location:** `com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.AdSlotLoadException.<init>`
* **Stack Trace Summary:**
  ```text
  Non-fatal Exception: u7: Ad slot 'ca-app-pub-5294151573817700/4743100951' failed for unit 'ca-app-pub-5294151573817700/4743100951': code=NETWORK_ERROR (WebView HTTP error -6: net::ERR_CONNECTION_REFUSED on url: https://googleads.g.doubleclick.net/mads/static/sdk/native/sdk-core-android.html)
         at com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.AdSlotLoadException.<init>(AdSlotLoadException.java:134)
         at com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.AdLoadReporter.onAdFailedToLoad(AdLoadReporter.java:86)
         at com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdHolder$load$1$1.onAdFailedToLoad$lambda$1(NativeAdHolder.java:217)
  ```

## Affected Area

* **Module:** `:library:ui` / ad integration infrastructure
* **Component:** `AdLoadReporter`, `NativeAdHolder`, `AdSlotLoadException`

## Guidance & Open Questions

1. This is a non-fatal telemetry recording of network-level ad load failures (e.g., ad blockers, device offline, DNS filtering, or doubleclick server connectivity issues).
2. Verify if `AdSlotLoadException` logging in `AdLoadReporter` is intended to record expected network-level ad failures or if non-fatal reporting threshold should be tuned for routine network errors.
3. Keep as **Open / Unresolved** until decision on non-fatal ad error telemetry filters is finalized.

## References

* [`docs/crashes/README.md`](../../README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
* [session.json](session.json)
