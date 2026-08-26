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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.ads

import kotlinx.coroutines.flow.StateFlow

/**
 * Whether ad slots may request and render an ad at all.
 *
 * This is the single place that turns the stored limit-ads opt-in into a rendering decision, and
 * the only thing that behaves differently between debug and release builds:
 *
 * | Build   | Toggle reads | Opt-in on                          |
 * |---------|--------------|------------------------------------|
 * | Release | Reduce ads   | app-open ads stop, the rest render |
 * | Debug   | Disable ads  | nothing renders                    |
 *
 * App-open ads are suppressed identically under both, by the host's own policy, so this contract
 * covers native and banner slots only.
 *
 * ### Why debug differs
 *
 * Verifying how the app looks and behaves with no ads at all needs a switch that actually stops
 * them, and a developer build is the right place for it. Shipping that switch would hand every user
 * an ad-free build, which is what the removed ads-enabled preference did.
 *
 * ### Where premium plugs in
 *
 * A paid ad-free tier is the planned third answer to this same question. It belongs here: the
 * release branch becomes "allowed unless the user has bought ad removal", and no ad slot, no
 * settings screen, and no SDK code changes. Adding a second preference that ad slots also consult
 * would recreate the disagreement that used to crash host processes — see the module README.
 */
interface AdsDisplayPolicy {

    /**
     * Hot, always-current answer.
     *
     * A `StateFlow` because ad slots read it during composition and must never wait on a first
     * emission; while a value is still pending it reads as "allowed", the release behaviour.
     */
    val adsAllowed: StateFlow<Boolean>
}
