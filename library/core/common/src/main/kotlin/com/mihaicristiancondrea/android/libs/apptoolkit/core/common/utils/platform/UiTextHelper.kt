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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

/**
 * Text that remains unresolved until rendering, allowing state holders to supply literal text,
 * string resources, or plural resources without retaining a [Context].
 */
sealed class UiTextHelper {
    data class DynamicString(val content: String) : UiTextHelper()
    data class StringResource(val resourceId: Int, val arguments: List<Any> = emptyList()) :
        UiTextHelper()

    /**
     * A plural resource whose [count] selects the quantity. Include [count] in [arguments] as
     * well if the text displays it.
     */
    data class PluralResource(
        val resourceId: Int,
        val count: Int,
        val arguments: List<Any> = emptyList(),
    ) : UiTextHelper()

    /**
     * Resolves text using the supplied context and its current resource configuration.
     *
     * @throws android.content.res.Resources.NotFoundException if the string or plural resource
     * is missing.
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> content
            is StringResource -> context.getString(resourceId, *arguments.toTypedArray())
            is PluralResource -> context.resources.getQuantityString(
                resourceId,
                count,
                *arguments.toTypedArray(),
            )
        }
    }

    /**
     * Resolves text against the current composition configuration, including locale changes.
     *
     * @throws android.content.res.Resources.NotFoundException if the string or plural resource
     * is missing.
     * @throws java.util.MissingFormatArgumentException if required format arguments are absent.
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> content
            is StringResource -> stringResource(resourceId, *arguments.toTypedArray())
            is PluralResource -> pluralStringResource(resourceId, count, *arguments.toTypedArray())
        }
    }
}
