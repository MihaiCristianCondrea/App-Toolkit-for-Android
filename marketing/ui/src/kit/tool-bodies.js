import { html, nothing, svg } from "lit";
import { asset, shapeMaskStyle } from "./assets.js";

/**
 * Interactive bodies for the tool bottom sheet, plus the live sensor dials shared with the Quick Tools
 * glance card. Every tool here works without a special permission: sensors, display, torch.
 */

/** BreathingRepository.kt: inhale 4 s, hold 2 s, exhale 4 s, hold 1 s. */
export const BREATH_STEPS = [["Inhale", 4], ["Hold", 2], ["Exhale", 4], ["Hold", 1]];

/** The brand's own MaterialShapes exports in marketing/assets/shapes, used as masks. */
const brandShape = (name) => asset(`shapes/${encodeURIComponent(name)}.svg`);

/** Two values side by side as a connected pair: round outer corners, small inner ones (ResultPill.kt colours). */
const pillPair = (a, b) => html`<div class="tk-pill-pair"><span class="tk-pill">${a}</span><span class="tk-pill">${b}</span></div>`;

const CARDINALS = ["N", "NE", "E", "SE", "S", "SW", "W", "NW"];
export const cardinal = (deg) => CARDINALS[Math.round((((deg % 360) + 360) % 360) / 45) % 8];

/** Compass dial: the needle points at magnetic north, so it turns against the device heading. */
export const compassDial = (heading = 312, size = 96) => {
  const ticks = Array.from({ length: 24 }, (_, i) => {
    const long = i % 6 === 0;
    return html`<line x1="50" y1="${long ? 7 : 8}" x2="50" y2="${long ? 15 : 12}" transform="rotate(${i * 15} 50 50)" />`;
  });
  return html`<svg class="tk-dial tk-dial--compass" width=${size} height=${size} viewBox="0 0 100 100" role="img"
    aria-label="Compass, heading ${heading} degrees ${cardinal(heading)}">
    <circle class="tk-dial__ring" cx="50" cy="50" r="46" />
    <g class="tk-dial__ticks">${ticks}</g>
    <text class="tk-dial__n" x="50" y="29" text-anchor="middle">N</text>
    <g transform="rotate(${-heading} 50 50)">
      <path class="tk-dial__needle" d="M50 22 L56 50 L44 50 Z" />
      <path class="tk-dial__needle-tail" d="M50 78 L56 50 L44 50 Z" />
    </g>
    <circle class="tk-dial__hub" cx="50" cy="50" r="3.5" />
  </svg>`;
};

const DIRECTIONS = ["North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest"];
/** "Northwest" for 312: the eight-point name of a heading. */
export const direction = (deg) => DIRECTIONS[Math.round((((deg % 360) + 360) % 360) / 45) % 8];

/**
 * Compass rose: a ring of ticks every 5 degrees, degree numbers every 30 and the four cardinal names, turning
 * against the heading so the fixed pointer in the middle always points where the phone points. `labels: false`
 * leaves out the numbers and names, for small sizes.
 */
export const compassRose = (heading = 312, size = 240, { labels = true, face = true } = {}) => {
  const ticks = Array.from({ length: 72 }, (_, i) => {
    const deg = i * 5;
    const kind = deg % 30 === 0 ? "major" : deg % 10 === 0 ? "mid" : "minor";
    const len = { major: 9, mid: 6, minor: 4 }[kind];
    return svg`<line class="tk-rose__tick tk-rose__tick--${kind} ${deg === 0 ? "tk-rose__tick--north" : ""}"
      x1="100" y1="6" x2="100" y2="${6 + len}" transform="rotate(${deg} 100 100)" />`;
  });
  const numbers = labels
    ? [30, 60, 120, 150, 210, 240, 300, 330].map((deg) => svg`<text class="tk-rose__deg" x="100" y="27" text-anchor="middle" transform="rotate(${deg} 100 100)">${deg}</text>`)
    : nothing;
  const names = [["N", "North", 0], ["E", "East", 90], ["S", "South", 180], ["W", "West", 270]].map(([short, long, deg]) =>
    svg`<text class="tk-rose__card ${deg === 0 ? "tk-rose__card--north" : ""}" x="100" y="${labels ? 44 : 34}" text-anchor="middle"
      transform="rotate(${deg} 100 100)">${labels ? long : short}</text>`);
  return html`<svg class="tk-rose" width=${size} height=${size} viewBox="0 0 200 200" role="img"
    aria-label="Compass, heading ${heading} degrees ${direction(heading)}">
    ${face ? svg`<circle class="tk-rose__face" cx="100" cy="100" r="98" />` : nothing}
    <g class="tk-rose__dial" style="transform:rotate(${-heading}deg)">${ticks}${numbers}${names}</g>
    <path class="tk-rose__lubber" d="M100 0 L105 9 L95 9 Z" />
    <path class="tk-rose__pointer" d="M100 62 L124 128 L100 116 L76 128 Z" />
  </svg>`;
};

