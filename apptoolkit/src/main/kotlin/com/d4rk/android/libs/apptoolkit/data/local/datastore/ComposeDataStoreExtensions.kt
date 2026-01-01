package com.d4rk.android.libs.apptoolkit.data.local.datastore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Remembers a [CommonDataStore] scoped to the current composition.
 */
@Composable
fun rememberCommonDataStore(): CommonDataStore {
    val context = LocalContext.current
    return remember(context) { CommonDataStore.getInstance(context) }
}
