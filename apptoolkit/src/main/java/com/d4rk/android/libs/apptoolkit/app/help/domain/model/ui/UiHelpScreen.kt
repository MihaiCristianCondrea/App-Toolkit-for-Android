package com.d4rk.android.libs.apptoolkit.app.help.domain.model.ui

import androidx.compose.runtime.Immutable
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.UiHelpQuestion
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class UiHelpScreen(
    val questions: ImmutableList<UiHelpQuestion> = persistentListOf()
)

