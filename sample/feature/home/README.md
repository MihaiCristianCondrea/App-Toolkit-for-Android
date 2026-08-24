# `:sample:feature:home` Logic Graph

## Purpose

The application shell: the scaffold, navigation drawer, bottom bar and FAB that host every
destination.

## Owns

- `MainScreen` and `MainShell`, the drawer, the FAB, and the changelog dialog trigger.
- `MainViewModel` and its contracts.
- `NavigationConfigurationRepository`, which exposes the persisted showcase access flag, and
  `AppNavigationItemsProvider`, which maps that configuration to drawer presentation models.

## Does not own

- Which destinations exist. `MainScreen` receives its entry builders as a parameter; `:sample:app`
  supplies them.
- `MainActivity`, which lives in `:sample:app` because it is the launcher activity and the place
  where the feature set is named.

## Depends on

- `:sample:feature:components` for `ComponentsActivity`, the drawer's showcase target.
- `:sample:core:navigation`, `:sample:core:datastore`.
- [`:library:apptoolkit`](../../../library/apptoolkit/README.md) for the toolkit's own destinations,
  top bar and drawer content.

## Used by

- `:sample:app`.

## Flow chart

```mermaid
flowchart TD
    App[":sample:app"] -->|start route and builders| Screen[MainScreen]
    Screen --> BackStack[Navigation 3 back stack]
    Screen --> Scene{Window / destination scene selection}
    Scene --> Shell[MainShell]
    Shell --> Drawer[Navigation drawer]
    Shell --> Bottom[Bottom bar or rail]
    Shell --> Fab[Contextual FAB]
    Screen --> VM[MainViewModel]
    VM --> Provider[AppNavigationItemsProvider]
    Provider --> NavRepo[NavigationConfigurationRepository]
    NavRepo --> Store[DatastoreInterface unlock Flow]
    Provider --> Drawer
    App --> Builders[appNavigationEntryBuilders]
    Builders --> Entries[Host and toolkit destinations]
    Entries --> Scene
    Drawer --> BackStack
    Bottom --> BackStack
```

## Architectural decisions

- The app injects destination builders into `MainScreen`; the shell never imports the full feature
  graph and can be tested with a smaller set.
- The back stack is the navigation source of truth. Drawer, bottom/rail, FAB, and adaptive scene
  selection are projections of the current destination and window state.
- Conditional navigation items come from an observable repository/provider pipeline so the drawer
  updates when the showcase is unlocked without owning persistence.

## Public contracts

- `MainScreen(startRoute, entryBuilders)`, `MainViewModel`, and `NavigationItemsProvider`. The app
  host resolves the persisted startup destination before composing the shell.

## Internal implementations

- Scene selection, drawer/bottom-bar state, random-app handler plumbing.

## Current risks

The shell knows the toolkit's standalone activities (settings, help, support) by class, so adding a
toolkit destination that opens as an activity means editing this module.

## Migration notes

`MainScreen` used to call `appNavigationEntryBuilders` directly, which imported the apps and tiles
features. Taking the builders as a parameter is what lets those features stay leaves; the only
sibling this module still names is `:sample:feature:components`.
