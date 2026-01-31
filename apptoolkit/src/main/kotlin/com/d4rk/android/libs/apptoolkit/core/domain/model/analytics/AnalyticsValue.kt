package com.d4rk.android.libs.apptoolkit.core.domain.model.analytics

sealed interface AnalyticsValue {
    data class Str(val value: String) : AnalyticsValue
    data class LongVal(val value: Long) : AnalyticsValue
    data class DoubleVal(val value: Double) : AnalyticsValue
    data class Bool(val value: Boolean) : AnalyticsValue
}

data class AnalyticsEvent(
    val name: String,
    val params: Map<String, AnalyticsValue> = emptyMap(),
)