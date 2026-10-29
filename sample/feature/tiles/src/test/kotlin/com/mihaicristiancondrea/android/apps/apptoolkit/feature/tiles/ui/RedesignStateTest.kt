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

import androidx.lifecycle.SavedStateHandle
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.audio.SpeakerAudioDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.BillCurrency
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionUnit
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BillSplitterRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.UnitConversionRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.WaterEjectRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BillSplitterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.UnitConverterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WheelToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WaterEjectToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.protractorGeometry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RedesignStateTest {
    companion object { @JvmField @RegisterExtension val dispatcher = UnconfinedDispatcherExtension() }
    @Test fun currencyTipAndRestoration() {
        val saved = SavedStateHandle()
        val model = BillSplitterToolViewModel(BillSplitterRepository(), FakeTelemetryRepository(), saved)
        model.onEvent(BillSplitterToolEvent.BillChanged("100"))
        model.onEvent(BillSplitterToolEvent.PeopleChanged("3"))
        model.onEvent(BillSplitterToolEvent.CurrencySelected(BillCurrency.JPY))
        assertEquals(BigDecimal("39"), model.state.value.result?.perPerson)
        model.onEvent(BillSplitterToolEvent.TipEnabled(false))
        assertEquals(BigDecimal("34"), model.state.value.result?.perPerson)
        assertEquals(15, model.state.value.tipPercent)
        model.onEvent(BillSplitterToolEvent.Dismiss)
        val restored = BillSplitterToolViewModel(BillSplitterRepository(), FakeTelemetryRepository(), saved)
        assertEquals(model.state.value, restored.state.value)
        restored.onEvent(BillSplitterToolEvent.BillChanged("100.50"))
        assertTrue(restored.state.value.invalidInput)
        restored.onEvent(BillSplitterToolEvent.CurrencySelected(BillCurrency.EUR))
        assertFalse(restored.state.value.invalidInput)
        assertEquals(BigDecimal("33.50"), restored.state.value.result?.perPerson)
    }
    @Test fun rulerDraftReadingAndRangeRestore() {
        val preferences = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.FakeToolkitTilesPreferencesDataSource()
        val repository = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.RulerRepository(preferences)
        val saved = SavedStateHandle()
        val first = RulerToolViewModel(repository, FakeTelemetryRepository(), saved)
        first.onEvent(com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent.RangeChanged(.2f,.8f))
        first.onEvent(com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent.MarkerMoved(.3f))
        first.onEvent(com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent.ScaleChanged(1.4f))
        first.onEvent(com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent.Dismiss)
        val restored = RulerToolViewModel(repository, FakeTelemetryRepository(), saved)
        assertEquals(first.state.value, restored.state.value)
        assertEquals(.3f, restored.state.value.marker)
        assertEquals(.2f, restored.state.value.start)
        assertEquals(.8f, restored.state.value.end)
        restored.onEvent(com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent.SaveScale)
        assertEquals(1.4f, preferences.rulerScale.value)
    }
    @Test fun converterSwapAndRestoration() {
        val saved = SavedStateHandle()
        val model = UnitConverterToolViewModel(UnitConversionRepository(), FakeTelemetryRepository(), saved)
        model.onEvent(UnitConverterToolEvent.InputChanged("2", true))
        model.onEvent(UnitConverterToolEvent.Swap)
        assertEquals(ConversionUnit.Centimetre, model.state.value.from)
        assertEquals("200", model.state.value.left)
        assertEquals("2", model.state.value.right)
        model.onEvent(UnitConverterToolEvent.InputChanged("3", false))
        model.onEvent(UnitConverterToolEvent.Swap)
        assertEquals("3", model.state.value.left)
        assertEquals("300", model.state.value.right)
        val restored = UnitConverterToolViewModel(UnitConversionRepository(), FakeTelemetryRepository(), saved)
        assertEquals(model.state.value, restored.state.value)
    }
    @Test fun wheelSelectionDuplicateSpinAndRestoration() {
        val saved = SavedStateHandle()
        val model = WheelToolViewModel(FakeTelemetryRepository(), saved, choose = { it - 1 })
        model.onEvent(WheelToolEvent.ChoicesChanged("Alpha\nBeta\nGamma"))
        model.onEvent(WheelToolEvent.Spin)
        val spinning = model.state.value
        assertEquals("Gamma", spinning.winner)
        model.onEvent(WheelToolEvent.Spin)
        model.onEvent(WheelToolEvent.ChoicesChanged("Changed"))
        assertEquals(spinning, model.state.value)
        assertEquals(60f, spinning.target % 360f)
        model.onEvent(WheelToolEvent.Dismiss)
        assertFalse(model.state.value.spinning)
        assertEquals(model.state.value.target, model.state.value.rotation)
        val restored = WheelToolViewModel(FakeTelemetryRepository(), saved)
        assertEquals(listOf("Alpha","Beta","Gamma"), restored.state.value.choices)
        model.onEvent(WheelToolEvent.ChoicesChanged((1..13).joinToString("\n")))
        model.onEvent(WheelToolEvent.Spin)
        assertFalse(model.state.value.spinning)
    }
    @Test fun protractorLeavesRoomForTheHeader() {
        for ((width, height) in listOf(430f to 957f, 360f to 640f, 957f to 430f, 840f to 780f)) {
            val geometry = protractorGeometry(width, height)
            assertTrue(geometry.y - geometry.radius >= 144f, "Dial overlaps the header at $width x $height")
        }
    }

    @Test fun protractorGeometryMatchesTouchCoordinates() {
        val portrait = protractorGeometry(430f,957f)
        assertFalse(portrait.landscape)
        assertEquals(0f, portrait.x)
        assertEquals(0f, portrait.angleAt(0f, portrait.y - 100f))
        assertEquals(90f, portrait.angleAt(100f, portrait.y))
        assertEquals(180f, portrait.angleAt(0f, portrait.y + 100f))
        val landscape = protractorGeometry(957f,430f)
        assertTrue(landscape.landscape)
        assertEquals(430f, landscape.y)
        assertEquals(90f, landscape.angleAt(landscape.x, landscape.y - 100f))
        assertEquals(180f, landscape.angleAt(landscape.x - 100f, landscape.y))
    }
    @Test fun audioCancellationCompletionFailureAndRetry() {
        var starts = 0
        var releases = 0
        var fail = false
        val source = object : SpeakerAudioDataSource {
            override suspend fun playCycle(onSecond: (Int) -> Unit) {
                starts++
                try {
                    if (fail) error("audio unavailable")
                    for (second in 30 downTo 1) { onSecond(second); delay(1000) }
                    onSecond(0)
                } finally { releases++ }
            }
        }
        val model = WaterEjectToolViewModel(WaterEjectRepository(source), FakeTelemetryRepository())
        model.onEvent(WaterEjectToolEvent.Start)
        model.onEvent(WaterEjectToolEvent.Start)
        assertEquals(1, starts)
        assertTrue(model.state.value.running)
        model.onEvent(WaterEjectToolEvent.Stop)
        assertFalse(model.state.value.running)
        assertEquals(1, releases)
        model.onEvent(WaterEjectToolEvent.Start)
        dispatcher.testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(model.state.value.running)
        assertEquals(0, model.state.value.seconds)
        assertEquals(2, releases)
        fail = true
        model.onEvent(WaterEjectToolEvent.Start)
        assertTrue(model.state.value.failed)
        fail = false
        model.onEvent(WaterEjectToolEvent.Start)
        assertFalse(model.state.value.failed)
        model.onEvent(WaterEjectToolEvent.Stop)
        assertEquals(4, releases)
    }
}
