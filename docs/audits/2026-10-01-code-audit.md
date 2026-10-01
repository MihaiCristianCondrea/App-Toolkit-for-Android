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

Everything below is still open. Remove an item, or move it to the changelog, when it is fixed.

Already clean at the time of the review: no Kotlin package is split across modules, every file's
package matches its directory, no feature depends on a sibling feature, no library module depends
on the sample, and there is no `GlobalScope`, `runBlocking` or `!!` in main code.

## Bugs

- The consent repository's in-flight request still holds its host activity until UMP answers. A
  new host no longer joins it, but the activity is only released when that request ends.
- `UsageAndDiagnosticsViewModel` still applies consent to the SDKs from the ViewModel, so it only
  happens while that screen is open, and `observeSettings()` still combines one flow per key.
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
  `map`/`filter`/`sortedBy` on every frame of a roll.
- The apps widget still fetches the whole catalogue from the network on each update, before it
  falls back to the saved one.
- `library/feature/theme/.../ThemeSettingsScreen.kt:343-424` and
  `library/feature/onboarding/.../ThemeOnboardingPageTab.kt:290-339` build new page lambdas and lists
  on every recomposition.
- Main-thread safety sits in ViewModels instead of the class doing the blocking work:
  `DefaultAboutRepository` (PackageManager), `DefaultCacheRepository` (`deleteRecursively`) and
  `AndroidInstalledAppsLocalDataSource` only stay off the main thread because their ViewModels add
  `flowOn(io)`. The issue report send switches to IO four times. DataStore calls in
  `AppsListViewModel` and `MainActivity.kt:121` are wrapped in `withContext(io)` without need.

## Structure

- `library/core/common/build.gradle.kts:56-66` exposes Firebase, the Mobile Ads SDK and multidex as
  `api`, and `core/common` holds `AdsSdkState` and Firebase setup. `library/core/ui/.../views/ads/`
  holds seven ad views, including the 888-line `NativeAdRenderer`. This code belongs in
  `:library:integration:ads` and `:library:integration:firebase`.
- `library/core/designsystem/.../style/Theme.kt:117-123` and `ThemePreferencesState.kt:46-52` read
  `CommonDataStore` data sources directly although `ThemePreferencesRepository` exists. This is also
  the only reason `:library:core:designsystem` depends on `:library:core:datastore`.
- Shell settings are written from composables through a CompositionLocal
  (`DisplaySettingsScreen.kt:222`, `ShellDisplayRows.kt:61-81`, `DeveloperOptionsScreen.kt:78`),
  and `:library:feature:developer` and `:library:feature:display` depend on all of `:library:shell`
  just for `shell.settings`.
- `BillingRepository` exposes `ProductDetails`, so Play Billing types reach the support UI.
- `library/core/datastore/.../CommonDataStore.kt:141-142,186-190,309-314` and
  `DefaultFavoritesPreferencesDataSource` hold favorites and `componentsShowcaseUnlocked`, which only
  the sample uses.
- `library/feature/permissions/.../DefaultPermissionsRepository.kt:47-95` hard-codes the permission
  list instead of reading the host manifest, and returns the UI model `SettingsConfig`.
- `library/core/network/.../Errors.kt:45-60` contains feature-specific errors (FAQ, SKU details,
  consent, review) and a `Database` group.
- `domain/` packages that hold only models or constants, against `AGENTS.md`: `core/common/domain`,
  `core/network/domain`, `feature/diagnostics/domain`, `feature/onboarding/domain`,
  `feature/support/domain`, `integration/billing/domain`, `integration/consent/domain`,
  `integration/update/domain`, and in the sample `feature/apps/domain`, `feature/onboarding/domain`,
  `feature/tiles/domain` and `core/analytics/domain`.
