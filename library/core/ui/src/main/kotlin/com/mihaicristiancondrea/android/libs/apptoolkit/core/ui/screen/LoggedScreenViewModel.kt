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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * A [ScreenViewModel] that reports to Firebase: a breadcrumb when it is created and for every
 * event, and for each operation a start breadcrumb and `vm_op_start` event, and on failure an error
 * breadcrumb, a `vm_op_error` event and a Crashlytics report.
 *
 * The messages, keys and events are fixed: dashboards and Crashlytics filters read them.
 *
 * @param screenName The name reported as the `screen` key and parameter.
 * @param viewModelName The name reported as the breadcrumb `viewModel` key and the GA4
 * `view_model` parameter. It is passed in rather than read from the class because R8 renames
 * classes in release builds, which would make those values unreadable. Defaults to [screenName].
 */
abstract class LoggedScreenViewModel<S, E : Any>(
    initialState: S,
    protected val telemetryRepository: TelemetryRepository,
    private val screenName: String,
    protected val viewModelName: String = screenName,
) : ScreenViewModel<S, E>(initialState) {

    init {
        breadcrumb(
            message = Breadcrumb.Messages.VM_INIT,
            attributes = mapOf(Breadcrumb.Keys.STEP to "init"),
        )
    }

    override fun onEventReceived(event: E) {
        breadcrumb(
            message = Breadcrumb.Messages.VM_EVENT,
            attributes = mapOf(Breadcrumb.Keys.EVENT to event.breadcrumbName()),
        )
    }

    /** Logs a breadcrumb carrying this screen's name and ViewModel name. */
    protected fun breadcrumb(
        message: String,
        attributes: Map<String, String> = emptyMap(),
    ) {
        telemetryRepository.logBreadcrumb(
            message = message,
            attributes = buildMap(attributes.size + 2) {
                put(Breadcrumb.Keys.SCREEN, screenName)
                put(Breadcrumb.Keys.VIEW_MODEL, viewModelName)
                putAll(attributes)
            },
        )
    }

    /** Logs the start of the operation [action]. [launchReport] already does this. */
    protected fun startOperation(
        action: String,
        extra: Map<String, String> = emptyMap(),
    ) {
        breadcrumb(
            message = Breadcrumb.Messages.VM_OP_START,
            attributes = buildMap(extra.size + 2) {
                put(Breadcrumb.Keys.ACTION, action)
                put(Breadcrumb.Keys.STEP, "start")
                putAll(extra)
            },
        )

        telemetryRepository.logEvent(
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

    /**
     * Runs [block] as the operation [action]: logs its start, and if it throws, reports the
     * failure and calls [onError]. Cancellation is passed through, never reported.
     *
     * @param extra Attributes added to the operation's breadcrumbs and events.
     */
    protected fun launchReport(
        action: String,
        extra: Map<String, String> = emptyMap(),
        onError: suspend (Throwable) -> Unit = {},
        block: suspend () -> Unit,
    ): Job {
        startOperation(action = action, extra = extra)
        return viewModelScope.launch {
            try {
                block()
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (throwable: Throwable) {
                reportOperationError(action = action, extra = extra, throwable = throwable)
                onError(throwable)
            }
        }
    }

    /**
     * Collects this flow as the operation [action], the flow counterpart of [launchReport]: logs its
     * start, passes every value to [onEach], and if the flow throws, reports the failure and calls
     * [onError]. Collection ends with the failure, so restart the returned [Job] to try again.
     * Cancellation is passed through, never reported.
     *
     * @param extra Attributes added to the operation's breadcrumbs and events.
     */
    protected fun <V> Flow<V>.collectReport(
        action: String,
        extra: Map<String, String> = emptyMap(),
        onError: suspend (Throwable) -> Unit = {},
        onEach: suspend (V) -> Unit,
    ): Job = launchReport(action = action, extra = extra, onError = onError) {
        collect { value -> onEach(value) }
    }

    /**
     * Reports a failure of this flow as the operation [action], then hands it to [block], which can
     * emit a fallback. Cancellation is passed through, never reported.
     *
     * For a flow that goes on after the failure, such as one combined with others or shared with
     * `stateIn`. A flow whose values go straight into state uses [collectReport] instead.
     */
    protected fun <V> Flow<V>.catchReport(
        action: String,
        extra: Map<String, String> = emptyMap(),
        block: suspend FlowCollector<V>.(Throwable) -> Unit = {},
    ): Flow<V> = catch { throwable ->
        if (throwable is CancellationException) throw throwable
        reportOperationError(action = action, extra = extra, throwable = throwable)
        block(throwable)
    }

    /** Logs the error breadcrumb and the `vm_op_error` event, then reports [throwable]. */
    private fun reportOperationError(
        action: String,
        extra: Map<String, String>,
        throwable: Throwable,
    ) {
        val errorClass: String = throwable::class.java.simpleName
        breadcrumb(
            message = Breadcrumb.Messages.VM_OP_ERROR,
            attributes = buildMap(extra.size + 3) {
                put(Breadcrumb.Keys.ACTION, action)
                put(Breadcrumb.Keys.STEP, "catch")
                put(Breadcrumb.Keys.ERROR, errorClass)
                putAll(extra)
            },
        )

        telemetryRepository.logEvent(
            AnalyticsEvent(
                name = "vm_op_error",
                params = buildMap {
                    put("screen", AnalyticsValue.Str(screenName))
                    put("view_model", AnalyticsValue.Str(viewModelName))
                    put("action", AnalyticsValue.Str(action))
                    put("error_class", AnalyticsValue.Str(errorClass))
                    putAll(extra.toAnalyticsParams())
                },
            ),
        )

        telemetryRepository.reportViewModelError(
            viewModelName = viewModelName,
            action = action,
            throwable = throwable,
        )
    }

    /**
     * A name for this event that survives R8.
     *
     * A data class or data object's `toString()` starts with its source name, written into the
     * bytecode as a string literal that R8 does not rename, unlike the class name. Only that prefix
     * is kept, so a field value (which could be text the user typed) never reaches a breadcrumb.
     * Any other event falls back to its class name.
     */
    private fun Any.breadcrumbName(): String {
        val name: String = toString().substringBefore('(')
        val looksLikeSourceName: Boolean =
            name.isNotEmpty() && name.none { it == '@' || it == '.' || it == '$' }
        return if (looksLikeSourceName) name else this::class.java.simpleName
    }

    private fun Map<String, String>.toAnalyticsParams(): Map<String, AnalyticsValue> =
        entries.associate { (key, value) -> key to AnalyticsValue.Str(value) }

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
