package com.mihaicristiancondrea.android.apptoolkit.dokka.mermaid

import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.DokkaPluginApiPreview
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement

class MermaidPlugin : DokkaPlugin() {
    private val dokkaBase by lazy { plugin<DokkaBase>() }

    val mermaidCodeBlocks by extending {
        dokkaBase.htmlCodeBlockRenderers with MermaidCodeBlockRenderer
    }

    val mermaidScript by extending {
        dokkaBase.htmlPreprocessors with MermaidScriptInstaller order { after(dokkaBase.scriptsInstaller) }
    }

    val publishMermaidScript by extending {
        CoreExtensions.postActions providing ::PublishMermaidScript
    }

    @DokkaPluginApiPreview
    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement =
        PluginApiPreviewAcknowledgement
}
