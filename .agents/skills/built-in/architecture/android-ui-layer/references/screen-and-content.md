# Screen and ScreenContent

The two composables every screen has, declared together in `XScreen.kt`: `XScreen` first, then
`XScreenContent`, its private helpers and its previews. Template: `templates/XScreen.kt.txt`.
Reference: `AboutScreen.kt` in `:library:feature:about`, and `FaqScreen.kt` in
`:library:feature:faq` for a screen with analytics on every tap, an ad slot and a bottom sheet.

## Who owns what

| Concern                                          | `XScreen` | `XScreenContent` |
|--------------------------------------------------|:---------:|:----------------:|
| `koinViewModel`, `koinInject`                    | yes       | no               |
| `LocalTelemetry.current` for its own events      | yes       | no               |
| collecting flows                                 | yes       | no               |
| `ScreenHost` (visits, status, messages)          | yes       | no               |
| navigator calls                                  | yes       | no, a callback   |
| `contentPadding()` from the shell                | yes       | no, a parameter  |
| `ScreenStateHandler` and the layout              | no        | yes              |
| visual element state (`remember`, animations)    | no        | yes              |
| resolving `UiTextHelper` with `LocalContext`     | no        | yes              |
| `@Preview`                                       | no        | yes              |

## XScreen

```kotlin
@Composable
fun XScreen() {
    val viewModel: XViewModel = koinViewModel()
    val state: XUiState by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalShellNavigator.current

    ScreenHost(screen = X_SCREEN, status = state.items, viewModel = viewModel) {
        XScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            onOpenDetails = { id -> navigator.navigate(XDetailsRoute(id)) },
            contentPadding = contentPadding(),
        )
    }
}
```

- Its parameters are what the host passes in, such as `onVersionTap` on `AboutScreen`. It takes no
  state.
- `ScreenHost` is the screen's boundary and its only tracking call. It reports a visit when the
  screen becomes visible and active, reports `status` at the start of each visit and when its label
  changes, and shows `viewModel`'s messages. It adds no layout, renders no `Loadable` and owns no
  navigation. See "ScreenHost" below.
- `status` gets the field that decides whether the screen worked; see `state.md`.
- Telemetry comes from `LocalTelemetry`, which `ShellHost` provides. `ScreenHost` and the
  design-system components read it themselves. A screen that logs an interaction of its own reads
  it once, `val telemetry = LocalTelemetry.current`, and calls it in its callbacks; it never
  injects `TelemetryRepository` with `koinInject`.
- `TelemetryRepository` is the only repository composables use directly. A screen view or a tap is
  a UI event, so routing it through the ViewModel would add an event per tap and change nothing
  reported. Every other repository is reached through the ViewModel.
- Keep it this short. Logic that grows here belongs in the ViewModel, and layout in the content.

## ScreenHost

One recipe for every kind of screen. `screen` is a `TrackedScreen`, the stable name and class from
`core.common.telemetry.models`, declared once per screen.

| Screen                                  | Call                                                                      |
|-----------------------------------------|---------------------------------------------------------------------------|
| static, nothing loads, no ViewModel     | `ScreenHost(screen = X_SCREEN) { XContent() }`                            |
| one loading section                     | `ScreenHost(screen = X_SCREEN, status = state.items, viewModel = viewModel) { ... }` |
| several loading sections                | `status` is the section that says how the screen is doing, or a status of its own |
| a custom workflow                       | `status` is the feature's sealed type implementing `TrackedStatus`        |
| a sheet or dialog with a name of its own | `ScreenHost` around the sheet, inside the screen that opens it           |
| a reusable piece inside a screen        | no `ScreenHost`: it is part of its screen's visit                         |

- Each `Loadable` section renders through its own `ScreenStateHandler` inside the content;
  `ScreenHost` never renders one.
- `onMessageAction` is called when a message's action is pressed, such as Retry.
- A screen whose messages show in a snackbar host of its own, or as toasts, passes no `viewModel`
  and shows them itself.
- A visit is not a composition. A screen that only animates in or out, is kept for a back
  gesture, or sits under an overlay is not reported; rotating keeps the visit; coming back from
  another tab, a page or the background is a new one; both panes of a list-detail layout report
  their own. Do not add a `LaunchedEffect` that logs `logScreenView`.
- Never manufacture a status, such as `Loadable.Ready(config)` for a fixed list, only to report one.

## XScreenContent

```kotlin
@Composable
internal fun XScreenContent(
    state: XUiState,
    onEvent: (XEvent) -> Unit,
    onOpenDetails: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    ScreenStateHandler(
        state = state.items,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(XEvent.Load) },
    ) { ready ->
        LazyColumn(contentPadding = contentPadding) {
            items(items = ready.value, key = { it.key }) { item ->
                XRow(item = item, onClick = { onOpenDetails(item.key) })
            }
        }
    }
}
```

- Parameter order: the state and required callbacks, then `modifier`, then optional parameters.
- `onEvent` carries everything the ViewModel handles; navigation targets get their own callbacks,
  since the ViewModel does not navigate.
