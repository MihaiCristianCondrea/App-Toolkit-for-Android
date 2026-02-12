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

package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * UiTextHelper is a sealed class that represents text that can be either a dynamic string or a string resource.
 * It provides methods to convert the text to a String in both a Context-based and a Composable context.
 *
 * This class is useful for handling text that might come from different sources, like hardcoded strings or
 * string resources, without having to worry about the specific type when using it.
 */
sealed class UiTextHelper {
    data class DynamicString(val content: String) : UiTextHelper()
    data class StringResource(val resourceId: Int, val arguments: List<Any> = emptyList()) :
        UiTextHelper()

    /**
     * Converts the current object to a String.
     *
     * This function handles two different types: `DynamicString` and `StringResource`.
     *
     * - If the object is a `DynamicString`, it returns the `content` directly.
     * - If the object is a `StringResource`, it retrieves the string from the Android resources
     *   using the provided `context` and the `resourceId`, substituting any format arguments
     *   if present.
     *
     * @param context The Android context used to retrieve string resources.
     * @return The string representation of the object.
     * @throws android.content.res.Resources.NotFoundException If the resourceId is not found in the resources.
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> content
            is StringResource -> context.getString(resourceId, *arguments.toTypedArray())
        }
    }

    /**
     * Converts a [UiTextHelper] to a plain String.
     *
     * This function is a Composable extension function on the [UiTextHelper] sealed class.
     * It provides a way to get the actual string value represented by the [UiTextHelper].
     *
     * There are two types of [UiTextHelper] supported:
     * - [DynamicString]: Represents a string that is directly provided.
     * - [StringResource]: Represents a string that is loaded from Android resources.
     *
     * For [DynamicString], it simply returns the stored string.
     * For [StringResource], it uses the Android Context to fetch the string from the resources
     * using the provided resource ID and arguments.
     *
     * @return The String representation of the [UiTextHelper].
     *
     * @throws android.content.res.Resources.NotFoundException if a [StringResource] is used and the resource ID does not exist.
     * @throws java.util.MissingFormatArgumentException if a [StringResource] is used with format arguments and not all of them are provided.
     *
     * @see DynamicString
     * @see StringResource
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> content
            is StringResource -> stringResource(resourceId, *arguments.toTypedArray())
        }
    }
}
