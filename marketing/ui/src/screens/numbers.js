import { html, nothing } from "lit";
import { renderToolBottomSheet, shapeBadge, toolMetaChips } from "../kit/components.js";
import { navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";

/**
 * The Measure tab (route id `numbers`, NumbersScreen.kt): length and angle, conversion and splitting, counting and
 * timing, chance. Visuals follow ToolkitTileVisuals.kt.
 */
const TOOLS = {
  ruler: { id: "ruler", title: "Surface Ruler", subtitle: "Measure small objects on screen", icon: "square_foot", shape: "cookie_12sided", palette: "gold", kindLabel: "Expanded tool", supportsTile: false },
  protractor: { id: "protractor", title: "Protractor", subtitle: "Measure an angle", icon: "architecture", shape: "cookie_9sided", palette: "gold", kindLabel: "Expanded tool", supportsTile: false },
  bill_splitter: { id: "bill_splitter", title: "Tip & Bill Splitter", subtitle: "Calculate tip and each person's share", icon: "calculate", shape: "soft_burst", palette: "gold", kindLabel: "Expanded tool", supportsTile: false },
  unit_converter: { id: "unit_converter", title: "Unit Converter", subtitle: "Length, weight, volume and temperature", icon: "straighten", shape: "cookie_9sided", palette: "gold", kindLabel: "Expanded tool", supportsTile: false },
  counter: { id: "counter", title: "Counter", subtitle: "Tap to add to a running count", icon: "dehaze", shape: "flower", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
  random_number: { id: "random_number", title: "Random Number", subtitle: "Pick a number within a range", icon: "casino", shape: "gem", palette: "gold", kindLabel: "Quick tool", supportsTile: false },
  coin_flip: { id: "coin_flip", title: "Coin Flip", subtitle: "Flip heads or tails instantly", icon: "monetization_on", shape: "cookie_12sided", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
  reaction_test: { id: "reaction_test", title: "Reaction Test", subtitle: "Test your reflexes with random delays", icon: "timer", shape: "cookie_9sided", palette: "gold", kindLabel: "Expanded tool", supportsTile: false },
  dice_roll: { id: "dice_roll", title: "Dice Roll", subtitle: "Roll a die from d6 to d20", icon: "casino", shape: "cookie_12sided", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
};

export default {
  id: "numbers",
  title: "Measure",
  category: "numbers",
  categoryLabel: "Measure",
  tier: 1,
  palette: "google",
  description: "Ruler, protractor, converter, bill splitter, counter, reaction test and chance tools.",
  defaults: {
    activeSheet: "none",
    rulerUnit: "mm",
    protractorAngle: 45,
    counterValue: 42,
    counterStep: 1,
    billAmount: 100,
    tipPercent: 15,
    splitPeople: 4,
    billRound: "exact",
    converterCategory: "Length",
    converterInput: 10,
    randomMin: 1,
    randomMax: 100,
    randomResult: 73,
    diceSides: 6,
    diceResult: 5,
    coinSide: "Heads",
    reactionPhase: "idle",
    reactionLast: 231,
    reactionBest: 214,
    reactionAverage: 246,
    drawerOpen: false,
    addedTiles: { counter: false, dice_roll: false, coin_flip: false },
  },
  controls: [
    {
      type: "presets",
      label: "Measure states",
      options: [
        ["Ruler sheet", { activeSheet: "ruler" }],
        ["Protractor sheet", { activeSheet: "protractor", protractorAngle: 60 }],
        ["Overview", { activeSheet: "none" }],
        ["Counter sheet", { activeSheet: "counter", counterValue: 24 }],
        ["Bill splitter sheet", { activeSheet: "bill_splitter" }],
        ["Unit converter sheet", { activeSheet: "unit_converter", converterCategory: "Temperature", converterInput: 22 }],
        ["Random number sheet", { activeSheet: "random_number" }],
        ["Dice roll sheet", { activeSheet: "dice_roll", diceSides: 20, diceResult: 19 }],
        ["Coin flip sheet", { activeSheet: "coin_flip" }],
        ["Reaction test: waiting", { activeSheet: "reaction_test", reactionPhase: "waiting" }],
        ["Reaction test: tap now", { activeSheet: "reaction_test", reactionPhase: "signal" }],
        ["Reaction test: result", { activeSheet: "reaction_test", reactionPhase: "result" }],
      ],
    },
    { key: "activeSheet", label: "Open sheet", type: "choice", options: ["none", "ruler", "protractor", "counter", "bill_splitter", "unit_converter", "random_number", "dice_roll", "coin_flip", "reaction_test"] },
    { key: "counterValue", label: "Counter", min: 0, max: 99, step: 1 },
  ],
  render(s) {
    const open = (id) => s.set({ activeSheet: id });
    const swallow = (e) => e.stopPropagation();
    const roll = (sides) => Math.floor(Math.random() * sides) + 1;

    const card = (id, cls, body) => html`
      <div class="numbers-card ${cls}" role="button" tabindex="0" @click=${() => open(id)}
        @keydown=${(e) => (e.key === "Enter" || e.key === " ") && (e.preventDefault(), open(id))}>${body}</div>`;
    const head = (t) => html`
      <div class="nm-head">${shapeBadge({ shape: t.shape, icon: t.icon, palette: t.palette, size: 64, iconSize: 32 })}
        <div class="nm-head__text"><h3 class="numbers-card-title">${t.title}</h3><p class="numbers-card-sub">${t.subtitle}</p></div></div>`;
    const chips = (t) => toolMetaChips({ kindLabel: t.kindLabel, supportsTile: t.supportsTile, tileAdded: Boolean(s.addedTiles?.[t.id]), palette: t.palette });
    const result = (label, value) => html`<div class="nm-result"><span>${label}</span><strong>${value}</strong></div>`;

    const content = html`
      <div class="tk-screen-pad">
        <section class="numbers-hero">
          <h1 class="numbers-hero-title">Measure, split &amp; count</h1>
          <p class="numbers-hero-sub">Simple tools for everyday tasks.</p>
        </section>

        <h2 class="numbers-section-title">Length &amp; angle</h2>
        <div class="bento-row-2">
          ${card("ruler", "corner-bento-left", html`${head(TOOLS.ruler)}${result("Length", s.rulerUnit === "in" ? "5.91 in" : "150 mm")}`)}
          ${card("protractor", "corner-bento-right", html`${head(TOOLS.protractor)}${result("Angle", `${s.protractorAngle}°`)}`)}
        </div>

        <h2 class="numbers-section-title">Convert &amp; split</h2>
        <div class="bento-row-2">
          ${card("unit_converter", "corner-bento-left", html`${head(TOOLS.unit_converter)}${chips(TOOLS.unit_converter)}`)}
          ${card("bill_splitter", "corner-bento-right", html`${head(TOOLS.bill_splitter)}${chips(TOOLS.bill_splitter)}`)}
        </div>

        <h2 class="numbers-section-title">Count &amp; time</h2>
        <div class="bento-stack">
          ${card("counter", "corner-bento-top numbers-card--wide", html`
            <div class="nm-counter-top">
              ${head(TOOLS.counter)}
              <span class="nm-counter-value" aria-label="Current count">${s.counterValue}</span>
            </div>
            <div class="nm-counter-actions tk-gold-actions">
              <md-text-button @click=${(e) => { swallow(e); s.set({ counterValue: 0 }); }}>Reset</md-text-button>
              <md-filled-tonal-icon-button aria-label="Subtract" @click=${(e) => { swallow(e); s.set({ counterValue: Math.max(0, s.counterValue - s.counterStep) }); }}><md-icon>remove</md-icon></md-filled-tonal-icon-button>
              <md-filled-button @click=${(e) => { swallow(e); s.set({ counterValue: s.counterValue + s.counterStep }); }}><md-icon slot="icon">add</md-icon>Add</md-filled-button>
            </div>
            ${chips(TOOLS.counter)}`)}
          ${card("reaction_test", "corner-bento-bottom numbers-card--wide", html`
            <div class="nm-counter-top">
              ${head(TOOLS.reaction_test)}
              <span class="nm-counter-value nm-counter-value--small" aria-label="Best time">${s.reactionBest ? `${s.reactionBest} ms` : "None"}</span>
            </div>
            <div class="nm-counter-actions tk-gold-actions">
              <md-filled-tonal-button @click=${(e) => { swallow(e); s.set({ activeSheet: "reaction_test", reactionPhase: "waiting" }); }}><md-icon slot="icon">play_arrow</md-icon>Start</md-filled-tonal-button>
            </div>`)}
        </div>

        <h2 class="numbers-section-title">Chance</h2>
        <div class="bento-stack">
          <div class="bento-row-2">
            ${card("random_number", "corner-bento-top-left", html`
              ${head(TOOLS.random_number)}${result(`Range ${s.randomMin}–${s.randomMax}`, s.randomResult)}
              <md-filled-tonal-button class="tk-gold-actions" @click=${(e) => { swallow(e); const lo = Math.min(s.randomMin, s.randomMax); const hi = Math.max(s.randomMin, s.randomMax); s.set({ randomResult: lo + Math.floor(Math.random() * (hi - lo + 1)) }); }}>Pick now</md-filled-tonal-button>`)}
            ${card("dice_roll", "corner-bento-top-right", html`
              ${head(TOOLS.dice_roll)}${result(`d${s.diceSides}`, s.diceResult)}
              <md-filled-tonal-button class="tk-gold-actions" @click=${(e) => { swallow(e); s.set({ diceResult: roll(s.diceSides) }); }}>Roll die</md-filled-tonal-button>`)}
          </div>
          ${card("coin_flip", "corner-bento-bottom numbers-card--wide", html`
            <div class="nm-counter-top">
              ${head(TOOLS.coin_flip)}
              <span class="nm-counter-value nm-counter-value--small" aria-label="Last flip">${s.coinSide}</span>
            </div>
            <div class="nm-counter-actions tk-gold-actions">
              <md-filled-tonal-button @click=${(e) => { swallow(e); s.set({ coinSide: Math.random() < 0.5 ? "Heads" : "Tails" }); }}>Flip coin</md-filled-tonal-button>
            </div>
            ${chips(TOOLS.coin_flip)}`)}
        </div>
      </div>`;

    const activeTool = TOOLS[s.activeSheet] || null;
    const overlay = activeTool
      ? renderToolBottomSheet({ tool: activeTool, state: s, onState: s.set, onClose: () => s.set({ activeSheet: "none" }) })
      : nothing;

    return screen({
      top: topAppBar({ title: "Measure", onNav: () => s.set({ drawerOpen: true }) }),
      content,
      bottom: navigationBar("numbers", s.go, s.window),
      drawer: navigationDrawer({ open: s.drawerOpen, onClose: () => s.set({ drawerOpen: false }), onNavigate: (t) => s.go(t, {}) }),
      overlay,
      navType: s.navType,
      window: s.window,
      tab: "numbers",
      onSelect: s.go,
    });
  },
};
