# Code audit, October 1, 2026

A read-only review of every `src/main` source set in `:library:*` and `:sample:*`, looking at
performance, module structure against `AGENTS.md`, and clean code. Each item names the file and
line where the problem was seen at the time of the review; line numbers drift as code changes.

The six most serious problems were fixed together with this record:

- Subscriptions were consumed instead of acknowledged (`DefaultBillingRepository`).
- `AdBanner` never destroyed its `AdView`.
- A damaged `shell_settings` file crashed every launch (`DataStoreShellPreferences`).
- The apps list never showed its saved catalogue offline, and Retry stopped working after a thrown
  error (`AppsListViewModel`).
- `extractChangesForVersion` stopped at `###` sub-headings.
- `BaseCoreManager` read its open `dispatchers` during construction, and `isAppLoaded` was not
  volatile.

A second pass fixed:

- The misplaced `catch` in `ToolkitTilesViewModel` and `IssueReporterViewModel`.
- The consent form's dismiss callback ignoring its `FormError`.
- The shared `persistJob` in `AdsSettingsViewModel`.
- The Favorites filter staying selected after the last favorite was removed.
- A failed cache write failing a successful catalogue fetch.
- The `persist { runCatching {} }` helpers in `DisplaySettingsViewModel`, `ThemeSettingsViewModel`
  and `OnboardingThemeViewModel`, and the `runCatching` calls in `SeasonalThemeOverlayViewModel`
  and `AboutSettingsContent`.
- The compass and level sensors running in the background.
- `staticCompositionLocalOf` in `UiPreferences`.
- The two-write palette changes in the theme and seasonal repositories.
- Per-frame recomposition in `ShellPlayerOverlay` and `DiceRollTool`.
- The pasted citation tag in `UsageAndDiagnosticsViewModel` and the unused `merged` map in
  `LoggedScreenViewModel.catchReport`.

A third pass fixed:

