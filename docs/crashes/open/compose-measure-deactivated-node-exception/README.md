# Incident Report: Compose Measure on Deactivated Node Crash

* **Status:** Open (Unresolved)
* **Issue ID:** `011e39c9c0421f6aeb85cf272c2f2a51`
* **Application:** `com.d4rk.android.apps.apptoolkit` version `26.10.31` (`137261031`)
* **Date:** October 13, 2026 at 06:55:21 EEST

## Behavior and Impact

During UI rendering / traversal on Android 15 (OnePlus CPH2465), Jetpack Compose attempts to measure a layout node that was deactivated or recycled (e.g., during subcomposition, animated content transition, or lazy list item reuse).

This triggers a fatal `java.lang.IllegalArgumentException: measure is called on a deactivated node`, crashing the application during layout traversal.

## Failure Details

* **Exception:** `java.lang.IllegalArgumentException: measure is called on a deactivated node`
* **Location:** `androidx.compose.ui.node.LayoutNode.measure` / `SubcomposeLayout` traversal
* **Stack Trace Summary:**
  ```text
  Fatal Exception: java.lang.IllegalArgumentException: measure is called on a deactivated node
         at tq3.a(r8-map-id-26bba04e034a85e1a8b05826a43cfd9d6bdb8ca505b2ad4571d1a1b59d0617fb:3)
         at gn4.v0(r8-map-id-26bba04e034a85e1a8b05826a43cfd9d6bdb8ca505b2ad4571d1a1b59d0617fb:13)
         at android.view.ViewRootImpl.doTraversal(ViewRootImpl.java:3306)
  ```

## Affected Area

* **Module:** `:library:core:ui` / Jetpack Compose UI layout pipeline
* **Component:** `SubcomposeLayout`, `LazyColumn` / animated layout nodes

## Guidance & Open Questions

1. Inspect screens using custom `SubcomposeLayout`, `AnimatedContent`, or `movableContentOf` where a measure pass might be requested on a disposed node.
2. Check whether upgrading Jetpack Compose UI library version addresses known upstream layout node deactivation state timing issues.
3. Keep as **Open / Unresolved** until reproduction steps or specific screen context is isolated.

## Investigation: October 17, 2026

The session identifies `:sample:feature:tiles`, not an unknown screen: after onboarding, the user
selected the `notadded` filter and toggled the `system` and `utilities` categories. The last toggle
was five seconds before the fatal event. Reproduce rapid expansion, collapse, filtering and scrolling
with ads arriving during those transitions on Android 15.

`ToolkitTilesScreen` uses primitive category/ad keys and separate content types. Category contents
use a bounded `Column` inside `AnimatedVisibility`, with `animateItem` on the category container.
No app-owned `SubcomposeLayout`, retained `Measurable`, or direct node measurement was found in this
path. These observations do not prove that the upstream layout is correct or identify a safe fix.

The available local release mapping has ID
`3b59f9440434c4e992af9f166d4cbae744c37dcad48cbb0b6dff2a1cb3eb44fc`, which differs from the
reported `26bba04e034a85e1a8b05826a43cfd9d6bdb8ca505b2ad4571d1a1b59d0617fb`. Obtain the matching
release mapping and full retraced stack before attributing this to a particular Compose component.
Do not disable animations or replace navigation based only on this stack.

The current debug APK passed a limited API 37 smoke check using the Not added filter, eight
Utilities category toggles and background/resume, with no app crash. This does not reproduce the
OnePlus Android 15 environment or establish that the crash is fixed.

## References

* [`docs/crashes/README.md`](../../README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
* [session.json](session.json)
