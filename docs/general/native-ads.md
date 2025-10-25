# Native Ads

This guide documents how native ads are implemented inside the **App Toolkit** library and how the
host app wires them in.
It covers the shared configuration model, the reusable composables that wrap Google Mobile Ads SDK
views, the XML layouts that
define each presentation, and the Koin bindings that provide ad unit identifiers inside the app
module.

## Architecture overview

Native ads are exposed to the Compose layer through reusable composables that host a `NativeAdView`
via `AndroidView`. Each
component performs the following common steps:

1. Resolve the current `AdsConfig` instance which carries the ad unit identifier (and optionally an
   `AdSize`).
2. Read the `adsEnabledFlow` flag from `CommonDataStore`; ads are skipped entirely when users
   disable them.
3. Short-circuit rendering when running inside preview/inspection mode so designers see a
   placeholder message instead of a live
   request.
4. Inflate the corresponding XML layout, load a native ad with `AdLoader`, and bind headline, body,
   advertiser, icon, media and
   call-to-action views when the response arrives.
5. Dispose of any previously loaded `NativeAd` instances to avoid leaks, mirroring Google’s
   recommended lifecycle management.

This pattern is implemented in every native ad composable, e.g. `AppDetailsNativeAd`,
`AppsListNativeAdCard`,
`HelpNativeAdCard`, `SupportNativeAdCard`, `NoDataNativeAdCard`, and `BottomNavigationNativeAdBanner`.
【F:
apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/ui/components/ads/AppDetailsNativeAd.kt†L18-L106】【F:
apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/ui/components/ads/SupportNativeAdCard.kt†L46-L117】

### Ads configuration model

Library consumers configure ad units with the `AdsConfig` data class which holds the string ID and
desired banner size (used by
banner surfaces that share the same DI entry points as native ads). Each native component receives
the config via parameters or
Koin injection.【F:
apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/domain/model/ads/AdsConfig.kt†L5-L9】

### Shared preview support

All native ad composables detect Compose inspection mode and render a lightweight preview text when
previews are rendered in
Android Studio. This keeps design-time previews fast and avoids attempted network access in the
IDE.【F:
apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/ui/components/ads/AppDetailsNativeAd.kt†L41-L52】

## Library-provided components

The library module ships multiple specialized native ad surfaces tailored to different screens. Each
composable expects an
`AdsConfig` and inflates its own XML layout:

- `AppDetailsNativeAd` – shown inside the app details bottom sheet. Binds headline, body,
  advertiser, icon and CTA button.
  Layout: `native_ad_app_details.xml`.
  【F:
  app/src/main/java/com/d4rk/android/apps/apptoolkit/app/apps/common/AppDetailsBottomSheet.kt†L145-L154】【F:
  apptoolkit/src/main/res/layout/native_ad_app_details.xml†L1-L111】
- `AppsListNativeAdCard` – inserted between items in app lists. Displays headline, body, advertiser
  and icon rows with rounded
  styling. Layout: `native_ad_apps_list_card.xml`.
  【F:
  app/src/main/java/com/d4rk/android/apps/apptoolkit/app/apps/common/screens/AppsList.kt†L162-L170】【F:
  apptoolkit/src/main/res/layout/native_ad_apps_list_card.xml†L1-L93】
- `HelpNativeAdCard` – embedded in the help screen alongside FAQ content. Uses a decorated icon
  container to match the support
  theme. Layout: `native_ad_help_card.xml`.
  【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/app/help/ui/HelpScreen.kt†L115-L136】【F:
  apptoolkit/src/main/res/layout/native_ad_help_card.xml†L1-L122】
- `SupportNativeAdCard` – blends into the support/donations screen and includes media view support
  for richer creatives.
  Layout: `native_ad_support_card.xml`.
  【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/app/support/ui/SupportScreen.kt†L204-L219】【F:
  apptoolkit/src/main/res/layout/native_ad_support_card.xml†L1-L125】
- `NoDataNativeAdCard` – presented on the reusable "no data" layout to monetize empty states while
  keeping copy and CTA visible.
  Layout: `native_ad_no_data_card.xml`.
  【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/ui/components/layouts/NoDataScreen.kt†L50-L112】【F:
  apptoolkit/src/main/res/layout/native_ad_no_data_card.xml†L1-L125】
