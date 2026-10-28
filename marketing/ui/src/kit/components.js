import { html, nothing } from "lit";
import { shapeMaskStyle, shapeUrl } from "./assets.js";
import { extraToolBody } from "./tool-bodies.js";

/**
 * MaterialShapes polygon badge matching `TileIconBadge` in `TileBadges.kt` and
 * `ToolkitTileVisuals.kt` (`SoftBurst`, `Cookie9Sided`, `Flower`, `Gem`, `Cookie12Sided`,
 * `Sunny`, `Clover4Leaf`, `Arch`, `Pill`, `Pentagon`, `Boom`, `Burst`, etc.).
 *
 * In Compose:
 * - `large = true` -> `56dp` (`LauncherIconSize 48dp + SmallSize 8dp`)
 * - `large = false` -> `44dp` (`LauncherIconSize 48dp - ExtraSmallSize 4dp`)
 * - Background is `colors.container.copy(alpha = 0.15f)`
 * - Icon is `24dp` tinted with `colors.content`
 */
export function shapeBadge({
  shape = "cookie_9sided",
  icon = "widgets",
  palette = "gold",
  bg,
  fg,
  size = 56,
  iconSize = 24,
} = {}) {
  const resolvedBg =
    bg ||
    (palette === "primary"
      ? "color-mix(in srgb, var(--md-sys-color-primary-container) 45%, transparent)"
      : `color-mix(in srgb, var(--tk-${palette}-container) 26%, var(--md-sys-color-surface-container-highest))`);
  const resolvedFg =
    fg ||
    (palette === "primary"
      ? "var(--md-sys-color-primary)"
      : `var(--tk-${palette}-content)`);

  return html`
    <div
      class="tk-shape-badge"
      style="
        width:${size}px;
        height:${size}px;
        background:${resolvedBg};
        color:${resolvedFg};
        ${shapeMaskStyle(shapeUrl(shape))}
      "
    >
      <span class="material-symbols-outlined" style="font-size:${iconSize}px">
        ${icon}
      </span>
    </div>
  `;
}

/**
 * Renders `ToolkitToolChips` (`TileBadges.kt` lines 81-160):
 * - `ToolTypeChip`: `secondaryContainer` / `onSecondaryContainer`, NO icon, `border = null`
 * - `TileAvailabilityChip`: `primaryContainer` / `onPrimaryContainer`, `check_circle` icon, `border = null`
 * - `TileStatusChip`: `Added` (`primaryContainer`) or `Not added` (`surfaceContainerHighest`), `border = null`
 */
export function toolMetaChips({
  kindLabel = "Quick tool",
  supportsTile = true,
  tileAdded = false,
  showTileStatus = false,
  palette = "gold",
} = {}) {
  return html`
    <div class="tk-meta-chip-set" style="--chip-bg:color-mix(in srgb, var(--tk-${palette}-container) 24%, var(--md-sys-color-surface-container-highest));--chip-fg:var(--tk-${palette}-content)" role="group" aria-label="Tool capabilities">
      <md-assist-chip class="tk-chip--type" .label=${kindLabel}></md-assist-chip>
      ${supportsTile
        ? html`
            <md-assist-chip class="tk-chip--tile" label="Tile">
              <md-icon slot="icon">check_circle</md-icon>
            </md-assist-chip>
          `
        : nothing}
      ${supportsTile && showTileStatus
        ? html`
            <md-assist-chip
              class=${tileAdded ? "tk-chip--added" : "tk-chip--not-added"}
              .label=${tileAdded ? "Added" : "Not added"}
            >
              <md-icon slot="icon">
                ${tileAdded ? "check_circle" : "add_circle"}
              </md-icon>
            </md-assist-chip>
          `
        : nothing}
    </div>
  `;
}

/**
 * Interactive `ToolkitToolBottomSheet` modal overlay matching
 * `ToolkitToolBottomSheet.kt` (`dragHandle = null`, `ToolSheetHeader` with 56dp
 * `TileIconBadge`, `ToolInteractiveContent`, and 2-part grouped
 * `QuickSettingsIntegrationCard`).
 */
