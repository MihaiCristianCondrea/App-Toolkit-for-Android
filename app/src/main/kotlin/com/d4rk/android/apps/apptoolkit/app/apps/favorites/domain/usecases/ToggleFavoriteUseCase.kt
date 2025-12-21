package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.repository.FavoritesRepository
import com.d4rk.android.libs.apptoolkit.core.domain.usecases.Repository

class ToggleFavoriteUseCase(
    private val repository: FavoritesRepository,
) : Repository<String, Unit> {
    override suspend operator fun invoke(param: String) {
        repository.toggleFavorite(param)
    }
}

