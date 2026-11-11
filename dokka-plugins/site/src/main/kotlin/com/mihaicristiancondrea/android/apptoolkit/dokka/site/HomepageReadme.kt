package com.mihaicristiancondrea.android.apptoolkit.dokka.site

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import org.jsoup.nodes.Element
import java.io.File
import java.net.URI

private const val REPOSITORY_FILES =
    "https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/blob/develop/"
private const val REPOSITORY_ASSETS =
    "https://raw.githubusercontent.com/MihaiCristianCondrea/App-Toolkit-for-Android/develop/"

internal fun prependHomepageReadme(content: Element, output: File) {
    val options = MutableDataSet()
        .set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
        .set(HtmlRenderer.RENDER_HEADER_ID, true)
    val markdown = Parser.builder(options).build().parse(siteResource("README.md"))
    val readme = Element("section").addClass("toolkit-readme")
        .html(HtmlRenderer.builder(options).build().render(markdown))

    for (link in readme.select("a[href]")) {
        val target = URI(link.attr("href"))
        val path = target.path.orEmpty()
        if (target.isAbsolute || target.rawAuthority != null || path.isEmpty()) continue

        val modulePage = path.removeSuffix("README.md") + "index.html"
        val documentedModule = path.startsWith("library/") && path.endsWith("/README.md") &&
            target.rawFragment == null && target.rawQuery == null && output.resolve(modulePage).isFile
        val destination = if (documentedModule) {
            modulePage
        } else {
            URI(REPOSITORY_FILES).resolve(target).toString()
        }
        link.attr("href", destination)
    }
    for (image in readme.select("img[src]")) {
        image.attr("src", URI(REPOSITORY_ASSETS).resolve(image.attr("src")).toString())
    }
    for (code in readme.select("pre > code.language-mermaid")) {
        code.parent()!!.addClass("mermaid").text(code.wholeText())
    }
    content.prependChild(readme)
}
