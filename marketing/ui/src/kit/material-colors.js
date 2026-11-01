import { html } from "lit";
import { ref } from "lit/directives/ref.js";

// MaterialColorMappers.kt: the app colour scheme's roles, in the order the app lists them.
const ROLES = ["primary", "on-primary", "primary-container", "secondary", "tertiary", "error", "background", "surface", "surface-variant", "outline", "inverse-surface", "scrim"];
// The widget's strip: the three accents and their containers.
export const STRIP = ["primary", "primary-container", "secondary", "secondary-container", "tertiary", "tertiary-container"];

// A canvas turns any CSS colour (rgb(), color(srgb ...), oklch()) into its 8-bit channels.
const channels = (css) => {
  const ctx = (channels.ctx ??= document.createElement("canvas").getContext("2d", { willReadFrequently: true }));
  ctx.clearRect(0, 0, 1, 1);
  ctx.fillStyle = css;
  ctx.fillRect(0, 0, 1, 1);
  return [...ctx.getImageData(0, 0, 1, 1).data];
};
const luminance = (rgb) => {
  const [r, g, b] = rgb.map((v) => { const c = v / 255; return c <= 0.03928 ? c / 12.92 : ((c + 0.055) / 1.055) ** 2.4; });
  return 0.2126 * r + 0.7152 * g + 0.0722 * b;
};
// Each swatch reads its own colour once it is on the page, so its hex and ink follow the palette the screen is in.
const label = (el) => {
  if (!el) return;
  requestAnimationFrame(() => {
    if (!el.isConnected) return;
    const [r, g, b, a] = channels(getComputedStyle(el).backgroundColor);
    const surface = channels(getComputedStyle(el.closest('.mc-sheet')).backgroundColor);
    const visible = [r, g, b].map((v, i) => v * a / 255 + surface[i] * (1 - a / 255));
    el.style.color = luminance(visible) > 0.179 ? "#000" : "#fff";
    const hex = "#" + [a, r, g, b].map((v) => v.toString(16).padStart(2, "0")).join("").toUpperCase();
    el.querySelector(".mc-swatch__hex").textContent = hex;
    el.setAttribute('aria-label', `${el.dataset.role.replaceAll('-', ' ')} ${hex}, copy color`);
  });
};

/** MaterialColorsToolDialog.kt: the full-height sheet with the app colour scheme, each swatch named with its hex. */
export function materialColorsSheet(onClose, badge) {
  const copy = async (event) => {
    const button = event.currentTarget;
    const hex = button.querySelector('.mc-swatch__hex').textContent;
    if (!/^#[0-9A-F]{8}$/.test(hex)) return;
    const feedback = button.closest('.mc-sheet').querySelector('.mc-sheet__feedback');
    try {
      await navigator.clipboard.writeText(hex);
      feedback.textContent = `Copied ${hex}`;
    } catch {
      feedback.textContent = `Copy unavailable. ${hex}`;
    }
  };
  return html`<div class="mc-sheet">
    <div class="mc-sheet__head"><div class="mc-sheet__badge" aria-hidden="true">${badge}</div><h3>Material Colors</h3><md-icon-button aria-label="Close" @click=${() => onClose?.()}><md-icon>close</md-icon></md-icon-button></div>
    <div class="mc-sheet__body">
    <p class="mc-sheet__text">Compare the current application color scheme with Android system accent and neutral palettes.</p>
    <section class="mc-table"><h4>App color scheme</h4>
      <div class="mc-table__grid">${ROLES.map((role) => html`<button type="button" class="mc-swatch" data-role=${role} style="background:var(--md-sys-color-${role})" @click=${copy} ${ref(label)}>
        <span class="mc-swatch__name">${role.replaceAll("-", " ").toUpperCase()}</span><span class="mc-swatch__hex"></span></button>`)}</div>
    </section>
    <p class="mc-sheet__feedback" role="status" aria-live="polite"></p>
    </div>
  </div>`;
}
