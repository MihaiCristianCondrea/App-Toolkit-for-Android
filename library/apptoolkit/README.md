# `:library:apptoolkit` Logic Graph

## Purpose

Acts as the host-facing façade and composition root for reusable AppToolkit features. It assembles
Koin modules and Navigation 3 destinations while re-exporting the toolkit modules through Gradle
`api` dependencies.

## Owns

- `appToolkitModules`, the single entry point returning the toolkit's whole Koin graph.
- `appToolkitFoundationModules`, `appToolkitFeatureModules`, and `appToolkitSettingsModules`, the
  granular lists `appToolkitModules` composes.
- `appToolkitNavigationEntryBuilders` for shared embedded destinations.
- Host-to-library composition using `AppToolkitHostBuildConfig` and host provider factories.
- Common host defaults contributed through manifest/resource merging: the AppCompat application
  theme, RTL/window behavior, backup and data-extraction rules, splash resources, and shared colors.

## Does not own

- Feature screens, repositories, use cases, or SDK implementations; those stay in their
  feature/core/integration modules.
- Host application startup, app-specific routes, providers, and business logic, owned by `:sample`.
- Host identity and policy overrides such as the application class, icons, label, locale list,
  AdMob application ID, or any explicit replacement for a toolkit default.

## Depends on

- `:library:core:common`, `:library:core:datastore`, `:library:core:network`, `:library:core:ui`,
  `:library:core:designsystem` and `:library:navigation` to assemble common infrastructure and UI
  contracts.
- `:library:feature:about`, `:library:feature:help`, `:library:feature:issuereporter`,
  `:library:feature:onboarding`, `:library:feature:permissions`, `:library:feature:settings`, and
  `:library:feature:support` to provision toolkit ViewModels, repositories, and destinations.
- `:library:integration:ads`, `:library:integration:billing`, `:library:integration:consent`,
  `:library:integration:firebase`, `:library:integration:review`, and `:library:integration:update`
  to connect SDK implementations.

All dependencies are exported with `api`, making this a convenience façade rather than an isolation
boundary.

## Used by

- `:sample`, which loads the assembled DI modules and navigation builders.

## Flow chart

```mermaid
flowchart TD
    Host[Host config and providers] --> Facade[":library:apptoolkit"]
    Facade --> DI[Koin module lists]
    Facade --> Nav[Navigation entry builders]
    DI --> Features[Feature repositories and ViewModels]
    DI --> Integrations[SDK integrations]
    Nav --> Screens[Embedded toolkit screens]
```

## Public contracts

- `appToolkitModules`, the three DI module-list factories it composes, and
  `appToolkitNavigationEntryBuilders`.
- Transitive APIs from all `api(project(...))` dependencies are also visible to consumers.

### Manifest and resource defaults

Depending on this facade contributes the common `<application>` defaults needed by toolkit
activities, including `@style/AppTheme`, RTL support, resizable/window behavior, and the bundled
backup/data-extraction rules. It also supplies `AppTheme`, `SplashScreenTheme`, their splash assets,
and the shared launcher/shortcut colors. Integration modules contribute the permissions and
metadata they own; for example, ads owns network/ad-ID permissions and Mobile Ads tuning metadata.

These are defaults, not locked policy. Android merges the consuming application's manifest and
resources at higher priority, so a host can override an attribute in its own `<application>` tag or
replace a same-named resource when its product requirements differ. A host should declare only its
identity and intentional overrides rather than copy the defaults.

## Internal implementations

- Koin binding details, qualifier wiring, default palette registration, and private destination
  builders.

## Current risks

The façade exports nearly the complete internal graph, so consumers can couple to implementation
modules transitively. Its DI files also instantiate implementations owned by many other modules,
making constructor changes ripple into this composition module.

## Architecture guards

`RepositoryConventionsTest` runs here rather than in any single feature module, because this is the
only module that depends on every library module. It scans active production sources in `library`
and `sample` and fails when a repository breaks the project-wide convention:

- repository contracts and concrete repositories live in `data/repository`,
- concrete implementations do not use the ambiguous `Impl` suffix,
- a retained `XRepository` interface uses `DefaultXRepository` for its general implementation,
- a single implementation may be the concrete `XRepository` when an interface adds no boundary.

It replaces a hand-written list of interface/implementation pairs checked with `isAssignableFrom`,
which the compiler already guaranteed and which had fallen six repositories behind.

The test reads source files, not the classpath, so production Kotlin sources are declared as an
explicit input of this module's `Test` tasks in `build.gradle.kts`. Removing that declaration lets
Gradle treat the task as up to date after a file moves, which is precisely when the test needs to
run.

### Fixed: `Unable to start activity` from an unresolvable Koin definition

A definition that cannot be created does not fail when Koin starts — it fails the first time
something asks for it. In practice that is `MainActivity.onCreate` resolving `MainViewModel`, which
reports as `RuntimeException: Unable to start activity` caused by `InstanceCreationException`. The
trace names only the outermost ViewModel and the innermost definition, never the dependency that was
actually missing, and the app is dead before its first frame.

`HostKoinGraphTest` in `:sample:app` now resolves the whole graph by reflection in a plain unit
test,
so a missing binding fails the build instead of a launch. It checks two things:

- every definition in the graph `initializeKoin` builds can be resolved;
- the toolkit's own modules ask a host for nothing beyond four documented extension points
  (`SettingsProvider`, `AboutSettingsProvider`, `AdvancedSettingsProvider`, and the default
  `ColorPalette`). Adding a fifth is a new integration requirement that would crash every existing
  host, so it has to be added to that list deliberately.

Two mechanics are easy to get wrong when editing that test. Koin resolves a definition against its
own module plus that module's `includes`, so the graph must be wrapped as
`module { includes(allModules) }` — verifying a flat list makes every cross-module dependency read
as
missing. And `verify` reflects on the produced type's constructor regardless of how the definition
builds it, so types created by a factory function (`HttpClient`, `ColorPalette`) need their
constructor parameters listed as externally supplied rather than the check being skipped.

### Host integration: one entry point

`appToolkitModules(hostBuildConfig, startupProviderFactory)` returns every module the toolkit
needs, `:library:integration:billing` and `:library:integration:firebase` included. A host that
loads it has a complete toolkit graph and does not track which Gradle module owns which binding.

Before 3.0.0-pre2 a host assembled the graph from three module-list factories plus the loose
`firebaseModule` and `billingModule` values. Missing one produced no build error — only a
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
