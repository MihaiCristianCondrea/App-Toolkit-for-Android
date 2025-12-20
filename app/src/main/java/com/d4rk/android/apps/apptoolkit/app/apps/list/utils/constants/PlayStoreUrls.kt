package com.d4rk.android.apps.apptoolkit.app.apps.list.utils.constants

import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks

object PlayStoreUrls {
    private const val DEVELOPER_ID = "5390214922640123642"

    const val DEVELOPER_PAGE =
        "${AppLinks.PLAY_STORE_MAIN}store/apps/dev?id=$DEVELOPER_ID&hl=en" // FIXME: Unused

    const val DEFAULT_ICON_URL = "https://c.clc2l.com/t/g/o/google-playstore-Iauj7q.png"

    const val DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36"
    const val DEFAULT_LANGUAGE = "en-US,en;q=0.9" // FIXME: unused
}