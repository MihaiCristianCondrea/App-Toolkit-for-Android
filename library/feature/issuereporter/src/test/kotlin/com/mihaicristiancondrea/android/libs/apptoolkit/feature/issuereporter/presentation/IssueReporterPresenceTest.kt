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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

/** Covers the latch that keeps the reporter's two entry points from stacking sheets. */
class IssueReporterPresenceTest {

    @AfterEach
    fun clearPresence() {
        while (IssueReporterPresence.isShowing) {
            IssueReporterPresence.onHidden()
        }
    }

    @Test
    fun `nothing is showing to begin with`() {
        assertThat(IssueReporterPresence.isShowing).isFalse()
    }

    @Test
    fun `a composed sheet is reported as showing until it leaves`() {
        IssueReporterPresence.onShown()
        assertThat(IssueReporterPresence.isShowing).isTrue()

        IssueReporterPresence.onHidden()
        assertThat(IssueReporterPresence.isShowing).isFalse()
    }

    @Test
    fun `an unmatched hide cannot drive the count below nothing`() {
        // A stray release would otherwise let the next sheet open twice over.
        IssueReporterPresence.onHidden()
        IssueReporterPresence.onShown()

        assertThat(IssueReporterPresence.isShowing).isTrue()
    }
}
