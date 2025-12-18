package com.d4rk.android.libs.apptoolkit.app.about.domain.repository

import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import kotlinx.coroutines.flow.Flow

/**
 * Repository responsible for providing data for the about screen.
 */
interface AboutRepository {
    /**
     * Retrieve information displayed on the about screen.
     *
     * @return A [AboutUiState] data object.
     */
    fun getAboutInfoStream(): Flow<AboutUiState>

    /**
     * Copy the provided [deviceInfo] string to the clipboard with the given [label].
     *
     * Implementations should handle threading to ensure this call is safe from the
     * main thread and can be executed off the UI thread when necessary.
     */
    suspend fun copyDeviceInfo(label: String, deviceInfo: String)
}