/** Bubble level dial; x and y are tilt in degrees and move the bubble inside the 10 degree window. */
export const levelDial = (x = 0.4, y = -0.2, size = 96, { ring = true, edge = false } = {}) => {
  const px = 50 + Math.max(-1, Math.min(1, x / 10)) * 30;
  const py = 50 + Math.max(-1, Math.min(1, y / 10)) * 30;
  return html`<svg class="tk-dial tk-dial--level" width=${size} height=${size} viewBox="0 0 100 100" role="img"
    aria-label="Bubble level, ${x} degrees roll, ${y} degrees pitch">
    ${ring ? svg`<circle class="tk-dial__ring" cx="50" cy="50" r="46" />` : nothing}
    <circle class="tk-dial__guide" cx="50" cy="50" r="18" />
    <line class="tk-dial__guide" x1=${edge ? 0 : 8} y1="50" x2=${edge ? 100 : 92} y2="50" />
    <line class="tk-dial__guide" x1="50" y1=${edge ? 0 : 8} x2="50" y2=${edge ? 100 : 92} />
    <circle class="tk-dial__bubble" cx="${px}" cy="${py}" r="9" />
  </svg>`;
};

const stat = (label, value) => html`<div class="tk-stat"><span class="tk-stat__label">${label}</span><span class="tk-stat__value">${value}</span></div>`;

const MORSE = { S: "...", O: "---", E: ".", T: "-", A: ".-", N: "-.", I: "..", M: "--", H: "....", R: ".-.", L: ".-..", D: "-..", U: "..-", W: ".--", K: "-.-", G: "--.", C: "-.-.", B: "-...", F: "..-.", P: ".--.", J: ".---", Q: "--.-", V: "...-", X: "-..-", Y: "-.--", Z: "--..", " ": "/", 0:"-----", 1:".----", 2:"..---", 3:"...--", 4:"....-", 5:".....", 6:"-....", 7:"--...", 8:"---..", 9:"----." };
const toMorse = (text) => [...text.toUpperCase()].map((c) => MORSE[c] ?? "").filter(Boolean).join(" ");

const roundShare = (value, mode) => (mode === "up" ? Math.ceil(value) : mode === "five" ? Math.ceil(value / 5) * 5 : value);

