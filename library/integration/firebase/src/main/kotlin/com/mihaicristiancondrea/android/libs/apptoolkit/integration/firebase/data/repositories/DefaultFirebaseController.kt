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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.data.repositories


/** The previous name of [FirebaseTelemetryRepository], kept so existing imports still compile. */
@Deprecated(
    message = "Renamed to FirebaseTelemetryRepository, in integration.firebase.data.repositories.",
    replaceWith = ReplaceWith(
        expression = "FirebaseTelemetryRepository",
        "com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.data.repositories.FirebaseTelemetryRepository",
    ),
)
typealias DefaultFirebaseController = FirebaseTelemetryRepository

/** An older name of [FirebaseTelemetryRepository], kept so existing imports still compile. */
@Deprecated(
    message = "Renamed to FirebaseTelemetryRepository, in integration.firebase.data.repositories.",
    replaceWith = ReplaceWith(
        expression = "FirebaseTelemetryRepository",
        "com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.data.repositories.FirebaseTelemetryRepository",
    ),
)
typealias FirebaseControllerImpl = FirebaseTelemetryRepository
