# Module Taxonomy

This document defines the responsibilities and dependency rules for each type of module in the App Toolkit Sample App.

## Taxonomy

| Layer | Path | Purpose |
| :--- | :--- | :--- |
| **App** | `:sample:app` | Composition root, Android packaging, and launcher logic. |
| **Core** | `:sample:core:*` | Shared infrastructure, contracts, and cross-feature capabilities. |
| **Feature** | `:sample:feature:*` | User-facing capabilities or vertical slices of functionality. |
| **Widget** | `:sample:widget` | Home-screen widgets and their specific data providers. |

## Module Types

### App Module (`app`)
The `:sample:app` module is the glue that binds everything together. It handles:
- Application startup and configuration.
- DI composition (aggregating modules from all other modules).
- Android Manifest declarations (Launcher activity, global services).
- Wiring between features (e.g., navigation callbacks).

### Core Modules (`core:*`)
Core modules provide infrastructure that is reused across multiple features. Examples:
- `:sample:core:shell`: The application scaffold, drawer, and global navigation UI.
- `:sample:core:analytics`: Shared tracking infrastructure and stable screen vocabulary.
- `:sample:core:navigation`: Shared navigation keys and managers.
- `:sample:core:datastore`: Shared persistence contracts.

### Feature Modules (`feature:*`)
Feature modules represent distinct user-facing parts of the app. They should be independent of each other.
- `:sample:feature:apps`: The installed apps list and management.
- `:sample:feature:tiles`: The interactive toolkit tiles.
- `:sample:feature:settings`: App-specific settings.

## Dependency Rules

To maintain a healthy architecture, we enforce the following rules via the `ModuleBoundariesPlugin`:

1.  **Core cannot depend on Feature**: Infrastructure should not know about high-level features.
2.  **Feature cannot depend on Sibling Feature**: Features should be decoupled. If they need to share code, move it to a `core` module.
3.  **Core/Feature cannot depend on App**: The app module is the composition root and should be at the top of the dependency graph.
4.  **Module-Owned DI**: Every module must define its own DI (Koin) module to ensure it can be tested and composed independently.