- Class names that R8 renames in `LoggedScreenViewModel` telemetry (now `viewModelName`, and the
  event's source name), and its duplicated error-reporting block.
- Crashlytics keys that stuck to later reports, and billing errors reported as `SupportViewModel`.
- A cancelled in-app update request still starting the update flow.
- Consent requests joining one whose host was destroyed.
- The five separate writes behind "Allow all" and "Allow essential".
- The support page's full-page error for a failed purchase, and its missing Retry
  (`SupportEvent.QueryProductDetails` no longer carries a `BillingClient`).
- The `palette.kt` remember keys, `IllegalStateException` mapping to `NO_DATA`, the double review
  request, and the cached `KtorClient` with full-body logging.
- `themePreferencesState()` emitting once per changed key, the favorite lambda recomposing every
  card, the tile state written on every shade open, and the widget's serial uncached icon loads.
- `FavoritesChangedReceiver`, the inline four-hour constant in `AdsCoreManager`, and the stale
  `INSTANCE` after `DefaultBillingRepository.close()`.

A fourth pass fixed:

- Main-thread safety moved into `DefaultAboutRepository`, `DefaultCacheRepository` and
  `AndroidInstalledAppsLocalDataSource`, and the redundant dispatcher switches in the issue report
  send, `AppsListViewModel` and `MainActivity` were dropped.
- Diagnostics consent is applied to the SDKs by `DefaultUsageAndDiagnosticsRepository` after each
  write instead of by the ViewModel while the screen is open.
- The dead branch in `SettingsViewModel`, the non-atomic `BaseViewModel.updateSuccessState`, the
  copied `updateX` functions and ads persist functions, the apps filter rule written twice, and the
  duplicated scene transition and fade specs in `:library:shell` and `:library:navigation`.
- The widget waiting on the network before drawing anything.

Everything below is still open. Remove an item, or move it to the changelog, when it is fixed.

Already clean at the time of the review: no Kotlin package is split across modules, every file's
package matches its directory, no feature depends on a sibling feature, no library module depends
on the sample, and there is no `GlobalScope`, `runBlocking` or `!!` in main code.

## Bugs

- The consent repository's in-flight request still holds its host activity until UMP answers. A
  new host no longer joins it, but the activity is only released when that request ends.
- `DefaultUsageAndDiagnosticsRepository.observeSettings()` still combines one flow per key. It now
  only feeds the screen, so a mixed state only shows for a frame, but one snapshot of storage would
  remove it.
- `library/consumer-rules.pro` files exist for `:library:apptoolkit` and `:library:navigation`, but
  no build file sets `consumerProguardFiles`, so consumer apps never receive those keep rules
  (including the `kotlinx.serialization` ones).

## Performance

- `library/integration/billing/.../di/BillingModule.kt:33` creates billing with
  `createdAtStart = true`, so every cold start binds the Play Billing service and queries purchases.
  The repository also reconnects by hand in three places on top of `enableAutoServiceReconnection()`,
  and each retry runs another purchase query.
- `library/shell/.../chrome/NavigationSurfaces.kt:199-247` reads the animated rail width in
  composition, so the rail and all its items recompose on every frame of an expand or collapse.
  The alignment and spacing it drives are composition parameters of `Column` and
  `PinnedFooterColumn`, so the fix needs a custom layout; left until it can be checked on a device.
- `sample/feature/tiles/.../tools/DiceRollTool.kt` still builds the face lists with
  `map`/`filter`/`sortedBy` on every frame of a roll: a few dozen small objects per frame for about
  a second. Reusing arrays would mean rewriting the projection math, which is not worth it without
  a profile showing a cost.

## Structure

- `library/core/common/build.gradle.kts:56-66` exposes Firebase, the Mobile Ads SDK and multidex as
  `api`, and `core/common` holds `AdsSdkState` and Firebase setup. `library/core/ui/.../views/ads/`
  holds seven ad views, including the 888-line `NativeAdRenderer`. This code belongs in
  `:library:integration:ads` and `:library:integration:firebase`.
- `library/core/designsystem/.../style/Theme.kt:117-123` and `ThemePreferencesState.kt:46-52` read
  `CommonDataStore` data sources directly although `ThemePreferencesRepository` exists. This is also
  the only reason `:library:core:designsystem` depends on `:library:core:datastore`.
- Shell settings are written from composables through a CompositionLocal
  (`DeveloperOptionItem.kt`), and `:library:feature:developer` depends on all of `:library:shell`
  just for `shell.settings`. `:library:feature:display` no longer depends on the shell.
- `BillingRepository` exposes `ProductDetails`. The support screen and state no longer see it, but
  `SupportViewModel` keeps it privately to launch a donation. Billing should:
  - throw from `queryProductDetails` on a non-OK response instead of emitting
    `PurchaseResult.Failed`, which `SupportViewModel` now has to treat as the query's failure while
    it loads;
  - give `PurchaseResult.Failed` a typed reason instead of Play's English debug message, which the
    page shows as it is;
  - launch a donation by product id, so features never hold Play types;
  - take a `BillingHost`, like `ReviewHost`, so `SupportEvent.Donate` no longer carries an
    `Activity`.
  `ProductDetails.primaryOfferToken` in `feature/support/.../ProductDetailsExtensions.kt` is unused.
- `library/core/datastore/.../CommonDataStore.kt:141-142,186-190,309-314` and
  `DefaultFavoritesPreferencesDataSource` hold favorites and `componentsShowcaseUnlocked`, which only
  the sample uses.
- `library/feature/permissions/.../DefaultPermissionsRepository.kt:41-89` hard-codes the permission
  list instead of reading the host manifest, and returns the UI model `SettingsConfig`.
- `domain/` packages that hold only models or constants, against `AGENTS.md`: `core/common/domain`,
  `feature/diagnostics/domain`, `feature/support/domain`, `integration/billing/domain`, `integration/consent/domain`,
  `integration/update/domain`, and in the sample `feature/apps/domain`, `feature/onboarding/domain`,
  `feature/tiles/domain` and `core/analytics/domain`.
- UI creating data sources directly: `TrackedTileService.kt:92` and `QuickSettingsTileRequests.kt:71`
  build `AndroidQuickSettingsTilesLocalDataSource`. `sample/feature/tiles/.../di/TilesModule.kt:68-72`
  writes into the raw `CommonDataStore`, bypassing `:sample:core:datastore`.
- Modules import modules they never declare and rely on `api` leaking through. For example,
  `:library:feature:developer` declares only `:library:shell` but imports `core:ui`, `navigation`
  and `core:common`. `onboarding`, `faq`, `issuereporter`, `settings`, `startup`, `support` and
  `shell` import `designsystem` without declaring it, and `feature/advanced` imports
  `core:datastore` (`storageCall`, `SeasonalThemeRepository`) without declaring it.
- Declared dependencies with no imports: `core:datastore` in `core/ui`, `feature/changelog`,
  `feature/faq` and `feature/settings`; `core:network` in `feature/onboarding`, `feature/settings`,
  `feature/permissions`, `feature/advanced` and `feature/support` (since their move to
  `core.ui.screen`); `core:common` in
  `integration/update` and `navigation`. The comment in `core/ui/build.gradle.kts:52-53` about
  `CommonDataStore` is stale.
- `FirebaseController`, `DefaultFirebaseController` and `FirebaseControllerImpl` are deprecated
  aliases of `TelemetryRepository` and `FirebaseTelemetryRepository`, kept for one release. Remove them in the release
  after this one. (Components now read `LocalTelemetry` instead of taking a parameter.)
- `:library:integration:ads` owns a full settings screen and ViewModel.
- `:sample:integration:ads` depends on the whole `:library:apptoolkit` while it uses only
  `core:common` and `core:ui`. `:sample:widget` uses `api(project(":sample:feature:apps"))`.

## Clean code

- Gaps in `core.ui.screen` that the migrations worked around locally:
  - `MessageHost` has no `modifier`, so `FirebaseOnboardingPage` draws its own host, and no toast
    mode, so the issue reporter sheet has a private `MessageToasts`.
  - `ScreenViewModel` has no `clearMessages()`; the issue reporter's reset calls `messageShown` for
    each queued message.
  - There is no `Loadable<T>.valueOrNull()`, so screens unwrap `Loadable.Ready` with a `when` or a
    cast.
  - There is no in-coroutine form of `launchReport` to run two reported steps in order, so
    `SeasonalThemeOverlayViewModel` starts its greeting lookup from both the block and `onError`.
- Strings that two features both need are copied into each, because features may not depend on
  each other: for example `device_info` (About, issue reporter), `oss_license_title` (About, FAQ,
  licenses), `usage_and_diagnostics` (diagnostics, onboarding, privacy) and `privacy_policy`. Moving
  such a string to `:library:core:ui` is the fix when its copies must stay identical. Copies of
  strings `:library:core:ui` already provides are gone.
- `DefaultUsageAndDiagnosticsRepository.kt:49-64` and `UsageAndDiagnosticsSettings` duplicate
  `DefaultConsentRepository.readPersistedSettings` and `ConsentSettings`.
- `sample/feature/apps/.../ui/views/AppActions.kt:36-74` and `AppActionLauncher.kt:77-174` both open
  and share apps, with different share text.
- `GeneralTextField.kt` calls `OutlinedTextField` and `TextField` four times with about 20
  identical arguments.
- Oversized composables: `ListDetailLayout` (about 230),
  `ShellBody` (about 215), `ShellChrome` (about 170).
- Unused public API, kept because removing it breaks consumers: `AdsCoreManager.buildInfoProvider`.
  Remove it in a breaking release, with a migration guide entry.
- The comment above `core:datastore` in `library/core/ui/build.gradle.kts:52-53` says the module
  references `CommonDataStore` by type, which it no longer does.

## Decided

- The GitHub issue-report token in `BuildConfig` (`IssueReporterModule.kt:73`) stays as it is.
- Consent, including ad personalization, keeps defaulting to granted in release builds
  (`!isDebugBuild`).
