package com.d4rk.android.libs.apptoolkit.app.ads.data.repository

import app.cash.turbine.test
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException

class TestAdsSettingsRepositoryImpl {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private fun createRepository(
        dataStore: CommonDataStore,
        debugBuild: Boolean = false,
    ): AdsSettingsRepositoryImpl {
        val buildInfoProvider = mockk<BuildInfoProvider> {
            every { isDebugBuild } returns debugBuild
        }
        return AdsSettingsRepositoryImpl(
            dataStore = dataStore,
            buildInfoProvider = buildInfoProvider,
        )
    }

    @Test
    fun `observeAdsEnabled emits datastore value`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] observeAdsEnabled emits datastore value")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flowOf(false)
        val repository = createRepository(dataStore, debugBuild = false)

        repository.observeAdsEnabled().test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeAdsEnabled propagates error`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flow { throw IOException("boom") }
        val repository = createRepository(dataStore, debugBuild = false)

        repository.observeAdsEnabled().test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
        }
    }

    @Test
    fun `observeAdsEnabled rethrows cancellation`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] observeAdsEnabled rethrows cancellation")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flow { throw CancellationException("boom") }
        val repository = createRepository(dataStore, debugBuild = false)

        val thrown = runCatching { repository.observeAdsEnabled().collect() }.exceptionOrNull()

        assertThat(thrown).isInstanceOf(CancellationException::class.java)
    }

    /*
        FIXME:

        🚀 [TEST] setAdsEnabled returns success when persisted

    expected instance of: com.d4rk.android.libs.apptoolkit.core.domain.model.Result$Success
    but was instance of : kotlin.Unit
    with value          : kotlin.Unit
    expected instance of: com.d4rk.android.libs.apptoolkit.core.domain.model.Result$Success
    but was instance of : kotlin.Unit
    with value          : kotlin.Unit
        at app//com.d4rk.android.libs.apptoolkit.app.ads.data.repository.TestAdsSettingsRepositoryImpl$setAdsEnabled returns success when persisted$1.invokeSuspend(TestAdsSettingsRepositoryImpl.kt:90)
        at app//com.d4rk.android.libs.apptoolkit.app.ads.data.repository.TestAdsSettingsRepositoryImpl$setAdsEnabled returns success when persisted$1.invoke(TestAdsSettingsRepositoryImpl.kt)
        at app//com.d4rk.android.libs.apptoolkit.app.ads.data.repository.TestAdsSettingsRepositoryImpl$setAdsEnabled returns success when persisted$1.invoke(TestAdsSettingsRepositoryImpl.kt)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invokeSuspend(TestBuilders.kt:317)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invoke(TestBuilders.kt)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invoke(TestBuilders.kt)
        at app//kotlinx.coroutines.intrinsics.UndispatchedKt.startCoroutineUndispatched(Undispatched.kt:20)
        at app//kotlinx.coroutines.CoroutineStart.invoke(CoroutineStart.kt:360)
        at app//kotlinx.coroutines.AbstractCoroutine.start(AbstractCoroutine.kt:134)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invokeSuspend(TestBuilders.kt:312)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invoke(TestBuilders.kt)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invoke(TestBuilders.kt)
        at app//kotlinx.coroutines.test.TestBuildersJvmKt$createTestResult$1.invokeSuspend(TestBuildersJvm.kt:11)
        at app//kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:34)
        at app//kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)
        at app//kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:263)
        at app//kotlinx.coroutines.BlockingCoroutine.joinBlocking(Builders.kt:94)
        at app//kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(Builders.kt:70)
        at app//kotlinx.coroutines.BuildersKt.runBlocking(Unknown Source)
        at app//kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(Builders.kt:48)
        at app//kotlinx.coroutines.BuildersKt.runBlocking$default(Unknown Source)
        at app//kotlinx.coroutines.test.TestBuildersJvmKt.createTestResult(TestBuildersJvm.kt:10)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0(TestBuilders.kt:309)
        at app//kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0(Unknown Source)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0(TestBuilders.kt:167)
        at app//kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0(Unknown Source)
        at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0$default(TestBuilders.kt:159)
        at app//kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0$default(Unknown Source)
        at app//com.d4rk.android.libs.apptoolkit.app.ads.data.repository.TestAdsSettingsRepositoryImpl.setAdsEnabled returns success when persisted(TestAdsSettingsRepositoryImpl.kt:82)
        */
    @Test
    fun `setAdsEnabled returns success when persisted`() =
        runTest(dispatcherExtension.testDispatcher) {
            println("\uD83D\uDE80 [TEST] setAdsEnabled returns success when persisted")
            val dataStore = mockk<CommonDataStore>()
            coEvery { dataStore.saveAds(any()) } returns Unit
            val repository = createRepository(dataStore, debugBuild = false)

            val result = repository.setAdsEnabled(true)

            assertThat(result).isInstanceOf(Result.Success::class.java)
            coVerify { dataStore.saveAds(isChecked = true) }
        }

    /*
    FIXME:

    🚀 [TEST] setAdsEnabled returns error on failure

boom
java.io.IOException: boom
	at com.d4rk.android.libs.apptoolkit.app.ads.data.repository.TestAdsSettingsRepositoryImpl$setAdsEnabled returns error on failure$1.invokeSuspend(TestAdsSettingsRepositoryImpl.kt:98)
	at com.d4rk.android.libs.apptoolkit.app.ads.data.repository.TestAdsSettingsRepositoryImpl$setAdsEnabled returns error on failure$1.invoke(TestAdsSettingsRepositoryImpl.kt)
	at com.d4rk.android.libs.apptoolkit.app.ads.data.repository.TestAdsSettingsRepositoryImpl$setAdsEnabled returns error on failure$1.invoke(TestAdsSettingsRepositoryImpl.kt)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invokeSuspend(TestBuilders.kt:317)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invoke(TestBuilders.kt)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invoke(TestBuilders.kt)
	at kotlinx.coroutines.intrinsics.UndispatchedKt.startCoroutineUndispatched(Undispatched.kt:20)
	at kotlinx.coroutines.CoroutineStart.invoke(CoroutineStart.kt:360)
	at kotlinx.coroutines.AbstractCoroutine.start(AbstractCoroutine.kt:134)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invokeSuspend(TestBuilders.kt:312)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invoke(TestBuilders.kt)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invoke(TestBuilders.kt)
	at kotlinx.coroutines.test.TestBuildersJvmKt$createTestResult$1.invokeSuspend(TestBuildersJvm.kt:11)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:34)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)
	at kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:263)
	at kotlinx.coroutines.BlockingCoroutine.joinBlocking(Builders.kt:94)
	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(Builders.kt:70)
	at kotlinx.coroutines.BuildersKt.runBlocking(Unknown Source)
	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(Builders.kt:48)
	at kotlinx.coroutines.BuildersKt.runBlocking$default(Unknown Source)
	at kotlinx.coroutines.test.TestBuildersJvmKt.createTestResult(TestBuildersJvm.kt:10)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0(TestBuilders.kt:309)
	at kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0(Unknown Source)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0(TestBuilders.kt:167)
	at kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0(Unknown Source)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0$default(TestBuilders.kt:159)
	at kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0$default(Unknown Source)
	at com.d4rk.android.libs.apptoolkit.app.ads.data.repository.TestAdsSettingsRepositoryImpl.setAdsEnabled returns error on failure(TestAdsSettingsRepositoryImpl.kt:95)
    */
    @Test
    fun `setAdsEnabled returns error on failure`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] setAdsEnabled returns error on failure")
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveAds(any()) } throws IOException("boom")
        val repository = createRepository(dataStore, debugBuild = false)

        val result = repository.setAdsEnabled(true)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        coVerify { dataStore.saveAds(isChecked = true) }
    }

    @Test
    fun `defaultAdsEnabled false in debug builds`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = createRepository(dataStore = mockk(), debugBuild = true)
        assertThat(repository.defaultAdsEnabled).isFalse()
    }

    @Test
    fun `defaultAdsEnabled true in release builds`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = createRepository(dataStore = mockk(), debugBuild = false)
        assertThat(repository.defaultAdsEnabled).isTrue()
    }
}
