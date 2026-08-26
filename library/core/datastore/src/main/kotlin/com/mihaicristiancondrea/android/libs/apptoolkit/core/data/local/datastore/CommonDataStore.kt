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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.CommonDataStoreCore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.AdsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.AppStatePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ChangelogPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.DisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.FavoritesPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.OnboardingPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ReviewPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.UsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.migrations.commonDataStoreMigrations
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultAdsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultAppStatePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultChangelogPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultDisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultFavoritesPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultOnboardingPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultReviewPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.sources.DefaultThemePreferencesDataSource
import kotlinx.coroutines.flow.Flow

/**
 * Process-safe delegate for the toolkit's single `settings` Preferences DataStore file.
 *
 * Migrations run here rather than in a data source so they complete before the first read is
 * served; a source that migrated on construction would let consumers observe the old values first.
 */
val Context.commonDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DataStoreNamesConstants.DATA_STORE_SETTINGS,
    produceMigrations = { commonDataStoreMigrations() },
)

/**
 * Facade over the toolkit's preference data sources.
 *
 * Every value lives in the single `settings` Preferences DataStore, which is what
 * [Context.commonDataStore] hands out; the preferences are grouped into cohesive data sources
 * rather than split across files so that no stored key moves and existing installs keep their
 * data. Each group is exposed here and registered in the Koin graph, so new code can depend on the
 * narrow contract it needs — [ThemePreferencesDataSource], [ReviewPreferencesDataSource], and so
 * on — instead of on this whole surface.
 *
 * The members below delegate to those sources and exist so that callers written against the
 * previous single-class API keep compiling.
 */
