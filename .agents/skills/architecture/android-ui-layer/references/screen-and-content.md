# Screen and ScreenContent

The two composables every screen has. Templates: `templates/XScreen.kt.txt` and
`templates/XScreenContent.kt.txt`. Reference: `AboutScreen.kt` and `AboutScreenContent.kt` in
`:library:feature:about`.

## Who owns what

| Concern                                          | `XScreen` | `XScreenContent` |
|--------------------------------------------------|:---------:|:----------------:|
| `koinViewModel`, `koinInject`                    | yes       | no               |
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
    val firebaseController: FirebaseController = koinInject()
    val navigator = LocalShellNavigator.current

    TrackScreenView(firebaseController = firebaseController, screenName = X_SCREEN_NAME, screenClass = X_SCREEN_CLASS)
    TrackScreenState(firebaseController = firebaseController, screenName = X_SCREEN_NAME, state = state.items)

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
- A component that logs its own GA4 tap events, such as `SettingsPreferenceItem`, may take a
  nullable `FirebaseController` parameter. The screen passes the injected one; previews pass
  nothing.
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
