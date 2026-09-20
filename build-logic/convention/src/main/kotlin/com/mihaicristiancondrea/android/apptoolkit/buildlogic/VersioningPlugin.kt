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

/** Registers typed SDK and application-version access as `versioning`. */
class VersioningPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("versioning", VersioningExtension::class.java, target)
    }
}

open class VersioningExtension(project: Project) {
    private val releasePropertiesFile = project.rootProject.file("release.properties")
    private val properties = Properties().apply {
        check(releasePropertiesFile.exists()) {
            "Missing release.properties at ${releasePropertiesFile.path}"
        }
        releasePropertiesFile.inputStream().use { input -> load(input) }
    }

    private fun intProperty(key: String): Int =
        checkNotNull(properties.getProperty(key)) { "Missing $key in release.properties" }.toInt()

    val minSdk: Int get() = intProperty("MIN_SDK")
    val targetSdk: Int get() = intProperty("TARGET_SDK")
    val compileSdk: Int get() = intProperty("COMPILE_SDK")

    fun phoneVersion(): VersionInfo {
        val minSdk = minSdk
        val targetSdk = targetSdk
        val compileSdk = compileSdk

        check(minSdk <= targetSdk) { "MIN_SDK ($minSdk) must be <= TARGET_SDK ($targetSdk)" }
        check(targetSdk <= compileSdk) { "TARGET_SDK ($targetSdk) must be <= COMPILE_SDK ($compileSdk)" }

        val today = ZonedDateTime.now(ZoneId.of("Europe/Bucharest"))
        val version = calendarVersion(
            productFamily = intProperty("PHONE_PRODUCT_FAMILY"),
            targetSdk = targetSdk,
            year = today.year,
            month = today.monthValue,
            upload = intProperty("PHONE_UPLOAD"),
        )

        return VersionInfo(
            compileSdk = compileSdk,
            minSdk = minSdk,
            targetSdk = targetSdk,
            versionCode = version.versionCode,
            versionName = version.versionName,
        )
    }
}

/**
 * The generated Play Store version for one build.
 *
 * Both values encode the same five facts, so either one can be decoded without consulting Git
 * history or the Play Console.
 */
data class CalendarVersion(
    val versionCode: Int,
    val versionName: String,
)

/**
 * Derives `versionName` and `versionCode` from the release calendar rather than a running counter.
 *
 * `versionCode` is nine digits, `FSSYYMMBB`:
 *
 * ```
 * 1 37 26 08 17
 * │ │  │  │  └─ upload   the build number within this month
 * │ │  │  └──── month
 * │ │  └─────── year     two digits
 * │ └────────── SDK      the target SDK this build compiles against
 * └──────────── family   the product family (phone is 1)
 * ```
 *
 * `versionName` is the same story without the parts a user cannot act on: `YY.MM.BB`, so
 * `26.08.17` reads as the seventeenth August 2026 build.
 *
 * The only value a release touches by hand is `PHONE_UPLOAD`: reset it to 1 for the first upload of
 * a month, and increment it for each further upload in the same month. Year and month come from the
 * build date, which is why a month boundary raises `versionCode` on its own.
 *
 * ### Why two digits for the upload counter
 *
 * Play caps `versionCode` at 2,100,000,000. Nine digits leave the leading family digit free to run
 * to 9 (`937261299`) and stay under the cap. A three-digit counter would push a second product
 * family to 2,372,608,012, which Play rejects — so the counter is capped at 99 uploads per month,
 * far more than a monthly release train needs.
 *
 * ### What keeps versionCode increasing
 *
 * Play requires every upload to exceed the last. Because the SDK digits outrank the date, that
 * holds as long as `TARGET_SDK` never *decreases*: lowering it after a release would generate a
 * smaller code than one already published and Play would reject the upload.
 *
 * @param productFamily 1..9, the leading digit that keeps sibling apps in separate ranges.
 * @param targetSdk 0..99, the API level this build targets.
 * @param year the four-digit build year; only its last two digits are encoded.
 * @param month 1..12, the build month.
 * @param upload 1..99, the build number within [month].
 */
fun calendarVersion(
    productFamily: Int,
    targetSdk: Int,
    year: Int,
    month: Int,
    upload: Int,
): CalendarVersion {
    check(productFamily in 1..9) { "PHONE_PRODUCT_FAMILY ($productFamily) must be in 1..9" }
    check(targetSdk in 0..99) { "TARGET_SDK ($targetSdk) must be in 0..99" }
    check(month in 1..12) { "month ($month) must be in 1..12" }
    check(upload in 1..99) {
        "PHONE_UPLOAD ($upload) must be in 1..99; reset it to 1 for the first upload of a month"
    }

    val shortYear = year % 100

    val versionCode =
        productFamily * 100_000_000L +
            targetSdk * 1_000_000L +
            shortYear * 10_000L +
            month * 100L +
            upload

    check(versionCode <= 2_100_000_000L) {
        "versionCode ($versionCode) exceeds the Google Play limit"
    }

    return CalendarVersion(
        versionCode = versionCode.toInt(),
        versionName = String.format(Locale.ROOT, "%02d.%02d.%02d", shortYear, month, upload),
    )
}

data class VersionInfo(
    val compileSdk: Int,
    val minSdk: Int,
    val targetSdk: Int,
    val versionCode: Int,
    val versionName: String,
)