open class CommonDataStore(
    context: Context,
) : OnboardingPreferencesDataSource, UsageAndDiagnosticsPreferencesDataSource,
    ConsentPreferencesDataSource, CommonDataStoreCore {

    val dataStore: DataStore<Preferences> = context.commonDataStore

    /** Appearance preferences: theme mode, AMOLED, and palette selection. */
    val themePreferences: ThemePreferencesDataSource =
        DefaultThemePreferencesDataSource(dataStore = dataStore)

    /** Display preferences: language, startup destination, and interaction chrome. */
    val displayPreferences: DisplayPreferencesDataSource =
        DefaultDisplayPreferencesDataSource(dataStore = dataStore)

    /** First-run state. */
    val onboardingPreferences: OnboardingPreferencesDataSource =
        DefaultOnboardingPreferencesDataSource(dataStore = dataStore)

    /** Consent and usage-diagnostics toggles. */
    val diagnosticsPreferences: DefaultDiagnosticsPreferencesDataSource =
        DefaultDiagnosticsPreferencesDataSource(dataStore = dataStore)

    /** The limit-ads opt-in, the only stored ads preference. */
    val adsPreferences: AdsPreferencesDataSource =
        DefaultAdsPreferencesDataSource(dataStore = dataStore)

    /** In-app review counters. */
    val reviewPreferences: ReviewPreferencesDataSource =
        DefaultReviewPreferencesDataSource(dataStore = dataStore)

    /** Last seen changelog version and its cached Markdown. */
    val changelogPreferences: ChangelogPreferencesDataSource =
        DefaultChangelogPreferencesDataSource(dataStore = dataStore)

    /** One-shot app-state flags and timestamps. */
    val appStatePreferences: AppStatePreferencesDataSource =
        DefaultAppStatePreferencesDataSource(dataStore = dataStore)

    /** Favorited package names. */
    val favoritesPreferences: FavoritesPreferencesDataSource =
        DefaultFavoritesPreferencesDataSource(dataStore = dataStore)

    companion object {
        @Volatile
        private var instance: CommonDataStore? = null

        /**
         * Returns the process-wide facade.
         *
         * Prefer dependency injection in production; callers must not use this to create a parallel
         * preference graph.
         */
        fun getInstance(context: Context): CommonDataStore {
            return instance ?: synchronized(lock = this) {
                instance ?: CommonDataStore(context = context.applicationContext)
                    .also { instance = it }
            }
        }
    }

    // Nothing here owns a coroutine any more; the member stays because `CommonDataStoreCore`
    // declares it and hosts call it on teardown.
    override fun close() = Unit

    // region App state

    val lastUsed: Flow<Long> get() = appStatePreferences.lastUsed

    suspend fun saveLastUsed(timestamp: Long) = appStatePreferences.saveLastUsed(timestamp)

    val settingsInteracted: Flow<Boolean> get() = appStatePreferences.settingsInteracted

    suspend fun markSettingsInteracted() = appStatePreferences.markSettingsInteracted()

    val componentsShowcaseUnlocked: Flow<Boolean>
        get() = appStatePreferences.componentsShowcaseUnlocked

    suspend fun saveComponentsShowcaseUnlocked(isUnlocked: Boolean) =
        appStatePreferences.saveComponentsShowcaseUnlocked(isUnlocked)

    // endregion

    // region Onboarding

    override val startup: Flow<Boolean> get() = onboardingPreferences.startup

    /** Stores whether the app has completed its first-time startup flow. */
    override suspend fun saveStartup(isFirstTime: Boolean) =
        onboardingPreferences.saveStartup(isFirstTime)

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

    /**
     * Theme mode mirrored as Compose state so the settings UI can reflect a tap before the write
     * round-trips through DataStore. Presentation state rather than stored data; the persisted
     * value is [themeMode].
     */
    val themeModeState = mutableStateOf(value = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM)

    val themeMode: Flow<String> get() = themePreferences.themeMode

    suspend fun saveThemeMode(mode: String) = themePreferences.saveThemeMode(mode)

    val amoledMode: Flow<Boolean> get() = themePreferences.amoledMode

    suspend fun saveAmoledMode(isChecked: Boolean) = themePreferences.saveAmoledMode(isChecked)

    val dynamicColors: Flow<Boolean> get() = themePreferences.dynamicColors

    suspend fun saveDynamicColors(isChecked: Boolean) =
        themePreferences.saveDynamicColors(isChecked)

    val dynamicPaletteVariant: Flow<Int> get() = themePreferences.dynamicPaletteVariant

    suspend fun saveDynamicPaletteVariant(variant: Int) =
        themePreferences.saveDynamicPaletteVariant(variant)

    val staticPaletteId: Flow<String> get() = themePreferences.staticPaletteId

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

    // region Ads

    /** The user's limit-ads opt-in; how a build acts on it is policy, not storage. */
    val limitAds: Flow<Boolean> get() = adsPreferences.limitAds

    suspend fun saveLimitAds(isChecked: Boolean) = adsPreferences.saveLimitAds(isChecked)

    // endregion

    // region Favorites

    val favoriteApps: Flow<Set<String>> get() = favoritesPreferences.favoriteApps

    suspend fun toggleFavoriteApp(packageName: String) =
        favoritesPreferences.toggleFavoriteApp(packageName)

    // endregion

    // region Review

    val sessionCount: Flow<Int> get() = reviewPreferences.sessionCount

    val hasPromptedReview: Flow<Boolean> get() = reviewPreferences.hasPromptedReview

    suspend fun incrementSessionCount() = reviewPreferences.incrementSessionCount()

    suspend fun setHasPromptedReview(value: Boolean) =
        reviewPreferences.setHasPromptedReview(value)

    // endregion

    // region Changelog

    fun getLastSeenVersion(default: String = ""): Flow<String> =
        changelogPreferences.lastSeenVersion(default = default)

    suspend fun saveLastSeenVersion(version: String) =
        changelogPreferences.saveLastSeenVersion(version)

    fun getCachedChangelog(default: String = ""): Flow<String> =
        changelogPreferences.cachedChangelog(default = default)

    suspend fun saveCachedChangelog(changelog: String) =
        changelogPreferences.saveCachedChangelog(changelog)

    // endregion
}
