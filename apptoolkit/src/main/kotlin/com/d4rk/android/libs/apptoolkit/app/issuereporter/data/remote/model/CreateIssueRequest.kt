package com.d4rk.android.libs.apptoolkit.app.issuereporter.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateIssueRequest(
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
    @SerialName("labels") val labels: List<String>? = null,
    @SerialName("assignees") val assignees: List<String>? = null,
)
