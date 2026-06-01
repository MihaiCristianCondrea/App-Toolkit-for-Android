/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.apps.apptoolkit.app.tiles.ui

import android.app.StatusBarManager
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.BatteryChargingFull
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentPasteOff
import androidx.compose.material.icons.outlined.Dehaze
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTile
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileCategory
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileIcon
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.ToolkitTileStatus
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.getTileServiceRequests
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.contract.ToolkitTilesAction
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.contract.ToolkitTilesEvent
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitTilesFilter
import com.d4rk.android.apps.apptoolkit.app.tiles.ui.state.ToolkitTilesUiState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel

/** Route-level composable for the Toolkit Tiles catalog. */
@Composable
fun ToolkitTilesRoute(
    paddingValues: PaddingValues,
) {
    val viewModel: ToolkitTilesViewModel = koinViewModel()
    val screenState: UiStateScreen<ToolkitTilesUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(viewModel, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(ToolkitTilesEvent.Refresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.actionEvent.collect { action ->
            when (action) {
                is ToolkitTilesAction.RequestAddTile -> requestQuickSettingsTile(
                    context = context,
                    requestKey = action.requestKey,
                )
                ToolkitTilesAction.ShowSetupRequiredMessage -> Toast.makeText(
                    context,
                    R.string.tiles_setup_required_message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen(paddingValues = paddingValues) },
        onEmpty = { NoDataScreen() },
        onError = { NoDataScreen() },
        onSuccess = { state ->
            ToolkitTilesScreen(
                state = state,
                paddingValues = paddingValues,
                onEvent = viewModel::onEvent,
            )
        },
    )
}

/** Stateless Material 3 screen that renders Quick Settings tile categories and actions. */
@Composable
fun ToolkitTilesScreen(
    state: ToolkitTilesUiState,
    paddingValues: PaddingValues,
    onEvent: (ToolkitTilesEvent) -> Unit,
) {
    val filteredCategories = remember(state.categories, state.selectedFilter) {
        state.categories.filterFor(state.selectedFilter)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = SizeConstants.LargeSize,
            top = paddingValues.calculateTopPadding() + SizeConstants.LargeSize,
            end = SizeConstants.LargeSize,
            bottom = paddingValues.calculateBottomPadding() + SizeConstants.LargeSize,
        ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        item {
            TilesFilters(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { filter -> onEvent(ToolkitTilesEvent.FilterSelected(filter)) },
            )
        }
        if (filteredCategories.isEmpty()) {
            item {
                EmptyFilterCard()
            }
        } else {
            items(
                items = filteredCategories,
                key = ToolkitTileCategory::id,
            ) { category ->
                val expanded = category.id in state.expandedCategoryIds
                TileCategorySection(
                    category = category,
                    expanded = expanded,
                    onToggle = { onEvent(ToolkitTilesEvent.CategoryToggled(category.id)) },
                    onAddTile = { tile -> onEvent(ToolkitTilesEvent.AddTileClicked(tile.requestKey)) },
                    onSetupTile = { tile -> onEvent(ToolkitTilesEvent.TileSetupClicked(tile.id)) },
                )
            }
        }
        item {
            HowToAddTilesCard()
        }
        item {
            NavigationBarSpacer()
        }
    }
}

@Composable
private fun TilesFilters(
    selectedFilter: ToolkitTilesFilter,
    onFilterSelected: (ToolkitTilesFilter) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
    ) {
        FilterItems.forEach { item ->
            FilterChip(
                selected = selectedFilter == item.filter,
                onClick = { onFilterSelected(item.filter) },
                label = { Text(text = stringResource(id = item.labelResId)) },
                leadingIcon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(SizeConstants.ButtonIconSize),
                    )
                },
            )
        }
    }
}

