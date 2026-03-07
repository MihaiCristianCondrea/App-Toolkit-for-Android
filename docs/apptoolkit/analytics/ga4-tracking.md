# GA4 Tracking Guide (Composables, Screens, and ViewModels)

This guide explains how to use AppToolkit’s GA4 tracking APIs so host apps can instrument UI and business flows consistently.

The library gives you three tracking layers:

1. **Composable interaction tracking** with `Ga4EventData` + `logGa4Event(...)`
2. **Screen impression/state tracking** with `TrackScreenView(...)` and `TrackScreenState(...)`
3. **ViewModel lifecycle/operation/error tracking** with `LoggedScreenViewModel`

Use all three together to get complete observability: **what users saw, what they tapped, and what happened in the data flow**.

---

## 1) GA4 primitives provided by the library

### `Ga4EventData`

Path: `apptoolkit/core/ui/model/analytics/Ga4EventData.kt`

`Ga4EventData` is the UI-friendly container for GA4 events:

- `name: String` → event name
- `params: Map<String, AnalyticsValue>` → typed event params

It converts to domain-level `AnalyticsEvent` with `toAnalyticsEvent()`.

### `AnalyticsValue`

Path: `apptoolkit/core/domain/model/analytics/AnalyticsValue.kt`

Supported value types:

- `AnalyticsValue.Str`
- `AnalyticsValue.LongVal`
- `AnalyticsValue.DoubleVal`
- `AnalyticsValue.Bool`

Use these types instead of raw `Any` values to keep event payloads explicit and safe.

### `FirebaseController.logEvent(...)`

Path: `apptoolkit/core/domain/repository/FirebaseController.kt`

`FirebaseControllerImpl` validates payloads before sending:

- Event names must match GA4 constraints (`^[A-Za-z][A-Za-z0-9_]{0,39}$`)
- Reserved prefixes are dropped (`firebase_`, `google_`, `ga_`)
- Param count is capped to 25
- Param string values are truncated to 100 chars

If invalid, the event/param is dropped and a Crashlytics breadcrumb is written.

---

## 2) Tracking custom composables

For reusable UI components (buttons, preferences, chips, dropdown items, FABs, etc.), the pattern is:

1. Add optional parameters to your composable:
   - `firebaseController: FirebaseController? = null`
   - `ga4Event: Ga4EventData? = null`
2. On the user action callback, call:
   - `firebaseController.logGa4Event(ga4Event)`
3. Then execute the business callback (`onClick()`, `onCheckedChange(...)`, etc.)

Helper path: `apptoolkit/core/ui/views/analytics/Ga4EventLogger.kt`

```kotlin
fun FirebaseController?.logGa4Event(ga4Event: Ga4EventData?) {
    if (this == null || ga4Event == null) return
    logEvent(ga4Event.toAnalyticsEvent())
}
```

### Example: custom composable button

```kotlin
@Composable
fun MyTrackedAction(
    onClick: () -> Unit,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    Button(
        onClick = {
            firebaseController.logGa4Event(ga4Event)
            onClick()
        }
    ) {
        Text("Do action")
    }
}
```

### Event naming and params for composables

Recommended schema:

- Event name: `<feature>_click` or `<feature>_toggle`
- Params:
  - `component` (e.g., `button`, `switch`, `chip`)
  - `variant` (e.g., `primary`, `outlined`, `danger`)
  - `screen` (optional but useful for cross-screen shared components)
  - `value` (for toggles/selection changes)

Example payload:

```kotlin
Ga4EventData(
    name = "settings_click",
    params = mapOf(
        "component" to AnalyticsValue.Str("button"),
        "variant" to AnalyticsValue.Str("primary"),
        "screen" to AnalyticsValue.Str("settings"),
    ),
)
```

---

## 3) Tracking screens (impressions + state)

### 3.1 Screen impressions with `TrackScreenView`

Path: `apptoolkit/core/ui/views/layouts/TrackScreenView.kt`

`TrackScreenView` emits a GA4 `screen_view` using `FirebaseController.logScreenView(...)`.

Use it once near the top of each screen composable:

```kotlin
TrackScreenView(
    firebaseController = firebaseController,
    screenName = "Help",
    screenClass = "HelpScreen",
)
```

### 3.2 Screen-state transitions with `TrackScreenState`

Path: `apptoolkit/core/ui/views/layouts/TrackScreenState.kt`

`TrackScreenState` logs a GA4 event named `screen_state` whenever the state changes between:

- `loading`
- `success`
- `no_data`
- `error`

This gives visibility beyond basic screen views.

```kotlin
TrackScreenState(
    firebaseController = firebaseController,
    screenName = "Help",
    screenState = uiState.screenState,
)
```

### 3.3 Track explicit user actions at screen level

For screen-specific actions not covered by reusable components (top-bar back, retry buttons, menu actions), call:

```kotlin
firebaseController.logEvent(
    AnalyticsEvent(
        name = "help_action",
        params = mapOf("action" to AnalyticsValue.Str("back_click")),
    )
)
```

---

## 4) Tracking ViewModels

### 4.1 Extend `LoggedScreenViewModel` when you want automatic instrumentation

Path: `apptoolkit/core/ui/base/LoggedScreenViewModel.kt`

