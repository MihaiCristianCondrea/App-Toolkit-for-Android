package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.actions

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed class FavoriteAppsEvent : UiEvent {
    data object LoadFavorites : FavoriteAppsEvent()
    data object OpenRandomApp : FavoriteAppsEvent()
}

