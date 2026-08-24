# `:library:feature` Logic Graph

## Purpose

Acts as the implicit Gradle parent for user-facing AppToolkit feature modules. It is an
organizational container, not a runtime artifact.

## Owns

- Grouping for about, help, issue reporter, onboarding, permissions, settings, and support modules.

## Does not own

- Feature contracts or implementations; each child feature owns its complete current UI/domain/data
  slice.
- Cross-feature composition, owned by `:library:apptoolkit` and the host application.

## Depends on

No internal Gradle modules.

## Used by

No internal module declares a dependency on `:library:feature`; consumers select child feature
modules.

## Flow chart

```mermaid
flowchart TD
    Parent[":library:feature implicit parent"] --> About["about: app information and changelog"]
    Parent --> Help["help: FAQ and review action"]
    Parent --> Reporter["issuereporter: device capture and GitHub submission"]
    Parent --> Onboarding["onboarding: startup and first-run flow"]
    Parent --> Permissions["permissions: inspection and settings actions"]
    Parent --> Settings["settings: toolkit settings surfaces"]
    Parent --> Support["support: donation UI"]
    Settings --> About
    Settings --> Help
    Settings --> Reporter
    Onboarding --> Settings
    Permissions --> Settings
    About --> Support
```

## Architectural decisions

- Each child owns its current UI/data/domain slice; the parent contributes no runtime code.
- Cross-feature dependencies shown above are existing composition edges, not a rule that features
  may depend freely on siblings. New shared contracts should stay with their true owner.
- Destination aggregation remains in `:library:apptoolkit` so this parent never becomes a service
  locator or umbrella dependency.

## Public contracts

No runtime contracts are exposed.

## Internal implementations

There is no implementation; this is an implicit Gradle hierarchy node.

## Current risks

Several feature-to-feature edges exist for settings composition. Changes to a shared route or
provider can therefore ripple beyond the module that renders the screen.