`LoggedScreenViewModel` automatically adds:

- Breadcrumb on VM init (`vm_init`)
- Breadcrumb on every event sent to `onEvent` (`vm_event`)
- Standardized operation start event (`vm_op_start`)
- Standardized operation failure event (`vm_op_error`) + Crashlytics report

This is the recommended base when you need Firebase breadcrumbs/error telemetry.

### 4.2 Use helper APIs inside ViewModel operations

- `startOperation(action, extra)`
- `Flow<T>.catchReport(action, extra) { ... }`
- `launchReport(action, extra, block, onError)`

These helpers guarantee consistent analytics keys across features.

### Example pattern

```kotlin
class ExampleViewModel(
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<ExampleUiState, ExampleEvent, ExampleAction>(
    initialState = UiStateScreen(),
    firebaseController = firebaseController,
    screenName = "Example",
) {
    override fun handleEvent(event: ExampleEvent) {
        when (event) {
            ExampleEvent.Load -> observeData()
        }
    }

    private fun observeData() {
        flowUseCase()
            .catchReport(action = "load_data") { throwable ->
                setState { it.copy(screenState = ScreenState.Error(throwable)) }
            }
            .collectInViewModel()
    }
}
```

---

## 5) End-to-end tracking recipe for host apps

Apply this checklist to each feature/screen you integrate:

1. **Screen mount**
   - Call `TrackScreenView(...)`
2. **State transitions**
   - Call `TrackScreenState(...)` with `uiState.screenState`
3. **Reusable UI interactions**
   - Pass `firebaseController` + `ga4Event` into AppToolkit composables
4. **Screen-local interactions**
   - Call `firebaseController.logEvent(...)` for actions not tied to reusable components
5. **ViewModel operations/errors**
   - Prefer `LoggedScreenViewModel`
   - Wrap flows with `catchReport(...)` and operation launches with `launchReport(...)`

If all five are used, you can usually reconstruct the entire user journey and failure path from GA4 + Crashlytics.

---

## 6) Event taxonomy recommendations (for "track everything" safely)

To avoid noisy/inconsistent telemetry, standardize names and params across the app.

### 6.1 Naming

- Keep names short, lowercase snake_case
- Prefix by feature, e.g.:
  - `help_action`
  - `settings_toggle`
  - `onboarding_click`
  - `screen_state`

### 6.2 Common params

Use a shared baseline whenever possible:

- `screen`
- `component`
- `variant`
- `action`
- `value`
- `index` (for list items)

### 6.3 Avoid high-cardinality values

Do not send raw free text, unique IDs, stack traces, email addresses, or other personal data in event params.

Instead, send normalized categories (`"network_error"`, `"faq_item"`, `"dark"`, `"enabled"`).

---

## 7) Common mistakes and how to avoid them

1. **Not passing `firebaseController` into reusable composables**
   - Result: no event even though `ga4Event` is provided.

2. **Invalid event names/params**
   - Result: dropped by `FirebaseControllerImpl`.
   - Fix: use alphanumeric + underscore, start with a letter, avoid reserved prefixes.

3. **Tracking only clicks, not screen/state**
   - Result: impossible to correlate interactions with page/session context.

4. **Using only manual ViewModel logging**
   - Result: inconsistent error metadata.
   - Fix: use `LoggedScreenViewModel` helpers for standard telemetry shape.

5. **Over-tracking rapid UI changes**
   - Result: noisy analytics and inflated event volume.
   - Fix: track meaningful transitions and final selections.

---

## 8) Minimal integration examples

### A) Reusable AppToolkit composable call-site

```kotlin
GeneralButton(
    label = "Save",
    onClick = { viewModel.onEvent(SettingsEvent.SaveClicked) },
    firebaseController = firebaseController,
    ga4Event = Ga4EventData(
        name = "settings_click",
        params = mapOf(
            "component" to AnalyticsValue.Str("button"),
            "variant" to AnalyticsValue.Str("save")
        )
    )
)
```

### B) Screen-level tracking

```kotlin
TrackScreenView(firebaseController, screenName = "Settings", screenClass = "SettingsScreen")
TrackScreenState(firebaseController, screenName = "Settings", screenState = uiState.screenState)
```

### C) ViewModel operation tracking

```kotlin
launchReport(
    action = "save_settings",
    block = { saveSettingsUseCase() },
    onError = { throwable ->
        setState { it.copy(screenState = ScreenState.Error(throwable)) }
    }
)
```

---

## 9) Quick reference

- `Ga4EventData`:
  - `core/ui/model/analytics/Ga4EventData.kt`
- `logGa4Event` helper:
  - `core/ui/views/analytics/Ga4EventLogger.kt`
- Screen tracking composables:
  - `core/ui/views/layouts/TrackScreenView.kt`
  - `core/ui/views/layouts/TrackScreenState.kt`
- ViewModel telemetry base class:
  - `core/ui/base/LoggedScreenViewModel.kt`
- Firebase contract + implementation:
  - `core/domain/repository/FirebaseController.kt`
  - `core/data/remote/firebase/FirebaseControllerImpl.kt`

Use these APIs together to give host apps full-funnel tracking with a consistent, maintainable structure.
