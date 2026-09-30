# Incident Report: Compass Azimuth Cannot Round NaN Value Crash

* **Status:** Resolved
* **Issue ID:** `0cf2f88e03ab392549f0bb2efb1dcebb`
* **Application:** `com.d4rk.android.apps.apptoolkit` version `26.09.22` (`137260922`)
* **Date:** September 24, 2026 at 22:06:16 GMT+0300

## Behavior and Impact

When processing compass sensor updates, `AndroidSensorLocalDataSource$getCompassAzimuth$1$listener$1.onSensorChanged`
calculates the azimuth angle using `SensorManager.getOrientation`.

If `orientation[0]` evaluates to `Float.NaN` (which can occur under specific device orientation states or degenerate sensor values),
`Math.toDegrees(orientation[0].toDouble())` evaluates to `Double.NaN`. Calling `roundToInt()` on `NaN` throws a fatal
`java.lang.IllegalArgumentException: Cannot round NaN value.`, causing the host app to crash.

## Failure Details

* **Exception:** `java.lang.IllegalArgumentException: Cannot round NaN value.`
* **Location:** `com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors.AndroidSensorLocalDataSource$getCompassAzimuth$1$listener$1.onSensorChanged` ([AndroidSensorLocalDataSource.kt:81](../../../../sample/feature/tiles/src/main/kotlin/com/mihaicristiancondrea/android/apps/apptoolkit/feature/tiles/data/local/sensors/AndroidSensorLocalDataSource.kt#L81))
* **Stack Trace Summary:**
  ```text
  Fatal Exception: java.lang.IllegalArgumentException: Cannot round NaN value.
         at kotlin.math.MathKt__MathJVMKt.roundToInt(MathKt__MathJVM.kt:722)
         at com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors.AndroidSensorLocalDataSource$getCompassAzimuth$1$listener$1.onSensorChanged(AndroidSensorLocalDataSource.kt:81)
         at android.hardware.SystemSensorManager$SensorEventQueue.dispatchSensorEvent(SystemSensorManager.java:837)
  ```

## Affected Area

* **Module:** `:sample:feature:tiles`
* **File:** `AndroidSensorLocalDataSource.kt`
* **Class:** `AndroidSensorLocalDataSource`

## Root Cause Analysis

In `AndroidSensorLocalDataSource.kt`:

1. `SensorManager.getOrientation()` computes the orientation array.
2. Under edge-case sensor data (e.g. zero-magnitude gravity or magnetic vectors, extreme gimbal lock orientations, or sensor noise), `orientation[0]` can evaluate to `NaN`.
3. Executing `(Math.toDegrees(orientation[0].toDouble()).roundToInt() + 360) % 360f` invokes `roundToInt()` directly on `NaN`, which standard Kotlin math functions reject by throwing an `IllegalArgumentException`.

## Root Cause, Confirmed Against the Framework

`SensorManager.getOrientation` computes the azimuth as `atan2(R[1], R[4])`, which is NaN only when
the rotation matrix itself holds NaN. The matrix is built directly from the event values
(`getRotationMatrixFromVector`) or from the stored accelerometer and magnetometer vectors
(`getRotationMatrix`, whose `normH < 0.1f` guard is false for NaN, so it does not reject them).
A NaN azimuth therefore means the sensor driver delivered a non-finite reading, which some drivers
do while they calibrate. The crashing device was a OnePlus 8 Pro on Android 11.

The level (`getLevelOrientation`) had the same exposure: it would not crash, since it does not
round, but it would emit NaN pitch and roll to the level preview.

## Fix

In `:sample:feature:tiles`, `data/local/sensors/SensorReadings.kt`:

1. `FloatArray.isFiniteReading()`: both sensor listeners drop any event whose values are not all
   finite, before copying them into the stored gravity or magnetic vectors, so one bad reading
   cannot poison the later accelerometer and magnetometer readings either.
2. `azimuthDegrees(radians)`: returns null for a non-finite azimuth instead of calling
   `roundToInt()`, and normalises the heading to 0 to 359. The compass emits only non-null
   headings.
3. `tiltDegrees(pitch, roll)`: returns null when either angle is not finite; the level emits only
   real tilts.

The compass and the level keep their last good value while a driver reports NaN.

## Verification

* `SensorReadingsTest` covers NaN and infinite azimuths (no heading, no exception), heading
  normalisation, NaN tilts, and readings with a non-finite value.
* Not reproduced on a device: the driver behaviour is specific to the reporting hardware. Watch
  Crashlytics issue `0cf2f88e03ab392549f0bb2efb1dcebb` after the next release.

## Timeline

* September 24, 2026: crash reported by Crashlytics on version `26.09.22`.
* September 30, 2026: investigated, fixed and moved to `fixed/`.

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
* [session.json](session.json)
