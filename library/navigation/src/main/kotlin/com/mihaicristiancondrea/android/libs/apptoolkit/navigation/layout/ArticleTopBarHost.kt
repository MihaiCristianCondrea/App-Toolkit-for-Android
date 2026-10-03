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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter

/**
 * Where a screen declares an article app bar: the scaffold around the screen holds one and draws
 * what the screen puts in it. Until a screen calls [ScaffoldArticleTopBar], the bar is the one the
 * destination registered.
 */
@Stable
class ArticleTopBarHost {
    private var owner: Any? by mutableStateOf(null)
    private var compactSource: () -> Boolean by mutableStateOf(NeverCompact)
    private var progressSource: (() -> Float)? by mutableStateOf(null)

    /** Whether a screen declares an article bar now. */
    val isDeclared: Boolean
        get() = owner != null

    /** The article's title, shown in the bar once it is [isCompact]. */
    var title: String by mutableStateOf("")
        private set

    /** The mark drawn before the title, as it is drawn, without a tint. Null for none. */
    var brand: Painter? by mutableStateOf(null)
        private set

    /** What a screen reader says for [brand]; null leaves it out as decoration. */
    var brandContentDescription: String? by mutableStateOf(null)
        private set

    /**
     * Whether the article's own header has scrolled away, so the bar shows the title. It follows
     * the scroll, so read it inside `derivedStateOf`.
     */
    val isCompact: Boolean
        get() = isDeclared && compactSource()

    /** Whether the screen reports how much of the article has been read. */
    val hasProgress: Boolean
        get() = isDeclared && progressSource != null

    /**
     * How much of the article has been read, from 0 to 1: the screen's value clamped, and 0 when
     * it is not a number or nothing reports it. It follows the scroll, so read it while drawing.
     */
    fun progress(): Float {
        val value = progressSource?.invoke() ?: return 0f
        return if (value.isNaN()) 0f else value.coerceIn(0f, 1f)
    }

    internal fun set(
        owner: Any,
        title: String,
        compact: () -> Boolean,
        progress: (() -> Float)?,
        brand: Painter?,
        brandContentDescription: String?,
    ) {
        this.owner = owner
        this.title = title
        this.compactSource = compact
        this.progressSource = progress
        this.brand = brand
        this.brandContentDescription = brandContentDescription
    }

    internal fun clear(owner: Any) {
        if (this.owner !== owner) return
        this.owner = null
        title = ""
        compactSource = NeverCompact
        progressSource = null
        brand = null
        brandContentDescription = null
    }
}

private val NeverCompact: () -> Boolean = { false }

/**
 * The [ArticleTopBarHost] of the scaffold around this point of the composition: a page's frame, a
 * tab screen's entry, or one pane of a list and its detail. Null outside them.
 */
val LocalArticleTopBarHost = staticCompositionLocalOf<ArticleTopBarHost?> { null }

/**
 * Turns the app bar of the scaffold around this screen into an article bar, as news apps draw over
 * a story: at first only the navigation and actions; once [compact], the [title] after an optional
 * [brand]; and, with [progress], a thin line along the bar's bottom edge showing how much has been
 * read. It follows the screen's state, since this is called from the screen, and leaves with it.
 *
 * ```
 * val listState = rememberLazyListState()
 * ScaffoldArticleTopBar(
 *     title = article.title,
 *     compact = { listState.firstVisibleItemIndex > 0 },
 *     progress = listState::readingProgress,
 *     brand = painterResource(R.drawable.publisher_logo),
 * )
 * ```
 *
 * A large bar is drawn small while the article is declared, a hidden one stays hidden, and a search
 * field in the bar takes the title's place. Outside a Toolkit scaffold it does nothing.
 *
 * @param compact Whether the article's own header has scrolled away. Read while drawing the bar, so
 * pass a lambda over the scroll state rather than a value, and the screen does not recompose as it
 * scrolls.
 * @param progress How much has been read, from 0 to 1; values outside it are clamped. Null draws no
 * line. See [readingProgress] for scroll states.
 * @param brand Drawn before the title at the title's height, keeping its own colours, so a
 * multicoloured logo or a vector drawable works. Null draws nothing and leaves no gap.
 * @param brandContentDescription What a screen reader says for [brand]; null for none.
 */
@Composable
fun ScaffoldArticleTopBar(
    title: String,
    compact: () -> Boolean,
    progress: (() -> Float)? = null,
    brand: Painter? = null,
    brandContentDescription: String? = null,
) {
    val host = LocalArticleTopBarHost.current ?: return
    val owner = remember { Any() }
    SideEffect { host.set(owner, title, compact, progress, brand, brandContentDescription) }
    DisposableEffect(host) { onDispose { host.clear(owner) } }
}
