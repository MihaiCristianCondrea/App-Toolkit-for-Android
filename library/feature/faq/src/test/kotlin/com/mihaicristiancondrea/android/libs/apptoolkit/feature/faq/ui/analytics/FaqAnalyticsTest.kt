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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FaqAnalyticsTest {

    @Test
    fun `screen identity preserves Help identity and FaqScreen class`() {
        assertEquals(expected = "Help", actual = FaqAnalytics.SCREEN.name)
        assertEquals(expected = "FaqScreen", actual = FaqAnalytics.SCREEN.className)
    }

    @Test
    fun `faqQuestionSelectEvent formats select_content with expected parameters`() {
        val event = faqQuestionSelectEvent(
            faqId = "question_3",
            position = 2,
            expanded = true,
        )

        assertEquals(expected = "select_content", actual = event.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = "Help"),
            actual = event.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = FaqContentTypes.FAQ_ITEM),
            actual = event.params[FaqParams.CONTENT_TYPE],
        )
        assertEquals(expected = AnalyticsValue.Str(value = "question_3"), actual = event.params[FaqParams.ITEM_ID])
        assertEquals(expected = AnalyticsValue.LongVal(value = 2L), actual = event.params[FaqParams.FAQ_POSITION])
        assertEquals(expected = AnalyticsValue.Bool(value = true), actual = event.params[FaqParams.EXPANDED])
    }

    @Test
    fun `faqSelectContentEvent formats select_content for feedback and support`() {
        val feedbackEvent = faqSelectContentEvent(
            contentType = FaqContentTypes.FEEDBACK_OPTION,
            itemId = FaqItemIds.REQUEST_FEATURE,
        )

        assertEquals(expected = "select_content", actual = feedbackEvent.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = "Help"),
            actual = feedbackEvent.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = FaqContentTypes.FEEDBACK_OPTION),
            actual = feedbackEvent.params[FaqParams.CONTENT_TYPE],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = FaqItemIds.REQUEST_FEATURE),
            actual = feedbackEvent.params[FaqParams.ITEM_ID],
        )

        val contactEvent = faqSelectContentEvent(
            contentType = FaqContentTypes.SUPPORT,
            itemId = FaqItemIds.CONTACT_US,
        )

        assertEquals(expected = "select_content", actual = contactEvent.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = "Help"),
            actual = contactEvent.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = FaqContentTypes.SUPPORT),
            actual = contactEvent.params[FaqParams.CONTENT_TYPE],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = FaqItemIds.CONTACT_US),
            actual = contactEvent.params[FaqParams.ITEM_ID],
        )
    }

    @Test
    fun `faqActionEvent formats settings_action with action name and Help screen`() {
        val event = faqActionEvent(actionName = FaqActionNames.FEEDBACK_SHEET_OPENED)

        assertEquals(expected = "settings_action", actual = event.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = "Help"),
            actual = event.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = FaqActionNames.FEEDBACK_SHEET_OPENED),
            actual = event.params[SettingsAnalytics.Params.ACTION_NAME],
        )
    }

    @Test
    fun `all action names are unique and match GA4 naming conventions`() {
        val actions = listOf(
            FaqActionNames.FEEDBACK_SHEET_OPENED,
            FaqActionNames.FEEDBACK_SHEET_DISMISSED,
            FaqActionNames.SHOW_MORE_QUESTIONS,
            FaqActionNames.RETRY_LOAD,
            FaqActionNames.STORE_LISTING_OPENED,
            FaqActionNames.MENU_PLAY_STORE,
            FaqActionNames.MENU_VERSION_INFO,
            FaqActionNames.MENU_BETA_PROGRAM,
            FaqActionNames.MENU_TERMS_OF_SERVICE,
            FaqActionNames.MENU_PRIVACY_POLICY,
            FaqActionNames.MENU_LICENSES,
        )

        assertEquals(expected = actions.distinct().size, actual = actions.size, message = "Action names must be unique")

        val validIdentifierRegex = Regex(pattern = "^[a-z][a-z0-9_]{1,39}$")
        for (action in actions) {
            assertTrue(
                actual = validIdentifierRegex.matches(input = action),
                message = "Action name '$action' must be valid GA4 parameter format",
            )
        }
    }

    @Test
    fun `events never contain sensitive or unbounded parameter keys`() {
        val forbiddenKeys = setOf("email", "user_id", "message", "query", "url", "path")
        val selectEvent = faqQuestionSelectEvent(faqId = "q1", position = 0, expanded = true)
        val actionEvent = faqActionEvent(actionName = FaqActionNames.MENU_TERMS_OF_SERVICE)

        for (key in forbiddenKeys) {
            assertFalse(actual = selectEvent.params.containsKey(key = key))
            assertFalse(actual = actionEvent.params.containsKey(key = key))
        }
    }
}
