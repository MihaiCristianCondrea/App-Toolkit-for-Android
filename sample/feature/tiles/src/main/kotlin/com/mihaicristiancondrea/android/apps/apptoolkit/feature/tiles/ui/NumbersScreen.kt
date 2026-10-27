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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitToolKind
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.logQuickSettingsTileRequest
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CounterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils.requestQuickSettingsTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ToolkitToolBottomSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.TileIconBadge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolkitToolChips
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logViewItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel

private val NumbersTrackedScreen = AppScreenTracking.Screens.NUMBERS

/**
 * Dedicated Numbers workspace for calculation, conversion, counting, and chance.
 *
 * Presents a warm gold hero banner, clickable grouped cards for Tip & Bill Splitter and Unit
 * Converter under "Calculate & convert", and clickable grouped cards for Counter, Random Number
 * Picker, and Dice Roll under "Count & chance". Every tool card opens [ToolkitToolBottomSheet].
 */
@Composable
fun NumbersScreen() {
    val counterViewModel: CounterToolViewModel = koinViewModel()
    val counterState: CounterToolUiState by counterViewModel.state.collectAsStateWithLifecycle()

    ScreenHost(screen = NumbersTrackedScreen) {
        NumbersScreenContent(
            counterState = counterState,
            contentPadding = contentPadding(),
        )
    }
}

