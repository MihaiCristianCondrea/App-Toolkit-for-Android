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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4ContractValidator
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.MorsePlaybackState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.StandardDispatcherExtension
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ToolAnalyticsTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = StandardDispatcherExtension()
    }

    private val firebaseController = FakeFirebaseController()

    private fun toolUses(): List<String> = firebaseController.loggedEvents
        .filter { it.name == AppGa4Contract.EventName.TOOL_USED }
        .map { (it.params.getValue(AppGa4Contract.Param.TOOL_ID) as AnalyticsValue.Str).value }

    @Test
    fun `a tool is reported used once per opening, however often it is used`() {
        val viewModel = CoinFlipToolViewModel(firebaseController)

        repeat(times = 5) { viewModel.flip() }

        assertEquals(listOf(ToolkitTileIds.COIN_FLIP), toolUses())
    }

    @Test
    fun `reopening a tool reports its use again`() {
        val viewModel = DiceRollToolViewModel(firebaseController)

        viewModel.roll()
        viewModel.dismiss()
        viewModel.roll()

        assertEquals(listOf(ToolkitTileIds.DICE_ROLL, ToolkitTileIds.DICE_ROLL), toolUses())
    }

    @Test
    fun `opening a tool without using it reports nothing`() {
        val viewModel = CoinFlipToolViewModel(firebaseController)

        viewModel.dismiss()

        assertTrue(firebaseController.loggedEvents.isEmpty())
    }

    @Test
    fun `a Morse message that cannot be sent is not use`() {
        val morse: MorseRepository = mockk(relaxed = true) {
            every { state } returns MutableStateFlow(MorsePlaybackState())
        }
        val viewModel = MorseToolViewModel(morse, firebaseController)

        viewModel.updateInput(input = "   ")
        viewModel.toggle()
        assertTrue(toolUses().isEmpty())

        viewModel.updateInput(input = "HELLO")
        viewModel.toggle()
        assertEquals(listOf(ToolkitTileIds.MORSE), toolUses())
    }

    @Test
    fun `stopping SOS is not reported as another use`() {
        var active = false
        val sos: SosRepository = mockk(relaxed = true) {
            every { isActive } answers { active }
            every { toggle() } answers { active = !active }
        }
        val viewModel = SosToolViewModel(sos, firebaseController)

        viewModel.toggle()
        viewModel.dismiss()
        active = true
        viewModel.toggle()

        assertEquals(listOf(ToolkitTileIds.SOS), toolUses())
    }

    @Test
    fun `a finished reaction round is posted as its score`() {
        var now = 1_000L
        val viewModel = ReactionTestToolViewModel(
            firebaseController = firebaseController,
            timeProvider = { now },
        )

        viewModel.startTest(delayMs = 100L)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        now += 215L
        viewModel.handleTap()

        val score = firebaseController.loggedEvents
            .single { it.name == AppGa4Contract.EventName.POST_SCORE }
        assertEquals(AnalyticsValue.LongVal(215L), score.params[AppGa4Contract.Param.SCORE])
        assertEquals(
            AnalyticsValue.Str(ToolkitTileIds.REACTION_TEST),
            score.params[AppGa4Contract.Param.CHARACTER],
        )
        assertEquals(listOf(ToolkitTileIds.REACTION_TEST), toolUses())
    }

    @Test
    fun `a watched tool counts as used only once it has stayed open`() {
        val sensors: SensorRepository = mockk {
            every { getCompassAzimuth() } returns emptyFlow()
        }
        val scheduler = dispatcherExtension.testDispatcher.scheduler

        val glance = CompassToolViewModel(sensors, firebaseController)
        glance.open()
        scheduler.advanceTimeBy(FlowToolViewModel.WATCHED_USE_DELAY_MS - 1)
        glance.dismiss()
        scheduler.advanceUntilIdle()
        assertTrue(toolUses().isEmpty())

        glance.open()
        scheduler.advanceTimeBy(FlowToolViewModel.WATCHED_USE_DELAY_MS + 1)
        assertEquals(listOf(ToolkitTileIds.COMPASS), toolUses())
    }

    @Test
    fun `every tool event satisfies the app GA4 contract`() {
        CoinFlipToolViewModel(firebaseController).flip()
        var now = 0L
        ReactionTestToolViewModel(firebaseController, timeProvider = { now }).apply {
            startTest(delayMs = 1L)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            now += 300L
            handleTap()
        }

        assertTrue(firebaseController.loggedEvents.isNotEmpty())
        firebaseController.loggedEvents.forEach { it.assertSatisfiesContract() }
    }

    private fun AnalyticsEvent.assertSatisfiesContract() {
        assertTrue(AppGa4ContractValidator.isValidEventName(name), name)
        assertEquals(emptySet(), AppGa4ContractValidator.missingRequiredParams(name, params.keys))
        assertEquals(emptySet(), AppGa4ContractValidator.forbiddenParams(params.keys))
    }
}