export function renderToolBottomSheet({
  tool,
  state,
  onState,
  onClose,
  showQuickSettingsAction = true,
}) {
  if (!tool) return nothing;

  const isAdded = Boolean(state.addedTiles?.[tool.id]);
  const showQsCard = showQuickSettingsAction && tool.supportsTile !== false;

  return html`
    <div class="tk-sheet-backdrop" @click=${onClose}>
      <div class="tk-sheet-surface" role="dialog" aria-label=${tool.title} @click=${(e) => e.stopPropagation()}>
        <div class="tk-sheet-title-row">
          ${shapeBadge({
            shape: tool.shape,
            icon: tool.icon,
            palette: tool.palette || "gold",
            size: 64,
            iconSize: 32,
          })}
          <div class="tk-sheet-title-meta">
            <h3 class="tk-sheet-title">${tool.title}</h3>
            <p class="tk-sheet-sub">${tool.subtitle}</p>
          </div>
          <md-icon-button aria-label="Close" @click=${onClose}>
            <md-icon>close</md-icon>
          </md-icon-button>
        </div>

        ${renderToolBody(tool, state, onState)}

        ${showQsCard
          ? html`
              <div class="tk-qs-integration-group">
                <div
                  class="tk-qs-integration-top ${isAdded
                    ? "tk-qs-integration-top--single"
                    : ""}"
                >
                  <span
                    class="material-symbols-outlined"
                    style="font-size:24px;flex-shrink:0"
                  >
                    ${isAdded ? "check_circle" : "widgets"}
                  </span>
                  <div style="display:flex;flex-direction:column;gap:4px">
                    <div
                      style="font:600 14px/20px var(--md-ref-typeface-plain);letter-spacing:0.1px"
                    >
                      ${isAdded ? "Added to Quick Settings" : "Quick Settings tile"}
                    </div>
                    <div
                      style="font:400 12px/16px var(--md-ref-typeface-plain);letter-spacing:0.4px"
                    >
                      ${isAdded
                        ? "This tool is already in your Quick Settings panel. Pull down from the top of the screen to use it anytime."
                        : `Use ${tool.title} from your phone's Quick Settings without opening App Toolkit.`}
                    </div>
                  </div>
                </div>
                ${!isAdded
                  ? html`
                      <button
                        class="tk-qs-integration-btn"
                        @click=${() => {
                          const nextAdded = {
                            ...(state.addedTiles || {}),
                            [tool.id]: true,
                          };
                          onState({ addedTiles: nextAdded });
                        }}
                      >
                        <span
                          class="material-symbols-outlined"
                          style="font-size:18px"
                        >
                          add
                        </span>
                        Add to Quick Settings
                      </button>
                    `
                  : nothing}
              </div>
            `
          : nothing}
      </div>
    </div>
  `;
}