- Use cases that only forward one repository call: `SendIssueReportUseCase` (which also repeats the
  repository's breadcrumb and dispatcher switch) and `GetChangelogUseCase`.
- UI creating data sources directly: `TrackedTileService.kt:92` and `QuickSettingsTileRequests.kt:71`
  build `AndroidQuickSettingsTilesLocalDataSource`. `sample/feature/tiles/.../di/TilesModule.kt:68-72`
  writes into the raw `CommonDataStore`, bypassing `:sample:core:datastore`.
- Modules import modules they never declare and rely on `api` leaking through. For example,
  `:library:feature:developer` declares only `:library:shell` but imports `core:ui`, `navigation`
  and `core:common`. `onboarding`, `faq`, `issuereporter`, `settings`, `startup`, `support` and
  `shell` import `designsystem` without declaring it.
- Declared dependencies with no imports: `core:datastore` in `core/ui`, `feature/changelog`,
  `feature/faq` and `feature/settings`; `core:network` in `feature/onboarding`; `core:common` in
  `integration/update` and `navigation`. The comment in `core/ui/build.gradle.kts:52-53` about
  `CommonDataStore` is stale.
- `Errors.asUiText()` lives in `core/network/data/remote/extensions/ErrorExtensions.kt:30`, a data
  package, and seven ViewModels import it.
- `:library:integration:ads` owns a full settings screen and ViewModel.
- `:sample:integration:ads` depends on the whole `:library:apptoolkit` while it uses only
  `core:common` and `core:ui`. `:sample:widget` uses `api(project(":sample:feature:apps"))`.

## Clean code

- The theme picker exists twice, in `feature/onboarding/.../ThemeOnboardingPageTab.kt:104-201` and
  `feature/theme/.../ThemeSettingsScreen.kt:163-264`, and the copies have drifted (onboarding
  ignores `seasonalThemesUnlocked`).
- `PermissionsViewModel.kt:86-151` and `SettingsViewModel.kt:82-143` have the same load pipeline.
  `SettingsViewModel.kt:132-136` is a dead branch.
- The `persist` helper is still written out in `DisplaySettingsViewModel`, `ThemeSettingsViewModel`
  and `OnboardingThemeViewModel`; it could move into `ScreenViewModel` once that has a way to
  report.
- `DefaultUsageAndDiagnosticsRepository.kt:49-64` and `UsageAndDiagnosticsSettings` duplicate
  `DefaultConsentRepository.readPersistedSettings` and `ConsentSettings`.
- `sample/feature/apps/.../ui/views/AppActions.kt:36-74` and `AppActionLauncher.kt:77-174` both open
  and share apps, with different share text.
- `GeneralTextField.kt` calls `OutlinedTextField` and `TextField` four times with about 20
  identical arguments.
- Oversized composables: `ThemeSettingsScreen` (about 460 lines), `ListDetailLayout` (about 230),
  `ShellBody` (about 215), `ShellChrome` (about 170).
- `ShellChrome.kt:670-673` duplicates `ShellHost.kt:299-302`, and `ScreenTransitions.kt:129-135`
  duplicates `TabTransitions.kt:73-79`.
- `BaseViewModel.kt:107` takes `stateMutex` for one writer while every other state helper writes
  without it, so the mutex protects nothing.
- Unused public API, kept because removing it breaks consumers: the deprecated
  `FirebaseControllerImpl` typealias and `AdsCoreManager.buildInfoProvider`. Remove both in a
  breaking release, with a migration guide entry.
- `MainAction.ReviewOutcomeReported` and `InAppUpdateResultReported` (and
  `FaqAction.ReviewOutcomeReported`) are sent but mapped to `Unit`. They are the only actions of
  their ViewModels, so removing them means changing those ViewModels' action type.
- The five `updateX` functions in `UsageAndDiagnosticsViewModel` are copies of one another.
- `DefaultAdsSettingsRepository.kt:63-76` repeats `persistPreference`, and `AdsSettingsViewModel`'s
  two persist functions are near-copies.
- The apps filter chip rules exist twice, in `AppsListViewModel.observeFilterValidity` and
  `sample/feature/apps/.../ui/views/screens/AppsList.kt:294-312`.
- The comment above `core:datastore` in `library/core/ui/build.gradle.kts:52-53` says the module
  references `CommonDataStore` by type, which it no longer does.

## Decided

- The GitHub issue-report token in `BuildConfig` (`IssueReporterModule.kt:73`) stays as it is.
- Consent, including ad personalization, keeps defaulting to granted in release builds
  (`!isDebugBuild`).
