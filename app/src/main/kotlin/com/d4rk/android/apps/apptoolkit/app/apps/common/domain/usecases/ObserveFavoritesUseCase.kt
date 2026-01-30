package com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase(
    private val repository: FavoritesRepository,
) {
    operator fun invoke(): Flow<Set<String>> = repository.observeFavorites()
}
