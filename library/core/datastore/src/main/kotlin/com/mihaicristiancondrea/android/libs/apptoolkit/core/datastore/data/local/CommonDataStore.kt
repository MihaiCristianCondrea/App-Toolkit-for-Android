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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import kotlinx.coroutines.flow.Flow

/**
 * Process-safe delegate for the toolkit's single `settings` Preferences DataStore file.
 *
 * Migrations run here rather than as work a data source does when constructed, so they finish
 * before the first read is served, including reads from sources owned by other modules.
 *
 * A file that can no longer be parsed is replaced with empty preferences, so every value falls back
 * to its default and the user sees first-run state again. Without the handler DataStore throws
 * `CorruptionException` from every read, and several of those reads run at startup outside any
 * catch, so one damaged file would crash the app on every launch until its data was cleared.
 */
val Context.commonDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DataStoreNamesConstants.DATA_STORE_SETTINGS,
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
    produceMigrations = { commonDataStoreMigrations() },
)

/** Shared appearance, consent, app-state and favorites preferences over the single settings file. */
open class CommonDataStore(
    context: Context,
) : UsageAndDiagnosticsPreferencesDataSource, ConsentPreferencesDataSource {

    val dataStore: DataStore<Preferences> = context.commonDataStore

    /** Appearance preferences: theme mode, AMOLED, and palette selection. */
    val themePreferences: ThemePreferencesDataSource =
        DefaultThemePreferencesDataSource(dataStore = dataStore)

    /** Seasonal themes: the easter egg unlock, the weather effect, and holiday theme bookkeeping. */
    val seasonalThemePreferences: SeasonalThemePreferencesDataSource =
        DefaultSeasonalThemePreferencesDataSource(dataStore = dataStore)

    /** Display preferences: language, startup destination, and interaction chrome. */
    val displayPreferences: DisplayPreferencesDataSource =
        DefaultDisplayPreferencesDataSource(dataStore = dataStore)

    /** Consent and usage-diagnostics toggles. */
    val diagnosticsPreferences: DefaultUsageAndDiagnosticsPreferencesDataSource =
        DefaultUsageAndDiagnosticsPreferencesDataSource(dataStore = dataStore)

    /** One-shot app-state flags and timestamps. */
    val appStatePreferences: AppStatePreferencesDataSource =
        DefaultAppStatePreferencesDataSource(dataStore = dataStore)

    /** Favorited package names. */
    val favoritesPreferences: FavoritesPreferencesDataSource =
        DefaultFavoritesPreferencesDataSource(dataStore = dataStore)

    companion object {
        @Volatile
        private var instance: CommonDataStore? = null

        /** Returns the process-wide shared preference adapter. */
        fun getInstance(
            context: Context,
        ): CommonDataStore {
            return instance ?: synchronized(lock = this) {
                instance ?: CommonDataStore(
                    context = context.applicationContext,
                ).also { instance = it }
            }
        }
    }

    // region App state

    val lastUsed: Flow<Long> get() = appStatePreferences.lastUsed

    suspend fun saveLastUsed(timestamp: Long) = appStatePreferences.saveLastUsed(timestamp)

    val settingsInteracted: Flow<Boolean> get() = appStatePreferences.settingsInteracted

    @Suppress("unused")
    suspend fun markSettingsInteracted() = appStatePreferences.markSettingsInteracted()

    val componentsShowcaseUnlocked: Flow<Boolean>
        get() = appStatePreferences.componentsShowcaseUnlocked

    suspend fun saveComponentsShowcaseUnlocked(isUnlocked: Boolean) =
        appStatePreferences.saveComponentsShowcaseUnlocked(isUnlocked)

    // endregion

    // region Display

    /**
     * Observes the preferred startup route.
     *
     * @param default value emitted when the preference has not been set yet.
     */
    fun getStartupPage(default: String = ""): Flow<String> =
        displayPreferences.startupPage(default = default)

    /** Persists the route that should be opened when the app launches. */
    suspend fun saveStartupPage(route: String) = displayPreferences.saveStartupPage(route)

    fun getShowBottomBarLabels(): Flow<Boolean> = displayPreferences.showBottomBarLabels

    suspend fun saveShowLabelsOnBottomBar(isChecked: Boolean) =
        displayPreferences.saveShowBottomBarLabels(isChecked)

    val bouncyButtons: Flow<Boolean> get() = displayPreferences.bouncyButtons

    suspend fun saveBouncyButtons(isChecked: Boolean) =
        displayPreferences.saveBouncyButtons(isChecked)

    fun getLanguage(): Flow<String> = displayPreferences.language

    suspend fun saveLanguage(language: String) = displayPreferences.saveLanguage(language)

    // endregion

    // region Theme

    val themeMode: Flow<String> get() = themePreferences.themeMode

    suspend fun saveThemeMode(mode: String) = themePreferences.saveThemeMode(mode)

    val amoledMode: Flow<Boolean> get() = themePreferences.amoledMode

    suspend fun saveAmoledMode(isChecked: Boolean) = themePreferences.saveAmoledMode(isChecked)

    val dynamicColors: Flow<Boolean> get() = themePreferences.dynamicColors

    suspend fun saveDynamicColors(isChecked: Boolean) =
        themePreferences.saveDynamicColors(isChecked)

    val dynamicPaletteVariant: Flow<Int> get() = themePreferences.dynamicPaletteVariant

    @Suppress("unused")
    suspend fun saveDynamicPaletteVariant(variant: Int) =
        themePreferences.saveDynamicPaletteVariant(variant)

    val staticPaletteId: Flow<String> get() = themePreferences.staticPaletteId

    @Suppress("unused")
    suspend fun saveStaticPaletteId(id: String) = themePreferences.saveStaticPaletteId(id)

    // endregion

    // region Consent and diagnostics
    override fun usageAndDiagnostics(default: Boolean): Flow<Boolean> =
        diagnosticsPreferences.usageAndDiagnostics(default)

    override suspend fun saveUsageAndDiagnostics(isChecked: Boolean) =
        diagnosticsPreferences.saveUsageAndDiagnostics(isChecked)

    override fun analyticsConsent(default: Boolean): Flow<Boolean> =
        diagnosticsPreferences.analyticsConsent(default)

    override suspend fun saveAnalyticsConsent(isGranted: Boolean) =
        diagnosticsPreferences.saveAnalyticsConsent(isGranted)

    override fun adStorageConsent(default: Boolean): Flow<Boolean> =
        diagnosticsPreferences.adStorageConsent(default)

    override suspend fun saveAdStorageConsent(isGranted: Boolean) =
        diagnosticsPreferences.saveAdStorageConsent(isGranted)

    override fun adUserDataConsent(default: Boolean): Flow<Boolean> =
        diagnosticsPreferences.adUserDataConsent(default)

    override suspend fun saveAdUserDataConsent(isGranted: Boolean) =
        diagnosticsPreferences.saveAdUserDataConsent(isGranted)

    override fun adPersonalizationConsent(default: Boolean): Flow<Boolean> =
        diagnosticsPreferences.adPersonalizationConsent(default)

    override suspend fun saveAdPersonalizationConsent(isGranted: Boolean) =
        diagnosticsPreferences.saveAdPersonalizationConsent(isGranted)
    // endregion

    // region Favorites
    val favoriteApps: Flow<Set<String>> get() = favoritesPreferences.favoriteApps
    suspend fun toggleFavoriteApp(packageName: String) =
        favoritesPreferences.toggleFavoriteApp(packageName)
    // endregion

}
