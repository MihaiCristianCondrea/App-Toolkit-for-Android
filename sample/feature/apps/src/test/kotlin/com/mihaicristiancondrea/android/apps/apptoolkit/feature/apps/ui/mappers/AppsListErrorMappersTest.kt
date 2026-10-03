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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppsListErrorMappersTest {

    private val UiTextHelper.resourceId: Int
        get() = (this as UiTextHelper.StringResource).resourceId

    @Test
    fun `a failure with a shared text keeps it and its retry`() {
        val failed = NetworkException(reason = NetworkException.Reason.TIMEOUT).toCatalogueFailed()

        assertEquals(CoreUiR.string.screen_error_timeout, failed.message.resourceId)
        assertTrue(failed.retryable)
    }

    @Test
    fun `a failure without a text of its own shows the apps fallback`() {
        val failed = IllegalStateException("Unexpected").toCatalogueFailed()

        assertEquals(R.string.error_failed_to_load_apps, failed.message.resourceId)
        assertTrue(failed.retryable)
    }

    @Test
    fun `an unreadable catalogue cannot be retried`() {
        val failed = NetworkException(reason = NetworkException.Reason.SERIALIZATION).toCatalogueFailed()

        assertEquals(R.string.error_failed_to_load_apps, failed.message.resourceId)
        assertFalse(failed.retryable)
    }

    @Test
    fun `the stale catalogue message is an error with a Try again action`() {
        val message = NetworkException(reason = NetworkException.Reason.NO_INTERNET).toStaleCatalogueMessage()

        assertTrue(message.isError)
        assertEquals(CoreUiR.string.screen_error_no_internet, message.text.resourceId)
        assertEquals(CoreUiR.string.try_again, message.actionLabel?.resourceId)
    }

    @Test
    fun `a favorite failure shows the favorite fallback without an action`() {
        val message = IllegalStateException("Unexpected").toFavoriteErrorMessage()

        assertTrue(message.isError)
        assertEquals(R.string.error_failed_to_update_favorite, message.text.resourceId)
        assertNull(message.actionLabel)
    }

    @Test
    fun `a full disk keeps its shared text for a favorite`() {
        val message = StorageException(reason = StorageException.Reason.FULL).toFavoriteErrorMessage()

        assertEquals(CoreUiR.string.screen_error_storage_full, message.text.resourceId)
    }
}
