package com.mihaicristiancondrea.android.apptoolkit.dokka.site

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import java.net.URI

private val guideNames = listOf("getting-started", "shell-and-navigation")

internal fun publishGuides(root: File) {
    val directory = root.resolve("guides").apply { mkdirs() }
    val options = MutableDataSet()
        .set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
        .set(HtmlRenderer.RENDER_HEADER_ID, true)
    val parser = Parser.builder(options).build()
    val renderer = HtmlRenderer.builder(options).build()

    val pages = guideNames.map { name ->
        val markdown = siteResource("guides/$name.md")
        val document = Jsoup.parse(renderer.render(parser.parse(markdown)))
        document.selectFirst("html")?.attr("lang", "en")
        document.title("${document.selectFirst("h1")?.text() ?: name} | App Toolkit")
        document.head().appendElement("meta").attr("charset", "utf-8")
        document.head().appendElement("meta").attr("name", "viewport").attr("content", "width=device-width, initial-scale=1")
        document.head().appendElement("link").attr("rel", "stylesheet").attr("href", "../$SITE_STYLES")
        document.head().appendElement("script").attr("src", "../$SITE_SCRIPT").attr("defer", "")
        document.select("pre > code.language-mermaid").forEach { code ->
            code.parent()!!.addClass("mermaid").text(code.wholeText())
        }

        val body = document.body().addClass("toolkit-guide")
        val main = Element("main").id("main")
        val content = main.appendElement("div").id("content")
        content.appendChildren(body.childNodesCopy())
        body.empty().appendChild(main)

        val page = directory.resolve("$name.html")
        page.writeText("<!doctype html>\n${document.outerHtml()}")
        directory.resolve("$name.md").writeText(markdown)
        page to content.select("a[href], img[src]")
    }
    for ((page, links) in pages) {
        for (link in links) {
            val reference = link.attr(if (link.normalName() == "a") "href" else "src")
            requireGuideLink(root, page, reference)
        }
    }
}

private fun requireGuideLink(root: File, page: File, reference: String) {
    val target = URI(reference)
    if (target.isAbsolute || target.rawAuthority != null) return

    val relativePath = target.path.orEmpty()
    val file = when {
        relativePath.isEmpty() -> page
        relativePath.startsWith("/") -> root.resolve(relativePath.trimStart('/'))
        else -> page.parentFile.resolve(relativePath)
    }.canonicalFile
    val destination = if (file.isDirectory) file.resolve("index.html") else file
    val fragment = target.fragment.orEmpty()
    val exists = file.toPath().startsWith(root.canonicalFile.toPath()) && destination.isFile
    val anchorExists = fragment.isEmpty() || (
        destination.extension == "html" && exists &&
            Jsoup.parse(destination, "UTF-8").getElementById(fragment) != null
    )
    check(exists && anchorExists) {
        "Broken guide link in ${page.relativeTo(root).invariantSeparatorsPath}: $reference"
    }
}
