package com.mihaicristiancondrea.android.apptoolkit.buildlogic

import java.io.File
import java.net.URI
import java.util.Locale
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

abstract class ReadmeModuleDocTask : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val readme: RegularFileProperty

    @get:Input
    abstract val moduleName: Property<String>

    @get:OutputFile
    abstract val output: RegularFileProperty

    init {
        // Linked files can change independently of this module's README.
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun generate() {
        val source = readme.get().asFile
        val lines = source.readLines().dropWhile { !it.startsWith("# ") }
        if (lines.isEmpty()) throw GradleException("README has no top-level heading: $source")
        var fence: String? = null
        val body = mutableListOf<String>()
        val prose = mutableListOf<String>()

        fun flushProse() {
            if (prose.isEmpty()) return
            body += inlineLink.replace(prose.joinToString("\n")) { link ->
                val target = link.groupValues[2]
                val uri = URI(target)
                if (uri.isAbsolute || uri.rawAuthority != null) {
                    link.value
                } else {
                    validateLink(source, target, uri)
                    link.groupValues[1].trim().replace(whitespace, " ")
                }
            }
            prose.clear()
        }

        for (line in lines.drop(1)) {
            val marker = fencePattern.find(line)?.groupValues?.get(1)
            val openFence = fence
            if (openFence != null) {
                body += line
                if (marker != null && marker.startsWith(openFence) &&
                    line.trim().all { it == openFence.first() }
                ) fence = null
            } else if (marker != null) {
                flushProse()
                fence = marker
                body += line
            } else {
                // Dokka's fragment splitter also matches nested headings beginning with
                // Module or Package. Emphasis preserves the visible title.
                val heading = reservedHeading.replace(line) { match ->
                    "${match.groupValues[1]}**${match.groupValues[2]}**"
                }
                prose += if (headingPattern.containsMatchIn(heading) && !heading.startsWith("######")) {
                    "#$heading"
                } else {
                    heading
                }
            }
        }
        flushProse()
        output.get().asFile.apply {
            parentFile.mkdirs()
            writeText("# Module ${moduleName.get()}\n\n" + body.joinToString("\n") + "\n")
        }
    }

    private fun validateLink(source: File, target: String, uri: URI) {
        val path = uri.path.orEmpty()
        val file = if (path.isEmpty()) source else source.parentFile.resolve(path)
        val anchor = uri.fragment
        if (!file.exists() || (anchor != null && file.extension == "md" && !hasHeading(file, anchor))) {
            throw GradleException("Broken README link '$target' in $source")
        }
    }

    private fun hasHeading(file: File, anchor: String): Boolean {
        val headings = mutableSetOf<String>()
        for (line in file.readLines()) {
            val text = headingPattern.find(line)?.groupValues?.get(1) ?: continue
            val slug = text.lowercase(Locale.ROOT)
                .replace(headingPunctuation, "")
                .replace(' ', '-')
            var candidate = slug
            var suffix = 1
            while (!headings.add(candidate)) candidate = "$slug-${suffix++}"
        }
        return anchor in headings
    }

    private companion object {
        val fencePattern = Regex("""^ {0,3}(`{3,}|~{3,})""")
        val reservedHeading = Regex("""^(#{1,6}\s+)(Module|Package)""")
        val headingPattern = Regex("""^#{1,6}\s+(.+?)\s*#*\s*$""")
        val inlineLink = Regex("""!?\[([^\]]+)]\(([^)]+)\)""")
        val whitespace = Regex("""\s+""")
        val headingPunctuation = Regex("""[^\p{L}\p{N}_ -]""")
    }
}
