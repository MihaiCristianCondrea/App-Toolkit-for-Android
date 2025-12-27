package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.app.main.ui.contract.MainAction
import com.d4rk.android.apps.apptoolkit.app.main.ui.contract.MainEvent
import com.d4rk.android.apps.apptoolkit.app.main.ui.states.MainUiState
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class MainViewModel(
    private val navigationRepository: NavigationRepository
) : ScreenViewModel<MainUiState, MainEvent, MainAction>(
    initialState = UiStateScreen(data = MainUiState())
) {

    init {
        onEvent(MainEvent.LoadNavigation)
    }

    override fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.LoadNavigation -> loadNavigationItems()
        }
    }

    private fun loadNavigationItems() {
        viewModelScope.launch {
            navigationRepository.getNavigationDrawerItems()
                .catch { error ->
                    screenState.successData {
                        copy(
                            showSnackbar = true,
                            snackbarMessage = error.message ?: "Failed to load navigation"
                        )
                    }
                }
                .collect { items: List<NavigationDrawerItem> ->
                    screenState.successData {
                        copy(navigationDrawerItems = items.toImmutableList())
                    }
                }
        }
    }
}