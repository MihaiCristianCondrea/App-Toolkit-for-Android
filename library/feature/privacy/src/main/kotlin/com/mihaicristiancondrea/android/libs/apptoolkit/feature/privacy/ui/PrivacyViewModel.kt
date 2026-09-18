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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.mappers.toUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.states.PrivacyUiState
import kotlinx.coroutines.launch

/**
 * Owns the privacy screen entries and routes their clicks.
 *
 * Host destinations that live outside this module are reached through [PrivacySettingsProvider];
 * opening a URL needs a `Context`, so it leaves as a [PrivacyAction] for the screen to perform.
 */
class PrivacyViewModel(
    private val provider: PrivacySettingsProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<PrivacyUiState, PrivacyEvent, PrivacyAction>(
    initialState = UiStateScreen(data = PrivacyUiState()),
    firebaseController = firebaseController,
    screenName = "Privacy",
) {

    init {
        onEvent(event = PrivacyEvent.Load)
    }

    override fun handleEvent(event: PrivacyEvent) {
        when (event) {
            is PrivacyEvent.Load -> loadItems()
            is PrivacyEvent.ItemClicked -> onItemClicked(action = event.action)
        }
    }

    private fun loadItems() {
        startOperation(action = Actions.LOAD_PRIVACY_ITEMS)
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.setSuccess(data = provider.toUiState())
            }
        }
    }

    private fun onItemClicked(action: PrivacyItemAction) {
        startOperation(action = Actions.OPEN_PRIVACY_ITEM)
        when (action) {
            is PrivacyItemAction.OpenUrl -> sendAction(PrivacyAction.OpenUrl(url = action.url))
            is PrivacyItemAction.OpenPermissions -> provider.openPermissionsScreen()
            is PrivacyItemAction.OpenAds -> provider.openAdsScreen()
            is PrivacyItemAction.OpenUsageAndDiagnostics -> provider.openUsageAndDiagnosticsScreen()
        }
    }

    private object Actions {
        const val LOAD_PRIVACY_ITEMS: String = "loadPrivacyItems"
        const val OPEN_PRIVACY_ITEM: String = "openPrivacyItem"
    }
}
