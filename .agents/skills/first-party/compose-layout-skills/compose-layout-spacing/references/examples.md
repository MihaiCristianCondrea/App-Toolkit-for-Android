# Spacing examples

Illustrative snippets assume Compose foundation/layout and Material 3 imports. Replace example dimensions and components with project equivalents.

## Uniform sibling spacing

Avoid manufacturing a 16 dp gap with two independent 8 dp paddings. This also creates outer gaps and changes each child's bounds.

```kotlin
// Avoid for uniform sibling spacing.
Column {
    SummaryCard(modifier = Modifier.padding(vertical = 8.dp))
    StorageCard(modifier = Modifier.padding(vertical = 8.dp))
}

// Prefer when the desired gap is 16 dp and outer edges are flush.
Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
    SummaryCard()
    StorageCard()
}
```

If an optional child is absent, arrangement spaces the actual emitted children. A child emitting multiple direct layout nodes can affect that count; inspect the layout tree.

## Screen gutters versus content insets

```kotlin
// Inset cards intentionally.
Column(
    modifier = Modifier.padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    SummaryCard()
    StorageCard()
}

// Full-width interactive settings rows, with inset text inside each row.
Column(modifier = Modifier.fillMaxWidth()) {
    SettingsItem(title = "General", onClick = onGeneral)
    SettingsItem(title = "Appearance", onClick = onAppearance)
}
```

The second example assumes SettingsItem owns its content padding. An ancestor may still constrain the available width. Full width is relative to the current parent, not necessarily the device.

## Mixed content: inset heading, edge-to-edge items

```kotlin
Column(modifier = Modifier.fillMaxWidth()) {
    Text(
        text = sectionTitle,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    )
    SettingsItem(title = firstTitle, onClick = onFirst)
    HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
    SettingsItem(title = secondTitle, onClick = onSecond)
}
```

The section layout selects a nonuniform gap and divider inset. Do not place an arbitrary `spacedBy` around every element when dividers and rows must touch.

## Component content padding

```kotlin
@Composable
fun InfoPanel(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.medium) {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
```

The caller can choose an external gutter through `modifier`; `contentPadding` changes the inside of the surface. Only expose it if needed by real callers.

## Nonuniform gaps and text baselines

Use `Spacer(Modifier.height(...))` for a deliberate larger gap between groups or a slot with a specific relationship. Do not forbid Spacer categorically. For typography defined by baselines, use `paddingFromBaseline` when appropriate instead of guessing text box padding. Prefer existing typography line-height settings for line spacing.
