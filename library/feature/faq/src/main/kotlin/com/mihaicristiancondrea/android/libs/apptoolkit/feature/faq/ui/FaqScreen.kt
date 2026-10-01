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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openPlayStoreForApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.sendEmailToDeveloper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

// The analytics screen name stays "Help": the destination is still "Help & feedback" to the
// user, and changing it would split this screen's history in GA4 at the module rename.
private const val FAQ_SCREEN_NAME: String = "Help"
private const val FAQ_SCREEN_CLASS: String = "FaqScreen"

private object FaqPreferenceKeys {
    const val FEEDBACK: String = "feedback"
    const val REQUEST_FEATURE: String = "request_feature"
    const val LEAVE_REVIEW: String = "leave_review"
    const val FAQ_ITEM: String = "faq_item"
    const val SHOW_MORE_QUESTIONS: String = "show_more_questions"
    const val CONTACT_US: String = "contact_us"
}

private object FaqActionNames {
    const val RETRY_LOAD: String = "retry_load"
    const val FEEDBACK_SHEET_OPENED: String = "feedback_sheet_opened"
}

/**
 * Help and feedback: the questions, a contact card and a button that opens the feedback sheet.
 *
 * The body of the help page, which `helpPage()` registers with `FaqMenuActions` in its app bar.
 * This is the stateful half. It owns the [FaqViewModel], tracks the screen, logs each tap, opens
 * links, mail and the store listing, and hands the rendering to [FaqScreenContent].
 */
@Composable
fun FaqScreen() {
    val viewModel: FaqViewModel = koinViewModel()
    val state: FaqUiState by viewModel.state.collectAsStateWithLifecycle()
    val firebaseController: FirebaseController = koinInject()
    val adsConfig: AdsConfig = koinInject(qualifier = named(AdsQualifiers.HELP_NATIVE_AD))
    val adsEnabled: Boolean = rememberAdsEnabled()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val reviewHost: ReviewHost? = remember(activity) { activity?.let(::ReviewHost) }

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = FAQ_SCREEN_NAME,
        screenClass = FAQ_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = FAQ_SCREEN_NAME,
        state = state.questions,
    )

    LaunchedEffect(state.openStoreListing) {
        if (state.openStoreListing) {
            context.openPlayStoreForApp(context.packageName)
            viewModel.onEvent(FaqEvent.StoreListingOpened)
        }
    }

    FaqScreenContent(
        state = state,
        onRetry = {
            firebaseController.logEvent(faqActionEvent(actionName = FaqActionNames.RETRY_LOAD))
            viewModel.onEvent(FaqEvent.Load)
        },
        onFeedbackOpened = {
            firebaseController.logGa4Event(faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.FEEDBACK))
            firebaseController.logEvent(faqActionEvent(actionName = FaqActionNames.FEEDBACK_SHEET_OPENED))
        },
        onRequestFeature = {
            firebaseController.logGa4Event(faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.REQUEST_FEATURE))
            context.openUrl(AppLinks.FEATURE_REQUESTS_FORM)
        },
        onLeaveReview = {
            firebaseController.logGa4Event(faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.LEAVE_REVIEW))
            reviewHost?.let { host -> viewModel.onEvent(FaqEvent.RequestReview(host = host)) }
        },
        onQuestionToggled = { question, position, expanded ->
            firebaseController.logGa4Event(
                faqPreferenceTapEvent(
                    preferenceKey = FaqPreferenceKeys.FAQ_ITEM,
                    faqId = question.id.value,
                    faqPosition = position,
                    expanded = expanded,
                )
            )
        },
        onShowMoreQuestions = {
            firebaseController.logGa4Event(
                faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.SHOW_MORE_QUESTIONS)
            )
        },
        onContactUs = {
            firebaseController.logEvent(
                faqPreferenceTapEvent(preferenceKey = FaqPreferenceKeys.CONTACT_US).toAnalyticsEvent()
            )
            context.sendEmailToDeveloper(applicationNameRes = CommonR.string.app_name)
        },
        contentPadding = contentPadding(),
        adUnitId = adsConfig.bannerAdUnitId.takeIf { adsEnabled && it.isNotBlank() },
    )

    MessageHost(viewModel = viewModel)
}

private fun faqPreferenceTapEvent(
    preferenceKey: String,
    faqId: String? = null,
    faqPosition: Int? = null,
    expanded: Boolean? = null,
): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = buildMap {
            put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(FAQ_SCREEN_NAME))
            put(SettingsAnalytics.Params.PREFERENCE_KEY, AnalyticsValue.Str(preferenceKey))
            faqId?.let { put(SettingsAnalytics.Params.FAQ_ID, AnalyticsValue.Str(it)) }
            faqPosition?.let { put(SettingsAnalytics.Params.FAQ_POSITION, AnalyticsValue.LongVal(it.toLong())) }
            expanded?.let { put(SettingsAnalytics.Params.EXPANDED, AnalyticsValue.Bool(it)) }
        },
    )
}

private fun faqActionEvent(actionName: String): AnalyticsEvent {
    return AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(FAQ_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}
