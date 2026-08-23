package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.caffeine

interface CaffeineServiceDataSource {
    fun start(durationMillis: Long?)

    fun stop()
}
