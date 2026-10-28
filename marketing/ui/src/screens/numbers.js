import { html, svg, nothing } from "lit";
import { previewScreen } from "material-next-web-components/marketing.js";
import { toolOverlay } from "../kit/components.js";
import { billSummary, conversionSummary } from "../kit/measure.js";
import { APP } from "../config/identity.js";

/**
 * The Measure tab (route id `numbers`, NumbersScreen.kt): length and angle, conversion and splitting, counting and
 * timing. Visuals follow ToolkitTileVisuals.kt.
 */
const TOOLS = {
  ruler: { id: "ruler", title: "Surface Ruler", subtitle: "Measure small objects on screen", icon: "square_foot", shape: "cookie_12sided", palette: "gold", supportsTile: false },
  protractor: { id: "protractor", title: "Protractor", subtitle: "Measure an angle", icon: "architecture", shape: "cookie_9sided", palette: "gold", supportsTile: false },
  bill_splitter: { id: "bill_splitter", title: "Tip & Bill Splitter", subtitle: "Calculate tip and each person's share", icon: "calculate", shape: "soft_burst", palette: "gold", supportsTile: false },
  unit_converter: { id: "unit_converter", title: "Unit Converter", subtitle: "Length, weight, temperature, volume and data", icon: "straighten", shape: "cookie_9sided", palette: "gold", supportsTile: false },
  counter: { id: "counter", title: "Counter", subtitle: "Tap to add to a running count", icon: "dehaze", shape: "flower", palette: "gold", supportsTile: true },
  reaction_test: { id: "reaction_test", title: "Reaction Test", subtitle: "Test your reflexes with random delays", icon: "timer", shape: "cookie_9sided", palette: "gold", supportsTile: false },
};

