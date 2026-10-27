/**
 * Interactive host for screens in the browser (gallery cards and the prototype page).
 *
 * A screen is a pure function of state. The host owns that state and gives the screen:
 *   state.set(patch)       merge a patch and re-render (clicks, drags, sliders)
 *   state.go(id, state)    navigate to another screen (handled by the page)
 */
import { render } from "lit";
import { keyed } from "lit/directives/keyed.js";
import { byId } from "./screens/index.js";
import { device } from "./kit/shell.js";
import { windowOf } from "./kit/layout.js";

export class ScreenHost {
  /**
   * @param {HTMLElement} el      where the screen renders
   * @param {object} opts         { id, state, palette ("captured" or a palette name), theme, frame, window (a WINDOWS key or { width, height, system }), showAds (ad stand-ins, off by default), onGo, onChange }
   */
  constructor(el, { id, state = {}, palette = "captured", theme, frame = false, navType = "gesture", window: win, showAds, onGo, onChange } = {}) {
    this.el = el;
    this.opts = { palette, theme, frame, navType, window: win, showAds, onGo, onChange };

    this.load(id, state);
  }

  load(id, state = {}) {
    this.screen = byId(id);
    if (!this.screen) return;
    this.state = { ...structuredClone(this.screen.defaults), ...state };
    this.applyTheme();
    this.draw();
  }

  reset() {
    this.load(this.screen.id);
    this.opts.onChange?.(this);
  }

  applyTheme() {
    const { palette, theme } = this.opts;
    const p = palette === "captured" ? this.screen.palette : palette;
    if (p) this.el.dataset.palette = p;
    else delete this.el.dataset.palette;
    const resolvedTheme = theme === "system"
      ? (window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light")
      : theme;
    if (resolvedTheme === "dark") this.el.dataset.theme = "dark";
    else delete this.el.dataset.theme;
  }

  destroy() {
    render(null, this.el);
  }

  setOptions(o) {
    if (Object.entries(o).every(([key, value]) => this.opts[key] === value)) return;
    Object.assign(this.opts, o);
    this.applyTheme();
    this.draw();
  }

  set = (patch) => {
    Object.assign(this.state, typeof patch === "function" ? patch(this.state) : patch);
    this.draw();
    this.opts.onChange?.(this);
  };

  go = (id, state) => (this.opts.onGo ? this.opts.onGo(id, state, this) : this.load(id, state));

  draw() {
    if (!this.screen) return;
    const navType = this.opts.navType ?? this.state.navType ?? "gesture";
    // The window is the host's, not the screen's: it stays the same as the screens change.
    const win = this.opts.window ?? this.state.window;
    // Ads, like the window, are the host's: marketing media never shows them, the gallery can to check them.
    const showAds = this.opts.showAds ?? this.state.showAds ?? false;
    const tpl = this.screen.render({ ...this.state, window: win, showAds, set: this.set, go: this.go, navType });
    render(keyed(this.screen.id, this.opts.frame ? device(tpl, { device: windowOf(win).device }) : tpl), this.el);
  }

}
