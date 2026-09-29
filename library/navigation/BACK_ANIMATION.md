# Android's cross-activity back animation, and how the shell reproduces it

The back gesture between two activities is animated by the system, not by the app. This page
records where that happens in AOSP, what each class does with the gesture, and which part of
`:library:navigation` reproduces it for pages that are not activities. Read it before changing
`CrossActivityBackMotion` or `ShellNavDisplay`.

All paths are in `platform/frameworks/base` (Apache License 2.0).

## The classes

| Class | Where | Role |
|---|---|---|
| `BackAnimationController` | `libs/WindowManager/Shell/src/com/android/wm/shell/back/BackAnimationController.java` | Receives the system back gesture in the Shell (SystemUI) process. Decides what the back target is (another activity, another task, home, or the app's own callback), starts the matching `ShellBackAnimation`, and hands it the gesture. When the target is the app itself, the gesture goes to the app's `OnBackAnimationCallback` instead, which is the case for the shell's pages. |
| `BackProgressAnimator` | `core/java/android/window/BackProgressAnimator.java` | Smooths the raw gesture progress with a spring (`STIFFNESS_MEDIUM`, no bounce) before anyone sees it. Used on both sides: the Shell's animations and the app's `WindowOnBackInvokedDispatcher` both read progress through it. On cancel it springs the progress back to 0 and only then reports the cancel. With a swipe edge of `EDGE_NONE` (three-button navigation, from Android 16) it animates the progress toward 1 by itself with a low-stiffness spring while the button is held. |
| `BackTouchTracker` | `core/java/android/window/BackTouchTracker.java` | Turns touch positions into raw progress and the trigger threshold. |
| `WindowOnBackInvokedDispatcher` | `core/java/android/window/WindowOnBackInvokedDispatcher.java` | The app side: delivers `onBackStarted`, the smoothed `onBackProgressed`, `onBackCancelled` and `onBackInvoked` to the app's callback. AndroidX `navigationevent` receives these and passes them on as `NavigationEvent`s. |
| `CrossActivityBackAnimation` | `libs/WindowManager/Shell/src/com/android/wm/shell/back/CrossActivityBackAnimation.kt` | The base of the animation between two activities: the pre-commit phase, the vertical follow, the scrim, the post-commit timing and the fling spring. Works in rectangles: every frame places the closing and the entering window in a rectangle of the display. |
| `DefaultCrossActivityBackAnimation` | `libs/WindowManager/Shell/src/com/android/wm/shell/back/DefaultCrossActivityBackAnimation.kt` | The default subclass: where the rectangles start and end, and the post-commit movement. |
| `ProgressVelocityTracker` | `libs/WindowManager/Shell/src/com/android/wm/shell/back/ProgressVelocityTracker.kt` | Measures how fast the progress changes, in progress per second, for the fling. |
| `Interpolators` | `libs/WindowManager/Shell/shared/src/com/android/wm/shell/shared/animation/Interpolators.java` | `BACK_GESTURE` is `BackGestureInterpolator`, a `PathInterpolator(0.1, 0.1, 0, 1)`; `EMPHASIZED` is the two-segment `fast_out_extra_slow_in` path. |

## What the animation does

The values below are the ones the shell copies.

### Pre-commit, while the finger is down

`CrossActivityBackAnimation.onGestureProgress`:

1. `progress = BACK_GESTURE(event.progress)`, where `event.progress` has already been smoothed by
   `BackProgressAnimator`. It is not smoothed a second time.
2. The closing rectangle goes from the full display to the display scaled by `MAX_SCALE` (0.9)
   around its centre, then, unless the swipe came from the right edge, moved so its right edge is
   `cross_task_back_vertical_margin` (8dp) from the display's right edge.
3. The entering rectangle starts `cross_activity_back_entering_start_offset` (96dp) to the left
   of the display and shrinks by the same 0.9 around its own centre.
4. Both are interpolated linearly by `progress` between start and target.
5. Both shift vertically by `getYOffset`: the finger's vertical travel, as a ratio of half the
   display's height, through a `DecelerateInterpolator`, times the room left by the **current**
   closing rectangle, `(displayHeight − currentHeight) / 2 − 8dp`, never below 0. At the start of
   a gesture the page is barely smaller than the display, so it can barely move up or down.
6. A black scrim sits under the closing window, over everything else, at 0.2 alpha in light mode
   and 0.8 in dark mode, from the first frame.
7. Every window keeps the display's corner radius.

### Commit

`DefaultCrossActivityBackAnimation.onGestureCommitted` takes the rectangles **where they are**:
`startClosingRect = currentClosingRect`, `startEnteringRect = currentEnteringRect`. The targets
are the full display for the entering window, and the full display moved right by
`currentClosingRect.left + 96dp` for the closing one. A release at 22% continues from 22%.

### Post-commit

Over `POST_COMMIT_DURATION` (450ms), with the `EMPHASIZED` interpolator:

- both rectangles move from their commit position to their target;
- the closing window's alpha is `max(1 − linearProgress × 5, 0)`, gone within the first 90ms;
- the scrim fades from its full alpha to 0 linearly.

On top of that, a spring (`STIFFNESS_LOW`, `DAMPING_RATIO_LOW_BOUNCY`) starts at 100 with the
gesture's velocity: `velocity × 100 × (1 − 0.9)`, doubled for a swipe from a side edge, at least
120 when the gesture had barely started, at most 1000. Both rectangles are scaled around their
centres by `min(springValue / 100, 1)`, so a quick flick makes them dip a little smaller before
they settle.

### Cancel

`BackProgressAnimator.onBackCancelled` springs the progress back to 0, reporting it along the way,
and the rectangles follow it. Only then is `onBackCancelled` delivered.

### A swipe from the right edge

`DefaultCrossActivityBackAnimation.preparePreCommitClosingRectMovement` moves the closing window
against the right edge only `if (swipeEdge != BackEvent.EDGE_RIGHT)`: *"scale closing target into
the middle for rhs and to the right for lhs"*. A swipe from the right shrinks the window in place,
centred, and never moves it sideways with the finger, which is why it feels less direct than a
swipe from the left. This is unchanged in Android 16 (checked against LineageOS 23.2, which
tracks Android 16 QPR2); `BackTouchTracker` computes progress the same way from both edges.

The shell departs from Android here, on purpose, and by default draws a swipe from the right as
the mirror image of one from the left (`BackEdgeStyle.FollowFinger`,
`CrossActivityBackMotion.mirrorRightEdge`): the page rests against the left edge, following the
finger, and the page underneath waits on the right, with every AOSP value unchanged. Screens that
use a seeked transition, such as `ScreenTransition.Slide`, are mirrored the same way.
Setting `CrossActivityBackMotion.mirrorRightEdge` to false (in NavTest, `BackEdgeStyle.System` in the developer options) restores Android's shrink in place.

## How the shell reproduces it

| AOSP | Shell |
|---|---|
| The windows | The scenes of a `NavDisplay`. `ShellNavDisplay` wraps every scene its strategies calculate in a layer that `CrossActivityBackMotion` places in a rectangle each frame. |
| `BackAnimationController` handing the gesture to the animation | `ShellNavDisplay`'s handler on the system dispatcher. It forwards the gesture to the display's own handler, on a private dispatcher, at progress 0, so the display composes the scene underneath and holds both with a transition that moves nothing. The gesture itself goes to `CrossActivityBackMotion`. |
| `CrossActivityBackAnimation` + `DefaultCrossActivityBackAnimation` | `CrossActivityBackMotion`: the same rectangles, vertical follow, scrim, commit from the current rectangles, emphasized post-commit, closing fade and fling spring. |
| `ProgressVelocityTracker` | A `VelocityTracker1D` fed with the eased progress and the event's frame time. |
| `BackProgressAnimator` | Not reimplemented: the platform has already applied it to the progress the app receives. |

A back between two tabs is not a back between activities. There, `ShellNavDisplay` passes the
gesture through untouched and the display seeks the tab transition, as `NavDisplay` would.

## History

The first version seeked one `ContentTransform` timeline whose first half was the gesture and
whose second half was the post-commit phase. Three things made it feel close to Android but not
quite right, all fixed by the rewrite above:

- **The commit boundary.** A release anywhere was first played on to the halfway point in 120ms,
  the canonical end of the gesture, before the post-commit half began. Android continues from
  where the finger let go. The same flaw made a three-button press, which reports a small
  progress and then invokes, visibly shrink the page and then grow it again.
- **The vertical room** was computed from the final 0.9 scale, not from the current rectangle, so
  pages could drift further early in a gesture than a window can, and the vertical ratio used the
  gesture curve instead of a `DecelerateInterpolator`.
- **The fling.** Android's post-commit carries the gesture's speed into a spring; the timeline
  had a fixed settle.
