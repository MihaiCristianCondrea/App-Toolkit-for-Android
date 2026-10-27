/**
 * Framework-free entry for motion tools (Remotion, Playwright, anything with a DOM).
 *
 *   import { setup, mount, SCREENS } from "marketing/ui/src/embed.js";
 *   await setup();
 *   await mount(element, "numbers", { activeSheet: "counter" });
 */
import { render, html } from "lit";
import { setAssetBase } from "./kit/assets.js";
import { device, icon } from "./kit/shell.js";
import { WINDOWS, windowOf, shellLayout } from "./kit/layout.js";
import { loadFonts } from "./theme/index.js";
import { byId, SCREENS } from "./screens/index.js";
import {
  shapeBadge,
  toolMetaChips,
  renderToolBottomSheet,
} from "./kit/components.js";

export { SCREENS, byId, WINDOWS, shellLayout };

export const PARTS = {
  shapeBadge,
  toolMetaChips,
  renderToolBottomSheet,
  icon,
};

export const setup = async ({ assetBase = "/", palette, theme } = {}) => {
  setAssetBase(assetBase);
  document.documentElement.dataset.motion = "frame";
  if (palette) document.documentElement.dataset.palette = palette;
  if (theme) document.documentElement.dataset.theme = theme;
  await loadFonts();
  await customElements.whenDefined("md-icon");
};

const settle = async (root) => {
  for (let i = 0; i < 3; i++) {
    const els = [...root.querySelectorAll("*")].filter((e) => e.updateComplete);
    await Promise.all(els.map((e) => e.updateComplete));
  }
};

export const mount = async (el, id, state = {}, { frame = false, palette, window: win, showAds = false } = {}) => {
  const s = byId(id);
  if (!s) throw new Error(`Unknown screen "${id}". Known: ${SCREENS.map((x) => x.id).join(", ")}`);
  const p = palette === "captured" ? s.palette : palette;
  if (p) el.dataset.palette = p;
  const w = win ?? state.window;
  const tpl = s.render({ ...s.defaults, ...state, window: w, showAds: state.showAds ?? showAds });
  const name = windowOf(w).device;
  render(frame && name ? device(tpl, { device: name }) : tpl, el);
  await settle(el);
};

export const mountPart = async (el, build, { palette } = {}) => {
  if (palette) el.dataset.palette = palette;
  render(html`<div class="sc-part">${build(PARTS)}</div>`, el);
  await settle(el);
};
