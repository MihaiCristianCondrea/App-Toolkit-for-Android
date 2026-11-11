const content = document.querySelector("#main #content");
if (content) {
    const toolbar = document.createElement("nav");
    toolbar.className = "toolkit-docs-toolbar";
    toolbar.setAttribute("aria-label", "Documentation");
    content.prepend(toolbar);

    const markdown = new URL(location.href);
    markdown.pathname = markdown.pathname.endsWith("/")
        ? markdown.pathname + "index.md"
        : markdown.pathname.replace(/\.html$/, ".md");
    markdown.search = "";
    markdown.hash = "";
    const rawLink = document.createElement("a");
    rawLink.href = markdown.href;
    rawLink.textContent = "View raw Markdown";
    toolbar.append(rawLink);
    if (navigator.clipboard?.writeText) {
        const button = document.createElement("button");
        button.type = "button";
        button.textContent = "Copy page as Markdown";
        const status = document.createElement("span");
        status.setAttribute("role", "status");
        rawLink.before(button);
        rawLink.after(status);

        button.addEventListener("click", async () => {
            button.disabled = true;
            status.textContent = "";
            try {
                const response = await fetch(rawLink.href);
                if (!response.ok) throw new Error("Markdown download failed");
                await navigator.clipboard.writeText(await response.text());
                status.textContent = "Copied";
            } catch {
                status.textContent = "Could not copy. Open raw Markdown to copy it.";
            } finally {
                button.disabled = false;
            }
        });
    }
}

(async () => {
    const diagrams = [...document.querySelectorAll("pre.mermaid")];
    if (diagrams.length) {
        try {
            const { default: mermaid } = await import(
                "https://cdn.jsdelivr.net/npm/mermaid@12.3.0/dist/mermaid.esm.min.mjs"
            );
            mermaid.initialize({ startOnLoad: false, securityLevel: "strict" });
            for (const [index, diagram] of diagrams.entries()) {
                try {
                    const { svg } = await mermaid.render(`toolkit-diagram-${index}`, diagram.textContent);
                    const figure = document.createElement("figure");
                    figure.className = "toolkit-diagram";
                    figure.innerHTML = svg;
                    (diagram.closest("pre") ?? diagram).replaceWith(figure);
                } catch (error) {
                    console.warn("Could not render Mermaid diagram", error);
                }
            }
        } catch (error) {
            console.warn("Could not load Mermaid", error);
        }
    }
})();
