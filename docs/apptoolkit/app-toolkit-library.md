# Library Overview

This page documents the **AppToolkit** library and its major components. The library is organized into feature-based packages that can be mixed and matched in Android apps.

## Architecture

AppToolkit follows a modular architecture with clean separation between *data*, *domain*, and *UI* layers. View models handle user events and expose immutable UI state. Repositories abstract data sources such as network clients or `DataStore`.

## Core utilities

The `core` package provides foundational helpers and models:

- Dependency injection qualifiers and coroutine dispatchers.
- Utility helpers for intents, permissions, reviews, clipboard operations, screen sizing and more.
- Domain models for results, errors, navigation drawer items and reusable UI elements.
- Composable UI components such as dialogs, text fields, carousels, snackbars and preference items.

### Core UI catalog

For a grouped inventory of reusable UI building blocks (buttons, dialogs, layouts, preferences, ads,
and text), see:

- `docs/apptoolkit/core/ui-components-catalog.md`

## Adaptive layout support

- Material 3 window size classes are exported as API dependencies so apps using the toolkit inherit
  responsive layout tooling by default.
- Use `rememberWindowWidthSizeClass()` from `core.ui.window` to query the current width class in
  composables and toggle between phone and tablet experiences without relying on orientation
  heuristics.
- For implementation guidance (imports, usage patterns, and breakpoints), see
  `docs/apptoolkit/compose/adaptive-window-size-classes.md`.

## Feature packages

Each feature lives in its own package with actions, events, repositories, UI state and composables:

- **Help** – FAQ style support screen with contact options.
- **Permissions** – flows for requesting and explaining app permissions.
- **Support** – donation screen backed by Google Play Billing.
- **Ads Settings** – configuration for displaying ads and consent forms.
- **Consent** – UMP-backed consent orchestration that emits loading/success/error flows.
- **Startup** – initial screens and utilities to run on app launch.
- **Diagnostics** – usage and diagnostics consent management.
- **Advanced Settings** – cache controls and developer options.
- **General Settings** – core application preferences.
- **Onboarding** – animated onboarding pages and theme selection.
- **Display & Theme** – dialogs and lists for appearance preferences.
- **About** – app information and links to licenses.
- **Issue Reporter** – collect device info and create GitHub issues.

### Feature inventory quick map

| Feature | Package root | Typical entrypoint |
| --- | --- | --- |
| Main | `app/main` | `MainViewModel`, `AppNavigationHost` |
| Startup | `app/startup` | `StartupActivity`, `StartupScreen`, `StartupViewModel` |
| Onboarding | `app/onboarding` | `OnboardingScreen`, `OnboardingViewModel` |
| Help | `app/help` | `HelpActivity`, `HelpScreen`, `HelpViewModel` |
| Support | `app/support` | `SupportScreen`, `SupportViewModel` |
| Settings | `app/settings` | `SettingsActivity`, `SettingsScreen`, `SettingsViewModel` |
| About | `app/about` | `AboutScreen`, `AboutViewModel` |

## Data layer

Common networking is handled by a shared `KtorClient`, while persistent preferences rely on a reusable `CommonDataStore`. Specialized repositories such as `FaqRepositoryImpl` or `OnboardingRepositoryImpl` extend this infrastructure.

## Extensibility

Apps can depend on the library module and selectively enable features. Each screen exposes an accompanying Activity for easy integration or the underlying composable functions for custom navigation setups.

## Host-driven Koin integration

Toolkit modules are intentionally loaded by the **host application**. Use:

- `AppToolkitHostBuildConfig` for host-specific build/runtime values.
- `appToolkitFoundationModules(...)` for platform/core wiring (dispatchers, datastore, ktor, consent, GMS host factory).
- `appToolkitSettingsModules()` for reusable settings-focused bindings.
- `appToolkitFeatureModules(...)` to obtain toolkit Koin modules and add them to the host `startKoin {}` call.
- `AppToolkitDiConstants` for named qualifiers (for example `GITHUB_CHANGELOG`, `DEVELOPER_APPS_API_URL`, and `DEFAULT_THEME_PALETTE`).

This keeps `BuildConfig` ownership in the host app while moving reusable toolkit bindings into the library itself.

## Testing

The project includes unit tests for view models, repositories and utility classes. Run `./gradlew test` to execute the full test suite.

## Next steps

See the README for installation instructions and explore the source code for implementation details. Contributions and improvements to this documentation are welcome.
