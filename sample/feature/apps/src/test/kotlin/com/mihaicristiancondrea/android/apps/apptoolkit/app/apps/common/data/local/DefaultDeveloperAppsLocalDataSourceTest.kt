/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.models.AppSummaryDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.models.AppsListDataDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.models.AppsListResponseDto
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class DefaultDeveloperAppsLocalDataSourceTest {

    @Test
    fun `write replaces the cached catalogue and read returns the latest value`() = runTest {
        val directory = Files.createTempDirectory("developer-apps-test").toFile()
        val cacheFile = directory.resolve("catalogue.json")
        val source = DefaultDeveloperAppsLocalDataSource(
            cacheFile,
            Json,
            TestDispatchers(StandardTestDispatcher(testScheduler)),
        )
        val first = response("First")
        val second = response("Second")

        source.write(first)
        source.write(second)

        assertEquals(second, source.read())
        assertFalse(directory.resolve("catalogue.json.tmp").exists())
        directory.deleteRecursively()
    }

    @Test
    fun `read removes corrupt cache data`() = runTest {
        val directory = Files.createTempDirectory("developer-apps-test").toFile()
        val cacheFile = directory.resolve("catalogue.json").apply { writeText("not-json") }
        val source = DefaultDeveloperAppsLocalDataSource(
            cacheFile,
            Json,
            TestDispatchers(StandardTestDispatcher(testScheduler)),
        )

        assertNull(source.read())
        assertFalse(cacheFile.exists())
        directory.deleteRecursively()
    }

    private fun response(name: String): AppsListResponseDto = AppsListResponseDto(
        AppsListDataDto(
            listOf(
                AppSummaryDto(
                    name = name,
                    packageName = "com.example.${name.lowercase()}",
                    iconUrl = "https://example.com/$name.png",
                ),
            ),
        ),
    )
}
