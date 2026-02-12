# Main (AppToolkit)

The **Main** feature provides reusable "app shell" building blocks used across AppToolkit-powered apps:
navigation chrome (top app bar, bottom bar, rail, drawer), common navigation handling, and Google Play
Services (GMS) host abstractions used by in-app update / review / consent flows.

This module exists to keep **UI**, **domain contracts**, and **data implementations** separated, while
still giving host apps a ready-to-go default shell that can be customized or replaced.

> If you are integrating AppToolkit into a host app, you can use these defaults as-is, or selectively
> adopt only the components you want and replace the rest with your own UI.

---

## What’s included

### Navigation 3 UI (Compose + Material 3)
- **`MainTopAppBar`**: the default top app bar (title + navigation icon + overflow actions).
- **`BottomNavigationBar`**: bottom bar for compact layouts, with haptics + click sounds, and optional labels.
- **`LeftNavigationRail`**: navigation rail for tablets/foldables/large screens (collapsed/expanded).
- **`NavigationDrawerItemContent`**: a single drawer item row with optional dividers.
- **`HideOnScrollBottomBar`**: bottom bar container that collapses as content scrolls.

### Navigation behavior helpers
- **`handleNavigationItemClick(...)`**: default drawer click handling for common routes:
  Settings, Help & Feedback, Updates (changelog), Share with support for host overrides.

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
- The **navigation drawer** supports phone-first layouts.
- The **changelog dialog** is used to display "what changed" for apps, typically sourced from GitHub,
  keeping release notes discoverable inside the app.
- **GMS host abstractions** exist to avoid leaking concrete `Activity` dependencies into domain/data,
  while still making Play Core flows easy to wire in host apps.

Host apps can adopt the defaults or override any part (UI, routes, handlers, or repositories).

---

## Key types

### `BottomBarItem`
Represents a top-level destination for the bottom bar / navigation rail.

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
navigation icon callback.

```kotlin
MainTopAppBar(
    navigationIcon = Icons.AutoMirrored.Outlined.ArrowBack,
    onNavigationIconClick = { /* open drawer or navigate up */ },
    scrollBehavior = scrollBehavior,
)
```

### 2) Bottom navigation (`BottomNavigationBar`)

`BottomNavigationBar` supports:

* current route selection
* optional label visibility (driven by the library’s common DataStore)
* click sounds + context haptics

```kotlin
BottomNavigationBar(
    currentRoute = currentRoute,
    items = items,
    onNavigate = { route -> /* navigate */ },
)
```

### 3) Drawer item click handling (`handleNavigationItemClick`)

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

### 4) In-app update flow (Play Core via `RequestInAppUpdateUseCase`)

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

    * Provide your own top bar / bottom bar / rail / drawer UI.
    * Keep `BottomBarItem` and routes, or define your own equivalents.

* **Navigation**

    * Replace `handleNavigationItemClick` with your own handler.
    * Or keep it and provide `additionalHandlers` for app-specific routes.

* **Data**

    * Replace `MainRepositoryImpl` to generate different drawer items.
    * Replace `InAppUpdateRepositoryImpl` if you want flexible updates or different policy.

* **Changelog**

    * Change the URL source, the parsing strategy, or remove the feature entirely.