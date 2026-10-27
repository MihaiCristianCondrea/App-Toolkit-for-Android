import { html, nothing } from "lit";
import { shapeMaskStyle, shapeUrl } from "../kit/assets.js";
import { renderToolBottomSheet, shapeBadge, toolMetaChips } from "../kit/components.js";
import { navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";

/**
 * Numbers screen tools matching `NumbersScreen.kt`, `ToolkitTileVisuals.kt`,
 * and `sample/feature/tiles/src/main/res/values/strings.xml`.
 */
const NUMBERS_TOOLS = {
  bill_splitter: {
    id: "bill_splitter",
    title: "Tip & Bill Splitter",
    subtitle: "Calculate tip and per-person split",
    icon: "calculate",
    shape: "soft_burst",
    palette: "gold",
    kindLabel: "Expanded tool",
    supportsTile: false,
  },
  unit_converter: {
    id: "unit_converter",
    title: "Unit Converter",
    subtitle: "Convert common length, weight, and temperature units",
    icon: "straighten",
    shape: "cookie_9sided",
    palette: "gold",
    kindLabel: "Expanded tool",
    supportsTile: false,
  },
  counter: {
    id: "counter",
    title: "Counter",
    subtitle: "Increment a quick running count",
    icon: "dehaze",
    shape: "flower",
    palette: "gold",
    kindLabel: "Quick tool",
    supportsTile: true,
  },
  random_number: {
    id: "random_number",
    title: "Random Number",
    subtitle: "Pick a number within a range",
    icon: "casino",
    shape: "gem",
    palette: "gold",
    kindLabel: "Quick tool",
    supportsTile: false,
  },
  dice_roll: {
    id: "dice_roll",
    title: "Dice Roll",
    subtitle: "Roll one or two six-sided dice",
    icon: "casino",
    shape: "cookie_12sided",
    palette: "gold",
    kindLabel: "Quick tool",
    supportsTile: true,
  },
};

export default {
  id: "numbers",
  title: "Numbers",
  category: "numbers",
  categoryLabel: "Numbers",
  tier: 1,
  palette: "google",
  description:
    "Number tools under a warm hero banner, with a counter and a sheet for each tool.",
  defaults: {
    activeSheet: "none",
    cornerStyle: "bento",
    counterValue: 0,
    counterStep: 1,
    billAmount: 84,
    tipPercent: 15,
    splitPeople: 3,
    converterCategory: "Length",
    converterInput: 10,
    randomMin: 1,
    randomMax: 100,
    randomResult: 42,
    diceSides: 6,
    diceResult: 5,
    drawerOpen: false,
    addedTiles: { counter: false, dice_roll: false },
  },
  controls: [
    {
      type: "presets",
      label: "Numbers states",
      options: [
        ["Overview (Bento Grid)", { activeSheet: "none", cornerStyle: "bento", counterValue: 0 }],
        ["Counter Sheet", { activeSheet: "counter", counterValue: 24 }],
        ["Bill Splitter Sheet", { activeSheet: "bill_splitter", billAmount: 120, tipPercent: 18, splitPeople: 4 }],
        ["Unit Converter Sheet", { activeSheet: "unit_converter", converterCategory: "Temperature", converterInput: 22 }],
        ["Random Number Sheet", { activeSheet: "random_number", randomMin: 1, randomMax: 50, randomResult: 37 }],
        ["Dice Roll Sheet", { activeSheet: "dice_roll", diceSides: 20, diceResult: 19 }],
      ],
    },
    {
      key: "activeSheet",
      label: "Open Tool Bottom Sheet",
      type: "choice",
      options: ["none", "counter", "bill_splitter", "unit_converter", "random_number", "dice_roll"],
    },
    {
      key: "cornerStyle",
      label: "Card Corner Geometry",
      type: "choice",
      options: ["bento", "uniform"],
    },
    {
      key: "counterValue",
      label: "Counter Live Readout",
      min: 0,
      max: 99,
      step: 1,
    },
  ],
  render(s) {
    const bento = s.cornerStyle !== "uniform";
    const activeTool =
      s.activeSheet && s.activeSheet !== "none" ? NUMBERS_TOOLS[s.activeSheet] : null;

    const content = html`
      <div class="tk-screen-pad">
        <!-- NumbersHeroBanner (NumbersScreen.kt lines 236-310) -->
        <section class="numbers-hero">
          <div
            class="numbers-hero-watermark"
            style=${shapeMaskStyle(shapeUrl("soft_burst"))}
          ></div>
          <div style="position:relative;z-index:1;flex:1;min-width:0">
            <span class="numbers-eyebrow">NUMBERS</span>
            <h1 class="numbers-hero-title">Count, split
&amp; convert</h1>
            <p class="numbers-hero-sub">Simple tools for everyday tasks.</p>
          </div>
          <div
            class="numbers-hero-badge"
            style=${shapeMaskStyle(shapeUrl("cookie_12sided"))}
          >
            <span class="material-symbols-outlined" style="font-size:36px">
              numbers
            </span>
          </div>
        </section>

        <!-- Section 1: Calculate & convert -->
        <div class="numbers-section-header">
          <span class="numbers-section-title">Calculate &amp; convert</span>
        </div>

        <div class="bento-row-2">
          ${renderBentoToolCard({
            tool: NUMBERS_TOOLS.bill_splitter,
            cornerClass: bento ? "corner-bento-left" : "corner-uniform",
            tileAdded: Boolean(s.addedTiles?.bill_splitter),
            onOpen: () => s.set({ activeSheet: "bill_splitter" }),
          })}
          ${renderBentoToolCard({
            tool: NUMBERS_TOOLS.unit_converter,
            cornerClass: bento ? "corner-bento-right" : "corner-uniform",
            tileAdded: Boolean(s.addedTiles?.unit_converter),
            onOpen: () => s.set({ activeSheet: "unit_converter" }),
          })}
        </div>

        <!-- Section 2: Count & chance -->
        <div class="numbers-section-header">
          <span class="numbers-section-title">Count &amp; chance</span>
        </div>

        <div class="bento-stack">
          <!-- Full-width clickable Counter Feature Card (NumbersCounterFeatureCard) -->
          <button
            class="numbers-card numbers-card--feature ${bento ? "corner-bento-top" : "corner-uniform"}"
            @click=${() => s.set({ activeSheet: "counter" })}
          >
            <div class="numbers-counter-top">
              ${shapeBadge({
                shape: NUMBERS_TOOLS.counter.shape,
                icon: NUMBERS_TOOLS.counter.icon,
                palette: NUMBERS_TOOLS.counter.palette,
                size: 56,
                iconSize: 24,
              })}
              <div class="numbers-counter-copy">
                <h3 class="numbers-card-title">${NUMBERS_TOOLS.counter.title}</h3>
                <p class="numbers-counter-sub">${NUMBERS_TOOLS.counter.subtitle}</p>
              </div>
              <div
                class="numbers-counter-pill"
                title="Current counter value"
                style=${shapeMaskStyle(shapeUrl("cookie_9sided"))}
              >
                ${s.counterValue ?? 0}
              </div>
            </div>
            ${toolMetaChips({
              palette: "gold",
              kindLabel: NUMBERS_TOOLS.counter.kindLabel,
              supportsTile: NUMBERS_TOOLS.counter.supportsTile,
              tileAdded: Boolean(s.addedTiles?.counter),
            })}
          </button>

          <div class="bento-row-2">
            ${renderBentoToolCard({
              tool: NUMBERS_TOOLS.random_number,
              cornerClass: bento ? "corner-bento-bottom-left" : "corner-uniform",
              tileAdded: Boolean(s.addedTiles?.random_number),
              onOpen: () => s.set({ activeSheet: "random_number" }),
            })}
            ${renderBentoToolCard({
              tool: NUMBERS_TOOLS.dice_roll,
              cornerClass: bento ? "corner-bento-bottom-right" : "corner-uniform",
              tileAdded: Boolean(s.addedTiles?.dice_roll),
              onOpen: () => s.set({ activeSheet: "dice_roll" }),
            })}
          </div>
        </div>
      </div>
    `;

    const overlay = activeTool
      ? renderToolBottomSheet({
          tool: activeTool,
          state: s,
          onState: s.set,
          onClose: () => s.set({ activeSheet: "none" }),
          showQuickSettingsAction: true,
        })
      : nothing;

    return screen({
      top: topAppBar({
        title: "Numbers",
        onNav: () => s.set({ drawerOpen: true }),
      }),
      content,
      bottom: navigationBar("numbers", s.go, s.window),
      drawer: navigationDrawer({
        open: s.drawerOpen,
        onClose: () => s.set({ drawerOpen: false }),
        onNavigate: (target) => s.go(target, {}),
      }),
      overlay,
      navType: s.navType,
      window: s.window,
      tab: "numbers",
      onSelect: s.go,
    });
  },
};

function renderBentoToolCard({ tool, cornerClass, tileAdded, onOpen }) {
  return html`
    <button class="numbers-card ${cornerClass}" @click=${onOpen}>
      ${shapeBadge({
        shape: tool.shape,
        icon: tool.icon,
        palette: tool.palette,
        size: 56,
        iconSize: 24,
      })}
      <div>
        <h3 class="numbers-card-title">${tool.title}</h3>
        <p class="numbers-card-sub">${tool.subtitle}</p>
      </div>
      ${toolMetaChips({
        palette: "gold",
        kindLabel: tool.kindLabel,
        supportsTile: tool.supportsTile,
        tileAdded,
      })}
    </button>
  `;
}
