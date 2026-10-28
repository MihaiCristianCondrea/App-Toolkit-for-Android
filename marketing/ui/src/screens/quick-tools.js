import { html, nothing } from "lit";
import { renderToolBottomSheet, shapeBadge } from "../kit/components.js";
import { asset, shapeMaskStyle, shapeUrl } from "../kit/assets.js";
import { navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";
import { BREATH_STEPS, compassRose, direction, levelDial } from "../kit/tool-bodies.js";

/**
 * Quick Tools: tools that run on sensors and the torch, so none needs a special permission. Ruler, protractor,
 * counter, chance and the calculators live on the Measure tab.
 */
const TOOLS = {
  compass: { id: "compass", title: "Compass", subtitle: "Heading from the geomagnetic sensor", icon: "explore", shape: "sunny", palette: "teal", kindLabel: "Quick tool", supportsTile: true },
  bubble_level: { id: "bubble_level", title: "Bubble Level", subtitle: "Roll and pitch from the gravity sensor", icon: "straighten", shape: "pill", palette: "teal", kindLabel: "Quick tool", supportsTile: true },
  flash_dimmer: { id: "flash_dimmer", title: "Flash Dimmer", subtitle: "Set the torch brightness", icon: "flashlight_on", shape: "burst", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
  morse_sos: { id: "morse_sos", title: "Morse & SOS", subtitle: "Send a message with the torch", icon: "emergency", shape: "cookie_7sided", palette: "magenta", kindLabel: "Quick tool", supportsTile: true },
  // ToolkitTileVisuals.kt: Breathing is a SoftBurst in the orange role, with the FavoriteBorder icon.
  breathing: { id: "breathing", title: "Breathe", subtitle: "Start a short guided breathing pause", icon: "favorite", shape: "soft_burst", palette: "orange", kindLabel: "Quick tool", supportsTile: false },
};

/** The brand's own MaterialShapes exports in marketing/assets/shapes, used as masks. */
const brandShape = (name) => asset(`shapes/${encodeURIComponent(name)}.svg`);

// A press on the torch shape is a tap until the pointer moves this far; then it is a swipe that sets brightness.
const SWIPE_SLOP = 6;
let torchPress = null;

const SECTIONS = [
  { id: "sensors", title: "Sensors", icon: "sensors", tools: ["compass", "bubble_level"], live: "sensors" },
  { id: "light", title: "Light & signals", icon: "flashlight_on", tools: ["flash_dimmer", "morse_sos"], live: "light" },
  { id: "wellbeing", title: "Wellbeing", icon: "self_improvement", tools: ["breathing"] },
];

/** The native ad sits between Sensors and Light & signals, as the app places one between categories. */
const AD_AFTER = "sensors";

export default {
  id: "quick-tools",
  title: "Quick Tools",
  category: "tools",
  categoryLabel: "Quick Tools",
  tier: 1,
  palette: "google",
  description: "Live sensors and Quick Settings tiles that need no special permission.",
  defaults: {
    searchQuery: "",
    activeToolId: "none",
    drawerOpen: false,
    flashOn: true,
    flashLevel: 3,
    // The torch's maximum level comes from the device: 1 means it cannot dim (on or off only); a Pixel 8 Pro has 45.
    flashMaxLevel: 5,
    breathPhase: "INHALE",
    breathSeconds: 3,
    breathProgress: 0.7,
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
        ["Overview", { activeToolId: "none" }],
        ["Compass sheet", { activeToolId: "compass" }],
        ["Bubble level sheet", { activeToolId: "bubble_level" }],
        ["Flash dimmer sheet", { activeToolId: "flash_dimmer" }],
        ["Flash dimmer, Pixel 8 Pro (45 levels)", { activeToolId: "flash_dimmer", flashMaxLevel: 45, flashLevel: 30 }],
        ["Flash dimmer, no dimming", { activeToolId: "flash_dimmer", flashMaxLevel: 1, flashLevel: 1 }],
        ["Breathe: inhale", { activeToolId: "breathing", breathPhase: "INHALE", breathSeconds: 3, breathProgress: 0.7 }],
        ["Breathe: exhale", { activeToolId: "breathing", breathPhase: "EXHALE", breathSeconds: 2, breathProgress: 0.6 }],
        ["Morse & SOS sheet", { activeToolId: "morse_sos" }],
      ],
    },
    { key: "compassHeading", label: "Heading", min: 0, max: 359, step: 1 },
    { key: "flashOn", label: "Flashlight on", type: "toggle" },
  ],
  render(s) {
    const query = (s.searchQuery || "").trim().toLowerCase();
    const open = (id) => s.set({ activeToolId: id });
    const matches = (id) => !query || TOOLS[id].title.toLowerCase().includes(query) || TOOLS[id].subtitle.toLowerCase().includes(query);
    const activate = (id) => (e) => (e.key === "Enter" || e.key === " ") && (e.preventDefault(), open(id));
    const flat = Math.abs(s.levelRoll) < 1 && Math.abs(s.levelPitch) < 1;
    const heading = s.compassHeading;
    const maxLevel = Math.max(1, Number(s.flashMaxLevel ?? 5));
    const dims = maxLevel > 1;
    const flashLevel = s.flashOn ? Math.min(s.flashLevel, maxLevel) : 0;
    const setFlash = (level) => s.set({ flashOn: level > 0, flashLevel: level > 0 ? level : s.flashLevel });

    // Every widget opens its sheet on a tap. On a torch that can dim, a vertical swipe over its shape sets the
    // brightness instead, and the shape fills like a progress bar.
    const torch = {
      down(e) {
        torchPress = { x: e.clientX, y: e.clientY, swiping: false };
        if (dims) e.currentTarget.setPointerCapture(e.pointerId);
      },
      move(e) {
        if (!torchPress || !dims || !e.buttons) return;
        if (!torchPress.swiping && Math.hypot(e.clientX - torchPress.x, e.clientY - torchPress.y) < SWIPE_SLOP) return;
        torchPress.swiping = true;
        const r = e.currentTarget.querySelector(".qt-live__face").getBoundingClientRect();
        const level = Math.round(maxLevel * (1 - Math.min(Math.max(e.clientY - r.top, 0), r.height) / r.height));
        if (level !== flashLevel) setFlash(level);
      },
      up() {
        if (torchPress && !torchPress.swiping) open("flash_dimmer");
        torchPress = null;
      },
      key(e) {
        const step = { ArrowUp: 1, ArrowRight: 1, ArrowDown: -1, ArrowLeft: -1 }[e.key];
        if (step && dims) { e.preventDefault(); setFlash(Math.min(maxLevel, Math.max(0, flashLevel + step))); }
        else activate("flash_dimmer")(e);
      },
    };

    const face = (mask, content, extra = "") => html`
      <div class="qt-live__face ${extra}" style=${shapeMaskStyle(mask)}>${content}</div>`;

    const live = {
      compass: () => html`
        <div class="qt-live" data-accent="teal" role="button" tabindex="0" @click=${() => open("compass")} @keydown=${activate("compass")}
          aria-label="Compass, ${heading} degrees ${direction(heading)}">
          <span class="qt-live__label">Compass</span>
          ${face(brandShape("8 Sided Cookie"), compassRose(heading, 136, { labels: false, face: false }))}
          <span class="qt-live__value">${heading}°</span>
          <span class="qt-live__note">${direction(heading)}</span>
        </div>`,
      bubble_level: () => html`
        <div class="qt-live" data-accent="teal" role="button" tabindex="0" @click=${() => open("bubble_level")} @keydown=${activate("bubble_level")}
          aria-label="Bubble level, ${flat ? "level" : "tilted"}">
          <span class="qt-live__label">Bubble Level</span>
          ${face(brandShape("16 Sided Cookie"), levelDial(s.levelRoll, s.levelPitch, 148, { ring: false, edge: true }))}
          <span class="qt-live__value">${flat ? "Level" : "Tilted"}</span>
          <span class="qt-live__note">${Math.abs(s.levelRoll).toFixed(1)}° · ${Math.abs(s.levelPitch).toFixed(1)}°</span>
        </div>`,
      flash_dimmer: () => html`
        <div class="qt-live qt-live--signal" data-accent="gold">
          <div class="qt-live__control" role=${dims ? "slider" : "button"} tabindex="0"
            aria-label=${dims ? "Flashlight brightness. Swipe to change, tap for settings" : "Flashlight settings"}
            aria-valuemin=${dims ? 0 : nothing} aria-valuemax=${dims ? maxLevel : nothing} aria-valuenow=${dims ? flashLevel : nothing}
            @pointerdown=${torch.down} @pointermove=${torch.move} @pointerup=${torch.up} @pointercancel=${() => (torchPress = null)} @keydown=${torch.key}>
            <span class="qt-live__label">Flashlight</span>
            ${face(shapeUrl("burst"), html`<span class="qt-live__fill" style="height:${(flashLevel / maxLevel) * 100}%"></span>
              <md-icon class="qt-live__icon">${s.flashOn ? "flashlight_on" : "flashlight_off"}</md-icon>`, dims ? "qt-live__face--torch" : "")}
            <span class="qt-live__value">${!s.flashOn ? "Off" : dims ? `${flashLevel} of ${maxLevel}` : "On"}</span>
            <span class="qt-live__note">${dims ? "Swipe for brightness" : "Tap for settings"}</span>
          </div>
        </div>`,
      morse_sos: () => html`
        <div class="qt-live qt-live--signal" data-accent="magenta" role="button" tabindex="0" @click=${() => open("morse_sos")} @keydown=${activate("morse_sos")}
          aria-label="SOS, ${s.sosActive ? "sending" : "ready"}">
          <span class="qt-live__label">SOS</span>
          ${face(shapeUrl("cookie_7sided"), html`<span class="qt-live__sos">SOS</span>`, s.sosActive ? "qt-live__face--sending" : "")}
          <span class="qt-live__value">${s.sosActive ? "Sending" : "Ready"}</span>
          <span class="qt-live__note">Morse with the torch</span>
        </div>`,
    };

    // Breathe has no live sensor to show, so it is one wide card: its shape, its name and its rhythm.
    const breathe = () => {
      const t = TOOLS.breathing;
      return html`
        <div class="qt-breathe" role="button" tabindex="0" @click=${() => open("breathing")} @keydown=${activate("breathing")}>
          ${shapeBadge({ shape: t.shape, icon: t.icon, palette: t.palette, size: 72, iconSize: 32 })}
          <div class="qt-breathe__text">
            <h3 class="qt-breathe__title">${t.title}</h3>
            <p class="qt-breathe__rhythm">${BREATH_STEPS.map(([name, sec]) => html`<span>${name} ${sec}s</span>`)}</p>
          </div>
        </div>`;
    };

    const title = (icon, text) => html`<h2 class="qt-section__title"><md-icon class="sc-rounded" aria-hidden="true">${icon}</md-icon>${text}</h2>`;

    // The native ad, drawn like a widget: the advertiser's icon in a shape, its headline and line, one action. The
    // "Sponsored content" title marks it, so it carries no badge of its own.
    // Marketing media never shows it; the prototype's Show ads switch turns it on to check the layout.
    const ad = () => html`
      <section class="qt-section" aria-label="Sponsored content">
        ${title("campaign", "Sponsored content")}
        <div class="qt-ad">
          <div class="qt-ad__face" style=${shapeMaskStyle(shapeUrl("cookie_9sided"))}><md-icon>apps</md-icon></div>
          <div class="qt-ad__text">
            <h3 class="qt-ad__headline">Advertiser app name</h3>
            <p class="qt-ad__body">One short line from the advertiser about what the app does.</p>
            <div class="qt-ad__meta"><md-icon>star</md-icon>4.6 · Free</div>
          </div>
          <md-filled-tonal-button class="qt-ad__cta">Install</md-filled-tonal-button>
        </div>
      </section>`;

    const sections = SECTIONS.map((sec) => {
      const ids = sec.tools.filter(matches);
      if (!ids.length) return nothing;
      const body = sec.live
        ? html`<div class="qt-live-row qt-live-row--${sec.live}">${ids.map((id) => live[id]())}</div>`
        : breathe();
      return html`<section class="qt-section">${title(sec.icon, sec.title)}${body}</section>
        ${sec.id === AD_AFTER && s.showAds && !query ? ad() : nothing}`;
    });

    const content = html`
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
