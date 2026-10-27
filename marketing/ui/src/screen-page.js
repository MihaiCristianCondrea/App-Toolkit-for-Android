/**
 * Screen Details & Prototype Page
 * 
 * Focused inspection and side-by-side comparison for an individual app screen.
 * Supports:
 * - Full interactive screen prototype with live state tweaks & presets
 * - Side-by-side variant comparison:
 *   - Light vs Dark
 *   - Gesture vs 3-button navigation
 *   - Palette A vs Palette B
 *   - Device frame on/off
 *
 * Query parameters:
 *   id=home-feed
 *   mode=inspect|compare
 *   compare=theme|nav|palette|frame
 *   palette=captured|dynamic|android|blue|...
 *   theme=system|dark|light
 *   nav=gesture|buttons
 *   frame=device|none
 *   window=phone|fold|desktop   (kit/layout.js WINDOWS; from 600 dp the app's wide layout)
 *   ads=on                      show the ad stand-ins (off by default; marketing media never shows ads)
 *   state={"filter":"news"}
 */
import { html, render } from "lit";
import { loadFonts } from "./theme/index.js";
import "./theme/hub.js";
import "./page.css";
import { SCREENS, byId } from "./screens/index.js";
import { ScreenHost } from "./host.js";
import { PALETTES, THEME_OPTIONS, NAV_OPTIONS, NAV_ICONS, chips, controlPanel, paletteLabel, segments, materialSelect } from "./controls.js";

const q = new URLSearchParams(location.search);
const capture = q.get("frame") === "none" && q.get("panel") === "none";
const useDevice = q.get("frame") === "device";

// View state
const view = {
  mode: q.get("mode") ?? "inspect", // "inspect" | "compare"
  compareAxis: q.get("compare") ?? "theme", // "theme" | "nav" | "palette" | "frame"
  palette: q.get("palette") ?? "android",
  paletteB: q.get("paletteB") ?? "captured",
  theme: q.get("theme") ?? "system", // "system" | "dark" | "light"
  device: useDevice,
  nav: q.get("nav") ?? "gesture",
  window: q.get("window") ?? "phone",
  // Off by default: screenshots and marketing media never show ad UI.
  showAds: q.get("ads") === "on",
};

let id = byId(q.get("id") ?? "")?.id ?? SCREENS[0].id;
let initial = {};
try {
  if (q.has("state")) initial = JSON.parse(q.get("state"));
} catch (e) {
  console.warn("Failed to parse state param:", e);
}
if (q.has("preset")) {
  const targetScreen = byId(id);
  const presetCtrl = targetScreen?.controls?.find((c) => c.type === "presets");
  const foundPreset = presetCtrl?.options?.find(
    ([name]) => name.toLowerCase() === q.get("preset").toLowerCase(),
  );
  if (foundPreset) Object.assign(initial, foundPreset[1]);
}

await loadFonts();
document.body.className = capture ? "page-bare" : "page-proto";

// Resolve dark mode taking system into account
const resolveDark = (t = view.theme) => {
  if (t === "system") {
    return window.matchMedia("(prefers-color-scheme: dark)").matches;
  }
  return t === "dark";
};

// System color scheme change listener
window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", () => {
  if (view.theme === "system" && !capture) {
    updateThemes();
  }
});

let host = null;
let hostA = null;
let hostB = null;

const updateThemes = () => {
  const resolved = resolveDark() ? "dark" : "light";
  document.documentElement.dataset.theme = resolved;
  if (host) host.setOptions({ theme: resolved });
  if (hostA && view.compareAxis !== "theme") hostA.setOptions({ theme: resolved });
  if (hostB && view.compareAxis !== "theme") hostB.setOptions({ theme: resolved });
};

