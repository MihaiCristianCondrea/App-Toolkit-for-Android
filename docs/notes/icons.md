# Toolkit icons

`ToolkitIcon` is the icon slot used by every toolkit component that draws an icon: navigation drawer
items, bottom bar and rail items, and the `General*Button` family. It lives in
`:library:core:designsystem`, package
`com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons`.

## The three sources

| Source | Declares | Animates |
| --- | --- | --- |
| `ToolkitIcon.Vector(imageVector)` | A Compose `ImageVector`, for example `Icons.Rounded.Share` | No |
| `ToolkitIcon.Resource(resId)` | A drawable or vector XML resource, drawn through a painter | No |
| `ToolkitIcon.AnimatedVector(resId)` | An `animated-vector` resource | Yes, on click and on selection |

`ToolkitIcon.of(...)` and `ToolkitIcon.animated(...)` are shorthand factories for the same three
types.

`AnimatedVector` requires a real `animated-vector` drawable, the kind
`androidx.compose.animation.graphics` can read: one `vector` plus the `target` animators that move it
from the first frame to the last one. A plain `vector` resource passed as `AnimatedVector` fails to
inflate at runtime. Declare it as `Resource` instead.

Anything else, such as a Coil `Painter` or a bitmap, is not accepted. Draw those with a plain
`Image` next to the component instead of through the icon slot.

## Components with a selected state

Navigation items own two slots, `icon` for the unselected state and `selectedIcon` for the selected
one. `selectedIcon` defaults to `icon`, so a single icon covers both. Any source is allowed in either
slot, which gives four combinations:

| `icon` | `selectedIcon` | Result |
| --- | --- | --- |
| static | static | Plain swap when selection changes. Nothing animates. |
| static | animated | The static icon is drawn at rest. The first click swaps in the animated one and plays it, and every later click plays it again. |
| animated | animated (same or another) | The drawable rests on its first frame while unselected and on its last frame while selected, and replays on every click. |
| animated | static | The animated icon plays while the item is unselected; the static one takes over once it is selected. |

The static plus animated combination is the one to use for an action such as Share, which is clicked
repeatedly and never becomes a selected destination. Draw the first frame of the animated vector
like the static icon, otherwise the swap on the first click is visible. When the first frame already
matches, passing the animated vector alone as `icon` is simpler and behaves the same.

## Components without a selected state

Buttons take a single `icon`. An animated icon plays every time the button is clicked; a static one
just draws. Nothing else about the button changes.

```kotlin
GeneralButton(
    onClick = ::share,
    label = stringResource(id = R.string.share),
    icon = ToolkitIcon.AnimatedVector(R.drawable.anim_share),
)
```

## Replaying an animation

A click that happens while the drawable already rests on its last frame is a replay, and
`ToolkitIconReplayMode` decides what it looks like:

- `Restart`, the default, plays the animation forward from its first frame again. It suits the usual
  icon whose last frame is drawn like its first one, so the reset is invisible and every click looks
  the same.
- `Reverse` plays the animation backwards, from the last frame to the first one. Use it for a
  drawable that morphs between two distinct shapes and should visibly travel back, such as a
  play/pause or a menu/close toggle.

```kotlin
ToolkitIcon.AnimatedVector(
    resId = R.drawable.anim_menu,
    replayMode = ToolkitIconReplayMode.Reverse,
)
```

`atEnd` sets the frame the drawable rests on before anything happens, and is `false`, the first
frame, by default.

## Rendering an icon outside a toolkit component

Two composables are public:

- `ToolkitIconContent(icon, contentDescription, modifier, atEnd, tint)` draws exactly one icon and
  owns no state. `atEnd` picks the frame of an animated vector, and changing it animates between the
  two frames.
- `AnimatedToolkitIcon(icon, clickCount, contentDescription, modifier, selectedIcon, selected, tint)`
  is the stateful one the toolkit components use. The owner keeps a click counter and increments it
  on every click; each increment replays the animation. Owners that never animate pass `0`.

`resolveToolkitIcon(icon, selectedIcon, selected, interacted)` returns the icon a component should be
drawing, and is useful when a host renders its own navigation surface from toolkit item models.

## Accessibility

The content description belongs to the component, not to the icon. Navigation items reuse their
title, and buttons take `iconContentDescription`, which is required for an icon-only button because
nothing else describes it.
