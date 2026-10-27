/**
 * Design System Specifications & Architecture Page
 * 
 * Comprehensive reference for Material Design 3 Expressive tokens,
 * device geometry, surface tiers, and system navigation in App Toolkit.
 */
import { html, render } from "lit";
import { loadFonts } from "./theme/index.js";
import "./theme/hub.js";
import "./gallery.css";
import {setHubTheme} from "material-next-web-components/marketing.js";
import {referenceShell} from "./reference-shell.js";
import { PALETTES, paletteLabel } from "./controls.js";
import { PRODUCT_CONFIG } from "./config/product.js";

const view = {
  theme: PRODUCT_CONFIG.theme.defaultTheme ?? "system",
};

const isDark = () => {
  if (view.theme === "system") {
    return window.matchMedia("(prefers-color-scheme: dark)").matches;
  }
  return view.theme === "dark";
};

window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", () => {
  if (view.theme === "system") draw();
});

const draw = () => {
  const resolvedDark = isDark();
  setHubTheme(resolvedDark ? "dark" : "light");
  const root = document.documentElement;
  if (resolvedDark) root.dataset.theme = "dark";
  else root.dataset.theme = "light";

  render(
    referenceShell({heading: "Specifications", active: "specs", theme: view.theme, onTheme: theme => {view.theme = theme; draw();}}, html`

      <!-- Editorial Header -->
      <header class="g-header">
        <div class="g-header__inner">
          <span class="g-header__eyebrow">App Toolkit · Specifications</span>
          <h1 class="g-header__title">Design system specifications</h1>
          <p class="g-header__desc">
            Tokens, geometry, typography, and surface tiers for Material Design 3 Expressive and App Toolkit.
          </p>
        </div>
      </header>

      <!-- Specifications Main Container -->
      <main class="g-section">
        <!-- Target Device & System Metrics Grid -->
        <div class="g-section-header">
          <div>
            <h2 class="g-section-title">Core Architecture Metrics</h2>
            <p class="g-section-sub">Hardware geometry and framework dependencies</p>
          </div>
        </div>

        <div class="g-specs-grid">
          ${PRODUCT_CONFIG.specs.map(
            (sp) => html`
              <div class="g-spec-item">
                <span class="g-spec-item__label">${sp.label}</span>
                <span class="g-spec-item__val">${sp.value}</span>
              </div>
            `,
          )}
          <div class="g-spec-item">
            <span class="g-spec-item__label">App Toolkit Version</span>
            <span class="g-spec-item__val">${PRODUCT_CONFIG.toolkitVersion}</span>
          </div>
          <div class="g-spec-item">
            <span class="g-spec-item__label">Component Parity</span>
            <span class="g-spec-item__val">1:1 Material Web & Compose</span>
          </div>
        </div>

        <!-- M3 OLED Dark Surface Stack Breakdown -->
        <div class="g-surface-stack" style="margin-top:32px;">
          <div>
            <h3 class="t-title-medium" style="font-size:18px;font-weight:700;color:var(--g-text);">
              Material 3 Dark Theme Surface Stack
            </h3>
            <p class="t-body-small c-variant" style="margin-top:4px;line-height:1.5;">
              Calibrated luminance layers provide rich visual depth on OLED screens while eliminating harsh pure-black clipping.
            </p>
          </div>

          <div class="g-surface-tier-row">
            <div class="g-surface-tier-box" style="background:#090F12">
              <span class="g-surface-tier-box__title">Lowest</span>
              <span class="g-surface-tier-box__hex">#090F12</span>
              <span class="g-surface-tier-box__role">Deep background</span>
            </div>
            <div class="g-surface-tier-box" style="background:#171C20">
              <span class="g-surface-tier-box__title">Low</span>
              <span class="g-surface-tier-box__hex">#171C20</span>
              <span class="g-surface-tier-box__role">Page container</span>
            </div>
            <div class="g-surface-tier-box" style="background:#1B2024">
              <span class="g-surface-tier-box__title">Container</span>
              <span class="g-surface-tier-box__hex">#1B2024</span>
              <span class="g-surface-tier-box__role">Base surface</span>
            </div>
            <div class="g-surface-tier-box" style="background:#252B2E">
              <span class="g-surface-tier-box__title">High</span>
              <span class="g-surface-tier-box__hex">#252B2E</span>
              <span class="g-surface-tier-box__role">Elevated cards</span>
            </div>
            <div class="g-surface-tier-box" style="background:#303539">
              <span class="g-surface-tier-box__title">Highest</span>
              <span class="g-surface-tier-box__hex">#303539</span>
              <span class="g-surface-tier-box__role">Dialogs & popups</span>
            </div>
          </div>
        </div>

        <!-- System Navigation Specifications -->
        <div class="g-surface-stack" style="margin-top:32px;">
          <div>
            <h3 class="t-title-medium" style="font-size:18px;font-weight:700;color:var(--g-text);">
              System Navigation Standards
            </h3>
            <p class="t-body-small c-variant" style="margin-top:4px;line-height:1.5;">
              Dual-mode Android navigation bar support with edge-to-edge window insets.
            </p>
          </div>

          <div class="g-specs-grid" style="margin-top:8px;margin-bottom:0;">
            <div class="g-spec-item">
              <span class="g-spec-item__label">Gesture Bar (Default)</span>
              <span class="g-spec-item__val">72 × 5 dp rounded bar, 24 dp bottom inset</span>
            </div>
            <div class="g-spec-item">
              <span class="g-spec-item__label">3-Button Navigation</span>
              <span class="g-spec-item__val">48 dp bar: Back, Home, Recents</span>
            </div>
            <div class="g-spec-item">
              <span class="g-spec-item__label">Edge-to-Edge Status Bar</span>
              <span class="g-spec-item__val">44 dp top inset with punch-hole camera</span>
            </div>
          </div>
        </div>

        <!-- Palettes Catalog -->
        <div class="g-surface-stack" style="margin-top:32px;">
          <div>
            <h3 class="t-title-medium" style="font-size:18px;font-weight:700;color:var(--g-text);">
              Dynamic Color Palettes Catalog
            </h3>
            <p class="t-body-small c-variant" style="margin-top:4px;line-height:1.5;">
              Integrated palettes matching App Toolkit's theme provider engine.
            </p>
          </div>

          <div style="display:flex;flex-wrap:wrap;gap:12px;margin-top:8px;">
            ${PALETTES.map(
              (p) => html`
                <div
                  style="background:var(--g-surface-low);border:1px solid var(--g-border);border-radius:14px;padding:10px 16px;display:flex;align-items:center;gap:10px;"
                >
                  <md-icon style="color:var(--g-accent);--md-icon-size:18px">palette</md-icon>
                  <div>
                    <div style="font-size:13px;font-weight:600;color:var(--g-text);">${paletteLabel(p)}</div>
                    <div style="font-size:11px;font-family:monospace;color:var(--g-text-subtle);">${p}</div>
                  </div>
                </div>
              `,
            )}
          </div>
        </div>
      </main>

    `),
    document.body,
  );
};

await loadFonts();
draw();
