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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ArticleReadingProgress
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ArticleTopBarHost
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ArticleTopBarTitle

/**
 * One small app bar over both panes, each half titled after the page under it. A pane that declares
 * an article bar shows the article's compact title in its half, with its reading progress along
 * that half's bottom edge only.
 */
@Composable
internal fun ListDetailTopBar(
    listChrome: PageChrome?,
    detailChrome: PageChrome?,
    listArticle: ArticleTopBarHost,
    detailArticle: ArticleTopBarHost?,
    listWidth: () -> Dp,
    onCloseList: () -> Unit,
    showsBack: Boolean,
    containerColor: Color,
    listTitleModifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.widthOf(listWidth).fillMaxHeight()) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Start)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // A list standing in for a tab has no way back but the navigation beside it.
                if (showsBack) {
                    // This module cannot depend on core UI, so it applies the bounce click locally.
                    IconButton(
                        onClick = {
                            onCloseList()
                        },
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .bounceClick(),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.shell_navigate_back))
                    }
                }
                PaneTitle(
                    title = listChrome?.title?.invoke().orEmpty(),
                    article = listArticle,
                    modifier = Modifier.padding(start = if (showsBack) 4.dp else 16.dp, end = 16.dp),
                    titleModifier = listTitleModifier,
                )
            }
            if (listArticle.isDeclared) {
                ArticleReadingProgress(listArticle, Modifier.align(Alignment.BottomStart))
            }
        }
        Spacer(Modifier.width(SeparatorWidth))
        AnimatedContent(
            targetState = detailChrome to detailArticle,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier.weight(1f),
            label = "DetailTitle",
        ) { (chrome, article) ->
            Box(Modifier.fillMaxHeight()) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.End)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PaneTitle(
                        title = chrome?.title?.invoke().orEmpty(),
                        article = article,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                    )
                    chrome?.actions?.invoke(this)
                }
                if (article?.isDeclared == true) {
                    ArticleReadingProgress(article, Modifier.align(Alignment.BottomStart))
                }
            }
        }
    }
}

/**
 * A pane's half of the shared bar: its page's [title], or the compact title of the article the
 * page declares. [titleModifier] applies to the page's title only.
 */
@Composable
private fun PaneTitle(
    title: String,
    article: ArticleTopBarHost?,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
) {
    if (article?.isDeclared == true) {
        Box(modifier, contentAlignment = Alignment.CenterStart) {
            ArticleTopBarTitle(article, textStyle = MaterialTheme.typography.titleLarge)
        }
    } else {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.then(titleModifier),
        )
    }
}
