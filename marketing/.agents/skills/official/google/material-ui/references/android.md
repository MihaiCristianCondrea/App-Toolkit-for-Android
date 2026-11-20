# Material 3 on Android

Implementation guidance for Material 3 on Android phones, tablets, foldables, desktop windows, Wear
OS, and widgets. Availability by feature is in `platforms.md`; versions and links are in
`sources.md`. Screen architecture (ViewModels, UI state, events) belongs to the `android-ui-layer`
skill, not here.

Official pages: `documentation/develop/android/jetpack-compose.md`,
`documentation/develop/android/mdc-android.md`, `documentation/blog/material-is-compose-first.md`.

## Choose the toolkit

- Jetpack Compose with `androidx.compose.material3` is the primary path. Material announced that
  "Material Android is all-in on Compose", and Compose gets new components and Expressive features
  first.
- Android Views with Material Components for Android (MDC-Android) is in maintenance mode. 1.14.0 is
  the final feature release. Keep existing View screens working; do not migrate a View flow to Compose
  as part of a small visual change, and do not start new features in Views unless the project
  requires it.
- Wear OS uses `androidx.wear.compose:compose-material3`, a separate Wear-specific implementation.
  Phone components are not substitutes for round-screen, rotary, or edge-hugging Wear components.
  Use the `wear-compose-m3` skill at `.agents/skills/official/google/android/wear/wear-compose-m3`.
- App widgets use Glance (`androidx.glance:glance-material3` for theming) where Glance is the widget
  stack. Use the `glance-widget` skill. Widget capabilities are narrower than in-app Compose.
- Android XR has its own Material guidance (`documentation/foundations/xr/`).
- Compose Multiplatform ships its own Material 3 artifact. Check which version and APIs each target
  supports; Android-only APIs (dynamic color, some adaptive and window APIs) may not exist on others.

If the project depends on a shared design-system or app framework library (App Toolkit, for
example), use its public theme, components, and screen conventions first and treat this file as the
Material rules underneath them. Check the project's dependencies before assuming such a library
exists.

## Library map

Resolve versions through the project's version catalog or BOM, and check current releases before
adding or upgrading anything.

| Purpose | Artifact |
|---|---|
| Phone and tablet components | `androidx.compose.material3:material3` |
| Window size and posture | `androidx.compose.material3.adaptive:adaptive` |
| Canonical pane layouts | `androidx.compose.material3.adaptive:adaptive-layout` |
| Pane navigation | `androidx.compose.material3.adaptive:adaptive-navigation` |
| Navigation 3 integration | `androidx.compose.material3.adaptive:adaptive-navigation3` |
| Bar and rail switching | `androidx.compose.material3:material3-adaptive-navigation-suite` |
| Wear OS | `androidx.wear.compose:compose-material3` |
| Widgets | `androidx.glance:glance-material3` |
| Views | `com.google.android.material:material` |
| Icons | Material Symbols downloaded as vector drawables. `material-icons-extended` is no longer updated. |

## Dependency and API gate

Before writing code that depends on a Material API:

1. Read the version catalog, Compose BOM, Kotlin and AGP versions, `compileSdk`, `minSdk`, and the
   existing opt-ins.
2. Find the API in the reference for the installed version, or in the library sources in the Gradle
   cache. Do not infer availability from a design, a blog post, or this skill.
3. Prefer a stable API that meets the requirement.
4. If the design needs an experimental API, keep the opt-in narrow (per function, not per file) and
   say so in the handoff.
5. Do not change the release channel (stable to alpha) or upgrade the BOM as a side effect of a
   design task. Propose it separately with the tradeoff.
6. Do not add a library for something the existing Material artifact already provides.

Compose Material 3 1.5.0 (stable, November 2026) graduated most Expressive components and the
Expressive theming APIs. Loading indicators and `MaterialShapes` remained experimental in 1.5.0.
Projects on 1.4.x or earlier need `@OptIn(ExperimentalMaterial3ExpressiveApi::class)` for Expressive
APIs that exist there. Recheck this against the installed version.

## Material 2 to Material 3

What changes when a project moves from Material 2:

- Tone-based surface roles replace shadows and elevation overlays as the main depth cue.
- A full scheme is generated from one source color, with optional dynamic color.
- The 10-step corner scale replaces the three size-based shape categories.
- The 15-style type scale (display to label) replaces the M2 scale names (h1 to overline).
- Spring-based motion schemes replace fixed easing for components.
- Three contrast levels are built into the scheme.

Migrate the theme first, then components, and do not mix `androidx.compose.material` and
`androidx.compose.material3` components on one screen except during a planned migration. Guide:
https://developer.android.com/develop/ui/compose/designsystems/material2-material3

## Theme

Provide the theme once near the app root and read roles below it. The Compose theme pattern,
dynamic color, and Views theme setup are in `theming.md`.

- Use `MaterialTheme.colorScheme`, `MaterialTheme.typography`, `MaterialTheme.shapes`, and
  `MaterialTheme.motionScheme` inside components.
- Keep app-specific roles in a small explicit extension instead of literal colors in screens.
- Review generated theme files against existing product tokens before replacing anything.

## Compose components