export default {
  id: "numbers",
  title: "Measure",
  category: "numbers",
  categoryLabel: "Measure",
  tier: 1,
  palette: "google",
  description: "Ruler, protractor, converter, bill splitter, counter, reaction test.",
  defaults: {
    activeSheet: "none",
    rulerUnit: "mm",
    protractorAngle: 45,
    counterValue: 42,
    rulerScale: 1, rulerStart: 0, rulerEnd: 0.5,
    reactionHistory: [], reactionRound: 0,
    billAmount: 100,
    tipPercent: 15,
    splitPeople: 4,
    billRound: "exact",
    converterCategory: "Length",
    converterInput: "1",
    reactionPhase: "idle",
    reactionLast: 231,
    reactionBest: 214,
    reactionAverage: 246,
    addedTiles: { counter: false },
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
        ["Reaction test: waiting", { activeSheet: "reaction_test", reactionPhase: "waiting" }],
        ["Reaction test: tap now", { activeSheet: "reaction_test", reactionPhase: "signal" }],
        ["Reaction test: result", { activeSheet: "reaction_test", reactionPhase: "result" }],
      ],
    },
    { key: "activeSheet", label: "Open sheet", type: "choice", options: ["none", "ruler", "protractor", "counter", "bill_splitter", "unit_converter", "reaction_test"] },
    { key: "counterValue", label: "Counter", min: 0, max: 99, step: 1 },
  ],
  render(s) {
    const open = (id) => s.set?.({ activeSheet: id });
    const count = Math.max(0, Math.min(2147483647, Number(s.counterValue ?? 0)));
    const bill = billSummary(s);
    const conversion = conversionSummary(s);
    const angle = Math.max(0, Math.min(180, Number(s.protractorAngle ?? 45)));
    const armX = 90 + 65 * Math.cos(angle * Math.PI / 180);
    const armY = 90 - 65 * Math.sin(angle * Math.PI / 180);
    const sectionTitle = (icon, title) => html`<h2 class="qt-section__title"><md-icon class="sc-rounded" aria-hidden="true">${icon}</md-icon>${title}</h2>`;
    const widget = (id, extra, visual, detail, actions = nothing) => {
      const tool = TOOLS[id];
      return html`<article class="numbers-card mw-widget ${extra}" data-tool=${id}>
        <h3 class="mw-title"><button class="mw-open" type="button" @click=${() => open(id)}>${tool.title}</button></h3>
        ${visual}
        <div class="mw-detail">${detail}</div>
        ${actions}
      </article>`;
    };

    const content = html`
      <div class="tk-screen-pad measure-overview">
        <section class="qt-section">
          ${sectionTitle("square_foot", "Length & angle")}
          <div class="bento-row-2">
            ${widget("ruler", "corner-bento-left", html`
              <div class="mw-visual mw-ruler" aria-hidden="true">
                <svg viewBox="0 0 180 104"><rect x="10" y="18" width="160" height="68" rx="12" fill="currentColor" opacity=".12"/>
                  ${Array.from({length: 21}, (_, i) => svg`<line x1=${20 + i * 7} x2=${20 + i * 7} y1="18" y2=${i % 5 ? 30 : 42} stroke="currentColor" stroke-width=${i % 5 ? 1 : 2}/>${i % 5 ? nothing : svg`<text x=${20 + i * 7} y="65" text-anchor="middle" fill="currentColor">${i / 5}</text>`}`)}
                </svg>
              </div>`, html`<strong>mm / in</strong><span>Calibrate to your screen</span>`)}
            ${widget("protractor", "corner-bento-right", html`
              <div class="mw-visual" aria-hidden="true"><svg viewBox="0 0 180 104">
                <path d="M20 90 A70 70 0 0 1 160 90 Z" fill="currentColor" opacity=".12"/>
                ${Array.from({length: 19}, (_, i) => { const r = i * Math.PI / 18; return svg`<line x1=${90 + 70 * Math.cos(r)} y1=${90 - 70 * Math.sin(r)} x2=${90 + (i % 3 ? 63 : 57) * Math.cos(r)} y2=${90 - (i % 3 ? 63 : 57) * Math.sin(r)} stroke="currentColor"/>`; })}
                <path d="M155 90H90L${armX} ${armY}" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round"/><circle cx="90" cy="90" r="4" fill="currentColor"/>
              </svg></div>`, html`<strong>${angle}°</strong><span>Find the right angle</span>`)}
          </div>
        </section>
        <section class="qt-section">
          ${sectionTitle("swap_horiz", "Convert & split")}
          <div class="bento-row-2">
            ${widget("unit_converter", "corner-bento-left mw-widget--convert", html`
              <div class="mw-conversion"><span><b>${conversion.input}</b> ${conversion.inputUnit}</span><md-icon aria-hidden="true">swap_vert</md-icon><span><b>${conversion.output ?? "—"}</b> ${conversion.outputUnit}</span></div>
              `, html`<span>${conversion.category}</span>`)}
            ${widget("bill_splitter", "corner-bento-right mw-widget--bill", html`
              <div class="mw-share"><md-icon class="sc-rounded" aria-hidden="true">group</md-icon><strong>${bill.valid ? bill.perPerson : "—"}</strong><span>per person</span></div>
              `, html`<span>${s.tipPercent}% tip · ${s.splitPeople} ${Number(s.splitPeople) === 1 ? "person" : "people"}</span>`)}
          </div>
        </section>
        <section class="qt-section">
          ${sectionTitle("timer", "Count & time")}
          <div class="bento-stack">
            ${widget("counter", `corner-bento-top mw-widget--counter ${count >= 1000000 ? "mw-widget--long-count" : ""}`, html`
              <div class="mw-count" aria-live="polite" aria-label="Current count">${count}</div>
              `, nothing, html`<div class="mw-actions">
                <md-text-button ?disabled=${count === 0} @click=${() => s.set?.({counterValue: 0})}>Reset</md-text-button>
                <md-outlined-icon-button aria-label="Subtract" ?disabled=${count === 0} @click=${() => s.set?.({counterValue: count - 1})}><md-icon>remove</md-icon></md-outlined-icon-button>
                <md-filled-button ?disabled=${count === 2147483647} @click=${() => s.set?.({counterValue: count + 1})}><md-icon slot="icon">add</md-icon>Add</md-filled-button>
              </div>`)}
            ${widget("reaction_test", "corner-bento-bottom mw-widget--reaction", html`
              <div class="mw-reaction"><div><strong>${s.reactionBest ? html`${s.reactionBest}<small> ms</small>` : "Ready?"}</strong><span>${s.reactionBest ? "Personal best" : "Test your reflexes"}</span></div></div>
              `, nothing, html`<div class="mw-actions"><md-filled-tonal-button @click=${() => s.set?.({activeSheet: "reaction_test", reactionPhase: "idle"})}><md-icon slot="icon">play_arrow</md-icon>Start</md-filled-tonal-button></div>`)}
          </div>
        </section>
      </div>`;

    return previewScreen(APP, s, {
      tab: "numbers",
      content,
      overlay: toolOverlay({ tool: TOOLS[s.activeSheet], state: s, onState: s.set, onClose: () => s.set?.({ activeSheet: "none" }) }),
    });
  },
};
