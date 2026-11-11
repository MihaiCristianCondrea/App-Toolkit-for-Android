(async () => {
    const diagrams = [...document.querySelectorAll("pre.mermaid, pre > code.language-mermaid")];
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
