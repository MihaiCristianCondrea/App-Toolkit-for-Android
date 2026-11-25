---
name: compose-layout-constraints
description: Reason about Compose measurement policies, parent constraints, Row/Column/Box arrangement and alignment, weights, sizing, modifier order and adaptive layout. Load when children are misaligned, unexpectedly stretched, clipped, too tall or incorrectly sized.
---

# Compose layout constraints

For related layout concerns, see the [Compose layout map](../README.md).

## Rules

For connected lists, connected buttons, positional group shapes, grids or bento compositions, load `compose-connected-layouts` and apply `ConnectedNativeFirst-1` and the relevant geometry rules.

For list-shaped children in a `Column`, `LazyColumn`, or equivalent collection, load `compose-interactive-surfaces` and apply its mandatory `ListItemFirst-1` and `ListItemApi-1` rules before implementing layout or spacing, even for non-interactive entries. Structural content is exempt.

- **Constraints-1:** Trace incoming minimum/maximum width and height. Constraints travel down, measured sizes return up, and placement follows. Do not infer actual size from a modifier alone; its position and the parent's constraints matter.
- **Axis-1:** In Column the main axis is vertical and cross axis horizontal; in Row the reverse. Arrangement distributes children on the main axis; alignment positions individual children on the cross axis. Use `Box` for overlapping content or a single child's two-dimensional alignment rather than nesting Row/Column only for centering.
- **Arrangement-1:** `SpaceBetween`, `SpaceAround`, `SpaceEvenly`, `Center` and `Bottom` need available free main-axis space to differ visibly from default packing. Do not add `fillMaxHeight`/`fillMaxSize` solely to make an arrangement work unless the intended layout should occupy that space. Fixed sibling gaps use `spacedBy`.
- **Weight-1:** Use `weight` on direct Row/Column children to allocate remaining main-axis space in proportion to weights. Measure fixed content first conceptually. Default `fill = true` fills the allocated dimension; `false` permits smaller measured content and unused allocation is not redistributed. Do not rely on weights for distributing an unbounded scroll axis.
- **Sizing-1:** Use content-driven sizing by default. Add `widthIn`, `heightIn`, `sizeIn`, `fillMaxWidth`, or fixed size only to express an actual requirement. Avoid fixed text-row heights that clip large fonts. `requiredSize` can override incoming constraints and lead to overflow; do not use it to silence layout problems.
- **ModifierOrder-1:** Reason from outer to inner. Padding reduces constraints passed inward; size and fill modifiers also affect the downstream constraints. An offset changes placement, not measured allocation, so do not substitute it for layout spacing.
- **Direction-1:** Prefer `Alignment.Start`/`End`, directional padding and relative placement unless physical left/right is intentional. A child `align` overrides the parent cross-axis default; baseline alignment is a distinct Row relationship.
- **Measurement-1:** Use standard layouts and their arrangement/alignment parameters first. Introduce custom Layout or intrinsic measurement only when a demonstrable requirement cannot be expressed simply with standard layout APIs. Do not implement private AndroidX policy classes as application code or assume their internals are stable.
- **Adaptive-1:** Choose layouts using available parent/window constraints and project adaptive APIs, not hardcoded device widths. Verify compact, expanded, RTL and large-font layouts; preserve meaningful content and touch targets.

## Measurement model

`Column` delegates to a `MeasurePolicy`. In AndroidX implementations using `columnMeasurePolicy`, the default Top/Start configuration reuses a shared `DefaultColumnMeasurePolicy`, while other configurations may be remembered. Policy reuse shares behavior, not child sizes or coordinates. A typical implementation measures unweighted children, allocates bounded remaining space to weighted children, computes its own size, and determines main-axis arrangement and cross-axis alignment. These details explain behavior but are not public API contracts.

## Review

Trace the affected axis and parent constraints; inspect every size/fill/weight/padding modifier in order. Check if a wrapper introduces unwanted width/height or duplicate measurement. Validate small and large available sizes, long text and scroll-axis constraints. Use the project's existing previews and tests. Report what was not actually rendered.

Read [examples](references/examples.md). Official references: [Layout basics](https://developer.android.com/develop/ui/compose/layouts/basics), [Constraints and modifier order](https://developer.android.com/develop/ui/compose/layouts/constraints-modifiers), [Modifiers](https://developer.android.com/develop/ui/compose/modifiers), [Custom layouts](https://developer.android.com/develop/ui/compose/layouts/custom).
