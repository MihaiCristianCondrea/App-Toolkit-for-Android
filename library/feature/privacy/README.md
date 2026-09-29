# `:library:feature:privacy` Logic Graph

## Purpose

Owns the privacy and legal preference surface: policy links, the host destinations reached from it,
and the strings that name them.

## Owns

- `PrivacyScreen`, the data-driven privacy and legal preference list.
- `PrivacyViewModel`, `PrivacyUiState`, `PrivacyItem`, and the click routing between them.
- `PrivacySettingsProvider`, the host contract supplying the legal URLs.
- `privacySettingsPage()`, the registration of `PrivacySettingsRoute` as a detail of the settings
  list.
- Being the page Android's permission manager and privacy dashboard open from the information icon
  beside the app. `PermissionUsageActivity` in [`:library:apptoolkit`](../../apptoolkit/README.md)
  shows it on its own, with the pages it links to, over the system's settings.
- Privacy, legal, ads, permissions, and usage-and-diagnostics strings.

## Does not own

- The ads, permissions, and usage-and-diagnostics pages, owned by `:library:integration:ads`,
  [`:library:feature:permissions`](../permissions/README.md) and
  [`:library:feature:diagnostics`](../diagnostics/README.md). This list opens them by key.
- The settings list whose row opens this page, owned by
  [`:library:feature:settings`](../settings/README.md).
- Consent collection, owned by `:library:integration:consent`.

## Depends on

- `:library:core:common` and `:library:core:ui` for shared state, platform helpers, Compose and,
  through `:library:core:ui`, the keys and graph builder of `:library:navigation`.
- No other feature or integration module.

## Used by

- [`:library:apptoolkit`](../../apptoolkit/README.md), which calls `privacySettingsPage()` from
  `toolkitPages()`, and `:sample:feature:settings`, which binds a default provider.

## Flow chart

```mermaid
flowchart TD
    Screen[PrivacyScreen] --> VM[PrivacyViewModel]
    VM --> Provider[PrivacySettingsProvider]
    VM --> Mapper[PrivacyMappers to PrivacyItem list]
    List --> Click[PrivacyEvent.ItemClicked]
    Click --> VM
    VM --> Navigate[PrivacyAction.Navigate: key]
    Navigate --> Pages[Permissions / ads / diagnostics pages]
    VM --> OpenUrl[PrivacyAction.OpenUrl]
    OpenUrl --> Browser[Device browser]
```

## Architectural decisions

- The list is data-driven: `PrivacyViewModel` maps the host-supplied `PrivacySettingsProvider` into
  an ordered list of `PrivacyItem` models with pre-computed card positions, so the composable stays
  declarative and the entries are testable without Compose.
- Opening a URL needs a `Context`, and opening a page needs the shell's navigator, so both leave
  the ViewModel as a `PrivacyAction` the screen performs: `OpenUrl` or `Navigate(key)`. The
  permissions, ads and diagnostics rows carry their Toolkit keys, so the provider no longer supplies
  a callback for each, and an app replaces one of those pages by registering its key.
- Provider URLs keep interface defaults from `AppLinks`, so a host overrides only the links it
  actually changes.

## Public contracts

- `PrivacySettingsProvider`, `PrivacyScreen`, `PrivacyViewModel`, `PrivacyUiState`,
  `PrivacyItem`, `PrivacyItemAction`, `PrivacyEvent`, `PrivacyAction`, `privacySettingsPage()`
  and `privacyModule`.

## Internal implementations

- Privacy item assembly with grouped card position calculation, and preference tap analytics.

## Current risks

The permissions, ads and diagnostics rows are always listed. An app without one of those pages has
to register its own page for the key, or the row opens nothing.
