import { html, nothing } from "lit";

export const PALETTES = ["captured", "dynamic", "android-vivid", "android", "blue", "green", "purple", "rose", "monochrome"];
export const paletteLabel = (p) => (p === "captured" ? "As captured" : p);

/** Icons for the two Android system navigation modes, shared by every hub control. */
export const NAV_ICONS = { gesture: "swipe_up", buttons: "more_horiz" };
export const THEME_OPTIONS = [["system", "System", "brightness_auto"], ["light", "Light", "light_mode"], ["dark", "Dark", "dark_mode"]];
export const NAV_OPTIONS = [["gesture", "Gesture", NAV_ICONS.gesture], ["buttons", "3-button", NAV_ICONS.buttons]];

const fmt = (c, v) => (typeof v === "number" ? `${Number.isInteger(v) ? v : v.toFixed(2)}${c.unit ?? ""}` : String(v));

/** Builds a tweak panel from a screen's `controls` list. `apply(patch)` updates the screen. */
export const controlPanel = (controls = [], state, apply) => {
  const withExtra = (c, v) => ({ [c.key]: v, ...(typeof c.patch === "function" ? c.patch(v) : c.patch ?? {}) });
  return controls.map((c) => {
    if (c.type === "presets") {
      return html`<div class="p-control">
        <div class="t-label-large c-variant">${c.label}</div>
        <md-chip-set>${c.options.map(([label, patch]) => html`<md-assist-chip label=${label} @click=${() => apply(patch)}></md-assist-chip>`)}</md-chip-set>
      </div>`;
    }
    if (c.type === "choice") {
      return html`<div class="p-control">
        <div class="t-label-large c-variant">${c.label}</div>
        <md-chip-set>
          ${c.options.map(
            (o) => html`<md-filter-chip label=${String(o)} .selected=${state[c.key] === o} @click=${(e) => { e.currentTarget.selected = true; apply(withExtra(c, o)); }}></md-filter-chip>`,
          )}
        </md-chip-set>
      </div>`;
    }
    if (c.type === "toggle") {
      return html`<label class="p-control p-control--row t-label-large"
        ><md-switch ?selected=${!!state[c.key]} @change=${(e) => apply(withExtra(c, e.target.selected))}></md-switch>${c.label}</label
      >`;
    }
    return html`<div class="p-control">
      <div class="p-control__row t-label-large"><span class="c-variant">${c.label}</span><span>${fmt(c, state[c.key])}</span></div>
      <md-slider
        min=${c.min}
        max=${c.max}
        step=${c.step ?? 1}
        .value=${state[c.key]}
        @input=${(e) => apply(withExtra(c, Number(e.target.value)))}
      ></md-slider>
    </div>`;
  });
};

export const chips = (items, current, onPick) => html`<md-chip-set>
  ${items.map(([value, label]) => html`<md-filter-chip label=${label} .selected=${current === value} @click=${(e) => { e.currentTarget.selected = true; onPick(value); }}></md-filter-chip>`)}
</md-chip-set>`;

export const maybe = (cond, tpl) => (cond ? tpl : nothing);

/** Single-select segmented controls use the Labs set's selection event and child labels. */
export const segments = (items, current, onPick, label) => html`
  <md-outlined-segmented-button-set aria-label=${label}
    @segmented-button-set-selection=${(e) => { if (e.detail.selected) onPick(items[e.detail.index][0]); }}>
    ${items.map(([value, text, icon]) => html`<md-outlined-segmented-button label=${text} .selected=${value === current}
      ?has-icon=${!!icon}>${icon ? html`<md-icon slot="icon">${icon}</md-icon>` : nothing}</md-outlined-segmented-button>`)}
  </md-outlined-segmented-button-set>`;

/** Outlined select without a floating label; the caller supplies a visible label beside it. */
export const materialSelect = (label, items, value, onPick) => html`
  <md-outlined-select aria-label=${label} .value=${value} menu-positioning="fixed" @change=${(e) => onPick(e.target.value)}>
    ${items.map(([key, text]) => html`<md-select-option value=${key} ?selected=${key === value}>
      <div slot="headline">${text}</div></md-select-option>`)}
  </md-outlined-select>`;
