package com.d4rk.android.libs.apptoolkit.app.issuereporter.data.mapper

import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.remote.model.CreateIssueRequest
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report

fun Report.toCreateIssueRequest(): CreateIssueRequest =
    CreateIssueRequest(
        title = title,
        body = getDescription(),
        labels = listOf("bug", "from-mobile"),
    )
