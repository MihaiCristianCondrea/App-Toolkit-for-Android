# Main (AppToolkit)

The **Main** feature provides reusable "app shell" building blocks used across AppToolkit-powered apps:
navigation chrome (top app bar, adaptive navigation bar/rail, drawer), common navigation handling,
and
Google Play
Services (GMS) host abstractions used by in-app update / review / consent flows.

This module exists to keep **UI**, **domain contracts**, and **data implementations** separated, while
still giving host apps a ready-to-go default shell that can be customized or replaced.

> If you are integrating AppToolkit into a host app, you can use these defaults as-is, or selectively
> adopt only the components you want and replace the rest with your own UI.

---

## What’s included

### Navigation 3 UI (Compose + Material 3)
- **`MainTopAppBar`**: the default top app bar (title + navigation icon + overflow actions).
- **Host-owned shell scaffolding**: the sample app composes a stable `Scaffold` with a bottom
  navigation bar on compact windows and a navigation rail on larger windows.
- **`NavigationDrawerItemContent`**: a single drawer item row with optional dividers.

### Navigation behavior helpers
- **`handleNavigationItemClick(...)`**: default drawer click handling for common routes:
  Settings, Help & Feedback, Updates (changelog), Share with support for host overrides.
- **`appToolkitNavigationEntryBuilders(...)`**: shared Navigation 3 entries for library-owned
  destinations such as Settings, Help, Support, permissions, licenses, ads settings, and library
  extras.
- **Shared route keys**: library destinations are exposed as `StableNavKey` implementations from the
  library, so host apps can combine them with app-owned route keys in one `NavDisplay`.
- **Scene-specific motion**: host apps can use a stable main-shell `Scene` for top-level routes and
  focused sub-screen scenes whose Nav3 metadata defines activity-like push and pop transitions.
- **Typed entry identity**: scene-based hosts must register each `NavEntry` with its
  `StableNavKey` as `contentKey`; Nav3 otherwise defaults to a string and typed scene routing
  cannot identify the shell destination.
- Navigation key/state serialization guidance: see
  [`docs/general/core/serialization-boundaries.md`](../../../general/core/serialization-boundaries.md).

### App update & GMS host abstractions
- **In-app update flow** (Play Core):
  - `InAppUpdateRepository` (domain contract)
  - `InAppUpdateRepositoryImpl` (data implementation)
  - `RequestInAppUpdateUseCase` (domain use case)
  - `InAppUpdateHost` + `InAppUpdateResult` (domain models)
- **`GmsHostFactory`**: creates host abstractions for:
  - consent (`ConsentHost`)
  - in-app review (`ReviewHost`)
  - in-app updates (`InAppUpdateHost`)

### Changelog UI
- **`ChangelogDialog`**: loads a markdown changelog from a URL (typically a GitHub changelog md file),
  extracts the section for the current app version, and renders it.

### Home screen widgets (Glance)
- **`AppIconsWidget`**: responsive, scrollable app launcher widget built with Jetpack Glance.
- Widget implementation guide: [`widgets.md`](./widgets.md).

---

## Layering and boundaries (why it’s built this way)

AppToolkit enforces **UI → Domain → Data** direction:

- **UI**
  - Contains Compose components and navigation handlers.
  - Does **not** directly talk to Play Core / HTTP / DataStore without going through appropriate
    abstractions or helper modules.

- **Domain**
  - Declares stable contracts and models:
    - `InAppUpdateRepository`, `NavigationRepository`
    - `InAppUpdateHost`, `InAppUpdateResult`, `BottomBarItem`
  - Exposes use cases such as `RequestInAppUpdateUseCase`.

- **Data**
  - Implements domain contracts:
    - `InAppUpdateRepositoryImpl` wraps Play Core APIs.
    - `MainRepositoryImpl` provides default navigation drawer items.

This structure keeps the library maintainable and testable, and allows host apps to replace pieces
without rewriting everything.

---

## Design intent (author notes)

This feature exists because the author maintains many apps and wanted a consistent, reusable "shell"
that feels native and is ready to ship:

- The **navigation rail** supports tablets/foldables/large screens.
- The **navigation drawer** remains available for app-wide actions (settings/help/updates/share),
  while a host-owned shell keeps top-level navigation chrome stable as tab content changes.
- The **changelog dialog** is used to display "what changed" for apps, typically sourced from GitHub,
  keeping release notes discoverable inside the app.
- **GMS host abstractions** exist to avoid leaking concrete `Activity` dependencies into domain/data,
  while still making Play Core flows easy to wire in host apps.

