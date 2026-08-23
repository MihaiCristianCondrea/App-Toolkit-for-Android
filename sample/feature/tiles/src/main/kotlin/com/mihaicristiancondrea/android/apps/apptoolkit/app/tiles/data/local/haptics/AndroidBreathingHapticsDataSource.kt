package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.haptics

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission

class AndroidBreathingHapticsDataSource(context: Context) : BreathingHapticsDataSource {
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        manager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun tick() = vibrate(Haptic.Tick)

    override fun heavyClick() = vibrate(Haptic.HeavyClick)

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun vibrate(haptic: Haptic) {
        if (!vibrator.hasVibrator()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val effectId = when (haptic) {
                Haptic.Tick -> VibrationEffect.EFFECT_TICK
                Haptic.HeavyClick -> VibrationEffect.EFFECT_HEAVY_CLICK
            }
            vibrator.vibrate(VibrationEffect.createPredefined(effectId))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(LEGACY_VIBRATION_MS)
        }
    }

    private enum class Haptic { Tick, HeavyClick }

    private companion object {
        const val LEGACY_VIBRATION_MS = 50L
    }
}
