import { html, nothing } from "lit";
import { renderToolBottomSheet, shapeBadge } from "../kit/components.js";
import { navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";
import { cardinal, compassDial, levelDial } from "../kit/tool-bodies.js";

/**
 * Quick Tools: tools that run on sensors, the display and the torch, so none needs a special
 * permission. Counter, dice and the calculators live on the Numbers tab.
 */
const TOOLS = {
  compass: { id: "compass", title: "Compass", subtitle: "Heading from the geomagnetic sensor", icon: "explore", shape: "sunny", palette: "teal", kindLabel: "Quick tool", supportsTile: true },
  bubble_level: { id: "bubble_level", title: "Bubble Level", subtitle: "Roll and pitch from the gravity sensor", icon: "straighten", shape: "pill", palette: "teal", kindLabel: "Quick tool", supportsTile: true },
  ruler: { id: "ruler", title: "Surface Ruler", subtitle: "Measure small objects on screen", icon: "square_foot", shape: "pentagon", palette: "orange", kindLabel: "Expanded tool", supportsTile: false },
  protractor: { id: "protractor", title: "Protractor", subtitle: "Measure an angle", icon: "architecture", shape: "arch", palette: "orange", kindLabel: "Expanded tool", supportsTile: false },
  flash_dimmer: { id: "flash_dimmer", title: "Flash Dimmer", subtitle: "Set the torch brightness", icon: "flashlight_on", shape: "burst", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
  screen_light: { id: "screen_light", title: "Screen Light", subtitle: "Use the display as a soft light", icon: "wb_incandescent", shape: "very_sunny", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
  morse_sos: { id: "morse_sos", title: "Morse & SOS", subtitle: "Send a message with the torch", icon: "emergency", shape: "cookie_7sided", palette: "magenta", kindLabel: "Quick tool", supportsTile: true },
  breathing_box: { id: "breathing_box", title: "Box Breathing", subtitle: "Follow a steady 4-4-4-4 rhythm", icon: "air", shape: "flower", palette: "amber", kindLabel: "Quick tool", supportsTile: false },
};

const SECTIONS = [
  { id: "sensors", title: "Measure", filter: "Sensors & measurement", tools: ["ruler", "protractor"], glance: true },
  { id: "system", title: "Light & signals", filter: "Light & signals", tools: ["flash_dimmer", "screen_light", "morse_sos"] },
  { id: "wellbeing", title: "Calm", filter: "Wellbeing", tools: ["breathing_box"] },
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
    screenLightOn: false,
    lightTemp: "warm",
    breathRunning: false,
    protractorAngle: 45,
    rulerUnit: "mm",
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
        ["Protractor sheet", { activeToolId: "protractor" }],
        ["Flash dimmer sheet", { activeToolId: "flash_dimmer" }],
        ["Box breathing sheet", { activeToolId: "breathing_box", breathRunning: true }],
        ["Morse & SOS sheet", { activeToolId: "morse_sos" }],
      ],
    },
    { key: "selectedCategory", label: "Filter", type: "choice", options: ["all", "sensors", "system", "wellbeing"] },
    { key: "flashOn", label: "Flashlight on", type: "toggle" },
  ],
  render(s) {
    const query = (s.searchQuery || "").trim().toLowerCase();
    const cat = s.selectedCategory || "all";
    const open = (id) => s.set({ activeToolId: id });
    const matches = (id) => !query || TOOLS[id].title.toLowerCase().includes(query) || TOOLS[id].subtitle.toLowerCase().includes(query);

    const status = (id) => ({
      ruler: s.rulerUnit === "in" ? "5.91 in" : "150 mm",
      protractor: `${s.protractorAngle}°`,
      flash_dimmer: s.flashOn ? `On · level ${s.flashLevel}` : "Off",
      screen_light: s.screenLightOn ? `On · ${s.lightTemp}` : "Off",
      morse_sos: s.sosActive ? "SOS sending" : "Ready",
      breathing_box: s.breathRunning ? "Running" : "4 · 4 · 4 · 4",
    })[id];

    const swallow = (e) => e.stopPropagation();
    const control = (id) => {
      if (id === "flash_dimmer") return html`<md-switch aria-label="Flashlight" ?selected=${s.flashOn} @click=${swallow} @change=${(e) => s.set({ flashOn: e.target.selected })}></md-switch>`;
      if (id === "screen_light") return html`<md-switch aria-label="Screen light" ?selected=${s.screenLightOn} @click=${swallow} @change=${(e) => s.set({ screenLightOn: e.target.selected })}></md-switch>`;
      if (id === "breathing_box") return html`<md-filled-tonal-icon-button aria-label=${s.breathRunning ? "Pause breathing" : "Start breathing"} @click=${(e) => { swallow(e); s.set({ breathRunning: !s.breathRunning }); }}><md-icon>${s.breathRunning ? "pause" : "play_arrow"}</md-icon></md-filled-tonal-icon-button>`;
      return nothing;
    };

    const tile = (id) => {
      const t = TOOLS[id];
      return html`
        <div class="qt-tile ${id === "breathing_box" ? "qt-tile--wide" : ""}" data-accent=${t.palette} role="button" tabindex="0"
          @click=${() => open(id)} @keydown=${(e) => (e.key === "Enter" || e.key === " ") && (e.preventDefault(), open(id))}>
          <div class="qt-tile__top">${shapeBadge({ shape: t.shape, icon: t.icon, palette: t.palette, size: 48, iconSize: 24 })}${control(id)}</div>
          <div class="qt-tile__text"><h3 class="qt-tile__title">${t.title}</h3><p class="qt-tile__status">${status(id)}</p></div>
        </div>`;
    };

    const glanceCell = (id, dial, value, note) => html`
      <button class="qt-glance__cell" data-accent="teal" @click=${() => open(id)} aria-label="Open ${TOOLS[id].title}">
        <span class="qt-glance__label">${TOOLS[id].title}</span>${dial}
        <span class="qt-glance__value">${value}</span><span class="qt-glance__note">${note}</span>
      </button>`;

    const glance = html`
      <section class="qt-glance" aria-label="Live sensors">
        ${glanceCell("compass", compassDial(s.compassHeading, 88), `${s.compassHeading}° ${cardinal(s.compassHeading)}`, "Magnetic north")}
        ${glanceCell("bubble_level", levelDial(s.levelRoll, s.levelPitch, 88), Math.abs(s.levelRoll) < 1 && Math.abs(s.levelPitch) < 1 ? "Level" : "Tilted", `${Math.abs(s.levelRoll).toFixed(1)}° roll`)}
      </section>`;

    const sections = SECTIONS.filter((sec) => cat === "all" || cat === sec.id)
      .map((sec) => {
        const ids = sec.tools.filter(matches);
        const showGlance = sec.glance && (!query || ["compass", "bubble_level"].some(matches));
        if (!ids.length && !showGlance) return nothing;
        return html`
          <section class="qt-section">
            <h2 class="qt-section__title">${sec.title}</h2>
            ${showGlance ? glance : nothing}
            ${ids.length ? html`<div class="qt-grid">${ids.map(tile)}</div>` : nothing}
          </section>`;
      });

    const content = html`
      <div class="ai-filter-row" style="padding:8px 16px 4px">
        <md-chip-set aria-label="Filter tools">
          <md-filter-chip label="All tools" ?selected=${cat === "all"} @click=${() => s.set({ selectedCategory: "all" })}></md-filter-chip>
          ${SECTIONS.map((sec) => html`<md-filter-chip label=${sec.filter} ?selected=${cat === sec.id} @click=${() => s.set({ selectedCategory: sec.id })}></md-filter-chip>`)}
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
