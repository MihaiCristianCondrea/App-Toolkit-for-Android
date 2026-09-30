# Incident Report: Compass Azimuth Cannot Round NaN Value Crash

* **Status:** Open (Unresolved)
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

## Guidance & Open Questions

1. Validate that the orientation value is a valid finite number (`orientation[0].isFinite()`) before converting and rounding.
2. Alternatively, filter out `NaN` azimuth values or fallback safely without emitting invalid sensor readings to downstream flows.

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
* [session.json](session.json)
