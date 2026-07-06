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

package com.d4rk.android.apptoolkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Properties
import java.time.ZonedDateTime
import java.time.ZoneId

class VersioningPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("versioning", VersioningExtension::class.java, target)
    }
}

open class VersioningExtension(private val project: Project) {
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
        val major = getProperty("VERSION_MAJOR").toInt()
        val minor = getProperty("VERSION_MINOR").toInt()

        // Safety checks
        check(minSdk <= targetSdk) { "MIN_SDK ($minSdk) must be <= TARGET_SDK ($targetSdk)" }
        check(targetSdk <= compileSdk) { "TARGET_SDK ($targetSdk) must be <= COMPILE_SDK ($compileSdk)" }
        check(major in 0..9) { "VERSION_MAJOR ($major) must be in 0..9" }
        check(minor in 0..99) { "VERSION_MINOR ($minor) must be in 0..99" }
        check(productFamily in 1..9) { "PRODUCT_FAMILY ($productFamily) must be in 1..9" }
        check(upload in 1..999) { "UPLOAD ($upload) must be in 1..999" }

        val appVersion = major * 100 + minor
        val now = ZonedDateTime.now(ZoneId.of("Europe/Bucharest"))
        val year = now.year % 100
        val month = now.monthValue
        
        val versionName = String.format("%02d.%02d.%d", year, month, upload)

        // Version code: PP VVV UUU (8 digits)
        // Previous was 127 (3 digits). 
        // 1 * 10,000,000 + 37 * 100,000 = 13,700,000 (8 digits)
        // This is safely above 127.
        val versionCodeBase = productFamily.toLong() * 10_000_000 + targetSdk.toLong() * 100_000
        val versionCode = versionCodeBase + appVersion.toLong() * 1_000 + upload

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
