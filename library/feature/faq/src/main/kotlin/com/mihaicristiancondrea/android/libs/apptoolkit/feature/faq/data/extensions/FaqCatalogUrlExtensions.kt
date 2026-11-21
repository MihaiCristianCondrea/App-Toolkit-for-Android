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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.extensions

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.boolean.toApiEnvironment

/**
 * Appends the build environment and catalog filename to a base URL without a trailing slash.
 *
 * @param isDebugBuild Selects the debug catalog when true and the release catalog otherwise.
 */
fun String.faqCatalogUrl(isDebugBuild: Boolean): String {
    val catalogEnvironment = isDebugBuild.toApiEnvironment()
    return "$this/$catalogEnvironment/catalog.json"
}
