# Events and messages

What goes into the ViewModel and what comes back out. Template: `templates/XEvent.kt.txt`.

## Events

```kotlin
sealed interface XEvent {
    /** Loads the entries, on start and on retry. */
    data object Load : XEvent

    data class CopyToClipboard(val label: String, val text: String) : XEvent

    data class QueryChanged(val query: String) : XEvent
}
```

- Name an event after what the user did or wants, not after the ViewModel's reaction:
  `QueryChanged`, not `UpdateQueryState`.
- Use `data object` and `data class`. The `vm_event` breadcrumb reads the source name from
  `toString()` and keeps only the part before `(`, so a field value, which may be text the user
  typed, never reaches a report.
- One `Load` event serves the first load and every retry. The ViewModel sends it to itself from
  `init`.
- Resolve a `UiTextHelper` to a `String` in the content before it goes into an event when the
  ViewModel needs the exact text the user saw, as About does for the clipboard.

## Messages

```kotlin
showMessage(UiMessage(UiTextHelper.StringResource(R.string.x_saved)))
showMessage(error.toErrorMessage(fallback = UiTextHelper.StringResource(R.string.x_save_failed)))
showMessage(
    UiMessage(
        text = UiTextHelper.StringResource(R.string.x_deleted),
        actionLabel = UiTextHelper.StringResource(R.string.x_undo),
    ),
)
```

- Messages are a queue in state. A message raised while the screen is hidden or rotating waits and
  shows when the screen is back, instead of being dropped.
- `MessageHost(viewModel)` shows one message at a time, oldest first, and calls `messageShown(id)`
  when it leaves. A message still on screen when the host leaves composition shows again later.
- Errors and messages with an action stay longer than plain confirmations.
- `MessageHost` draws through the surrounding Toolkit scaffold by default. Pass a
  `snackbarHostState` only when the screen draws its own host.

### Message actions

`MessageHost(viewModel, onAction = { message -> ... })` is called when the action is pressed,
before the message is marked as shown. Turn it into an event for the ViewModel:

```kotlin
MessageHost(
    viewModel = viewModel,
    onAction = { message -> if (message.id == state.undoMessageId) viewModel.onEvent(XEvent.Undo) },
)
```

The ViewModel keeps the id of the message it raised (`UiMessage.id`) in state when more than one
kind of message has an action.

## Navigation and other effects

Navigation the screen can decide on its own is a callback from the content to `XScreen`, which owns
the navigator. Opening the licenses page from a row is one.

When navigation depends on the result of the ViewModel's work, such as leaving the screen after a
save succeeds, first try to express it as state the screen reacts to:

```kotlin
// XUiState
val saved: Boolean = false

// XScreen
LaunchedEffect(state.saved) { if (state.saved) navigator.goBack() }
```

The flag survives rotation, so the screen still leaves when the save finishes while it is being
recreated. A one-off event sent at that moment could be lost.

When the screen stays and the effect can happen again, the screen reports back once it has acted,
and the ViewModel clears the flag. The help page does this for the store listing it opens when
the in-app review cannot show:

```kotlin
// FaqScreen
LaunchedEffect(state.openStoreListing) {
    if (state.openStoreListing) {
        context.openPlayStoreForApp(context.packageName)
        viewModel.onEvent(FaqEvent.StoreListingOpened) // the ViewModel sets the flag back to false
    }
}
```

`core.ui.screen` has no effect stream. If a screen genuinely needs one, add it to `core.ui.screen`
once, for every screen, rather than a per-feature `Channel` or `SharedFlow`.
