# `:library:feature:developer` Logic Graph

## Purpose

The developer options page: every variation of the shell, switchable while the app runs, and a
live readout of the state the shell is in. Changes apply at once and persist, so the app can be
restarted into a variation.

## Owns

- `DeveloperOptionsScreen`: where the next launch starts, the navigation layout, the app bar style,
  the bottom bar style, the rail and app bar colour, hide-on-scroll, the content width limit, the
  bottom accessories and banner style, the tab transition, the right-edge back swipe, the
  animation speed, a reset, and the window size and every back stack as they change.
- `developerOptionsPage()`, the registration of `DeveloperOptionsRoute`.
- Its strings, in every supported locale, including the page title `shell_developer_options`.

## Does not own

- The settings it edits, owned by [`:library:shell`](../../shell/README.md) (`ShellSettings`,
  `ShellPreferences`).
- Adding the page to a graph, done by `toolkitPages()` in
  [`:library:apptoolkit`](../../apptoolkit/README.md). An app replaces the page by registering
  `DeveloperOptionsRoute` itself.
- Where the page is offered. The app links to it, typically from the drawer in debug builds only,
  as `:sample:app` does.

## Depends on

- [`:library:shell`](../../shell/README.md), as `api`, and through it
  [`:library:navigation`](../../navigation/README.md) and [`:library:core:ui`](../../core/ui/README.md),
  whose preference rows and dialog it draws with.

## Used by

- [`:library:apptoolkit`](../../apptoolkit/README.md).

## Architectural decisions

- The page reads the shell's composition locals (`LocalShellSettings`, `LocalShellPreferences`,
  `LocalShellLayout`, `LocalShellNavigator`, `LocalShellGraph`), so it only works as a page of a
  `ShellHost`. It keeps no view model: the settings store is its state.
- It uses the Toolkit's grouped preference rows and `BasicAlertDialog`, so it looks like the
  Toolkit's other settings pages. NavTest's own preference rows were not ported.

## Public contracts

- `DeveloperOptionsScreen()` and `developerOptionsPage()`.
