package com.d4rk.android.libs.apptoolkit.data.datastore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberCommonDataStore(): CommonDataStore {
    val context = LocalContext.current
    return remember(context) { CommonDataStore.getInstance(context) }
}
