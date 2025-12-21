package com.d4rk.android.libs.apptoolkit.app.main.domain.repository

import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import kotlinx.coroutines.flow.Flow

interface NavigationRepository {
    fun getNavigationDrawerItems(): Flow<List<NavigationDrawerItem>>
}

