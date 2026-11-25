# Connected layout examples

Illustrative Kotlin snippets target the Material 3 1.5.0 API. Verify resolved versions, imports, opt-ins and project tokens; these snippets are not compiled standalone. Domain functions and data below are illustrative. Do not copy generic names or spacing values into a project when an equivalent exists.

## 1. Native segmented actions

Avoid: reproducing first/middle/last/single shape selection with per-item external `clip`, a rectangular clickable `Card`, a manually laid out icon/text row and separate vertical padding. These recreate native list functionality.

Prefer the native shape factory and parent gap:

```kotlin
Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
) {
    visibleActions.forEachIndexed { index, action ->
        key(action.id) {
            SegmentedListItem(
                onClick = action.onClick,
                shapes = ListItemDefaults.segmentedShapes(
                    index = index,
                    count = visibleActions.size,
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = action.enabled,
                leadingContent = { ActionIcon(action) },
                supportingContent = { Text(action.description) },
            ) {
                Text(action.title)
            }
        }
    }
}
```

For an empty group render nothing if the enclosing surface would otherwise leave padding. Compute `visibleActions` before iteration, excluding structural separators or grouping them separately. A header in its own surface is not a list segment. For lazy collections use a stable item key and a group-local index/count, not the index of the whole lazy list.

Customize the slots rather than rewriting the component. If a project needs its own shape states, evaluate `ListItemDefaults.shapes(...)` and the `defaultShapes` argument of `segmentedShapes`; check actual base and interaction shape resolution and do not assume arbitrary Shape types receive corner modifications. Native defaults remain the starting point.

## 2. Whole-row toggles and radio choices

For a single logical toggle, attach checked state to the row and make the trailing control passive:

```kotlin
SegmentedListItem(
    checked = checked,
    onCheckedChange = onCheckedChange,
    shapes = ListItemDefaults.segmentedShapes(index = index, count = count),
    enabled = enabled,
    trailingContent = {
        Switch(checked = checked, onCheckedChange = null, enabled = enabled)
    },
) {
    Text(title)
}
```

For a single-choice group use `Modifier.selectableGroup()` on the parent, the selected overload of `SegmentedListItem`, and a passive `RadioButton(selected = selected, onClick = null)` where appropriate. Preserve the current selection contract (including whether deselection is allowed). For a truly independent secondary switch, preserve a separate accessible action; use the click overload and a separately interactive switch or a suitable custom split surface if the native slots cannot maintain independent hit targets and divider geometry. Test keyboard/accessibility and callback counts. Do not conflate a two-action preference with a whole-row toggle.

## 3. Shape belongs to the surface

Avoid this when the card itself supports shape:

```kotlin
Card(
    modifier = modifier.clip(resolvedShape),
    shape = RectangleShape,
    onClick = onClick,
) { TileContent() }
```

Prefer, for a genuinely custom non-list tile:

```kotlin
Card(
    modifier = modifier,
    shape = resolvedShape,
    onClick = onClick,
) { TileContent() }
```

Content padding belongs inside the card. External gutters belong to the caller/group. If the item is an ordinary segmented list entry use `SegmentedListItem` instead of either example. A manual primitive may still need its own clip; the rule is not a blanket ban on `clip`.

## 4. Shared dimensions in a connected grid

For simple wrapping cells with content-driven heights, evaluate:

```kotlin
FlowRow(
    modifier = modifier.fillMaxWidth(),
    maxItemsInEachRow = 2,
    horizontalArrangement = Arrangement.spacedBy(gap),
    verticalArrangement = Arrangement.spacedBy(gap),
) {
    tiles.forEach { tile ->
        key(tile.id) {
            Card(
                modifier = Modifier.weight(1f).fillMaxRowHeight(),
                shape = tile.shape,
                onClick = tile.onClick,
            ) {
                TileContent(tile)
            }
        }
    }
}
```

This solves shared row height for compatible flow content, not arbitrary span placement. `tile.shape` must come from a plan that matches actual wrapping; if wrap may change with constraints, don't derive it from a fixed item index alone. A short last row with weighted items may expand to fill available width; verify this is desired. For explicit planned rows, a bounded `Row(Modifier.height(IntrinsicSize.Min))` and child `fillMaxHeight()` can work when all children support intrinsic measurement. Never apply it indiscriminately to ads/AndroidView, lazy or subcomposed content. Test actual equal bounds, not just row height.

## 5. Bento without a new layout engine

A known three-tile arrangement can use ordinary layouts with a deliberately bounded group height. The caller must supply an appropriate height and adapt to content or width changes. For example:

```kotlin
Row(
    modifier = modifier.fillMaxWidth().height(groupHeight),
    horizontalArrangement = Arrangement.spacedBy(gap),
) {
    PrimaryWidget(
        modifier = Modifier.weight(1f).fillMaxHeight(),
        shape = primaryShape,
    )
    Column(
        modifier = Modifier.weight(1f).fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        SecondaryWidget(modifier = Modifier.weight(1f).fillMaxWidth(), shape = topShape)
        TertiaryWidget(modifier = Modifier.weight(1f).fillMaxWidth(), shape = bottomShape)
    }
}
```

Use this only when content fits the bounded height; choose a one-column/content-driven alternative when text or available width requires it. All three shapes are derived from the actual arrangement. Use `GridItemSpan` in a lazy vertical grid for cross-axis spans (including `maxLineSpan` for full-width items), not as a row-span API. Use a staggered grid for independent variable heights, not connected shared seams. For a complex two-axis span requirement, specify the occupancy/track plan, deterministic rounding of pixel gaps, constraints, reading order and shape resolution before introducing a custom `Layout`. A simple group is not justification for a general bento framework.

## 6. Geometry test cases

- Empty; one tile (all four outer corners); one row; one column; full rows; short last row; mixed spans; nonrectangular perimeter if supported.
- Stable ids under reorder, filtering, insertion/removal and resizing; conditional and animated items; collapsed ad versus visible ad; no extra gap around invisible content.
- Same row/shared seam heights under different content sizes, long localized labels and large text; bounded versus unbounded constraints.
- Directional corners under RTL; correct visual shape/ripple under real press; enabled/disabled/checked/selected state, focus and separate trailing actions.
- Snapshot/preview/device validation and test reports must distinguish actual execution from static reasoning.

References: [segmented list API](https://developer.android.com/reference/kotlin/androidx/compose/material3/SegmentedListItem.composable), [list defaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/ListItemDefaults), [flow layouts](https://developer.android.com/develop/ui/compose/layouts/flow), [lazy grids](https://developer.android.com/develop/ui/compose/lists), [custom layouts](https://developer.android.com/develop/ui/compose/layouts/custom).
