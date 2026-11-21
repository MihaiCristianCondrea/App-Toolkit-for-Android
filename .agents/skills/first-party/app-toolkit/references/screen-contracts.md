# Screen contracts

A stateful `XScreen` (or sheet/dialog entry point) resolves the ViewModel at the destination,
collects its state with lifecycle awareness, and passes current values and event callbacks to a
stateless, previewable `XScreenContent` in the same file. Keep Koin lookup, navigation, tracking
and side effects at the screen boundary. Content takes the data it displays and callbacks for
what the person can do.

In the Toolkit screen setup, `ScreenViewModel` owns observable state, typed events and queued
`UiMessage` values. `LoggedScreenViewModel` adds operation reporting. `ScreenHost` connects
screen visits, optional status and messages to the Toolkit shell. Use the existing message host
when the surface owns its own snackbar. Never attach two hosts to the same queue. The host consumes
messages and reports action presses back to the screen.

Use a feature-owned immutable state data class. Give each independently loading section a
`Loadable` field and use `ScreenStateHandler` for its loading, empty, failed and ready cases. A failed first load with no usable content belongs in the
content area. A failed mutation or a failed refresh with still usable content can use a snackbar.
Map errors through the shared helpers and use localized feature text for a specific fallback.
Reuse shared strings for actions such as Try again. Wire retry to the corresponding event.

Changelog shows initial loading, empty and retryable failure in the sheet. Theme waits for the
stored selection before drawing the preference rows. Developer Options starts with the last known
settings or host defaults and uses a snackbar action to retry a failed read. These are choices
based on what each surface can still render.
