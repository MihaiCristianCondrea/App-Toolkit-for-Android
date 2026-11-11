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
