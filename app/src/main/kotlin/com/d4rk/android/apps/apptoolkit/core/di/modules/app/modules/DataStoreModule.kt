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

package com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules

import com.d4rk.android.apps.apptoolkit.core.data.local.datastore.DataStore
import com.d4rk.android.apps.apptoolkit.core.data.local.datastore.DatastoreInterface
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Host datastore bindings.
 *
 * Change rationale:
 * - Before: app layers consumed [com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore] directly.
 * - Now: app DI exposes [DatastoreInterface] backed by [DataStore], keeping app contracts app-owned.
 * - Better because host data/domain layers are decoupled from shared datastore implementation details.
 */
val dataStoreModule: Module = module {
    single<DataStore> { DataStore(commonDataStore = get()) }
    single<DatastoreInterface> { get<DataStore>() }
}
