const rawLink = document.querySelector("[data-markdown]");
if (rawLink && navigator.clipboard?.writeText) {
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

const diagrams = [...document.querySelectorAll("pre > code.language-mermaid")];
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
                diagram.parentElement.replaceWith(figure);
            } catch (error) {
                console.warn("Could not render Mermaid diagram", error);
            }
        }
    } catch (error) {
        console.warn("Could not load Mermaid", error);
    }
}
