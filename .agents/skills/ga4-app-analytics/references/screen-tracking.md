# Screen Tracking

## Purpose

Screen tracking should describe meaningful user-visible destinations.

Modern Android apps often use a single Activity with Compose navigation, so explicit screen tracking is frequently necessary.

## App Toolkit

For App Toolkit hosts, prefer `TrackScreenView` for meaningful top-level Compose destinations.

The helper sends the screen view when the stable screen name changes.

## Track destinations, not recompositions

Correct model:

```text
navigation destination becomes visible
        ↓
screen_view
```

Incorrect model:

```text
Composable recomposes
        ↓
screen_view
```

A screen event should not fire simply because state changed and Compose recomposed the UI.

## Stable names

Prefer stable, human-readable identifiers:

```text
Home
Scanner
SpeedTest
Settings
DeviceDetails
LessonDetails
```

Avoid dynamic identifiers:

```text
Device_192_168_1_5
Lesson_729148
Search_kotlin_coroutines
File_/storage/emulated/0/...
```

Dynamic screen names create high cardinality and can expose private information.

## Bottom sheets and dialogs

Track a bottom sheet as a screen only when it behaves like a meaningful destination with enough interaction or content to justify separate analysis.

Examples that may deserve screen tracking:

- Detailed device information sheet.
- Product details sheet.
- Lesson details sheet.

Examples that usually do not need a separate screen:

- Simple confirmation dialog.
- Tiny menu.
- One-action warning.

When a small surface matters, a product event is often better than a screen view.

## Navigation inspection

During review, compare the navigation graph with existing screen tracking.

Look for:

- Missing meaningful destinations.
- Duplicate screen calls.
- Dynamic screen names.
- Screens tracked from both Activity and Compose paths.
- Sheets tracked inconsistently.
- Embedded screens that may be visible without navigation changes.

## Screen state

App Toolkit's `TrackScreenState` can report bounded states such as:

```text
loading
success
no_data
error
```

Use it when state transitions are useful for product or reliability analysis.

Do not treat screen state events as a substitute for product outcome events.