- Prefer Material components. They carry sizing, semantics, states, and theme integration.
- Customize through parameters and `*Defaults` factories (colors, elevation, shape, padding, scroll
  behavior). Do not copy a Material component's source to change one value.
- Keep the 48dp minimum interactive size. A small icon sits inside a larger target.
- Hoist state and events out of reusable visual components; follow the project's screen architecture.
- Use stable keys and content types in lazy lists.
- Let the screen's `Scaffold` own snackbars, sheets, dialogs, and loading overlays, not leaf
  composables.
- Use `rememberSaveable` or the state holder for user-visible state that must survive recreation.
- Let navigation own predictive back. Avoid scattering competing `BackHandler` calls.

Short examples:

```kotlin
// Emphasis by variant, not by color overrides.
Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    OutlinedButton(onClick = onCancel) { Text(stringResource(R.string.cancel)) }
    Button(onClick = onSave) { Text(stringResource(R.string.save)) }
}

// Text field with label, supporting text, error, and IME action.
OutlinedTextField(
    value = email,
    onValueChange = onEmailChange,
    label = { Text(stringResource(R.string.email)) },
    supportingText = { if (emailError != null) Text(emailError) },
    isError = emailError != null,
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
)
```

## Loading and forms in Compose

- Keep loading state in the screen state holder so it can coordinate retry, cancellation, and stale
  data.
- Use placeholders that match the final layout when the structure is known.
- Use `LinearProgressIndicator` or `CircularProgressIndicator` for focused work, determinate when the
  fraction is real. `LoadingIndicator` is the Expressive indicator for short indeterminate waits; it
  is still experimental in 1.5.0.
- Keep stale content visible during refresh, with a local pending indicator (for example
  `PullToRefreshBox`).
- Use `SnackbarHost`, inline errors, or a retry action according to the failure's scope.
- Use `TextField` or `OutlinedTextField` with `KeyboardOptions`, `KeyboardActions`, and IME insets
  handling instead of hand-built text boxes.
- Make submit idempotent and prevent duplicate events at the state boundary.

## Edge-to-edge and insets

- Enable edge-to-edge in the Activity, usually with `enableEdgeToEdge()`. Apps targeting API 35 are
  edge-to-edge on Android 15, and the opt-out is removed for apps targeting API 36.
- Draw backgrounds and scrolling content behind system bars; inset text, controls, and gesture
  targets with the right `WindowInsets`.
- Account for status bar, navigation bar, display cutout, caption bar, hinge, and IME.
- Material components such as `Scaffold`, top app bars, and navigation bars already handle insets.
  Do not apply the same inset in the parent and the child.
- Use `imePadding()` or `Scaffold` content insets so the focused field and its action stay visible.
- Keep system bar icons legible against the actual background.

Inset bugs usually come from padding applied twice or consumed at the wrong level. Review the whole
modifier chain. The `edge-to-edge` skill at `.agents/skills/official/google/android/system/edge-to-edge`
covers this in depth.

## Android Views

- Use a Material 3 theme (`Theme.Material3.*`, or `Theme.Material3Expressive.*` on MDC-Android
  1.14) and Material Components widgets, not AppCompat lookalikes with manual styling.
- Resolve colors through theme attributes and shapes through `shapeAppearance` styles.
- Handle insets with the Views insets APIs (`ViewCompat.setOnApplyWindowInsetsListener`) and avoid
  mixing manual status-bar padding with automatic handling.
- For Compose inside Views, set a `ViewCompositionStrategy`, provide the app's Compose theme, and keep
  state ownership explicit.

### Expressive in MDC-Android 1.14

MDC-Android 1.14 includes a Views Expressive set:

- the `Theme.Material3Expressive.*` theme family and the emphasized type scale;
- Expressive styles for lists, buttons and icon buttons, button groups, FABs, top app bar,
  `BottomNavigationView`, navigation rail, search, progress indicators, and sliders;
- spring tokens for the motion physics system, which are not wired into components.

The API is style and resource based and does not mirror Compose names. Check each component's
documentation in the MDC repository. Upgrading to 1.14 raises `minSdk` to 23 and changes toolchain
requirements, so do not take it as an incidental dependency. No further feature releases are planned.

## Performance

- Do not allocate per frame or run color generation, image decoding, or text measurement in
  composition without caching.
- Do not nest same-direction lazy scrolling without a defined size and interaction model.
- Keep shape and size animations local; check them on a representative device when they run inside
  long lists.
- Large blurred shadows and stacked translucent layers cost rendering time as well as clarity.

## Android verification

Compile the changed module, then check the relevant targets:

- Phone and tablet: compact portrait plus every affected window class, edge-to-edge, IME, rotation and
  resize, and touch plus keyboard and mouse where supported.
- Wear: supported round and square sizes, rotary and touch input, ambient mode where relevant.
- Widgets: every supported size, host theme, resizing, and truncation.
- All targets: light, dark, and dynamic color; large font scale; long localized text; target sizes;
  and the loading, error, selected, disabled, and restored states the feature can reach.

Compose previews (`@Preview` with `uiMode`, `fontScale`, and device specs) help catch layout and
theme problems. They are not device verification. Report what was observed on a device or emulator
separately from what compiled or rendered in preview.
