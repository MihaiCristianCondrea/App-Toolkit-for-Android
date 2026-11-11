package com.mihaicristiancondrea.android.apptoolkit.buildlogic

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask

fun Project.checkDokkaWarnings() {
    tasks.withType<DokkaGenerateTask>().configureEach {
        doLast(CheckDokkaWarnings)
    }
}

private object CheckDokkaWarnings : Action<Task> {
    override fun execute(task: Task) {
        val log = (task as DokkaGenerateTask).workerLogFile.get().asFile
        val warnings = log.useLines { lines ->
            lines.filter { it.startsWith("[WARN]") }
                .filterNot {
                    it.contains("`androidx.lifecycle.Lifecycle.State` is unresolved in androidx.core.view/MenuHost/addMenuProvider/")
                }
                .distinct()
                .toList()
        }
        if (warnings.isNotEmpty()) {
            throw GradleException("Unexpected Dokka warnings in ${task.path}:\n" + warnings.joinToString("\n"))
        }
    }
}
