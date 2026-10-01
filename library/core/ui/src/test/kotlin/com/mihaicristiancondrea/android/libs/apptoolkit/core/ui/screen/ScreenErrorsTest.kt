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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import org.junit.jupiter.api.Test

class ScreenErrorsTest {

    private val fallback = UiTextHelper.DynamicString("Could not load the list")

    @Test
    fun `failures the user can act on get their own text`() {
        assertThat(NetworkException(NetworkException.Reason.NO_INTERNET).toUiText(fallback))
            .isEqualTo(UiTextHelper.StringResource(R.string.screen_error_no_internet))
        assertThat(NetworkException(NetworkException.Reason.TIMEOUT).toUiText(fallback))
            .isEqualTo(UiTextHelper.StringResource(R.string.screen_error_timeout))
        assertThat(NetworkException(NetworkException.Reason.SERVER).toUiText(fallback))
            .isEqualTo(UiTextHelper.StringResource(R.string.screen_error_server))
        assertThat(StorageException(StorageException.Reason.FULL).toUiText(fallback))
            .isEqualTo(UiTextHelper.StringResource(R.string.screen_error_storage_full))
    }

    @Test
    fun `other failures get the screen's own text`() {
        assertThat(NetworkException(NetworkException.Reason.SERIALIZATION).toUiText(fallback)).isEqualTo(fallback)
        assertThat(StorageException(StorageException.Reason.CORRUPT).toUiText(fallback)).isEqualTo(fallback)
        assertThat(IllegalStateException("bug").toUiText(fallback)).isEqualTo(fallback)
    }

    @Test
    fun `without a fallback the generic text is used`() {
        assertThat(IllegalStateException("bug").toUiText()).isEqualTo(GenericErrorText)
    }

    @Test
    fun `failures that repeat the same way are not retryable`() {
        assertThat(NetworkException(NetworkException.Reason.CLIENT).toFailed(fallback).retryable).isFalse()
        assertThat(StorageException(StorageException.Reason.CORRUPT).toFailed(fallback).retryable).isFalse()
        assertThat(NetworkException(NetworkException.Reason.NO_INTERNET).toFailed(fallback).retryable).isTrue()
        assertThat(IllegalStateException("bug").toFailed(fallback).retryable).isTrue()
    }

    @Test
    fun `an error message carries the mapped text`() {
        val message = NetworkException(NetworkException.Reason.NO_INTERNET).toErrorMessage(fallback)

        assertThat(message.isError).isTrue()
        assertThat(message.text).isEqualTo(UiTextHelper.StringResource(R.string.screen_error_no_internet))
    }
}
