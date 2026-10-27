/**
 * Product UI Showcase - Screen Catalog Shell
 * 
 * An official Google Material 3-style product UI catalog.
 * Decoupled from application-specific logic via PRODUCT_CONFIG.
 * 
 * Purpose: Browse and preview all app screens with live controls.
 */
import { html, render, nothing } from "lit";
import { loadFonts } from "./theme/index.js";
import "./theme/hub.js";
import "./gallery.css";
import { SCREENS, byId } from "./screens/index.js";
import { ScreenHost } from "./host.js";
import { PALETTES, THEME_OPTIONS, NAV_OPTIONS, NAV_ICONS, paletteLabel, segments, materialSelect } from "./controls.js";
import { PRODUCT_CONFIG } from "./config/product.js";

// View state: default system theme and gesture bar navigation
const view = {
  theme: PRODUCT_CONFIG.theme.defaultTheme ?? "system", // "system" | "dark" | "light"
  navType: PRODUCT_CONFIG.theme.defaultNavType ?? "gesture", // "gesture" | "buttons"
  palette: PRODUCT_CONFIG.theme.defaultPalette ?? "android",
  deviceFrame: false,
  cardThemeOverrides: new Map(),
  cardNavOverrides: new Map(),
};

try {
  const saved = JSON.parse(localStorage.getItem("android-insider-preview-v2") || "null");
  if (saved) {
    if (["system", "light", "dark"].includes(saved.theme)) view.theme = saved.theme;
    if (["gesture", "buttons"].includes(saved.navType)) view.navType = saved.navType;
    if (PALETTES.includes(saved.palette)) view.palette = saved.palette;
    if (typeof saved.deviceFrame === "boolean") view.deviceFrame = saved.deviceFrame;
  }
} catch { /* Preview remains usable when storage is unavailable. */ }

const hosts = new Map();

// Helper to check if any non-default preview settings are active
const hasSettingsOverrides = () => {
  return (
    view.theme !== (PRODUCT_CONFIG.theme.defaultTheme ?? "system") ||
    view.navType !== (PRODUCT_CONFIG.theme.defaultNavType ?? "gesture") ||
    view.palette !== (PRODUCT_CONFIG.theme.defaultPalette ?? "android") ||
    view.deviceFrame
  );
};

// Reset preview settings to configuration defaults
const resetSettings = () => {
  view.theme = PRODUCT_CONFIG.theme.defaultTheme ?? "system";
  view.navType = PRODUCT_CONFIG.theme.defaultNavType ?? "gesture";
  view.palette = PRODUCT_CONFIG.theme.defaultPalette ?? "android";
  view.deviceFrame = false;
  view.cardThemeOverrides.clear();
  view.cardNavOverrides.clear();
  draw();
};

// Resolve active dark mode considering system preference
const isDark = () => {
  if (view.theme === "system") {
    return window.matchMedia("(prefers-color-scheme: dark)").matches;
  }
  return view.theme === "dark";
};

// Listen for OS system theme changes
window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", () => {
  if (view.theme === "system") draw();
});

// Update top app bar container background on scroll
window.addEventListener("scroll", () => {
  const isScrolled = window.scrollY > 8;
  const nav = document.querySelector(".g-nav");
  if (nav) {
    nav.classList.toggle("g-nav--scrolled", isScrolled);
  }
}, { passive: true });

// Navigate to the dedicated screen details / prototype page
const openScreen = (id, newTab = false) => {
  const u = new URL("screen.html", location.href);
  u.searchParams.set("id", id);
  if (view.theme !== "system") u.searchParams.set("theme", view.theme);
  if (view.navType !== "gesture") u.searchParams.set("nav", view.navType);
  if (view.palette !== "captured") u.searchParams.set("palette", view.palette);
  if (view.deviceFrame) u.searchParams.set("frame", "device");
  else u.searchParams.set("frame", "none");

  if (newTab) {
    window.open(u.href, "_blank");
  } else {
    location.href = u.href;
  }
};

// Screen card component in the catalog
const screenCard = (s) => {
  const cardTheme = view.cardThemeOverrides.get(s.id) ?? (isDark() ? "dark" : "light");
  const cardNav = view.cardNavOverrides.get(s.id) ?? view.navType;
  const isCardDark = cardTheme === "dark";

  return html`<article class="g-card" data-id=${s.id}>
    <div
      class="g-card__preview"
      aria-label=${`${s.title} interactive preview`}
    >
      <div class="g-card__preview-scale">
        <div
          class="g-card__screen"
          data-screen=${s.id}
          data-mount-key="card-${s.id}"
          data-theme-override=${cardTheme}
          data-nav-override=${cardNav}
          data-frame=${view.deviceFrame ? "true" : "false"}
        ></div>
      </div>
    </div>
    <div class="g-card__body">
      <div class="g-card__header">
        <h3><md-text-button
          class="g-card__title"
          title="Click to open ${s.title}"
          @click=${() => openScreen(s.id)}
        >
          ${s.title}
        </md-text-button></h3>
        <span class="g-tag">${s.categoryLabel ?? s.category ?? "Screen"}</span>
      </div>
      <p class="g-card__desc">${s.description}</p>
      <div class="g-card__actions">
        <div class="g-card__mini-tools">
          <md-icon-button toggle .selected=${isCardDark} aria-label=${`Dark theme for ${s.title}`} title="Dark theme"
            @change=${(e) => { view.cardThemeOverrides.set(s.id, e.target.selected ? "dark" : "light"); draw(); }}>
            <md-icon>dark_mode</md-icon>
            <md-icon slot="selected" class="g-filled">dark_mode</md-icon>
          </md-icon-button>
          <md-icon-button toggle .selected=${cardNav === "buttons"} aria-label=${`3-button navigation for ${s.title}`} title="3-button navigation"
            @change=${(e) => { view.cardNavOverrides.set(s.id, e.target.selected ? "buttons" : "gesture"); draw(); }}>
            <md-icon>${NAV_ICONS.buttons}</md-icon>
            <md-icon slot="selected" class="g-filled">${NAV_ICONS.buttons}</md-icon>
          </md-icon-button>
        </div>
        <md-filled-tonal-button @click=${() => openScreen(s.id)}>
          <md-icon slot="icon">open_in_new</md-icon>
          Open
        </md-filled-tonal-button>
      </div>
    </div>
  </article>`;
};