/** Returns the template for `tool`, or null when the generic body in components.js should render. */
export function extraToolBody(tool, state, onState) {
  switch (tool.id) {
    case "bill_splitter": {
      const bill = Number(state.billAmount ?? 100);
      const tipPct = Number(state.tipPercent ?? 15);
      const people = Math.max(1, Number(state.splitPeople ?? 4));
      const mode = state.billRound || "exact";
      const tip = (bill * tipPct) / 100;
      const total = bill + tip;
      const share = roundShare(total / people, mode);
      return html`
        <div class="tk-tool-interactive">
          <div class="tk-result">
            <span class="tk-result__label">Total per person</span>
            <span class="tk-result__value">$${share.toFixed(2)}</span>
            <div class="tk-stat-grid">
              ${stat("Subtotal", `$${bill.toFixed(2)}`)} ${stat(`Tip (${tipPct}%)`, `$${tip.toFixed(2)}`)}
              ${stat("Total bill", `$${total.toFixed(2)}`)} ${stat("Party split", `${people} ${people === 1 ? "person" : "people"}`)}
            </div>
          </div>
          <md-outlined-text-field label="Bill subtotal ($)" type="number" style="width:100%" .value=${String(bill)}
            @input=${(e) => onState({ billAmount: Number(e.target.value) || 0 })}></md-outlined-text-field>
          <div class="tk-field">
            <span class="tk-field__label">Tip</span>
            <md-chip-set aria-label="Tip percentage">
              ${[10, 15, 18, 20, 25].map((p) => html`<md-filter-chip label="${p}%" ?selected=${tipPct === p} @click=${() => onState({ tipPercent: p })}></md-filter-chip>`)}
            </md-chip-set>
          </div>
          <div class="tk-split-stepper">
            <span class="body-medium">Split between <strong>${people}</strong></span>
            <div style="display:flex;gap:8px">
              <md-filled-tonal-icon-button aria-label="Fewer people" @click=${() => onState({ splitPeople: Math.max(1, people - 1) })}><md-icon>remove</md-icon></md-filled-tonal-icon-button>
              <md-filled-tonal-icon-button aria-label="More people" @click=${() => onState({ splitPeople: people + 1 })}><md-icon>add</md-icon></md-filled-tonal-icon-button>
            </div>
          </div>
          <div class="tk-field">
            <span class="tk-field__label">Round each share</span>
            <md-chip-set aria-label="Rounding">
              ${[["exact", "Exact"], ["up", "Round up"], ["five", "Round to $5"]].map(([id, label]) => html`<md-filter-chip label=${label} ?selected=${mode === id} @click=${() => onState({ billRound: id })}></md-filter-chip>`)}
            </md-chip-set>
          </div>
        </div>`;
    }

    case "compass": {
      const heading = Number(state.compassHeading ?? 312);
      return html`
        <div class="tk-tool-interactive tk-tool-interactive--center">
          ${compassRose(heading, 272)}
          <div class="tk-compass-readout">
            <span class="tk-compass-readout__deg">${heading}°</span>
            <span class="tk-compass-readout__dir">${direction(heading)}</span>
            <span class="tk-readout__note">Magnetic north, from the geomagnetic sensor</span>
          </div>
        </div>`;
    }

    case "bubble_level": {
      // LevelTool.kt: a 200dp face over the pitch and roll pills, with a 40dp target ring and a 30dp bubble that moves
      // 80dp at 45°, primary when level and secondary otherwise. The face is the 16-sided cookie, crossed edge to
      // edge like the Quick Tools widget, and the pills sit under it.
      const roll = Number(state.levelRoll ?? 0.4);
      const pitch = Number(state.levelPitch ?? -0.2);
      const flat = Math.abs(roll) < 1 && Math.abs(pitch) < 1;
      const shift = (deg) => (-Math.max(-45, Math.min(45, deg)) / 45) * 80;
      return html`
        <div class="tk-tool-interactive tk-tool-interactive--center">
          <div class="tk-level" style=${shapeMaskStyle(brandShape("16 Sided Cookie"))} role="img"
            aria-label="Bubble level, ${flat ? "level" : "tilted"}, pitch ${Math.trunc(pitch)} degrees, roll ${Math.trunc(roll)} degrees">
            <svg class="tk-level__cross" viewBox="0 0 100 100" aria-hidden="true">
              <line x1="0" y1="50" x2="100" y2="50" /><line x1="50" y1="0" x2="50" y2="100" />
            </svg>
            <span class="tk-level__target"></span>
            <span class="tk-level__bubble ${flat ? "is-level" : ""}" style="transform:translate(${shift(roll)}px, ${shift(pitch)}px)"></span>
          </div>
          ${pillPair(`P: ${Math.trunc(pitch)}°`, `R: ${Math.trunc(roll)}°`)}
        </div>`;
    }

    case "flash_dimmer": {
      // FlashDimmerTool.kt: presets on the left (weight 3), a segmented level bar on the right (weight 2) that
      // follows the finger, 240dp tall. Level 0 is off; the maximum comes from the device (a Pixel 8 Pro has 45).
      const max = Math.max(1, Number(state.flashMaxLevel ?? 5));
      const level = state.flashOn ? Math.min(Number(state.flashLevel ?? 3), max) : 0;
      if (max === 1) {
        // SimpleTorchControl: a torch that cannot dim is only on or off.
        return html`
          <div class="tk-tool-interactive tk-tool-interactive--center">
            <span class="tk-pill tk-pill--single">${state.flashOn ? "On" : "Off"}</span>
            <md-filled-button @click=${() => onState({ flashOn: !state.flashOn, flashLevel: 1 })}>${state.flashOn ? "Turn off" : "Turn on"}</md-filled-button>
          </div>`;
      }
      const half = Math.max(1, Math.floor((max + 1) / 2));
      const setLevel = (next) => onState({ flashOn: next > 0, flashLevel: next > 0 ? next : Number(state.flashLevel ?? 3) });
      const preset = (label, value) => level === value
        ? html`<md-filled-button @click=${() => setLevel(value)}>${label}</md-filled-button>`
        : html`<md-filled-tonal-button @click=${() => setLevel(value)}>${label}</md-filled-tonal-button>`;
      const fromPointer = (e) => {
        const r = e.currentTarget.getBoundingClientRect();
        const y = Math.min(Math.max(e.clientY - r.top, 0), r.height);
        const next = Math.round(max * (1 - y / r.height));
        if (next !== level) setLevel(next);
      };
      return html`
        <div class="tk-flash">
          <p class="tk-flash__level">Brightness ${level} of ${max}</p>
          <div class="tk-flash__row">
            <div class="tk-flash__presets">
              ${preset("Maximum", max)}${preset("Half", half)}${preset("Minimum", 1)}${preset("Turn off", 0)}
            </div>
            <div class="tk-flash__bar" role="slider" tabindex="0" aria-label="Flashlight level" aria-valuemin="0"
              aria-valuemax=${max} aria-valuenow=${level} aria-valuetext="Brightness ${level} of ${max}"
              @pointerdown=${(e) => { e.currentTarget.setPointerCapture(e.pointerId); fromPointer(e); }}
              @pointermove=${(e) => e.buttons && fromPointer(e)}
              @keydown=${(e) => {
                const step = { ArrowUp: 1, ArrowRight: 1, ArrowDown: -1, ArrowLeft: -1 }[e.key];
                if (step) { e.preventDefault(); setLevel(Math.min(max, Math.max(0, level + step))); }
              }}>
              ${Array.from({ length: max }, (_, i) => html`<span class="tk-flash__segment ${level >= max - i ? "is-on" : ""}"></span>`)}
            </div>
          </div>
        </div>`;
    }

    case "breathing": {
      // BreathingTool.kt: a 200dp circle over the phase and seconds pills; it grows to 1 on the inhale and shrinks to
      // 0.4 on the exhale, primary while inhaling and secondary while exhaling. It starts when the sheet opens.
      const phase = state.breathPhase ?? "INHALE";
      const tone = phase === "INHALE" ? "inhale" : phase === "EXHALE" ? "exhale" : "hold";
      return html`
        <div class="tk-tool-interactive tk-tool-interactive--center">
          <div class="tk-breathe tk-breathe--${tone}" style="transform:scale(${Number(state.breathProgress ?? 0.7)})" role="img" aria-label="Breathing, ${phase.toLowerCase().replace("_", " ")}">
            <md-icon>favorite</md-icon>
          </div>
          ${pillPair(phase.replace("_", " "), `${state.breathSeconds ?? 3}s`)}
        </div>`;
    }

    case "morse_sos": {
      const messageMode=state.morseMode==='message',text=messageMode?(state.morseText??'SOS'):'SOS',sending=Boolean(state.sosActive),code=toMorse(text),valid=/[a-z0-9]/i.test(text);
      return html`<div class="sos-body">
        <md-outlined-segmented-button-set aria-label="Signal mode" @segmented-button-set-selection=${e=>{if(e.detail.selected&&!sending)onState?.({morseMode:e.detail.index?'message':'sos'});}}>
          <md-outlined-segmented-button label="SOS" .selected=${!messageMode} ?disabled=${sending}></md-outlined-segmented-button>
          <md-outlined-segmented-button label="Morse message" .selected=${messageMode} ?disabled=${sending}></md-outlined-segmented-button>
        </md-outlined-segmented-button-set>
        <div class="sos-signal" data-active=${sending}><div class="sos-signal-mark" aria-hidden="true"><md-icon>${messageMode?'sms':'emergency'}</md-icon></div><span class="sos-signal-label">${messageMode?'YOUR MESSAGE':'DISTRESS SIGNAL'}</span><strong>${messageMode?'Morse code':'SOS'}</strong><span class="sos-status" role="status">${sending?'Preview running':'Ready'}</span></div>
        ${messageMode?html`<md-outlined-text-field label="Message" supporting-text="Letters A–Z and numbers 0–9" maxlength="120" style="width:100%" .value=${state.morseText??'SOS'} ?disabled=${sending} @input=${e=>onState?.({morseText:e.target.value})}></md-outlined-text-field>`:nothing}
        <div class="sos-code-panel"><span>${messageMode?'MORSE CODE':'THREE SHORT · THREE LONG · THREE SHORT'}</span><div class="tk-morse" aria-label="Signal code">${code||'Type a message'}</div></div>
        <md-filled-button class="sos-primary" ?disabled=${!valid&&!sending} @click=${()=>onState?.({sosActive:!sending})}><md-icon slot="icon">${sending?'stop':'play_arrow'}</md-icon>${sending?'Stop preview':messageMode?'Preview message':'Start SOS preview'}</md-filled-button>
        <p class="sos-guidance"><md-icon aria-hidden="true">visibility</md-icon>Keep flashing light out of anyone's eyes.</p>
      </div>`;
    }

    default:
      return null;
  }
}