- Give list items a stable `key`, so rows keep their state when the list changes.
- A tap that also has to be logged, or needs a `Context` (open a link, send mail), is a named
  callback the screen implements: `onContactUs`, not `onEvent`. The content stays free of
  telemetry and platform calls, and the screen logs exactly what it logged before.
  `FaqScreenContent` takes seven such callbacks.
- A design-system component that logs its own GA4 tap events, such as `SettingsPreferenceItem`,
  takes only the `ga4Event` and logs it through `LocalTelemetry`. In a preview `LocalTelemetry` is
  a no-op, so nothing has to be passed.
- Resolve Koin values the content needs, such as an ad unit, in the screen and pass the plain value
  (`adUnitId: String?`), so the content renders without Koin.
- Visual state that belongs to one interaction, such as About's version-tap counter and konfetti,
  stays here with `rememberSaveable`. It moves to the ViewModel once it has to survive the screen
  or be reported.

## ScreenStateHandler

```kotlin
ScreenStateHandler(
    state = state.items,
    contentPadding = contentPadding,
    onRetry = { onEvent(XEvent.Load) },
    icon = Icons.Outlined.Folder,
) { ready ->
    XList(items = ready.value, refreshing = ready.refreshing)
}
```

- Every case except `Ready` is drawn by the app's `ScreenStates`, so pass only what differs, as
  parameters rather than a copy of `NoDataScreen`:

  | Need | Pass |
  |---|---|
  | The screen's icon on the empty and failure content | `icon = Icons.Outlined.X`, or any `ToolkitIcon` |
  | Retry on an empty result too (a catalog that may update) | `retryWhenEmpty = true` |
  | No native ad on the empty and failure content | `showAd = false` |
  | A layout of its own for this section, such as a skeleton | `onLoading`, `onEmpty`, `onError` |

- The default failure content shows Retry when `onRetry` is given and the failure is `retryable`.
- It animates between cases, not between two `Ready` values, so updating the content does not flash.
- A screen with several `Loadable` fields draws one handler per field.
- An app draws its own loading, empty and failed content, or its own transition, once:
  `CompositionLocalProvider(LocalScreenStates provides ScreenStates(...))` around its content.
  Every handler follows it; a section's own `onLoading`, `onEmpty` or `onError` still wins.
- A state that is not a `Loadable`, such as the phases of a flow (idle, cleaning, done), switches
  through `StateContent(state) { when (it) { ... } }`, which animates the same way. Keep the phase a
  sealed type or enum of the feature's; derive its `TrackedStatus` for `ScreenHost` from it.
- The screen's floating action buttons for its current state go on `ScreenHost(fabs = ...)`, once
  for the screen, not inside the cases: two cases that both declare buttons while one fades into
  the other fight over them. A button that opens something the content draws, such as a sheet,
  keeps that flag in `XScreen` with `rememberSaveable` and passes it to the content with its
  dismiss callback (`showFeedbackSheet`, `onFeedbackSheetDismissed`), so the content stays
  stateless and can be previewed open.

## Entrance animation

```kotlin
LazyColumn {
    itemsIndexed(rows, key = { _, row -> row.id }) { index, row ->
        RowCard(row, modifier = Modifier.animateItem().animateVisibility(index = index))
    }
}
```

- `Modifier.animateVisibility(index)` brings an element in the first time it is shown. Give list
  items their `index`; elements without one cascade in the order they appear.
- The cascade counts from what is shown, not from the top of the list: a screen's first items
  arrive one after another, and an item scrolled in later starts at once. Do not offset `index` to
  fake this, and do not add delays of your own.
- The entrance starts when the element is first placed, so items a lazy list prefetches still
  enter on screen. Once started it stays revealed, saved with the item.
- The style is the app's, from `LocalEntranceStyle` (`Slide`, `Scale`, `Fade`, `None`). Set it once
  with `appToolkitDefaults { shell { entranceStyle = ... } }`; pass `style` only for one element
  that must differ. The motion follows `MaterialTheme.motionScheme`, so never pass durations or
  offsets.
- An element that comes and goes, such as a hint or a filter's result, uses
  `Modifier.animateVisibility(visible)`. It keeps its place in the layout while hidden; to take it
  out of the layout use `AnimatedVisibility` instead.
- Put `animateItem()` before `animateVisibility` on the item's root, never inside an
  `AnimatedVisibility` or another wrapper of the item.

## Previews

```kotlin
@Preview(showBackground = true)
@Composable
private fun XScreenContentPreview() {
    MaterialTheme {
        XScreenContent(
            state = XUiState(items = Loadable.Ready(persistentListOf(/* sample items */))),
            onEvent = {},
            onOpenDetails = {},
        )
    }
}
```

- Wrap previews in `MaterialTheme`. The Toolkit's `AppTheme` reads the theme settings from Koin,
  which a preview does not have.
- Use `UiTextHelper.DynamicString` for sample text, so the preview needs no resources.
- Preview `Ready` and `Loading`. The default `Empty` and `Failed` content resolves its ad unit
  through Koin while its ad shows, so preview those with `showAd = false`, or cover them in a
  Compose test with Koin started (`testing.md`).
- Add one preview per meaningful layout of the ready content: an empty search, a long list, a
  selected row.
