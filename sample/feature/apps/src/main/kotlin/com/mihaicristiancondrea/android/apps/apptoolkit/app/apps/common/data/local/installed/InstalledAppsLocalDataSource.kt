package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.installed

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppInstallInfo

/** Android platform source for installed package metadata. */
interface InstalledAppsLocalDataSource {
    fun getInstalledPackages(packageNames: Collection<String>): Set<String>

    fun getInstallInfo(packageName: String): AppInstallInfo
}
