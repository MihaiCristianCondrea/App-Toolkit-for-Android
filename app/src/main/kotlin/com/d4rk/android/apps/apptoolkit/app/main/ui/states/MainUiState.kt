package com.d4rk.android.apps.apptoolkit.app.main.ui.states

import androidx.compose.runtime.Immutable
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MainUiState(
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
    val showDialog: Boolean = false,
    val navigationDrawerItems: ImmutableList<NavigationDrawerItem> = persistentListOf()
)