@Composable
private fun TileCategorySection(
    category: ToolkitTileCategory,
    expanded: Boolean,
    onToggle: () -> Unit,
    onAddTile: (ToolkitTile) -> Unit,
    onSetupTile: (ToolkitTile) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(SizeConstants.ExtraLargeIncreasedSize),
    ) {
        Column(modifier = Modifier.padding(SizeConstants.MediumSize)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TileIconBadge(icon = category.icon)
                Spacer(modifier = Modifier.width(SizeConstants.MediumSize))
                Text(
                    text = stringResource(id = category.titleResId),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(id = R.string.tiles_count_format, category.tiles.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = stringResource(
                            id = if (expanded) R.string.tiles_collapse_category else R.string.tiles_expand_category,
                        ),
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                    Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize))
                    category.tiles.forEachIndexed { index, tile ->
                        ToolkitTileCard(
                            tile = tile,
                            position = groupedItemPosition(index, category.tiles.size),
                            onAddTile = { onAddTile(tile) },
                            onSetupTile = { onSetupTile(tile) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ToolkitTileCard(
    tile: ToolkitTile,
    position: GroupedItemPosition,
    onAddTile: () -> Unit,
    onSetupTile: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .groupedCorners(position),
        shape = RectangleShape,
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TileIconBadge(icon = tile.icon, large = true)
                Spacer(modifier = Modifier.width(SizeConstants.LargeSize))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = tile.titleResId),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(id = tile.summaryResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = stringResource(id = R.string.tiles_more_options),
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        shape = RoundedCornerShape(SizeConstants.LargeSize),
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.tiles_add)) },
                            onClick = {
                                showMenu = false
                                onAddTile()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                )
                            },
                            enabled = tile.status == ToolkitTileStatus.Available,
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.tiles_setup)) },
                            onClick = {
                                showMenu = false
                                onSetupTile()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.WarningAmber,
                                    contentDescription = null,
                                )
                            },
                        )
                    }
                }
            }
            if (tile.status != ToolkitTileStatus.Available && tile.status != ToolkitTileStatus.NeedsSetup) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        SizeConstants.SmallSize,
                        Alignment.End
                    ),
                ) {
                    TileStatusChip(status = tile.status)
                }
            }
        }
    }
}

@Composable
private fun TileIconBadge(
    icon: ToolkitTileIcon,
    large: Boolean = false,
) {
    val colors = icon.iconColors()
    val size = if (large) SizeConstants.ExtraExtraLargeSize + SizeConstants.SmallSize else SizeConstants.FortyFourSize
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(colors.container.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.imageVector(),
            contentDescription = null,
            tint = colors.content,
        )
    }
}

@Composable
private fun TileStatusChip(
    status: ToolkitTileStatus,
    onClick: () -> Unit = {},
) {
    val colors = status.statusColors()
    AssistChip(
        onClick = onClick,
        label = { Text(text = stringResource(id = status.labelResId())) },
        leadingIcon = {
            Icon(
                imageVector = status.icon(),
                contentDescription = null,
                modifier = Modifier.size(SizeConstants.ButtonIconSize),
            )
        },
        colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
            containerColor = colors.container,
            labelColor = colors.content,
            leadingIconContentColor = colors.content,
        ),
        border = null,
    )
}

@Composable
private fun EmptyFilterCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SizeConstants.LargeSize),
    ) {
        Text(
            modifier = Modifier.padding(SizeConstants.LargeSize),
            text = stringResource(id = R.string.tiles_filter_empty),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun HowToAddTilesCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SizeConstants.ExtraLargeIncreasedSize),
        color = MaterialTheme.colorScheme.tertiaryContainer,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SizeConstants.LargeSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            Spacer(modifier = Modifier.width(SizeConstants.LargeSize))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.tiles_how_to_add_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = stringResource(id = R.string.tiles_how_to_add_summary),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
    }
}

private data class FilterItem(
    val filter: ToolkitTilesFilter,
    val labelResId: Int,
    val icon: ImageVector,
)

private val FilterItems: ImmutableList<FilterItem> = persistentListOf(
    FilterItem(ToolkitTilesFilter.All, R.string.tiles_filter_all, Icons.Outlined.GridView),
    FilterItem(ToolkitTilesFilter.Added, R.string.tiles_filter_added, Icons.Outlined.CheckCircle),
    FilterItem(ToolkitTilesFilter.NeedsSetup, R.string.tiles_filter_needs_setup, Icons.Outlined.WarningAmber),
    FilterItem(ToolkitTilesFilter.Unsupported, R.string.tiles_filter_unsupported, Icons.Outlined.Block),
)

