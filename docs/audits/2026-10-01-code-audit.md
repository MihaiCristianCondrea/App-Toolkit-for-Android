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

Everything below is still open. Remove an item, or move it to the changelog, when it is fixed.

Already clean at the time of the review: no Kotlin package is split across modules, every file's
package matches its directory, no feature depends on a sibling feature, no library module depends
on the sample, and there is no `GlobalScope`, `runBlocking` or `!!` in main code.

## Bugs

- `library/core/ui/.../base/LoggedScreenViewModel.kt:50,65,160` sends `this::class.java.simpleName`
  and `event::class.java.simpleName` to Crashlytics breadcrumbs and the GA4 `view_model` parameter.
  No `-keepnames` rule covers ViewModels or events, so release builds report obfuscated names.
  Use explicit names (`screenName`) or ship keep rules. Its `?: "Unknown…"` fallbacks are dead.
- `library/integration/billing/.../DefaultBillingRepository.kt` reports its own errors as
  `viewModelName = "SupportViewModel"`. `DefaultFirebaseController.kt:146-152,165-169` stores
  per-report values with `setCustomKey`, which then stick to every later crash.
- `library/integration/consent/.../UmpConsentRemoteDataSource.kt:107-110`: the `consentForm.show`
  dismiss callback ignores its `FormError` and always reports success.
- `library/integration/consent/.../DefaultConsentRepository.kt:57-58,100-147`: the process-wide
  request scope keeps `host.activity` alive with no timeout, and a later caller joins an in-flight
  request regardless of its own host, so its form can show on a destroyed window.
- `library/feature/diagnostics/.../UsageAndDiagnosticsViewModel.kt:112-161`: "Allow all" and "Allow
  essential" make five separate DataStore writes, and the ViewModel pushes each intermediate
  consent state into the Firebase SDK. Write the bundle in one `edit` and apply consent in the data
  layer.
- `sample/feature/apps/.../AppsListViewModel.kt:209-227`: `observeFilterValidity` runs only when
  `screenState` changes, so removing the last favorite leaves the Favorites filter selected on an
  empty grid. The chip rules are duplicated in `ui/views/screens/AppsList.kt:294-312`. Combine
  `screenState` and `favorites` in one place.
- `library/feature/support/.../SupportViewModel.kt:159-166,259-263`: a failed or cancelled purchase
  sets `ScreenState.Error`, which replaces the whole page with "failed to load SKU details" and no
  retry. Show it as a snackbar and keep `Success`.
- `library/feature/issuereporter/.../IssueReporterViewModel.kt:197-204`: `.catch { emit(...) }` sits
  after `onEach(handleResult)`, so the error goes to an empty `collect {}` and the sheet stays in
  its sending state.
- `library/integration/ads/.../AdsSettingsViewModel.kt:126-211`: both settings share one
  `persistJob`, so toggling one cancels the other's write. `DefaultAdsSettingsRepository.kt:63-76`
  repeats `persistPreference`.
- `library/integration/update/.../DefaultInAppUpdateRepository.kt:37-77`: Task listeners are not
  removed in `awaitClose`, so a cancelled request can still start the update flow on a dead
  activity's launcher.
- `sample/feature/apps/.../DefaultDeveloperAppsRepository.kt:45-54`: a failed cache write turns a
  successful network fetch into an error. Report the write failure on its own.
- `sample/feature/tiles/.../ToolkitTilesViewModel.kt:103-125` has the same outer `catch` placement
  that was fixed in `AppsListViewModel`, so its retry stops after one thrown error.
- `library/core/ui/.../views/drawable/palette.kt:52-54`: the `remember` keys leave out `legs`,
  `grass` and `backgroundTrees`, so those colors go stale.
- `library/core/network/.../ThrowableExtensions.kt:93` maps every `IllegalStateException` to
  `NO_DATA` although `INVALID_STATE` exists.

## Performance

- `library/integration/billing/.../di/BillingModule.kt:33` creates billing with
  `createdAtStart = true`, so every cold start binds the Play Billing service and queries purchases.
  The repository also reconnects by hand in three places on top of `enableAutoServiceReconnection()`,
  and each retry runs another purchase query.
- `library/shell/.../chrome/ShellPlayerOverlay.kt:110` reads `expansion.value` in composition, so
  the overlay and the host's player content recompose on every animation frame and drag.
