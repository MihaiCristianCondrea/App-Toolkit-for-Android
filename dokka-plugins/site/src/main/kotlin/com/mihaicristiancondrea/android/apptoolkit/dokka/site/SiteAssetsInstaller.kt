package com.mihaicristiancondrea.android.apptoolkit.dokka.site

import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.transformers.pages.PageTransformer
import java.io.File

internal const val SITE_SCRIPT = "scripts/site_deferred.js"
internal const val SITE_STYLES = "styles/site.css"

object SiteAssetsInstaller : PageTransformer {
    override fun invoke(input: RootPageNode): RootPageNode =
        input.transformContentPagesTree {
            it.modified(embeddedResources = (it.embeddedResources + listOf(SITE_SCRIPT, SITE_STYLES)).distinct())
        }

    // Partials carry relative asset references. Only a complete publication writes the shared files.
    internal fun publish(root: File, hasGuides: Boolean) {
        root.resolve(SITE_SCRIPT).writeText("const toolkitHasGuides = $hasGuides;\n" + siteResource("site.js"))
        root.resolve(SITE_STYLES).writeText(siteResource("site.css"))
    }
}

internal fun siteResource(name: String): String =
    checkNotNull(SiteAssetsInstaller::class.java.getResourceAsStream("/site/$name")) {
        "Missing Dokka site resource: $name"
    }.bufferedReader().use { it.readText() }
