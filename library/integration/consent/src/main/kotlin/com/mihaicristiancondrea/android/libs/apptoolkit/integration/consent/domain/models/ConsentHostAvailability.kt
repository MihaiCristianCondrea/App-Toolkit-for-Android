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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Whether the host activity can still be used as a context for a UMP round trip.
 *
 * Consent is requested from onboarding, startup, and each host's `MainActivity`, so a request can
 * easily be issued by an activity that is already on its way out. UMP keeps a reference to that
 * activity for the whole request, so starting one from a dead host is wasted work at best.
 */
val ConsentHost.isAlive: Boolean
    get() = !activity.isFinishing && !activity.isDestroyed

/**
 * Whether the host is on screen and can legally host a consent form.
 *
 * Falls back to [isAlive] for activities that are not [LifecycleOwner]s, which keeps the check
 * usable for hosts created by consumers that do not extend the AndroidX activity classes.
 */
val ConsentHost.canShowConsentForm: Boolean
    get() {
        if (!isAlive) return false
        val lifecycleOwner: LifecycleOwner = activity as? LifecycleOwner ?: return true
        return lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }
