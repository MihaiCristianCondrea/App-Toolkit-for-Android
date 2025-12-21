package com.d4rk.android.libs.apptoolkit.app.about.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import kotlinx.coroutines.flow.Flow

/**
 * Use case that exposes the about information as a cold [Flow].
 *
 * This abstraction allows the [AboutViewModel][com.d4rk.android.libs.apptoolkit.app.about.ui.AboutViewModel]
 * to remain agnostic of the underlying data source implementation and follows the
 * guidance of the official Google architecture recommendations.
 */
class ObserveAboutInfoUseCase(
    private val repository: AboutRepository,
) {
    /**
     * Invoke the use case to receive updates for the about screen.
     */
    operator fun invoke(): Flow<AboutUiState> = repository.getAboutInfoStream()
}

