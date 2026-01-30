package com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository

class ToggleFavoriteUseCase(
    private val repository: FavoritesRepository,
) {
    suspend operator fun invoke(packageName: String) {
        repository.toggleFavorite(packageName)
    }
}
