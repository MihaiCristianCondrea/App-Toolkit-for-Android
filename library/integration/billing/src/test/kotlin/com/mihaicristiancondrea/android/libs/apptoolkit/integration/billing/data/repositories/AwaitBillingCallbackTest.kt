package com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repositories

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AwaitBillingCallbackTest {
    @Test
    fun `duplicate timeout response preserves first result`() = runTest {
        val result = awaitBillingCallback<String> { complete ->
            complete("OK")
            complete("SERVICE_UNAVAILABLE: Timeout communicating with service")
        }
        assertEquals("OK", result)
    }

    @Test
    fun `concurrent responses complete without double resume`() = runTest {
        lateinit var complete: (Int) -> Unit
        val result = async(start = CoroutineStart.UNDISPATCHED) {
            awaitBillingCallback<Int> { complete = it }
        }
        val pool = Executors.newFixedThreadPool(2)
        val start = CountDownLatch(1)
        try {
            val calls = (1..2).map { value ->
                pool.submit { check(start.await(5, TimeUnit.SECONDS)); complete(value) }
            }
            start.countDown()
            calls.forEach { it.get(5, TimeUnit.SECONDS) }
            assertTrue(result.await() in 1..2)
        } finally {
            pool.shutdownNow()
        }
    }

    @Test
    fun `callbacks after cancellation are ignored`() = runTest {
        lateinit var complete: (Int) -> Unit
        val result = async(start = CoroutineStart.UNDISPATCHED) {
            awaitBillingCallback<Int> { complete = it }
        }
        result.cancel()
        complete(1)
        complete(2)
        result.join()
        assertTrue(result.isCancelled)
    }

    @Test
    fun `late callback from previous attempt cannot complete retry`() = runTest {
        lateinit var previous: (String) -> Unit
        assertEquals("timeout", awaitBillingCallback<String> {
            previous = it
            it("timeout")
        })
        val retry = awaitBillingCallback<String> {
            previous("late success")
            it("retry success")
        }
        assertEquals("retry success", retry)
    }
}
