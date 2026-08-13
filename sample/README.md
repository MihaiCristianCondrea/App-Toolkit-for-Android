# `:sample` Logic Graph

## Purpose

Provides the installable App Toolkit for Android application and demonstrates how a host composes the reusable toolkit libraries with app-specific features, providers, resources, and platform components.

## Owns

- The Android application, manifest, startup `Application`, `MainActivity`, root screen, host navigation graph, and Koin bootstrap.
- App-specific tools/tiles and Android services, developer-app browsing/favorites, component showcase, and apps widget.
- Host implementations of startup, onboarding, settings, build/ad configuration, DataStore, and navigation provider contracts.
- Application resources, localization, store listing, icons, and host Firebase/Play configuration.

## Does not own

- Reusable AppToolkit feature screens and infrastructure, owned by `:library:*` modules.
- Shared DI/navigation assembly, owned by [`:library:apptoolkit`](../library/apptoolkit/README.md).
- SDK-neutral contracts and reusable UI/theme components, owned by the corresponding core modules.

## Depends on

- [`:library:apptoolkit`](../library/apptoolkit/README.md) for shared DI and navigation composition.
- `:library:core:common`, `:library:core:ui`, `:library:core:designsystem`, and `:library:navigation` for host contracts, presentation foundations, theming, and navigation.
- `:library:feature:about`, `:library:feature:help`, `:library:feature:issuereporter`, `:library:feature:onboarding`, `:library:feature:permissions`, `:library:feature:settings`, and `:library:feature:support` because the host exposes and configures those screens.
- `:library:integration:ads`, `:library:integration:billing`, `:library:integration:consent`, `:library:integration:firebase`, `:library:integration:review`, and `:library:integration:update` to install the optional SDK behavior.

The application does not directly declare `:library:core`, `:library:core:datastore`, or `:library:core:network`; those arrive through the façade/feature graph.

## Used by

No internal module depends on `:sample`; it is the application entry point.

## Flow chart

```mermaid
flowchart TD
    App[AppToolkit Application] --> Koin[Host Koin bootstrap]
    Koin --> Facade[AppToolkit DI module lists]
    Koin --> HostBindings[Host providers and repositories]
    Main[MainActivity / MainScreen] --> Nav[Host Navigation 3 graph]
    Nav --> HostFeatures[Apps, tiles, components]
    Nav --> ToolkitBuilders[AppToolkit destination builders]
    HostFeatures --> Logic[Host repositories]
    Logic --> Android[Android sensors, packages, services, DataStore]
```

## Public contracts

This is an application module and is not intended as a library API. Its important integration surface is the set of host provider implementations and configuration values passed into AppToolkit DI factories.

## Internal implementations

- Developer-app remote/local repositories, DTO mapping, favorites persistence, and installed-app inspection.
- Concrete quick-tool data repositories, domain models/use cases with actual policy, Compose tools, Quick Settings tiles, and the caffeine service.
- Main/components/apps-list ViewModels and UI, widget implementation, host DI modules, and navigation builders.

## Current risks

The host module still owns substantial reusable-looking apps-list, quick-tools, widget, and component-showcase business logic. Those flows cannot currently be reused without depending on the application module or extracting dedicated feature modules.

## Migration notes

The current worktree reflects an ongoing migration from the former `:app` project to `:sample` and from the old package namespace to the published library/sample namespaces. Avoid restoring deleted `app/` sources while completing modularization.

Quick-tool repositories intentionally use concrete `XRepository` classes. They each wrap one
Android platform source, have no alternate implementation, and do not cross a module boundary;
adding matching interfaces or pass-through use cases would not create useful substitution.

The apps-list, navigation-drawer and components-showcase use cases were removed for the same reason
in reverse: each forwarded one call to a repository that already logged the breadcrumb the use case
claimed to add, so ViewModels now call those repositories directly. The unlock write moved to
`ComponentsShowcaseRepository` rather than disappearing — dropping its use case without an owner
would have left a ViewModel holding `DatastoreInterface`, a data source, with no repository between
them. Use cases that carry real policy stay.
