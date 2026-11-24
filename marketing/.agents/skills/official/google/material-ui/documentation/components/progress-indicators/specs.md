---
title: "Progress indicators"
source_url: "https://m3.material.io/components/progress-indicators/specs"
lastmod: "2026-02-27"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/9253b2648ce8-mloz0082-01-3P.png"
  - "assets/images/72638a16985d-mloz0zuq-02-3P.png"
  - "assets/images/63bc8aec97fb-mloz1qik-03-3P.png"
  - "assets/images/cba7a19a73d2-mloz2lzv-04-3P.png"
  - "assets/images/716471b9ba53-mloz3iks-05-3P.png"
  - "assets/images/e3fbce4fdfbe-mloz3ss3-06-3P.png"
  - "assets/images/51047aa86b0a-mloz4e98-07-3P.png"
  - "assets/images/2a2e08582ff8-mloz4zbx-08-3P.png"
  - "assets/images/1d6cc20e6e56-mloz5p1v-09-3P.png"
---

# Progress indicators

## Specs

## Variants

![2 variant of progress indicators.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz0082-01-3P.png?alt=media&token=7398f087-5c03-4034-89bf-b5df8884ccf1>)

_Linear progress indicator; Circular progress indicator_

| Variant | M3 | M3 Expressive |
| --- | --- | --- |
| Linear progress indicator | Available | Available |
| Circular progress indicator | Available | Available |

## Configurations

![4 configurations of the linear determinate progress indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz0zuq-02-3P.png?alt=media&token=d3ebb66e-f2a4-4489-83d2-f827a5f54f9c>)

_Behavior: Determinate and indeterminate; Thickness: Default (4dp) and variable; Shape: Flat and wavy_

| Category | Configuration | M3 | M3 Expressive |
| --- | --- | --- | --- |
| Behavior | Determinate (default), Indeterminate | Available | Available |
| Track thickness | Fixed (4dp) | Available | Available |
| Configurable | \-- | Available |  |
| Shape | Flat (default) | Available | Available |
| Wavy | \-- | Available |  |

## Tokens & specs

