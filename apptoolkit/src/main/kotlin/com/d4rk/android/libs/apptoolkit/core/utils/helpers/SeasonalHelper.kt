package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

object SeasonalHelper {
    fun isChristmasSeason(zoneId: ZoneId = ZoneId.systemDefault()): Boolean {
        val today: LocalDate = LocalDate.now(zoneId)
        return (today.month == Month.DECEMBER) || (today.month == Month.JANUARY && today.dayOfMonth <= 6)
    }
}
