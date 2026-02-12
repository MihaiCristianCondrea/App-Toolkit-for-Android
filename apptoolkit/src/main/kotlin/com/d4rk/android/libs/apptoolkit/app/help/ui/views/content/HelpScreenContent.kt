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

package com.d4rk.android.libs.apptoolkit.app.help.ui.views.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.cards.ContactUsCard
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.lists.HelpQuestionsList
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.HelpNativeAdCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.sendEmailToDeveloper
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun HelpScreenContent(
    questions: ImmutableList<FaqItem>,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val adsConfig: AdsConfig = koinInject(qualifier = named("help_large_banner_ad"))

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding(),
            start = SizeConstants.LargeSize,
            end = SizeConstants.LargeSize
        ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)
    ) {
        item {
            Text(text = stringResource(id = R.string.popular_help_resources))
        }

        item {
            HelpQuestionsList(questions = questions)
        }

        item {
            HelpNativeAdCard(
                adUnitId = adsConfig.bannerAdUnitId,
                modifier = Modifier.animateItem()
            )
        }

        item {
            ContactUsCard(
                onClick = {
                    context.sendEmailToDeveloper(
                        applicationNameRes = R.string.app_name
                    )
                }
            )
            repeat(3) { ExtraLargeVerticalSpacer() }
        }
    }
}
