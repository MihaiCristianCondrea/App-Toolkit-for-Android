package com.mihaicristiancondrea.android.apptoolkit.dokka.site

import kotlinx.html.FlowContent
import kotlinx.html.pre
import org.jetbrains.dokka.base.renderers.html.HtmlCodeBlockRenderer

object MermaidCodeBlockRenderer : HtmlCodeBlockRenderer {
    override fun isApplicableForDefinedLanguage(language: String): Boolean =
        language.equals("mermaid", ignoreCase = true)

    override fun isApplicableForUndefinedLanguage(code: String): Boolean = false

    override fun FlowContent.buildCodeBlock(language: String?, code: String) {
        pre(classes = "mermaid") { +code }
    }
}
