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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FirebaseAnalyticsExtensionsTest {

    private lateinit var telemetryRepository: FakeTelemetryRepository

    @BeforeEach
    fun setUp() {
        telemetryRepository = FakeTelemetryRepository()
    }

    @Test
    fun `logTutorialBegin logs tutorial_begin event`() {
        telemetryRepository.logTutorialBegin()

        assertEquals(1, telemetryRepository.loggedEvents.size)
        val event = telemetryRepository.loggedEvents.first()
        assertEquals("tutorial_begin", event.name)
        assertEquals(emptyMap(), event.params)
    }

    @Test
    fun `logTutorialComplete logs tutorial_complete event`() {
        telemetryRepository.logTutorialComplete()

        assertEquals(1, telemetryRepository.loggedEvents.size)
        val event = telemetryRepository.loggedEvents.first()
        assertEquals("tutorial_complete", event.name)
        assertEquals(emptyMap(), event.params)
    }

    @Test
    fun `logSearch logs search event with search_term parameter`() {
        telemetryRepository.logSearch("android architecture")

        assertEquals(1, telemetryRepository.loggedEvents.size)
        val event = telemetryRepository.loggedEvents.first()
        assertEquals("search", event.name)
        assertEquals(
            mapOf("search_term" to AnalyticsValue.Str("android architecture")),
            event.params,
        )
    }

    @Test
    fun `logSearch ignores blank search terms`() {
        telemetryRepository.logSearch("   ")

        assertEquals(0, telemetryRepository.loggedEvents.size)
    }

    @Test
    fun `logSelectContent logs select_content event with content_type and item_id`() {
        telemetryRepository.logSelectContent(contentType = "button", itemId = "submit_feedback")

        assertEquals(1, telemetryRepository.loggedEvents.size)
        val event = telemetryRepository.loggedEvents.first()
        assertEquals("select_content", event.name)
        assertEquals(
            mapOf(
                "content_type" to AnalyticsValue.Str("button"),
                "item_id" to AnalyticsValue.Str("submit_feedback"),
            ),
            event.params,
        )
    }

    @Test
    fun `logShare logs share event with method, content_type, and item_id`() {
        telemetryRepository.logShare(
            method = "system_share",
            contentType = "app",
            itemId = "com.example.app",
        )

        assertEquals(1, telemetryRepository.loggedEvents.size)
        val event = telemetryRepository.loggedEvents.first()
        assertEquals("share", event.name)
        assertEquals(
            mapOf(
                "method" to AnalyticsValue.Str("system_share"),
                "content_type" to AnalyticsValue.Str("app"),
                "item_id" to AnalyticsValue.Str("com.example.app"),
            ),
            event.params,
        )
    }

    @Test
    fun `logViewItem logs view_item event with item_id, item_name, and optional item_category`() {
        telemetryRepository.logViewItem(
            itemId = "com.example.app",
            itemName = "Example App",
            itemCategory = "tools",
        )

        assertEquals(1, telemetryRepository.loggedEvents.size)
        val event = telemetryRepository.loggedEvents.first()
        assertEquals("view_item", event.name)
        assertEquals(
            mapOf(
                "item_id" to AnalyticsValue.Str("com.example.app"),
                "item_name" to AnalyticsValue.Str("Example App"),
                "item_category" to AnalyticsValue.Str("tools"),
            ),
            event.params,
        )
    }

    @Test
    fun `logViewItemList logs view_item_list event with list params`() {
        telemetryRepository.logViewItemList(
            itemListId = "popular_apps",
            itemListName = "Popular Developer Apps",
        )

        assertEquals(1, telemetryRepository.loggedEvents.size)
        val event = telemetryRepository.loggedEvents.first()
        assertEquals("view_item_list", event.name)
        assertEquals(
            mapOf(
                "item_list_id" to AnalyticsValue.Str("popular_apps"),
                "item_list_name" to AnalyticsValue.Str("Popular Developer Apps"),
            ),
            event.params,
        )
    }

    @Test
    fun `logUnlockAchievement logs unlock_achievement event with achievement_id`() {
        telemetryRepository.logUnlockAchievement("showcase_unlocked")

        assertEquals(1, telemetryRepository.loggedEvents.size)
        val event = telemetryRepository.loggedEvents.first()
        assertEquals("unlock_achievement", event.name)
        assertEquals(
            mapOf("achievement_id" to AnalyticsValue.Str("showcase_unlocked")),
            event.params,
        )
    }
}
