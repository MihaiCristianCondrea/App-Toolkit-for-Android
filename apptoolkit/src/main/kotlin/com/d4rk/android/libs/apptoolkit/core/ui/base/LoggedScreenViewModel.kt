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

package com.d4rk.android.libs.apptoolkit.core.ui.base

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Base ViewModel for screens that want standardized Firebase breadcrumbs + crash reporting.
 *
 * This class:
 * - logs ViewModel init once
 * - logs every UI event
 * - provides helpers for "operation start" + "catch + report"
 *
 * Subclasses implement [handleEvent] instead of overriding [onEvent].
 */
abstract class LoggedScreenViewModel<T, E : UiEvent, A : ActionEvent>(
    initialState: UiStateScreen<T>,
    protected val firebaseController: FirebaseController,
    private val screenName: String,
) : ScreenViewModel<T, E, A>(initialState) {

    protected val viewModelName: String = this::class.java.simpleName ?: "UnknownViewModel"

    init {
        breadcrumb(
            message = Breadcrumb.Messages.VM_INIT,
            attributes = mapOf(
                Breadcrumb.Keys.STEP to "init",
            ),
        )
    }

    final override fun onEvent(event: E) {
        breadcrumb(
            message = Breadcrumb.Messages.VM_EVENT,
            attributes = mapOf(
                Breadcrumb.Keys.EVENT to (event::class.java.simpleName ?: "UnknownEvent"),
            ),
        )
        handleEvent(event)
    }

    /** Handles UI events after the base class logs [Breadcrumb.Messages.VM_EVENT]. */
    protected abstract fun handleEvent(event: E)

    /** Logs a breadcrumb with stable keys to help you filter/debug in Firebase. */
    protected fun breadcrumb(
        message: String,
        attributes: Map<String, String> = emptyMap(),
    ) {
        val merged = HashMap<String, String>(attributes.size + 2)
        merged[Breadcrumb.Keys.SCREEN] = screenName
        merged[Breadcrumb.Keys.VIEW_MODEL] = viewModelName
        merged.putAll(attributes)

        firebaseController.logBreadcrumb(
            message = message,
            attributes = merged,
        )
    }

    /** Logs "operation started". */
    protected fun startOperation(
        action: String,
        extra: Map<String, String> = emptyMap(),
    ) {
        val merged = HashMap<String, String>(extra.size + 2)
        merged[Breadcrumb.Keys.ACTION] = action
        merged[Breadcrumb.Keys.STEP] = "start"
        merged.putAll(extra)

        breadcrumb(
            message = Breadcrumb.Messages.VM_OP_START,
            attributes = merged,
        )

        firebaseController.logEvent(
            AnalyticsEvent(
                name = "vm_op_start",
                params = buildMap {
                    put("screen", AnalyticsValue.Str(screenName))
                    put("view_model", AnalyticsValue.Str(viewModelName))
                    put("action", AnalyticsValue.Str(action))
                    putAll(extra.toAnalyticsParams())
                },
            ),
        )
    }

    /** Reports a ViewModel error to Firebase (Crashlytics / your implementation). */
    protected fun reportError(
        action: String,
        throwable: Throwable,
    ) {
        firebaseController.reportViewModelError(
            viewModelName = viewModelName,
            action = action,
            throwable = throwable,
        )
    }

    /**
     * Cancels the previous job (if any) and starts a new one.
     *
     * Use this for:
     * - [generalJob] patterns
     * - dedicated jobs like observeJob/toggleJob
     */
    protected fun Job?.restart(start: () -> Job): Job {
        this?.cancel()
        return start()
    }

    /**
     * Standard catch block:
     * - rethrows cancellations
     * - logs breadcrumb
     * - reports ViewModel error
     * - then delegates to [block] for UI recovery / fallback emissions
     */
    protected fun <V> Flow<V>.catchReport(
        action: String,
        extra: Map<String, String> = emptyMap(),
        block: suspend FlowCollector<V>.(Throwable) -> Unit,
    ): Flow<V> {
        return catch { throwable ->
            if (throwable is CancellationException) throw throwable

            val merged = HashMap<String, String>(extra.size + 3)
            merged[Breadcrumb.Keys.ACTION] = action
            merged[Breadcrumb.Keys.STEP] = "catch"
            merged[Breadcrumb.Keys.ERROR] = throwable::class.java.simpleName ?: "Throwable"
            merged.putAll(extra)

            breadcrumb(
                message = Breadcrumb.Messages.VM_OP_ERROR,
                attributes = buildMap(extra.size + 3) {
                    put(Breadcrumb.Keys.ACTION, action)
                    put(Breadcrumb.Keys.STEP, "catch")
                    put(Breadcrumb.Keys.ERROR, throwable::class.java.simpleName ?: "Throwable")
                    putAll(extra)
                },
            )

            firebaseController.logEvent(
                AnalyticsEvent(
                    name = "vm_op_error",
                    params = buildMap {
                        put("screen", AnalyticsValue.Str(screenName))
                        put("view_model", AnalyticsValue.Str(viewModelName))
                        put("action", AnalyticsValue.Str(action))
                        put(
                            "error_class",
                            AnalyticsValue.Str(throwable::class.java.simpleName ?: "Throwable")
                        )
                        putAll(extra.toAnalyticsParams())
                    },
                ),
            )

            reportError(action = action, throwable = throwable)
            block(throwable)
        }
    }

    /**
     * Launches a coroutine that logs "operation started" before execution and
     * automatically handles error reporting via [catchReport] if the block fails.
     *
     * @param action A descriptive name for the operation being performed.
     * @param extra Additional metadata to include in the breadcrumbs.
     * @param block The asynchronous work to be executed within the coroutine.
     * @return The [Job] associated with the launched coroutine.
     */
    protected fun launchReport(
        action: String,
        extra: Map<String, String> = emptyMap(),
        block: suspend () -> Unit,
        onError: suspend (Throwable) -> Unit,
    ): Job {
        startOperation(action = action, extra = extra)

        return viewModelScope.launch {
            runCatching { block() }
                .onFailure { throwable ->
                    if (throwable is CancellationException) throw throwable

                    breadcrumb(
                        message = Breadcrumb.Messages.VM_OP_ERROR,
                        attributes = buildMap(extra.size + 3) {
                            put(Breadcrumb.Keys.ACTION, action)
                            put(Breadcrumb.Keys.STEP, "catch")
                            put(
                                Breadcrumb.Keys.ERROR,
                                throwable::class.java.simpleName ?: "Throwable"
                            )
                            putAll(extra)
                        },
                    )

                    firebaseController.logEvent(
                        AnalyticsEvent(
                            name = "vm_op_error",
                            params = buildMap {
                                put("screen", AnalyticsValue.Str(screenName))
                                put("view_model", AnalyticsValue.Str(viewModelName))
                                put("action", AnalyticsValue.Str(action))
                                put(
                                    "error_class",
                                    AnalyticsValue.Str(
                                        throwable::class.java.simpleName ?: "Throwable"
                                    )
                                )
                                putAll(extra.toAnalyticsParams())
                            },
                        ),
                    )

                    reportError(action = action, throwable = throwable)
                    onError(throwable)
                }
        }
    }

    private fun Map<String, String>.toAnalyticsParams(): Map<String, AnalyticsValue> {
        return entries.associate { (k, v) ->
            k to AnalyticsValue.Str(v)
        }
    }

    companion object {
        object Breadcrumb {
            object Messages {
                const val VM_INIT: String = "vm_init"
                const val VM_EVENT: String = "vm_event"
                const val VM_OP_START: String = "vm_op_start"
                const val VM_OP_ERROR: String = "vm_op_error"
            }

            object Keys {
                const val SCREEN: String = "screen"
                const val VIEW_MODEL: String = "viewModel"
                const val EVENT: String = "event"
                const val ACTION: String = "action"
                const val STEP: String = "step"
                const val ERROR: String = "error"
            }
        }
    }
}