package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.models

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitQuickTool
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.ToolkitToolKind
import kotlinx.collections.immutable.ImmutableList

enum class ToolkitTileIcon {
    Level, Compass, Lux, Coin, Dice, Counter, Caffeine, Sound, Music, Breathing, Sos, Morse,
    FlashDimmer, Palette,
}

@Immutable
data class ToolkitTileCategory(
    val id: String,
    @StringRes val titleResId: Int,
    val icon: ToolkitTileIcon,
    val tiles: ImmutableList<ToolkitTile>,
)

@Immutable
data class ToolkitTile(
    val id: String,
    @StringRes val titleResId: Int,
    @StringRes val summaryResId: Int,
    val icon: ToolkitTileIcon,
    val status: ToolkitTileStatus,
    val kind: ToolkitToolKind = ToolkitToolKind.Expanded,
    val quickTool: ToolkitQuickTool? = null,
    val requestKey: String? = null,
)
