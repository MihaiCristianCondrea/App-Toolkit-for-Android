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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui.views.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.domain.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui.views.cards.ContactUsCard
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui.views.cards.QuestionCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.sendEmailToDeveloper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.HelpNativeAdCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.R
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

private const val INITIAL_VISIBLE_QUESTION_COUNT = 5

@Composable
fun HelpScreenContent(
    questions: ImmutableList<FaqItem>,
    paddingValues: PaddingValues,
) {
    val firebaseController: FirebaseController = koinInject()
    val context = LocalContext.current
    val adsConfig: AdsConfig = koinInject(qualifier = named(AdsQualifiers.HELP_LARGE_BANNER_AD))
    val adsEnabled = rememberAdsEnabled()
    val hasAdSlot = adsEnabled && adsConfig.bannerAdUnitId.isNotBlank()
    var showAllQuestions by rememberSaveable { mutableStateOf(false) }
    var isAdLoaded by remember { mutableStateOf(false) }
    val visibleQuestions = if (showAllQuestions) {
        questions
    } else {
        questions.take(INITIAL_VISIBLE_QUESTION_COUNT)
    }
    val hasHiddenQuestions = visibleQuestions.size < questions.size
    val popularGroupItemCount = visibleQuestions.size + if (hasHiddenQuestions) 1 else 0
    val supportGroupItemCount = 1 + if (hasAdSlot && isAdLoaded) 1 else 0

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
            Text(
                text = stringResource(id = R.string.popular_help_resources)
            )
        }

        visibleQuestions.forEachIndexed { index: Int, question: FaqItem ->
            item(key = question.id.value) {
                var isExpanded by rememberSaveable(question.id.value) {
                    mutableStateOf(false)
                }
                QuestionCard(
                    title = question.question,
                    summary = question.answer,
                    isExpanded = isExpanded,
                    groupedPosition = groupedItemPosition(
                        index = index,
                        size = popularGroupItemCount,
                    ),
                    onToggleExpand = {
                        val expanded = !isExpanded
                        firebaseController.logGa4Event(
                            helpPreferenceTapEvent(
                                preferenceKey = "faq_item",
                                faqId = question.id.value,
                                faqPosition = index,
                                expanded = expanded,
                            )
                        )
                        isExpanded = expanded
                    },
                    modifier = Modifier.animateVisibility(index = index),
                )
            }
        }

        if (hasHiddenQuestions) {
            item(key = "show_more_questions") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .groupedCorners(
                            position = groupedItemPosition(
                                index = popularGroupItemCount - 1,
                                size = popularGroupItemCount,
                            ),
                            outerRadius = SizeConstants.ExtraLargeIncreasedSize,
                        ),
                    shape = RectangleShape,
                    onClick = {
                        firebaseController.logGa4Event(
                            helpPreferenceTapEvent(preferenceKey = "show_more_questions")
                        )
                        showAllQuestions = true
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = SizeConstants.LargeSize,
                                vertical = SizeConstants.MediumSize,
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.show_more),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Icon(
                            imageVector = Icons.Filled.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.padding(start = SizeConstants.SmallSize),
                        )
                    }
                }
            }
        }

        item {
            Text(text = stringResource(id = R.string.need_more_help))
        }

        if (hasAdSlot) {
            item {
                HelpNativeAdCard(
                    adUnitId = adsConfig.bannerAdUnitId,
                    groupedPosition = groupedItemPosition(
                        index = 0,
                        size = supportGroupItemCount,
                    ),
                    modifier = Modifier.animateItem(),
                    onAdLoaded = { isAdLoaded = it },
                )
            }
        }

        item {
            ContactUsCard(
                groupedPosition = groupedItemPosition(
                    index = supportGroupItemCount - 1,
                    size = supportGroupItemCount,
                ),
                onClick = {
                    firebaseController.logEvent(
                        helpPreferenceTapEvent(preferenceKey = "contact_us").toAnalyticsEvent()
                    )
                    context.sendEmailToDeveloper(
                        applicationNameRes = R.string.app_name
                    )
                }
            )
            repeat(3) { ExtraLargeVerticalSpacer() }
        }
    }
}


private fun helpPreferenceTapEvent(
    preferenceKey: String,
    faqId: String? = null,
    faqPosition: Int? = null,
    expanded: Boolean? = null,
): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = buildMap {
            put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str("Help"))
            put(SettingsAnalytics.Params.PREFERENCE_KEY, AnalyticsValue.Str(preferenceKey))
            faqId?.let { put(SettingsAnalytics.Params.FAQ_ID, AnalyticsValue.Str(it)) }
            faqPosition?.let {
                put(
                    SettingsAnalytics.Params.FAQ_POSITION,
                    AnalyticsValue.LongVal(it.toLong())
                )
            }
            expanded?.let { put(SettingsAnalytics.Params.EXPANDED, AnalyticsValue.Bool(it)) }
        },
    )
}