function renderToolBody(tool, state, onState) {
  const extra = extraToolBody(tool, state, onState);
  if (extra) return extra;
  switch (tool.id) {
    case "counter": {
      const count = Number(state.counterValue ?? 12);
      const step = Number(state.counterStep ?? 1);
      return html`
        <div class="tk-tool-interactive">
          <div class="tk-counter-Readout">
            <div
              class="tk-counter-shape"
              style=${shapeMaskStyle(shapeUrl("cookie_9sided"))}
            >
              <span class="display-medium" style="font-weight:700">${count}</span>
            </div>
            <span
              class="label-medium"
              style="color:var(--md-sys-color-on-surface-variant)"
            >
              Current tally · Step ±${step}
            </span>
          </div>

          <md-chip-set aria-label="Step increment">
            ${[1, 5, 10].map(
              (s) => html`
                <md-filter-chip
                  label="+${s} step"
                  ?selected=${step === s}
                  @click=${() => onState({ counterStep: s })}
                ></md-filter-chip>
              `,
            )}
          </md-chip-set>

          <div class="tk-action-row">
            <md-filled-tonal-button
              @click=${() => onState({ counterValue: count - step })}
            >
              <md-icon slot="icon">remove</md-icon>
              -${step}
            </md-filled-tonal-button>
            <md-outlined-button @click=${() => onState({ counterValue: 0 })}>
              <md-icon slot="icon">restart_alt</md-icon>
              Reset
            </md-outlined-button>
            <md-filled-button
              @click=${() => onState({ counterValue: count + step })}
            >
              <md-icon slot="icon">add</md-icon>
              +${step}
            </md-filled-button>
          </div>
        </div>
      `;
    }

    case "bill_splitter": {
      const bill = Number(state.billAmount ?? 84);
      const tipPct = Number(state.tipPercent ?? 15);
      const people = Math.max(1, Number(state.splitPeople ?? 3));
      const tipTotal = (bill * tipPct) / 100;
      const grandTotal = bill + tipTotal;
      const perPerson = grandTotal / people;

      return html`
        <div class="tk-tool-interactive">
          <div class="tk-result-banner">
            <div>
              <div class="label-small">Each person pays</div>
              <div class="headline-medium" style="font-weight:700">
                $${perPerson.toFixed(2)}
              </div>
            </div>
            <div style="text-align:right">
              <div class="label-small">Total with ${tipPct}% tip</div>
              <div class="title-medium" style="font-weight:600">
                $${grandTotal.toFixed(2)}
              </div>
            </div>
          </div>

          <div class="tk-form-row">
            <md-outlined-text-field
              label="Bill Subtotal ($)"
              type="number"
              style="width:100%"
              .value=${String(bill)}
              @input=${(e) => onState({ billAmount: Number(e.target.value) || 0 })}
            ></md-outlined-text-field>
          </div>

          <div>
            <div class="label-medium" style="margin-bottom:6px">Tip percentage</div>
            <md-chip-set aria-label="Tip percentage">
              ${[10, 15, 18, 20, 25].map(
                (pct) => html`
                  <md-filter-chip
                    label="${pct}%"
                    ?selected=${tipPct === pct}
                    @click=${() => onState({ tipPercent: pct })}
                  ></md-filter-chip>
                `,
              )}
            </md-chip-set>
          </div>

          <div class="tk-split-stepper">
            <span class="body-medium">
              Split between <strong>${people}</strong> people
            </span>
            <div style="display:flex;gap:8px">
              <md-filled-tonal-icon-button
                aria-label="Fewer people"
                @click=${() => onState({ splitPeople: Math.max(1, people - 1) })}
              >
                <md-icon>remove</md-icon>
              </md-filled-tonal-icon-button>
              <md-filled-tonal-icon-button
                aria-label="More people"
                @click=${() => onState({ splitPeople: people + 1 })}
              >
                <md-icon>add</md-icon>
              </md-filled-tonal-icon-button>
            </div>
          </div>
        </div>
      `;
    }

    case "unit_converter": {
      const category = state.converterCategory || "Length";
      const inputVal = Number(state.converterInput ?? 10);
      const conversions = {
        Length: { from: "km", to: "mi", factor: 0.621371 },
        Mass: { from: "kg", to: "lb", factor: 2.20462 },
        Temperature: { from: "°C", to: "°F", calc: (c) => (c * 9) / 5 + 32 },
        Volume: { from: "L", to: "gal", factor: 0.264172 },
      };
      const spec = conversions[category] || conversions.Length;
      const outVal = spec.calc ? spec.calc(inputVal) : inputVal * spec.factor;

      return html`
        <div class="tk-tool-interactive">
          <md-chip-set aria-label="Unit category">
            ${Object.keys(conversions).map(
              (cat) => html`
                <md-filter-chip
                  label=${cat}
                  ?selected=${category === cat}
                  @click=${() => onState({ converterCategory: cat })}
                ></md-filter-chip>
              `,
            )}
          </md-chip-set>

          <div class="tk-converter-grid">
            <md-outlined-text-field
              label="Input (${spec.from})"
              type="number"
              .value=${String(inputVal)}
              @input=${(e) =>
                onState({ converterInput: Number(e.target.value) || 0 })}
            ></md-outlined-text-field>

            <div class="tk-result-banner" style="margin:0">
              <div>
                <div class="label-small">Converted (${spec.to})</div>
                <div class="headline-small" style="font-weight:700">
                  ${outVal.toFixed(2)} ${spec.to}
                </div>
              </div>
              <span class="material-symbols-outlined">swap_vert</span>
            </div>
          </div>
        </div>
      `;
    }

    case "random_number": {
      const min = Number(state.randomMin ?? 1);
      const max = Number(state.randomMax ?? 100);
      const result = Number(state.randomResult ?? 42);
      return html`
        <div class="tk-tool-interactive">
          <div class="tk-counter-Readout">
            <div
              class="tk-counter-shape"
              style=${shapeMaskStyle(shapeUrl("gem"))}
            >
              <span class="display-medium" style="font-weight:700">${result}</span>
            </div>
            <span
              class="label-medium"
              style="color:var(--md-sys-color-on-surface-variant)"
            >
              Range: ${min} to ${max}
            </span>
          </div>

          <div style="display:grid;grid-template-columns:1fr 1fr;gap:10px">
            <md-outlined-text-field
              label="Min"
              type="number"
              .value=${String(min)}
              @input=${(e) => onState({ randomMin: Number(e.target.value) || 0 })}
            ></md-outlined-text-field>
            <md-outlined-text-field
              label="Max"
              type="number"
              .value=${String(max)}
              @input=${(e) => onState({ randomMax: Number(e.target.value) || 100 })}
            ></md-outlined-text-field>
          </div>

          <md-filled-button
            style="width:100%"
            @click=${() => {
              const lo = Math.min(min, max);
              const hi = Math.max(min, max);
              const next = Math.floor(Math.random() * (hi - lo + 1)) + lo;
              onState({ randomResult: next });
            }}
          >
            <md-icon slot="icon">casino</md-icon>
            Generate Random Number
          </md-filled-button>
        </div>
      `;
    }

    case "dice_roll": {
      const sides = Number(state.diceSides ?? 6);
      const diceResult = Number(state.diceResult ?? 5);
      return html`
        <div class="tk-tool-interactive">
          <div class="tk-counter-Readout">
            <div
              class="tk-counter-shape"
              style=${shapeMaskStyle(shapeUrl("cookie_12sided"))}
            >
              <span class="display-medium" style="font-weight:700">
                ${diceResult}
              </span>
            </div>
            <span
              class="label-medium"
              style="color:var(--md-sys-color-on-surface-variant)"
            >
              d${sides} die · Rolled ${diceResult}
            </span>
          </div>

          <md-chip-set aria-label="Die sides">
            ${[6, 8, 10, 12, 20].map(
              (s) => html`
                <md-filter-chip
                  label="d${s}"
                  ?selected=${sides === s}
                  @click=${() =>
                    onState({
                      diceSides: s,
                      diceResult: Math.floor(Math.random() * s) + 1,
                    })}
                ></md-filter-chip>
              `,
            )}
          </md-chip-set>

          <md-filled-button
            style="width:100%"
            @click=${() => {
              const next = Math.floor(Math.random() * sides) + 1;
              onState({ diceResult: next });
            }}
          >
            <md-icon slot="icon">casino</md-icon>
            Roll d${sides}
          </md-filled-button>
        </div>
      `;
    }

    default: {
      return html`
        <div class="tk-tool-interactive">
          <div class="tk-result-banner">
            <div>
              <div class="label-small">Interactive Tool Surface</div>
              <div class="title-medium" style="font-weight:600">
                ${tool.title} is ready
              </div>
            </div>
            <span class="material-symbols-outlined">${tool.icon}</span>
          </div>
          <p
            class="body-medium"
            style="margin:0;color:var(--md-sys-color-on-surface-variant)"
          >
            ${tool.description || tool.subtitle}
          </p>
        </div>
      `;
    }
  }
}
