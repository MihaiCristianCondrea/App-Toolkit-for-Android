package com.mihaicristiancondrea.android.apptoolkit.dokka.mermaid

import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.transformers.pages.PageTransformer
import org.jetbrains.dokka.renderers.PostAction

private const val SCRIPT_PATH = "scripts/mermaid-loader_deferred.js"

object MermaidScriptInstaller : PageTransformer {
    override fun invoke(input: RootPageNode): RootPageNode =
        input.transformContentPagesTree {
            it.modified(embeddedResources = it.embeddedResources + SCRIPT_PATH)
        }
}

class PublishMermaidScript(private val context: DokkaContext) : PostAction {
    override fun invoke() {
        // Partial pages carry the script reference. The publication owns the shared asset.
        if (context.configuration.delayTemplateSubstitution) return

        val output = context.configuration.outputDir.resolve(SCRIPT_PATH)
        output.parentFile.mkdirs()
        checkNotNull(javaClass.getResourceAsStream("/mermaid-loader.js")).use { input ->
            output.outputStream().use { input.copyTo(it) }
        }
    }
}
