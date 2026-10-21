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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

/**
 * [items] whose every item enters with the screen's cascade, in list order, and moves with
 * `animateItem` when the list changes: a list's whole entrance in one call.
 *
 * ```kotlin
 * LazyColumn {
 *     animatedItems(apps, key = { it.packageName }) { app -> AppRow(app) }
 * }
 * ```
 *
 * Toolkit components inside an item, such as preference rows, do not enter a second time.
 */
fun <T> LazyListScope.animatedItems(
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) = animatedItemsIndexed(
    items = items,
    key = key?.let { itemKey -> { _: Int, item: T -> itemKey(item) } },
    contentType = { _, item -> contentType(item) },
) { _, item -> itemContent(item) }

/** [animatedItems] with each item's index. */
fun <T> LazyListScope.animatedItemsIndexed(
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
) {
    items(
        count = items.size,
        key = key?.let { itemKey -> { index: Int -> itemKey(index, items[index]) } },
        contentType = { index -> contentType(index, items[index]) },
    ) { index ->
        val scope: LazyItemScope = this
        EnteringItem(Modifier.animateItem().animateVisibility(index = index)) { scope.itemContent(index, items[index]) }
    }
}

/** [items] of a grid, entering as [LazyListScope.animatedItems] does. */
fun <T> LazyGridScope.animatedItems(
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
    contentType: (item: T) -> Any? = { null },
    itemContent: @Composable LazyGridItemScope.(item: T) -> Unit,
) = animatedItemsIndexed(
    items = items,
    key = key?.let { itemKey -> { _: Int, item: T -> itemKey(item) } },
    span = span?.let { itemSpan -> { _: Int, item: T -> itemSpan(item) } },
    contentType = { _, item -> contentType(item) },
) { _, item -> itemContent(item) }

/** [animatedItems] of a grid with each item's index. */
fun <T> LazyGridScope.animatedItemsIndexed(
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    span: (LazyGridItemSpanScope.(index: Int, item: T) -> GridItemSpan)? = null,
    contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    itemContent: @Composable LazyGridItemScope.(index: Int, item: T) -> Unit,
) {
    items(
        count = items.size,
        key = key?.let { itemKey -> { index: Int -> itemKey(index, items[index]) } },
        span = span?.let { itemSpan -> { index: Int -> itemSpan(index, items[index]) } },
        contentType = { index -> contentType(index, items[index]) },
    ) { index ->
        val scope: LazyGridItemScope = this
        EnteringItem(Modifier.animateItem().animateVisibility(index = index)) { scope.itemContent(index, items[index]) }
    }
}

/**
 * One [LazyListScope.item] that enters with the screen's cascade, in its place in the list, and
 * moves with `animateItem`: a header, a picker or a card among a list's items.
 */
fun LazyListScope.animatedItem(
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable LazyItemScope.() -> Unit,
) {
    item(key = key, contentType = contentType) {
        val scope: LazyItemScope = this
        EnteringItem(Modifier.animateItem().animateVisibility()) { scope.content() }
    }
}

/** One grid item entering as [LazyListScope.animatedItem] does. */
fun LazyGridScope.animatedItem(
    key: Any? = null,
    span: (LazyGridItemSpanScope.() -> GridItemSpan)? = null,
    contentType: Any? = null,
    content: @Composable LazyGridItemScope.() -> Unit,
) {
    item(key = key, span = span, contentType = contentType) {
        val scope: LazyGridItemScope = this
        EnteringItem(Modifier.animateItem().animateVisibility()) { scope.content() }
    }
}

/** An item that brings [content] in, telling the Toolkit components inside not to enter again. */
@Composable
private fun EnteringItem(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier, propagateMinConstraints = true) {
        CompositionLocalProvider(LocalInsideEntrance provides true, content = content)
    }
}

/** Whether an element around this point already enters, so the components inside do not. */
internal val LocalInsideEntrance = staticCompositionLocalOf { false }
