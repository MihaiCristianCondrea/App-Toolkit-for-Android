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

## References

* [`docs/crashes/README.md`](../../README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
* [session.json](session.json)
