# `:sample:feature:settings` Logic Graph

## Purpose

The settings this app adds on top of the toolkit: its About content, and the hidden version-tap
gesture that content hosts.

## Owns

- `AboutSettingsContent`, the sample's About page content, which `:sample:app` registers with
  `aboutPages { AboutSettingsContent() }`.
- `ShowcaseUnlockRepository`: the version-tap threshold and the write that persists the unlock,
  bound by `settingsModule`.

## Does not own

- The settings pages themselves, owned by
  [`:library:feature:settings`](../../../library/feature/settings/README.md) and the feature modules
  that register the keys the rows open.
- The rows of the settings list and every other answer to the toolkit's extension points, owned
  with the rest of the App Toolkit setup by
  [`:sample:core:apptoolkit`](../../core/apptoolkit/README.md).
- The showcase the gesture reveals, owned by
  [`:sample:feature:components`](../components/README.md), which only reads the same flag.
- The startup-screen choices, composed by `:sample:app`, which is the only module that may name
  every top-level destination.

## Depends on

- [`:library:apptoolkit`](../../../library/apptoolkit/README.md) for the About page and its
  extension.
- [`:sample:core:datastore`](../../core/datastore/README.md) for the persisted unlock flag.

## Used by

- `:sample:app`, which loads `settingsModule` and registers `AboutSettingsContent` as the About page.

## Flow chart

```mermaid
flowchart TD
    App[":sample:app"] --> Module[settingsModule]
    Module --> Unlock
    App -->|aboutPages| About[AboutSettingsContent]
    About -->|version taps| Unlock[ShowcaseUnlockRepository]
    Unlock --> Store[":sample:core:datastore" unlock flag]
```

## Architectural decisions

- This sample adapter intentionally has no `domain` package. Its only app-owned operation is a
  single repository mutation, so adding a forwarding use case would not isolate reusable logic.
- The About gesture lives with the settings that host it. It reaches the Components showcase
  through the shared flag in `:sample:core:datastore` rather than through the feature, so neither
  feature depends on its sibling and no registration contract is needed.
- The About content reaches the graph as an argument of `aboutPages`, not through a Koin binding,
  so there is no single binding a second module could silently override.

## Public contracts

- `settingsModule`, `AboutSettingsContent`, `ShowcaseUnlockRepository`.

## Internal implementations

- The tap-threshold rule.

## Current risks

The unlock threshold and the reader of the flag sit in different modules. Changing the flag's
meaning here without checking `:sample:feature:components` will silently change what that feature
shows.
