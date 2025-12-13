package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.ui

import androidx.compose.runtime.Immutable
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


@Immutable
data class UiHomeScreen(
    val apps: ImmutableList<AppInfo> = persistentListOf()
)