if (capture) {
  // Capture mode: exactly one screen or bare framed device for headless screenshots
  if (useDevice) {
    document.body.style.display = "grid";
    document.body.style.placeItems = "center";
    document.body.style.minHeight = "100vh";
    document.body.style.background = "transparent";
    document.body.style.padding = "24px";
  }
  const resolvedTheme = resolveDark() ? "dark" : "light";
  host = new ScreenHost(document.body, {
    id,
    state: initial,
    palette: view.palette,
    theme: resolvedTheme,
    frame: useDevice,
    navType: view.nav,
    window: view.window,
    showAds: view.showAds,
  });
  host.opts.onGo = () => {};
} else {
  // Full Interactive Inspection & Comparison Interface
  render(
    html`<main class="p-layout">
      <section class="p-stage"></section>
      <aside class="p-panel"></aside>
    </main>`,
    document.body,
  );

  const stage = document.querySelector(".p-stage");
  const panel = document.querySelector(".p-panel");

  const syncUrl = () => {
    const currentScreenId = host?.screen?.id ?? hostA?.screen?.id ?? id;
    const u = new URL(location.href);
    u.searchParams.set("id", currentScreenId);
    u.searchParams.delete("state");

    if (view.mode !== "inspect") u.searchParams.set("mode", view.mode);
    else u.searchParams.delete("mode");

    if (view.mode === "compare") {
      u.searchParams.set("compare", view.compareAxis);
      if (view.compareAxis === "palette") u.searchParams.set("paletteB", view.paletteB);
    } else {
      u.searchParams.delete("compare");
      u.searchParams.delete("paletteB");
    }

    if (view.theme !== "system") u.searchParams.set("theme", view.theme);
    else u.searchParams.delete("theme");

    if (view.nav !== "gesture") u.searchParams.set("nav", view.nav);
    else u.searchParams.delete("nav");

    if (view.palette !== "android") u.searchParams.set("palette", view.palette);
    else u.searchParams.delete("palette");

    if (view.showAds) u.searchParams.set("ads", "on");
    else u.searchParams.delete("ads");

    if (view.window !== "phone") u.searchParams.set("window", view.window);
    else u.searchParams.delete("window");

    if (!view.device) u.searchParams.set("frame", "none");
    else u.searchParams.set("frame", "device");

    history.replaceState(null, "", u);
    const activeScreen = byId(currentScreenId);
    document.title = `${activeScreen?.title ?? currentScreenId} · Android Insider UI`;
  };

  const drawStage = () => {
    const currentScreenId = host?.screen?.id ?? hostA?.screen?.id ?? id;
    const retainedState = structuredClone(host?.state ?? hostA?.state ?? initial);
    host?.destroy();
    hostA?.destroy();
    hostB?.destroy();
    const resolvedTheme = resolveDark() ? "dark" : "light";
    document.documentElement.dataset.theme = resolvedTheme;

    if (view.mode === "inspect") {
      // Single Stage Inspection Mode
      render(
        html`<div class="p-screen-container">
          <div class="p-screen"></div>
        </div>`,
        stage,
      );

      const screenEl = stage.querySelector(".p-screen");
      host = new ScreenHost(screenEl, {
        id: currentScreenId,
        state: retainedState,
        palette: view.palette,
        theme: resolvedTheme,
        frame: view.device,
        navType: view.nav,
        window: view.window,
        showAds: view.showAds,
        onGo: (target, st, h) => {
          h.load(target, st);
          syncUrl();
          drawPanel();
        },
        onChange: () => drawPanel(),
      });
      hostA = null;
      hostB = null;
    } else {
      // Side-by-side Variant Comparison Mode
      let labelA = "";
      let labelB = "";
      let iconA = "tune";
      let iconB = "tune";

      let optsA = {
        id: currentScreenId,
        state: retainedState,
        palette: view.palette,
        theme: resolvedTheme,
        frame: view.device,
        navType: view.nav,
        window: view.window,
        showAds: view.showAds,
      };

      let optsB = {
        id: currentScreenId,
        state: retainedState,
        palette: view.palette,
        theme: resolvedTheme,
        frame: view.device,
        navType: view.nav,
        window: view.window,
        showAds: view.showAds,
      };

      if (view.compareAxis === "theme") {
        labelA = "M3 Dark Theme (OLED Layered Stack)";
        labelB = "M3 Light Theme (High Contrast Clean)";
        iconA = "dark_mode";
        iconB = "light_mode";
        optsA.theme = "dark";
        optsB.theme = "light";
      } else if (view.compareAxis === "nav") {
        labelA = "Gesture Bar (Edge-to-Edge Pill)";
        labelB = "3-Button Navigation (Back / Home / Recents)";
        iconA = NAV_ICONS.gesture;
        iconB = NAV_ICONS.buttons;
        optsA.navType = "gesture";
        optsB.navType = "buttons";
      } else if (view.compareAxis === "palette") {
        labelA = `Palette A: ${paletteLabel(view.palette)}`;
        labelB = `Palette B: ${paletteLabel(view.paletteB)}`;
        iconA = "palette";
        iconB = "palette";
        optsA.palette = view.palette;
        optsB.palette = view.paletteB;
      } else if (view.compareAxis === "frame") {
        labelA = "Device Frame: Pixel 9 Pro Shell";
        labelB = "Clean Screen: Flat Surface View";
        iconA = "smartphone";
        iconB = "crop_free";
        optsA.frame = true;
        optsB.frame = false;
      }

      render(
        html`<div class="p-compare-wrap">
          <div class="p-compare-col">
            <div class="p-compare-header">
              <md-icon>${iconA}</md-icon>
              <span>${labelA}</span>
            </div>
            <div class="p-compare-stage" id="p-stage-a"></div>
          </div>
          <div class="p-compare-col">
            <div class="p-compare-header">
              <md-icon>${iconB}</md-icon>
              <span>${labelB}</span>
            </div>
            <div class="p-compare-stage" id="p-stage-b"></div>
          </div>
        </div>`,
        stage,
      );

      const stageA = stage.querySelector("#p-stage-a");
      const stageB = stage.querySelector("#p-stage-b");

      hostA = new ScreenHost(stageA, {
        ...optsA,
        onGo: (target, st) => {
          id = target;
          hostA.load(target, st);
          hostB?.load(target, st);
          syncUrl();
          drawPanel();
        },
      });

      hostB = new ScreenHost(stageB, {
        ...optsB,
        onGo: (target, st) => {
          id = target;
          hostA?.load(target, st);
          hostB.load(target, st);
          syncUrl();
          drawPanel();
        },
      });

      host = null;
    }
    requestAnimationFrame(fitScreens);
  };

  // A frameless screen at a window size (fold, desktop) keeps that size, as the app lays itself out for it, and is
  // scaled down to fit its stage instead of running past it. The phone keeps filling the workspace.
  const fitScreens = () => {
    for (const el of document.querySelectorAll(".p-screen > .sc-screen, .p-compare-stage > .sc-screen")) {
      const w = parseFloat(el.style.width);
      const h = parseFloat(el.style.height);
      if (!w || !h) continue;
      const box = el.parentElement.getBoundingClientRect();
      const zoom = Math.min(1, box.width / w, (window.innerHeight - 200) / h);
      // Only a change is written: the observer below sees style changes, and an unchanged value must not loop.
      const target = zoom < 1 ? zoom.toFixed(4) : "";
      if (el.style.zoom !== target) el.style.zoom = target;
    }
  };
  addEventListener("resize", () => requestAnimationFrame(fitScreens));
  new MutationObserver(() => requestAnimationFrame(fitScreens)).observe(document.body, { childList: true, subtree: true, attributes: true, attributeFilter: ["style"] });

  const drawPanel = () => {
    const activeScreen = host?.screen ?? hostA?.screen ?? byId(id);
    const s = activeScreen;

    render(
      html`
        <!-- Navigation Breadcrumb -->
        <md-outlined-button class="p-back" href="./">
          <md-icon slot="icon">arrow_back</md-icon>
          Screens catalog
        </md-outlined-button>

        <!-- Screen Meta -->
        <div class="p-meta">
          <span class="p-tag">${s.categoryLabel ?? s.category ?? "Screen"}</span>
          <h1 class="t-headline-small p-title">${s.title}</h1>
          <p class="t-body-medium c-variant">${s.description}</p>
          <code class="t-label-small c-variant p-source">${s.source}</code>
        </div>

        <!-- Mode Switch: Inspect vs. Compare -->
        <div class="p-section">
          <div class="t-label-large c-variant">View Mode</div>
          ${segments([["inspect", "Inspect", "search"], ["compare", "Compare", "compare"]], view.mode,
            value => { view.mode = value; drawStage(); syncUrl(); drawPanel(); }, "View mode")}

        </div>

        <!-- Active Screen Selection -->
        <div class="p-section">
          <div class="t-label-large c-variant">Screen</div>
          <md-outlined-select aria-label="Screen" .value=${s.id} menu-positioning="fixed"
            class="p-select t-body-medium"
            @change=${(e) => {
              id = e.target.value;
              if (host) host.go(e.target.value);
              if (hostA) hostA.go(e.target.value);
              if (hostB) hostB.go(e.target.value);
              syncUrl();
              drawPanel();
            }}
          >
            ${SCREENS.map((x) => html`<md-select-option value=${x.id} ?selected=${x.id === s.id}><div slot="headline">${x.title}</div></md-select-option>`)}
          </md-outlined-select>
        </div>

        ${view.mode === "compare"
          ? html`
              <!-- Side-by-side Comparison Controls -->
              <div class="p-section">
                <div class="t-label-large c-variant">Comparison Axis</div>
                ${chips(
                  [
                    ["theme", "Light vs. Dark"],
                    ["nav", "Gesture vs. 3-Button"],
                    ["palette", "Palette A vs. B"],
                    ["frame", "Frame On vs. Off"],
                  ],
                  view.compareAxis,
                  (ax) => {
                    view.compareAxis = ax;
                    drawStage();
                    syncUrl();
                    drawPanel();
                  },
                )}

                ${view.compareAxis === "palette"
                  ? html`
                      <div class="p-palette-compare-picker" style="margin-top:12px">
                        <label class="t-label-small c-variant">Palette A</label>
                        <md-outlined-select aria-label="Palette A" .value=${view.palette} menu-positioning="fixed"
                          class="p-select t-body-medium"
                          @change=${(e) => {
                            view.palette = e.target.value;
                            hostA?.setOptions({ palette: e.target.value });
                            syncUrl();
                            drawPanel();
                          }}
                        >
                          ${PALETTES.map(
                            (p) => html`<md-select-option value=${p} ?selected=${view.palette === p}><div slot="headline">${paletteLabel(p)}</div></md-select-option>`,
                          )}
                        </md-outlined-select>

                        <label class="t-label-small c-variant" style="margin-top:8px">Palette B</label>
                        <md-outlined-select aria-label="Palette B" .value=${view.paletteB} menu-positioning="fixed"
                          class="p-select t-body-medium"
                          @change=${(e) => {
                            view.paletteB = e.target.value;
                            hostB?.setOptions({ palette: e.target.value });
                            syncUrl();
                            drawPanel();
                          }}
                        >
                          ${PALETTES.map(
                            (p) => html`<md-select-option value=${p} ?selected=${view.paletteB === p}><div slot="headline">${paletteLabel(p)}</div></md-select-option>`,
                          )}
                        </md-outlined-select>
                      </div>
                    `
                  : ""}
              </div>
            `
          : html`
              <!-- Single Screen Inspection Controls -->
              <div class="p-section">
                <div class="t-label-large c-variant">Theme</div>
                ${segments(THEME_OPTIONS, view.theme,
                  value => { view.theme = value; updateThemes(); syncUrl(); drawPanel(); }, "Preview theme")}

              </div>

              <div class="p-section">
                <div class="t-label-large c-variant">System Navigation</div>
                ${segments(
                  NAV_OPTIONS,
                  view.nav,
                  (n) => {
                    view.nav = n;
                    host?.setOptions({ navType: n });
                    syncUrl();
                    drawPanel();
                  },
                  "System navigation",
                )}
              </div>

              <div class="p-section">
                <div class="t-label-large c-variant">Window</div>
                ${segments(
                  [["phone", "Phone", "smartphone"], ["fold", "Fold", "devices_fold"], ["desktop", "Desktop", "desktop_windows"]],
                  view.window,
                  (w) => {
                    view.window = w;
                    host?.setOptions({ window: w });
                    syncUrl();
                    drawPanel();
                  },
                  "Window size",
                )}
              </div>

              <div class="p-section">
                <div class="t-label-large c-variant">Palette</div>
                ${materialSelect("Palette", PALETTES.map(p => [p, paletteLabel(p)]), view.palette,
                  value => { view.palette = value; host?.setOptions({ palette: value }); syncUrl(); drawPanel(); })}
                <label class="p-control--row t-label-large" style="margin-top:10px">
                  <md-switch
                    ?selected=${view.device}
                    @change=${(e) => {
                      view.device = e.target.selected;
                      host?.setOptions({ frame: view.device });
                      syncUrl();
                      drawPanel();
                    }}
                  ></md-switch>
                  Device frame
                </label>
                <label class="p-control--row t-label-large" style="margin-top:10px">
                  <md-switch
                    ?selected=${view.showAds}
                    @change=${(e) => {
                      view.showAds = e.target.selected;
                      host?.setOptions({ showAds: view.showAds });
                      syncUrl();
                      drawPanel();
                    }}
                  ></md-switch>
                  Show ads
                </label>
              </div>

              ${s.controls?.length
                ? html`<div class="p-section">${controlPanel(s.controls, host?.state ?? initial, (patch) => host?.set(patch))}</div>`
                : ""}
            `}

        <!-- Actions -->
        <div class="p-section p-actions">
          ${view.mode === "inspect"
            ? html`<md-outlined-button @click=${() => host?.reset()}>Reset state</md-outlined-button>`
            : ""}
          <md-text-button href="screen.html?id=${s.id}&frame=none&panel=none" target="_blank">
            Capture view
          </md-text-button>
        </div>
      `,
      panel,
    );
  };

  drawStage();
  syncUrl();
  drawPanel();
}

Promise.all([document.fonts.ready, customElements.whenDefined("md-icon")]).then(() =>
  requestAnimationFrame(() => document.documentElement.setAttribute("data-ready", "")),
);
