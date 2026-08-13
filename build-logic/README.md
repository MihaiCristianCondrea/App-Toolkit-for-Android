# `build-logic` Logic Graph

## Purpose

Provides the included Gradle build that hosts repository-local convention plugins without adding build logic to runtime modules.

## Owns

- Included-build repository and version-catalog wiring.
- The [`build-logic:convention`](convention/README.md) child project.

## Does not own

- Android runtime code or dependencies.
- Individual convention-plugin behavior, owned by the child project.

## Depends on

No application Gradle modules. Its settings import the root version catalog for plugin compilation.

## Used by

The root build includes `build-logic` through `pluginManagement`, allowing active Android modules to apply its convention plugin.

## Flow chart

```mermaid
flowchart LR
    Root[Root pluginManagement] --> Included[build-logic]
    Catalog[Root version catalog] --> Included
    Included --> Convention[build-logic:convention]
    Convention --> Modules[Android modules]
```

## Public contracts

- The included-build name and plugins published by its child projects.

## Internal implementations

- Repository setup and version-catalog import.

## Current risks

The included build imports the root catalog by relative path, so relocating it requires updating that path and root plugin management together.
