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

package com.d4rk.android.libs.apptoolkit.app

import com.d4rk.android.libs.apptoolkit.app.about.data.repository.AboutRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.ads.data.repository.AdsSettingsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.data.repository.CacheRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.diagnostics.data.repository.UsageAndDiagnosticsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository.UsageAndDiagnosticsRepository
import com.d4rk.android.libs.apptoolkit.app.help.data.repository.FaqRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.FaqRepository
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.repository.IssueReporterRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.app.main.data.repository.MainRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.app.onboarding.data.repository.OnboardingRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.repository.OnboardingRepository
import com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository.GeneralSettingsRepositoryImpl
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
            contract(CacheRepository::class, CacheRepositoryImpl::class),
            contract(OnboardingRepository::class, OnboardingRepositoryImpl::class),
            contract(
                UsageAndDiagnosticsRepository::class,
                UsageAndDiagnosticsRepositoryImpl::class
            ),
            contract(FaqRepository::class, FaqRepositoryImpl::class),
            contract(AboutRepository::class, AboutRepositoryImpl::class),
            contract(GeneralSettingsRepository::class, GeneralSettingsRepositoryImpl::class),
            contract(IssueReporterRepository::class, IssueReporterRepositoryImpl::class),
            contract(NavigationRepository::class, MainRepositoryImpl::class),
            contract(AdsSettingsRepository::class, AdsSettingsRepositoryImpl::class),
        )

        private fun contract(
            interfaceClass: KClass<*>,
            implementationClass: KClass<*>,
        ): Arguments = Arguments.of(interfaceClass, implementationClass)
    }
}
