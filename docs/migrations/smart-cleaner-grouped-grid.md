# Smart Cleaner → `GroupedGrid` (App Toolkit `3.0.0-pre13`)

**Status:** TODO, not started. Do this in Smart Cleaner when it moves to App Toolkit
`3.0.0-pre13`.

Smart Cleaner grew the grouped category grid, the block of rounded cells the storage breakdown and
the WhatsApp summary are read in. The toolkit now owns that layout as
`core.ui.views.grid.GroupedGrid`, so the app can delete its copy instead of maintaining a second
one.

## What the toolkit provides

| Smart Cleaner today | Toolkit `3.0.0-pre13` |
| --- | --- |
| `clean.ui.views.grids.GroupedGridLayout` | `GroupedGrid` |
| `clean.ui.views.cards.GridCardItem` | `GroupedGridItem`, rendered by `GroupedGrid` |
| `clean.ui.models.GridCardModel` | `GroupedGridItem` |
| `clean.ui.theme.GroupedGridStyle` | `GroupedGridDefaults` and `GroupedGridColors` |
| `clean.ui.views.ads.GridNativeAdCard` | the `adUnitId` parameter of `GroupedGrid` |
| `feature.systemmonitor.ui.views.StorageBreakdownGrid` | a `GroupedGrid` call built from the same map |
| `feature.whatsappcleaner.summary.ui.views.DirectoryGrid` and its `DirectoryCard` | a `GroupedGrid` call built from the same items |

## What changes when the app adopts it

- **Corners are cut per cell, not clipped per group.** The grid used to clip the whole column with
  `gridClipShape` and give every card a 2dp corner. `GroupedGrid` gives each cell the corners its
  position calls for: `GroupedGridDefaults.OuterRadius` at the outside of the block,
  `GroupedGridDefaults.InnerRadius` at every seam. A grid of one cell rounds all four of its
  corners, which the clip could not express.
- **The ad row is part of the grid.** `GroupedGrid` places one full-width native ad row directly
  under the first row of cells, from two cells up, and it is cut with the same radii as the cells.
  The `MIN_ITEMS_FOR_NATIVE_AD = 4` gate in `DirectoryGrid` and the `index == 2` insertion in
  `StorageBreakdownGrid` both become the toolkit's single rule; delete them and pass `adUnitId`.
  Their tests go with them, because `groupedGridRows` is tested in the toolkit.
- **An ad that never loads no longer leaves a seam.** The row collapses and the cells above round
  off as if no ad had been asked for.
- **Badges take any shape.** `GroupedGridItem.iconShape` accepts `MaterialShapes.<name>.toShape()`,
  so each storage bucket can carry its own silhouette instead of the shared rounded square. Leave it
  unset to get the grid's shape.
- **Cell height is a size class.** `GroupedGridMeasurements` picks the height and everything that
  scales with it, the way `ButtonMeasurements` does for buttons. `Medium`, the default, is the size
  the cleaner's grids are drawn at today, so adopting it is not a visual change.
- **Icons go through `ToolkitIcon`.** `GridCardModel`'s three icon fields (`iconVector`,
  `iconPainter`, `iconRes`) collapse into one `ToolkitIcon`, which also accepts animated vectors and
  bundled Lottie.

## Steps

1. Raise the App Toolkit dependency to `3.0.0-pre13`.
2. Replace `StorageBreakdownGrid` and `DirectoryGrid` with `GroupedGrid` calls, mapping each
   category or directory to a `GroupedGridItem` and passing the native ad unit as `adUnitId`.
3. Delete `GroupedGridLayout`, `GridCardItem`, `GridCardModel`, `GroupedGridStyle`,
   `GridNativeAdCard`, `DirectoryCard`, and the ad-placement tests those files carry.
4. Check `SystemMonitorShimmer`, which draws placeholders shaped like the old cards, against
   `GroupedGridMeasurements.Medium` and `GroupedGridDefaults` so the shimmer still matches the
   loaded grid.
5. Keep the storage-category filtering (`visibleStorageBreakdown`) in the app. It is a data rule
   about which buckets are worth showing, not a layout concern, and the toolkit renders whatever
   list it is handed.
