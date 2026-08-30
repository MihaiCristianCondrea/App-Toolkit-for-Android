# `:sample:core:shell` Logic Graph

## Purpose

The application shell: the scaffold, navigation drawer, bottom bar and FAB that host every
destination.

## Owns

- `MainScreen` and `MainShell`, the drawer, the FAB, and the changelog dialog trigger.
- `MainViewModel` and its contracts.
- `NavigationItemsProvider`, the port the shell reads drawer items from. `:sample:app` implements
  it, so the shell renders the drawer without knowing which features fill it.

## Does not own

- Which destinations exist. `MainScreen` receives its entry builders as a parameter; `:sample:app`
  supplies them.
- `MainActivity`, which lives in `:sample:app` because it is the launcher activity and the place
  where the feature set is named.
- `ComponentsActivity`, which lives in `:sample:feature:components`.

## Depends on

- `:sample:core:navigation` and `:sample:core:analytics`.
- [`:library:apptoolkit`](../../../library/apptoolkit/README.md) for the toolkit's own destinations,
  top bar and drawer content.

## Used by

- `:sample:app`.

## Flow chart

```mermaid
flowchart TD
    App[":sample:app"] -->|start route and builders| Screen[MainScreen]
    App -->|activity launch handler| Screen
    Screen --> BackStack[Navigation 3 back stack]
    Screen --> Scene{Window / destination scene selection}
    Scene --> Shell[MainShell]
    Shell --> Drawer[Navigation drawer]
    Shell --> Bottom[Bottom bar or rail]
    Shell --> Fab[Contextual FAB]
    Screen --> VM[MainViewModel]
    VM --> Provider[NavigationItemsProvider]
    App -->|NavigationItemsProvider implementation| Provider
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
- The shell receives an `onLaunchActivity` callback for standalone activities that it doesn't own
  (like the Components showcase), decoupling it from feature modules.
- The back stack is the navigation source of truth. Drawer, bottom/rail, FAB, and adaptive scene
  selection are projections of the current destination and window state.
- Drawer items arrive as a flow through `NavigationItemsProvider`, so an item that appears only
  once a feature unlocks it changes the drawer without the shell knowing why.

## Public contracts

- `MainScreen(startRoute, entryBuilders, onLaunchActivity)`, `MainViewModel`, and `NavigationItemsProvider`. The app
  host resolves the persisted startup destination before composing the shell.

## Internal implementations

- Scene selection, drawer/bottom-bar state, random-app handler plumbing.

## Current risks

The shell still knows some toolkit activities (settings, help, support) by class because they are
part of the library's stable API, but app-specific standalone activities are decoupled.
