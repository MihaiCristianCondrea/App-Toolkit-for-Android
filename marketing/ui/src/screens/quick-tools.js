import { html, nothing } from "lit";
import { renderToolBottomSheet, shapeBadge } from "../kit/components.js";
import { navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";
import { compassRose, direction, levelDial } from "../kit/tool-bodies.js";

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

const SECTIONS = [
  { id: "sensors", title: "Sensors", tools: ["compass", "bubble_level"] },
  { id: "light", title: "Light & signals", tools: ["flash_dimmer", "morse_sos"] },
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

    // Sensors are read live, so their cards show the reading itself instead of an icon.
    const sensorCard = (id) => id === "compass"
      ? html`
        <div class="qt-sensor qt-sensor--compass" role="button" tabindex="0" @click=${() => open(id)} @keydown=${activate(id)}
          aria-label="Compass, ${s.compassHeading} degrees ${direction(s.compassHeading)}">
          ${compassRose(s.compassHeading, 168, { labels: false })}
          <div class="qt-sensor__text">
            <span class="qt-sensor__label">Compass</span>
            <span class="qt-sensor__value">${s.compassHeading}°</span>
            <span class="qt-sensor__note">${direction(s.compassHeading)}</span>
          </div>
        </div>`
      : html`
        <div class="qt-sensor" role="button" tabindex="0" @click=${() => open(id)} @keydown=${activate(id)}
          aria-label="Bubble level, ${flat ? "level" : "tilted"}">
          ${levelDial(s.levelRoll, s.levelPitch, 72)}
          <div class="qt-sensor__text">
            <span class="qt-sensor__label">Bubble Level</span>
            <span class="qt-sensor__value qt-sensor__value--small">${flat ? "Level" : "Tilted"}</span>
            <span class="qt-sensor__note">${Math.abs(s.levelRoll).toFixed(1)}° roll · ${Math.abs(s.levelPitch).toFixed(1)}° pitch</span>
          </div>
        </div>`;

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
      const body = sec.id === "sensors"
        ? html`<div class="qt-stack">${ids.map(sensorCard)}</div>`
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
        <div class="qt-howto-card">
          <div class="qt-howto-copy">
            <div class="qt-howto-title">Add a tool to Quick Settings</div>
            <p class="qt-howto-text">Swipe down twice, tap the pencil, and drag a tool into your tiles. Open any tool to add it from here.</p>
          </div>
        </div>
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
