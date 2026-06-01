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

package com.wstxda.toolkit.manager.nfc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.NfcManager as SystemNfcManager
import com.wstxda.toolkit.permissions.PermissionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NfcManager(context: Context) {

    private val appContext = context.applicationContext
    private val permissionManager = PermissionManager(appContext)
    private val systemNfcManager =
        appContext.getSystemService(Context.NFC_SERVICE) as? SystemNfcManager
    private val nfcAdapter: NfcAdapter? = systemNfcManager?.defaultAdapter
    val hasHardware: Boolean = nfcAdapter != null

    private val _isEnabled = MutableStateFlow(getCurrentSystemMode())
    val isEnabled = _isEnabled.asStateFlow()

    private var isListening = false

    private val receiverObserver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) {
                syncStateWithSystem()
            }
        }
    }

    private fun getCurrentSystemMode(): Boolean {
        if (nfcAdapter == null) return false
        return try {
            nfcAdapter.isEnabled
        } catch (_: Exception) {
            false
        }
    }

    private fun syncStateWithSystem() {
        if (!hasHardware) return
        val systemState = getCurrentSystemMode()
        if (_isEnabled.value != systemState) {
            _isEnabled.value = systemState
        }
    }

    fun start() {
        if (isListening || !hasHardware) return
        syncStateWithSystem()
        appContext.registerReceiver(
            receiverObserver, IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)
        )
        isListening = true
    }

    fun stop() {
        if (!isListening) return
        try {
            appContext.unregisterReceiver(receiverObserver)
        } catch (_: Exception) {
        }
        isListening = false
    }

    fun cleanup() {
        stop()
    }

    fun hasPermission(): Boolean = permissionManager.hasWriteSecureSettingsPermission()

    fun toggle() {
        if (!hasHardware || !hasPermission()) return
        val newState = !_isEnabled.value

        if (setSystemMode(newState)) {
            _isEnabled.value = newState
        }
    }

    private fun setSystemMode(enable: Boolean): Boolean {
        if (nfcAdapter == null) return false
        return try {
            val method = nfcAdapter.javaClass.getDeclaredMethod(if (enable) "enable" else "disable")
            method.isAccessible = true
            val success = method.invoke(nfcAdapter) as? Boolean ?: true
            success
        } catch (_: Exception) {
            false
        }
    }
}