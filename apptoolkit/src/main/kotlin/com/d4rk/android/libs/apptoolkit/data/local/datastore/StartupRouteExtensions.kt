package com.d4rk.android.libs.apptoolkit.data.local.datastore

import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Maps the stored startup page to a stable navigation key.
 *
 * The mapping function allows apps to convert persisted string routes into their
 * own navigation key implementations while keeping the lookup reusable.
 */
fun <T : StableNavKey> CommonDataStore.startupDestinationFlow(
    defaultRoute: String,
    mapToKey: (String) -> T,
): Flow<T> = getStartupPage(default = defaultRoute).map { route ->
    val safeRoute = route.ifBlank { defaultRoute }
    mapToKey(safeRoute)
}
