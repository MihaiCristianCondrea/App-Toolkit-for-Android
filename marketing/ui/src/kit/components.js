import {wheelBody} from "./decision-wheel.js";
import {waterBody} from "./water-eject.js";
import { measureBody, rulerDialog, protractorDialog } from "./measure.js";
import { chanceBody } from "./chance.js";
import { html, nothing } from "lit";
import { shapeMaskStyle, shapeUrl } from "./assets.js";
import { extraToolBody } from "./tool-bodies.js";
import { materialColorsSheet } from "./material-colors.js";

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
 * The overlay a tool opens, for the shell's `overlay` (Material Next draws the scrim, the sheet, dismissal and
 * focus): `ToolkitToolBottomSheet.kt` (`ToolSheetHeader` with the 56 dp `TileIconBadge`,
 * `ToolInteractiveContent`, the two-part `QuickSettingsIntegrationCard`), or the Surface Ruler's full-screen dialog.
 * `onState` is absent in videos and captures.
 */
export function toolOverlay({ tool, state, onState, onClose, showQuickSettingsAction = true }) {
  if (!tool) return null;
  if (tool.id === "protractor") {
    return { presentation: "fullscreen", label: tool.title, content: protractorDialog(state, onState, onClose), onDismiss: onClose };
  }
  if (tool.id === "material_colors") {
    return { presentation: "sheet", label: tool.title, handle: true, flush: true, content: materialColorsSheet(onClose), onDismiss: onClose };
  }
  if (tool.id === "ruler") {
    return { presentation: "fullscreen", label: tool.title, content: rulerDialog(state, onState, onClose), onDismiss: onClose };
  }
  const isAdded = Boolean(state.addedTiles?.[tool.id]);
  const showQsCard = showQuickSettingsAction && tool.supportsTile !== false;
  const content = html`
    <div class="tk-sheet-content">
      <div class="tk-sheet-title-row">
        ${shapeBadge({ shape: tool.shape, icon: tool.icon, palette: tool.palette || "gold", size: 64, iconSize: 32 })}
        <div class="tk-sheet-title-meta">
          <h3 class="tk-sheet-title">${tool.title}</h3>
          <p class="tk-sheet-sub">${tool.subtitle}</p>
        </div>
        <md-icon-button aria-label="Close" @click=${() => onClose?.()}><md-icon>close</md-icon></md-icon-button>
      </div>
      ${renderToolBody(tool, state, onState)}
      ${showQsCard
        ? html`<div class="tk-qs-integration-group">
            <div class="tk-qs-integration-top ${isAdded ? "tk-qs-integration-top--single" : ""}">
              <span class="material-symbols-outlined" style="font-size:24px;flex-shrink:0">${isAdded ? "check_circle" : "widgets"}</span>
              <div style="display:flex;flex-direction:column;gap:4px">
                <div style="font:600 14px/20px var(--md-ref-typeface-plain);letter-spacing:0.1px">
                  ${isAdded ? "Added to Quick Settings" : "Quick Settings tile"}
                </div>
                <div style="font:400 12px/16px var(--md-ref-typeface-plain);letter-spacing:0.4px">
                  ${isAdded
                    ? "This tool is already in your Quick Settings panel. Pull down from the top of the screen to use it anytime."
                    : `Use ${tool.title} from your phone's Quick Settings without opening App Toolkit.`}
                </div>
              </div>
            </div>
            ${!isAdded
              ? html`<button class="tk-qs-integration-btn" @click=${() => onState?.({ addedTiles: { ...(state.addedTiles || {}), [tool.id]: true } })}>
                  <span class="material-symbols-outlined" style="font-size:18px">add</span>
                  Add to Quick Settings
                </button>`
              : nothing}
          </div>`
        : nothing}
    </div>
  `;
  return { presentation: "sheet", label: tool.title, content, onDismiss: onClose };
}

function renderToolBody(tool, state, onState) {
  if(tool.id === "decision_wheel") return wheelBody(state,onState);
  if(tool.id === "water_eject") return waterBody(state,onState);
  const measure = measureBody(tool,state,onState);
  if (measure) return measure;
  const chance = chanceBody(tool, state, onState);
  if (chance) return chance;
  const extra = extraToolBody(tool, state, onState);
  if (extra) return extra;
  switch (tool.id) {
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
