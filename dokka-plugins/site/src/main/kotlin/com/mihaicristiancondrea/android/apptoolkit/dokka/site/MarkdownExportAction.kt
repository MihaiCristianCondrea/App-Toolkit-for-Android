package com.mihaicristiancondrea.android.apptoolkit.dokka.site

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter
import com.vladsch.flexmark.util.data.MutableDataSet
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.renderers.PostAction
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

private const val PUBLISHED_SITE = "https://mihaicristiancondrea.github.io/App-Toolkit-for-Android/"

class MarkdownExportAction(private val context: DokkaContext) : PostAction {
    override fun invoke() {
        if (context.configuration.delayTemplateSubstitution) return

        val root = context.configuration.outputDir
        val homepage = root.resolve("index.html").takeIf { context.configuration.modules.isNotEmpty() }
        SiteAssetsInstaller.publish(root)

        val converter = FlexmarkHtmlConverter.builder(
            MutableDataSet().set(FlexmarkHtmlConverter.OUTPUT_ATTRIBUTES_ID, false)
        ).build()
        root.walkTopDown().filter { it.isFile && it.extension == "html" }.forEach { page ->
            val baseUrl = PUBLISHED_SITE + page.relativeTo(root).invariantSeparatorsPath
            val document = Jsoup.parse(page, "UTF-8", baseUrl)
            val content = document.selectFirst("#main #content") ?: return@forEach
            if (page == homepage) {
                prependHomepageReadme(content, root)
                document.outputSettings().prettyPrint(false)
                page.writeText(document.outerHtml())
            }
            prepareMarkdown(content)
            page.resolveSibling("${page.nameWithoutExtension}.md")
                .writeText(converter.convert(content).trim() + "\n")
        }
    }

    private fun prepareMarkdown(content: Element) {
        content.select("button, script, style, .breadcrumbs").remove()
        for (element in content.select("a[href], img[src]")) {
            val attribute = if (element.normalName() == "a") "href" else "src"
            val absolute = element.absUrl(attribute)
            if (absolute.isNotEmpty()) element.attr(attribute, absolute)
        }

        for (block in content.select("pre.mermaid")) {
            val source = block.wholeText()
            block.empty().appendElement("code").addClass("language-mermaid").text(source)
        }
        for (code in content.select("pre > code")) {
            val language = code.classNames().firstOrNull { it.startsWith("lang-") }
            if (language != null) code.attr("class", "language-${language.removePrefix("lang-")}")
        }
        for (signature in content.select(".symbol")) {
            for (source in signature.select(".source-link-wrapper")) {
                source.remove()
                signature.after(source)
            }
            for (annotation in signature.children().filter { it.hasClass("block") }) {
                annotation.after(TextNode("\n"))
            }
            val block = Element("pre")
            block.appendElement("code").addClass("language-kotlin").text(signature.wholeText().trim())
            signature.replaceWith(block)
        }
    }
}
