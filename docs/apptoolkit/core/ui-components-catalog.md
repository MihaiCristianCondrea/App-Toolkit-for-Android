# AppToolkit Core UI Components Catalog

This catalog summarizes reusable Compose components available in the library under:

- `apptoolkit/src/main/kotlin/com/d4rk/android/libs/apptoolkit/core/ui/views/*`

Use this page as an adoption entrypoint before diving into file-level KDoc.

---

## Buttons (`core/ui/views/buttons`)

### Problem it solves
Provides consistent, expressive button primitives with shared feedback behavior (haptics, click sound, optional GA4 event logging) and icon/text composition patterns.

### Key composables and signatures
- `GeneralButton(...)`
- `GeneralOutlinedButton(...)`
- `GeneralTextButton(...)`
- `GeneralTonalButton(...)`
- `IconOnlyButton(...)`
- `AnimatedFloatingActionButton(...)`
- `AnimatedExtendedFloatingActionButton(...)`

### Minimal usage
```kotlin
GeneralButton(
    label = "Save",
    onClick = onSave,
)
```

### Behavioral notes
- Prefer state hoisting: pass `enabled`, labels, and click handlers from the caller.
- `GeneralButton` requires at least a label or icon.
- Keep `iconContentDescription` non-null for icon-only affordances when meaningful.
- Reuse provided feedback and analytics params to preserve interaction consistency.

---

## Dialogs (`core/ui/views/dialogs`)

### Problem it solves
Reusable modal primitives for confirm/cancel flows, date picking, full-screen dialogs, and app/version info.

### Key composables and signatures
- `BasicAlertDialog(...)`
- `BasicFullScreenDialog(...)`
- `DatePickerDialog(...)`
- `VersionInfoAlertDialog(...)`

### Minimal usage
```kotlin
BasicAlertDialog(
    title = "Delete item",
    content = { Text("This action cannot be undone.") },
    onConfirm = onConfirmDelete,
    onDismiss = onDismissDialog,
)
```

### Behavioral notes
- Keep visibility state in the screen/view-model state, not inside dialog composables.
- Ensure destructive actions use explicit confirmation copy.
- Provide clear dismiss behavior and avoid ambiguous “outside tap only” exits for critical flows.

---

## Layouts (`core/ui/views/layouts`)

### Problem it solves
Standardized screen-state rendering, tracking wrappers, loading/empty placeholders, and reusable structural containers.

### Key composables and signatures
- `ScreenStateHandler(screenState, onLoading, onEmpty, onSuccess, onError)`
- `RootContentContainer(...)`
- `LoadingScreen(...)`
- `NoDataScreen(...)`
- `TrackScreenView(...)`
- `TrackScreenState(...)`
- `NonLazyGrid(...)`

### Minimal usage
```kotlin
ScreenStateHandler(
    screenState = state,
    onLoading = { LoadingScreen() },
    onEmpty = { NoDataScreen() },
    onSuccess = { data -> Content(data) },
)
```

### Behavioral notes
- Treat `UiStateScreen<T>` as the single render source for screen content.
- Keep business logic out of layout wrappers; they should remain presentation-only.
- Prefer these standardized wrappers to avoid per-screen loading/error divergence.

---

## Preferences (`core/ui/views/preferences`)

### Problem it solves
Shared settings-row components for switches, radio buttons, checkboxes, and grouped preference categories.

### Key composables and signatures
- `PreferenceItem(...)`
- `PreferenceCategoryItem(...)`
- `SwitchPreferenceItem(...)`
- `SwitchPreferenceItemWithDivider(...)`
- `SwitchCardItem(...)`
- `RadioButtonPreferenceItem(...)`
- `CheckBoxPreferenceItem(...)`

### Minimal usage
```kotlin
SwitchPreferenceItem(
    title = "Enable notifications",
    checked = state.notificationsEnabled,
    onCheckedChange = onNotificationsToggled,
)
```

### Behavioral notes
- Hoist `checked` and callbacks from the caller.
- Keep preference rows accessible (touch target, label clarity, role semantics).
- Avoid coupling preference rows to persistence APIs directly; persist in ViewModel/domain.

---

## Ads (`core/ui/views/ads`)

### Problem it solves
Composable hosts and card variants for native/banner ad placements used across screens.

### Key composables and signatures
- `HelpNativeAdCard(modifier, adUnitId)`
- `SupportNativeAdCard(...)`
- `AppsListNativeAdCard(...)`
- `NoDataNativeAdCard(...)`
- `BottomAppBarNativeAdBanner(...)`
- `AdBanner(...)`
- `NativeAdViewHost(...)`

### Minimal usage
```kotlin
HelpNativeAdCard(
    adUnitId = helpAdUnitId,
)
```

### Behavioral notes
- Always maintain clear ad disclosure labeling (for example “Ad”) in rendered templates.
- Respect user ad/consent settings before loading ad content.
- Keep ad content visually separated from functional app controls.

---

## Text (`core/ui/views/text`)

### Problem it solves
Reusable text renderers for HTML-like content and “learn more” actions.

### Key composables and signatures
- `HtmlText(...)`
- `LearnMoreText(...)`

### Minimal usage
```kotlin
LearnMoreText(
    text = "Learn more",
    onClick = onLearnMoreClick,
)
```

### Behavioral notes
- Prefer these components when rendering mixed-format text or link affordances repeatedly.
- Keep link actions explicit and auditable through screen-level event handling.

---

## Related APIs often used with core components

- Snackbar hosts/handlers: `core/ui/views/snackbar/*`
- Navigation app bars: `core/ui/views/navigation/*`
- Haptic/animation modifiers: `core/ui/views/modifiers/*`
- Common spacing primitives: `core/ui/views/spacers/*`

For architecture and ViewModel event/state contracts, see:
- `docs/apptoolkit/app-toolkit-library.md`
- `docs/viewmodel-rules-coroutines-flows-state.md`
