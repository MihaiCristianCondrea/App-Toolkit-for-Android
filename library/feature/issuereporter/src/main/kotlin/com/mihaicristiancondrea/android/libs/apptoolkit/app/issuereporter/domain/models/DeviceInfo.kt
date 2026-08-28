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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models

/**
 * The device and app details attached to an issue report.
 *
 * A plain value type: no Android imports, no construction from a `Context`, no formatting. It used
 * to read `android.os.Build` in its own field initialisers, expose a `create(context)` companion
 * that mutated a half-built instance, and, worst of it, import a `core:ui` extension to read the
 * package version, which pointed the domain layer at the UI module.
 *
 * Capture now lives in `DeviceInfoLocalDataSource` and formatting in `domain/mappers`, so this can
 * be built in a test without a device.
 */
data class DeviceInfo(
    val appVersionName: String?,
    val appVersionCode: Long,
    val buildVersion: String,
    val releaseVersion: String,
    val sdkVersion: Int,
    val buildId: String,
    val brand: String,
    val manufacturer: String,
    val device: String,
    val model: String,
    val product: String,
    val hardware: String,
    /** Lists rather than arrays: this is a value type, and arrays compare by identity. */
    val abis: List<String>,
    val abis32Bit: List<String>,
    val abis64Bit: List<String>,
)
