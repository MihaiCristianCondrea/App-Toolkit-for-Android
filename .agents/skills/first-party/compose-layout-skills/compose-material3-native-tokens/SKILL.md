---
name: compose-material3-native-tokens
description: Review and implement Jetpack Compose Material 3 styling with public AndroidX component defaults and semantic theme roles. Use when choosing or auditing colors, shapes, typography, motion, spacing, dimensions, borders, elevation, or insets, especially when replacing redundant hardcoded values, defaults, or token wrappers.
---

# Native Material 3 tokens

## Mandatory rules

- **NativeTokens-1 (Default first):** Inspect the actual Material component and its parameter defaults before adding or retaining styling. Omit redundant overrides. If a standard native component replaces a custom implementation without losing required behavior, prefer the native component.
- **NativeTokens-2 (Semantic ownership):** Use the matching component's public `*Defaults` API when explicit configuration is necessary. Match component type, variant, size, state, and intended purpose, not numeric equality. Never borrow an unrelated component's token as generic spacing.
- **NativeTokens-3 (Theme):** Use `MaterialTheme.colorScheme`, `shapes`, `typography`, and `motionScheme` for custom content where the semantic role fits. Prefer inherited content color and text style where appropriate. Preserve correct foreground/background pairing and state-aware component defaults.
- **NativeTokens-4 (No duplicate systems):** Do not introduce or retain app-level aliases, spacing objects, composition locals, custom theme extensions, copied internal tokens, or helper functions that only reproduce public library behavior. Remove obsolete definitions and migrate their consumers when in scope. Do not remove abstractions that supply independent domain behavior or an intentional design requirement.
- **NativeTokens-5 (Version gate):** Inspect the resolved dependency for the target module and platform. Verify the exact public symbol, overload, annotations, and deprecation status in the matching AndroidX source or IDE. Do not upgrade dependencies, add broad opt-ins, import internal tokens, or use reflection merely to obtain a default.
- **NativeTokens-6 (Fallback):** When no semantically equivalent public token exists, keep an appropriate explicit value. For ordinary layout spacing prefer straightforward `Dp`, `PaddingValues`, and `Arrangement.spacedBy`; a suggested local scale is 4, 8, 12, 16, 24, 32, 48 dp, not a MaterialTheme API. Preserve intentional zero, asymmetric spacing, adaptive layout, geometry and brand-specific values.
- **NativeTokens-7 (States and accessibility):** Preserve selected, checked, pressed, disabled, focused, dragged and error states, touch targets, semantics, RTL, dynamic color, dark theme, font scaling, and inset ownership. Do not replace component color/elevation/state factories with a single static value. Avoid double padding or inset consumption.
- **NativeTokens-8 (Review enforcement):** A proven redundant custom value in the affected scope is a required fix, not an optional recommendation. If equivalence or design intent is uncertain, investigate; keep and explain the exception rather than mechanically substitute.

## Procedure

1. Read the target module's resolved dependency and inspect native component defaults. Use the [public API catalog](references/public-api-catalog.md) to find candidates. Confirm availability in the resolved version.
2. Find hardcoded `dp`, `sp`, `Color`, `RoundedCornerShape`, `BorderStroke`, `TextStyle`, custom motion specs, inset approximations and custom default/token wrappers in the affected UI.
3. Classify each candidate: redundant override; matching public component default; semantic theme role; genuine custom requirement; unavailable API.
4. Apply fixes in that order. Remove dead imports, constants, helpers and wrappers only after all in-scope consumers are migrated. Keep public compatibility constraints unless a breaking change is authorized.
5. Compile affected modules and run relevant tests. Verify light/dark, dynamic colors, state changes, accessibility and adaptive layout where affected. Report substitutions, removed duplication, retained intentional values, version blockers, and unperformed validation.

## Examples

- Segmented list group: `Arrangement.spacedBy(ListItemDefaults.SegmentedGap)` and `ListItemDefaults.segmentedShapes(index, count)` rather than duplicating gaps and positional shapes.
- Icon in a standard button: `ButtonDefaults.IconSize` and `ButtonDefaults.IconSpacing`; for size-dependent expressive buttons verify `iconSizeFor`, `iconSpacingFor`, `contentPaddingFor`, `shapesFor`, and `textStyleFor` for the same container height.
- Standard card: omit shape, colors and elevation overrides. Use `CardDefaults` only if explicit configuration is necessary. For custom surfaces select theme roles by purpose.
- Unrelated screen sections: `Arrangement.spacedBy(16.dp)` may be correct. Do not substitute `ButtonDefaults.IconSpacing` or a list item gap just to avoid a literal.
- Preserve brand colors, data visualization palettes, media crops, bespoke artwork and application-specific visual semantics when the Material role is not equivalent.
