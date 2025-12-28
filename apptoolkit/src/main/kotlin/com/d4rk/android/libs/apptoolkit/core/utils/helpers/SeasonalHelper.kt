package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

val LocalDate.isChristmasSeason: Boolean
    get() = month == Month.DECEMBER || (month == Month.JANUARY && dayOfMonth <= 6)

fun ZoneId.isChristmasSeason(): Boolean = LocalDate.now(this).isChristmasSeason
