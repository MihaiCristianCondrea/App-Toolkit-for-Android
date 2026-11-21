# Screen contracts

A stateful `XScreen` (or sheet/dialog entry point) resolves the ViewModel at the destination,
collects its state with lifecycle awareness, and passes current values and event callbacks to a
stateless, previewable `XScreenContent` in the same file. Keep ViewModel lookup, navigation, tracking
and side effects at the screen boundary. Content takes the data it displays and callbacks for
what the person can do.

In the Toolkit screen setup, `ScreenViewModel` owns observable state, typed events and queued
`UiMessage` values. `LoggedScreenViewModel` adds operation reporting. `ScreenHost` connects
screen visits, optional status and messages to the Toolkit shell. Use the existing message host
when the surface owns its own snackbar. Never attach two hosts to the same queue. The host consumes
messages and reports action presses back to the screen.

Give each independently loading section explicit status in the feature-owned state. In Toolkit
screens use `Loadable` and `ScreenStateHandler` for loading, empty, failed and ready cases.
Standalone screens can render those cases directly from their existing state and use Compose
snackbar APIs for transient feedback. A failed first load belongs in the content area when there
is no usable data. A failed mutation or a failed refresh with still usable content can use a snackbar.
Map errors through the shared helpers and use localized feature text for a specific fallback.
Reuse shared strings for actions such as Try again. Wire retry to the corresponding event.

Changelog shows initial loading, empty and retryable failure in the sheet. Theme waits for the
stored selection before drawing the preference rows. Developer Options starts with the last known
settings or host defaults and uses a snackbar action to retry a failed read. These are choices
based on what each surface can still render.

## Example: add filtering to an existing screen

First identify the current owner of the query and the rows. Toolkit Settings keeps its local
query and index at the screen boundary. For a settings entry, check whether the resolved API
already supports indexing it through host settings or a settings search provider. For a local
filter over loaded rows, keep the query near the rendering owner and derive visible rows from
the existing data. Distinguish no matches from load failure. If the query drives repository
requests, handle it as a typed event and apply latest-query behavior using the UI specialist's
operation guidance. Test clearing the query, no matches and stale results where applicable.
