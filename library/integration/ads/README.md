# `:library:integration:ads` Logic Graph

## Purpose

Owns ad preference settings and Google Mobile Ads integration UI used by AppToolkit hosts.

## Owns

- Ads settings repository, ViewModel, screen, and activity.
- `AdsCoreManager`, `AdsSdkInitializer`, and Google Mobile Ads SDK initialization.
- App-open ad lifecycle; the `INTERNET`, `ACCESS_NETWORK_STATE`, and `AD_ID` permissions required by
  the SDK; and default Mobile Ads initialization/loading metadata.

## Does not own

- Consent acquisition, owned by `:library:integration:consent`.
- Generic native-ad rendering primitives, currently owned by `:library:core:ui`.
- Host ad-unit IDs and host-specific ad policies, owned by the host/common configuration. That
  includes what "reduce ads" means: this module toggles the preference, the host decides what it
  suppresses.

## Depends on

- [`:library:core:common`](../../core/common/README.md) for ads/Firebase contracts and host
  constants.
- [`:library:core:datastore`](../../core/datastore/README.md) for the persisted reduced-ads opt-in.
- [`:library:core:network`](../../core/network/README.md) for shared result/error types.
- [`:library:core:ui`](../../core/ui/README.md) for screen contracts and reusable Compose UI.
- [`:library:integration:consent`](../consent/README.md) so ad state respects consent.

## Used by

- `:sample` and `:library:apptoolkit`.

## Flow chart

```mermaid
flowchart TD
    Settings[AdsSettingsScreen] --> VM[AdsSettingsViewModel]
    VM --> Repo[AdsSettingsRepository]
    Repo -->|persist opt-in| Reduce[CommonDataStore reduceAds]
    Repo -->|apply privacy choice| Consent[ConsentRepository]
    Reduce --> Policy[Host ad policy]
    Policy --> Manager[AdsCoreManager]
    Manifest[Host manifest AdMob application ID] --> Id[AdMobAppIdProvider]
    Id --> Manager
    Manager --> Gate{Valid app ID?}
    Gate -->|no| Disabled[SDK remains not ready]
    Gate -->|yes, once per process| Initializer[AdsSdkInitializer]
    Initializer --> SDK[Google Mobile Ads]
    SDK --> Ready[AdsSdkState.isReady]
    Ready --> Slot[Compose ad slot]
    Slot -->|ready| Request[Banner / native / app-open request]
    Request --> SDK
    Request -->|failure| Empty[Empty non-fatal slot]
```

## Architectural decisions

- There is no ads-enabled preference. The SDK initializes and ad slots render for every install;
  the only stored ads preference is the reduced-ads opt-in, and it never gates rendering. Two
  readings of an enablement preference with different defaults is what used to take host processes
  down, and the preference no longer exists to disagree with itself.
- Reduced ads is one preference with one meaning here: store it and toggle it. What it suppresses is
  host policy — in the sample, app-open ads and nothing else. `AdsSettingsUiState` carries only that
  opt-in, so every control on the screen is live at all times.
- Installs that had switched ads off under the removed preference are carried onto the opt-in by
  `ReduceAdsMigration` in `:library:core:datastore`.
- SDK initialization is idempotent, mutex-protected, and conditional on a valid host-manifest app
  ID; the toolkit never supplies a fallback publisher ID. That id check is the only thing that can
  stop it.
- Readiness is explicit state because asynchronous SDK initialization is a different fact from
  wanting an ad. Ad slots wait and retry when readiness changes.
- Ad rendering fails closed: SDK exceptions or unavailable consent produce an empty slot, never a
  process-fatal composition error.

## Public contracts

- Ads settings screen/activity, repository contract (`observeReduceAds`/`setReduceAds`), and UI
  event/action/state contracts.
- `AdsCoreManager` and its replaceable `AdsSdkInitializer` test seam.

## Internal implementations

- `DefaultAdsSettingsRepository` and SDK/persistence coordination.

## Current risks

Ad rendering is split between this integration and `:library:core:ui`, which weakens the integration
boundary and makes the generic UI module depend conceptually on an optional SDK concern.

## Migration notes

### Fixed:
`IllegalStateException: MobileAds.initialize must be called before using the Google Mobile Ads SDK`

Reported from `NativeAdLoader.load` inside a Compose `DisposableEffect`, which made it fatal: the
throw happened during composition, so it took the process down instead of failing one ad slot.

Two independent causes, both fixed:

- The ads-enabled preference had two sources with different defaults. `AdsCoreManager` gated SDK
  initialization on one and the ad views read the other, so in a debug build the views could believe
  ads were on while the SDK had never been initialized. The preference has since been removed
  outright: initialization and rendering are both unconditional, so there is nothing left to
  disagree.
- Even with one source, initialization is asynchronous. An ad slot composed during startup could
  request before the SDK was up. Requests now wait on `AdsSdkState.isReady` and re-key when
  readiness
  changes, so a slot composed early starts by itself rather than throwing and never retrying.

`rememberNativeAd` additionally wraps the load in `runCatching`, so any remaining synchronous SDK
throw renders an empty slot instead of killing the host. An ad is never worth a crash.

Ads, consent, and Mobile Ads initialization previously diverged in ways that could terminate every
consumer app. The durable safeguards are documented at the modules that own them:

- [`:library:core:common`](../../core/common/README.md) owns host-manifest AdMob ID validation and
  the narrowly scoped UMP crash guard.
- [`:library:core:datastore`](../../core/datastore/README.md) owns the reduced-ads preference and
  the migration off the removed enablement key.
- [`:library:integration:consent`](../consent/README.md) owns consent single-flight and
  host-lifecycle
  validation.
- [`:library:core:ui`](../../core/ui/README.md) owns fail-closed ad loading and host-overridable
  native-ad surfaces.

Changes to ad initialization or rendering must preserve all four boundaries; fixing only the
settings repository is not sufficient.
