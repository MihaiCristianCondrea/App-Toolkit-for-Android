# Screen Tracking

## Purpose

Screen tracking should describe meaningful user-visible destinations.

Modern Android apps often use a single Activity with Compose navigation, so explicit screen tracking
is frequently necessary.

## App Toolkit

For App Toolkit hosts, wrap every meaningful destination in `ScreenHost(screen = ...)`, with the
screen's stable `TrackedScreen`. It is the one screen-tracking call.

It reports a screen view per visit: when the destination becomes visible and active (its
lifecycle reaches `RESUMED`), not when it enters composition. A screen that only animates, is
kept for a back gesture, or sits under an overlay is not reported; rotating keeps the visit;
returning from another tab, a page or the background starts a new one; each pane of a list-detail
layout reports its own visits.

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

Track a bottom sheet as a screen only when it behaves like a meaningful destination with enough
interaction or content to justify separate analysis.

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

App Toolkit's `ScreenHost(status = ...)` reports bounded states, at the start of each visit and
when the label changes, such as:

```text
loading
success
no_data
error
```

Use it when state transitions are useful for product or reliability analysis.

Do not treat screen state events as a substitute for product outcome events.
