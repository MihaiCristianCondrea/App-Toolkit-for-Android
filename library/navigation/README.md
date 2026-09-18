# `:library:navigation` Logic Graph

## Purpose

Defines navigation models, route identifiers, repository contracts, back-stack operations, and
transition helpers shared by host and feature UI.

## Owns

- `NavigationDestination`, `MainNavigationItem`, `NavigationDrawerItem`, and `BottomBarItem` models.
- `StableNavKey` and the reusable typed AppToolkit route keys.
- Drawer route identifiers and the repository contract hosts implement to supply items.
- Back-stack mutation helpers.
- Shared activity and bottom-navigation transitions.
- Click and selection state for navigation icons; reusable AVD resources live in DesignSystem.
- Bottom navigation, navigation rail, drawer-item content, and hide-on-scroll shell rendering.
- `DefaultNavigationRepository`, the standard four-entry drawer list, and the labels that name it.

## Does not own

- Destination registration, owned by `:library:apptoolkit` and host composition roots.
- The icon slot and its rendering, owned by [`:library:core:designsystem`](../core/designsystem/README.md).
- `MainTopAppBar`, owned by [`:library:core:ui`](../core/ui/README.md).
- Host-app routes and the root navigation graph, owned by `:sample`.

## Depends on

- [`:library:core:common`](../core/common/README.md) for shared sizing constants.
- [`:library:core:designsystem`](../core/designsystem/README.md) for interaction feedback, global
  UI preference values, and the `ToolkitIcon` slot navigation items expose.
- Navigation 3, Compose, and immutable collections materially define the module's public role.

## Used by

- `:sample` for host navigation.
- `:library:apptoolkit` and `:library:core:ui` for shared destination registration and navigation
  UI.
- `:library:feature:about`, `:library:feature:help`, `:library:feature:issuereporter`,
  `:library:feature:onboarding`, `:library:feature:permissions`, `:library:feature:settings`, and
  `:library:feature:support` for feature routes and navigation surfaces.

## Flow chart

```mermaid
flowchart TD
    Host[Host composition root] --> Builders[Feature entry builders]
    Builders --> Entries[Navigation 3 entries]
    Keys[StableNavKey route values] --> Entries
    Keys --> Type{Destination type}
    Type -->|TopLevel| Top[navigateTopLevel trims nested entries]
    Type -->|ActivityLike or Nested| Single[navigateSingleTop]
    Top --> Stack[SnapshotStateList back stack]
    Single --> Stack
    Stack --> Scenes[Host scene / destination content]
    Items[Bottom bar / rail / drawer models] --> Shell[Navigation shell composables]
    Stack --> Shell
    Transitions[Shared activity and bottom-nav transitions] --> Scenes
```

## Architectural decisions

- Route keys are typed, parcelable, and Compose-stable so host and library destinations share one
  Navigation 3 vocabulary without string parsing.
- Destination type is data on the route contract. Back-stack helpers validate top-level navigation
  and suppress duplicate single-top entries.
- This module owns shell rendering and mutation primitives but not destination registration; only
  the host/toolkit composition roots know the complete feature set.
- The drawer repository contract and its default four-entry implementation live together here,
  because the entries name navigation destinations this module already owns. Keeping the default in
  a feature module forced that feature to own navigation labels it did not otherwise use.
- `MainTopAppBar` lives in [`:library:core:ui`](../core/ui/README.md), not here. It is built from
  that module's buttons and dropdown, and `:library:core:ui` already depends on this module for
  `StableNavKey`, so hosting the app bar here would invert that edge into a dependency cycle.

## Public contracts

- Navigation destination/item models, including `NavigationDrawerItem` and `BottomBarItem`, whose
  `icon` and `selectedIcon` are `ToolkitIcon` values. The `animatedIcon` constructor accepts a single
  `ToolkitIcon.Animated` and uses it for both states, preserving Restart/Reverse behavior. See [the design system README](../core/designsystem/README.md#toolkit-icon-api).
- `StableNavKey` and `AppToolkitNavKey` route implementations.
- `NavigationDrawerRoutes` and `navigation.data.repositories.NavigationRepository`.
- Back-stack action extensions and transition helpers.
- `BottomNavigationBar`, `LeftNavigationRail`, `NavigationDrawerItemContent`, `NavigationDrawerSheet`, and
  `HideOnScrollBottomBar`.

## Internal implementations

- Compose rendering and transition specifications remain implementation helpers; destination
  registration stays with consumers.

## Current risks

The module mixes navigation models with Compose rendering and animation, so non-UI consumers still
receive a UI-oriented artifact.
