# Visual reference library

`visuals/` (at the skill root) holds 2,587 official images from m3.material.io: component anatomy
diagrams with lettered or numbered callouts, size and measurement specs, state grids, placement
diagrams, do and don't pairs, and full example screens. Use them when a written spec is ambiguous or
when you need to see how something is composed.

The images are unmodified Google assets. Do not resize, recompress, or rename them; see `NOTICE.md`.

## Finding an image

Every image has one row in `visuals/INDEX.tsv`:

```
<relative path>\t<official caption>
```

The caption is Material's own caption or alt text, so search the index for what you want to see, not
for a filename. Run these from the skill root:

```bash
grep -i "container" visuals/INDEX.tsv                     # anatomy diagrams list part names
grep -i "don't" visuals/INDEX.tsv                         # documented anti-patterns
grep -i "measurement" visuals/INDEX.tsv                   # dp specs
grep -iE "placement|position|alignment" visuals/INDEX.tsv
grep -i "^components_toolbars/" visuals/INDEX.tsv         # everything for one component
```

PowerShell equivalent: `Select-String -Path visuals/INDEX.tsv -Pattern "^components_toolbars/"`.

Filenames are derived from captions, so globbing also works: `visuals/components_buttons/*shape*`.

## Folder layout

One folder per M3 page, named after its route with `/` replaced by `_`:

| Folder prefix | Contents |
|---|---|
| `building-with-m3-expressive/` | The M3 Expressive announcement visuals: the seven tactics, before and after pairs. |
| `m3-expressive-motion-theming/` | Animated spring examples (spatial and effects). |
| `components_*` | Component pages: anatomy, sizes, states, placement, do and don't. |
| `styles_*` | Color, typography, shape, motion, elevation, spacing, icons. |
| `foundations_layout_*` | Breakpoints, canonical layouts, scaffold, grids, RTL. |
| `foundations_interaction_*` | States, gestures, inputs, selection. |
| `foundations_xr_*` | XR design and components. |
| `foundations_content-design_*` | Writing, notifications, global writing. |
| `develop_*` | Platform pages. |

## Most useful image sets

- Full example screens: `foundations_layout_canonical-examples/` (list-detail, feed, supporting
  pane) and `foundations_layout_layout-overview/` (regions, panes, arrangements per window size).
  These show correct region colors, navigation choice, and pane structure at each size.
- Component anatomy: callout diagrams in each `components_*` folder, more often lettered (`A.`,
  `B.`) than numbered. No caption uses the word "anatomy", so search part names such as `container`
  or `label`. Callouts map to the anatomy lists in `components/*.md`.
- Measurement specs: search `measurement`, `size`, `padding`, or `height`.
- State grids: search `enabled` or `hovered`. Each shows enabled, disabled, hovered, focused, and
  pressed (and dragged or selected where relevant) side by side.
- Do and don't pairs: search `don't` and `avoid`. They are the fastest way to check a design against
  the guidelines.
- Shape library: `styles_shape/` (the 35 shapes, the corner radius scale, optical roundness).
- Type scale: `styles_typography/` (the scale, emphasized versus baseline, variable axes, editorial
  treatments).
- Color roles: `styles_color_roles/` and `styles_color_system/` (role swatches, pairings, contrast
  levels, correct and incorrect mappings).

## Using them well

- Captions are Material's own words. Quote them instead of describing what you think you see.
- Numbered callouts mean nothing without the matching anatomy list. Read the component reference
  alongside the image.
- These are PNG, JPG, and GIF renders of the guidelines, not source assets. Do not treat pixel
  measurements as spec. The numbers in `tokens.md`, `component-tokens.md`, and `components/*.md` are
  authoritative.
- The images were captured from m3.material.io in July 2026. If the user's platform version differs,
  the written specs age better than the renders.
- Images can be viewed directly with an image-capable file reader. Load one or two targeted images,
  not whole folders.
