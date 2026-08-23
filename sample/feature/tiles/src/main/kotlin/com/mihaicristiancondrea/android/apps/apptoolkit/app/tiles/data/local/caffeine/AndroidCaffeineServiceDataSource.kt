package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.caffeine

import android.content.Context
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.service.CaffeineService

class AndroidCaffeineServiceDataSource(
    private val context: Context,
) : CaffeineServiceDataSource {
    override fun start(durationMillis: Long?) = CaffeineService.start(context, durationMillis)

    override fun stop() = CaffeineService.stop(context)
}
