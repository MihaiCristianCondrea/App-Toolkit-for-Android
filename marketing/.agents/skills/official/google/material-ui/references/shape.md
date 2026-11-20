# Shape

"Shape" covers three separate things: the corner radius scale for component rectangles, the shape
library of 35 iconic shapes for decoration, and shape morphing between shapes. The numeric scale is
in `tokens.md`; per-component shape assignments are in `component-tokens.md`.

Official pages: `documentation/styles/shape/overview-principles.md`,
`documentation/styles/shape/corner-radius-scale.md`, `documentation/styles/shape/shape-morph.md`.
Images: `grep -i "^styles_shape" visuals/INDEX.tsv`.

## Principles

- Use abstract shapes thoughtfully, to add emphasis and decorative flair.
- Use Material shapes for built-in shape morphing.
- Rectangular shapes are fully rounded in all corners by default.
- Individual corners can be adjusted to create asymmetric rectangles.

## The shape library: 35 shapes

Circle, Square, Slanted, Arch, Fan, Arrow, SemiCircle, Oval, Pill, Triangle, Diamond, ClamShell,
Pentagon, Gem, Sunny, VerySunny, Cookie4Sided, Cookie6Sided, Cookie7Sided, Cookie9Sided,
Cookie12Sided, Clover4Leaf, Clover8Leaf, Ghostish, Burst, SoftBurst, Boom, SoftBoom, Flower, Puffy,
PuffyDiamond, PixelCircle, PixelTriangle, Bun, Heart.

All 35 can morph into each other. They are available in the Figma Design Kit shape library and the
Compose `MaterialShapes` API. Material lists them as unavailable on the web.

### Where to use them

Use the shape library for mostly visual elements: image crops, avatar masks, loading indicators,
graphics, decorative UI, and other non-interactive elements.

Avoid unconventional shapes on text-heavy containers. Use them sparingly; sparing use is what makes
them read as emphasis.

## Shape morphing

Morph shapes to improve understanding. A morph can communicate:

- Interaction states, such as a button becoming selected.
- Actions in progress, such as a friend typing or a page loading.
- Changes in the environment, such as sound, temperature, or time of day.

Consider how shapes react to tapping, swiping, scrolling, releasing, and long pressing. Shape morphing
should respond to user interaction.

Material itself uses morph in the standard button group (to show interaction) and the loading
indicator (to show progress). Morph uses the expressive motion scheme by default.

Buttons morph by state. Pressed buttons become more square, and round and square buttons share the
same pressed shape. Toggle buttons also swap their resting shape: round when unselected and square
when selected, or the reverse if the unselected shape is square.

## Shape is versatile, not semantic

Do not make shapes literal or give one shape a fixed meaning. The loading indicator can be wavy, but a
waveform is not a strict symbol of progress; rotating shapes or a morph work equally well. Waveforms
are also fine on a button container, where they say nothing about progress.

## Tension

Tension is a deliberate change in the shape story: contrasting shapes, square mixed with rounded, or
an unconventional silhouette.

Material historically favored rounded shapes. Using sharp shapes on purpose adds tension and makes a
design more dynamic and memorable. Use tension to convey state, draw attention to an element, or
improve the composition.

Combining shapes and corner radii creates either tension or cohesion, and both direct focus. Break
from the surrounding shape style to draw attention to one element.

Watch the inverse failure: smaller shapes make essential actions look less important.

## Shape and type must agree

M3 shapes echo attributes of M3 typography; M3 shapes and Google Sans Flex share roundness attributes.
Use shape and type together. A round shape language paired with a rigid, condensed typeface reads as
unresolved.

## Layered shape (2.5D)

Applying motion and shape differently on each layer gives the illusion of depth and volume.

## Symmetry and inner corners

Components can be symmetric (all corners equal) or asymmetric (per-corner values). Both use the same
10-step scale. Asymmetric shapes appear in M3 components with closely grouped items, such as menus and
split buttons. Those corners are called inner corners, and inner-corner component tokens always map
to individual corner-value tokens.

## Customizing

There are two levels:

- Style level: change what a scale step means, for example make `medium` a different size. Every
  component mapped to that step changes unless overridden, so cards and small FABs both move.
- Component level: remap one component to a different step. Buttons default to `full`; remap them to
  `small` or `medium` for a less rounded product.

The shape family can change from rounded to cut (a straight line instead of a curve). Add extra
padding with cut corners, because a large cut clips more content than a rounded corner of the same
size.

Don't apply large or full corners to information-dense components such as cards. They clip content and
images.

Products should use the M3 shape styles consistently. Material encourages customization for hero
moments and custom components.

## Optical roundness for nesting

When nesting rounded objects, never reuse the container's radius on the child. It looks unbalanced.
Make the radii proportional:

```
inner radius = outer radius - padding      48dp - 14dp = 34dp
```

## Platform notes

- Compose: `MaterialTheme.shapes` holds the scale (`extraSmall` through `extraLarge` and the
  Expressive additions in recent releases). `MaterialShapes` and the `toShape()` extension expose the
  shape library and are still experimental in Compose Material 3 1.5.0.
- Views: `?attr/shapeAppearanceCornerSmall` and related attributes, and `ShapeAppearanceModel` for
  custom shapes.
- Web: Material Web reads `--md-sys-shape-corner-*` custom properties. There is no shape library or
  morph. Approximate a corner change with a `border-radius` transition when the motion is not
  interruptible, as described in `motion.md`.

```css
:root {
  --md-sys-shape-corner-none: 0px;
  --md-sys-shape-corner-extra-small: 4px;
  --md-sys-shape-corner-small: 8px;
  --md-sys-shape-corner-medium: 12px;
  --md-sys-shape-corner-large: 16px;
  --md-sys-shape-corner-large-increased: 20px;
  --md-sys-shape-corner-extra-large: 28px;
  --md-sys-shape-corner-extra-large-increased: 32px;
  --md-sys-shape-corner-extra-extra-large: 48px;
  --md-sys-shape-corner-full: 9999px;
}
```

Material Web's own token set may not define the Expressive steps (`large-increased`,
`extra-large-increased`, `extra-extra-large`). Define them in the product theme when a design uses
them.

## Don't

- Don't compromise clarity for visual design.
- Don't add a differently shaped element without a reason. Shapes without a clear reason add clutter.
- Don't apply unconventional shapes to text-heavy containers.
- Don't reuse the same corner radius on nested objects.
