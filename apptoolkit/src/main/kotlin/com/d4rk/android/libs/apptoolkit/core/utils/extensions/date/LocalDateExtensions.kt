package com.d4rk.android.libs.apptoolkit.core.utils.extensions.date

import java.time.LocalDate
import java.time.Month

val LocalDate.isChristmasSeason: Boolean
    get() = month == Month.DECEMBER || (month == Month.JANUARY && dayOfMonth <= 6)