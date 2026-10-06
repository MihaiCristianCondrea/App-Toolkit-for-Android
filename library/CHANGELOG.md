# Changelog

---

# October 06, 2026

**Version:** `3.0.0-pre23`

### Added

- `ShellLayoutPolicy(hideSingleTabBottomBar = true)` hides a single tab's phone bottom bar while
  keeping the menu, rail and drawer. A second tab restores the bar automatically. Existing hosts
  keep their current behavior by default.

### Fixed

- Rainfall wind starts at its initial gust phase, preventing a sudden change in direction on the
  first animation frame.

### Improved

- The version information dialog loads the host app icon without querying `PackageManager` during
  composition.

---

# October 03, 2026

**Version:** `3.0.0-pre22`

This release replaces the Toolkit's navigation with a one-activity shell. It removes the old
navigation instead of deprecating it; the [3.0.0 migration guide](../docs/migration/3.0.0.md) maps
every removed API to its replacement.

### Added

- Added the shell navigation core to `:library:navigation`: `shellGraph { }` describes tabs,
  children, pages, the drawer, the overflow menu, start screens and deep links; `ShellNavigator`
  moves through them with one stack per tab and a tab history that back follows; `ShellNavDisplay`
  plays Android's cross-activity back animation on the back gesture; `ListDetailScene` shows a list
  page and its detail side by side with a draggable separator. Tabs, drawer entries and overflow
  entries take `ToolkitIcon`, so animated vector drawables and Lottie icons play on them.
- Added `:library:shell` with `ShellHost`, which draws an app's graph in its one activity: app bar,
  navigation bar, rail, drawers with the app's header, overflow menu, banner, player and floating
  action button, built from the Toolkit's own buttons and icons.
- Added `:library:feature:developer` with `DeveloperOptionsScreen`, which switches every shell
  variation while the app runs and shows the window size and every back stack.
- Added `toolkitGraph { }` and `toolkitPages()` to `:library:apptoolkit`, which register the
  Toolkit's pages in an app's graph unless the app registers the key itself.
- Added a page registration to each feature: `settingsPage()`, `displaySettingsPage()`,
  `themeSettingsPage()`, `privacySettingsPage()`, `diagnosticsSettingsPage()`,
  `advancedSettingsPage()`, `aboutPages()`, `licensesPage()`, `helpPage()`, `supportPage()`,
  `permissionsPage()`, `adsSettingsPage()`, `developerOptionsPage()` and `onboardingPages()`. The
  settings categories are details of the settings list, so on wide windows they open beside it.
- Added `StartupRoute` and `OnboardingRoute` as start screens: the first launch runs inside the
  app's one activity, chosen in `ShellHost(resolveStart = ...)`.
- Added `PermissionUsageActivity` to `:library:apptoolkit`, declared in its manifest for
  `VIEW_PERMISSION_USAGE` and `VIEW_PERMISSION_USAGE_FOR_PERIOD`: every app gets the information
  icon in Android's permission manager and privacy dashboard, which opens the privacy page over the
  system's settings.
- Added graphs of pages only to `:library:navigation`: a graph without tabs starts on a page and is
  left from it. A page's back arrow now leaves the app when that page is the whole stack, as back
  does.
- Added `SettingsPreference.destination` and `SettingsPreference.action`: a settings row opens a
  key, unless the app's action handles the click first.
- Added `LocalPageSnackbarHostState` and `rememberPageSnackbarHostState()` to `:library:core:ui`, so
  a page's snackbars sit above the shell's bottom chrome.
- Added the `IssueReporterSheet` contract to `:library:core:ui`, bound by
  `:library:feature:issuereporter`, and `ConsentHost(activity)` and `ReviewHost(activity)` builders.
- Added screenshot tests of the shell's chrome to `:library:shell`, recorded with Roborazzi.
- Added a column of floating action buttons to the Toolkit's scaffolds: `ToolkitFab` in
  `:library:core:designsystem` describes one (icon, action, optional label for an extended button,
  Material 3 size, container colour, expanded, visible), and `ToolkitFabColumn` in
  `:library:core:ui` draws a list with the Toolkit's sound, haptics, bounce and animated icons.
  Declare them in the graph with `fabs = { listOf(...) }` on a tab, child or page, or from the
  screen with `ScaffoldFabs(listOf(...))`; `PageScaffold` takes `fabs` too.
- Added `DrawerBuilder.footer { }`, entries that always close the drawer, pinned to its bottom edge,
  and `toolkitFooter(onShowUpdates)` in `:library:apptoolkit`, which puts Settings, Help and
  feedback, Updates and Share there.
- Added `ChoicePreferenceItem` to `:library:core:ui`: a settings row that shows the chosen option
  and opens a radio-list dialog.
- Added a Developer options row to the advanced settings, shown once the About screen's version
  easter egg is found.
- Added the page frame to `:library:core:ui`: `PageScaffold`, `ShellTopAppBar`,
  `LocalContentPadding` and `contentPadding()`, `ContentWidthBox`, `PanePlaceholder` and
  `ListPlaceholder`.
- Added `FabScrollBehavior` to `:library:core:ui`, and an `expanded` parameter to `ToolkitFabColumn`
  and `ToolkitFloatingActionButton`: `PageScaffold` and the shell's tab scaffold fold extended
  buttons to their icon while the content scrolls down and unfold them when it scrolls back.
- Added `placeholder` to `page { }` and `pageIfAbsent { }` in `:library:navigation`: what the detail
  side of a `PaneRole.List` page shows while no detail is open, `ListPlaceholder` when absent.
- Added `LocalBesideNavigation` and `isTopLevelPage(key)` to `:library:navigation`, which tell a
  page whether the rail or permanent drawer opened it.
- Added a weather effect action to the theme settings' app bar, shown once the About screen's easter
  egg is found. Its dialog picks Automatic (snow with the Christmas palette, the default), Snow
  (snow over any palette, all year), Rain (rain over any palette) or Off (the palette without the
  snow): `WeatherEffect`, `SeasonalThemeState.weatherEffect` and
  `SeasonalThemeRepository.setWeatherEffect`, stored under a new `seasonal_weather_effect` key.
- Added `Modifier.rainfall` and `RainfallStyle` to `:library:core:designsystem`: falling rain drawn
  over any element, with density, colors, streak length and thickness, speed, wind, gusts, showers,
  splashes and opacity. Drops fall at different depths, lean with the gusting wind, thicken and thin
  in showers, and splash where they land. Like the snow, it runs in the draw phase only, stops in
  the background, and lets taps through.
- Added a settings search to `:library:feature:settings`: a field above the rows that finds the
  host's rows and the rows of the display, theme, privacy, advanced and About pages, which each
  register them as a `SettingsSearchProvider` (with `SettingsSearchEntry`, in `:library:core:ui`).
- Added `dialogIcon` to `ChoicePreferenceItem`, so a row without an icon still gives its dialog one.
- Added hiding the app bar on scroll, for every style: `ShellSettings.hideTopBarOnScroll`, offered
  in the developer options, and `HideOnScrollTopBar`, `TopBarHideState` and
  `LocalHideTopBarOnScroll` in `:library:core:ui`, which `PageScaffold` and the shell's tab scaffold
  use. A large bar collapses before it slides away.
- Added `ShellCapabilities` to `:library:navigation`, what an app's declared graph and layout policy
  can show (`hasTabs`, `hasMultipleTabs`, `usesBottomNavigation`, `usesWideNavigation`,
  `hasShellTopBars`, `hasContentWidthLimit`, `hasBanner`, `hasPlayer`, `hasAccessories`,
  `hasMultipleStartOptions`, `hasBackNavigation`), built with `ShellCapabilities.of(graph, policy)`
  and provided by `ShellHost` as `LocalShellCapabilities`, and `ShellLayoutPolicy.reachesBottomBar`
  and `reachesWideNavigation`. They describe the app as declared, never the window drawn now or the
  developer options, so settings decide which rows mean something in the app from one set of rules.
- Added `DisplaySettingsProvider.startupPageChoices`: how many places the host's startup page dialog
  offers. The startup page row shows only with more than one; while it is null, as before, with more
  than one tab.
- Added `AboutRoute`, `ThemeSettingsRoute`, `DisplaySettingsRoute`, `PrivacySettingsRoute`,
  `AdvancedSettingsRoute`, `DiagnosticsSettingsRoute`, `DeveloperOptionsRoute`, `StartupRoute` and
  `OnboardingRoute`, one key per Toolkit page.
- Added `FrameScrollTint`, `FrameTint`, `FollowScrollWithFrameTint` and
  `BesideNavigation.scrollTint` to `:library:navigation`, and `TopAppBarScrollBehavior.frameTint` to
  `:library:core:ui`: while the navigation colour follows scrolling, the app bar on top drives it, a
  page's as well as a tab's.
- Added `ScaffoldSnackbars`, `LocalScaffoldSnackbars` and `rememberScaffoldSnackbars()` to
  `:library:core:ui`: the shell's tab scaffold now draws snackbars too, as `PageScaffold` does, and
  a screen shows one through the scaffold around it with `post` or `show`, in the normal or error
  style (`ToolkitSnackbarStyle`), with an action, an icon, its own `ToolkitSnackbarColors`, or drawn
  by the screen through `content`. `ToolkitSnackbar` and `ToolkitSnackbarDefaults` draw the default
  look.
- Added `UiSnackbar.actionLabel` and `DefaultSnackbarHandler(getActionEvent, drawHost)`, and
  `DefaultSnackbarHandler` now uses the scaffold's host when given none.
- Added `ShellHost(snackbarHostState)`: an app shows its own messages, such as its activity's view
  model's, in the tabs' scaffold with every other snackbar, above the bottom bar, its buttons and
  the player, on any window size.
- Added `besideNavigationTitle()` and `LocalBesideNavigationTransitions` to `:library:navigation`,
  and `titleModifier` to `ShellTopAppBar`: beside a rail or drawer, the title of the tab's bar and
  of the page standing in for it grow or shrink into one another as one replaces the other in place.
- Added
  `ThemePreferencesDataSource.savePalette(dynamicColors, dynamicPaletteVariant, staticPaletteId)`,
  which stores a palette choice in one write. It has a default implementation, so other
  implementations of the interface keep compiling.
- Added `ThemePreferencesDataSource.storedPreferences`, every theme value read from one snapshot of
  storage, which `themePreferencesState()` now follows.
- Added `UsageAndDiagnosticsPreferencesDataSource.saveAll(...)` and
  `UsageAndDiagnosticsRepository.setAll(settings)`, which store a whole consent answer in one write.
  Both have default implementations.
- Added a `viewModelName` parameter to `LoggedScreenViewModel`, defaulting to `screenName`, which
  sets the name its breadcrumbs and `vm_op_*` events report.
