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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions.IssueReportRejectedException
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IssueReporterErrorMappersTest {

    companion object {
        @JvmStatic
        fun rejections(): List<Arguments> = listOf(
            Arguments.of(IssueReportRejectedException.Reason.UNAUTHORIZED, R.string.error_unauthorized),
            Arguments.of(IssueReportRejectedException.Reason.FORBIDDEN, R.string.error_forbidden),
            Arguments.of(IssueReportRejectedException.Reason.GONE, R.string.error_gone),
            Arguments.of(IssueReportRejectedException.Reason.UNPROCESSABLE, R.string.error_unprocessable),
        )
    }

    private val UiMessage.resourceId: Int
        get() = (text as UiTextHelper.StringResource).resourceId

    @ParameterizedTest
    @MethodSource("rejections")
    fun `a refusal shows the text for its reason`(reason: IssueReportRejectedException.Reason, expected: Int) {
        val message = IssueReportRejectedException(reason = reason).toSendFailedMessage()

        assertEquals(expected, message.resourceId)
        assertTrue(message.isError)
    }

    @Test
    fun `a network failure with a shared text shows that text`() {
        val message = NetworkException(reason = NetworkException.Reason.NO_INTERNET).toSendFailedMessage()

        assertEquals(CoreUiR.string.screen_error_no_internet, message.resourceId)
        assertTrue(message.isError)
    }

    @Test
    fun `any other failure shows the reporter's text`() {
        val message = IllegalStateException("boom").toSendFailedMessage()

        assertEquals(R.string.snack_report_failed, message.resourceId)
        assertTrue(message.isError)
    }
}
