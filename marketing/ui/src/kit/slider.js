import { html, nothing } from "lit";

// Compose Material 3 Slider and RangeSlider (SliderDefaults): a 16 dp track, primary up to the value
// and secondary container after it, a 4 x 44 dp handle with a 6 dp gap on each side, and a 4 dp stop
// indicator at each inactive end. Native range inputs carry the keyboard and the accessible value;
// the pointer is handled on the whole track, so every handle has a 44 dp target.

const clamp = (v, min, max) => Math.min(max, Math.max(min, v));
const snap = (v, min, max, step) => (step === "any" ? v : clamp(min + Math.round((v - min) / step) * step, min, max));
const fraction = (v, min, max) => (max > min ? clamp((v - min) / (max - min), 0, 1) : 0);

/** The value under the pointer, from the track's own box. */
const valueAt = (e, min, max, step) => {
  const r = e.currentTarget.getBoundingClientRect();
  const f = clamp((e.clientX - r.left - 2) / Math.max(1, r.width - 4), 0, 1);
  return snap(min + f * (max - min), min, max, step);
};

const track = (handles) => html`<span class="tk-slider__track" aria-hidden="true">
    ${handles.length > 1 ? html`<span class="tk-slider__lo"></span>` : nothing}
    ${handles.map((f, i) => html`<span class="tk-slider__handle" style="--f:${f}" data-i=${i}></span>`)}
  </span>`;

/**
 * One value. `onInput(value)` while it changes; without it the slider is drawn as given (captures).
 */
export const slider = ({ value, min = 0, max = 1, step = "any", label, onInput, className = "" }) => {
  const f = fraction(value, min, max);
  const drag = onInput && ((e) => {
    if (e.type === "pointerdown") e.currentTarget.setPointerCapture(e.pointerId);
    else if (!e.currentTarget.hasPointerCapture(e.pointerId)) return;
    onInput(valueAt(e, min, max, step));
  });
  return html`<div class="tk-slider ${className}" style="--start:0;--end:${f}" @pointerdown=${drag ?? nothing} @pointermove=${drag ?? nothing}>
    ${track([f])}
    <input class="tk-slider__input" type="range" min=${min} max=${max} step=${step} .value=${String(value)}
      aria-label=${label} ?disabled=${!onInput} @input=${(e) => onInput?.(Number(e.target.value))}>
  </div>`;
};

/** Two values. `onInput([start, end])` while either changes; the nearer handle follows the pointer. */
export const rangeSlider = ({ start, end, min = 0, max = 1, step = "any", labelStart, labelEnd, onInput, className = "" }) => {
  const fs = fraction(start, min, max);
  const fe = fraction(end, min, max);
  let moving = 0;
  const drag = onInput && ((e) => {
    const v = valueAt(e, min, max, step);
    if (e.type === "pointerdown") {
      e.currentTarget.setPointerCapture(e.pointerId);
      moving = Math.abs(v - start) <= Math.abs(v - end) ? 0 : 1;
      e.currentTarget.dataset.moving = moving;
    } else if (!e.currentTarget.hasPointerCapture(e.pointerId)) return;
    moving = Number(e.currentTarget.dataset.moving);
    onInput(moving === 0 ? [Math.min(v, end), end] : [start, Math.max(v, start)]);
  });
  return html`<div class="tk-slider tk-slider--range ${className}" style="--start:${fs};--end:${fe}" @pointerdown=${drag ?? nothing} @pointermove=${drag ?? nothing}>
    ${track([fs, fe])}
    <input class="tk-slider__input" type="range" min=${min} max=${max} step=${step} .value=${String(start)}
      aria-label=${labelStart} ?disabled=${!onInput} @input=${(e) => onInput?.([Math.min(Number(e.target.value), end), end])}>
    <input class="tk-slider__input" type="range" min=${min} max=${max} step=${step} .value=${String(end)}
      aria-label=${labelEnd} ?disabled=${!onInput} @input=${(e) => onInput?.([start, Math.max(Number(e.target.value), start)])}>
  </div>`;
};