- `BottomNavigationNativeAdBanner` – slides into the bottom navigation bar container and uses a
  horizontal layout optimized for the
  app chrome. Layout: `native_ad_bottom_bar.xml`.
  【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/app/main/ui/components/navigation/BottomNavigationBar.kt†L42-L56】【F:
  apptoolkit/src/main/res/layout/native_ad_bottom_bar.xml†L1-L92】

Each binding function hides unavailable fields (e.g., missing body text or icons) before calling
`setNativeAd`, ensuring the UI
stays policy compliant across different creative payloads.【F:
apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/ui/components/ads/AppsListNativeAdCard.kt†L139-L174】

## App module integration

The application module wires these composables through Koin. `AdsModule` registers named `AdsConfig`
singletons for every native
placement, which allows screens to inject the proper configuration without hardcoding IDs.
Qualifiers include
`apps_list_native_ad`, `app_details_native_ad`, `no_data_native_ad`, `bottom_nav_bar_native_ad`,
`help_large_banner_ad`, and
`support_native_ad`.【F:
app/src/main/java/com/d4rk/android/apps/apptoolkit/core/di/modules/AdsModule.kt†L31-L65】

Each qualifier ultimately resolves to a release or debug ad unit string via `AdsConstants`. In debug
builds the app serves Google
sample IDs to satisfy policy, while release builds use production values. This logic also exposes
the shared `NATIVE_AD_UNIT_ID`
used for generic placements.【F:
app/src/main/java/com/d4rk/android/apps/apptoolkit/core/utils/constants/ads/AdsConstants.kt†L7-L41】

Screens that participate in native ads request the appropriate config through `koinInject` (or
receive it as a parameter) and
pass it down to the library composable. Examples include:

- `AppsList` injecting `apps_list_native_ad` to interleave ads across the grid feed.
- `AppsListRoute` fetching `app_details_native_ad` for the bottom sheet placement that wraps
  `AppDetailsNativeAd`.
- `NoDataScreen` exposing an `adsConfig` parameter defaulted to the `no_data_native_ad` binding.
- `BottomNavigationBar` pulling `bottom_nav_bar_native_ad` for the persistent chrome ad.
- `HelpScreen` and `SupportScreen` injecting `help_large_banner_ad` and `support_native_ad`
  respectively.
  【F:
  app/src/main/java/com/d4rk/android/apps/apptoolkit/app/apps/common/screens/AppsList.kt†L56-L170】【F:
  app/src/main/java/com/d4rk/android/apps/apptoolkit/app/apps/list/ui/AppsListScreen.kt†L35-L104】【F:
  app/src/main/java/com/d4rk/android/apps/apptoolkit/app/apps/common/AppDetailsBottomSheet.kt†L63-L155】【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/ui/components/layouts/NoDataScreen.kt†L50-L112】【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/app/main/ui/components/navigation/BottomNavigationBar.kt†L38-L55】【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/app/help/ui/HelpScreen.kt†L115-L136】【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/app/support/ui/SupportScreen.kt†L98-L220】

## Implementation notes

- All native ad loaders configure `AdChoices` to display in the top-right corner, matching Google’s
  UX guidelines.【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/ui/components/ads/AppDetailsNativeAd.kt†L87-L104】
- Failure listeners hide the `NativeAdView` when a load error occurs so empty containers do not
  remain visible.【F:
  apptoolkit/src/main/java/com/d4rk/android/libs/apptoolkit/core/ui/components/ads/AppDetailsNativeAd.kt†L97-L104】
- Each layout includes an explicit "Ad" badge and respects Material 3 spacing to align with policy
  and accessibility guidance.【F:apptoolkit/src/main/res/layout/native_ad_support_card.xml†L9-L36】【F:
  apptoolkit/src/main/res/layout/native_ad_help_card.xml†L8-L30】
- When extending the system, create a new XML layout in `apptoolkit/src/main/res/layout`, register a
  matching `AdsConfig` in
  `AdsModule`, and forward the qualifier into your Compose screen via Koin.

These conventions ensure the library and host app stay policy-compliant while keeping integrations
declarative and easy to test.
