"""Add guide pages and Markdown downloads to the generated Dokka site."""

import argparse
from html import escape
from pathlib import Path
from shutil import copyfile
from urllib.parse import urljoin

from bs4 import BeautifulSoup, NavigableString
from markdown_it import MarkdownIt
from markdownify import markdownify

SOURCE = Path(__file__).resolve().parent
SITE_URL = "https://mihaicristiancondrea.github.io/App-Toolkit-for-Android/"
GUIDES = {
    "getting-started": "Getting Started",
    "shell-and-navigation": "Shell and Navigation",
}


def toolbar(document, prefix, markdown_name):
    nav = document.new_tag("nav", attrs={"class": "toolkit-docs-toolbar", "aria-label": "Documentation"})
    for path, title in (
        ("index.html", "API reference"),
        *((f"guides/{slug}.html", title) for slug, title in GUIDES.items()),
    ):
        link = document.new_tag("a", href=prefix + path)
        link.string = title
        nav.append(link)
    raw = document.new_tag("a", href=markdown_name, attrs={"data-markdown": ""})
    raw.string = "View raw Markdown"
    nav.append(raw)
    return nav


def decorate(document, body, page, root):
    prefix = "../" * (len(page.relative_to(root).parts) - 1)
    body.insert(0, toolbar(document, prefix, page.with_suffix(".md").name))
    document.head.append(document.new_tag(
        "link", rel="stylesheet", href=prefix + "toolkit-docs/site.css"
    ))
    document.body.append(document.new_tag(
        "script", type="module", src=prefix + "toolkit-docs/site.js"
    ))
    page.write_text(str(document), encoding="utf-8")


def export_api(page, root):
    document = BeautifulSoup(page.read_text(encoding="utf-8"), "html.parser")
    body = document.select_one("#main #content")
    if body is None:
        return False

    export = BeautifulSoup(str(body), "html.parser")
    for element in export.select("button, script, style, .breadcrumbs"):
        element.decompose()
    base = urljoin(SITE_URL, page.relative_to(root).as_posix())
    for link in export.select("a[href]"):
        link["href"] = urljoin(base, link["href"])
    for image in export.select("img[src]"):
        image["src"] = urljoin(base, image["src"])
    for signature in export.select(".symbol"):
        sources = signature.select(".source-link-wrapper")
        for source in sources:
            signature.insert_after(source.extract())
        code = export.new_tag("code", attrs={"class": "language-kotlin"})
        for annotation in signature.select(":scope > .block"):
            annotation.insert_after(NavigableString("\n"))
        code.string = signature.get_text().strip()
        block = export.new_tag("pre")
        block.append(code)
        signature.replace_with(block)
    content = markdownify(
        str(export), heading_style="ATX", bullets="-",
        code_language_callback=lambda code: next(
            (name.removeprefix("language-") for name in (code.find("code") or code).get("class", [])
             if name.startswith("language-")), ""
        ),
    ).strip()
    page.with_suffix(".md").write_text(content + "\n", encoding="utf-8")
    decorate(document, body, page, root)
    return True


def render_guides(root):
    directory = root / "guides"
    directory.mkdir(exist_ok=True)
    parser = MarkdownIt("commonmark").enable("table")
    for slug, title in GUIDES.items():
        source = SOURCE / f"{slug}.md"
        html = parser.render(source.read_text(encoding="utf-8"))
        document = BeautifulSoup(
            f'<!doctype html><html lang="en"><head><meta charset="utf-8">'
            f'<meta name="viewport" content="width=device-width, initial-scale=1">'
            f'<title>{escape(title)} | App Toolkit</title></head>'
            f'<body class="toolkit-guide"><main>{html}</main></body></html>',
            "html.parser",
        )
        copyfile(source, directory / source.name)
        decorate(document, document.main, directory / f"{slug}.html", root)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("output", type=Path)
    root = parser.parse_args().output
    index = root / "index.html"
    if not index.is_file() or not export_api(index, root):
        parser.error("expected a generated Dokka index with #main #content")

    for page in root.glob("library/**/*.html"):
        export_api(page, root)
    render_guides(root)
    assets = root / "toolkit-docs"
    assets.mkdir(exist_ok=True)
    for name in ("site.css", "site.js"):
        copyfile(SOURCE / name, assets / name)


if __name__ == "__main__":
    main()
