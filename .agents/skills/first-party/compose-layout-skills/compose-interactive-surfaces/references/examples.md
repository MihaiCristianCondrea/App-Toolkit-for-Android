# Interactive surface examples

## Mandatory list-item selection

### Avoid reconstructing a compatible item

```kotlin
// Noncompliant for a conventional settings row: duplicates Material layout.
Column {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Notifications, contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text("Notifications")
            Text("Manage reminders")
        }
    }
}
```

```kotlin
// Compatible legacy Material 3 overload; prefer the native interactive overload
// below when the project's resolved version supports it.
Column {
    ListItem(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        headlineContent = { Text("Notifications") },
        supportingContent = { Text("Manage reminders") },
        leadingContent = {
            Icon(Icons.Default.Notifications, contentDescription = null)
        }
    )
}
```

```kotlin
// Newer Material 3 interactive overload. Check version/opt-in requirements.
Column {
    ListItem(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        leadingContent = {
            Icon(Icons.Default.Notifications, contentDescription = null)
        },
        supportingContent = { Text("Manage reminders") },
        contentPadding = ListItemDefaults.ContentPadding
    ) {
        Text("Notifications")
    }
}
```

### Custom content is not an exception

```kotlin
// Newer interactive API. Custom slots are normal Compose subtrees.
ListItem(
    onClick = onClick,
    leadingContent = { StorageCategoryIcon(category = category) },
    supportingContent = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LinearProgressIndicator(
                progress = { usedFraction },
                modifier = Modifier.fillMaxWidth()
            )
            Text(fileCountLabel)
        }
    },
    trailingContent = {
        Column(horizontalAlignment = Alignment.End) {
            Text(sizeLabel)
            Text(percentageLabel)
        }
    }
) {
    Text(categoryLabel)
}
```

Use the project's localized labels and existing components. Slot sizing and alignment remain subject to Material's measurement policy; test long text, narrow widths, and font scaling.

### Segmented groups

```kotlin
// Newer Material 3 API. The list must represent one visible segment group.
Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
    entries.forEachIndexed { index, entry ->
        SegmentedListItem(
            onClick = { onEntryClick(entry) },
            shapes = ListItemDefaults.segmentedShapes(index = index, count = entries.size)
        ) {
            Text(entry.title)
        }
    }
}
```

For LazyColumn, preserve the same item selection policy with `items` or `itemsIndexed`, and stable keys where appropriate. Do not use a global index for shapes across unrelated groups. A continuous list need not have segmented shapes or gaps.

### Legitimate mixed Column

```kotlin
Column {
    SectionHeading(title = storageTitle)
    StorageUsageChart(usage = usage)
    ListItem(
        headlineContent = { Text(downloadsTitle) },
        supportingContent = { Text(downloadsSizeLabel) }
    )
    HorizontalDivider()
    Button(onClick = onManageStorage) { Text(manageStorageLabel) }
}
```

A heading, chart, divider and button are not list entries. Do not wrap them in ListItem. Custom layouts are also valid when a concrete requirement exceeds the slots (for example an edge-to-edge image above the entire content, or independently positioned content that cannot fit the available slot model). Preserve an existing compatible design-system wrapper rather than duplicating it.

## Padding order

```kotlin
// Wrong IF the intended ripple must cover the full row including the inset.
Modifier
    .fillMaxWidth()
    .padding(16.dp)
    .clickable(onClick = onClick)

// Correct for a full-width rectangular row with inset content.
Modifier
    .fillMaxWidth()
    .clickable(onClick = onClick)
    .padding(16.dp)
```

Both are legitimate for different surface boundaries. The former leaves padding outside the indication; the latter includes padding in its bounds. Small touch targets may be expanded at the input layer independently.

## Manual full-width row: fallback only

```kotlin
@Composable
fun SettingsItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 12.dp
    )
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null
        )
    }
}
```

Under ListItemFirst-1 this manual settings-row pattern is not a compliant replacement for a compatible ListItem. It is a geometry teaching example, usable only for a documented fallback that cannot be expressed with available Material or project components; integrate project disabled-state content colors and typography. The arrow is decorative. The actual minimum height depends on incoming constraints. No ancestor horizontal padding is assumed.

## Shaped clickable surface

```kotlin
@Composable
fun ActionPanel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}
```

Do not add another root clickable. This pattern delegates clipping and indication to Surface. For a manually implemented surface, the equivalent geometric ordering is:

```kotlin
Modifier
    .fillMaxWidth()
    .clip(shape)
    .background(containerColor)
    .clickable(onClick = onClick)
    .padding(contentPadding)
```

A caller may add external padding before this chain if inset cards are intended. Prefer passing shape explicitly if the group must select per-item shapes. Clipping must surround the indication; `background(color, shape)` by itself does not do that.

## Separate trailing control versus shared toggle

If a row and a switch perform the same toggle, let the row own the toggle and use `Switch(checked = checked, onCheckedChange = null)` as a visual child. Prefer the appropriate Material list item selection overload if it matches the semantics, or attach `toggleable` to a non-interactive ListItem and place the passive Switch in `trailingContent`. The manual Row below is an interaction geometry example, not a recommended replacement for ListItem.

```kotlin
Row(
    modifier = modifier
        .fillMaxWidth()
        .heightIn(min = 56.dp)
        .toggleable(
            value = checked,
            role = Role.Switch,
            onValueChange = onCheckedChange
        )
        .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Text(text = label, modifier = Modifier.weight(1f))
    Switch(checked = checked, onCheckedChange = null)
}
```

If the trailing action opens a different destination or performs a distinct operation, use its own interactive component and verify independent semantics. Do not blindly merge both actions.

## Validation matrix

| Scenario | Expected result |
|---|---|
| Tap near a full-width row edge inside content padding | Row action fires once; indication includes the padding. |
| Press inside shaped card | Indication follows shape; external gutter remains outside indication. |
| Tap between sufficiently sized, separated cards | No card action, unless design intentionally defines a larger touch area. |
| Large text | Text wraps without truncating essential content; row can grow. |
| RTL | Logical start/end and directional icons follow layout direction. |
| Disabled | No action; appropriate disabled visual and semantics. |
| Focus/keyboard/hover where supported | Appropriate interaction and visual state retained. |

Use coordinate-based touch tests when validating edge taps, not only `performClick()` on semantics. Inspect pressed screenshots or a device recording for ripple shape and bounds. Do not claim visual parity from compilation alone.
