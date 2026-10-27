import { html } from "lit";
import { shapeBadge, toolMetaChips } from "../kit/components.js";
import { screen, topAppBar } from "../kit/shell.js";

export default {
  id: "components",
  title: "Components Showcase",
  category: "system",
  categoryLabel: "Components",
  tier: 2,
  palette: "google",
  source: "sample/feature/components/src/main/kotlin/.../ComponentsShowcaseScreen.kt",
  description:
    "Live Material Web 3 reference gallery showing tonal surfaces (zero 1px borders), official buttons, borderless tonal assist chips, switches, sliders, and MaterialShapes polygons.",
  defaults: {
    notificationsEnabled: true,
    hapticsEnabled: true,
    sliderValue: 65,
  },
  controls: [
    { key: "notificationsEnabled", label: "Haptic Feedback Switch", type: "toggle" },
    { key: "sliderValue", label: "Intensity Slider", min: 0, max: 100, step: 5 },
  ],
  render(s) {
    const content = html`
      <div class="tk-screen-pad">
        <section class="app-card-surface">
          <div class="title-medium" style="font-weight:700">Official Material Web 3 Buttons</div>
          <div style="display:flex;flex-wrap:wrap;gap:8px">
            <md-filled-button>
              <md-icon slot="icon">bolt</md-icon>
              Filled
            </md-filled-button>
            <md-filled-tonal-button>
              <md-icon slot="icon">auto_awesome</md-icon>
              Tonal
            </md-filled-tonal-button>
            <md-outlined-button>Outlined</md-outlined-button>
            <md-text-button>Text</md-text-button>
          </div>
        </section>

        <section class="app-card-surface">
          <div class="title-medium" style="font-weight:700">Borderless Tonal Tool Chips &amp; Controls</div>
          ${toolMetaChips({
            kindLabel: "Quick tool",
            supportsTile: true,
            tileAdded: true,
            showTileStatus: true,
          })}

          <div style="display:flex;align-items:center;justify-content:space-between;padding-top:6px">
            <span class="body-medium">Expressive Haptic Feedback</span>
            <md-switch
              ?selected=${Boolean(s.notificationsEnabled)}
              @change=${(e) => s.set({ notificationsEnabled: e.target.selected })}
            ></md-switch>
          </div>

          <div>
            <div class="label-small" style="color:var(--md-sys-color-on-surface-variant)">
              Intensity (${s.sliderValue}%)
            </div>
            <md-slider
              min="0"
              max="100"
              .value=${Number(s.sliderValue ?? 65)}
              @input=${(e) => s.set({ sliderValue: Number(e.target.value) })}
            ></md-slider>
          </div>
        </section>

        <section class="app-card-surface">
          <div class="title-medium" style="font-weight:700">MaterialShapes Expressive Badges</div>
          <div style="display:flex;flex-wrap:wrap;gap:12px">
            ${[
              ["soft_burst", "calculate", "gold"],
              ["cookie_9sided", "straighten", "gold"],
              ["flower", "dehaze", "gold"],
              ["gem", "casino", "gold"],
              ["cookie_12sided", "casino", "gold"],
              ["sunny", "explore", "teal"],
              ["clover_4leaf", "casino", "orange"],
              ["cookie_7sided", "tune", "magenta"],
              ["clover_8leaf", "self_improvement", "amber"],
            ].map(([shape, icon, palette]) =>
              shapeBadge({ shape, icon, palette, size: 56, iconSize: 24 }),
            )}
          </div>
        </section>
      </div>
    `;

    return screen({
      top: topAppBar({
        nav: "back",
        title: "Components Showcase",
        onNav: () => s.go("numbers", {}),
      }),
      content,
      navType: s.navType,
      window: s.window,
      tab: "quick-tools",
      onSelect: s.go,
      carded: false,
    });
  },
};
