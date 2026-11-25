# Constraints and alignment examples

## Column arrangements

```kotlin
Column(
    modifier = Modifier.height(240.dp),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.Start
) {
    Header()
    Footer()
}
```

The explicit height is useful only if the parent permits it and the design calls for that height. A wrap-content Column with no extra height cannot distribute additional space. For ordinary content use `Arrangement.spacedBy` rather than arbitrarily forcing height.

For a 300 px tall Column with child heights 40, 60 and 50 px, `SpaceBetween` allocates 75 px per gap (assuming the specified sizes are actually measured). Y positions are 0, 115 and 250. For width 200 and child widths 100, 120 and 80, `CenterHorizontally` yields X positions 50, 40 and 60. Real measurement includes constraints, rounding, weights and other layout behavior.

## Weighted text and trailing action

```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Column(modifier = Modifier.weight(1f)) {
        Text(text = title)
        Text(text = description)
    }
    IconButton(onClick = onAction) {
        Icon(imageVector = actionIcon, contentDescription = actionLabel)
    }
}
```

Reserve space for the trailing action and let text wrap in the remaining space. Avoid giving the text column `fillMaxWidth()` before measuring an unweighted trailing action. Weight requires direct scope parent data and meaningful bounded space.

## Modifier order

```kotlin
// 100 dp requested outer size, then 16 dp content inset on each side.
Modifier.size(100.dp).padding(16.dp)

// 16 dp outer inset around a requested 100 dp inner size.
Modifier.padding(16.dp).size(100.dp)
```

With permissive incoming constraints, the first occupies 100 dp, the second 132 dp. Under tighter constraints these numbers may differ. The chain is not commutative.

## Alignment overrides and Box

```kotlin
Column(horizontalAlignment = Alignment.Start) {
    Text(text = title)
    Text(text = detail, modifier = Modifier.align(Alignment.End))
}

Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    CircularProgressIndicator()
}
```

`fillMaxSize` is intentional for a loading screen, not a universal default. For an overlay constrained to a Box use `matchParentSize` on a direct child if it must match the measured Box without contributing to its size.

## Minimum versus exact height

```kotlin
// Fragile when title or subtitle wraps.
Modifier.height(56.dp)

// Permits growth within parent constraints.
Modifier.heightIn(min = 56.dp)
```

Minima do not override a smaller maximum imposed by the parent. A fixed-height visual element may still be appropriate, for example a decorative thumbnail. Verify that text and interactive content are not clipped.
