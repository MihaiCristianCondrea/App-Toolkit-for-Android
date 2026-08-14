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

package com.mihaicristiancondrea.android.apptoolkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import java.util.Properties

class VersioningPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("versioning", VersioningExtension::class.java, target)
    }
}

open class VersioningExtension(project: Project) {
    private val releasePropertiesFile = project.rootProject.file("release.properties")
    private val properties = Properties().apply {
        if (releasePropertiesFile.exists()) {
            releasePropertiesFile.inputStream().use { load(it) }
        }
    }

    private fun getProperty(key: String): String = properties.getProperty(key) ?: "0"

    val minSdk get() = getProperty("MIN_SDK").toInt()
    val targetSdk get() = getProperty("TARGET_SDK").toInt()
    val compileSdk get() = getProperty("COMPILE_SDK").toInt()

    fun phoneVersion(): VersionInfo {
        return calculateVersion(
            productFamily = getProperty("PHONE_PRODUCT_FAMILY").toInt(),
            upload = getProperty("PHONE_UPLOAD").toInt()
        )
    }

    private fun calculateVersion(
        productFamily: Int,
        upload: Int
    ): VersionInfo {
        val minSdk = getProperty("MIN_SDK").toInt()
        val targetSdk = getProperty("TARGET_SDK").toInt()
        val compileSdk = getProperty("COMPILE_SDK").toInt()

        // Safety checks
        check(minSdk <= targetSdk) { "MIN_SDK ($minSdk) must be <= TARGET_SDK ($targetSdk)" }
        check(targetSdk <= compileSdk) { "TARGET_SDK ($targetSdk) must be <= COMPILE_SDK ($compileSdk)" }
        check(productFamily in 1..9) { "PRODUCT_FAMILY ($productFamily) must be in 1..9" }
        check(upload in 1..9999) { "UPLOAD ($upload) must be in 1..9999" }

        val now = ZonedDateTime.now(ZoneId.of("Europe/Bucharest"))
        val year = now.year % 100
        val month = now.monthValue
        
        // Version Name: YY.MM.UUUU
        // Human-readable representation of the release date and sequence.
        val versionName = String.format(
            Locale.ROOT,
            "%02d.%02d.%d",
            year,
            month,
            upload
        )

        // Version code: P SS UUUU (up to 7 digits)
        // P:   Product Family (1=Phone, 2=TV, 3=Wear)
        // SS:  Target SDK (2 digits, e.g., 37)
        // UUUU: Global upload counter (4 digits, max 9999)
        //
        // This scheme ensures that version codes are unique, monotonically increasing, 
        // and safely under the Google Play 2.1 billion limit.
        // Example: P=1, SDK=37, Upload=42 -> 1,370,042

        val versionCode = productFamily.toLong() * 1_000_000 + // Product Family (Millions)
                targetSdk.toLong() * 10_000 +                 // Target SDK (Ten Thousands)
                upload                                        // Upload Counter

        check(versionCode <= 2_100_000_000) { "versionCode ($versionCode) exceeds Google Play limit" }

        return VersionInfo(
            compileSdk = compileSdk,
            minSdk = minSdk,
            targetSdk = targetSdk,
            versionCode = versionCode.toInt(),
            versionName = versionName
        )
    }
}

data class VersionInfo(
    val compileSdk: Int,
    val minSdk: Int,
    val targetSdk: Int,
    val versionCode: Int,
    val versionName: String
)
