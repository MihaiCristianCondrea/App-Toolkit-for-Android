# Use AppToolkit window size classes for adaptive layouts

This guide explains how to use AppToolkit window size helpers in apps that depend on the library.

The API lives in:

- `com.d4rk.android.libs.apptoolkit.core.ui.window`

It extends Material 3 adaptive width handling with two additional width buckets (`Large`, `ExtraLarge`) so your UI can better adapt to tablets, desktop windows, and large displays.

## What this API offers

`WindowSizeClassUtils.kt` provides:

- `AppWindowWidthSizeClass` enum
    - `Compact`
    - `Medium`
    - `Expanded`
    - `Large`
    - `ExtraLarge`
- `rememberWindowAdaptiveInfo()`
    - Wraps Material adaptive info and enables large/xlarge width support by default.
- `rememberWindowSizeClass()`
    - Returns the current `WindowSizeClass`.
- `rememberWindowWidthSizeClass()`
    - Returns `AppWindowWidthSizeClass` for the active window.
- `WindowSizeClass.toAppWindowWidthSizeClass()`
    - Maps raw width breakpoints into the AppToolkit enum.

## Add imports

In your composable screen, add:

```kotlin
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.d4rk.android.libs.apptoolkit.core.ui.window.rememberWindowWidthSizeClass
```

If you need lower-level access:

```kotlin
import androidx.window.core.layout.WindowSizeClass
import com.d4rk.android.libs.apptoolkit.core.ui.window.rememberWindowAdaptiveInfo
import com.d4rk.android.libs.apptoolkit.core.ui.window.rememberWindowSizeClass
import com.d4rk.android.libs.apptoolkit.core.ui.window.toAppWindowWidthSizeClass
```

## Basic usage in a screen

```kotlin
@Composable
fun AppsScreen() {
    val windowWidthSizeClass: AppWindowWidthSizeClass = rememberWindowWidthSizeClass()

    when (windowWidthSizeClass) {
        AppWindowWidthSizeClass.Compact -> PhoneLayout()
        AppWindowWidthSizeClass.Medium,
        AppWindowWidthSizeClass.Expanded,
        AppWindowWidthSizeClass.Large,
        AppWindowWidthSizeClass.ExtraLarge -> TabletOrDesktopLayout()
    }
}
```

## Recommended pattern

- Compute the window class once at the screen entry point.
- Pass `windowWidthSizeClass` down as a parameter to child composables.
- Keep business logic in the ViewModel; use size class only for layout decisions.
- Avoid orientation-only branching (`isLandscape`) when width classes can represent the UI intent more accurately.

## Example in this repository

The sample app already uses this pattern:

- `MainScreen` reads `rememberWindowWidthSizeClass()` and switches layout behavior.
- App list/loading composables adapt grid columns by `AppWindowWidthSizeClass`.

See:

- `app/main/ui/MainScreen.kt`
- `app/apps/common/ui/views/screens/AppsList.kt`
- `app/apps/common/ui/views/screens/loading/HomeLoadingScreen.kt`

## Breakpoint behavior

The mapping order is:

1. `WIDTH_DP_EXTRA_LARGE_LOWER_BOUND` -> `ExtraLarge`
2. `WIDTH_DP_LARGE_LOWER_BOUND` -> `Large`
3. `WIDTH_DP_EXPANDED_LOWER_BOUND` -> `Expanded`
4. `WIDTH_DP_MEDIUM_LOWER_BOUND` -> `Medium`
5. Otherwise -> `Compact`

Because mapping checks from largest to smallest, every width resolves to exactly one bucket.

## When to use which API

- Use `rememberWindowWidthSizeClass()` in most screens.
- Use `rememberWindowSizeClass()` when you need direct `WindowSizeClass` APIs.
- Use `rememberWindowAdaptiveInfo()` if you need the full adaptive object.
- Use `toAppWindowWidthSizeClass()` for custom conversion pipelines or tests.
