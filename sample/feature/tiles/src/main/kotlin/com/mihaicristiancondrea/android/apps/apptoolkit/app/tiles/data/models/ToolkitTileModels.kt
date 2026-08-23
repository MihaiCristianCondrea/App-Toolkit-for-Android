package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models

import kotlinx.collections.immutable.ImmutableList

enum class ToolkitTileStatus { Added, Available, NeedsSetup, Unsupported }

enum class ToolkitToolKind { Quick, Expanded }

enum class ToolkitQuickTool { MaterialColors }

data class ToolkitTileCategoryData(
    val id: String,
    val tiles: ImmutableList<ToolkitTileData>,
    val initiallyExpanded: Boolean = false,
)

data class ToolkitTileData(
    val id: String,
    val status: ToolkitTileStatus,
    val kind: ToolkitToolKind = ToolkitToolKind.Expanded,
    val quickTool: ToolkitQuickTool? = null,
    val requestKey: String? = null,
)
