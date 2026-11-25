# Compose layout skills

Use these skills to decide where Compose layout, spacing, and interaction belong. They document
this project's conventions alongside official Android guidance.

## Routing

| Skill | Load when |
|---|---|
| `compose-layout-spacing` | Choosing padding, sibling gaps, screen gutters, component content padding, or spacing tokens. |
| `compose-layout-constraints` | Working with Row, Column, Box, alignment, weights, size modifiers, adaptive layouts, or custom measurement. |
| `compose-interactive-surfaces` | Creating or reviewing any list-shaped entry (static or interactive), clickable cards, touch targets, ripples, shape clipping, selectable or toggleable elements. |
| `compose-connected-layouts` | Segmented lists, connected buttons, positional shapes, grids, bento layouts and native-equivalent cleanup. |
| `compose-scroll-insets` | Working with Scaffold, LazyColumn/LazyRow, scrollable content, system bars, IME or edge-to-edge. |

Load multiple skills when the task crosses boundaries. For example, a full-width clickable settings list uses spacing, interactive surfaces and scroll/insets. Each skill owns its rules; references provide implementation examples.

## Shared workflow

1. Inspect the actual screen, component API, existing design tokens, Compose/Material versions, and parent constraints. Preserve intentional visual design and behavior.
2. Identify the owner of every gap, content inset, interactive surface and system inset before changing modifiers. Trace modifier order from outermost to innermost.
3. Inspect every affected vertical collection for list-shaped entries. Apply mandatory `ListItemFirst-1` and `ListItemApi-1` from `compose-interactive-surfaces`: reuse an appropriate project item or use the compatible Material ListItem/SegmentedListItem. Customize slots before considering a manual layout. Explain any concrete exception in the review summary. Do not introduce wrappers, tokens, or custom layouts without a concrete need.
4. For connected collections, apply `ConnectedNativeFirst-1`, `SegmentedFirst-1` and `MigrationCleanup-1` in `compose-connected-layouts`. Use native equivalents before custom shape or measurement code; preserve necessary custom geometry and follow the explicitly approved release compatibility policy. For an authorized breaking release, delete obsolete public APIs after migrating consumers and documenting replacements.
5. Read the selected skill's reference examples for the relevant case. Adapt identifiers and dimensions to the project; sample values are illustrative, not mandatory design tokens.
6. Review modified call sites, compile against the project's versions and validate representative layouts and interactions. Report unverified visual or device behavior explicitly.

## Review gate

New or modified list-shaped entries implemented manually without a concrete exception are review findings. Apply the rule to the affected scope; do not mass-refactor unrelated existing layouts. Preserve intentional design, accessible interaction and supported API versions.

Connected layout implementations that duplicate an available native API without a documented requirement are findings within the scoped migration.

## Discovery and use

The five skills are registered in the [root manifest](../../../skills.json). Select only the skills that
cover the affected behavior. Read each relevant skill once, even when another skill refers back to it.
Use the Material UI skill for design specifications and follow the project's resolved version and
compatibility policy for implementation.

## Sources

Each skill links to official Android documentation and example patterns. Rule IDs identify local
conventions. Verify the example APIs against the dependencies of the project you are changing.
