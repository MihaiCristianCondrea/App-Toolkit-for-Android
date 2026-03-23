# GMA Next-Gen SDK for Android (Beta) — Migration Notes

## Why migrate

Upgrading from `com.google.android.gms:play-services-ads` to GMA Next-Gen SDK can provide:

- Faster banner ad request latency (Google reports up to 27% in an internal comparison).
- Lower SDK footprint on device.
- Background-oriented startup behavior to reduce startup impact.
- Better stability by removing RPC/process-boundary communication patterns.
- Kotlin-first API surface with Java support.
- Support for all existing Google Mobile Ads formats.

> Reference date from Google docs used for these notes: **January 10, 2025**.

---

## Core migration checklist

1. Replace dependency:
   - Remove: `com.google.android.gms:play-services-ads`
   - Add: `com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk`
2. If mediation adapters are present, globally exclude:
   - `com.google.android.gms:play-services-ads`
   - `com.google.android.gms:play-services-ads-lite`
3. Verify SDK levels:
   - `minSdk >= 24`
   - `compileSdk >= 34` (or higher if required by current guidance)
4. Keep `com.google.android.gms.ads.APPLICATION_ID` in manifest for UMP compatibility.
5. Initialize `MobileAds` before loading any ad or calling most SDK APIs.
6. Pass AdMob App ID via `InitializationConfig.Builder("<APP_ID>")`.
7. Move UI work in ad callbacks onto main/UI thread (`runOnUiThread` / `Dispatchers.Main`).

---

## API pattern changes to apply across the codebase

### Initialization

- **Old:** `MobileAds.initialize(context) {}`
- **Next-Gen:** `MobileAds.initialize(context, InitializationConfig.Builder(APP_ID).build()) {}`

### Ad request construction

- Ad unit ID moves into request builders.
- Interstitial/rewarded/app open load methods no longer take context + adUnit in the old style.

### Banner

- Prefer Next-Gen `banner.AdView`.
- Use `BannerAdRequest.Builder(adUnitId, adSize)`.
- Event callbacks are format-specific (for example `BannerAdEventCallback`).

### Native

- Load with `NativeAdLoader.load(NativeAdRequest, NativeAdLoaderCallback)`.
- Register the native ad with media content using `NativeAdView.registerNativeAd(...)`.

### Threading

- Next-Gen callbacks are not guaranteed on main thread.
- Any UI updates inside callbacks must explicitly switch to the UI thread.

---

## Validation checklist before considering migration complete

- Project builds cleanly.
- No remaining imports from `com.google.android.gms.ads.*` in migrated files.
- `MobileAds.initialize(...)` occurs before ad loads and before non-exempt API access.
- Ad callbacks that touch UI are thread-safe (main thread dispatch).
- Banner/native/interstitial/rewarded flows compile and run.
