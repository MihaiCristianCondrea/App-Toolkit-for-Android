# State

How to shape `XUiState` and choose its statuses. Template: `templates/XUiState.kt.txt`.

## The state is the feature's own type

```kotlin
@Immutable
data class XUiState(
    val items: Loadable<ImmutableList<XItem>> = Loadable.Loading,
    val query: String = "",
    val selectedId: String? = null,
)
```

Only a part that loads gets a `Loadable`. `query` and `selectedId` exist from the first frame, so
they are plain fields. A feature never wraps the whole state in a status.

## `Loadable`

| Case                              | `screen_state` label | Meaning                                       |
|-----------------------------------|----------------------|-----------------------------------------------|
| `Loading`                         | `loading`            | nothing to show yet                           |
| `Ready(value, refreshing, stale)` | `success`            | content, possibly refreshing or a saved copy  |
| `Empty(message)`                  | `no_data`            | loaded, and there is nothing                  |
| `Failed(message, retryable)`      | `error`              | the load failed; Retry when `retryable`       |

Choose cases by what the user should see:

- **No spinner.** Start at `Loadable.Empty()` and go straight to `Ready` when the data arrives.
  Nothing makes a field start at `Loading`.
- **Refresh behind shown content.** Keep `Ready` and set `refreshing = true`; the content draws its
  own indicator. Going back to `Loading` would blank the screen.
- **Offline copy.** `Ready(value, stale = true)`, so the content can say the data may be old.
- **A failure.** Build it from the thrown failure with `error.toFailed(fallback)`, which picks the
  text and whether a retry can help (`errors.md`). `ScreenStateHandler` shows no Retry button
  when `retryable` is false.
- **An empty result.** `Empty(message)` with a message that says what is empty, or `Empty()` for the
  generic one.

## Several sections

One `Loadable` per section, so one failing does not blank the rest:

```kotlin
@Immutable
data class DashboardUiState(
    val summary: Loadable<Summary> = Loadable.Loading,
    val activity: Loadable<ImmutableList<ActivityItem>> = Loadable.Loading,
)
```

The content draws one `ScreenStateHandler` per section. `TrackScreenState` takes the field that
decides whether the screen worked, usually the main one.

## Custom statuses

When the screen's phases are not loading phases, the feature declares its own sealed type and
implements `TrackedStatus`, so `TrackScreenState` still reports it:

```kotlin
@Immutable
sealed interface CheckoutStatus : TrackedStatus {
    data object Editing : CheckoutStatus {
        override val trackingLabel: String get() = "editing"
    }

    data object Submitting : CheckoutStatus {
        override val trackingLabel: String get() = "submitting"
    }

    data class Done(val orderId: String) : CheckoutStatus {
        override val trackingLabel: String get() = "done"
    }
}
```

Labels are snake_case literals, like `Loadable`'s. Render a custom status with a `when`;
`ScreenStateHandler` is for `Loadable`.

## Derived values

Compute a value from other fields in a getter on the state, not in a second field the ViewModel
must keep in step:

```kotlin
val canSubmit: Boolean get() = query.isNotBlank() && items is Loadable.Ready
```

A computation that needs data outside the state belongs in the ViewModel or the mapper.

## Text

Text the app writes is a `UiTextHelper` (`StringResource`, `PluralResource` or `DynamicString`), so
it resolves against the current locale and configuration when it is drawn. Text the user typed, or
text that has to stay exactly as shown (the About screen's clipboard text), is a `String`.