// Draw / Render the entire screen catalog shell
const draw = () => {
  try {
    localStorage.setItem("android-insider-preview-v2", JSON.stringify({ theme: view.theme, navType: view.navType, palette: view.palette, deviceFrame: view.deviceFrame }));
  } catch { /* Private browsing may disable storage. */ }
  const resolvedDark = isDark();
  const root = document.documentElement;
  if (resolvedDark) root.dataset.theme = "dark";
  else root.dataset.theme = "light";

  const isScrolled = typeof window !== "undefined" && window.scrollY > 8;

  render(
    html`
      <!-- Top app bar: brand only -->
      <a class="g-skip-link" href="#screens">Skip to screen catalog</a>
      <nav class="g-nav ${isScrolled ? "g-nav--scrolled" : ""}">
        <a class="g-nav__brand" href="./" title=${PRODUCT_CONFIG.name}>
          <img src=${PRODUCT_CONFIG.icon} alt="" class="g-nav__logo" />
        </a>
      </nav>

      <!-- Compact Intro Header (One Short Sentence) -->
      <header class="g-header">
        <div class="g-header__inner">
          <span class="g-header__eyebrow">${PRODUCT_CONFIG.name}</span>
          <h1 class="g-header__title">${PRODUCT_CONFIG.headline}</h1>
          <p class="g-header__desc">${PRODUCT_CONFIG.description}</p>
        </div>
      </header>

      <!-- Sticky Preview Settings Bar (Directly Visible Controls) -->
      <section class="g-controls-wrap">
        <div class="g-controls-bar">
          <div class="g-control-group"><span class="g-control-label">Theme</span>
            ${segments(THEME_OPTIONS, view.theme, (value) => { view.theme = value; draw(); }, "Preview theme")}
          </div>
          <div class="g-control-group"><span class="g-control-label">Navigation</span>
            ${segments(NAV_OPTIONS, view.navType, (value) => { view.navType = value; draw(); }, "System navigation")}
          </div>
          <div class="g-control-group"><span class="g-control-label">Palette</span>
            ${materialSelect("Palette", PALETTES.map(p => [p, paletteLabel(p)]), view.palette, (value) => { view.palette = value; draw(); })}
          </div>
          <div class="g-control-group"><span class="g-control-label">Device frame</span>
            <md-switch aria-label="Device frame" .selected=${view.deviceFrame}
              @change=${e => { view.deviceFrame = e.target.selected; draw(); }}></md-switch>
          </div>
          ${hasSettingsOverrides() ? html`<md-text-button class="g-settings-reset" @click=${resetSettings}>
            <md-icon slot="icon">restart_alt</md-icon>Reset defaults</md-text-button>` : nothing}
        </div>
      </section>

      <!-- Main Screen Catalog (Showcase All Screens) -->
      <main class="g-section" id="screens" tabindex="-1">
        <div class="g-section-header">
          <div>
            <h2 class="g-section-title">Screen catalog (${SCREENS.length})</h2>
            <p class="g-section-sub">
              Live interactive previews translated from Jetpack Compose with Material Web
            </p>
          </div>
        </div>

        <div class="g-grid">${SCREENS.map(screenCard)}</div>
      </main>
    `,
    document.body,
  );

  mountHosts(resolvedDark);
};

// Mount or update all live ScreenHost instances in the DOM
const mountHosts = (resolvedDark) => {
  // Prune disconnected hosts
  for (const [key, host] of hosts.entries()) {
    if (!document.contains(host.el)) {
      host.destroy();
      hosts.delete(key);
    }
  }

  // Mount/update visible hosts
  for (const el of document.querySelectorAll("[data-screen]")) {
    const screenId = el.dataset.screen;
    const key = el.dataset.mountKey || screenId;
    const cardTheme = el.dataset.themeOverride || (resolvedDark ? "dark" : "light");
    const cardNav = el.dataset.navOverride || view.navType;
    const cardPalette = el.dataset.paletteOverride || view.palette;
    const cardFrame = el.dataset.frame === "true";

    let h = hosts.get(key);
    if (!h || h.el !== el) {
      h?.destroy();
      h = new ScreenHost(el, {
        id: screenId,
        palette: cardPalette,
        theme: cardTheme,
        frame: cardFrame,
        navType: cardNav,
        onGo: (target) => openScreen(target),
      });
      hosts.set(key, h);
    } else {
      h.setOptions({
        palette: cardPalette,
        theme: cardTheme,
        frame: cardFrame,
        navType: cardNav,
      });
    }
  }
};

await loadFonts();
draw();
