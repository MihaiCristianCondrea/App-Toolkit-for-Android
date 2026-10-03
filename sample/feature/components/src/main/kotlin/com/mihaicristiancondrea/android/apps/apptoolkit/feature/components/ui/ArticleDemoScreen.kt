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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ScaffoldArticleTopBar
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.readingProgress

/**
 * A made-up article that turns its page's app bar into an article bar. The bar shows only its
 * buttons until the headline scrolls away, then the headline, after the sample publisher's mark
 * when [branded], with the reading progress along its bottom edge.
 */
@Composable
fun ArticleDemoScreen(branded: Boolean) {
    val listState = rememberLazyListState()
    val headline = stringResource(id = R.string.components_article_headline)
    val paragraph = stringResource(id = R.string.components_article_paragraph)
    val brand = if (branded) painterResource(id = R.drawable.components_article_brand) else null
    ScaffoldArticleTopBar(
        title = headline,
        compact = { listState.firstVisibleItemIndex > 0 },
        progress = listState::readingProgress,
        brand = brand,
        brandContentDescription = if (branded) stringResource(id = R.string.components_article_brand) else null,
    )
    LazyColumn(state = listState, contentPadding = contentPadding()) {
        item(key = "headline") {
            Column(
                modifier = Modifier.padding(all = SizeConstants.LargeSize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                if (brand != null) {
                    Image(painter = brand, contentDescription = null, modifier = Modifier.height(32.dp))
                }
                Text(text = headline, style = MaterialTheme.typography.headlineMedium)
            }
        }
        items(count = ParagraphCount) {
            Text(
                text = paragraph,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = SizeConstants.LargeSize, vertical = SizeConstants.SmallSize),
            )
        }
    }
}

private const val ParagraphCount = 16
