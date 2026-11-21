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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.views.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers.HorizontalWavyDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.transformers.splitAtThematicBreaks
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.transformers.toChangelogReleases
import dev.jeziellago.compose.markdowntext.MarkdownText

/** Renders the release history and initially positions it at the installed release when present. */
@Composable
internal fun ChangelogReleaseList(markdown: String, currentVersion: String) {
    key(markdown, currentVersion) {
        val releases = remember(key1 = markdown, key2 = currentVersion) {
            markdown.toChangelogReleases(currentVersion = currentVersion)
        }
        val currentIndex = releases.indexOfFirst { it.isCurrent }.coerceAtLeast(minimumValue = 0)
        val listState = rememberLazyListState(initialFirstVisibleItemIndex = currentIndex)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag(tag = "changelog_releases"),
            state = listState,
            contentPadding = PaddingValues(bottom = SizeConstants.LargeSize),
        ) {
            itemsIndexed(items = releases) { index, release ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (index > 0) {
                        HorizontalWavyDivider(modifier = Modifier.padding(vertical = SizeConstants.LargeSize))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(space = SizeConstants.SmallSize),
                    ) {
                        Column(modifier = Modifier.weight(weight = 1f)) {
                            if (release.heading.isNotEmpty()) {
                                Text(
                                    text = release.heading,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                            if (release.versionMarkdown.isNotEmpty()) {
                                MarkdownText(
                                    modifier = Modifier.fillMaxWidth(),
                                    markdown = release.versionMarkdown,
                                )
                            }
                        }
                        if (release.isCurrent) {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            ) {
                                Text(
                                    text = stringResource(id = R.string.changelog_current),
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(
                                        horizontal = SizeConstants.SmallSize,
                                        vertical = SizeConstants.ExtraTinySize,
                                    ),
                                )
                            }
                        }
                    }
                    release.markdown.splitAtThematicBreaks().forEachIndexed { partIndex, part ->
                        if (partIndex > 0) {
                            HorizontalWavyDivider(modifier = Modifier.padding(vertical = SizeConstants.LargeSize))
                        }
                        MarkdownText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (partIndex == 0 && release.heading.isNotEmpty()) {
                                        SizeConstants.LargeSize
                                    } else {
                                        0.dp
                                    },
                                ),
                            markdown = part,
                        )
                    }
                }
            }
        }
    }
}