- `library/shell/.../chrome/NavigationSurfaces.kt:199-247` reads the animated rail width in
  composition, so the rail and all its items recompose on every frame.
- `sample/feature/tiles/.../tools/DiceRollTool.kt:210-214,353,414` reads rotations in composition and
  builds a new `Path` and `PathEffect` every frame.
- `library/core/designsystem/.../style/UiPreferences.kt:23-29`: `staticCompositionLocalOf` with a
  hard-coded `true` initial value invalidates the whole app tree on the first DataStore emission
  and on every toggle. Use `compositionLocalOf`.
- `library/core/datastore/.../DefaultThemePreferencesRepository.kt:56-63` and
  `DefaultSeasonalThemeRepository.kt:75-76,92-93` write two keys in separate `edit` calls, so the
  theme rebuilds twice and can flash the wrong palette.
- `sample/feature/tiles/.../ToolViewModels.kt:147-167`: the compass and level sensors are collected
  in `viewModelScope`, so they keep running while the app is in the background.
- `sample/feature/tiles/.../services/TrackedTileService.kt:91-94` writes SharedPreferences every time
  the Quick Settings shade opens.
- `sample/widget/.../AppIconsWidget.kt:97-156` downloads up to nine icons one after another over raw
  `URLConnection` without a cache, up to about 90 seconds in the worst case.
- `sample/feature/apps/.../AppsListScreen.kt:115-129`: the favorite lambda is keyed on `favorites`
  and the app list, so one tap recomposes every visible card.
- `library/feature/theme/.../ThemeSettingsScreen.kt:343-424` and
  `library/feature/onboarding/.../ThemeOnboardingPageTab.kt:290-339` build new page lambdas and lists
  on every recomposition.
- Main-thread safety sits in ViewModels instead of the class doing the blocking work:
  `DefaultAboutRepository` (PackageManager), `DefaultCacheRepository` (`deleteRecursively`) and
  `AndroidInstalledAppsLocalDataSource` only stay off the main thread because their ViewModels add
  `flowOn(io)`. The issue report send switches to IO four times. DataStore calls in
  `AppsListViewModel` and `MainActivity.kt:121` are wrapped in `withContext(io)` without need.
- `library/core/network/.../client/KtorClient.kt:45-79` caches the client in an unsynchronized
  `var`, which can build two clients, and debug logging uses `LogLevel.ALL`, which prints headers.
- `library/integration/review/.../DefaultReviewRepository.kt:61-75` requests the review flow twice
  per review and swallows both failures.

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
- `BillingRepository` exposes `ProductDetails`, and `SupportEvent.kt:25` carries a `BillingClient`, so
  Play Billing types reach the support UI.
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
- `LoggedScreenViewModel.kt:154-191` and `:212-249` repeat the error-reporting block, and lines
  157-161 build a `merged` map that is never used.
- `persist { viewModelScope.launch { runCatching { block() } } }` is copied in
  `DisplaySettingsViewModel`, `ThemeSettingsViewModel`, `OnboardingThemeViewModel` and
  `SeasonalThemeOverlayViewModel`, and swallows every failure including cancellation.
  `sample/feature/settings/.../AboutSettingsContent.kt:37-46` does the same.
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
- Dead code: `FavoritesChangedReceiver` (nothing sends its action), the deprecated
  `FirebaseControllerImpl` typealias, `AdsCoreManager.buildInfoProvider`, the `ReviewOutcomeReported`
  and `InAppUpdateResultReported` actions that `MainActivity` maps to `Unit`, the widget's
  unreachable `Loading` branch, and the hand-written `INSTANCE` in `DefaultBillingRepository`,
  which `close()` leaves pointing at a closed client.
- `UsageAndDiagnosticsViewModel.kt:139` contains a pasted `:contentReference[oaicite:2]` tag, and
  its five `updateX` functions are copies of one another.
- `AdsCoreManager.kt:212-213` uses an inline `3600000 * 4`.

## Decisions for the maintainer

- The GitHub issue-report token ships in `BuildConfig`, only base64-encoded
  (`IssueReporterModule.kt:73`). Anyone can extract it, so it should be a fine-grained token limited
  to creating issues on the one repository.
- Every consent, including ad personalization, defaults to granted in release builds
  (`!isDebugBuild`, `DefaultUsageAndDiagnosticsRepository.kt:51-55` and the consent integration).
  That is a privacy policy choice made inside the library.
