/**
 * Motion Sequences & Remotion Flows Page
 * 
 * Showcases interactive animations, transitions, and programmatic
 * Remotion video workflows for App Toolkit.
 */
import { html, render } from "lit";
import { loadFonts } from "./theme/index.js";
import "./theme/hub.js";
import "./gallery.css";
import {setHubTheme} from "material-next-web-components/marketing.js";
import {referenceShell} from "./reference-shell.js";
import { byId } from "./screens/index.js";
import { ScreenHost } from "./host.js";
import { PRODUCT_CONFIG } from "./config/product.js";

// View state
const view = {
  theme: PRODUCT_CONFIG.theme.defaultTheme ?? "system",
};

const hosts = new Map();

const isDark = () => {
  if (view.theme === "system") {
    return window.matchMedia("(prefers-color-scheme: dark)").matches;
  }
  return view.theme === "dark";
};

window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", () => {
  if (view.theme === "system") draw();
});

const openScreen = (id) => {
  const u = new URL("screen.html", location.href);
  u.searchParams.set("id", id);
  if (view.theme !== "system") u.searchParams.set("theme", view.theme);
  window.open(u.href, "_blank");
};

const draw = () => {
  const resolvedDark = isDark();
  setHubTheme(resolvedDark ? "dark" : "light");
  const root = document.documentElement;
  if (resolvedDark) root.dataset.theme = "dark";
  else root.dataset.theme = "light";

  render(
    referenceShell({heading: "Motion", active: "motion", theme: view.theme, onTheme: theme => {view.theme = theme; draw();}}, html`

      <!-- Editorial Header -->
      <header class="g-header">
        <div class="g-header__inner">
          <span class="g-header__eyebrow">App Toolkit · Motion</span>
          <h1 class="g-header__title">Motion sequences & Remotion flows</h1>
          <p class="g-header__desc">
            Screen sequences, transitions, and marketing motion scenes for Play Store reels, social demos, and Remotion video generation.
          </p>
        </div>
      </header>

      <!-- Main Motion Container -->
      <main class="g-section">
        <div class="g-motion-grid">
          ${PRODUCT_CONFIG.motion.stories.map(
            (story) => html`
              <div class="g-motion-card">
                <span class="g-motion-card__badge">
                  <md-icon style="--md-icon-size:16px">movie</md-icon>
                  Motion Sequence
                </span>
                <h3 class="g-motion-card__title">${story.title}</h3>
                <p class="g-motion-card__subtitle">${story.subtitle}</p>
                <p class="g-motion-card__desc">${story.description}</p>

                <!-- Sequence Thumbnails Preview -->
                <div style="display:flex;gap:16px;overflow-x:auto;padding:12px 0;">
                  ${story.screens.map(
                    (sId, idx) => html`
                      <div
                        style="display:flex;flex-direction:column;align-items:center;gap:6px;cursor:pointer;"
                        @click=${() => openScreen(sId)}
                      >
                        <div
                          style="background:var(--g-surface-tonal);border-radius:18px;padding:12px 8px;display:flex;justify-content:center;align-items:center;border:1px solid var(--g-border-subtle);"
                        >
                          <div style="zoom:0.32;">
                            <div
                              data-screen=${sId}
                              data-mount-key="motion-${story.id}-${sId}-${idx}"
                              data-theme-override=${resolvedDark ? "dark" : "light"}
                              data-frame="false"
                            ></div>
                          </div>
                        </div>
                        <span style="font-size:11px;font-weight:600;color:var(--g-text-variant);">
                          Step ${idx + 1}: ${byId(sId)?.title ?? sId}
                        </span>
                      </div>
                    `,
                  )}
                </div>

                <div class="g-motion-card__seq">
                  <md-filled-tonal-button @click=${() => openScreen(story.screens[0])}>
                    <md-icon slot="icon">play_arrow</md-icon>
                    Experience Flow
                  </md-filled-tonal-button>
                </div>
              </div>
            `,
          )}
        </div>

        <!-- Remotion Architecture Blueprint -->
        <div style="margin-top:48px;" class="g-surface-stack">
          <h3 class="t-title-medium" style="color:var(--g-text);font-size:18px;font-weight:700;">
            Programmatic Remotion Video Architecture
          </h3>
          <p class="t-body-small c-variant" style="line-height:1.6;">
            Each motion story defines a deterministic sequence rendered via Remotion with pixel-perfect device mockups.
            Frames are generated dynamically from live Web Components with 60fps springs, camera pans, and touch ripple indicators.
          </p>
          <div class="g-specs-grid" style="margin-top:12px;margin-bottom:0;">
            <div class="g-spec-item">
              <span class="g-spec-item__label">Video Resolution</span>
              <span class="g-spec-item__val">1080 × 1920 (9:16 Vertical)</span>
            </div>
            <div class="g-spec-item">
              <span class="g-spec-item__label">Framerate</span>
              <span class="g-spec-item__val">60 fps (Hardware Fluid)</span>
            </div>
            <div class="g-spec-item">
              <span class="g-spec-item__label">Rendering Engine</span>
              <span class="g-spec-item__val">Remotion 4.0 + Chrome Headless</span>
            </div>
          </div>
        </div>
      </main>

    `),
    document.body,
  );

  mountHosts(resolvedDark);
};

const mountHosts = (resolvedDark) => {
  for (const [key, host] of hosts.entries()) {
    if (!document.contains(host.el)) {
      host.destroy();
      hosts.delete(key);
    }
  }

  for (const el of document.querySelectorAll("[data-screen]")) {
    const screenId = el.dataset.screen;
    const key = el.dataset.mountKey || screenId;
    const cardTheme = el.dataset.themeOverride || (resolvedDark ? "dark" : "light");
    const cardFrame = el.dataset.frame === "true";

    let h = hosts.get(key);
    if (!h || h.el !== el) {
      h = new ScreenHost(el, {
        id: screenId,
        theme: cardTheme,
        frame: cardFrame,
        palette: "android",
        navType: "gesture",
        onGo: (target) => openScreen(target),
      });
      hosts.set(key, h);
    } else {
      h.setOptions({
        theme: cardTheme,
        frame: cardFrame,
      });
    }
  }
};

await loadFonts();
draw();