Browse the component elements, attributes, tokens, and their values. [View baseline tokens](specs.md#c6f484b0-2bc6-4d37-bd75-f859a35a3594)

### TOKEN_TABLE

Component: Progress indicators

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| Progress Indicator - Common | md.comp.progress-indicator | COMPONENT | 10 | Common tokens for circular and linear progress indicators. |
| Progress indicator - Linear | md.comp.progress-indicator.linear | COMPONENT | 17 | For linear progress indicators. |
| Progress indicator - Circular | md.comp.progress-indicator.circular | COMPONENT | 11 | For circular progress indicators. |

#### Progress Indicator - Common (md.comp.progress-indicator)

Common tokens for circular and linear progress indicators.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.progress-indicator.active-indicator.color | COLOR | Color | `md.sys.color.primary` |  |  |
| md.comp.progress-indicator.track.color | COLOR | Color | `md.sys.color.secondary-container` |  |  |
| md.comp.progress-indicator.stop-indicator.color | COLOR | Color | `md.sys.color.primary` |  |  |
| md.comp.progress-indicator.active-indicator.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |
| md.comp.progress-indicator.track.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |
| md.comp.progress-indicator.stop-indicator.shape | SHAPE | Shape | `md.sys.shape.corner.full` |  |  |
| md.comp.progress-indicator.active-indicator.thickness | LENGTH | [Deprecated] Enabled / [Deprecated] Active Indicator | 4dp |  | Token is deprecated. |
| md.comp.progress-indicator.track.thickness | LENGTH | [Deprecated] Enabled / [Deprecated] Track | 4dp |  | Token is deprecated. |
| md.comp.progress-indicator.stop-indicator.size | LENGTH | [Deprecated] Enabled / [Deprecated] Stop indicator | 4dp |  | Token is deprecated. |
| md.comp.progress-indicator.active-indicator-track-space | LENGTH | [Deprecated] Enabled / [Deprecated] Spacing | 4dp |  | Token is deprecated. |

#### Progress indicator - Linear (md.comp.progress-indicator.linear)

For linear progress indicators.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.progress-indicator.linear.height | LENGTH | Linear - baseline | 4dp |  |  |
| md.comp.progress-indicator.linear.with-wave.height | LENGTH | Linear - baseline | 10dp |  |  |
| md.comp.progress-indicator.linear.active-indicator.thickness | LENGTH | Linear - baseline | 4dp |  |  |
| md.comp.progress-indicator.linear.track.thickness | LENGTH | Linear - baseline | 4dp |  |  |
| md.comp.progress-indicator.linear.stop-indicator.size | LENGTH | Linear - baseline | 4dp |  |  |
| md.comp.progress-indicator.linear.track-active-indicator-space | LENGTH | Linear - baseline | 4dp |  |  |
| md.comp.progress-indicator.linear.stop-indicator.trailing-space | LENGTH | Linear - baseline | 0dp |  |  |
| md.comp.progress-indicator.linear.active-indicator.wave.amplitude | LENGTH | Linear - baseline | 3dp |  |  |
| md.comp.progress-indicator.linear.active-indicator.wave.wavelength | LENGTH | Linear - baseline | 40dp |  |  |
| md.comp.progress-indicator.linear.indeterminate.active-indicator.wave.wavelength | LENGTH | Linear - baseline | 20dp |  |  |
| md.comp.progress-indicator.linear.thick.height | LENGTH | [Deprecated] Linear - thick | 8dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.linear.thick.active-indicator.thickness | LENGTH | [Deprecated] Linear - thick | 8dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.linear.thick.with-wave.height | LENGTH | [Deprecated] Linear - thick | 14dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.linear.thick.track.thickness | LENGTH | [Deprecated] Linear - thick | 8dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.linear.thick.stop-indicator.size | LENGTH | [Deprecated] Linear - thick | 4dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.linear.thick.track-active-indicator-space | LENGTH | [Deprecated] Linear - thick | 4dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.linear.thick.stop-indicator.trailing-space | LENGTH | [Deprecated] Linear - thick | 2dp |  | No longer tokenized as a variant, but rather a sample configuration in code |

#### Progress indicator - Circular (md.comp.progress-indicator.circular)

For circular progress indicators.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.progress-indicator.circular.size | LENGTH | Circular - baseline | 40dp |  |  |
| md.comp.progress-indicator.circular.with-wave.size | LENGTH | Circular - baseline | 48dp |  |  |
| md.comp.progress-indicator.circular.active-indicator.thickness | LENGTH | Circular - baseline | 4dp |  |  |
| md.comp.progress-indicator.circular.track.thickness | LENGTH | Circular - baseline | 4dp |  |  |
| md.comp.progress-indicator.circular.track-active-indicator-space | LENGTH | Circular - baseline | 4dp |  |  |
| md.comp.progress-indicator.circular.active-indicator.wave.amplitude | LENGTH | Circular - baseline | 1.6dp |  |  |
| md.comp.progress-indicator.circular.active-indicator.wave.wavelength | LENGTH | Circular - baseline | 15dp |  |  |
| md.comp.progress-indicator.circular.thick.size | LENGTH | [Deprecated] Circular - thick | 52dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.circular.thick.active-indicator.thickness | LENGTH | [Deprecated] Circular - thick | 8dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.circular.thick.track.thickness | LENGTH | [Deprecated] Circular - thick | 8dp |  | No longer tokenized as a variant, but rather a sample configuration in code |
| md.comp.progress-indicator.circular.thick.track-active-indicator-space | LENGTH | [Deprecated] Circular - thick | 4dp |  | No longer tokenized as a variant, but rather a sample configuration in code |

## Anatomy

![3 elements of a progress indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz1qik-03-3P.png?alt=media&token=e6f61f40-8a57-4255-82ce-99c8b6c5ac95>)

_Active indicator; Track; Stop indicator_

## Color

![2 color roles of a linear progress indicator in light and dark themes: the active indicator and stop indicator are primary and the track is secondary container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz2lzv-04-3P.png?alt=media&token=230849fe-6e25-46d1-839f-37a02b0323fb>)

_Progress indicator color roles used for light and dark schemes: Primary; Secondary container_

## Measurements

Wavy indicators use **amplitude** and **wavelength** to determine the shape of the wave. The height is the overall container height.

![Definitions of wave measurements for height and amplitude.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz3iks-05-3P.png?alt=media&token=fbe4684e-a3ce-4635-9b3c-e79a27e62a0f>)

_Amplitude measures from the center of the resting position to the center of the peak_

![Definitions of wave measurements for wavelength.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz3ss3-06-3P.png?alt=media&token=3eb483c9-5b25-4690-98ee-79b9f571d13e>)

_Wavelength measures the distance between two adjacent peaks_

![Linear progress indicator measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz4e98-07-3P.png?alt=media&token=9f230cb1-e0e3-4b44-a7ae-dbe345881407>)

_Size measurements for linear progress indicators. The thicker variants are provided as sample measurement for makers to adjust the default version based on their use cases._

![Circular progress indicator measurements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz4zbx-08-3P.png?alt=media&token=09e56fb3-e3bd-4798-b336-a5329bdf7585>)

_Size measurements for circular progress indicators. The thicker variants are provided as sample measurement for makers to adjust the default version based on their use cases._

![4dp padding on the left and right of the linear progress indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmloz5p1v-09-3P.png?alt=media&token=dc44f577-a8b4-4052-9bf2-e750bbcbc8b9>)

_The linear progress indicator is inset from the edge of the screen by 4dp_

## Baseline tokens

The circular and linear progress indicator had separate token sets. These are no longer recommended.

### TOKEN_TABLE

Component: Progress indicators

Configured context tags: None, Medium contrast, Default, High contrast, 3P, Android, Dark, Light

#### Token sets

| Token set | Name | Type | Tokens | Description |
| --- | --- | --- | --- | --- |
| [Deprecated] Progress indicator - Circular | md.comp.circular-progress-indicator | COMPONENT | 8 | This token set is deprecated. Use `md.comp.progress-indicator.circular` instead. Original tokens for the circular progress indicator. |
| [Deprecated] Progress indicator - Linear | md.comp.linear-progress-indicator | COMPONENT | 10 | This token set is deprecated. Use `md.comp.progress-indicator.linear` instead. Original tokens for the linear progress indicator. |

#### [Deprecated] Progress indicator - Circular (md.comp.circular-progress-indicator)

This token set is deprecated. Use `md.comp.progress-indicator.circular` instead. Original tokens for the circular progress indicator.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.circular-progress-indicator.size | LENGTH | [Deprecated] Enabled / [Deprecated] Active indicator | 48dp |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.circular-progress-indicator.active-indicator.width | LENGTH | [Deprecated] Enabled / [Deprecated] Active indicator | 4dp |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.circular-progress-indicator.active-indicator.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Active indicator | `md.sys.shape.corner.none` |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.circular-progress-indicator.active-indicator.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `md.sys.color.primary` |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.circular-progress-indicator.four-color.active-indicator.one.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `goog.ref.brand-color.blue` | Audience: 3P: `md.sys.color.primary` | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.circular-progress-indicator.four-color.active-indicator.two.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `goog.ref.brand-color.red` | Audience: 3P: `md.sys.color.primary-container` | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.circular-progress-indicator.four-color.active-indicator.three.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `goog.ref.brand-color.yellow` | Audience: 3P: `md.sys.color.tertiary` | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.circular-progress-indicator.four-color.active-indicator.four.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `goog.ref.brand-color.green` | Audience: 3P: `md.sys.color.tertiary-container` | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |

#### [Deprecated] Progress indicator - Linear (md.comp.linear-progress-indicator)

This token set is deprecated. Use `md.comp.progress-indicator.linear` instead. Original tokens for the linear progress indicator.

| Token | Type | Group | Value | Context values | Description |
| --- | --- | --- | --- | --- | --- |
| md.comp.linear-progress-indicator.track.height | LENGTH | [Deprecated] Enabled / [Deprecated] Track | 4dp |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.track.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Track | `md.sys.shape.corner.none` |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.track.color | COLOR | [Deprecated] Enabled / [Deprecated] Track | `md.sys.color.surface-container-highest` |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.active-indicator.height | LENGTH | [Deprecated] Enabled / [Deprecated] Active indicator | 4dp |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.active-indicator.shape | SHAPE | [Deprecated] Enabled / [Deprecated] Active indicator | `md.sys.shape.corner.none` |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.active-indicator.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `md.sys.color.primary` |  | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.four-color.active-indicator.one.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `goog.ref.brand-color.blue` | Audience: 3P: `md.sys.color.primary` | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.four-color.active-indicator.two.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `goog.ref.brand-color.red` | Audience: 3P: `md.sys.color.primary-container` | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.four-color.active-indicator.three.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `goog.ref.brand-color.yellow` | Audience: 3P: `md.sys.color.tertiary` | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
| md.comp.linear-progress-indicator.four-color.active-indicator.four.color | COLOR | [Deprecated] Enabled / [Deprecated] Active indicator | `goog.ref.brand-color.green` | Audience: 3P: `md.sys.color.tertiary-container` | Token set deprecated in favour of a merged token set which combines the circular and linear progress indicator. Please use "md.com.progress-indicator" tokens instead. |
