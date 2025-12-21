package com.d4rk.android.libs.apptoolkit.app

import com.d4rk.android.libs.apptoolkit.app.about.data.DefaultAboutRepository
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.ads.data.DefaultAdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.data.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.data.DefaultCacheRepository
import com.d4rk.android.libs.apptoolkit.app.diagnostics.data.repository.DefaultUsageAndDiagnosticsRepository
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository.UsageAndDiagnosticsRepository
import com.d4rk.android.libs.apptoolkit.app.help.data.DefaultHelpRepository
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.DefaultIssueReporterRepository
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.app.main.data.repository.MainRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.app.onboarding.data.repository.DefaultOnboardingRepository
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.app.settings.general.data.DefaultGeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass

class ImplementationContractsTest {

    @ParameterizedTest(name = "{1} implements {0}")
    @MethodSource("implementationContracts")
    fun `implementation class conforms to its interface`(
        interfaceClass: KClass<*>,
        implementationClass: KClass<*>,
    ) {
        assertTrue(
            interfaceClass.java.isAssignableFrom(implementationClass.java),
            "${implementationClass.qualifiedName} should implement ${interfaceClass.qualifiedName}",
        )
    }

    companion object {
        @JvmStatic
        fun implementationContracts(): Stream<Arguments> = Stream.of(
            contract(CacheRepository::class, DefaultCacheRepository::class),
            contract(OnboardingRepository::class, DefaultOnboardingRepository::class),
            contract(
                UsageAndDiagnosticsRepository::class,
                DefaultUsageAndDiagnosticsRepository::class
            ),
            contract(HelpRepository::class, DefaultHelpRepository::class),
            contract(AboutRepository::class, DefaultAboutRepository::class),
            contract(GeneralSettingsRepository::class, DefaultGeneralSettingsRepository::class),
            contract(IssueReporterRepository::class, DefaultIssueReporterRepository::class),
            contract(NavigationRepository::class, MainRepositoryImpl::class),
            contract(AdsSettingsRepository::class, DefaultAdsSettingsRepository::class),
        )

        private fun contract(
            interfaceClass: KClass<*>,
            implementationClass: KClass<*>,
        ): Arguments = Arguments.of(interfaceClass, implementationClass)
    }
}
