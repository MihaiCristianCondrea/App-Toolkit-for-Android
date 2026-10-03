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
| `TrackScreenView`, `TrackScreenState`            | yes       | no               |
| `MessageHost`                                    | yes       | no               |
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

    TrackScreenView(screenName = X_SCREEN_NAME, screenClass = X_SCREEN_CLASS)
    TrackScreenState(screenName = X_SCREEN_NAME, state = state.items)

    XScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onOpenDetails = { id -> navigator.navigate(XDetailsRoute(id)) },
        contentPadding = contentPadding(),
    )

    MessageHost(viewModel = viewModel)
}
```

- Its parameters are what the host passes in, such as `onVersionTap` on `AboutScreen`. It takes no
  state.
- `TrackScreenState` gets the field that decides whether the screen worked; see `state.md`.
- Telemetry comes from `LocalTelemetry`, which `ShellHost` provides. `TrackScreenView`,
  `TrackScreenState` and the design-system components read it themselves. A screen that logs an
  event of its own reads it once, `val telemetryRepository = LocalTelemetry.current`, and calls it in its
  callbacks; it never injects `TelemetryRepository` with `koinInject`.
- `TelemetryRepository` is the only repository composables use directly. A screen view or a tap is
  a UI event, so routing it through the ViewModel would add an event per tap and change nothing
  reported. Every other repository is reached through the ViewModel.
- Keep it this short. Logic that grows here belongs in the ViewModel, and layout in the content.

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
    onEmpty = { empty -> XEmptyState(message = empty.message) },
) { ready ->
    XList(items = ready.value, refreshing = ready.refreshing)
}
```

- Every case except `Ready` has a design-system default, so pass only what differs: `onLoading`,
  `onEmpty`, `onError`.
- The default failure screen shows Retry when `onRetry` is given and the failure is `retryable`.
- It fades between cases, not between two `Ready` values, so updating the content does not flash.
- A screen with several `Loadable` fields draws one handler per field.

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
- Preview `Ready` and `Loading`. The default `Empty` and `Failed` screens are `NoDataScreen`, which
  calls `koinInject` for its ad unit even when no ad shows, so they fail in a preview. Cover them
  in a Compose test with Koin started (`testing.md`), or preview a screen-specific `onEmpty` or
  `onError` slot that does not use `NoDataScreen`.
- Add one preview per meaningful layout of the ready content: an empty search, a long list, a
  selected row.
