package com.d4rk.android.libs.apptoolkit.core.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResultTest {

    @Test
    fun `Success retains provided data`() {
        val success = Result.Success("payload")

        assertThat(success.data).isEqualTo("payload")
        val (captured) = success
        assertThat(captured).isEqualTo("payload")
    }

    @Test
    fun `Error retains provided exception`() {
        val exception = IllegalStateException("boom")

        val error = Result.Error(exception)

        assertThat(error.exception).isSameInstanceAs(exception)
        val (captured) = error
        assertThat(captured).isSameInstanceAs(exception)
    }

    @Test
    fun `Success equality depends on wrapped data`() {
        val first = Result.Success("data")
        val second = Result.Success("data")
        val third = Result.Success("other")

        assertThat(first).isEqualTo(second)
        assertThat(first).isNotEqualTo(third)
    }

    @Test
    fun `Error equality compares underlying exception`() {
        val exception = IllegalArgumentException("invalid")
        val first = Result.Error(exception)
        val second = Result.Error(exception)
        val third = Result.Error(IllegalArgumentException("invalid"))

        assertThat(first).isEqualTo(second)
        assertThat(first).isNotEqualTo(third)
    }
}
