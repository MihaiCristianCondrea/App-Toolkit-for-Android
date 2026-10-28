import { html, nothing, svg } from "lit";

/**
 * Interactive bodies for the tool bottom sheet, plus the live sensor dials shared with the Quick Tools
 * glance card. Every tool here works without a special permission: sensors, display, torch.
 */

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
export const compassRose = (heading = 312, size = 240, { labels = true } = {}) => {
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
    <circle class="tk-rose__face" cx="100" cy="100" r="98" />
    <g class="tk-rose__dial" style="transform:rotate(${-heading}deg)">${ticks}${numbers}${names}</g>
    <path class="tk-rose__lubber" d="M100 0 L105 9 L95 9 Z" />
    <path class="tk-rose__pointer" d="M100 62 L124 128 L100 116 L76 128 Z" />
  </svg>`;
};

/** Bubble level dial; x and y are tilt in degrees and move the bubble inside the 10 degree window. */
export const levelDial = (x = 0.4, y = -0.2, size = 96) => {
  const px = 50 + Math.max(-1, Math.min(1, x / 10)) * 30;
  const py = 50 + Math.max(-1, Math.min(1, y / 10)) * 30;
  return html`<svg class="tk-dial tk-dial--level" width=${size} height=${size} viewBox="0 0 100 100" role="img"
    aria-label="Bubble level, ${x} degrees roll, ${y} degrees pitch">
    <circle class="tk-dial__ring" cx="50" cy="50" r="46" />
    <circle class="tk-dial__guide" cx="50" cy="50" r="18" />
    <line class="tk-dial__guide" x1="8" y1="50" x2="92" y2="50" />
    <line class="tk-dial__guide" x1="50" y1="8" x2="50" y2="92" />
    <circle class="tk-dial__bubble" cx="${px}" cy="${py}" r="9" />
  </svg>`;
};

const stat = (label, value) => html`<div class="tk-stat"><span class="tk-stat__label">${label}</span><span class="tk-stat__value">${value}</span></div>`;

const MORSE = { S: "...", O: "---", E: ".", T: "-", A: ".-", N: "-.", I: "..", M: "--", H: "....", R: ".-.", L: ".-..", D: "-..", U: "..-", W: ".--", K: "-.-", G: "--.", C: "-.-.", B: "-...", F: "..-.", P: ".--.", J: ".---", Q: "--.-", V: "...-", X: "-..-", Y: "-.--", Z: "--..", " ": "/" };
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
      const roll = Number(state.levelRoll ?? 0.4);
      const pitch = Number(state.levelPitch ?? -0.2);
      const flat = Math.abs(roll) < 1 && Math.abs(pitch) < 1;
      return html`
        <div class="tk-tool-interactive tk-tool-interactive--center">
          ${levelDial(roll, pitch, 184)}
          <div class="tk-readout"><span class="tk-readout__value">${flat ? "Level" : "Tilted"}</span><span class="tk-readout__note">From the gravity sensor</span></div>
          <div class="tk-stat-grid">${stat("Roll", `${roll.toFixed(1)}°`)} ${stat("Pitch", `${pitch.toFixed(1)}°`)}</div>
        </div>`;
    }

    case "ruler": {
      const unit = state.rulerUnit || "mm";
      const length = unit === "mm" ? "150 mm" : "5.91 in";
      return html`
        <div class="tk-tool-interactive">
          <div class="tk-ruler tk-ruler--${unit}" role="img" aria-label="On-screen ruler, ${length}"></div>
          <div class="tk-readout"><span class="tk-readout__value">${length}</span><span class="tk-readout__note">Scaled to this screen's pixel density</span></div>
          <md-chip-set aria-label="Unit">
            ${[["mm", "Millimeters"], ["in", "Inches"]].map(([id, label]) => html`<md-filter-chip label=${label} ?selected=${unit === id} @click=${() => onState({ rulerUnit: id })}></md-filter-chip>`)}
          </md-chip-set>
        </div>`;
    }

    case "protractor": {
      const angle = Number(state.protractorAngle ?? 45);
      const rad = (angle * Math.PI) / 180;
      const x = 50 + 40 * Math.cos(Math.PI - rad);
      const y = 56 - 40 * Math.sin(rad);
      return html`
        <div class="tk-tool-interactive tk-tool-interactive--center">
          <svg class="tk-protractor" viewBox="0 0 100 62" width="248" role="img" aria-label="Protractor, ${angle} degrees">
            <path class="tk-dial__ring" d="M10 56 A40 40 0 0 1 90 56 Z" />
            <line class="tk-dial__guide" x1="50" y1="56" x2="50" y2="20" />
            <line class="tk-protractor__arm" x1="50" y1="56" x2="${x.toFixed(2)}" y2="${y.toFixed(2)}" />
            <circle class="tk-dial__hub" cx="50" cy="56" r="3" />
          </svg>
          <div class="tk-readout"><span class="tk-readout__value">${angle}°</span><span class="tk-readout__note">Drag to measure an angle</span></div>
          <md-slider min="0" max="180" .value=${angle} labeled aria-label="Angle" @input=${(e) => onState({ protractorAngle: Number(e.target.value) })}></md-slider>
        </div>`;
    }

    case "flash_dimmer": {
      const level = Number(state.flashLevel ?? 3);
      const on = Boolean(state.flashOn);
      return html`
        <div class="tk-tool-interactive">
          <div class="tk-toggle-row">
            <div><div class="title-medium">Flashlight</div><div class="body-medium tk-muted">${on ? `On at level ${level} of 5` : "Off"}</div></div>
            <md-filled-tonal-button @click=${() => onState({ flashOn: !on })}><md-icon slot="icon">${on ? "flashlight_off" : "flashlight_on"}</md-icon>${on ? "Turn off" : "Turn on"}</md-filled-tonal-button>
          </div>
          <div class="tk-field">
            <span class="tk-field__label">Brightness</span>
            <md-slider min="1" max="5" step="1" ticks .value=${level} labeled aria-label="Flashlight level" @input=${(e) => onState({ flashLevel: Number(e.target.value) })}></md-slider>
          </div>
          <p class="body-small tk-muted" style="margin:0">Uses the camera torch. Devices without adjustable torch strength switch it on at full brightness.</p>
        </div>`;
    }

    case "breathing_box": {
      const running = Boolean(state.breathRunning);
      return html`
        <div class="tk-tool-interactive tk-tool-interactive--center">
          <div class="tk-breath ${running ? "is-running" : ""}" role="img" aria-label="Breathing pacer"><span class="tk-breath__phase"></span></div>
          <div class="tk-readout"><span class="tk-readout__value">4 · 4 · 4 · 4</span><span class="tk-readout__note">Inhale, hold, exhale, hold, four seconds each</span></div>
          <md-filled-button @click=${() => onState({ breathRunning: !running })}>
            <md-icon slot="icon">${running ? "pause" : "play_arrow"}</md-icon>${running ? "Pause" : "Start"}
          </md-filled-button>
        </div>`;
    }

    case "morse_sos": {
      const text = state.morseText ?? "SOS";
      const sos = Boolean(state.sosActive);
      return html`
        <div class="tk-tool-interactive">
          <md-outlined-text-field label="Message" style="width:100%" .value=${text} @input=${(e) => onState({ morseText: e.target.value })}></md-outlined-text-field>
          <div class="tk-morse" aria-live="polite">${toMorse(text) || "Type a message"}</div>
          <div class="tk-action-row">
            <md-filled-tonal-button @click=${() => onState({ sosActive: false })}><md-icon slot="icon">flash_on</md-icon>Flash message</md-filled-tonal-button>
            <md-filled-button class="tk-sos" @click=${() => onState({ sosActive: !sos })}><md-icon slot="icon">emergency</md-icon>${sos ? "Stop SOS" : "SOS"}</md-filled-button>
          </div>
          <p class="body-small tk-muted" style="margin:0">Blinks the camera torch. Keep it out of anyone's eyes.</p>
        </div>`;
    }

    case "coin_flip": {
      const heads = (state.coinSide ?? "Heads") === "Heads";
      return html`
        <div class="tk-tool-interactive tk-tool-interactive--center">
          <div class="tk-coin ${heads ? "" : "tk-coin--tails"}" role="img" aria-label=${heads ? "Heads" : "Tails"}>
            <md-icon>${heads ? "person" : "token"}</md-icon>
          </div>
          <div class="tk-readout"><span class="tk-readout__value">${heads ? "Heads" : "Tails"}</span></div>
          <md-filled-button @click=${() => onState({ coinSide: Math.random() < 0.5 ? "Heads" : "Tails" })}><md-icon slot="icon">play_arrow</md-icon>Flip coin</md-filled-button>
        </div>`;
    }

    case "reaction_test": {
      // ReactionTestTool.kt: one pad whose colour and prompt follow the phase.
      const phase = state.reactionPhase ?? "idle";
      const last = Number(state.reactionLast ?? 231);
      const rating = last < 200 ? "Lightning ⚡" : last <= 250 ? "Fast 🚀" : last <= 350 ? "Average 👍" : "Relaxed ☕";
      const prompt = {
        idle: html`<span class="t-title-medium">Tap Start when ready</span>`,
        waiting: html`<span class="t-title-large" style="font-weight:700">Wait for green…</span>`,
        signal: html`<span class="t-headline-medium" style="font-weight:800">TAP NOW!</span>`,
        early: html`<span class="t-headline-small" style="font-weight:700">Too early!</span>`,
        result: html`<span class="t-headline-large" style="font-weight:700">${last} ms</span><span class="tk-reaction__rating">${rating}</span>`,
      }[phase];
      const running = phase === "waiting" || phase === "signal";
      const next = { waiting: "signal", signal: "result" }[phase];
      return html`
        <div class="tk-tool-interactive">
          <div class="tk-reaction tk-reaction--${phase}" role="button" tabindex="0" aria-live="polite"
            @click=${() => next && onState({ reactionPhase: next })}>${prompt}</div>
          <div class="tk-action-row">
            <md-filled-button ?disabled=${running} @click=${() => onState({ reactionPhase: "waiting" })}><md-icon slot="icon">play_arrow</md-icon>${phase === "idle" ? "Start" : running ? "In progress…" : "Try again"}</md-filled-button>
            ${state.reactionBest ? html`<md-outlined-button @click=${() => onState({ reactionPhase: "idle", reactionBest: 0, reactionAverage: 0 })}><md-icon slot="icon">refresh</md-icon>Reset</md-outlined-button>` : nothing}
          </div>
          ${state.reactionBest ? html`<div class="tk-stat-grid">${stat("Best", `${state.reactionBest} ms`)} ${stat("Average", `${state.reactionAverage} ms`)}</div>` : nothing}
        </div>`;
    }

    default:
      return null;
  }
}
