package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AppInfo(
    val name: String,
    val packageName: String,
    val iconUrl: String,
    val description: String,
    val screenshots: List<String>,
    val category: AppCategory? = null,
)

@Immutable
data class AppCategory(
    val label: String,
    val id: String,
)
