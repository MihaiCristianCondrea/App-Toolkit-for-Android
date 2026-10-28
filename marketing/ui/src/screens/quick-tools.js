import { html, nothing } from "lit";
import { renderToolBottomSheet, shapeBadge } from "../kit/components.js";
import { shapeMaskStyle, shapeUrl } from "../kit/assets.js";
import { navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";
import { FLASH_LEVELS, compassRose, direction, levelDial } from "../kit/tool-bodies.js";

/**
 * Quick Tools: tools that run on sensors and the torch, so none needs a special permission. Ruler, protractor,
 * counter, chance and the calculators live on the Measure tab.
 */
const TOOLS = {
  compass: { id: "compass", title: "Compass", subtitle: "Heading from the geomagnetic sensor", icon: "explore", shape: "sunny", palette: "teal", kindLabel: "Quick tool", supportsTile: true },
  bubble_level: { id: "bubble_level", title: "Bubble Level", subtitle: "Roll and pitch from the gravity sensor", icon: "straighten", shape: "pill", palette: "teal", kindLabel: "Quick tool", supportsTile: true },
  flash_dimmer: { id: "flash_dimmer", title: "Flash Dimmer", subtitle: "Set the torch brightness", icon: "flashlight_on", shape: "burst", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
  morse_sos: { id: "morse_sos", title: "Morse & SOS", subtitle: "Send a message with the torch", icon: "emergency", shape: "cookie_7sided", palette: "magenta", kindLabel: "Quick tool", supportsTile: true },
  breathing_box: { id: "breathing_box", title: "Box Breathing", subtitle: "Follow a steady 4-4-4-4 rhythm", icon: "air", shape: "flower", palette: "amber", kindLabel: "Quick tool", supportsTile: false },
};

// Whether the pointer on the torch shape moved since it went down: a drag sets brightness, a tap toggles.
let torchDragged = false;

const SECTIONS = [
  { id: "sensors", title: "Sensors", tools: ["compass", "bubble_level"], live: true },
  { id: "light", title: "Light & signals", tools: ["flash_dimmer", "morse_sos"], live: true },
  { id: "calm", title: "Calm", tools: ["breathing_box"] },
];

export default {
  id: "quick-tools",
  title: "Quick Tools",
  category: "tools",
  categoryLabel: "Quick Tools",
  tier: 1,
  palette: "google",
  description: "Live sensors and Quick Settings tiles that need no special permission.",
  defaults: {
    selectedCategory: "all",
    searchQuery: "",
    activeToolId: "none",
    drawerOpen: false,
    flashOn: true,
    flashLevel: 3,
    breathRunning: false,
    sosActive: false,
    morseText: "SOS",
    compassHeading: 312,
    levelRoll: 0.4,
    levelPitch: -0.2,
    addedTiles: { compass: true, flash_dimmer: false },
  },
  controls: [
    {
      type: "presets",
      label: "Quick Tools states",
      options: [
        ["Overview", { selectedCategory: "all", activeToolId: "none" }],
        ["Compass sheet", { activeToolId: "compass" }],
        ["Bubble level sheet", { activeToolId: "bubble_level" }],
        ["Flash dimmer sheet", { activeToolId: "flash_dimmer" }],
        ["Box breathing sheet", { activeToolId: "breathing_box", breathRunning: true }],
        ["Morse & SOS sheet", { activeToolId: "morse_sos" }],
      ],
    },
    { key: "selectedCategory", label: "Filter", type: "choice", options: ["all", "sensors", "light", "calm"] },
    { key: "compassHeading", label: "Heading", min: 0, max: 359, step: 1 },
    { key: "flashOn", label: "Flashlight on", type: "toggle" },
  ],
  render(s) {
    const query = (s.searchQuery || "").trim().toLowerCase();
    const cat = s.selectedCategory || "all";
    const open = (id) => s.set({ activeToolId: id });
    const matches = (id) => !query || TOOLS[id].title.toLowerCase().includes(query) || TOOLS[id].subtitle.toLowerCase().includes(query);
    const swallow = (e) => e.stopPropagation();
    const activate = (id) => (e) => (e.key === "Enter" || e.key === " ") && (e.preventDefault(), open(id));
    const flat = Math.abs(s.levelRoll) < 1 && Math.abs(s.levelPitch) < 1;

    const status = (id) => ({
      flash_dimmer: s.flashOn ? `On · level ${s.flashLevel}` : "Off",
      morse_sos: s.sosActive ? "SOS sending" : "Ready",
      breathing_box: s.breathRunning ? "Running" : "4 · 4 · 4 · 4",
    })[id];

    // Live widgets: each shows its tool working inside the tool's own shape. Sensors open their sheet on a tap; the
    // torch and SOS act on a tap and open their sheet from the tune button.
    const face = (shape, content, extra = "") => html`
      <div class="qt-live__face ${extra}" style=${shapeMaskStyle(shapeUrl(shape))}>${content}</div>`;
    const tune = (id) => html`<md-icon-button class="qt-live__tune" aria-label="${TOOLS[id].title} settings"
      @click=${(e) => { swallow(e); open(id); }}><md-icon>tune</md-icon></md-icon-button>`;
    const heading = s.compassHeading;
    const flashLevel = s.flashOn ? s.flashLevel : 0;

    // Drag on the torch shape like on the Flash Dimmer bar: the fill follows the finger. A tap without a drag
    // switches the torch on or off.
    const torch = {
      down(e) { e.currentTarget.setPointerCapture(e.pointerId); torchDragged = false; },
      move(e) {
        if (!e.buttons) return;
        torchDragged = true;
        const r = e.currentTarget.getBoundingClientRect();
        const level = Math.round(FLASH_LEVELS * (1 - Math.min(Math.max(e.clientY - r.top, 0), r.height) / r.height));
        if (level !== flashLevel) s.set({ flashOn: level > 0, flashLevel: level > 0 ? level : s.flashLevel });
      },
      up() { if (!torchDragged) s.set({ flashOn: !s.flashOn }); },
    };

    const live = {
      compass: () => html`
        <div class="qt-live" data-accent="teal" role="button" tabindex="0" @click=${() => open("compass")} @keydown=${activate("compass")}
          aria-label="Compass, ${heading} degrees ${direction(heading)}">
          <span class="qt-live__label">Compass</span>
          ${face("cookie_12sided", compassRose(heading, 132, { labels: false, face: false }))}
          <span class="qt-live__value">${heading}°</span>
          <span class="qt-live__note">${direction(heading)}</span>
        </div>`,
      bubble_level: () => html`
        <div class="qt-live" data-accent="teal" role="button" tabindex="0" @click=${() => open("bubble_level")} @keydown=${activate("bubble_level")}
          aria-label="Bubble level, ${flat ? "level" : "tilted"}">
          <span class="qt-live__label">Bubble Level</span>
          ${face("pill", levelDial(s.levelRoll, s.levelPitch, 128, { ring: false }))}
          <span class="qt-live__value">${flat ? "Level" : "Tilted"}</span>
          <span class="qt-live__note">${Math.abs(s.levelRoll).toFixed(1)}° · ${Math.abs(s.levelPitch).toFixed(1)}°</span>
        </div>`,
      flash_dimmer: () => html`
        <div class="qt-live" data-accent="gold">
          <span class="qt-live__label">Flashlight</span>${tune("flash_dimmer")}
          <div class="qt-live__control" role="slider" tabindex="0" aria-label="Flashlight brightness" aria-valuemin="0"
            aria-valuemax=${FLASH_LEVELS} aria-valuenow=${flashLevel}
            @pointerdown=${torch.down} @pointermove=${torch.move} @pointerup=${torch.up}
            @keydown=${(e) => {
              const step = { ArrowUp: 1, ArrowRight: 1, ArrowDown: -1, ArrowLeft: -1 }[e.key];
              if (step) { e.preventDefault(); const next = Math.min(FLASH_LEVELS, Math.max(0, flashLevel + step)); s.set({ flashOn: next > 0, flashLevel: next > 0 ? next : s.flashLevel }); }
              else if (e.key === "Enter" || e.key === " ") { e.preventDefault(); s.set({ flashOn: !s.flashOn }); }
            }}>
            ${face("burst", html`<span class="qt-live__fill" style="height:${(flashLevel / FLASH_LEVELS) * 100}%"></span>
              <md-icon class="qt-live__icon">${s.flashOn ? "flashlight_on" : "flashlight_off"}</md-icon>`, "qt-live__face--torch")}
          </div>
          <span class="qt-live__value">${s.flashOn ? `Level ${flashLevel}` : "Off"}</span>
          <span class="qt-live__note">Drag to set brightness</span>
        </div>`,
      morse_sos: () => html`
        <div class="qt-live" data-accent="magenta">
          <span class="qt-live__label">SOS</span>${tune("morse_sos")}
          <button class="qt-live__control" aria-pressed=${s.sosActive ? "true" : "false"}
            aria-label=${s.sosActive ? "Stop SOS" : "Send SOS"} @click=${() => s.set({ sosActive: !s.sosActive })}>
            ${face("cookie_7sided", html`<span class="qt-live__sos">SOS</span>`, s.sosActive ? "qt-live__face--sending" : "")}
          </button>
          <span class="qt-live__value">${s.sosActive ? "Sending" : "Ready"}</span>
          <span class="qt-live__note">${s.sosActive ? "Tap to stop" : "Tap to signal"}</span>
        </div>`,
    };

    const control = (id) => id === "breathing_box"
      ? html`<md-filled-tonal-icon-button aria-label=${s.breathRunning ? "Pause breathing" : "Start breathing"} @click=${(e) => { swallow(e); s.set({ breathRunning: !s.breathRunning }); }}><md-icon>${s.breathRunning ? "pause" : "play_arrow"}</md-icon></md-filled-tonal-icon-button>`
      : nothing;

    const tile = (id, wide) => {
      const t = TOOLS[id];
      return html`
        <div class="qt-tile ${wide ? "qt-tile--wide" : ""}" data-accent=${t.palette} role="button" tabindex="0" @click=${() => open(id)} @keydown=${activate(id)}>
          ${wide
            ? html`${shapeBadge({ shape: t.shape, icon: t.icon, palette: t.palette, size: 56, iconSize: 28 })}
                <div class="qt-tile__text"><h3 class="qt-tile__title">${t.title}</h3><p class="qt-tile__status">${status(id)}</p></div>${control(id)}`
            : html`<div class="qt-tile__top">${shapeBadge({ shape: t.shape, icon: t.icon, palette: t.palette, size: 64, iconSize: 32 })}${control(id)}</div>
                <div class="qt-tile__text"><h3 class="qt-tile__title">${t.title}</h3><p class="qt-tile__status">${status(id)}</p></div>`}
        </div>`;
    };

    const sections = SECTIONS.filter((sec) => cat === "all" || cat === sec.id).map((sec) => {
      const ids = sec.tools.filter(matches);
      if (!ids.length) return nothing;
      const body = sec.live
        ? html`<div class="qt-live-row">${ids.map((id) => live[id]())}</div>`
        : html`<div class="qt-grid">${ids.map((id) => tile(id, ids.length === 1))}</div>`;
      return html`<section class="qt-section"><h2 class="qt-section__title">${sec.title}</h2>${body}</section>`;
    });

    const content = html`
      <div class="ai-filter-row" style="padding:8px 16px 4px">
        <md-chip-set aria-label="Filter tools">
          <md-filter-chip label="All tools" ?selected=${cat === "all"} @click=${() => s.set({ selectedCategory: "all" })}></md-filter-chip>
          ${SECTIONS.map((sec) => html`<md-filter-chip label=${sec.title} ?selected=${cat === sec.id} @click=${() => s.set({ selectedCategory: sec.id })}></md-filter-chip>`)}
        </md-chip-set>
      </div>
      <div class="tk-screen-pad" style="padding-top:8px">
        ${sections}
      </div>`;

    const activeTool = TOOLS[s.activeToolId] || null;
    const overlay = activeTool
      ? renderToolBottomSheet({ tool: activeTool, state: s, onState: s.set, onClose: () => s.set({ activeToolId: "none" }) })
      : nothing;

    return screen({
      top: topAppBar({ search: "Search tools", searchValue: s.searchQuery || "", onSearch: (v) => s.set({ searchQuery: v }), onNav: () => s.set({ drawerOpen: true }) }),
      content,
      bottom: navigationBar("quick-tools", s.go, s.window),
      drawer: navigationDrawer({ open: s.drawerOpen, onClose: () => s.set({ drawerOpen: false }), onNavigate: (t) => s.go(t, {}) }),
      overlay,
      navType: s.navType,
      window: s.window,
      tab: "quick-tools",
      onSelect: s.go,
    });
  },
};