private fun ImmutableList<ToolkitTileCategory>.filterFor(
    filter: ToolkitTilesFilter,
): List<ToolkitTileCategory> = when (filter) {
    ToolkitTilesFilter.All -> this
    ToolkitTilesFilter.Added -> filterByStatus(ToolkitTileStatus.Added)
    ToolkitTilesFilter.NeedsSetup -> filterByStatus(ToolkitTileStatus.NeedsSetup)
    ToolkitTilesFilter.Unsupported -> filterByStatus(ToolkitTileStatus.Unsupported)
}

private fun ImmutableList<ToolkitTileCategory>.filterByStatus(
    status: ToolkitTileStatus,
): List<ToolkitTileCategory> = mapNotNull { category ->
    val tiles = category.tiles.filter { tile -> tile.status == status }
    if (tiles.isEmpty()) null else category.copy(tiles = tiles.toImmutableList())
}

private fun ToolkitTileIcon.imageVector(): ImageVector = when (this) {
    ToolkitTileIcon.Level -> Icons.Outlined.Straighten
    ToolkitTileIcon.Compass -> Icons.Outlined.Explore
    ToolkitTileIcon.Lux -> Icons.Outlined.WbSunny
    ToolkitTileIcon.Network -> Icons.Outlined.SyncAlt
    ToolkitTileIcon.Temperature -> Icons.Outlined.Thermostat
    ToolkitTileIcon.Coin -> Icons.Outlined.MonetizationOn
    ToolkitTileIcon.Dice -> Icons.Outlined.Casino
    ToolkitTileIcon.Counter -> Icons.Outlined.Dehaze
    ToolkitTileIcon.Clipboard -> Icons.Outlined.ContentPasteOff
    ToolkitTileIcon.Battery -> Icons.Outlined.BatteryChargingFull
    ToolkitTileIcon.Memory -> Icons.Outlined.Memory
    ToolkitTileIcon.Caffeine -> Icons.Outlined.Timer
    ToolkitTileIcon.Sound -> Icons.Outlined.GraphicEq
    ToolkitTileIcon.Volume -> Icons.AutoMirrored.Outlined.VolumeUp
    ToolkitTileIcon.Screenshot -> Icons.Outlined.Screenshot
    ToolkitTileIcon.Lock -> Icons.Outlined.Lock
    ToolkitTileIcon.Power -> Icons.Outlined.PowerSettingsNew
    ToolkitTileIcon.Music -> Icons.Outlined.MusicNote
    ToolkitTileIcon.Breathing -> Icons.Outlined.FavoriteBorder
    ToolkitTileIcon.Sos -> Icons.Outlined.WarningAmber
}

private fun ToolkitTileStatus.labelResId(): Int = when (this) {
    ToolkitTileStatus.Added -> R.string.tiles_status_added
    ToolkitTileStatus.Available -> R.string.tiles_status_available
    ToolkitTileStatus.NeedsSetup -> R.string.tiles_status_needs_setup
    ToolkitTileStatus.Unsupported -> R.string.tiles_status_unsupported
}

private fun ToolkitTileStatus.icon(): ImageVector = when (this) {
    ToolkitTileStatus.Added -> Icons.Outlined.CheckCircle
    ToolkitTileStatus.Available -> Icons.Outlined.Info
    ToolkitTileStatus.NeedsSetup -> Icons.Outlined.WarningAmber
    ToolkitTileStatus.Unsupported -> Icons.Outlined.Close
}

