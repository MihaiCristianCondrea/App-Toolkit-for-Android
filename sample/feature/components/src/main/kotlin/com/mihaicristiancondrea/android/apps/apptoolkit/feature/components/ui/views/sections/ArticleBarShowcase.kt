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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSurface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition

/** Opens the article app bar demo, with and without the sample publisher's mark. */
@Composable
fun ArticleBarShowcase(
    onLogEvent: (String, String?) -> Ga4EventData,
    onOpenArticleDemo: (Boolean) -> Unit,
) {
    val telemetryRepository = LocalTelemetry.current

    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_article_bar),
        icon = Icons.AutoMirrored.Filled.Article,
    )
    ShowcaseSection {
        ShowcaseSurface(position = GroupedItemPosition.SINGLE) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                OutlinedButton(
                    onClick = {
                        telemetryRepository.logGa4Event(onLogEvent("article_bar", "plain"))
                        onOpenArticleDemo(false)
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(id = R.string.components_article_without_brand))
                }
                FilledTonalButton(
                    onClick = {
                        telemetryRepository.logGa4Event(onLogEvent("article_bar", "brand"))
                        onOpenArticleDemo(true)
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(id = R.string.components_article_with_brand))
                }
            }
        }
    }
}
