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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.analytics

internal object AppInteractionGa4Contract {
    const val EVENT = "app_card_interaction"
    const val SOURCE = "source"
    const val INTERACTION = "interaction"
    const val PACKAGE_NAME = "package_name"
    const val APP_NAME = "app_name"
    const val APP_CATEGORY_ID = "app_category_id"
    const val APP_CATEGORY_LABEL = "app_category_label"
    const val INTERACTION_CONTEXT = "interaction_context"
    val requiredParams = setOf(SOURCE, INTERACTION, PACKAGE_NAME, APP_NAME)
}