@Composable
private fun ToolkitTileIcon.iconColors(): StatusColors {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    return when (this) {
        ToolkitTileIcon.Level,
        ToolkitTileIcon.Compass,
        ToolkitTileIcon.Lux -> StatusColors(
            container = if (isDark) Color(0xFF006A60) else Color(0xFF00A091),
            content = if (isDark) Color(0xFF74DED1) else Color(0xFF006A60),
        )

        ToolkitTileIcon.Network -> StatusColors(
            container = if (isDark) Color(0xFF004A77) else Color(0xFF0095EF),
            content = if (isDark) Color(0xFF70CFFF) else Color(0xFF004A77),
        )

        ToolkitTileIcon.Battery,
        ToolkitTileIcon.Memory -> StatusColors(
            container = if (isDark) Color(0xFF5A2D91) else Color(0xFF9158E2),
            content = if (isDark) Color(0xFFD3BFFF) else Color(0xFF5A2D91),
        )

        ToolkitTileIcon.Temperature,
        ToolkitTileIcon.Caffeine,
        ToolkitTileIcon.Breathing -> StatusColors(
            container = if (isDark) Color(0xFF8B4100) else Color(0xFFFF8B26),
            content = if (isDark) Color(0xFFFFB88E) else Color(0xFF8B4100),
        )

        ToolkitTileIcon.Coin,
        ToolkitTileIcon.Dice,
        ToolkitTileIcon.Counter -> StatusColors(
            container = if (isDark) Color(0xFF6B5E00) else Color(0xFFE2C900),
            content = if (isDark) Color(0xFFFBE44D) else Color(0xFF6B5E00),
        )

        ToolkitTileIcon.Clipboard,
        ToolkitTileIcon.Screenshot,
        ToolkitTileIcon.Volume,
        ToolkitTileIcon.Sound -> StatusColors(
            container = if (isDark) Color(0xFF91005A) else Color(0xFFE2008E),
            content = if (isDark) Color(0xFFFFB0D3) else Color(0xFF91005A),
        )

        ToolkitTileIcon.Lock,
        ToolkitTileIcon.Power -> StatusColors(
            container = if (isDark) Color(0xFF910000) else Color(0xFFFF2626),
            content = if (isDark) Color(0xFFFFB4B4) else Color(0xFF910000),
        )

        ToolkitTileIcon.Music -> StatusColors(
            container = if (isDark) Color(0xFF3F0091) else Color(0xFF6F00FF),
            content = if (isDark) Color(0xFFC8BFFF) else Color(0xFF3F0091),
        )

        ToolkitTileIcon.Sos -> StatusColors(
            container = if (isDark) Color(0xFFB10000) else Color(0xFFEE0000),
            content = if (isDark) Color(0xFFFFDAD6) else Color(0xFFB10000),
        )
    }
}

@Composable
private fun ToolkitTileStatus.statusColors(): StatusColors = when (this) {
    ToolkitTileStatus.Added -> StatusColors(
        container = MaterialTheme.colorScheme.primaryContainer,
        content = MaterialTheme.colorScheme.onPrimaryContainer,
    )
    ToolkitTileStatus.Available -> StatusColors(
        container = MaterialTheme.colorScheme.secondaryContainer,
        content = MaterialTheme.colorScheme.onSecondaryContainer,
    )
    ToolkitTileStatus.NeedsSetup -> StatusColors(
        container = MaterialTheme.colorScheme.tertiaryContainer,
        content = MaterialTheme.colorScheme.onTertiaryContainer,
    )
    ToolkitTileStatus.Unsupported -> StatusColors(
        container = MaterialTheme.colorScheme.errorContainer,
        content = MaterialTheme.colorScheme.onErrorContainer,
    )
}

private data class StatusColors(
    val container: Color,
    val content: Color,
)

private fun requestQuickSettingsTile(
    context: Context,
    requestKey: String,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        Toast.makeText(context, R.string.tiles_add_pre_android_13, Toast.LENGTH_LONG).show()
        context.startActivity(Settings.ACTION_SETTINGS.toIntent())
        return
    }

    val request = getTileServiceRequests()[requestKey]
    if (request == null) {
        Toast.makeText(context, R.string.tiles_setup_required_message, Toast.LENGTH_SHORT).show()
        return
    }

    val statusBarManager = context.getSystemService(StatusBarManager::class.java)
    statusBarManager.requestAddTileService(
        request.componentName(context),
        context.getString(request.labelResId),
        Icon.createWithResource(context, request.iconResId),
        context.mainExecutor,
    ) { result ->
        val messageResId = when (result) {
            StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED -> R.string.tiles_add_result_added
            StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED -> R.string.tiles_add_result_already_added
            else -> R.string.tiles_add_result_failed
        }
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()
    }
}

private fun String.toIntent(): android.content.Intent = android.content.Intent(this).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
