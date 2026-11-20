# Motion physics system

M3 Expressive (May 2025) introduced the motion physics system, which replaces the easing and duration
system for components. Easing and duration are still used for transitions; see `transitions.md`.
Spring values and the web curve conversions are in `tokens.md`.

Official pages: `documentation/styles/motion/overview/how-it-works.md`,
`documentation/styles/motion/overview/specs.md`, `documentation/blog/m3-expressive-motion-theming.md`.
Animated examples: `grep -i "^m3-expressive-motion-theming" visuals/INDEX.tsv`.

## The two schemes

The scheme defines how the product feels. Most motion in a product should use one scheme.

| Scheme | Feel | Use for |
|---|---|---|
| Expressive | Overshoots final values to add bounce. | Material's recommendation for most situations, particularly hero moments and key interactions. |
| Standard | Eases into final values with minimal bounce. | Utilitarian, functional products. |

Custom schemes are a supported path.

Material recommends Expressive for most products. That is a product-level default, not a reason to
make every interaction bounce. A dense utility screen inside an expressive product still benefits
from calm, fast feedback; Level 3 below is the sanctioned way to get it.

## Spatial and effects springs

| | Animates | Behavior |
|---|---|---|
| Spatial | x and y position, rotation, size, corner radius | Overshoots the final value and settles. |
| Effects | Color, opacity | Never overshoots. Damping is 1.0 in both schemes. |

This is the distinction most often gotten wrong. A bouncing opacity fade reads as a glitch. If the
property is not geometric, it takes an effects spring.

## Speed

Three speeds per style. Most motion should use default.

| Speed | Spatial example | Effects example |
|---|---|---|
| Default | Animations that partly cover the screen, such as a bottom sheet or an expanded navigation rail. | Opacity of content inside a navigation rail. |
| Fast | Small components, such as switches and buttons. | Color change of a switch handle. |
| Slow | Full-screen animations. | Full-screen content refresh. |

Smaller elements move faster and larger elements move slower.

Spring tokens are device-aware. `fast` is always faster than `default`, but the absolute values differ
for wearable, phone, and tablet so motion feels fast in context.

## Why springs

- One spring covers transitions, button feedback, and gestures, so expression stays consistent.
- Springs behave predictably, like physical objects.
- Springs handle gestures, interruptions, and retargeting. Fixed-duration curves do not, which is the
  reason to prefer springs on the web as well.

## Three levels of application

1. Use a default scheme. On Jetpack Compose, 21 Material components use the physics system out of
   the box.
2. Create a custom scheme. A custom `MotionScheme` returns a different `AnimationSpec` per property and
   changes every component and transition at once.
3. Swap the scheme per element. Run expressive product-wide and override the scheme through its
   `CompositionLocal` for one composable, screen, or element, or the reverse.

## Platform availability

| Platform | Status | How to apply |
|---|---|---|
| Jetpack Compose | Available. | Built-in components, the `MotionScheme` API, and spring tokens. |
| Android Views (MDC-Android) | Spring tokens exist but are not wired into components. | Use the spring values directly with `SpringAnimation`. |
| Web | Compatible with Compose springs. | Real springs where possible (Web Animations API with a spring library); otherwise the curve conversion table in `tokens.md`. |

### Compose

```kotlin
// Read springs from the active scheme instead of hard-coding them.
val spatial = MaterialTheme.motionScheme.defaultSpatialSpec<Dp>()
val effects = MaterialTheme.motionScheme.defaultEffectsSpec<Color>()

val width by animateDpAsState(if (expanded) 320.dp else 96.dp, animationSpec = spatial)
val tint by animateColorAsState(
    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    animationSpec = effects,
)
```

Set the scheme on the theme (`MaterialTheme(motionScheme = MotionScheme.expressive())`) and choose
`fast*Spec` or `slow*Spec` by element size. Confirm the function names in the installed release.

### Web

Prefer the spring values from `tokens.md` through a spring implementation when the motion can be
interrupted or driven by a gesture. For a non-interruptible animation, use the converted
`cubic-bezier` and duration pair. Respect `prefers-reduced-motion` by removing spatial movement and
keeping short effects.

## Custom components

Components outside Material, including your own, get the physics system through the spring tokens:
`md.sys.motion.spring.{default|fast|slow}.{spatial|effects}`. The product-level scheme decides the
feel. Do not hand-author durations for expressive motion.

## Shape morph

Shape morphing runs on the expressive scheme by default and can switch to standard. Morph is a
geometry change, so it uses spatial springs.

## What M3 does not publish

There are no official stagger values, no per-component choreography timings, and no "reduce motion"
scheme-swap rule. If a design needs them, they are design decisions. Label them as such and do not
present them as M3 spec. `marketing/.agents/skills/official/lottiefiles/motion-design` covers general
choreography principles.

## Motion review

- Each animation explains a state change, a spatial relationship, navigation, or direct
  manipulation.
- Spatial properties use spatial springs; color and opacity use effects springs.
- Animations are interruptible and land on the current state.
- Controls do not move away from the pointer or finger during an animation.
- The interface works with animations disabled or reduced.
- Repeated utility actions use the fast or standard path; hero moments are rare.
