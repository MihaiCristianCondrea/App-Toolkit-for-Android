import { html } from "lit";

// The app's shell (status bar, app bars, navigation, insets, drawers and sheets) is Material Next's Android
// preview, declared once in kit/app.js. These are the small pieces screens still draw themselves.

/**
 * Material Symbol icon (https://fonts.google.com/icons). `fill` mirrors Compose's Filled
 * variants; `rounded` uses Material Symbols Rounded.
 */
export const icon = (name, { fill = false, size, color, cls = "", rounded = false } = {}) =>
  html`<md-icon
    class=${cls}
    style="${size ? `--md-icon-size:${size}px;` : ""}${color ? `color:${color};` : ""}${fill ? "font-variation-settings:'FILL' 1;" : ""}${rounded ? "--md-icon-font:'Material Symbols Rounded';" : ""}"
    >${name}</md-icon
  >`;

/**
 * Material Web icon button. `selectedIcon` makes it a toggle (`selected` drives its state);
 * the selected icon is drawn filled, as Compose's Filled icons are.
 */
export const iconButton = ({ name, label, onClick, selected, selectedIcon, disabled = false, cls = "" }) =>
  selectedIcon
    ? html`<md-icon-button class=${cls} toggle .selected=${Boolean(selected)} ?disabled=${disabled} aria-label=${label}
        @click=${(e) => { e.stopPropagation(); onClick?.(e); }}>
        <md-icon>${name}</md-icon><md-icon slot="selected" class="sc-filled">${selectedIcon}</md-icon>
      </md-icon-button>`
    : html`<md-icon-button class=${cls} ?disabled=${disabled} aria-label=${label}
        @click=${(e) => { e.stopPropagation(); onClick?.(e); }}><md-icon>${name}</md-icon></md-icon-button>`;
