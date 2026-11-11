package com.mihaicristiancondrea.android.apptoolkit.dokka.site

import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.DokkaPluginApiPreview
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement

class SitePlugin : DokkaPlugin() {
    private val dokkaBase by lazy { plugin<DokkaBase>() }

    val mermaidCodeBlocks by extending {
        dokkaBase.htmlCodeBlockRenderers with MermaidCodeBlockRenderer
    }

    val siteAssets by extending {
        dokkaBase.htmlPreprocessors with SiteAssetsInstaller order { after(dokkaBase.scriptsInstaller) }
    }

    val siteExport by extending {
        CoreExtensions.postActions providing ::MarkdownExportAction
    }

    @DokkaPluginApiPreview
    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement =
        PluginApiPreviewAcknowledgement
}
