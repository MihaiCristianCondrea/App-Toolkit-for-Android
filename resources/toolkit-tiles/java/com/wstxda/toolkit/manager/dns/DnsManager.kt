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

package com.wstxda.toolkit.manager.dns

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.wstxda.toolkit.permissions.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class DnsManager(context: Context) {

    companion object {
        private const val PRIVATE_DNS_MODE = "private_dns_mode"
        private const val PRIVATE_DNS_SPECIFIER = "private_dns_specifier"
        private const val MODE_OFF = "off"
        private const val MODE_AUTO = "opportunistic"
        private const val MODE_HOSTNAME = "hostname"
    }

    private val appContext = context.applicationContext
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val permissionManager = PermissionManager(appContext)

    val currentProvider: StateFlow<DnsProvider> = callbackFlow {
        trySend(getCurrentProviderInternal())

        val handler = Handler(Looper.getMainLooper())
        val observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                trySend(getCurrentProviderInternal())
            }
        }

        val resolver = appContext.contentResolver
        resolver.registerContentObserver(
            Settings.Global.getUriFor(PRIVATE_DNS_MODE), false, observer
        )
        resolver.registerContentObserver(
            Settings.Global.getUriFor(PRIVATE_DNS_SPECIFIER), false, observer
        )

        awaitClose {
            resolver.unregisterContentObserver(observer)
        }
    }.distinctUntilChanged().stateIn(
        scope = managerScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = getCurrentProviderInternal(),
    )

    fun hasPermission(): Boolean = permissionManager.hasWriteSecureSettingsPermission()

    fun cycleProvider() {
        val current = getCurrentProviderInternal()
        val providers = DnsProvider.entries.filter { it != DnsProvider.CUSTOM }
        val currentIndex = providers.indexOf(current).takeIf { it >= 0 } ?: -1
        val next = providers[(currentIndex + 1) % providers.size]
        applyProvider(next)
    }

    fun getCurrentProviderInternal(): DnsProvider {
        val resolver = appContext.contentResolver
        val mode =
            Settings.Global.getString(resolver, PRIVATE_DNS_MODE) ?: return DnsProvider.AUTOMATIC
        val hostname = Settings.Global.getString(resolver, PRIVATE_DNS_SPECIFIER) ?: ""

        return when (mode) {
            MODE_OFF -> DnsProvider.DISABLED
            MODE_AUTO -> DnsProvider.AUTOMATIC
            MODE_HOSTNAME -> DnsProvider.entries.firstOrNull {
                it.hostname.isNotEmpty() && it.hostname == hostname
            } ?: DnsProvider.CUSTOM

            else -> DnsProvider.AUTOMATIC
        }
    }

    fun getDisplayHostname(): String {
        if (getCurrentProviderInternal() != DnsProvider.CUSTOM) return ""
        return Settings.Global.getString(appContext.contentResolver, PRIVATE_DNS_SPECIFIER) ?: ""
    }

    private fun applyProvider(provider: DnsProvider) {
        try {
            val resolver = appContext.contentResolver
            when (provider) {
                DnsProvider.DISABLED -> Settings.Global.putString(
                    resolver, PRIVATE_DNS_MODE, MODE_OFF
                )

                DnsProvider.AUTOMATIC -> Settings.Global.putString(
                    resolver, PRIVATE_DNS_MODE, MODE_AUTO
                )

                else -> {
                    Settings.Global.putString(resolver, PRIVATE_DNS_SPECIFIER, provider.hostname)
                    Settings.Global.putString(resolver, PRIVATE_DNS_MODE, MODE_HOSTNAME)
                }
            }
        } catch (_: SecurityException) {
        }
    }

    fun cleanup() {
        managerScope.cancel()
    }
}