- Added the `core.ui.screen` package to `:library:core:ui`, the new way a screen holds and shows its
  state: `ScreenViewModel<S, E>` and `LoggedScreenViewModel<S, E>` over the feature's own state
  type, changed with `setState`; `Loadable` for each piece of content that loads (`Loading`, `Ready`
  with `refreshing` and `stale`, `Empty`, `Failed`); `TrackedStatus` for statuses a screen declares
  itself; queued `UiMessage`s shown by `MessageHost`; and `ScreenStateHandler` and
  `TrackScreenState` for a `Loadable`. `LoggedScreenViewModel` sends the same breadcrumbs, events
  and Crashlytics reports as before. The `core.ui.base` and `core.ui.states` classes stay until
  every feature has moved. See the [`:library:core:ui` README](core/ui/README.md#screen-state).
- Added a `message` parameter to `NoDataScreen`, for a message that is not a string resource.
- Added `NetworkException` and `StorageException` to `:library:core:common`, the failures a
  repository throws, each with a `reason`. `networkCall { }` in `:library:core:network` and
  `storageCall { }` in `:library:core:datastore` translate the HTTP client's and the storage
  library's exceptions into them, keeping the original as the `cause`. `toError()` recognizes both,
  so code that still reads `Errors` keeps working.
- Added `collectReport(action, extra, onError) { value -> }` to
  `core.ui.screen.LoggedScreenViewModel`, the flow counterpart of `launchReport`: it logs the start
  once, passes every value on, and reports a failure before handing it to `onError`.
- Added `ClipboardRepository` and `DefaultClipboardRepository` to `:library:core:common`, bound in
  the foundation modules: a ViewModel copies text through it instead of holding a `Context`, and
  reads `confirmsCopies` instead of checking the Android version. `copyText` throws when the
  clipboard rejects the write.
- Added `toUiText(fallback)`, `toFailed(fallback)`, `toErrorMessage(fallback)` and `isRetryable` to
  `core.ui.screen`: the text a screen shows for a failure, with the same text everywhere for being
  offline, a timeout, a busy server, rate limiting and busy or full storage, and the screen's own
  fallback for anything else.
- Added `ThemeModePicker` and `ThemePalettePicker` to `core.ui.views.theme`: the theme mode cards
  and the wallpaper and static palette choice that the theme settings page and the onboarding theme
  page now both show. The palette picker offers the holiday palettes all year once the seasonal
  themes are unlocked, and opens each row scrolled to its selection.
- Added `observeReport(action, extra, onError) { value -> }` to
  `core.ui.screen.LoggedScreenViewModel`, for a stream that is expensive to keep running, such as
  location, sensors or a socket: it collects only while the screen collects `state`, stops five
  seconds after the screen leaves and starts over when it returns, the policy of
  `SharingStarted.WhileSubscribed(5_000)`. `ScreenViewModel.launchWhileSubscribed { }` exposes the
  same policy for any work, and `collectInBackground(flow)` in `:library:core:testing` subscribes a
  test the way a screen does. The Toolkit's own streams stay on `collectReport`, which collects for
  the ViewModel's lifetime.
- Added an optional article app bar, as news apps draw over a story: a screen calls
  `ScaffoldArticleTopBar(title, compact, progress, brand, brandContentDescription)` from
  `:library:navigation`, and the bar of the scaffold around it shows only its buttons until
  `compact`, then the title after an optional untinted `brand` painter, with a reading progress line
  along its bottom edge that keeps the bar's height. It works in `PageScaffold`, in the shell's tabs
  and children, and in each pane of a list and its detail; a large bar is drawn small while it is
  declared, a hidden bar stays hidden, and a search field keeps the title's place.
  `ArticleTopBarHost`, `LocalArticleTopBarHost`, `ArticleTopBarTitle`, `ArticleReadingProgress` and
  `readingProgress()` for `ScrollState` and `LazyListState` come with it, and `ShellTopAppBar` takes
  the host as a new optional `article` parameter. The components showcase in the sample opens a demo
  with and without a brand.
- Added `settingsSearchProvider(section, destination) { preference(title, summary, destination) }`
  and `SettingsSearchScope` to `:library:core:ui`, which declare a settings page's rows for the
  settings search with the page's section and destination given once, a row naming its own
  destination where it opens another page, and plain Kotlin conditions over the app's `capabilities`
  and `graph`. A provider's one input is a `SettingsSearchContext`, the app's `ShellGraph` and
  `ShellCapabilities` from the same `ShellHost` locals the settings pages read, so a page and its
  search rows follow the same rules; `SettingsSearchProvider { context -> ... }` receives the same
  context. `SettingsSearchProvider.unregisteredDestinations(context)` lets an app's tests check that
  every result opens a page its graph registers.
  The [settings README](feature/settings/README.md#settings-search) shows an app's own page
  declared, bound and checked end to end.
- Added report checks to `:library:feature:issuereporter`, against people filing empty or unrelated
  reports. `ValidateIssueReportUseCase` returns an `IssueReportValidation` holding an
  `IssueReportFieldError` for each field that cannot be filed: empty, too short or too long, too few
  letters or digits (counted in any script), one run of characters repeated, a link alone, a
  description repeating the title, or an invalid email. `IssueReportHistoryRepository`, kept in the
  app's DataStore, holds any report back for five minutes after the last one and refuses the same
  report for a day (`IssueReportRefusal`); it stores only a hash of each report and the time it was
  filed.

### Changed

- `BottomAppBarNativeAdBanner` is disabled by default. Hosts must explicitly pass `enabled = true`;
  `NoDataScreen` keeps its existing ad default. Review the placement warning and Google guidance in
  the [ads documentation](integration/ads/README.md#placement-warning-and-explicit-opt-in) before
  opting in.
- `ConsentRepository.requestConsent(host, showIfRequired)` suspends until completion and throws
  `ConsentException` on failure. Replace result-flow collection with a direct suspend call; loading
  belongs to the caller's UI state.
- Composables report through the new `LocalTelemetry` instead of a `firebaseController` parameter.
  `TrackScreenView`, `TrackScreenState`, the buttons, chips, text fields, dropdown items, FABs and
  every preference row read it themselves, so their `firebaseController` parameter is removed; pass
  only the `ga4Event`. `ShellHost` provides it, and `ProvideTelemetry { }` provides it to a
  composition outside `ShellHost`. Where nothing does, as in a preview, it is the new
  `NoOpTelemetryRepository`, so previews need no fake.
- Renamed `FirebaseController` to `TelemetryRepository`, in the same package, so the contract names
  no vendor; the SDKs behind it are its data sources. Its implementation `DefaultFirebaseController`
  is now `FirebaseTelemetryRepository`, and the test fake `FakeFirebaseController` is
  `FakeTelemetryRepository`. Every `firebaseController` parameter of a ViewModel, repository or
  other class is now `telemetryRepository`, so named arguments need the new name.
  `FirebaseController`, `DefaultFirebaseController` and `FirebaseControllerImpl` remain in their old
  packages as deprecated aliases. The [3.0.0 migration guide](../docs/migration/3.0.0.md#telemetry)
  has the mapping.
- Every `AppToolkitNavKey` is now a plain `@Serializable` `NavKey`, no longer `Parcelable` or a
  `StableNavKey`. `:library:navigation` exposes `kotlinx-serialization-core` as `api`.
- `:library:apptoolkit` now exposes `:library:shell`, `:library:feature:developer`,
  `:library:feature:advanced`, `:library:feature:diagnostics`, `:library:feature:display` and
  `:library:feature:theme` through `api`.
- Every Toolkit screen is now a page of the shell instead of an activity. Screens pad by
  `contentPadding()` and lost their `isEmbedded` and navigation callback parameters:
  `LicensesScreen()`, `FaqScreen()`, `SupportScreen()`, `PermissionsScreen()`, `AdsSettingsScreen()`
  and `LibraryExtrasScreen()` take none.
- Features open one another's pages by key and no longer depend on each other: the privacy page
  navigates to the permissions, ads and diagnostics keys; the display settings open
  `ThemeSettingsRoute`; About and Help open `LicensesRoute`; the advanced settings resolve
  `IssueReporterSheet` from Koin and hide the bug report when it is absent.
- `PrivacySettingsProvider` supplies only the legal links, and `DisplaySettingsProvider` no longer
  has `openThemeSettings`.
- Moved `SettingsConfig`, `SettingsCategory` and `SettingsPreference` to `:library:core:ui`
  (`core.ui.models.settings`).
- Moved `FirebaseOnboardingPage` and its strings from `:library:feature:onboarding` to
  `:library:feature:diagnostics`.
- `StartupProvider` supplies only `requiredPermissions`, and `OnboardingProvider` only the pages;
  finishing onboarding enters the shell.
- Beside a rail, an expanded rail or a permanent drawer, pages now open in the space next to the
  navigation instead of covering it: the navigation stays, marks the entry whose page is open, and a
  tab or another entry replaces the open pages. `ShellHost` draws that navigation itself, around its
  displays.
- The display settings keep only the person's own preferences: dark theme and dynamic colors, bounce
  buttons, the startup page, the navigation labels and the language. Every shell override is a
  developer option: the forced layout, app bar style, hiding the app bar and the bottom bar on
  scroll, navigation bar style, navigation tint, content width, where the app starts, tab
  transition, back swipe, animation speed and bottom accessories, under Layout and bars, Navigation,
  Motion and Accessories. `ShellPreferences.resetDeveloperOptions()` resets every `ShellSettings`
  value and none of the person's preferences, and the stored keys are unchanged.
  `:library:feature:display` no longer depends on `:library:shell`.
- Display rows and developer options show only where they act on something in the app, from
  `ShellCapabilities`: the navigation labels need more than one tab in a bottom bar the layout
  policy reaches, the startup page more than one choice, the tab transition more than one tab, the
  app bar overrides an app bar the shell draws, and the bottom accessory a declared banner or
  player. The display page and its settings search rows come from one list, `displayRows`, so the
  startup page is now searchable and the labels row is no longer offered with one tab, where the bar
  always labels its tab.
- A page the rail or permanent drawer opens is drawn like a tab: a small app bar without a back
  button, and, when the navigation and the app bar share a colour, the tab's rounded content card.
  `PageScaffold` and the list-detail scene both do this.
- The settings list shows its illustrated card, with a Get help button, beside it on wide windows
  while no category is open, instead of the generic placeholder.
- `ChangelogDialog` draws the Toolkit's wavy line between releases, where the Markdown has a rule
  (`splitAtThematicBreaks`), instead of the Markdown renderer's flat line.
- Moved the startup screen out of `:library:feature:onboarding` into a new
  `:library:feature:startup`: `StartupProvider`, `StartupScreen`, `StartupViewModel` and their
  contracts, now under `feature.startup`, with `startupPage()` registering `StartupRoute` and
  `startupModule(startupProviderFactory)` binding them. `onboardingPages()` registers only
  `OnboardingRoute`, and `onboardingModule` is a value without parameters. `toolkitPages()` and
  `appToolkitModules` include both, and `:library:apptoolkit` exposes the new module.
- Moved single-owner code out of `:library:core:common` into the module that uses it: `FaqConstants`
  and `faqCatalogUrl` to `:library:feature:faq`, `extractChangesForVersion` to
  `:library:feature:changelog`, `GithubToken` and `toToken` to `:library:feature:issuereporter`,
  `PurchaseResult` and `isValidForBilling` to `:library:integration:billing`,
  `OnShowAdCompleteListener` to `:library:integration:ads`, and `installingPackageNameOrNull`,
  `hasPlayStore` and `isInstalledFromPlayStore` to `:library:integration:review`.
  The [3.0.0 migration guide](../docs/migration/3.0.0.md#moved-apis) lists the new packages.
- The display settings rows, the developer options rows and the advanced settings' Developer options
  row no longer draw leading icons, matching the other settings rows. `ChoicePreferenceItem`'s
  `icon` moved after `modifier` and defaults to none.
- The content width override shows only when the app's `ShellLayoutPolicy` sets a maximum width, and
  the developer options offer the player accessory only to an app with a player.
- A page beside a rail or a permanent drawer keeps square start corners
  (`rememberDeviceCornerShape(squareStart = true)`), so its app bar is no longer rounded next to the
  rail on phones with rounded screens, in landscape.
- `PageSurface`, `PageScaffold`, the list-detail scene and the shell's tab scaffold now provide
  `LocalContentColor` for the colour they draw on, so text and icons outside a Material `Surface`
  follow the theme instead of defaulting to black in the dark theme.
- `DisplaySettingsViewModel`, `ThemeSettingsViewModel` and `OnboardingThemeViewModel` take a
  `TelemetryRepository`, and a failed preference write is reported and shows an error message
  instead of being dropped. Their Koin modules pass it already; code that builds them directly must
  pass one.
- `LoggedScreenViewModel` no longer reads names from classes, which R8 renames in release builds.
  The `view_model` breadcrumb key and GA4 parameter use `viewModelName`, which every Toolkit
  ViewModel sets to its class name, so the reported values stay the same. The event breadcrumb uses
  the event's source name.
- `FirebaseTelemetryRepository.reportViewModelError` and `recordNonFatal` attach their keys to that
  one report instead of setting them with `setCustomKey`, so a later, unrelated crash no longer
  carries them.
- `SupportEvent.QueryProductDetails` is a `data object` without the unused `BillingClient`
  parameter.
- `Throwable.toError()` maps `IllegalStateException` to `Errors.UseCase.INVALID_STATE` instead of
  `NO_DATA`.
- `KtorClient.createClient` builds a new client on every call instead of caching the first, whose
  logging setting later calls ignored. Debug logging prints headers with `Authorization` masked, and
  no longer prints bodies.
- `DefaultUsageAndDiagnosticsRepository` takes a `ConsentRepository` and applies the stored choices
  to the consent SDKs after every write, and `UsageAndDiagnosticsViewModel` no longer takes one or
  applies consent itself. A change now reaches the SDKs whether or not the diagnostics screen is
  still open; opening the screen no longer re-applies consent, which the host applies at startup
  with `ConsentRepository.applyInitialConsent()`.
- `DefaultAboutRepository` and `DefaultCacheRepository` take a `DispatcherProvider`, defaulting to
  `StandardDispatchers()`, and run their package manager lookup and cache delete on IO themselves,
  so both are safe to call from the main thread.
- `:library:feature:about` is the first feature on `core.ui.screen`. `AboutViewModel` no longer
  takes a `DispatcherProvider`, a `Context` or an `sdkIntProvider`; it takes a
  `ClipboardRepository`, exposes `state` (an `AboutUiState` whose `items` is a `Loadable`) and
  queued `messages`, and `AboutEvent.DismissSnackbar` and `AboutAction` are removed. `AboutScreen`
  now only wires the ViewModel, tracking, messages and navigation, and the list renders in a
  stateless, previewable `AboutScreenContent`. Its load and copy failures go through `toFailed` and
  `toErrorMessage`, so a storage or network failure shows its own text and a failure that would
  repeat offers no Retry.
- `:library:feature:settings` is on `core.ui.screen`. `SettingsViewModel` no longer takes a
  `DispatcherProvider` and exposes `state`, a `SettingsUiState` whose `config` is a `Loadable`: a
  provider with no category is `Loadable.Empty`, whose "No settings found" the empty page now shows
  (the old snackbar error was never displayed), and a provider that throws is `Loadable.Failed`.
  `SettingsAction` is removed, and `SettingsEvent` no longer extends `UiEvent`. `SettingsScreen`
  wires the ViewModel, the search query and index, tracking and navigation, and the list renders in
  the stateless `SettingsScreenContent`. Both live in `SettingsScreen.kt`, and `SettingsList` keeps
  its signature.
- `:library:feature:permissions` is on `core.ui.screen`.
  `PermissionsRepository.getPermissionsConfig()` returns the `SettingsConfig` instead of a `Flow`,
  and `DefaultPermissionsRepository` and `PermissionsViewModel` no longer take a
  `DispatcherProvider`, since reading string resources is main-safe. `PermissionsViewModel` exposes
  `state`, a `PermissionsUiState` whose `config` is a `Loadable`. `PermissionsAction` is removed,
  and `PermissionsEvent` no longer extends `UiEvent`. `PermissionsScreen` wires the ViewModel and
  tracking, and the catalog renders in the stateless `PermissionsScreenContent`. Both live in
  `PermissionsScreen.kt`, and `PermissionsContent` keeps its signature.
- `:library:feature:licenses` is on `core.ui.screen`. `LicensesViewModel` exposes `state`, an
  `LicensesUiState` whose `libraryCount` is a `Loadable`, and `LicensesAction` is removed.
  `LicensesScreen` wires the ViewModel, the metadata producer and tracking, and the list renders in
  the stateless `LicensesScreenContent`.
- `:library:feature:changelog` is on `core.ui.screen`.
  `ChangelogRepository.fetchChangelog(): Flow<DataState<…>>` is now
  `suspend fun getChangelog(packageName): String`, which throws a `NetworkException`, and
  `GetChangelogUseCase` is a `suspend` call returning the Markdown. `ChangelogViewModel` no longer
  takes a `DispatcherProvider` and exposes `state`, an `ChangelogUiState` whose `markdown` is a
  `Loadable`. `ChangelogEvent.Retry` is merged into `ChangelogEvent.Load`, and `ChangelogAction` is
  removed. `ChangelogDialog` moved from `feature.changelog.ui.views.dialogs` to
  `feature.changelog.ui`, and the sheet's body renders in the stateless `ChangelogDialogContent`. An
  offline device now sees the offline text, and a failure a retry cannot fix offers Done instead of
  Try again.
- `:library:feature:faq` is on `core.ui.screen`. `FaqRepository.fetchFaq(): Flow<DataState<…>>` is
  now `suspend fun getFaq(): List<FaqItem>`, which throws a `NetworkException` when the catalog
  failed and there are no bundled questions. `FaqViewModel` no longer takes a `DispatcherProvider`
  and exposes `state`, an `FaqUiState` whose `questions` is a `Loadable`, with `openStoreListing`
  for the review fallback. `FaqEvent.LoadFaq` is `FaqEvent.Load`, `DismissSnackbar` and
  `OpenFeatureRequestForm` are removed (the screen opens the form itself), `StoreListingOpened` is
  added, and `FaqAction` is removed. `FaqScreenContent` moved from `ui.views.content` into
  `FaqScreen.kt` and is internal and stateless; `FaqScreen` logs the same GA4 events as before.
- `:library:feature:advanced` is on `core.ui.screen`.
  `CacheRepository.clearCache(): Flow<DataState<…>>` is now a `suspend` function that throws a
  `StorageException`, and `AdvancedSettingsViewModel` no longer takes a `DispatcherProvider`. It
  exposes `state`, an `AdvancedSettingsUiState` with a `CacheClearStatus` and
  `developerOptionsUnlocked` (replacing the separate `developerOptionsUnlocked` flow), and queued
  `messages`. `AdvancedSettingsEvent.MessageShown` and `AdvancedSettingsAction` are removed, and
  `AdvancedSettingsEvent` no longer extends `UiEvent`. Clearing the cache confirms with a snackbar
  instead of a toast. `AdvancedSettingsScreen` no longer takes `paddingValues`, and the rows render
  in the stateless `AdvancedSettingsScreenContent` in the same file.
- `:library:feature:privacy` is on `core.ui.screen`. `PrivacyViewModel` exposes `state`, a
  `PrivacyUiState` whose `items` is a `Loadable`. `PrivacyAction` is removed: `PrivacyScreen` opens
  a row's link and navigates to the permissions, ads and diagnostics keys itself. `PrivacyEvent` no
  longer extends `UiEvent`. `PrivacyScreen` no longer takes `paddingValues`, and the list renders in
  the stateless `PrivacyScreenContent` in the same file.
- `:library:feature:diagnostics` is on `core.ui.screen`. `UsageAndDiagnosticsViewModel` no longer
  takes a `DispatcherProvider` and exposes `state`, a `UsageAndDiagnosticsUiState` whose `settings`
  is a `Loadable<UsageAndDiagnosticsSettings>`, and queued `messages`.
  `UsageAndDiagnosticsEvent.Initialize` is `Load`, `UsageAndDiagnosticsEvent` no longer extends
  `UiEvent`, and `UsageAndDiagnosticsAction` is removed. `FirebaseConsentDialog` and `DetailsPage`
  take `settings: UsageAndDiagnosticsSettings` instead of the UI state. `UsageAndDiagnosticsScreen`
  no longer takes `paddingValues`, and the page renders in the stateless
  `UsageAndDiagnosticsScreenContent` in the same file. `FirebaseOnboardingPage` keeps its signature
  and renders in `FirebaseOnboardingPageContent`.
- `:library:feature:display` is on `core.ui.screen`. `DisplaySettingsViewModel` is a
  `LoggedScreenViewModel` and exposes `state`, a `DisplaySettingsUiState` whose `settings` is a
  `Loadable` of the new `DisplaySettings`, which carries the start page route, and queued
  `messages`. `DisplaySettingsViewModel.startupRoute(defaultRoute)` is removed,
  `DisplaySettingsEvent.Initialize` is `Load`, and `DisplaySettingsEvent` no longer extends
  `UiEvent`. Each preference write is reported as its own operation (`setThemeMode`,
  `setDynamicColors`, `setBouncyButtons`, `setShowBottomBarLabels`, `setLanguage`,
  `setStartupPage`). `DisplaySettingsScreen` no longer takes `paddingValues`, and the list renders
  in the stateless `DisplaySettingsScreenContent` in the same file. GA4 events are unchanged.
- `:library:feature:theme` is on `core.ui.screen`. `ThemeSettingsViewModel` and
  `SeasonalThemeOverlayViewModel` extend `LoggedScreenViewModel` and expose `state` instead of
  `uiState`. `ThemeSettingsUiState` is a data class whose `preferences` is a
  `Loadable<ThemePreferencesState>`. `ThemeSettingsEvent.Initialize` is `Load`, and
  `ThemeSettingsEvent` and `SeasonalThemeOverlayEvent` no longer extend `UiEvent`. A preference
  write is the `persistThemeSetting` operation, with the setting in its `setting` parameter.
  `ThemeSettingsScreen` no longer takes `paddingValues`, and the page renders in the stateless
  `ThemeSettingsScreenContent` in the same file. The palette pager's pages and the theme mode and
  tab lists are no longer rebuilt on every recomposition.
- `:library:feature:support` is on `core.ui.screen`. `SupportViewModel` exposes `state`, a
  `SupportUiState` whose `donationOptions` is a `Loadable` of `DonationOption` (formerly
  `DonationOptionUiState`, now in `feature.support.ui.models`), and queued `messages`.
  `SupportScreenUiState` is `SupportUiState`, without its `error` field. `SupportAction`,
  `SupportEvent.SetUpBilling`, `SupportEvent.DismissSnackbar` and `setupBilling()` are removed,
  `SupportEvent` no longer extends `UiEvent`, and a donation is
  `SupportEvent.Donate(productId, activity)` instead of `onDonateClicked`. The page renders in the
  stateless, now internal `SupportScreenContent` in `SupportScreen.kt`, and the screen logs the same
  GA4 events as before.
- `:library:feature:onboarding` is on `core.ui.screen`. `OnboardingViewModel` no longer takes a
  `DispatcherProvider`; it takes a `ConsentRepository` and asks for consent itself with the
  `ConsentHost` in `OnboardingEvent.RequestConsent(host)`. It exposes `state`, an
  `OnboardingUiState` whose `completion` is an `OnboardingCompletion`, and queued `messages`.
  `OnboardingEvent.UpdateCurrentTab` is `PageSelected`, and `ObserveCompletion`, `DismissSnackbar`
  and `OnboardingAction` are removed. `OnboardingScreen` enters the shell once completion is saved,
  and the pager renders in the stateless `OnboardingScreenContent` in the same file. Pages show
  their messages through the onboarding screen's snackbar host. `OnboardingThemeViewModel` is a
  `LoggedScreenViewModel` exposing `state`, an `OnboardingThemeUiState`, and
  `OnboardingThemeEvent.Initialize` is removed.
  `DefaultOnboardingRepository.setOnboardingCompleted()` throws a `StorageException` when the write
  fails.
- `:library:feature:startup` is on `core.ui.screen`. `StartupViewModel` takes a `ConsentRepository`
  and asks for consent itself with the `ConsentHost` in `StartupEvent.RequestConsent(host)`,
  settling on any answer, on a failure or after 15 seconds as before. It exposes `state`, a
  `StartupUiState` whose `consent` is a `ConsentRequestStatus`. `StartupEvent.ConsentFormLoaded`,
  `StartupEvent.Continue` and `StartupAction` are removed. `StartupScreen()` takes no parameters and
  owns the permission request, consent and navigation, and its body renders in the stateless
  `StartupScreenContent` in the same file.
- `DefaultUsageAndDiagnosticsRepository` no longer takes a `DispatcherProvider`, since DataStore is
  main-safe, and its reads and writes fail with a `StorageException`. `AdsCoreManager` reads the ads
  preferences without a dispatcher switch and still initializes the Mobile Ads SDK on IO, and
  `DefaultBillingRepository` no longer switches dispatchers around its product and purchase queries,
  whose callbacks Play Billing already delivers asynchronously.
- Feature modules no longer keep their own copies of strings `:library:core:ui` provides, and read
  them from `core.ui.R` instead: the theme mode, palette and AMOLED labels in
  `:library:feature:theme` and `:library:feature:onboarding`, and `done_button_content_description`,
  `go_back`, `content_description_more_options`, `try_again`, `support_us`, `learn_more` and
  `startup_page` in About, Diagnostics, Startup, Display, Support and the ads integration. An app
  that overrode one of those copies by name overrides the `:library:core:ui` string instead.
- The onboarding theme page uses the same theme mode and palette pickers as the theme settings page
  instead of its own copy, so it now offers the holiday palettes all year once they are unlocked and
  opens on the selected palette. `OnboardingThemeViewModel` takes a `SeasonalThemeRepository`, and
  `OnboardingThemeUiState` gains `seasonalThemesUnlocked`. `OnboardingThemeChoice` is removed.
- `PermissionsViewModel` loads the permission catalog once, when it is created, instead of each time
  the page is shown; the catalog does not change while the app runs.
- `:library:integration:ads`'s settings page is on `core.ui.screen`. `AdsSettingsRepository` reads
  fail with a `StorageException` from their flows and `setAdsEnabled` and `setReduceAds` throw one
  instead of returning `DataState`, and `defaultAdsEnabled` is gone from the contract: the store's
  default applies. `AdsSettingsViewModel` no longer takes a `DispatcherProvider` and exposes
  `state`, an `AdsSettingsUiState` whose `preferences` is a `Loadable<AdsPreferences>`, and queued
  `messages`; a failed write keeps the switch where it was and shows an error message.
  `AdsSettingsAction` is removed, and `AdsSettingsEvent` no longer extends `UiEvent`. The page
  renders in the stateless `AdsSettingsScreenContent` in `AdsSettingsScreen.kt`.
- `:library:feature:issuereporter` is on `core.ui.screen`. `IssueReporterRepository.sendReport`
  returns the created issue's URL instead of an `IssueReportResult`, and throws an
  `IssueReportRejectedException` (with a `reason`) for GitHub's 401, 403, 410 and 422 answers, or a
  `NetworkException` for any other failure. `DefaultIssueReporterRepository` and
  `IssueReporterViewModel` no longer take a `DispatcherProvider`, and the ViewModel calls the
  repository directly. `IssueReporterViewModel` exposes `state`, an `IssueReporterUiState` whose
  `deviceInfo` is a `Loadable`, and queued `messages`. `IssueSubmissionState` gains `Failed`.
  `IssueReporterEvent` no longer extends `UiEvent`, and `DismissSnackbar` is removed. The sheet
  renders in the stateless `IssueReporterBottomSheetContent` in `IssueReporterBottomSheet.kt`, and
  logs the same GA4 events as before.
- The display, theme, privacy, About and advanced pages declare their settings search rows with
  `settingsSearchProvider`. The theme, privacy, About and advanced rows are as before; the display
  rows are those `displayRows` gives the page.
- The issue reporter requires an email, a title of 10 to 120 characters and a description of 40 to
  5,000 characters, and trims all three before filing. The sheet looks as before and Send stays
  enabled: an invalid field shows its error beneath it, and a report held back by the cooldown or as
  a duplicate shows a message. `IssueReporterViewModel` takes an `IssueReportHistoryRepository` and
  a `ValidateIssueReportUseCase`, and `IssueReporterUiState` gains `fieldErrors`. The `send_issue`
  event is now logged by the ViewModel, once per attempt, with `validation_failure` (such as
  `title_too_short`, `email_invalid`, `cooldown`, `duplicate`, or `none` for a report handed to
  GitHub) in place of `has_email`; no report text or email is ever logged. The
  `issue_email_optional_placeholder` and `error_invalid_report` strings are removed.
- Shake-to-report needs a deliberate, repeated shake: `ShakeDetector` counts distinct shakes and by
  default asks for six, three back-and-forth movements, over at least 800 ms, instead of four
  readings over 300 ms. The new `minimumShakes` parameter sets the count.

### Improved

- `AnimatedIconButtonDirection` crossfades its glyph when `icon` changes, so one button can turn
  from a menu button into a back arrow without popping.
- A navigation no longer recomposes every tab screen: the shell keeps its callbacks and the static
  `LocalShellChrome` controller across recompositions. The chrome and the player overlay no longer
  recompose on every frame the player moves.
- A page beside the rail or permanent drawer keeps its tab-like app bar while another entry replaces
  it, instead of showing a back button for the length of its exit.
- Beside a rail or a permanent drawer, the tabs and the page the navigation opened replace one
  another with a fade in place (`TabTransitions.inPlace(back)`), instead of the page sliding in like
  a new window under a navigation already on screen. Only the one on top fades, over one that stays
  opaque, so the display's grey backdrop no longer flashes between them.
- The expanded rail starts collapsed and collapses again when the layout changes, such as on
  rotation.
- A back swipe from the detail's own edge of a list and detail shrinks the detail in place,
  following the finger, instead of sliding the separator away from it.
- Switching between two tabs that both search keeps the search field in place instead of fading it
  out and in.
- The startup and onboarding pages keep to a 640dp column centred on tablets and wide windows
  instead of stretching across them.
- Improved the shell's performance without changing how it looks or moves: the list-detail
  separator, its back gesture and the hiding bottom bar no longer recompose the panes or the mini
  player on each frame; the rail and the permanent drawer no longer recompose on every navigation; a
  screen's floating action buttons redraw only when they change, and their hosts leave with their
  screens; the settings search builds its index on the first search instead of when the settings
  open.
- Improved snowfall and rainfall, which now share one frame node (`ParticleEffectNode`): the frame
  loop is cancelled on detach and requests no frames while there is nothing to move, and the rain's
  gusts and showers stay smooth however long it falls.
- The shell's player no longer recomposes itself or the app's player content on each frame while it
  is dragged, expands or collapses: its size, corners and shadow follow the drag in layout and
  drawing.
- Changing the bouncy buttons, bottom bar labels or ads setting now recomposes only the parts that
  read it (`LocalBouncyAnimationsEnabled`, `LocalShowBottomBarLabels`, `LocalAdsEnabled`), instead
  of everything under `AppTheme`.
- Choosing a palette, and the holiday theme applying or restoring itself, now store dynamic colors
  and the palette in one write, so a process death in between can no longer keep only half the
  choice. `AppTheme` also receives that write as one state, so the app no longer flashes the wrong
  palette in between.

### Removed

- Removed the old navigation from `:library:navigation`: the `animations`, `backstack`, `data`,
  `models` and `ui` packages (`BottomNavigationBar`, `LeftNavigationRail`, `NavigationDrawerSheet`,
  `NavigationDrawerHeader`, `HideOnScrollBottomBar`, `BottomBarItem`, `NavigationDrawerItem`,
  `StableNavKey`, `NavigationRepository` and the back-stack helpers) and `NavigationDrawerRoutes`.
  Use `shellGraph { }` or `toolkitGraph { }` with `ShellHost`.
- Removed `:library:core:ui`'s `navigation` package (`NavigationState`, `NavigationEntryBuilder`,
  `NavigationAnimations`) and `startupDestinationFlow`. Use `ShellNavigator`, and `startupValueFlow`
  from `:library:core:datastore`.
- Removed `appToolkitNavigationEntryBuilders`, replaced by `toolkitGraph { }`, and
  `handleNavigationItemClick` with the `DefaultNavigationRepository` binding from
  `:library:feature:about`; drawer entries are declared in the graph.
- Removed the Toolkit's activities and their manifest entries: `SettingsActivity`,
  `GeneralSettingsActivity`, `FaqActivity`, `SupportActivity`, `PermissionsActivity`,
  `AdsSettingsActivity`, `LicensesActivity`, `StartupActivity` and `OnboardingActivity`. Navigate to
  their keys instead.
- Removed `GeneralSettingsRoute`, `GeneralSettingsScreen`, `GeneralSettingsContentProvider`,
  `GeneralSettingsRepository` and `SettingsContent`; each settings category is its own page.
- Removed `BaseActivity` from `:library:core:ui`, `Context.openActivity` from
  `:library:core:common`, and `GmsHostFactory` from `:library:feature:about`.
- Removed the dependencies between feature modules, and `ALLOWED_LIBRARY_FEATURE_EDGES` with them:
  `checkModuleBoundaries` now rejects every feature-to-feature edge.
- Removed the previous screen contracts, now that every Toolkit feature and the sample are on
  `core.ui.screen`: `core.ui.base` (`BaseViewModel`, its `ScreenViewModel` and
  `LoggedScreenViewModel`, `UiEvent`, `ActionEvent`, `UiState`), `core.ui.states` (`UiStateScreen`,
  `ScreenState`, `UiSnackbar` and their helpers), `core.ui.views.layouts.ScreenStateHandler` and
  `TrackScreenState` (the `UiStateScreen` versions), `DefaultSnackbarHandler`,
  `ComponentActivity.observeActions`, and `ScreenMessageType` and `ScreenDataStatus` from
  `:library:core:common`. Use `core.ui.screen` and `MessageHost`.
- Removed `DataState`, `Error`, `Errors`, `Throwable.toError()` and `Errors.asUiText()` from
  `:library:core:network`, with its error strings. Repositories throw `NetworkException` or
  `StorageException`, and screens map them with `toUiText`, `toFailed` and `toErrorMessage`.
- Removed `SendIssueReportUseCase`, `IssueReportResult`, `IssueReporterAction` and the public
  `IssueReporterContent` from `:library:feature:issuereporter`. Hosts show
  `IssueReporterBottomSheet` or call `IssueReporterLauncher.show`.
- Removed the `XAction` contracts of the features moved to `core.ui.screen`:
  `AdvancedSettingsAction`, `PrivacyAction`, `UsageAndDiagnosticsAction`, `SupportAction`,
  `OnboardingAction` and `StartupAction`, with `AdvancedSettingsEvent.MessageShown`,
  `SupportEvent.DismissSnackbar` and `OnboardingEvent.DismissSnackbar`.
- Removed the banner style setting: `BannerStyle`, `ShellSettings.bannerStyle`,
  `ShellPreferences.setBannerStyle` and its display settings row. The banner now shows only docked
  on the bottom navigation bar, and not at all beside a rail or drawer. A value already stored under
  `banner_style` is ignored.

### Fixed

- Fixed the advanced settings' Developer options row sitting alone without a title and with its own
  spacing: it is in a Developer group, titled and spaced like the other groups.
- Fixed the theme settings and the holiday greeting showing English in Vietnamese and Traditional
  Chinese: `:library:feature:theme` now has both translations, like every other module.
- The Help native ad keeps its advertiser icon centred inside the shaped badge instead of covering
  the shape. `NativeAdStyle.iconInsetDp` lets a host set the same inset on its own placement without
  cropping the icon.
- Startup and onboarding use the suspend consent API again. Startup still allows continuing after
  success, failure, or 15 seconds without an answer.
- Fixed a failed cache clear replacing the advanced settings page with an error screen it could not
  leave: the rows stay, and a snackbar shows the failure, with its own text when storage is full or
  busy.
- Fixed the privacy page staying blank when its provider threw: it shows the failure with Retry.
- Fixed the display settings page crashing the app when its preferences could not be read: it shows
  a failure with Retry, and a failed write keeps the setting on screen and shows an error snackbar.
- Fixed the usage and diagnostics page showing every choice as off when the stored choices could not
  be read: it shows a failure with Retry. A failed write now shows its error snackbar, on the
  settings page and on the onboarding page.
- Fixed a failure reading the theme preferences or the seasonal state crashing the app from the
  theme page or the seasonal overlay: the theme page shows a failure with Retry, and the overlay
  reports it.
- Fixed the support page showing a generic error when the product query failed offline: it shows the
  offline text. A failed purchase launch shows a translated message instead of the exception's text.
- Fixed the onboarding screen reporting `screen_state` as `loading` for as long as it was open: it
  reports `success` while the pages show, `loading` while completion is saved, and `error` after a
  failed save.
- Fixed the issue reporter showing GitHub's raw response body or an exception's message when a send
  failed: it shows the shared text for the failure, such as no internet or a timeout, or "Failed to
  send report".
- Fixed the issue reporter crashing when the device details for its panel could not be read: the
  panel shows the failure and tries again the next time it opens.
- Fixed the help page reporting each review request twice as `vm_op_start`.
- Fixed the holiday greeting's checkbox sitting against its label and its press ripple being cut
  off: the row is rounded and padded, with room between the box and the text.
- Fixed the display settings' and developer options' choice dialogs losing their top icon along with
  their rows' icons.
- Fixed the tabs' floating action buttons sitting over the gesture bar once the bottom bar had
  hidden on scroll: they now rise by the part of the navigation bar's inset the hiding bar no longer
  covers.
- Fixed clicking the tab already shown replaying its icon: an icon used for both states and set to
  `ToolkitIconReplayMode.Reverse` travelled back to its unselected frame while the tab stayed
  selected. The bar, rail and drawers no longer replay the selected tab's icon, and
  `AnimatedToolkitIcon` keeps a selected component on its selected frame.
- Fixed the navigation colour not following scrolling (`NavigationTint.OnScroll`) on pages opened
  beside a rail or drawer, such as settings and its list-detail pages.
- Fixed rain replacing the snow of the Christmas theme: during the Christmas season, while the
  Christmas palette is worn, snow falls even with the Rain weather effect, and the rain comes back
  after the season.
- Fixed `DefaultSnackbarHost` drawing nothing for snackbars without `CustomSnackbarVisuals` and
  never drawing an action: every snackbar now shows, with its action when it has one.
- Fixed the ads settings page never showing its consent failures.
- Fixed `LargeTopAppBarWithScaffold` creating a new snackbar host on every recomposition, which
  dropped the snackbar showing, and drawing Material's plain snackbars: it keeps one host, draws the
  Toolkit's, and gives the screen inside `rememberScaffoldSnackbars()`.
- Fixed a snackbar shown through `ShellHost(snackbarHostState)` waiting, unseen, while a start
  screen or a page covered the tabs: the shell now shows it at the bottom of the window until the
  tabs are back.
- Fixed subscriptions bought through `BillingRepository.launchSubscriptionFlow` never being
  acknowledged, which made Play refund them after three days: subscriptions are now acknowledged
  instead of consumed, and `processPastPurchases` recovers unacknowledged subscriptions as well as
  unconsumed one-time purchases.
- Fixed `AdBanner` leaking its `AdView`, and with it the activity, each time the banner left the
  screen or was rebuilt: the view is now destroyed when it is replaced or leaves composition.
- Fixed a damaged shell settings file crashing the app on every launch: the shell now falls back to
  its default settings.
- Fixed `extractChangesForVersion` returning only the version heading for Keep a Changelog style
  notes: the section now ends at the next heading of the same or a higher level, so `### Added` and
  similar sub-headings stay in it.
- Fixed a `BaseCoreManager` subclass that overrides `dispatchers` crashing on start, and
  `BaseCoreManager.isAppLoaded` possibly staying `false` for readers on the main thread.
- Fixed the consent form reporting success when it closed with an error: the request now fails, as
  it does when the form cannot load.
- Fixed the issue reporter sheet staying in its sending state when sending threw: it returns to the
  form and shows the failure.
- Fixed toggling one ads setting cancelling the other's save while both were in flight.
- Fixed the seasonal theme overlay silently swallowing failures, including cancellation, while
  restoring the everyday theme or answering the holiday greeting: failures are now reported as
  non-fatals.
- Fixed a failed or declined purchase on the support page replacing the whole page with "failed to
  load SKU details": the donation options stay and a snackbar shows the error. When the products
  themselves cannot be loaded, the error page now has a Retry button.
- Fixed billing launch failures being reported to Crashlytics as `SupportViewModel` errors.
- Fixed a cancelled in-app update request still opening the update screen when Play answered
  afterwards.
- Fixed a consent request from a new screen, such as after a rotation, joining a request whose
  screen had been destroyed and whose form would show on that dead window. It now waits briefly for
  that request to end, then starts its own.
- Fixed "Allow all" and "Allow essential" in the diagnostics settings handing the consent SDKs
  several mixed states while saving: the whole answer is stored in one write.
- Fixed the review flow asking Play twice per review: the request made to check availability is
  reused to launch the review. Review failures are logged instead of dropped, and cancellation is no
  longer swallowed.
- Fixed `DefaultBillingRepository.getInstance` returning a closed repository after `close()`.
- Fixed the settings illustration (`rememberPaletteImageVector`) keeping stale grass, leg and
  background tree colors when only those theme colors changed.
- Fixed `BaseViewModel.updateSuccessState` possibly dropping a state change made at the same moment
  by another update: it now reads and writes the state in one atomic update.
- Fixed the About screen going blank when its entries failed to load: it now shows the failure with
  a Retry button.
- Fixed a settings search result crashing the app when it opened a page the app's graph does not
  register. Such a result is now left out, with a warning in the log; a root row with an action
  keeps its action in the results but loses the unregistered fallback page, and the settings list
  itself is unchanged.

---

# September 24, 2026

**Version:** `3.0.0-pre21`

### Changed

- Changed the dark Android palette to pair Android green with Android's navy and light blue on
  blue-gray surfaces, so it no longer looks like the green palette. The light scheme is unchanged.
- Changed `MaterialYouCircleSwatch` back to a swatch without divider lines or a ring. The selection
  check is drawn in the swatch's primary on a darker or lighter disc of the same hue, so it stays
  visible on every palette without taking colors from the app's theme.
- Changed `themePreferencesState()` to take only `themeModeDefault` and `staticPaletteIdDefault`.
  The removed `dynamicColorsDefault`, `amoledModeDefault`, and `dynamicPaletteVariantDefault` only
  shaped the placeholder first emission; the stored values, with the data source's own defaults for
  missing keys, were used right after.

### Fixed

- Fixed the theme settings palette rows not opening on the palette in use. `themePreferencesState()`
  emitted placeholder defaults (wallpaper colors on, default palette) before the stored values, so
  the rows were positioned on the default palette and stayed there. Its first emission is now the
  stored state, and each row is created positioned on the selection, then centers it.

### Removed

- Removed `GeneralSettingsContentProvider.ProvideActions`, whose only action was the seasonal themes
  one.
- Removed `SeasonalThemesAction`, `SeasonalThemesDialog`, `SeasonalThemesViewModel`,
  `SeasonalThemesEvent`, and `SeasonalThemesUiState` from `:library:feature:theme`.
- Removed the seasonal all-year and snowfall switches from `:library:core:datastore` and
  `:library:core:common`: `SeasonalThemeRepository.setSeasonalThemesAllYear` and
  `setSnowfallEnabled`, the matching `SeasonalThemePreferencesDataSource` members,
  `SeasonalThemeState.allYear` and `snowfallEnabled`, and the `DATA_STORE_SEASONAL_THEMES_ALL_YEAR`
  and `DATA_STORE_SNOWFALL_ENABLED` keys. Values already stored under those keys are ignored.
- Removed the `dividerColor` parameter from `MaterialYouCircleSwatch`.

---

# September 23, 2026

**Version:** `3.0.0-pre20`

### Added

- Added purple and orange static palettes (`purple`, `orange`), each with light and dark schemes.
  Orange is an everyday palette with soft peach containers, separate from the Halloween pumpkin and
  purple.
- Added `Modifier.snowfall` and `SnowfallStyle` to `:library:core:designsystem`: falling snow drawn
  over any element, with density, colors, flake size, speed, wind, opacity, and shape (dots,
  crystals, or both). It runs in the draw phase only, so nothing recomposes while snow falls, it
  stops while the app is in the background, and taps go through it.
- Added seasonal themes to `:library:feature:theme`. Once a host calls
  `SeasonalThemeManager.install()`, the first screen opened during Christmas (December 24 to January
  7) or Halloween (October 31 to November 2) greets the holiday and offers its theme with a
  checkbox. The greeting appears once per holiday. An accepted holiday theme is taken off when the
  holiday ends, bringing back the palette and wallpaper-colors setting from before, unless another
  palette was picked during the holiday. Snow falls over every screen while the Christmas theme is
  on, and is skipped when animations are turned off system-wide.
- Added a seasonal themes easter egg. Tapping the build version five times on the About screen now
  also unlocks a top app bar action on the theme settings page. Its dialog keeps the Christmas and
  Halloween palettes in the palette list all year and turns snowfall on or off.
- Added `SeasonalThemeRepository` to `:library:core:datastore`, which stores the seasonal themes
  state and owns the rules for applying and restoring a holiday theme.
- Added `Context.isSystemAnimationDisabled()` to `:library:core:common`, and
  `isAppInDarkTheme(themeMode)` and `ColorScheme.toSwatchColors()` to `:library:core:designsystem`.
- Added `GeneralSettingsContentProvider.ProvideActions`, which lets a settings page contribute top
  app bar actions. On tablets they appear in the settings bar while that page is open in the detail
  pane.
- Added `NativeAdCache` and `rememberNativeAdCache` to `:library:core:ui`. `NativeAdSlot`,
  `rememberNativeAd`, and `rememberNativeAdState` take a `cache` and `cacheKey`, so an ad in a lazy
  list or grid survives its item scrolling out of view instead of being destroyed and requested
  again. An ad older than an hour is replaced, and every ad is destroyed when the cache leaves
  composition.
- Added `HorizontalWavyDivider` and `VerticalWavyDivider` to `:library:core:ui`, drawing
  `il_wavy_line` as a divider. The wave scales with the divider and fits a whole number of
  half-waves into any length, so it ends cleanly at any size. It takes the colour of Material's
  plain dividers by default.

### Improved

- Added baseline profiles to `:library:core:designsystem`, `:library:core:ui`,
  `:library:navigation`, and `:library:feature:theme`. A host's release build merges them, so the
  classes a cold launch runs before its first frame (the theme and default palette, the icon slot,
  navigation state, the drawer and bottom bar, and the seasonal overlay) are compiled ahead of time
  on install. Before, no toolkit code was in the app's profile. The lists are hand-picked and not
  yet measured; a generated profile from a startup benchmark is the intended replacement.
- Improved `AppTheme` so it rebuilds its color scheme only when a theme setting changes, and builds
  the wallpaper-based schemes only when dynamic colors are on. It used to build both wallpaper
  schemes on every recomposition.
- Improved the seasonal overlay so it composes no second app theme over every activity; it borrows a
  theme only while the holiday greeting is on screen.
- Improved snowfall drawing so it allocates nothing per frame.
- Improved the theme settings page so each palette row opens scrolled to the palette in use,
  centered, instead of at the start of the row.
- Improved palette swatches in the theme picker and onboarding. They show each palette's most
  colorful variant of every accent, follow the theme the app is actually drawn in rather than the
  system setting, draw from one cached drawing node instead of eight nested layouts, and draw the
  selection check in black or white on the swatch's own color so it no longer disappears on dark
  palettes.
- Improved `AppTheme` so it no longer re-subscribes to the theme preferences on every recomposition.
  Each re-subscription replayed the defaults, which could briefly swap the whole app's color scheme
  and recompose everything under it.
- Improved `DisplaySettingsScreen` so it no longer restarts its startup-page subscription on every
  recomposition.
- Improved `VersionInfoAlertDialogContent` so it uses Coil's shared image loader instead of building
  a new one, with its own caches, on every recomposition.
- Improved `Modifier.animateVisibility` so `index` is optional. Without it, elements shown together
  still cascade, in the order they appear, so a `Column` or a group of cards needs no index. With an
  index the cascade is unchanged. The motion also runs in the draw phase instead of re-laying out
  the element on every frame, and is skipped when the system's animations are turned off.

### Fixed

- Fixed unreadable text in several static palettes. In light mode, Android green, yellow, skin,
  Halloween, and green used their bright brand color as `primary`, so text buttons, links, switches,
  and selected icons drawn in it fell as low as 1.35:1 against the background. Palettes keep their
  authored colors wherever contrast allows: on fills only the text color is adjusted, and a color
  moves only when it is the text on a surface and cannot pass there, to the nearest readable tone of
  the same hue. Those bright brand colors now appear exactly as the light `primaryContainer` or
  `tertiaryContainer` (floating action buttons, tonal buttons, selected chips). Dark schemes keep
  their authored accents. Snackbar actions (`inversePrimary`) are readable in every scheme, and
  monochrome's outline is visible against raised surfaces.
- Fixed error roles out of step with their containers: error, error container, and their foregrounds
  now come from the same tonal palette in every static scheme.
- Fixed static palettes showing Material's baseline purple in their fixed roles (`primaryFixed`,
  `secondaryFixed`, `tertiaryFixed` and their `on` roles). Every palette now defines them from its
  own colors.
- Fixed the Christmas palette using red for every accent. It now pairs festive red with evergreen
  green and gold.
- Fixed the build version row on the About screen copying the version to the clipboard on every tap,
  which buried the five-tap konfetti under clipboard confirmations. The other rows still copy their
  value.
- Fixed a damaged settings file crashing every app built on the toolkit at launch. The shared
  `settings` DataStore now replaces a file it can no longer read with empty preferences, so values
  fall back to their defaults instead of every read throwing `CorruptionException`.
- Fixed `AppTheme` crashing with `ClassCastException` when composed under a context that wraps its
  activity, such as a dialog's. It now finds the activity through the wrapper and leaves the status
  bar alone when there is none.
- Fixed a completed donation sometimes being followed by a failed purchase message.
  `DefaultBillingRepository` could consume the same purchase twice when the purchase callback and a
  purchase check on resume arrived together, and the second attempt reported `Item is not owned`.

### Changed

- Changed `AppTheme` so battery saver no longer forces the dark theme over an explicit Light choice.
  Light and Dark now mean what they say; "follow system" still goes dark in battery saver, because
  the system switches its own dark theme on there.
- Changed `AboutViewModel` to take a `SeasonalThemeRepository` (`seasonalThemes`), which records the
  easter egg unlock. `aboutModule` passes it; hosts that construct the ViewModel themselves pass
  `get()` from the graph.
- Renamed `DefaultDiagnosticsPreferencesDataSource` in `:library:core:datastore` to
  `DefaultUsageAndDiagnosticsPreferencesDataSource`, matching the
  `UsageAndDiagnosticsPreferencesDataSource` contract it implements.
  `CommonDataStore.diagnosticsPreferences` keeps its name and now has the renamed type. Hosts that
  name the class directly update the import.

### Removed

- Removed the forwarding `appToolkitNavigationEntryBuilders` in `feature.about.ui.navigation` from
  `:library:apptoolkit`. It put a package owned by `:library:feature:about` inside another module.
  Hosts import `appToolkitNavigationEntryBuilders` from `app.main.ui.navigation` instead, with the
  same signature, destinations, and route keys.

---

# September 20, 2026

**Version:** `3.0.0-pre19`

### Changed

- Changed the `SearchTopAppBar` title to animate between values the way `MainTopAppBar`'s does,
  instead of swapping instantly.

### Fixed

- Fixed `SearchTopAppBar` jumping as it swapped between its title and the search field. The slot
  followed the height of whichever was showing, so the title snapped up and slid back when search
  closed, and the field grew out of its centre, sliding the placeholder across the title. Both
  states now share one fixed-height slot and crossfade in place.
- Fixed the `SearchTopAppBar` title sitting above the navigation icon instead of centred beside it,
  where `MainTopAppBar` puts it.
- Fixed `SearchTopAppBar` never showing its title when its first composition was already searching.
  A host whose first screen searches no longer gets a blank title on its other screens.

---

# September 20, 2026

**Version:** `3.0.0-pre18`

### Added

- Added `SearchTopAppBar` to `:library:core:ui`, a top app bar whose title crossfades into a search
  field while `showSearch` is true. The query and the searching state stay with the caller, so one
  piece of state drives both the bar and the filtering underneath it. Filters are optional and sit
  inside the field through the `filters` slot, with `SearchFilterAction` as the ready-made toggle:
  tonal while a filter is applied, text while it is not. The search placeholder and clear-button
  description are translated across all 25 supported locales, so a host that has nothing to say
  about them passes nothing.
- Added `NavigationDrawerHeader` and `NavigationDrawerBranding` to `:library:navigation`, drawing an
  app's logo beside its name at the top of a drawer. `NavigationDrawerSheet` takes it through
  `branding`, which is null by default and draws no header, so an existing drawer is unchanged until
  a host opts in. The logo is a `ToolkitIcon`, the same slot every other toolkit component takes,
  and is drawn untinted unless `logoTint` says otherwise, so a multi-colour brand mark arrives
  intact. It is sized to the title's line height rather than to a fixed dimension, so the pair stays
  balanced as the person scales their font up.
- Added `NavigationDrawerRoutes.StandardRoutes`, naming the Settings, Help, Support, Updates, and
  Share entries every toolkit host has.
- Added `centerTitle` to `MainTopAppBar`, defaulting to `false`. Left off, the bar is the small top
  app bar it has always been; turned on, it becomes a centre-aligned one.

### Removed

- Removed the twelve `anim_device_*_off` drawables and `anim_media_pause` from
  `:library:core:designsystem`, and renamed the surviving `anim_device_*_on` drawables to drop the
  suffix: `anim_device_light_on` is now `anim_device_light`. Each removed drawable was the same
  animation as the one it was paired with, authored backwards: the same path morphs with `valueFrom`
  and `valueTo` swapped, and the delayed segment moved to the front. `ToolkitIconReplayMode.Reverse`
  already plays that direction, so nothing is lost and the same motion is no longer shipped twice. A
  host on a removed or renamed drawable moves to the new name and sets
  `replayMode = ToolkitIconReplayMode.Reverse` where it needs the opposite direction.

### Fixed

- Fixed `RequestInAppReviewUseCase` launching the Play review flow without checking availability
  first. An eligible user on an install Play cannot serve (sideloaded, no Play Store, a debug build
  run from the IDE) now reports `ReviewOutcome.Unavailable` instead of `ReviewOutcome.Failed`, which
  said a launch had failed when no launch was ever possible. The prompt flag stays unset either way,
  so the user still gets their one prompt once Play can serve it.
- Fixed `DefaultReviewRepository` running `launchReviewFlow` on whichever dispatcher its caller
  happened to be on. The call puts a dialog in front of the host activity, so it now runs on the
  main thread regardless, and the availability check, which reads the package manager over binder,
  runs on IO. Callers no longer decide where the dialog is shown.

### Changed

- Changed `NavigationDrawerSheet` so a host's own destinations take the top of the drawer and the
  standard Settings, Help, Updates, and Share entries drop to its bottom edge. A drawer holding
  nothing but the standard entries renders exactly as before. `pinStandardRoutes` turns this off and
  defaults to true, and `pinnedRoutes` names the entries it moves, defaulting to
  `NavigationDrawerRoutes.StandardRoutes`. The split itself is `navigationDrawerPlan`, which is unit
  tested.

---

# September 20, 2026

**Version:** `3.0.0-pre17`

### Added

- Added 30 reusable animated vector drawables to `:library:core:designsystem`, including paired
  on/off animations for blinds, cameras, fans, garage doors, lights, locks, outlets, security
  systems, switches, thermostats, TVs, and vacuums, plus playback, volume, sound bar, and container
  animations.
- Added shared root settings labels to `:library:feature:settings`, translated across all 25
  supported locales, so hosts no longer need to define their own labels and summaries for
  destinations already provided by `SettingsContent`.
- Added shake-to-report support to `:library:feature:issuereporter`, configurable through
  `IssueReporterConfig(shakeToReportEnabled = true)` and `IssueReporterShakeManager`.
- Added App Toolkit and Google Play services version information to the About screen, translated
  across all 25 supported locales.
- Added unit test coverage for `NavigationBackStackActions`, including top-level navigation,
  single-top deduplication, and back-stack popping.
- Added unit test coverage for `DefaultFirebaseController`, including consent settings, analytics
  events, screen views, user properties, and error reporting.

### Changed

- Replaced the standalone issue reporter activity with a bottom-sheet implementation using
  `IssueReporterBottomSheet`, `IssueReporterLauncher.show(activity)`, and `IssueReporterContent`.
  Advanced settings now opens the reporter over the current screen, while `IssueReporterActivity`,
  its manifest entry, `Theme.AppToolkit.IssueReporter`, `AdvancedSettingsProvider`, and the unused
  `bugReportUrl` were removed.
- Improved issue reporter submission feedback by replacing the form after a successful report with a
  compact confirmation state containing a check indicator, `Report submitted`, and a `Done` button.
  Failed submissions preserve the entered report, successful reports are cleared when the sheet is
  dismissed, results are surfaced through toasts, and the previous `Open issue` action, analytics
  event, and `open_button_label` resources were removed.
- Updated the AGP 9+ release configuration by removing the legacy `proguardFiles(...)` setup from
  `:library:apptoolkit` and the sample application, with the sample now using AGP 9 unified R8
  optimization through `optimization { enable = true }`.
- Updated Ktor from `3.5.2` to `3.6.0` and Robolectric from `4.16.1` to `4.17`.
- Reworked About screen copy handling so the application name, build version, App Toolkit version,
  Google Play services version, and device information can all be copied through the generic
  `AboutItemAction.CopyToClipboard`. Clipboard operations now live in `AboutViewModel`,
  `AboutRepository.copyDeviceInfo` and `CopyDeviceInfoUseCase` were removed, version taps use
  `AboutItem.Preference.countsVersionTap`, copy requests run independently on the main thread, and
  localized success and failure feedback is provided across all 25 supported locales.
- Split licenses, privacy, and changelog functionality out of `:library:feature:about` into the
  dedicated `:library:feature:licenses`, `:library:feature:privacy`, and
  `:library:feature:changelog` modules, moving their screens, resources, repositories, ViewModels,
  use cases, and providers to their matching packages. `PrivacySettingsList` is now `PrivacyScreen`
  with a data-driven `PrivacyItem` model, while `LicensesScreen` now exposes its state through
  `LicensesViewModel`.
- Replaced `:library:feature:help` with `:library:feature:faq`, moving the FAQ screen, activity,
  repository, data sources, UI components, native ad slot, overflow menu, and catalog into the new
  feature. Help-specific classes and constants were renamed to their FAQ equivalents, `helpModule`
  became `faqModule(hostBuildConfig)`, `GetFaqUseCase` was removed, `FaqMappers` moved to
  `data/remote/mappers`, `FaqItem` and `FaqId` moved to `data/models`, and the sample FAQ resources
  moved to `:sample:feature:faq`. The user-facing `Help & feedback` destination, `HelpRoute`,
  `HELP_NATIVE_AD` qualifier, and GA4 screen name remain unchanged.
- Moved `AboutInfo` from `domain/models` to `data/models`.
- Moved `DefaultNavigationRepository` and its drawer labels from `:library:feature:about` to
  `:library:navigation`.
- Moved `MainTopAppBar` from `:library:feature:about` to `:library:core:ui` and made its Support
  action depend on the host-provided `onSupportClick` callback instead of directly opening
  `SupportActivity`.
- Renamed `DisplaySettingsList` to `DisplaySettingsScreen`, `ThemeSettingsList` to
  `ThemeSettingsScreen`, `AdvancedSettingsList` to `AdvancedSettingsScreen`, and
  `UsageAndDiagnosticsList` to `UsageAndDiagnosticsScreen`.
- Renamed `FirebaseControllerImpl` to `DefaultFirebaseController` while keeping a
  backward-compatible typealias.
- Moved `DefaultInAppUpdateRepositoryTest` from `:library:feature:about` to
  `:library:integration:update` and applied the unit-test convention plugin to
  `:library:integration:update` and `:library:integration:firebase`.

### Fixed

- Fixed remote FAQ catalogs containing only blank entries rendering empty rows instead of falling
  back to the bundled FAQ content.

---

# September 13, 2026

**Version:** `3.0.0-pre16`

### Improved

- Updated `GeneralTextField` search handling so `GeneralTextFieldStyle.Search` placeholders use the
  same `textStyle` as entered text, keeping typography consistent before and after typing begins.
  The `TextFieldValue` overload also no longer exposes the unused `onSearch` callback, which only
  applies to the `String` overload used by the Material search input.

---

# September 12, 2026

**Version:** `3.0.0-pre15`

### Added

- Added `ToolkitIcon.Bitmap`, a runtime `ImageBitmap` source for toolkit icon slots. Bitmap artwork
  preserves its original colors by default and can opt into the host component's content color with
  `tintable = true`. `ToolkitIcon.of(imageBitmap)` provides the matching shorthand factory.

### Improved

- `ToolkitIconContent` now renders runtime bitmap artwork through the same shared path used by
  `GeneralButton`, navigation items, fields, and FABs, so consumers no longer need a parallel button
  implementation when an icon is resolved at runtime rather than bundled as a drawable resource.
- Added runtime bitmap coverage to the Components button showcase and the `GeneralButton`
  instrumented test. The icon documentation now describes the fifth source, its tint behavior, and
  the boundary for converting mutable Android `Drawable` instances into remembered `ImageBitmap`
  values before passing them to the immutable toolkit model.

---

# September 10, 2026

**Version:** `3.0.0-pre14`

### Added

- `ToolkitIconLoopTrigger` chooses when a looping animated icon starts playing. `Immediately`, the
  default, keeps the existing behaviour and plays as soon as the icon is composed. `OnInteraction`
  rests on the frame `atEnd` picks until the component is clicked or becomes selected, and loops
  from then on, so a screenful of animations no longer all play at once. It is independent of the
  replay mode, which still describes one cycle, and a reversing loop that starts on an interaction
  travels on from the frame it was resting on rather than snapping to the other one.
  `resolveToolkitIconLoop(icon, interacted)` exposes the same rule to custom renderers, and
  `ToolkitIconContent` takes the matching `interacted` flag.
- Added `GeneralTextField`, the input counterpart of `GeneralButton`: one field component whose
  defaults render exactly the Material filled field, with `Outlined` and `Grouped` styles, a
  `ToolkitIcon` in either icon slot, a trailing icon that becomes a button when it is given an
  action, `errorText` that marks the error state and replaces the supporting line in one parameter,
  and a GA4 event logged when the field takes focus. `Grouped` is the form treatment: no indicator
  line, corners cut to the field's `position` in the block. Both a `String` and a `TextFieldValue`
  overload are available.
- `GeneralTextFieldStyle.SearchOutlined` is the same search box as an outlined, fully rounded text
  field, for a search that sits on a page rather than in an app bar. Unlike `Search` it is an
  ordinary text field, so every parameter applies to it and the `TextFieldValue` overload accepts
  it.
- `GeneralTextFieldStyle.Search` draws the Material search input: the pill-shaped box that filters
  the content behind it as it is typed, leading with a search icon unless another is given. It is
  the input field used on its own rather than a `SearchBar`, whose collapsed form intercepts the
  keyboard and whose expanded form reserves height for results a filtering field does not have.
  `onSearch` reports the keyboard's search action, and `trailingContent` replaces the trailing slot
  with a row, for a field ending in more than one action, such as a filter beside a clear button.
- Added Markdown authoring to `GeneralTextField`, and with it to any screen rather than only the
  issue reporter: `GeneralTextFieldMarkdown.Highlight` styles Markdown syntax as it is typed through
  a length-preserving transformation, and `Editor` adds the formatting bar for bold, italic, inline
  code, code blocks, bulleted and numbered lists, quotes and links, which edits the Markdown source
  and places the caret. The bar is cut and filled to match the field above it. `onMarkdownFormat`
  reports the action used as a `MarkdownFormatAction` carrying a stable `analyticsName`. The issue
  reporter's form and description field are now this component; the Markdown pieces moved from that
  feature into `:library:core:ui`, where `MarkdownVisualTransformation` and
  `rememberMarkdownVisualTransformation` now live in
  `core.ui.views.fields.markdown`.

### Changed

- The **Usage and diagnostics** screen is the ads screen's layout: the reporting switch over a
  single **Advanced privacy settings** preference that opens the privacy choices dialog, the same
  dialog the onboarding flow shows. The four granular consents no longer sit on the screen as an
  expandable block of switch cards, which was a plainer second copy of what that dialog's Details
  tab explains.
  `FirebaseConsentDialog` moved from `:library:feature:onboarding` to
  `:library:feature:diagnostics`, whose state it reads and writes, and its strings moved with it as
  `privacy_choices_*`.
  `UsageAndDiagnosticsEvent.AllowAllConsent` and `AllowEssentialConsent` carry the dialog's
  whole-bundle answers, so what "everything" and "essentials" cover is decided once rather than at
  each call site. `ConsentToggleCard`, `ConsentSectionHeader` and `ExpandableConsentSectionHeader`
  are removed, with the strings only they used.

---

# September 9, 2026

**Version:** `3.0.0-pre13`

### Added

- Animated icons can now loop. `ToolkitIcon.AnimatedVector` and `ToolkitIcon.Lottie` accept
  `loop = true` to keep playing for as long as they are composed, instead of once per click or
  selection change. Looping is off by default, so existing icons are unchanged, and it is
  independent of the replay mode, which still describes one cycle: `Restart` repeats the animation
  forward, `Reverse` travels forward and back. A looping icon owns its playback, so clicks and
  selection no longer replay it, and every component that draws a `ToolkitIcon` supports it.
- Added Markdown authoring to the issue reporter's description field: a formatting bar for bold,
  italic, inline code, code blocks, bulleted and numbered lists, quotes and links, and Markdown
  syntax highlighted as it is typed.
- Added `GroupedGrid`, the grouped category block used for storage and media breakdowns and for
  blocks of actions. Cells are laid out in columns as one rounded group: only the corners at the
  outside of the block are rounded, seams between cells are cut small, and a grid of one cell rounds
  all four of its corners. `GroupedGridMeasurements` picks the cell height and everything that
  scales with it the way `ButtonMeasurements` does for buttons, `GroupedGridDefaults` carries the
  radii, spacing, colors and badge shape, and each cell may cut its badge from any `Shape`,
  including the Material 3 `MaterialShapes` set. Cell titles are semibold, and the press-scale
  animation is off unless a caller asks for it, so a group reads as one block rather than as cells
  that move on their own. Passing an ad unit adds one full-width native ad row in the middle of the
  block; a grid of one cell never shows one, and an ad that fails to load leaves the group cut as if
  it had never been asked for.
- Added the `GridRow` native ad presentation: an icon-led row that keeps its disclosure chip inline
  with the body instead of on a line of its own, and takes its badge, icon size, padding and
  headline size from the grid it sits in, so a sponsored row is no taller than the cells around it.
  Its badge is filled with the same silhouette the cells are cut from, `MaterialShapes` included,
  through `rememberNativeAdBadgeShape`, which flattens any Compose `Shape` to the path an ad's
  Android view can be drawn with.
- `CommonFilterChip` accepts an `icon` shown while the chip is unselected, and `hasAnimation` to
  turn off the crossfade into the selected checkmark.
- `CommonDropdownMenuItem` takes an optional `icon` and a plain `text` alongside the existing
  string-resource overload, so value pickers can use the toolkit row without a leading glyph.
- Added `ButtonMeasurements`, the five Material 3 Expressive button size classes (extra small,
  small, medium, large, extra large), accepted by `GeneralButton`. Container height, shape, content
  padding, icon spacing, and label typography all follow the selected size, so callers pick a size
  instead of restating the specification, `iconSize` included.
- Added an `animatedIcon` constructor to bottom-bar and drawer items: one AVD or Lottie icon can
  cover both navigation states, including reverse replay, without separate icon arguments. Without
  `animatedIcon`, callers must now supply both `icon` and `selectedIcon` explicitly.
- Added reusable Check, Clock, and Grid AVDs to DesignSystem and moved Settings/Share there.
  Consumers must import these drawable resources from `core.designsystem.R`. Private animation
  resources are now inline; the unused Success animation and imported dummy color were removed.
- Added bundled Lottie icons with forward restart by default, optional reverse replay, and optional
  content-color tinting. All FAB wrappers now accept the shared `ToolkitIcon` API while retaining
  their existing ImageVector/custom-content overloads.
- Added `ToolkitIcon`, the icon slot shared by navigation items and buttons. It accepts a Compose
  `ImageVector`, a drawable or vector resource, or an animated vector drawable that plays when the
  component is clicked. `ToolkitIconReplayMode` chooses whether a repeated click restarts the
  animation, the default, or plays it backwards. The accepted combinations are documented in
  `:library:core:designsystem` README.md.
- Added animated Settings and Share drawer icons, used by the standard drawer entries.
- Added `NavigationDrawerSheet`, a reusable navigation drawer component that renders
  `ModalDrawerSheet` with navigation drawer items, selection state, click handling, and dividers.
- Added core common's AppVersionMetadata and getVersionMetadata for package version lookup without a
  UI dependency, and core DataStore's startupValueFlow for caller-defined startup mapping.
- Exposed toolkit destination builders from app.main.ui.navigation in the main toolkit module. The
  historical About-package entry point, UI helpers, and AppVersionInfo remain compatible.

### Changed

- Added `NativeAdStyle`, which gives a native ad the finish of the screen it is on without a view
  tree of its own: badge silhouette, badge colour, headline and body size and colour, body line cap,
  and whether the call to action is a filled pill or a text button. `NativeAdSlot` takes one. A
  style overrides only what it names, and is applied to views that already exist, so changing one
  repaints the ad rather than rebuilding it and losing the loaded ad.
- The Help screen's ad now matches the rows it sits between: a `Cookie12Sided` badge on
  `primaryContainer` like Contact Us, `titleMedium` and `bodyMedium` text at the weight the question
  rows use, and a text button instead of a filled pill.
- **Breaking:** `NativeAdPresentation.GridRow` no longer carries `iconCornerRadiusDp`,
  `headlineTextSizeSp`, or `iconShape`. A presentation now describes the arrangement only, and those
  three moved to `NativeAdStyle`, which is where the rest of an ad's finish lives.
- **Breaking:** Single-screen native ad cards moved out of `:library:core:ui` to the code that draws
  them. `HelpNativeAdCard` is now in `:library:feature:help`, `SupportNativeAdCard` in
  `:library:feature:support`, and `AppsListNativeAdCard` moved to the sample app, whose screen is
  its only caller. Consumers importing them from `core.ui.views.ads` must update the import, or
  compose their own with `NativeAdSlot`, which is what a placement is. `AppDetailsNativeAd` is
  removed; it had no call site left.
- The issue reporter form is one grouped block of fields. The fields state themselves through a
  placeholder and a leading icon instead of a floating label, whose animation reserved the space
  that kept the two-dp grouping from reading as a group, and the description field grows to twelve
  rows before scrolling its own content.
- Filed issues now use a Markdown body with Description, Device info and Extra info sections; the
  device and extra tables are Markdown tables inside a collapsible block instead of raw HTML.
- **Breaking:** `TopListFilters` now takes `FilterChipItem` entries instead of plain strings, so a
  chip row carries a per-chip icon and label. Callers must map their filters to `FilterChipItem`.
  `hasAnimation` turns the chip and row animations off, and `contentPadding` lets a caller that
  already insets the row stop it from insetting itself.
- **Breaking:** `TopListFilters`'s `label` is now `leadingLabel`, the caption before the chips, and
  it defaults to `null` rather than "Sort by". A null, empty, or blank value renders neither the
  caption nor the gap after it, so the chips start where they would in a row that never had one.
  Rows that want the old caption must pass it explicitly.
- `GeneralButton` now renders through the Material 3 Expressive button and icon-button overloads, so
  every style picks up the expressive resting and pressed shapes. Buttons keep their previous height
  by defaulting to `ButtonMeasurements.Small`; icon-only content now follows the expressive
  icon-button container and icon metrics instead of a fixed 40dp box.
- `GeneralButton`'s `iconSize` is now `Dp?` and defaults to `null`, which scales the glyph with
  `measurements`. Pass `SizeConstants.ButtonIconSize` to pin it to the size toolkit icons are drawn
  at elsewhere, which suits small affordances such as favourite, share, and expand buttons, or any
  other `Dp` for a one-off. An explicit `shape` still overrides the resting shape.
- **Breaking (3.0):** Consolidated text, tonal, outlined, and filled action buttons into one
  adaptive
  `GeneralButton` with five styles, including Elevated. Removed the separate APIs without deprecated
  aliases. Icon-only content uses the matching Material icon button (a compact elevated button for
  Elevated); all forms share feedback, analytics, replay, icon position, and color overrides. Rename
  `iconContentDescription` to `contentDescription` and replace `ButtonColors` with
  `containerColor` / `contentColor`. `contentDescription` is optional, and icons beside labels no
  longer repeat the label. Migration is documented in the DesignSystem README.
- Navigation item icons and every `General*Button` now take a single `ToolkitIcon` instead of
  separate `ImageVector` and `Painter` parameters. Callers passing `vectorIcon = someIcon` to a
  button must pass `icon = ToolkitIcon.Vector(someIcon)`, and `NavigationIcon` is now `ToolkitIcon`
  from `core.designsystem.ui.icons`. `AnimatedIconButtonDirection` takes the same type.
- The standard Settings and Share drawer entries declare their animated icon once, so it covers both
  the unselected and the selected state.
- Standardized library APIs under module-owned `feature.*`, `core.*`, and `integration.*` package
  roots; consumers must update imports to the new packages.
- Moved library dependency-injection bindings into the owning feature/integration modules and
  exposed the datastore module from `core.datastore.di`; the main toolkit module now only composes
  those modules.
- Moved `ThemePreferencesState`, `BaseCoreManager`, and `FirebaseControllerImpl` into their
  layer-specific packages; consumers must update imports to `core.common.domain.models.theme`,
  `core.common.data.managers`, and `integration.firebase.data.repositories`.
- Moved support donation product IDs to `feature.support.domain.models`; consumers importing
  `DonationProductIds` must update to the new package.
- Settings, advanced, diagnostics, and display now expose their resources through their own `R`
  classes. Direct consumers of the removed settings resource artifact must update dependencies and
  imports; resource keys and translations are preserved.

### Improved

- The changelog sheet's action is now an extra-large expressive button, and `DropdownMenuBox` rows
  now match the rest of the toolkit's dropdowns instead of rendering as bare Material rows.
- Standardized changelog, alert-dialog, and date-picker actions with consistent button styling,
  haptic feedback, and press animations.

### Removed

- Removed the issue reporter's login section. Reports are always filed anonymously, so the
  `login_section_label`, `send_anonymously`, `use_github_account` and `optional_placeholder`
  resources are gone.
- Removed the bundled `shape_scalloped` vector drawable from the Help feature. The Contact Us badge
  now renders `MaterialShapes.Cookie12Sided`, so the toolkit no longer ships hand-authored shape
  artwork that Material 3 already provides.
- Removed `ButtonIconSpacer`, which `GeneralButton` no longer uses now that icon spacing comes from
  the size class. Callers building their own button content should use `ButtonDefaults.IconSpacing`,
  which is all it wrapped.
- Removed `Activity.isInAppReviewAvailable`, an exact duplicate of the wired-up
  `ReviewRepository.isReviewAvailable(activity)`. Call the repository instead.
- Removed unused constants that no call site referenced: `ApiHost.DOCS_URL` and
  `ApiHost.OPEN_API_URL` (both still documented in the `ApiHost` KDoc),
  `GithubConstants.GITHUB_PAGES`,
  `AppLinks.DEVELOPER_PAGE` and `AppLinks.CONTACT_PAGE`,
  `SettingsAnalytics.Params.NAVIGATION_ROUTE`,
  `DataStoreNamesConstants.DATA_STORE_DYNAMIC_VARIANT_INDEX` and `DATA_STORE_REVIEW_DONE`, and the
  `DISPLAY_SETTINGS`, `FAQ`, `SELECT_STARTUP_DIALOG`, and `SELECT_LANGUAGE_DIALOG` log tags.

### Fixed

- Fixed the issue reporter's device-info section. Its expansion was held in a process-wide property
  shared by every instance, so the panel reopened by itself on a later visit; it is now per-instance
  state that survives configuration changes. The section expands vertically instead of also
  unfolding sideways, the header no longer reacts to taps anywhere along the row, and only its
  arrow, now a `GeneralButton`, toggles it.
- Fixed Help and Settings menu buttons that still passed ImageVector values to the migrated icon API
  and prevented the sample app from compiling.
- Prevented duplicate or late Billing service responses from crashing purchase recovery, product
  queries, and donation consumption with an `Already resumed` error.
- Fixed icon-state handling in `NavigationDrawerItemContent` and `LeftNavigationRail` to display
  `selectedIcon` when selected and `icon` when unselected.
- Fixed animated navigation icons never playing. They were swapped in already on their last frame,
  and a second click did nothing. They now play on every click, in the drawer, the bottom bar, and
  the navigation rail.

---

# August 28, 2026

**Version:** `3.0.0-pre12`

### Changed

- Display ads now defaults to on in debug builds as well as release, so a fresh debug install
  renders ads instead of none. A stored choice still wins in every build. Reduce ads continues to
  default to off everywhere.
- `dataStoreModule()` no longer takes `isDebugBuild`, which it only used to pick that default.

### Added

- Added `AdLoadReporter`, which logs every ad load failure, adds a Crashlytics breadcrumb, and
  records a non-fatal for the failures that are not simply no fill.
- Added `AdSlotDebugPlaceholder`, shown by `NativeAdSlot` on debug builds where an empty ad slot
  would otherwise render nothing.
- Added `rememberNativeAdState`, which returns why a slot is empty alongside the ad.

---

# August 28, 2026

**Version:** `3.0.0-pre11`

### Added

- Added a **Help & feedback** action to the standalone General Settings top app bar.

### Documentation

- Documented how a host should render ads: use `NativeAdSlot`, `rememberNativeAd` or `AdBanner`
  rather than the Mobile Ads loaders directly, and what the toolkit handles on the host's behalf.

### Fixed

- Fixed the ads migration removing an explicit opt-in as well as an opt-out, which on debug builds
  turned ads off again at every launch.
- Fixed the **Help & feedback** overflow action appearing on every standalone settings sub-page
  instead of on the settings root.

---

# August 26, 2026

**Version:** `3.0.0-pre10`

### Added

- Added a **Reduce Ads** preference for suppressing App Open ads while keeping other supported ad
  formats enabled.

### Changed

- Integrated the Reduce Ads preference into the shared ads settings flow and persistent DataStore
  configuration.
- Updated the release Ads Settings screen to expose Reduce Ads while retaining the full Display Ads
  control for debug builds.
- Updated shared DataStore access to use the App Toolkit dependency graph.

### Fixed

- Improved persistence error handling for ads preferences.

---

# August 24, 2026

**Version:** `3.0.0-pre9`

### Improved

- Expanded module documentation across the library.
- Improved KDoc coverage for reusable APIs, contracts, ownership, and non-obvious behavior.

---

# August 24, 2026

**Version:** `3.0.0-pre8`

### Changed

- Moved additional shared application resources and configuration under App Toolkit ownership.
- Moved reusable themes, splash resources, locale configuration, backup rules, and extraction rules
  into the library.
- Reduced the amount of host application configuration required when integrating App Toolkit.

### Improved

- Improved manifest contracts and ownership between App Toolkit and host applications.
- Improved reusable Support state handling.
- Improved shared navigation destinations and back-stack helpers.

---

# August 22, 2026

**Version:** `3.0.0-pre7`

### Changed

- Advanced the App Toolkit 3.0 preview publishing version.

No meaningful consumer-facing library behavior changed in this preview.

---

# August 22, 2026

**Version:** `3.0.0-pre6`

### Improved

- Improved JitPack publishing reliability by pinning builds to a supported Temurin JDK.
- Added onboarding completion failure translations to every supported locale.

### Fixed

- Fixed missing onboarding translations that caused library lint failures.

---

# August 22, 2026

**Version:** `3.0.0-pre5`

### Changed

- Refined reusable onboarding preference ownership.
- Improved separation between onboarding, display, and theme state.

### Improved

- Added visible feedback when onboarding completion fails.

### Fixed

- Improved onboarding persistence failure handling.

---

# August 21, 2026

**Version:** `3.0.0-pre4`

### Improved

- Made startup more resilient when consent, permissions, or optional initialization cannot complete.
- Added safe fallback behavior when startup initialization fails.

### Fixed

- Fixed startup becoming permanently stuck on the loading screen.
- Fixed startup failing to complete when optional initialization work throws an error.
- Fixed missing onboarding dependency injection configuration.
- Fixed one-time startup actions being lost before the UI begins collecting them.
- Fixed repeated permission and consent work during startup.

---

# August 20, 2026

**Version:** `3.0.0-pre3`

### Changed

- Advanced the App Toolkit 3.0 preview publishing version.

No meaningful consumer-facing library behavior changed in this preview.

---

# August 16, 2026

**Version:** `3.0.0-pre2`

### Changed

- Refined the internal file and package structure of reusable components.

No public library behavior changed.

---

# August 15, 2026

**Version:** `3.0.0-pre1`

The first preview of the redesigned App Toolkit 3.0 architecture.

### Added

- Added a unified `appToolkitModules(...)` entry point for loading the standard App Toolkit
  dependency graph.
- Added automated dependency-graph verification for library integrations.
- Added manifest contract tests to prevent reusable modules from overriding host application
  configuration.
- Added dedicated reusable core, feature, integration, navigation, testing, and DataStore modules.

### Changed

- Split the previous App Toolkit structure into focused reusable modules.
- Split shared preferences into responsibility-specific data sources for themes, display,
  onboarding, consent, ads, review state, changelog state, favorites, and general application state.
- Reorganized reusable navigation contracts into dedicated modules.
- Reworked repository and data-layer boundaries to reduce unnecessary abstraction.
- Removed duplicate DataStore ownership.
- Updated publishing so all required App Toolkit modules are exposed correctly.
- Reworked host integration around clearer provider and dependency-injection contracts.

### Improved

- Reduced unnecessary Compose recompositions across reusable components.
- Improved shared loading and animation performance.
- Improved reusable modifier performance.
- Improved localization and plural handling across the library.
- Improved compact native ad sizing.
- Improved startup dialog behavior across screen sizes and content lengths.
- Improved host integration so fewer individual Koin modules need to be registered manually.
- Improved testing infrastructure for reusable modules.

### Fixed

- Fixed Mobile Ads SDK initialization crashes.
- Fixed UMP consent crashes.
- Fixed host applications inheriting incorrect toolkit theme configuration.
- Fixed RTL configuration being lost through manifest ownership.
- Fixed duplicate DataStore instances.
- Fixed release resource-linking failures after modularization.
- Fixed incorrect Material theme dependency ownership.
- Fixed API 26 to 28 vibration compatibility issues.
- Fixed mismatched ad preference defaults.
- Fixed dependency injection errors that previously appeared only when affected screens were opened.
- Fixed several publishing, manifest, resource, and module ownership issues.

---

# August 4, 2026

**Version:** `2.0.19`

### Changed

- Updated library publishing and build configuration in preparation for App Toolkit 3.0.

No public library behavior changed.

---

# August 4, 2026

**Version:** `2.0.18`

### Improved

- Expanded the reusable custom carousel API with configurable corner radius sizing.

---

# July 10, 2026

**Version:** `2.0.17`

### Changed

- Updated library publishing and versioning configuration.

---

# July 9, 2026

**Version:** `2.0.16`

### Changed

- Updated library publishing and versioning configuration.

---

# July 8, 2026

**Version:** `2.0.15`

### Changed

- Updated library publishing and versioning configuration.

---

# July 8, 2026

**Version:** `2.0.14`

### Changed

- Updated library publishing and versioning configuration.

---

# April 17, 2026

**Version:** `2.0.12`

### Added

- Added an `enabled` parameter to `AnimatedIconButtonDirection`.

### Improved

- Updated reusable dependencies and Navigation 3 foundations.

---

# March 30, 2026

**Version:** `2.0.11`

### Fixed

- Fixed Collapsed Toolbar state handling.

### Improved

- Updated reusable dependencies.

---

# March 26, 2026

**Version:** `2.0.10`

### Changed

- Updated reusable Google Android color palettes.

### Improved

- Updated shared Compose and AndroidX dependencies.

---

# March 24, 2026

**Version:** `2.0.9`

### Improved

- Updated shared dependencies.
- Improved error handling in reusable repositories and utilities.

---

# March 7, 2026

**Version:** `2.0.8`

### Changed

- Selected navigation labels now use stronger emphasis.
- Refined expressive loading indicators.
- Restyled grouped Help feedback components.

### Improved

- Improved icon sizing across reusable buttons, app bars, drawers, and navigation rails.
- Improved accessibility for icon-only controls and dialog actions.

### Fixed

- Fixed feedback sheet icon alignment.
- Fixed grouped preference card presentation.

---

# March 7, 2026

**Version:** `2.0.7`

### Improved

- Updated shared resources and reusable dependencies.
- Improved general library stability.

---

# March 6, 2026

**Version:** `2.0.6`

### Improved

- Updated reusable components and internal resources.
- Updated AndroidX, Compose, Firebase, Ktor, Coil, and build dependencies.

---

# February 21, 2026

**Version:** `2.0.5`

### Changed

- Updated App Toolkit publishing configuration for JitPack.

### Improved

- Improved large-screen foundations for reusable screens.
- Improved navigation drawer state handling.

### Fixed

- Fixed several shared component and state-management issues.

---

# February 20, 2026

**Version:** `2.0.4`

### Changed

- Updated reusable splash screen branding.

---

# January 25, 2026

**Version:** `2.0.1`

### Changed

- Continued migration of reusable screens and components to the newer App Toolkit architecture.

### Improved

- Updated shared dependencies.
- Improved component consistency and internal stability.

---

# January 10, 2026

**Version:** `2.0.0`

A major library release focused on Navigation 3, theming, architecture, and distribution.

### Added

- Added multiple reusable themes and color palettes.
- Added seasonal theme support.
- Added Navigation 3 foundations.

### Changed

- Migrated reusable navigation infrastructure to Navigation 3.
- Made JitPack the primary App Toolkit distribution method.
- Removed the previous Maven Central publishing configuration.

### Improved

- Improved reusable UI performance and responsiveness.
- Reduced library overhead.
- Improved internal architecture and dependency ownership.
- Improved JitPack repository configuration.

### Fixed

- Fixed several shared stability issues.

---

# December 22, 2025

**Version:** `1.1.7`

### Added

- Added centralized Maven publishing coordinates.
- Added generated source and documentation artifacts to Maven publications.
- Added complete POM metadata including project, license, developer, and SCM information.

### Changed

- Added support for switching publishing coordinates between JitPack and Maven environments.
- Centralized publishing configuration.

---

# December 21, 2025

**Version:** `1.1.6`

### Changed

- Updated the published App Toolkit library version to `1.1.6`.

---

# December 21, 2025

**Version:** `1.1.5`

### Added

- Added Blue, Green, Red, Yellow, Monochrome, and Rose palettes.
- Added multiple Material You wallpaper palette variants.
- Added a seasonal Christmas palette.
- Added reusable remote FAQ support with a local fallback.
- Added Firebase Cloud Messaging foundations.
- Added online Help fallback behavior when in-app review is unavailable.

### Changed

- Expanded the reusable theme system around `ColorPalette` providers.

### Improved

- Improved Compose stability.
- Improved native ad lifecycle handling.
- Improved in-app update behavior.
- Improved window inset handling.

---

# December 21, 2025

**Version:** `1.1.4`

### Improved

- Refined reusable UI components.
- Improved overall library performance and stability.

---

# October 3, 2025

**Version:** `1.1.3`

### Changed

- Added circular clipping to reusable dropdown menu items.

---

# September 14, 2025

**Version:** `1.1.2`

### Improved

- Improved shared reliability and reusable components.

---

# August 19, 2025

**Version:** `1.1.1`

### Improved

- Improved reusable startup handling.
- Improved shared navigation interactions.

### Fixed

- Fixed User Messaging Platform crashes.
- Fixed snackbar action colors.
- Fixed shared translation and stability issues.

---

# July 25, 2025

**Version:** `1.1.0`

### Changed

- Updated the published App Toolkit library to `1.1.0`.

### Improved

- Improved reusable startup and changelog foundations.

---

# July 24, 2025

**Version:** `1.0.42`

### Changed

- Updated dependency declarations to use named arguments.

No public library behavior changed.

---

# July 23, 2025

**Version:** `1.0.41`

### Changed

- Updated shared App Toolkit naming and copyright resources.

---

# July 23, 2025

**Version:** `1.0.40`

### Changed

- Removed the `v` prefix from library version references for consistency.

---

# July 23, 2025

**Version:** `1.0.39`

### Improved

- Added haptic feedback, sound feedback, and bounce interaction to expandable sections in the
  reusable Issue Reporter.

---

# July 17, 2025

**Version:** `1.0.38`

### Changed

- Updated reusable dialog dismiss actions to use `OutlinedButton`.
- Corrected an Arabic theme translation.

---

# July 11, 2025

**Version:** `1.0.37`

### Changed

- Consolidated lifecycle helpers into `LifecycleEventsEffect`.
- Renamed the previous lifecycle helper to `ActivityLifecycleEffect`.

### Improved

- Updated reusable Android and Compose dependencies.

---

# July 7, 2025

**Version:** `1.0.36`

### Improved

- Improved reusable ads test coverage and reliability.

---

# June 24, 2025

**Version:** `1.0.35`

### Improved

- Improved reusable side-navigation presentation and behavior.
- Improved loading-state components and animations.
- Improved diagnostic data available to the Issue Reporter.

---

# June 20, 2025

**Version:** `1.0.34`

### Fixed

- Fixed reusable library build issues.

---

# June 20, 2025

**Version:** `1.0.33`

### Improved

- Completed localization updates for reusable resources.

---

# June 20, 2025

**Version:** `1.0.32`

### Changed

- Updated shared library integration and release configuration.

---

# June 19, 2025

**Version:** `1.0.31`

### Changed

- Updated the published App Toolkit library version to `1.0.31`.

---

# June 18, 2025

**Version:** `1.0.30`

### Changed

- Updated the published App Toolkit library version to `1.0.30`.

---

# June 18, 2025

**Version:** `1.0.29`

### Changed

- Marked `OnboardingActivity` with `noHistory` so completed onboarding does not remain in the host
  application's back stack.

---

# June 17, 2025

**Version:** `1.0.28`

### Changed

- Updated the library compile SDK to Android API 36.
- Refined public reusable component APIs and parameter ordering.

---

# June 17, 2025

**Version:** `1.0.27`

### Fixed

- Corrected the French translation for the reusable Open action.

---

# June 12, 2025

**Version:** `1.0.26`

### Improved

- Completed localization updates for reusable review messaging.

---

# June 8, 2025

**Version:** `1.0.25`

### Changed

- Removed obsolete Ads Settings and consent abstractions.
- Simplified reusable Ads Settings integration.

---

# June 7, 2025

**Version:** `1.0.24`

### Added

- Added `AppLocalesMetadataHolderService` support for Android per-app language configuration.

---

# June 6, 2025

**Version:** `1.0.23`

### Improved

- Refined reusable consent-toggle interactions.

---

# June 6, 2025

**Version:** `1.0.22`

### Changed

- Removed redundant bounce animations from standard preference components.

### Improved

- Added haptic feedback to onboarding pager interactions.
- Improved theme onboarding component presentation and clipping.

---

# June 6, 2025

**Version:** `1.0.21`

### Improved

- Expanded KDoc across reusable APIs and components.
- Added haptic drawer interactions and button bounce feedback.
- Improved reusable error handling and error types.
- Updated tooltip components.

---

# June 4, 2025

**Version:** `1.0.20`

### Added

- Added `ScreenHelper` utilities for detecting landscape, tablet, and combined adaptive layouts.

---

# June 4, 2025

**Version:** `1.0.19`

### Added

- Added `TooltipIconButton`.
- Added reusable radio-button and checkbox preference components.
- Added reusable light and dark color-scheme foundations.

---

# June 4, 2025

**Version:** `1.0.18`

### Changed

- Refined shared DataStore constants.

---

# June 3, 2025

**Version:** `1.0.17`

### Changed

- Exposed the underlying DataStore from `CommonDataStore`.

---

# May 28, 2025

**Version:** `1.0.16`

### Added

- Added `CommonDataStore` for shared application preferences.
- Added reusable DataStore key constants.

---

# May 26, 2025

**Version:** `1.0.15`

### Changed

- Updated the published App Toolkit library version to `1.0.15`.

---

# May 18, 2025

**Version:** `1.0.12`

### Added

- Added `OnResumeEffect` for running callbacks when the host lifecycle reaches `ON_RESUME`.

### Improved

- Updated Android Gradle Plugin and reusable dependencies.

---

# May 13, 2025

**Version:** `1.0.11`

### Changed

- Refactored reusable library code and internal organization.

---

# May 9, 2025

**Version:** `1.0.10`

### Added

- Added optional ad support to `NoDataScreen`.

### Changed

- Added configuration parameters for controlling the ad slot and `AdsConfig`.

### Fixed

- Corrected retry-button text handling in `NoDataScreen`.

---

# May 9, 2025

**Version:** `1.0.9`

### Added

- Added one-time action support to the reusable base ViewModel infrastructure.

---

# May 8, 2025

**Version:** `1.0.8`

### Improved

- Updated Gradle, Compose, DataStore, Lifecycle, Navigation, and related reusable dependencies.

---

# May 5, 2025

**Version:** `1.0.7`

### Added

- Added `AnimatedFloatingActionButton` with visibility animation, bounce feedback, and click sound.

---

# May 3, 2025

**Version:** `1.0.6`

### Fixed

- Fixed out-of-bounds animation state access.

### Improved

- Improved shared animation reliability.

---

# May 1, 2025

**Version:** `1.0.5`

### Added

- Added `CustomSnackbarVisuals`.
- Added `DefaultSnackbarHandler`.
- Added `DefaultSnackbarHost`.

### Changed

- Replaced the previous status snackbar implementation with the new reusable snackbar
  infrastructure.
- Refined reusable About and Help state handling.

### Fixed

- Fixed ads preference checking in `AdsCoreManager`.

---

# May 1, 2025

**Version:** `1.0.4`

### Improved

- Simplified reusable animated visibility state handling.
- Improved defensive animation-state access in Help components.

---

# April 17, 2025

**Version:** `1.0.2`

### Changed

- Updated the published App Toolkit library version to `1.0.2`.

---

# April 17, 2025

**Version:** `1.0.1`

### Improved

- Refined reusable navigation, animation, update-checking, About, and ads infrastructure.

---

# April 7, 2025

**Version:** `1.0.0`

The first stable 1.x App Toolkit library release.

### Changed

- Refined reusable settings section shapes and typography.
- Standardized icon sizing across preference and button components.

### Improved

- Improved visual consistency across reusable preference components.

---

# January 30, 2025

**Version:** `0.0.47`

### Changed

- Renamed the reusable app description resource to `app_short_description`.
- Added a reusable `device_info` string resource.

---

# January 30, 2025

**Version:** `0.0.46`

### Improved

- Improved the reusable About screen.
- Added clearer EULA and changelog loading and error messages.
- Improved reusable Settings resource descriptions.
- Updated Compose dependencies.

---

# January 30, 2025

**Version:** `0.0.45`

### Added

- Added reusable app-update notification infrastructure.
- Added reusable app-usage notification infrastructure.
- Added the User Messaging Platform dependency.

### Fixed

- Fixed an incorrect notification summary.

---

# January 30, 2025

**Version:** `0.0.44`

### Added

- Reintroduced reusable app-update and app-usage notification infrastructure.

### Fixed

- Fixed an incorrect notification summary.

---

# January 28, 2025

**Version:** `0.0.43`

### Changed

- Removed an unused shortcut settings icon.

---

# January 28, 2025

**Version:** `0.0.41`

### Added

- Added reusable app-update notification management.
- Added reusable app-usage notification scheduling and workers.
- Added update and important-notification icons.

### Improved

- Improved theme selection and theme summary handling.

---

# January 26, 2025

**Version:** `0.0.40`

### Improved

- Updated translations and reusable dependencies.
- Improved usage and diagnostics descriptions.

---

# January 25, 2025

**Version:** `0.0.39`

### Added

- Added `ThemeSettingsList`.
- Added `UsageAndDiagnosticsList`.
- Added `UsageAndDiagnosticsSettingsProvider`.
- Added `DrawerStyle`.
- Added reusable display, privacy, advanced, and about settings provider contracts.

---

# January 25, 2025

**Version:** `0.0.38`

### Changed

- Refactored reusable theme and privacy settings infrastructure.
- Introduced `DrawerStyle` customization.

---

# January 25, 2025

**Version:** `0.0.37`

### Added

- Added reusable Theme Settings.
- Added system, dark, and light theme selection.
- Added AMOLED mode support.

---

# January 25, 2025

**Version:** `0.0.36`

### Added

- Added reusable Display Settings.
- Added startup-page selection.
- Added language-selection support.
- Added app-language settings integration.
- Expanded reusable Privacy Settings links.

---

# January 24, 2025

**Version:** `0.0.35`

### Changed

- Simplified settings provider contracts by removing unnecessary `Context` parameters.

### Improved

- Updated reusable dependencies and settings implementation.

---

# January 24, 2025

**Version:** `0.0.34`

### Added

- Added a reusable Privacy Settings screen.
- Added privacy policy, terms, code of conduct, permissions, ads, diagnostics, legal-notice, and
  license preferences.
- Added `PrivacySettingsProvider` for host customization.

---

# January 24, 2025

**Version:** `0.0.33`

### Improved

- Improved reusable About Settings state handling.

---

# January 24, 2025

**Version:** `0.0.32`

### Changed

- Improved reusable About Settings integration.
- Expanded `AboutSettingsProvider` with package and version information.
- Simplified reusable intent helpers.

---

# January 24, 2025

**Version:** `0.0.31`

### Added

- Added reusable About Settings.
- Added `AboutSettingsProvider`.
- Added app and device information presentation.

---

# January 24, 2025

**Version:** `0.0.30`

### Fixed

- Prevented Ktor client initialization failures from crashing host applications.

---

# January 24, 2025

**Version:** `0.0.29`

### Changed

- Updated ads behavior to follow persisted user preferences.

---

# January 24, 2025

**Version:** `0.0.28`

### Added

- Added an ads-enabled flag to `AdsCoreManager`.

### Changed

- Ads are initialized and displayed only when enabled.

---

# January 24, 2025

**Version:** `0.0.27`

### Changed

- Moved advertisement SDK initialization from `BaseCoreManager` into `AdsCoreManager`.

---

# January 24, 2025

**Version:** `0.0.26`

### Improved

- Refactored application initialization around coroutines and concurrent work.
- Improved initialization error handling.
- Improved advertisement SDK initialization and loading reliability.

### Added

- Added ad completion callbacks.
- Added the Internet permission required by network-backed integrations.

---

# January 24, 2025

**Version:** `0.0.25`

### Added

- Added `AdsCoreManager`.
- Added App Open ad initialization and presentation support.

---

# January 18, 2025

**Version:** `0.0.24`

Maintenance release.

---

# January 18, 2025

**Version:** `0.0.23`

### Added

- Added reusable `ErrorHandler`.
- Added the `ErrorReporter` contract.
- Added user-facing Snackbar handling for initialization and runtime errors.

### Improved

- Updated core reusable dependencies.

---

# January 16, 2025

**Version:** `0.0.21`

### Improved

- Expanded KDoc across reusable classes and components.
- Added haptic drawer feedback.
- Added bounce interaction feedback for buttons.
- Expanded reusable error types.
- Updated tooltip infrastructure.

---

# January 15, 2025

**Version:** `0.0.20`

### Added

- Added screen utilities for orientation and tablet detection.

---

# January 14, 2025

**Version:** `0.0.19`

### Added

- Added `TooltipIconButton`.
- Added reusable radio-button and checkbox preferences.
- Added reusable light and dark color schemes.

---

# January 13, 2025

**Version:** `0.0.18`

### Changed

- Refined DataStore key infrastructure.

---

# January 13, 2025

**Version:** `0.0.17`

### Changed

- Exposed the DataStore instance from `CommonDataStore`.

---

# January 13, 2025

**Version:** `0.0.16`

### Added

- Added `CommonDataStore`.
- Added shared DataStore preference keys.

---

# January 13, 2025

**Version:** `0.0.15`

### Improved

- Expanded documentation across reusable models, enums, constants, and components.

---

# January 12, 2025

**Version:** `0.0.14`

### Added

- Added `LoadingScreen`.
- Added reusable Snackbar presentation.
- Added `ClipboardHelper`.

### Improved

- Improved app icon loading in the reusable version information dialog.

---

# January 12, 2025

**Version:** `0.0.13`

### Added

- Added `ButtonState`.
- Added debug ad constants.
- Added `NavigationDrawerItem`.
- Added the App Open ad completion listener.
- Added `UiErrorModel`.

### Changed

- Reorganized open-source license utilities.

---

# January 12, 2025

**Version:** `0.0.12`

### Changed

- Updated `VersionInfoAlertDialog` to accept a copyright string resource.

---

# January 12, 2025

**Version:** `0.0.11`

### Changed

- Exposed Coil dependencies through the library API.

### Improved

- Improved changelog and EULA loading reliability.
- Improved reusable HTML state loading.

---

# January 11, 2025

**Version:** `0.0.10`

### Added

- Added `VersionInfoAlertDialog`.
- Added reusable switch, settings, and category preference components.
- Added reusable horizontal and vertical spacer components.
- Added `ErrorType`.
- Added Coil image-loading support.

---

# January 11, 2025

**Version:** `0.0.9`

### Added

- Added an animated extended floating action button.
- Added reusable error categorization.
- Added open-source license, EULA, and changelog utilities.
- Added developer-contact intent helpers.
- Added clipboard Snackbar callbacks.

---

# January 11, 2025

**Version:** `0.0.8`

### Changed

- Moved hardcoded reusable strings into Android resources for localization.

---

# January 11, 2025

**Version:** `0.0.7`

### Changed

- Exposed required dependencies through the library API.

### Improved

- Updated Gradle, Android Gradle Plugin, and reusable dependencies.

---

# January 10, 2025

**Version:** `0.0.6`

### Added

- Added clipboard helpers.
- Added app-sharing helpers.
- Added developer email helpers.
- Added reusable open-source licenses, EULA, and changelog navigation.
- Added a reusable error dialog.

### Improved

- Added Ktor timeouts and default headers.

---

# January 8, 2025

**Version:** `0.0.5`

### Added

- Added reusable About Libraries functionality.

### Improved

- Improved helper APIs and internal organization.

---

# January 8, 2025

**Version:** `0.0.4`

### Added

- Added reusable spacer components.
- Added preference components.
- Added a reusable error dialog.
- Added bug-report and feature-request foundations.

---

# January 8, 2025

**Version:** `0.0.3`

- Initial reusable App Toolkit foundations.

---

# January 8, 2025

**Version:** `0.0.3_pre1`

- Initial preview of the App Toolkit library.

---

# January 8, 2025

**Version:** `v0.0.2`

- Initial library development release.

---

# January 8, 2025

**Version:** `v0.0.1`

- Initial library release.