@Composable
internal fun NumbersScreenContent(
    counterState: CounterToolUiState,
    contentPadding: PaddingValues,
    calculateTools: ImmutableList<ToolkitTile> = NumbersCalculateTools,
    chanceTools: ImmutableList<ToolkitTile> = NumbersChanceTools,
) {
    val context = LocalContext.current
    val telemetry = LocalTelemetry.current
    var selectedTile by remember { mutableStateOf<ToolkitTile?>(null) }
    val counterTile = remember(chanceTools) {
        chanceTools.firstOrNull { it.id == ToolkitTileIds.COUNTER }
    }
    val secondaryChanceTools = remember(chanceTools) {
        chanceTools.filterNot { it.id == ToolkitTileIds.COUNTER }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = SizeConstants.LargeSize,
                top = contentPadding.calculateTopPadding() + SizeConstants.LargeSize,
                end = SizeConstants.LargeSize,
                bottom = contentPadding.calculateBottomPadding() + SizeConstants.LargeSize,
            ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        NumbersHeroBanner(
            modifier = Modifier.animateVisibility(index = 0),
        )

        if (calculateTools.isNotEmpty()) {
            NumbersSectionHeader(
                title = stringResource(id = R.string.numbers_section_calculate),
                modifier = Modifier.animateVisibility(index = 1),
            )
            NumbersGroupedToolRow(
                tools = calculateTools,
                topRow = true,
                bottomRow = true,
                modifier = Modifier.animateVisibility(index = 2),
                onOpenTool = { tile ->
                    telemetry.logViewItem(
                        itemId = tile.id,
                        itemName = tile.id,
                        itemCategory = "numbers_calculate",
                    )
                    selectedTile = tile
                },
            )
        }

        if (chanceTools.isNotEmpty()) {
            NumbersSectionHeader(
                title = stringResource(id = R.string.numbers_section_count_chance),
                modifier = Modifier.animateVisibility(index = 3),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateVisibility(index = 4),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraSmallSize),
            ) {
                counterTile?.let { tile ->
                    NumbersCounterFeatureCard(
                        tile = tile,
                        count = counterState.count,
                        hasBottomNeighbors = secondaryChanceTools.isNotEmpty(),
                        onClick = {
                            telemetry.logViewItem(
                                itemId = tile.id,
                                itemName = tile.id,
                                itemCategory = "numbers_chance",
                            )
                            selectedTile = tile
                        },
                    )
                }

                if (secondaryChanceTools.isNotEmpty()) {
                    NumbersGroupedToolRow(
                        tools = secondaryChanceTools,
                        topRow = counterTile == null,
                        bottomRow = true,
                        onOpenTool = { tile ->
                            telemetry.logViewItem(
                                itemId = tile.id,
                                itemName = tile.id,
                                itemCategory = "numbers_chance",
                            )
                            selectedTile = tile
                        },
                    )
                }
            }
        }

        NavigationBarSpacer()
    }

    selectedTile?.let { tile ->
        ToolkitToolBottomSheet(
            tile = tile,
            onClose = { selectedTile = null },
            onAddTile = {
                val requestKey = tile.requestKey ?: return@ToolkitToolBottomSheet
                requestQuickSettingsTile(
                    context = context,
                    requestKey = requestKey,
                    onResult = { outcome ->
                        telemetry.logQuickSettingsTileRequest(tileId = requestKey, outcome = outcome)
                    },
                )
            },
            onSetupTile = {},
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NumbersHeroBanner(
    modifier: Modifier = Modifier,
) {
    val colors = ToolkitTileIcon.RandomNumber.iconColors()
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SizeConstants.ExtraLargeIncreasedSize),
        color = colors.container.copy(alpha = 0.16f),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 28.dp, y = 18.dp)
                    .clip(MaterialShapes.SoftBurst.toShape())
                    .background(colors.container.copy(alpha = 0.12f)),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SizeConstants.ExtraLargeSize),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
                ) {
                    Surface(
                        shape = RoundedCornerShape(SizeConstants.LargeSize),
                        color = colors.container.copy(alpha = 0.24f),
                    ) {
                        Text(
                            text = stringResource(id = R.string.numbers_title).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.content,
                            modifier = Modifier.padding(
                                horizontal = SizeConstants.MediumSize,
                                vertical = SizeConstants.ExtraTinySize,
                            ),
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.numbers_hero_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(id = R.string.numbers_hero_summary),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(MaterialShapes.Cookie12Sided.toShape())
                        .background(colors.container.copy(alpha = 0.28f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Numbers,
                        contentDescription = null,
                        tint = colors.content,
                        modifier = Modifier.size(36.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun NumbersSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(
            start = SizeConstants.ExtraSmallSize,
            top = SizeConstants.ExtraSmallSize,
        ),
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NumbersCounterFeatureCard(
    tile: ToolkitTile,
    count: Int,
    hasBottomNeighbors: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val colors = tile.icon.iconColors()
    val outer = SizeConstants.ExtraLargeIncreasedSize
    val inner = SizeConstants.SmallSize
    val cardShape = RoundedCornerShape(
        topStart = outer,
        topEnd = outer,
        bottomStart = if (hasBottomNeighbors) inner else outer,
        bottomEnd = if (hasBottomNeighbors) inner else outer,
    )

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TileIconBadge(icon = tile.icon, large = true)
                Spacer(modifier = Modifier.width(SizeConstants.LargeSize))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
                ) {
                    Text(
                        text = stringResource(id = tile.titleResId),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(id = tile.summaryResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.width(SizeConstants.MediumSize))
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(MaterialShapes.Cookie9Sided.toShape())
                        .background(colors.container.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.content,
                    )
                }
            }
            ToolkitToolChips(tile = tile)
        }
    }
}

@Composable
private fun NumbersGroupedToolRow(
    tools: List<ToolkitTile>,
    topRow: Boolean,
    bottomRow: Boolean,
    modifier: Modifier = Modifier,
    onOpenTool: (ToolkitTile) -> Unit,
) {
    val outer = SizeConstants.ExtraLargeIncreasedSize
    val inner = SizeConstants.SmallSize

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.ExtraSmallSize),
    ) {
        tools.forEachIndexed { index, tile ->
            val isFirst = index == 0
            val isLast = index == tools.lastIndex
            val cardShape = RoundedCornerShape(
                topStart = if (topRow && isFirst) outer else inner,
                topEnd = if (topRow && isLast) outer else inner,
                bottomStart = if (bottomRow && isFirst) outer else inner,
                bottomEnd = if (bottomRow && isLast) outer else inner,
            )
            NumbersToolCard(
                tile = tile,
                shape = cardShape,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = { onOpenTool(tile) },
            )
        }
    }
}

@Composable
private fun NumbersToolCard(
    tile: ToolkitTile,
    shape: Shape,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SizeConstants.LargeSize),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                TileIconBadge(icon = tile.icon, large = true)
                Column(
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
                ) {
                    Text(
                        text = stringResource(id = tile.titleResId),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(id = tile.summaryResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(modifier = Modifier.height(SizeConstants.MediumSize))
            ToolkitToolChips(tile = tile)
        }
    }
}

internal val NumbersCalculateTools: ImmutableList<ToolkitTile> = persistentListOf(
    ToolkitTile(
        id = ToolkitTileIds.BILL_SPLITTER,
        titleResId = R.string.tile_bill_splitter_title,
        summaryResId = R.string.tile_bill_splitter_summary,
        icon = ToolkitTileIcon.BillSplitter,
        status = ToolkitTileStatus.Available,
        kind = ToolkitToolKind.Expanded,
    ),
    ToolkitTile(
        id = ToolkitTileIds.UNIT_CONVERTER,
        titleResId = R.string.tile_converter_title,
        summaryResId = R.string.tile_converter_summary,
        icon = ToolkitTileIcon.Converter,
        status = ToolkitTileStatus.Available,
        kind = ToolkitToolKind.Expanded,
    ),
)

internal val NumbersChanceTools: ImmutableList<ToolkitTile> = persistentListOf(
    ToolkitTile(
        id = ToolkitTileIds.COUNTER,
        titleResId = R.string.tile_counter_title,
        summaryResId = R.string.tile_counter_summary,
        icon = ToolkitTileIcon.Counter,
        status = ToolkitTileStatus.Available,
        kind = ToolkitToolKind.Quick,
        requestKey = ToolkitTileIds.COUNTER,
    ),
    ToolkitTile(
        id = ToolkitTileIds.RANDOM_NUMBER,
        titleResId = R.string.tile_random_number_title,
        summaryResId = R.string.tile_random_number_summary,
        icon = ToolkitTileIcon.RandomNumber,
        status = ToolkitTileStatus.Available,
        kind = ToolkitToolKind.Quick,
    ),
    ToolkitTile(
        id = ToolkitTileIds.DICE_ROLL,
        titleResId = R.string.tile_dice_roll_title,
        summaryResId = R.string.tile_dice_roll_summary,
        icon = ToolkitTileIcon.Dice,
        status = ToolkitTileStatus.Available,
        kind = ToolkitToolKind.Quick,
        requestKey = ToolkitTileIds.DICE_ROLL,
    ),
)

internal val NumbersSecondaryTools: ImmutableList<ToolkitTile> =
    persistentListOf(*(NumbersCalculateTools + NumbersChanceTools).toTypedArray())

@Preview(showBackground = true)
@Composable
private fun NumbersScreenContentPreview() {
    MaterialTheme {
        Surface {
            NumbersScreenContent(
                counterState = CounterToolUiState(count = 12),
                contentPadding = PaddingValues(),
            )
        }
    }
}
