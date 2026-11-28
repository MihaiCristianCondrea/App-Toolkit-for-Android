---
name: compose-layout-spacing
description: Author and review Jetpack Compose padding, spacing, gutters, sibling arrangements, and reusable content padding. Load for Row, Column, Box, list item, card, form, section, and screen spacing changes, including spacing-related interaction regressions.
---

# Compose layout spacing

For related layout concerns, see the [Compose layout map](../README.md).

## Rules

For connected lists, connected buttons, positional group shapes, grids or bento compositions, load `compose-connected-layouts` and apply `ConnectedNativeFirst-1` and the relevant geometry rules.

For list-shaped children in a `Column`, `LazyColumn`, or equivalent collection, load `compose-interactive-surfaces` and apply its mandatory `ListItemFirst-1` and `ListItemApi-1` rules before implementing layout or spacing, even for non-interactive entries. Structural content is exempt.

- **SpacingOwnership-1:** Classify each space as parent gutter, sibling gap, component content inset, deliberate caller-supplied external padding, or system inset. Assign one owner to each intended space. Do not treat `Modifier.padding` as an automatic CSS-like margin or internal padding; its position determines its effect.
- **SiblingGap-1:** For repeated uniform gaps in a Row, Column or lazy list, prefer the parent's `Arrangement.spacedBy`. Do not add identical top/bottom padding to each sibling to manufacture a gap. Use `Spacer` or a parent-selected wrapper for intentional nonuniform, structural or section-specific spacing. Avoid empty placeholder items and accidental first/last gaps.
- **ContentPadding-1:** Components own insets between their surface and content. Use a component's existing `contentPadding` parameter where available; otherwise apply padding inside the intended surface. Expose `PaddingValues` only when callers genuinely need that customization. Do not add external margins to reusable leaf components by default.
- **Gutter-1:** Parent padding is appropriate when every child surface must be inset. When a full-width interactive row has inset text, leave the row surface full width and inset its content instead. Mixed-width content may need parent-controlled per-item gutters, not a single horizontal screen padding.
- **ModifierContract-1:** Reusable UI elements accept `modifier: Modifier = Modifier` as their first optional parameter and apply it once to their outermost UI-emitting element. Append implementation modifiers to it. Do not discard it, apply it to multiple nodes, or silently prepend internal padding before it.
- **SpacingTokens-1:** Prefer native Material and Compose defaults (`MaterialTheme.shapes.*`, `ButtonDefaults.IconSize`, `ButtonDefaults.IconSpacing`, `SwitchDefaults.IconSize`, `DividerDefaults.Thickness`, `ListItemDefaults.SegmentedGap`). Use explicit `dp` dimensions when no appropriate native default exists. Introduce a custom dimension abstraction only when it represents a meaningful, reusable contract. Do not invent a universal 4/8/16 dp grid, global numeric alias objects, or fixed-dp `Spacer` wrapper composables, and do not force unrelated gaps to share a token merely because their current values match. Preserve meaningful spacing hierarchy and directional start/end behavior.
- **SpacingInteraction-1:** If spacing intersects a clickable or colored surface, load `compose-interactive-surfaces`. If it intersects a lazy viewport or window inset, load `compose-scroll-insets`.

## Procedure

Identify the relevant surface and its bounds, then sketch outer gutter -> surface -> content inset -> content -> sibling gap. Choose the narrowest owner that knows the relationship. Verify both first/last items and conditional children. Inspect the component's internal defaults before adding another padding modifier.

## Review

- Are repeated gaps parent-owned unless intentional exceptions are documented?
- Is each visual gap counted once? Are dividers and headers aligned intentionally?
- Does the caller's modifier affect the intended component root?
- Are full-width rows still full width, and are standalone cards inset only where intended?
- Do RTL, larger fonts, missing optional content and different screen widths retain the intended relationships?

Read [examples](references/examples.md) for comparison cases and common traps.

Official references: [Compose modifiers](https://developer.android.com/develop/ui/compose/modifiers), [Basic layouts](https://developer.android.com/codelabs/jetpack-compose-layouts), [Lazy lists](https://developer.android.com/develop/ui/compose/lists).
