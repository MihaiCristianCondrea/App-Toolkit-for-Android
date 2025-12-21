package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.repository.FavoritesRepository
import com.d4rk.android.libs.apptoolkit.core.domain.usecases.RepositoryWithoutParam
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase(private val repository: FavoritesRepository) :
    RepositoryWithoutParam<Flow<Set<String>>> {
    override suspend operator fun invoke(): Flow<Set<String>> = repository.observeFavorites()
}

