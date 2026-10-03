# `:library:apptoolkit` Logic Graph

## Purpose

Acts as the host-facing entry point and composition root for reusable AppToolkit features. It assembles
Koin modules and the Toolkit's pages of the shell graph while re-exporting the toolkit modules
through Gradle `api` dependencies.

## Owns

- `appToolkitModules`, the single entry point returning the toolkit's whole Koin graph.
- `appToolkitFoundationModules`, `appToolkitFeatureModules`, and `appToolkitSettingsModules`, the
  granular lists `appToolkitModules` composes.
- `toolkitGraph { }` and `ShellGraphBuilder.toolkitPages()`, which register the Toolkit's pages in
  an app's shell graph. See [Navigation](#navigation).
- `toolkitFooter(onShowUpdates)`, the drawer footer every Toolkit app ends with. See
  [The drawer's footer](#the-drawers-footer).
- `PermissionUsageActivity` and `permissionUsageGraph()`: the screen Android opens from the
  information icon beside the app. See [Permission usage](#permission-usage).
- Host-to-library composition using `AppToolkitHostBuildConfig` and host provider factories.
- Common host defaults contributed through manifest/resource merging: the AppCompat application
  theme, RTL/window behavior, backup and data-extraction rules, locale configuration resource,
  splash resources, shared colors, and host identity fallbacks.

## Does not own

- Feature screens, repositories, use cases, or SDK implementations; those stay in their
  feature/core/integration modules.
- Host application startup, app-specific routes, providers, and business logic, owned by `:sample`.
- Host identity and policy overrides such as the application class, icons, label, locale list,
  AdMob application ID, or any explicit replacement for a toolkit default.

## Depends on

- `:library:core:common`, `:library:core:datastore`, `:library:core:network`, `:library:core:ui`,
  `:library:core:designsystem`, `:library:navigation` and `:library:shell` to assemble common
  infrastructure, UI contracts and the shell.
- Every `:library:feature` module to provision toolkit ViewModels and repositories and to call each
  feature's page registration.
- `:library:integration:ads`, `:library:integration:billing`, `:library:integration:consent`,
  `:library:integration:firebase`, `:library:integration:review`, and `:library:integration:update`
  to connect SDK implementations.

All dependencies are exported with `api`, making this a convenience module rather than an isolation
boundary.

## Used by

- `:sample`, which loads the assembled DI modules and builds its graph with `toolkitGraph { }`.

## Flow chart

```mermaid
flowchart TD
    Host[Host application] --> Config[AppToolkitHostBuildConfig]
    Host --> Providers[Startup and settings provider factories]
    Config --> Modules[appToolkitModules]
    Providers --> Modules
    Modules --> Foundation[appToolkitFoundationModules]
    Modules --> Settings[appToolkitSettingsModules]
    Modules --> Features[appToolkitFeatureModules]
    Foundation --> Core[Core repositories and shared services]
    Settings --> ProviderDefaults[Default host extension bindings]
    Features --> FeatureVMs[Feature repositories and ViewModels]
    Features --> Integrations[SDK-backed integrations]
    Host --> Graph["toolkitGraph { }"]
    Graph --> Pages[Toolkit pages, unless the app registered the key]
    Pages --> Screens[Toolkit screens in the page frame]
    Manifest[AppToolkit manifest and resources] -->|manifest/resource merge| Host
```

## Architectural decisions

- This module is a composition boundary, not an implementation layer: constructors, SDK behavior,
  and Koin bindings remain owned by their source modules; this module only assembles their public
  DI modules.
- The all-in-one `appToolkitModules` entry point includes every toolkit-owned binding. A host still
  supplies the documented settings/startup provider contracts; granular factories remain public
  for hosts intentionally building a partial graph.
- All production child modules are `api` dependencies so a host can configure their public types;
  this convenience intentionally trades away strict implementation hiding.
- Manifest and resource values are overridable defaults. Product identity and policy remain owned
  by the consuming application, which has higher merger priority.

## Public contracts

- `appToolkitModules`, the three DI module-list factories it composes, `toolkitGraph`, and
  `toolkitPages`.
- Transitive APIs from all `api(project(...))` dependencies are also visible to consumers.

### Manifest and resource defaults

Depending on this module contributes the common `<application>` defaults needed by toolkit
activities, including `@style/AppTheme`, RTL support, resizable/window behavior, and the bundled
backup/data-extraction rules. It also supplies `AppTheme`, `SplashScreenTheme`, their splash assets,
the shared launcher/shortcut colors and shortcut artwork, `config_locales.xml`, plus `App Name`
placeholders for `app_name` and `app_full_name`, and the copyright resource. Integration modules
contribute the permissions and metadata they own; for example, ads owns network/ad-ID permissions
and Mobile Ads tuning metadata.

A module ships `src/main/AndroidManifest.xml` only when it contributes manifest entries. AGP takes
each module's namespace from Gradle, so modules with nothing to merge, such as
`:library:integration:consent` or `:library:core:datastore`, have no manifest. The
`ManifestContractTest` rejects an empty placeholder manifest in any `library` or `sample` module.

Android's manifest merger does not carry `android:localeConfig` from a library into the final
application manifest. A host using the bundled locale list therefore keeps the one-line
`android:localeConfig="@xml/config_locales"` application attribute while the XML list itself remains
owned here.

These are defaults, not locked policy. Android merges the consuming application's manifest and
resources at higher priority, so a host can override an attribute in its own `<application>` tag or
replace a same-named resource when its product requirements differ. A host should declare only its
identity and intentional overrides rather than copy the defaults. In particular, hosts customize
their product identity by defining `app_name`, `app_full_name`, and `copyright` with the same names.

## Internal implementations

- Koin module-list composition and qualifier wiring. Individual feature modules own their DI
  definitions and default palette registration.

## Publishing

App Toolkit is published through [JitPack](https://jitpack.io/#MihaiCristianCondrea/App-Toolkit-for-Android).

## Current risks

The module exports nearly the complete internal graph, so consumers can couple to implementation
modules transitively. Its module-list functions must stay synchronized with the feature
modules they compose.

## Navigation

`toolkitGraph { }` lives in `app.main.ui.navigation`. It runs the app's builder first and then
`toolkitPages()`, which calls each feature's own registration. Every registration uses
`pageIfAbsent`, so a key the app registered itself keeps the app's page:

| Registration | Module | Keys | Pane role |
|---|---|---|---|
| `settingsPage()` | settings | `SettingsRoute` | List |
| `displaySettingsPage()` | display | `DisplaySettingsRoute` | Detail |
| `themeSettingsPage()` | theme | `ThemeSettingsRoute` | None |
| `privacySettingsPage()` | privacy | `PrivacySettingsRoute` | Detail |
| `diagnosticsSettingsPage()` | diagnostics | `DiagnosticsSettingsRoute` | None |
| `permissionsPage()` | permissions | `PermissionsRoute` | None |
| `adsSettingsPage()` | integration:ads | `AdsSettingsRoute` | None |
| `advancedSettingsPage()` | advanced | `AdvancedSettingsRoute` | Detail |
| `aboutPages()` | about | `AboutRoute`, `LibraryExtrasRoute` | Detail, None |
| `licensesPage()` | licenses | `LicensesRoute` | None |
| `helpPage()` | faq | `HelpRoute` | None |
| `supportPage()` | support | `SupportRoute` | None |
| `developerOptionsPage()` | developer | `DeveloperOptionsRoute` | None |
| `startupPage()` | startup | `StartupRoute`, as a start screen | None |
| `onboardingPages()` | onboarding | `OnboardingRoute`, as a start screen | None |

The settings categories are details, so on a wide window they open beside the list. Pages opened
from a detail are `None`: a detail opened from a detail replaces it instead of stacking on it.

`toolkitPages()` is the only place that names every feature. The features know nothing of each
other and open one another's pages by key, which `checkModuleBoundaries` enforces: no
`:library:feature` module may depend on another.

It registers pages only; the app decides where each is offered (`settings()` and `supportUs()` in
the drawer and overflow builders, links, or a screen's button). An app that needs to pass a
registration arguments calls it in its own builder first, such as `aboutPages { ... }`. An app that
builds its graph with `ShellGraphBuilder` directly calls `toolkitPages()` after its own
destinations.

### The drawer's footer

Every Toolkit app ends its drawer the same way: Settings, Help and feedback, Updates and Share, in
that order, pinned to the bottom. `toolkitFooter(onShowUpdates)` adds them to the drawer's footer,
which the graph always places after the app's own entries:

```kotlin
drawer {
    link(DownloadsRoute, R.string.downloads, ToolkitIcon.Vector(Icons.Outlined.Download))
    toolkitFooter(onShowUpdates = { showChangelog = true })
}
```

Pass `null` for `onShowUpdates` to leave Updates out. The developer options are not a drawer entry:
the advanced settings offer them once the About screen's version easter egg is found.

## Permission usage

Android's permission manager (from each permission's page) and its privacy dashboard show an
information icon beside an app that declares an activity for `VIEW_PERMISSION_USAGE` and
`VIEW_PERMISSION_USAGE_FOR_PERIOD`, protected by `START_VIEW_PERMISSION_USAGE`. This module's
manifest declares `PermissionUsageActivity` for both, so every app built on the Toolkit gets the
icon without declaring anything. Tapping it opens the privacy page.

- **Only the system can open it.** `START_VIEW_PERMISSION_USAGE` is held by the permission
  controller alone, which is why the activity may be exported.
- **It opens over the system's settings.** It is a second activity on purpose: routing the intent
  to the app's own activity would bring the app's task forward (for a `singleTask` activity, push
  the page onto whatever the person had open) and back would stay in the app. Its empty
  `taskAffinity` keeps it out of the app's task even when the caller starts a new one.
- **It is a shell of its own.** It hosts `ShellHost` with `permissionUsageGraph()`, a graph of
  pages only: the privacy page as its start, and the permissions, ads and diagnostics pages it
  links to. Back from the privacy page, gesture or arrow, finishes it.
- **It uses the Toolkit's pages.** It cannot see the app's graph, so a page the app registered for
  one of these keys is not used here, though the app's `PrivacySettingsProvider` is. An app that
  wants its own screen removes this one and declares its own:

```xml
<activity
    android:name="com.mihaicristiancondrea.android.libs.apptoolkit.app.privacy.ui.PermissionUsageActivity"
    tools:node="remove" />
```

`PermissionUsageActivityTest` launches it under Robolectric with both actions, and
`ManifestContractTest` checks its declaration.

## Architecture guards

`RepositoryConventionsTest` runs here rather than in any single feature module, because this is the
only module that depends on every library module. It scans active production sources in `library`
and `sample` and fails when a repository breaks the project-wide convention:

- repository contracts and concrete repositories live in `data/repositories`,
- concrete implementations do not use the ambiguous `Impl` suffix,

It also checks production package/directory alignment and prevents sample storage and Issue Reporter data sources from importing core UI helpers.

The repository-placement check excludes `:library:core:testing`, whose shared test fixtures live
in `src/main` for consumption through `testImplementation`. Package alignment and repository naming
checks still cover those fixtures.

It replaces a hand-written list of interface/implementation pairs checked with `isAssignableFrom`,
which the compiler already guaranteed and which had fallen six repositories behind.

The test reads source files, not the classpath, so production Kotlin sources are declared as an
explicit input of this module's `Test` tasks in `build.gradle.kts`. Removing that declaration lets
Gradle treat the task as up to date after a file moves, which is precisely when the test needs to
run.

### Fixed: `Unable to start activity` from an unresolvable Koin definition

A definition that cannot be created does not fail when Koin starts, it fails the first time
something asks for it. In practice that is `MainActivity.onCreate` resolving `MainViewModel`, which
reports as `RuntimeException: Unable to start activity` caused by `InstanceCreationException`. The
trace names only the outermost ViewModel and the innermost definition, never the dependency that was
actually missing, and the app is dead before its first frame.

`HostKoinGraphTest` in `:sample:app` now resolves the sample's whole graph by reflection in a plain
unit test, so a missing constructor dependency fails the build instead of a launch. It checks two
things:

- every definition in the graph `initializeKoin` builds can be resolved;
- the sample adapter plus the accepted external/factory types can satisfy the toolkit graph.

This is not a complete generic-host contract test. Reflection verification sees constructor
dependencies, but it cannot discover `koinInject()` calls inside composables such as
`DisplaySettingsProvider` and `PrivacySettingsProvider`. The sample's feature modules provide their
own settings bindings, while `:sample:core:apptoolkit` supplies the default privacy provider.
[`:sample:app`](../../sample/app/README.md) connects them and directly tests provider resolution.

Two mechanics are easy to get wrong when editing that test. Koin resolves a definition against its
own module plus that module's `includes`, so the graph must be wrapped as
`module { includes(allModules) }`, verifying a flat list makes every cross-module dependency read
as
missing. And `verify` reflects on the produced type's constructor regardless of how the definition
builds it, so types created by a factory function (`HttpClient`, `ColorPalette`) need their
constructor parameters listed as externally supplied rather than the check being skipped.

### Host integration: one entry point

`appToolkitModules(hostBuildConfig, startupProviderFactory)` returns every module the toolkit owns,
`:library:integration:billing` and `:library:integration:firebase` included. A host does not track
which Gradle module owns those bindings, but it must add implementations of the host provider
contracts used by the settings/about surfaces. The sample wraps both steps in
`appToolkitHostModules`.

Before 3.0.0-pre2 a host assembled the graph from three module-list factories plus the loose
`firebaseModule` and `billingModule` values. Missing one produced no build error, only a
`NoDefinitionFoundException` the first time the app touched that dependency. Smart Cleaner shipped
without `billingModule` and died on first resume.

Hosts override toolkit defaults by load order. Koin's `allowOverride` defaults to true, so
definitions loaded after the toolkit replace it:

```kotlin
modules(
    buildList {
        addAll(appToolkitModules(hostBuildConfig, ::AppStartupProvider))
        add(hostSettingsProvidersModule)  // overrides the toolkit's defaults
        add(appModule)
    }
)
```

The granular factories stay public for hosts that need to compose a partial graph.

## Migration notes

Bindings were moved out of the sample app so other hosts can integrate the toolkit using explicit
host configuration and provider factories.
