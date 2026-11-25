---
name: compose-scroll-insets
description: Author and review Compose lazy and scrollable layouts, viewport versus content padding, Scaffold innerPadding, system insets, IME, and edge-to-edge content. Load for screens, lists, scrolling cards, and system-bar spacing.
---

# Compose scroll and inset ownership

For related layout concerns, see the [Compose layout map](../README.md).

## Rules

For connected lists, connected buttons, positional group shapes, grids or bento compositions, load `compose-connected-layouts` and apply `ConnectedNativeFirst-1` and the relevant geometry rules.

For list-shaped children in a `Column`, `LazyColumn`, or equivalent collection, load `compose-interactive-surfaces` and apply its mandatory `ListItemFirst-1` and `ListItemApi-1` rules before implementing layout or spacing, even for non-interactive entries. Structural content is exempt.

- **Viewport-1:** Distinguish padding outside a scroll viewport from padding inside scroll content. Use outer modifier padding when the viewport itself should be inset. Use LazyColumn/LazyRow `contentPadding` for content that should scroll through the viewport with leading/trailing content space.
- **LazySpacing-1:** Use lazy layout `verticalArrangement`/`horizontalArrangement` for uniform sibling gaps and `contentPadding` for list edge spacing. Horizontal content padding in a LazyColumn reduces the width available to its item surfaces; do not use it if rows must span the entire viewport width. Distinguish horizontal gutters from system-safe horizontal insets.
- **InsetOwner-1:** Trace the shell, Scaffold, Material components and child layout before applying system inset padding. Apply and consume each inset at the intended boundary; avoid duplicate top/bottom safe padding. Do not replace existing inset ownership without examining the host.
- **Scaffold-1:** Apply the actual `innerPadding` returned by Scaffold where appropriate, either as layout padding or as lazy content padding. Consume it at that boundary via `consumeWindowInsets(innerPadding)` so descendants do not reapply those insets. Do not assume Scaffold automatically pads its content.
- **Scroll-1:** Use `Column` for small bounded content, `verticalScroll` for suitable finite content and lazy lists for large or dynamic collections. Avoid nesting same-axis scroll containers with unbounded child constraints. Do not use weight to fill infinite remaining scroll space.
- **SafeContent-1:** Edge-to-edge window rendering does not imply tappable elements should overlap bars, cutouts or the keyboard. Separate background coverage, scroll viewport, content gutters and interactive safe regions. Verify status/navigation bars, gesture and three-button modes, cutouts, IME and landscape.
- **InsetScope-1:** Use inset-aware modifiers and existing Material inset handling where appropriate; add IME handling only at the correct owner for the screen. Check existing official edge-to-edge project guidance before changing Activity flags or app-wide policy.

## Review

Trace padding from window -> app shell -> viewport -> list content -> item surface -> item content. Check first/last visible items, scroll under app bars, resting and scrolling clipping, keyboard open/closed, and whether inset consumption prevents duplication. Report device scenarios not exercised.

Read [examples](references/examples.md). Official references: [Lazy lists](https://developer.android.com/develop/ui/compose/lists), [Material 3 insets](https://developer.android.com/develop/ui/compose/system/material-insets), [Set up window insets](https://developer.android.com/develop/ui/compose/system/insets-ui), [Scaffold](https://developer.android.com/reference/kotlin/androidx/compose/material3/Scaffold.composable).