Host apps can adopt the defaults or override any part (UI, routes, handlers, or repositories).

---

## Key types

### `BottomBarItem`

Represents a top-level destination for adaptive navigation UI (navigation bar / rail).

```kotlin
@Immutable
data class BottomBarItem<T : StableNavKey>(
    val route: T,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val title: Int
)
````

### In-app update contracts

* `InAppUpdateHost`: host-provided abstraction (activity + launcher)
* `InAppUpdateResult`: emitted outcome (Started / NotAvailable / NotAllowed / Failed)
* `InAppUpdateRepository`: domain contract exposing `Flow<InAppUpdateResult>`

---

## Usage examples

### 1) Top app bar (`MainTopAppBar`)

`MainTopAppBar` is designed for the library’s default main screen. It supports scroll behavior and a
navigation icon callback. Hosts can hide the default Support overflow and supply destination-owned
actions, such as the Help overflow menu.

```kotlin
MainTopAppBar(
    navigationIcon = Icons.AutoMirrored.Outlined.ArrowBack,
    onNavigationIconClick = { /* open drawer or navigate up */ },
    showSupportAction = false,
    actions = { /* destination-specific actions, if any */ },
    scrollBehavior = scrollBehavior,
)
```

### 2) Adaptive top-level navigation shell

The sample app owns the scaffold explicitly so navigation bars do not enter or exit with
destination content. It selects `NavigationBar` on compact windows and `NavigationRail` on larger
windows while reusing the same item contract.

```kotlin
Scaffold(
    bottomBar = {
        if (compactWindow) {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = item.route == currentRoute,
                        onClick = { onNavigate(item.route) },
                        icon = { /* destination icon */ },
                        label = { Text(stringResource(item.title)) },
                    )
                }
            }
        }
    },
) { paddingValues ->
    /* Animated destination content inside stable chrome */
}
```

For expanded layouts, render the same `items` through `NavigationRailItem` before additional
drawer destinations so every top-level route remains reachable.

### 3) Nav3 scenes and motion

Use a stable main-shell scene key for top-level destinations and a dedicated sub-screen scene for
Settings, Help, Support, and other pushed destinations. Assign transition metadata on each scene:
top-level tab content uses a fade within the mounted shell; transitions crossing between the shell
and a pushed destination, plus transitions between pushed destinations, use activity-like forward,
pop, and predictive-pop motion. This avoids animating the app bar and navigation chrome when
switching tabs and preserves native back motion when returning from a pushed screen. Values
reported by destination content, such as FAB actions, must be delivered through stable state
holders rather than scene-strategy identity so returning content cannot restart an active motion.
Pushed settings, Help, Support, and similar destinations retain their expanded large top app bars
inside the sub-screen shell; only the persistent top-level shell uses the compact app bar.

### 4) Drawer item click handling (`handleNavigationItemClick`)

Use the default drawer handler and override only what you need:

```kotlin
handleNavigationItemClick(
    context = context,
    item = item,
    drawerState = drawerState,
    coroutineScope = scope,
    onChangelogRequested = { showChangelogDialog = true },
    additionalHandlers = mapOf(
        "route_custom" to { /* custom action */ }
    ),
)
```

### 5) In-app update flow (Play Core via `RequestInAppUpdateUseCase`)

The host app provides an `ActivityResultLauncher<IntentSenderRequest>` and builds an `InAppUpdateHost`.
The update request emits a result as a `Flow`.

```kotlin
val gmsHostFactory = GmsHostFactory()
val updateHost = gmsHostFactory.createUpdateHost(
    activity = activity,
    launcher = updateResultLauncher,
)

requestInAppUpdateUseCase(updateHost)
    .onEach { result -> /* handle Started / Failed / NotAllowed / NotAvailable */ }
    .launchIn(scope)
```

---

## Customization points (host apps)

Host apps may replace any of the following:

* **UI**

    * Provide your own top bar / adaptive navigation / drawer UI.
    * Keep `BottomBarItem` and routes, or define your own equivalents.

* **Navigation**

    * Replace `handleNavigationItemClick` with your own handler.
    * Or keep it and provide `additionalHandlers` for app-specific routes.
  * Use `appToolkitNavigationEntryBuilders(...)` when the host shell embeds library-owned
    destinations instead of launching separate activities.

* **Data**

    * Replace `MainRepositoryImpl` to generate different drawer items.
    * Replace `InAppUpdateRepositoryImpl` if you want flexible updates or different policy.

* **Changelog**

    * Change the URL source, the parsing strategy, or remove the feature entirely.
