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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.SeasonalThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.ThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.models.HolidayThemeSnapshot
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DefaultSeasonalThemeRepositoryTest {

    private val christmasEve = LocalDate.parse("2026-12-24")
    private val newYearsDay = LocalDate.parse("2027-01-01")
    private val afterChristmas = LocalDate.parse("2027-01-08")
    private val halloween = LocalDate.parse("2026-10-31")
    private val summer = LocalDate.parse("2026-07-14")

    private val seasonal = FakeSeasonalPreferences()
    private val theme = FakeThemePreferences(paletteId = StaticPaletteIds.GOOGLE_BLUE, dynamic = true)
    private val repository = DefaultSeasonalThemeRepository(seasonal = seasonal, theme = theme)

    @Test
    fun `no greeting is due outside the holidays`() = runTest {
        assertNull(repository.pendingHolidayGreeting(summer))
    }

    @Test
    fun `a holiday is greeted once per occurrence`() = runTest {
        assertEquals(HolidaySeason.CHRISTMAS, repository.pendingHolidayGreeting(christmasEve))

        repository.answerHolidayGreeting(HolidaySeason.CHRISTMAS, christmasEve, useHolidayTheme = false)

        assertNull(repository.pendingHolidayGreeting(christmasEve))
        assertNull(repository.pendingHolidayGreeting(newYearsDay), "same season, new calendar year")
        assertEquals(
            HolidaySeason.CHRISTMAS,
            repository.pendingHolidayGreeting(LocalDate.parse("2027-12-25")),
        )
        assertEquals(HolidaySeason.HALLOWEEN, repository.pendingHolidayGreeting(halloween))
    }

    @Test
    fun `declining the holiday theme leaves the appearance alone`() = runTest {
        repository.answerHolidayGreeting(HolidaySeason.CHRISTMAS, christmasEve, useHolidayTheme = false)

        assertEquals(StaticPaletteIds.GOOGLE_BLUE, theme.staticPaletteId.first())
        assertTrue(theme.dynamicColors.first())
        assertNull(repository.state.first().holidayThemeInUse)
    }

    @Test
    fun `accepting applies the holiday palette and restores the old appearance afterwards`() =
        runTest {
            repository.answerHolidayGreeting(HolidaySeason.CHRISTMAS, christmasEve, useHolidayTheme = true)

            assertEquals(StaticPaletteIds.CHRISTMAS, theme.staticPaletteId.first())
            assertFalse(theme.dynamicColors.first())
            assertEquals(HolidaySeason.CHRISTMAS, repository.state.first().holidayThemeInUse)

            repository.restoreThemeAfterHoliday(newYearsDay)
            assertEquals(StaticPaletteIds.CHRISTMAS, theme.staticPaletteId.first(), "still Christmas")

            repository.restoreThemeAfterHoliday(afterChristmas)
            assertEquals(StaticPaletteIds.GOOGLE_BLUE, theme.staticPaletteId.first())
            assertTrue(theme.dynamicColors.first(), "wallpaper colors come back too")
            assertNull(repository.state.first().holidayThemeInUse)
        }

    @Test
    fun `a palette picked during the holiday survives its end`() = runTest {
        repository.answerHolidayGreeting(HolidaySeason.CHRISTMAS, christmasEve, useHolidayTheme = true)
        theme.saveStaticPaletteId(StaticPaletteIds.ROSE)

        repository.restoreThemeAfterHoliday(afterChristmas)

        assertEquals(StaticPaletteIds.ROSE, theme.staticPaletteId.first())
        assertNull(repository.state.first().holidayThemeInUse)
    }

    @Test
    fun `accepting while already wearing the holiday palette saves nothing to restore`() = runTest {
        theme.saveDynamicColors(false)
        theme.saveStaticPaletteId(StaticPaletteIds.HALLOWEEN)

        repository.answerHolidayGreeting(HolidaySeason.HALLOWEEN, halloween, useHolidayTheme = true)

        assertNull(repository.state.first().holidayThemeInUse)
        assertEquals(StaticPaletteIds.HALLOWEEN, theme.staticPaletteId.first())
    }

    @Test
    fun `a leftover holiday theme keeps the everyday appearance for the next holiday`() = runTest {
        seasonal.saveHolidayThemeSnapshot(
            HolidayThemeSnapshot(
                season = HolidaySeason.HALLOWEEN,
                previousPaletteId = StaticPaletteIds.GREEN,
                previousDynamicColors = false,
            ),
        )
        theme.saveDynamicColors(false)
        theme.saveStaticPaletteId(StaticPaletteIds.HALLOWEEN)

        repository.answerHolidayGreeting(HolidaySeason.CHRISTMAS, christmasEve, useHolidayTheme = true)
        repository.restoreThemeAfterHoliday(afterChristmas)

        assertEquals(StaticPaletteIds.GREEN, theme.staticPaletteId.first())
    }

    @Test
    fun `unlocking is stored once`() = runTest {
        assertFalse(repository.state.first().unlocked)

        assertTrue(repository.unlockSeasonalThemes(), "the first unlock is news")
        assertFalse(repository.unlockSeasonalThemes(), "later ones are not")

        assertTrue(repository.state.first().unlocked)
    }

    private class FakeSeasonalPreferences : SeasonalThemePreferencesDataSource {
        override val seasonalThemesUnlocked = MutableStateFlow(false)
        override val lastHolidayGreeting = MutableStateFlow<String?>(null)
        override val holidayThemeSnapshot = MutableStateFlow<HolidayThemeSnapshot?>(null)

        override suspend fun saveSeasonalThemesUnlocked(unlocked: Boolean) {
            seasonalThemesUnlocked.value = unlocked
        }

        override suspend fun saveLastHolidayGreeting(occurrenceKey: String) {
            lastHolidayGreeting.value = occurrenceKey
        }

        override suspend fun saveHolidayThemeSnapshot(snapshot: HolidayThemeSnapshot?) {
            holidayThemeSnapshot.value = snapshot
        }
    }

    private class FakeThemePreferences(paletteId: String, dynamic: Boolean) :
        ThemePreferencesDataSource {
        override val themeMode = MutableStateFlow("follow_system")
        override val amoledMode = MutableStateFlow(false)
        override val dynamicColors = MutableStateFlow(dynamic)
        override val dynamicPaletteVariant = MutableStateFlow(0)
        override val staticPaletteId = MutableStateFlow(paletteId)

        override suspend fun saveThemeMode(mode: String) {
            themeMode.value = mode
        }

        override suspend fun saveAmoledMode(isChecked: Boolean) {
            amoledMode.value = isChecked
        }

        override suspend fun saveDynamicColors(isChecked: Boolean) {
            dynamicColors.value = isChecked
        }

        override suspend fun saveDynamicPaletteVariant(variant: Int) {
            dynamicPaletteVariant.value = variant
        }

        override suspend fun saveStaticPaletteId(id: String) {
            staticPaletteId.value = id
        }
    }
}
