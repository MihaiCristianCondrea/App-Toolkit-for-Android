# `:library:feature:developer` Logic Graph

## Purpose

The developer options page, the shell's laboratory: every override that forces, compares or
exercises the shell's layout, bars, navigation, motion and accessories, applied while the app runs,
and a live readout of the state the shell is in. Changes apply at once and persist, so the app can
be restarted into a variation. The person's own preferences are display settings instead, in
[`:library:feature:display`](../display/README.md).

## Owns

- `DeveloperOptionsScreen`, with its headings and rows:

  | Heading | Row | Shown when |
  |---|---|---|
  | Live state | Window and layout, page stack | always |
  | | One stack per tab | per tab |
  | Layout and bars | Forced layout | the app has tabs |
  | | App bar style, hide the app bar on scroll | the shell draws an app bar (`hasShellTopBars`) |
  | | Navigation tint | the app has tabs |
  | | Navigation bar style, hide the bottom bar on scroll | the app has bottom navigation tabs (`hasBottomNavigationTabs`) |
  | | Limit content width | the layout policy sets a maximum width |
  | Navigation | Where the app starts | more than one start option |
  | | Tab transition | more than one tab |
  | | Back swipe from the right | more than one destination |
  | Motion | Animation speed | always |
  | Accessories | Bottom accessory | a banner or a player the tabs can show; all four choices with both, otherwise shown or hidden |

  A heading without a row is left out, and Reset is always last.
- `DeveloperOption`, `DeveloperCategory`, `developerOptions(capabilities)` and
  `accessoryModes(capabilities)`: which overrides act on something in this app.
- `DeveloperOptionItem`, the row of each override, and its option labels.
- `developerOptionsPage()`, the registration of `DeveloperOptionsRoute`.
- Its strings, in every supported locale, including the page title `shell_developer_options` and
  the override rows' titles and options.

## Does not own

- The settings it edits, owned by [`:library:shell`](../../shell/README.md) (`ShellSettings`,
  `ShellPreferences`).
- What the app's shell can show, `ShellCapabilities`, owned by
  [`:library:navigation`](../../navigation/README.md) and provided by `ShellHost`.
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

- **Every shell override lives here.** Each `ShellSettings` value is a developer option: it forces
  a variation over what the app declares. None is a display setting, so a person never meets the
  shell's implementation choices.
- **Overrides count what a forced layout reaches.** Display rows follow the app's declared layout
  policy; these follow what the page itself can force. An app with tabs gets the bottom bar's and
  the rail's overrides whatever its width thresholds, since the forced layout row can show either.
  A host that enables `hideSingleTabBottomBar` keeps its one-tab bottom bar hidden even in a forced
  phone layout, so its bottom bar overrides are left out. Rows that
  have nothing to act on (a tab transition with one tab, a width limit the app never sets,
  accessories it does not declare) are left out.
- **Not searchable.** The settings search does not list these overrides: the page is reached once
  unlocked, and a result would go around the unlock.
- **Reset is complete.** `ShellPreferences.resetDeveloperOptions()` puts every `ShellSettings`
  value back to its default, so the shell is again exactly as the app declares it. The person's
  preferences live in the common store and are not touched.
- The page reads the shell's composition locals (`LocalShellSettings`, `LocalShellPreferences`,
  `LocalShellLayout`, `LocalShellCapabilities`, `LocalShellNavigator`, `LocalShellGraph`), so it
  only works as a page of a `ShellHost`. It keeps no view model: the settings store is its state.
- It uses the Toolkit's grouped preference rows and `ChoicePreferenceItem`, so it looks like the
  Toolkit's other settings pages.

## Public contracts

- `DeveloperOptionsScreen()` and `developerOptionsPage()`. The option model and rows are internal.
