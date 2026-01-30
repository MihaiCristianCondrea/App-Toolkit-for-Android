package com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseDto(
    @SerialName("data") val data: AppDataWrapperDto = AppDataWrapperDto()
)

@Serializable
data class AppDataWrapperDto(
    @SerialName("apps") val apps: List<AppInfoDto> = emptyList()
)
