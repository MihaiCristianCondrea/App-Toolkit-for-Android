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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportRefusal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultIssueReportHistoryRepositoryTest {

    @TempDir
    lateinit var directory: Path

    private var now: Long = START_MILLIS

    private fun CoroutineScope.dataStore(): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = this,
        produceFile = { directory.resolve("history.preferences_pb").toFile() },
    )

    private fun repository(dataStore: DataStore<Preferences>) = DefaultIssueReportHistoryRepository(
        dataStore = dataStore,
        currentTimeMillis = { now },
    )

    @Test
    fun `a device that never filed a report may send one`() = runTest {
        val repository = repository(backgroundScope.dataStore())

        assertNull(repository.refusalFor(title = TITLE, description = DESCRIPTION))
    }

    @Test
    fun `any report is held back during the cooldown`() = runTest {
        val repository = repository(backgroundScope.dataStore())
        repository.recordSubmission(title = TITLE, description = DESCRIPTION)

        now += DefaultIssueReportHistoryRepository.COOLDOWN_MILLIS - 1

        assertEquals(
            IssueReportRefusal.COOLDOWN,
            repository.refusalFor(title = "A different title", description = "A different description"),
        )
    }

    @Test
    fun `a different report may be sent once the cooldown is over`() = runTest {
        val repository = repository(backgroundScope.dataStore())
        repository.recordSubmission(title = TITLE, description = DESCRIPTION)

        now += DefaultIssueReportHistoryRepository.COOLDOWN_MILLIS

        assertNull(repository.refusalFor(title = "A different title", description = "A different description"))
    }

    @Test
    fun `the same report is a duplicate for a day, whatever its case and spacing`() = runTest {
        val repository = repository(backgroundScope.dataStore())
        repository.recordSubmission(title = TITLE, description = DESCRIPTION)

        now += DefaultIssueReportHistoryRepository.COOLDOWN_MILLIS

        assertEquals(
            IssueReportRefusal.DUPLICATE,
            repository.refusalFor(title = "  ${TITLE.uppercase()} ", description = DESCRIPTION.replace(" ", "   ")),
        )
    }

    @Test
    fun `the same report may be sent again after a day`() = runTest {
        val repository = repository(backgroundScope.dataStore())
        repository.recordSubmission(title = TITLE, description = DESCRIPTION)

        now += DefaultIssueReportHistoryRepository.DUPLICATE_WINDOW_MILLIS

        assertNull(repository.refusalFor(title = TITLE, description = DESCRIPTION))
    }

    /** The guard is what keeps a flood out, so it must survive the sheet and the process. */
    @Test
    fun `the history survives a new repository on the same file`() = runTest {
        val dataStore = backgroundScope.dataStore()
        repository(dataStore).recordSubmission(title = TITLE, description = DESCRIPTION)

        assertEquals(
            IssueReportRefusal.COOLDOWN,
            repository(dataStore).refusalFor(title = TITLE, description = DESCRIPTION),
        )
    }

    @Test
    fun `only the most recent reports are remembered, and never their text`() = runTest {
        val dataStore = backgroundScope.dataStore()
        val repository = repository(dataStore)
        repeat(times = DefaultIssueReportHistoryRepository.MAX_RECENT_REPORTS + 1) { index ->
            repository.recordSubmission(title = "$TITLE $index", description = DESCRIPTION)
            now += DefaultIssueReportHistoryRepository.COOLDOWN_MILLIS
        }

        assertNull(repository.refusalFor(title = "$TITLE 0", description = DESCRIPTION))
        assertEquals(IssueReportRefusal.DUPLICATE, repository.refusalFor(title = "$TITLE 1", description = DESCRIPTION))

        val stored: String = dataStore.data.first().asMap().values.joinToString()
        assertTrue(TITLE !in stored && "Steps" !in stored)
    }

    /** A clock set back must not hold every report until it catches up. */
    @Test
    fun `a last report that seems to be in the future holds nothing back`() = runTest {
        val repository = repository(backgroundScope.dataStore())
        repository.recordSubmission(title = TITLE, description = DESCRIPTION)

        now -= DefaultIssueReportHistoryRepository.COOLDOWN_MILLIS

        assertNull(repository.refusalFor(title = "A different title", description = "A different description"))
    }

    private companion object {
        const val START_MILLIS: Long = 1_800_000_000_000L
        const val TITLE: String = "Crash when opening settings"
        const val DESCRIPTION: String = "Steps: open the drawer, tap Settings. The app closes at once."
    }
}
