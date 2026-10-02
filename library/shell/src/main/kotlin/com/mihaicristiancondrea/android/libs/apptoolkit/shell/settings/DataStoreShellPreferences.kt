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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * The shell's settings file.
 *
 * `ShellHost` collects it at the root of the app, so a read that throws would crash every launch.
 * A file that can no longer be parsed is replaced with empty preferences, which puts every shell
 * setting back to its default.
 */
private val Context.shellDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "shell_settings",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)

/**
 * Stores shell settings in the application-shared `shell_settings` DataStore. Default-valued
 * choices are omitted so future defaults reach users who have not overridden them. Unknown enum
 * names fall back to defaults.
 *
 * Corruption replaces the file with empty preferences; IO read failures use defaults for that
 * collection so the root composition can still start.
 */
internal class DataStoreShellPreferences(context: Context) : ShellPreferences {

    private val dataStore = context.applicationContext.shellDataStore

    override val settings: Flow<ShellSettings> = dataStore.data
        .catch { throwable -> if (throwable is IOException) emit(emptyPreferences()) else throw throwable }
        .map { it.read() }
        .onEach { lastRead = it }

    override val lastKnown: ShellSettings? get() = lastRead

    override suspend fun update(transform: (ShellSettings) -> ShellSettings) {
        dataStore.edit { preferences ->
            val current = preferences.read()
            val next = transform(current)
            Stored.all.forEach { it.write(preferences, current, next) }
        }
    }

    private fun Preferences.read(): ShellSettings {
        val defaults = Defaults
        return ShellSettings(
            layoutMode = enum(Keys.layoutMode, defaults.layoutMode),
            topBarOverride = enum(Keys.topBarOverride, defaults.topBarOverride),
            tabTransition = enum(Keys.tabTransition, defaults.tabTransition),
            accessoryMode = enum(Keys.accessoryMode, defaults.accessoryMode),
            hideBottomBarOnScroll = this[Keys.hideBottomBarOnScroll] ?: defaults.hideBottomBarOnScroll,
            limitContentWidth = this[Keys.limitContentWidth] ?: defaults.limitContentWidth,
            animationSpeed = enum(Keys.animationSpeed, defaults.animationSpeed),
            backEdgeStyle = enum(Keys.backEdgeStyle, defaults.backEdgeStyle),
            startOverride = this[Keys.startOverride] ?: defaults.startOverride,
            navigationBarStyle = enum(Keys.navigationBarStyle, defaults.navigationBarStyle),
            navigationTint = enum(Keys.navigationTint, defaults.navigationTint),
            hideTopBarOnScroll = this[Keys.hideTopBarOnScroll] ?: defaults.hideTopBarOnScroll,
        )
    }

    private inline fun <reified E : Enum<E>> Preferences.enum(key: Preferences.Key<String>, default: E): E =
        this[key]?.let { name -> enumValues<E>().firstOrNull { it.name == name } } ?: default

    /** One setting's key, and how to take it from [ShellSettings] and store it. */
    private class Stored<T, S : Any>(
        val key: Preferences.Key<S>,
        val of: (ShellSettings) -> T,
        val encode: (T) -> S,
    ) {
        /** Writes the setting if it changed, and removes it once it is back at its default. */
        fun write(preferences: MutablePreferences, current: ShellSettings, next: ShellSettings) {
            val value = of(next)
            if (value == of(current)) return
            if (value == of(Defaults)) preferences.remove(key) else preferences[key] = encode(value)
        }

        companion object {
            private fun <E : Enum<E>> enum(key: Preferences.Key<String>, of: (ShellSettings) -> E) =
                Stored(key, of) { it.name }

            private fun flag(key: Preferences.Key<Boolean>, of: (ShellSettings) -> Boolean) =
                Stored(key, of) { it }

            val all: List<Stored<*, *>> = listOf(
                enum(Keys.layoutMode) { it.layoutMode },
                enum(Keys.topBarOverride) { it.topBarOverride },
                enum(Keys.tabTransition) { it.tabTransition },
                enum(Keys.accessoryMode) { it.accessoryMode },
                flag(Keys.hideBottomBarOnScroll) { it.hideBottomBarOnScroll },
                flag(Keys.limitContentWidth) { it.limitContentWidth },
                enum(Keys.animationSpeed) { it.animationSpeed },
                enum(Keys.backEdgeStyle) { it.backEdgeStyle },
                Stored(Keys.startOverride, { it.startOverride }) { it },
                enum(Keys.navigationBarStyle) { it.navigationBarStyle },
                enum(Keys.navigationTint) { it.navigationTint },
                flag(Keys.hideTopBarOnScroll) { it.hideTopBarOnScroll },
            )
        }
    }

    private object Keys {
        val layoutMode = stringPreferencesKey("layout_mode")
        val topBarOverride = stringPreferencesKey("top_bar_override")
        val tabTransition = stringPreferencesKey("tab_transition")
        val accessoryMode = stringPreferencesKey("accessory_mode")
        val hideBottomBarOnScroll = booleanPreferencesKey("hide_bottom_bar_on_scroll")
        val limitContentWidth = booleanPreferencesKey("limit_content_width")
        val animationSpeed = stringPreferencesKey("animation_speed")
        val backEdgeStyle = stringPreferencesKey("back_edge_style")
        val startOverride = intPreferencesKey("start_override")
        val navigationBarStyle = stringPreferencesKey("navigation_bar_style")
        val navigationTint = stringPreferencesKey("navigation_tint")
        val hideTopBarOnScroll = booleanPreferencesKey("hide_top_bar_on_scroll")
    }

    private companion object {
        val Defaults = ShellSettings()

        /** Shared by every instance, since they share the store. */
        @Volatile
        var lastRead: ShellSettings? = null
    }
}
