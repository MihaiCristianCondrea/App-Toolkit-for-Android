# Quick Tools category lookahead crash

## Evidence

Reported on October 26, 2026 while scrolling Quick Tools and expanding a category.
The original log is preserved in `dsada.txt`.

The fatal exception is `IllegalArgumentException: measure is called on a deactivated node`.
The stack passes through `LookaheadPassDelegate.onBeforeLayoutChildren`, lazy layout placement,
and `MeasureAndLayoutDelegate`. It contains no application frame identifying the exact node.

## Change

`:sample:feature:tiles` now renders its four bounded category groups in a regular scrolling
column instead of reusable lazy-list items. Category expansion no longer changes the height of
an item that can be deactivated by vertical lazy-list recycling. The catalogue retains its
category expansion animations and one middle ad. Compose dependency versions are unchanged.

## Verification

- `:sample:feature:tiles:testDebugUnitTest`: 102 tests passed, including the new Robolectric
  scroll-and-expand test inside a lookahead scope.
- `:sample:app:assembleDebug` and `checkModuleBoundaries`: passed.
- Installed the fixed debug APK on `emulator-5554`, Android 17 (API 37).
- Scrolled through the catalogue and toggled all four groups, 11 category actions in total.
  The app process remained running and its process-filtered crash log was empty.

## Remaining uncertainty

The original intermittent exception was not reproduced before the change. The automated scene
also passed against the previous layout, so it checks scrolling and expansion behavior rather than
proving the original timing condition. This record remains open pending confirmation that the
reported failure no longer occurs during normal use.
