import { html, nothing } from "lit";
import { renderToolBottomSheet, shapeBadge, toolMetaChips } from "../kit/components.js";
import { navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";

/**
 * Quick Tools catalog matching `DefaultToolkitTilesRepository.kt`,
 * `ToolkitTileVisuals.kt`, `TileCatalogSections.kt`, and `TileInfoCards.kt`
 * (with Counter, Dice Roll, Bill Splitter, Unit Converter, and Random Number
 * migrated to the dedicated Numbers destination).
 */
const QUICK_TOOL_CATEGORIES = [
  {
    id: "sensors",
    title: "Sensors & Measurement",
    subtitle: "Compass, level, sound meter, ruler, and device checks",
    filterIcon: "explore",
    icon: "explore",
    shape: "sunny",
    palette: "teal",
    tools: [
      {
        id: "compass",
        title: "Compass",
        subtitle: "Check magnetic heading and cardinal direction",
        icon: "explore",
        shape: "sunny",
        palette: "teal",
        kindLabel: "Quick tool",
        supportsTile: true,
      },
      {
        id: "bubble_level",
        title: "Bubble Level",
        subtitle: "Measure surface tilt across X and Y axes",
        icon: "straighten",
        shape: "pill",
        palette: "teal",
        kindLabel: "Quick tool",
        supportsTile: true,
      },
      {
        id: "sound_meter",
        title: "Sound Meter",
        subtitle: "Monitor ambient noise level in decibels",
        icon: "graphic_eq",
        shape: "soft_boom",
        palette: "orange",
        kindLabel: "Quick tool",
        supportsTile: true,
      },
      {
        id: "screen_ruler",
        title: "Screen Ruler",
        subtitle: "Measure small objects in millimeters or inches",
        icon: "straighten",
        shape: "pentagon",
        palette: "orange",
        kindLabel: "Expanded tool",
        supportsTile: false,
      },
    ],
  },
  {
    id: "utilities",
    title: "Utilities & Fun",
    subtitle: "Flashlight, screen light, coin flip, stopwatch, and text tools",
    filterIcon: "casino",
    icon: "casino",
    shape: "clover_4leaf",
    palette: "orange",
    tools: [
      {
        id: "screen_light",
        title: "Screen Light",
        subtitle: "Turn the display into a bright soft light",
        icon: "light_mode",
        shape: "very_sunny",
        palette: "gold",
        kindLabel: "Quick tool",
        supportsTile: true,
      },
      {
        id: "coin_flip",
        title: "Coin Flip",
        subtitle: "Flip a virtual coin for quick decisions",
        icon: "casino",
        shape: "circle",
        palette: "orange",
        kindLabel: "Quick tool",
        supportsTile: true,
      },
      {
        id: "text_counter",
        title: "Text Counter",
        subtitle: "Count characters, words, and lines as you type",
        icon: "dehaze",
        shape: "cookie_6sided",
        palette: "orange",
        kindLabel: "Expanded tool",
        supportsTile: false,
      },
    ],
  },
  {
    id: "system",
    title: "System & Controls",
    subtitle: "Keep screen on, ringer mode, auto-rotate, and system shortcuts",
    filterIcon: "tune",
    icon: "tune",
    shape: "cookie_7sided",
    palette: "magenta",
    tools: [
      {
        id: "keep_screen_on",
        title: "Keep Screen On",
        subtitle: "Keep the display awake while reading or working",
        icon: "lightbulb",
        shape: "bun",
        palette: "gold",
        kindLabel: "Quick tool",
        supportsTile: true,
      },
      {
        id: "auto_rotate",
        title: "Auto-Rotate Toggle",
        subtitle: "Lock portrait orientation or enable auto-rotate",
        icon: "screen_rotation",
        shape: "burst",
        palette: "magenta",
        kindLabel: "Quick tool",
        supportsTile: true,
      },
    ],
  },
  {
    id: "wellbeing",
    title: "Wellbeing",
    subtitle: "Pomodoro focus timer, box breathing, and break reminders",
    filterIcon: "self_improvement",
    icon: "self_improvement",
    shape: "clover_8leaf",
    palette: "amber",
    tools: [
      {
        id: "pomodoro",
        title: "Pomodoro Timer",
        subtitle: "Run focused 25-minute work sessions and short breaks",
        icon: "timer",
        shape: "arch",
        palette: "gold",
        kindLabel: "Expanded tool",
        supportsTile: true,
      },
      {
        id: "breathing_box",
        title: "Breathing Exercise",
        subtitle: "Follow a steady 4-4-4-4 box-breathing rhythm",
        icon: "self_improvement",
        shape: "flower",
        palette: "amber",
        kindLabel: "Quick tool",
        supportsTile: false,
      },
    ],
  },
];

export default {
  id: "quick-tools",
  title: "Quick Tools",
  category: "tools",
  categoryLabel: "Quick Tools",
  tier: 1,
  palette: "google",
  description:
    "Quick Settings tiles grouped by category, with search and how to add a tile.",
  defaults: {
    selectedCategory: "all",
    searchQuery: "",
    expandedCategories: [],
    showHowToCard: true,
    activeToolId: "none",
    drawerOpen: false,
    addedTiles: {
      compass: true,
      screen_light: true,
      keep_screen_on: false,
    },
  },
  controls: [
    {
      type: "presets",
      label: "Quick Tools states",
      options: [
        ["Default (All Collapsed)", { selectedCategory: "all", expandedCategories: [], activeToolId: "none" }],
        ["Sensors Expanded", { selectedCategory: "all", expandedCategories: ["sensors"], activeToolId: "none" }],
        ["All Categories Expanded", { selectedCategory: "all", expandedCategories: ["sensors", "utilities", "system", "wellbeing"], activeToolId: "none" }],
        ["Compass Sheet Open", { selectedCategory: "sensors", expandedCategories: ["sensors"], activeToolId: "compass" }],
      ],
    },
    {
      key: "selectedCategory",
      label: "Category Filter Chip",
      type: "choice",
      options: ["all", "sensors", "utilities", "system", "wellbeing"],
    },
    {
      key: "showHowToCard",
      label: "Show How-to-Add Tiles Card",
      type: "toggle",
    },
  ],
  render(s) {
    const expanded = new Set(s.expandedCategories || []);
    const selectedCat = s.selectedCategory || "all";
    const query = (s.searchQuery || "").trim().toLowerCase();

    const visibleCategories = QUICK_TOOL_CATEGORIES.filter(
      (c) => selectedCat === "all" || c.id === selectedCat,
    )
      .map((c) => ({
        ...c,
        tools: query
          ? c.tools.filter(
              (t) =>
                t.title.toLowerCase().includes(query) ||
                t.subtitle.toLowerCase().includes(query),
            )
          : c.tools,
      }))
      .filter((c) => c.tools.length > 0);

    const allTools = QUICK_TOOL_CATEGORIES.flatMap((c) => c.tools);
    const activeTool = allTools.find((t) => t.id === s.activeToolId) || null;

    const toggleCategory = (catId) => {
      const next = new Set(expanded);
      if (next.has(catId)) next.delete(catId);
      else next.add(catId);
      s.set({ expandedCategories: [...next] });
    };

    const content = html`
      <!-- TopListFilters (ToolkitTilesScreen.kt lines 224-251) -->
      <div class="ai-filter-row" style="padding:16px 16px 4px">
        <span class="ai-filter-label">Sort by:</span>
        <md-chip-set aria-label="Filter tool categories">
          <md-filter-chip
            label="All tools"
            ?selected=${selectedCat === "all"}
            @click=${() => s.set({ selectedCategory: "all" })}
          >
            <md-icon slot="icon">apps</md-icon>
          </md-filter-chip>
          ${QUICK_TOOL_CATEGORIES.map(
            (c) => html`
              <md-filter-chip
                .label=${c.title}
                ?selected=${selectedCat === c.id}
                @click=${() =>
                  s.set({
                    selectedCategory: c.id,
                    expandedCategories: [...new Set([...expanded, c.id])],
                  })}
              >
                <md-icon slot="icon">${c.filterIcon}</md-icon>
              </md-filter-chip>
            `,
          )}
        </md-chip-set>
      </div>

      <div class="tk-screen-pad" style="padding-top:12px">
        <!-- Grouped Category Sections (TileCatalogSections.kt: 28dp outer, 4dp inner seam, 2dp gap) -->
        <div class="qt-categories-group">
          ${visibleCategories.map((cat, catIdx) => {
            const isExpanded = Boolean(query) || expanded.has(cat.id);
            const totalCats = visibleCategories.length;
            const catCornerCls =
              totalCats === 1
                ? "qt-category-card--single"
                : catIdx === 0
                  ? "qt-category-card--top"
                  : catIdx === totalCats - 1
                    ? "qt-category-card--bottom"
                    : "qt-category-card--middle";

            return html`
              <section class="qt-category-card ${catCornerCls}">
                <button
                  class="qt-category-header"
                  aria-expanded=${isExpanded}
                  @click=${() => toggleCategory(cat.id)}
                >
                  ${shapeBadge({
                    shape: cat.shape,
                    icon: cat.icon,
                    palette: cat.palette,
                    size: 56,
                    iconSize: 24,
                  })}
                  <div style="flex:1;min-width:0">
                    <div
                      style="font:600 16px/24px var(--md-ref-typeface-plain);letter-spacing:0.15px;color:var(--md-sys-color-on-surface)"
                    >
                      ${cat.title}
                    </div>
                    <div
                      style="margin-top:2px;font:400 14px/20px var(--md-ref-typeface-plain);letter-spacing:0.25px;color:var(--md-sys-color-on-surface-variant)"
                    >
                      ${cat.subtitle}
                    </div>
                  </div>
                  <span
                    class="material-symbols-outlined"
                    style="color:var(--md-sys-color-on-surface-variant)"
                  >
                    ${isExpanded ? "expand_less" : "expand_more"}
                  </span>
                </button>

                ${isExpanded
                  ? html`
                      <div class="qt-category-list">
                        ${cat.tools.map((tool, toolIdx) => {
                          const isAdded = Boolean(s.addedTiles?.[tool.id]);
                          const totalTools = cat.tools.length;
                          const itemCornerCls =
                            totalTools === 1
                              ? "qt-tile-item--single"
                              : toolIdx === 0
                                ? "qt-tile-item--top"
                                : toolIdx === totalTools - 1
                                  ? "qt-tile-item--bottom"
                                  : "qt-tile-item--middle";

                          return html`
                            <button
                              class="qt-tile-item ${itemCornerCls}"
                              @click=${() => s.set({ activeToolId: tool.id })}
                            >
                              <div class="qt-tile-row">
                                ${shapeBadge({
                                  shape: tool.shape,
                                  icon: tool.icon,
                                  palette: tool.palette,
                                  size: 56,
                                  iconSize: 24,
                                })}
                                <div style="flex:1;min-width:0">
                                  <div
                                    style="font:600 16px/24px var(--md-ref-typeface-plain);letter-spacing:0.15px;color:var(--md-sys-color-on-surface)"
                                  >
                                    ${tool.title}
                                  </div>
                                  <div
                                    style="margin-top:4px;font:400 14px/20px var(--md-ref-typeface-plain);letter-spacing:0.25px;color:var(--md-sys-color-on-surface-variant)"
                                  >
                                    ${tool.subtitle}
                                  </div>
                                </div>
                                <span
                                  class="material-symbols-outlined"
                                  style="color:var(--md-sys-color-on-surface-variant)"
                                >
                                  chevron_right
                                </span>
                              </div>
                              ${toolMetaChips({
                                kindLabel: tool.kindLabel,
                                supportsTile: tool.supportsTile,
                                tileAdded: isAdded,
                                showTileStatus: true,
                              })}
                            </button>
                          `;
                        })}
                      </div>
                    `
                  : nothing}
              </section>
            `;
          })}
        </div>

        <!-- HowToAddTilesCard (TileInfoCards.kt lines 49-103) -->
        ${s.showHowToCard
          ? html`
              <div class="qt-howto-card">
                ${shapeBadge({
                  shape: "cookie_9sided",
                  icon: "lightbulb",
                  palette: "primary",
                  size: 56,
                  iconSize: 24,
                })}
                <div style="flex:1;min-width:0">
                  <div
                    style="font:600 16px/24px var(--md-ref-typeface-plain);letter-spacing:0.15px;color:var(--md-sys-color-on-surface)"
                  >
                    Add tools to Quick Settings
                  </div>
                  <ol class="qt-howto-steps">
                    <li>1. Open any tool below to use it right away.</li>
                    <li>
                      2. Tools marked with a Tile badge can also be added to your
                      phone's Quick Settings panel.
                    </li>
                    <li>
                      3. Pull down from the top of your screen anytime for one-tap
                      access.
                    </li>
                  </ol>
                </div>
              </div>
            `
          : nothing}
      </div>
    `;

    const overlay = activeTool
      ? renderToolBottomSheet({
          tool: activeTool,
          state: s,
          onState: s.set,
          onClose: () => s.set({ activeToolId: "none" }),
          showQuickSettingsAction: true,
        })
      : nothing;

    return screen({
      top: topAppBar({
        search: "Search tools",
        searchValue: s.searchQuery || "",
        onSearch: (v) => s.set({ searchQuery: v }),
        onNav: () => s.set({ drawerOpen: true }),
      }),
      content,
      bottom: navigationBar("quick-tools", s.go, s.window),
      drawer: navigationDrawer({
        open: s.drawerOpen,
        onClose: () => s.set({ drawerOpen: false }),
        onNavigate: (target) => s.go(target, {}),
      }),
      overlay,
      navType: s.navType,
      window: s.window,
      tab: "quick-tools",
      onSelect: s.go,
    });
  },
};
