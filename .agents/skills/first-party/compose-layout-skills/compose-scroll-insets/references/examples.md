# Scrolling and inset examples

## Inset cards versus full-width rows

```kotlin
// Independent cards with outer content gutters.
LazyColumn(
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(cards, key = { it.id }) { card ->
        SummaryCard(card = card)
    }
}

// Full-width rows with internal content padding.
LazyColumn(contentPadding = PaddingValues(vertical = 12.dp)) {
    items(settings, key = { it.id }) { setting ->
        SettingsItem(title = setting.title, onClick = { onSetting(setting.id) })
    }
}
```

Do not remove horizontal system-safe insets merely to achieve a wider ripple. The second example assumes its parent already handles any required horizontal safe area. It illustrates design gutters only.

## Padding the viewport vs scroll content

```kotlin
// Viewport is inset; scrolling remains inside its bounds.
LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
    // Items
}

// First/last content have a scrollable inset.
LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
    // Items
}
```

Choose based on desired scroll behavior, not a universal rule. Outer padding may be necessary to reserve space for other UI. For ordinary scrollable Column, modifier order similarly distinguishes `.padding(...).verticalScroll(state)` from `.verticalScroll(state).padding(...)`.

## Scaffold with lazy content

```kotlin
Scaffold(topBar = { ScreenTopBar() }) { innerPadding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(innerPadding),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Items
    }
}
```

Use this when content is intended to scroll within the full viewport with Scaffold-provided edge spacing. If the viewport must stay below/above bars, use root `Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)` instead and do not also use `innerPadding` as list content padding. `consumeWindowInsets` marks padding as consumed; it does not add the padding itself. Any extra design gutters should be accounted for separately.

## Mixed gutters

```kotlin
LazyColumn(contentPadding = PaddingValues(vertical = 12.dp)) {
    item {
        Text(
            text = sectionTitle,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
    item {
        SummaryCard(modifier = Modifier.padding(horizontal = 16.dp))
    }
    items(settings, key = { it.id }) { setting ->
        SettingsItem(title = setting.title, onClick = { onSetting(setting.id) })
    }
}
```

Design gutters are selected per item by the list owner. The row implementation owns its internal padding. This example assumes safe horizontal insets are handled by an ancestor. For forms and keyboard interactions, inspect the shell's existing IME policy before adding another inset modifier.
