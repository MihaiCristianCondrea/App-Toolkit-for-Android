# `:library:feature:developer` Logic Graph

## Purpose

The developer options page: the shell's testing switches, applied while the app runs, and a live
readout of the state the shell is in. Changes apply at once and persist, so the app can be
restarted into a variation. The shell's layout choices a person would make (bar styles, colours,
transitions) are display settings instead, in
[`:library:feature:display`](../display/README.md).

## Owns

- `DeveloperOptionsScreen`: where the next launch starts (when the app has more than one start
  option), a forced navigation layout, the bottom accessories (when the app has a banner or a
  player, offering only the ones it has), the animation speed, a reset of those, and the window size and every back stack as they
  change.
- `developerOptionsPage()`, the registration of `DeveloperOptionsRoute`.
- Its strings, in every supported locale, including the page title `shell_developer_options`.

## Does not own

- The settings it edits, owned by [`:library:shell`](../../shell/README.md) (`ShellSettings`,
  `ShellPreferences`).
- Adding the page to a graph, done by `toolkitPages()` in
  [`:library:apptoolkit`](../../apptoolkit/README.md). An app replaces the page by registering
  `DeveloperOptionsRoute` itself.
- Where the page is offered. The advanced settings of
  [`:library:feature:advanced`](../advanced/README.md) list it once the About screen's version
  easter egg is found; an app may link to it elsewhere too.

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
- It uses the Toolkit's grouped preference rows and `ChoicePreferenceItem`, so it looks like the
  Toolkit's other settings pages. NavTest's own preference rows were not ported.
- Reset puts back only these options (`ShellPreferences.resetDeveloperOptions()`); the display
  settings are the person's and stay.

## Public contracts

- `DeveloperOptionsScreen()` and `developerOptionsPage()`.
