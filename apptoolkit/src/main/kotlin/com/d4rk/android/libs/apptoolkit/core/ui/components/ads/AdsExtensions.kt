package com.d4rk.android.libs.apptoolkit.core.ui.components.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore

/**
 * A Composable function that remembers and observes whether ads are enabled.
 *
 * This function retrieves the ads enabled status from [CommonDataStore] and collects it as a
 * state that recomposes the view when the value changes. It defaults to `true` if no value is set.
 *
 * @return `true` if ads are enabled, `false` otherwise. The value is lifecycle-aware.
 */
@Composable
fun rememberAdsEnabled(): Boolean {
    val context = LocalContext.current
    val dataStore: CommonDataStore = remember { CommonDataStore.getInstance(context) }
    return remember { dataStore.ads(default = true) }
        .collectAsStateWithLifecycle(initialValue = true).value
}