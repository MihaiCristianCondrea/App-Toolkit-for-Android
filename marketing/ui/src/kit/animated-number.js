import {html} from 'lit';
import {ref} from 'lit/directives/ref.js';
import {repeat} from 'lit/directives/repeat.js';

const values = new WeakMap();

/** Update real values immediately, rolling only the places that changed. No fake intermediate results. */
export function animatedNumber(value) {
  const text = String(value);
  const update = el => {
    if (!el) return;
    const previous = values.get(el);
    values.set(el, text);
    if (previous == null || previous === text || document.documentElement.dataset.motion === 'frame') return;
    requestAnimationFrame(() => {
      if (!el.isConnected || values.get(el) !== text) return;
      const reduced = matchMedia('(prefers-reduced-motion: reduce)').matches;
      for (const digit of el.querySelectorAll('[data-digit-place]')) {
        digit.getAnimations().forEach(animation => animation.cancel());
        const place = Number(digit.dataset.digitPlace);
        const before = previous.at(-place), after = text.at(-place);
        if (reduced || before === after || !/\d/.test(after)) continue;
        const increasing = !before || after > before || (before === '9' && after === '0');
        digit.animate([
          {transform: `translateY(${increasing ? '-100%' : '100%'})`, opacity: 0},
          {transform: 'translateY(0)', opacity: 1},
        ], {duration: 400, easing: 'cubic-bezier(.4,0,.2,1)'});
      }
    });
  };
  return html`<span aria-label=${text} dir="ltr" style="display:inline-flex;overflow:hidden;vertical-align:bottom;font-variant-numeric:tabular-nums" ${ref(update)}>${repeat([...text].map((char,index) => ({char,place:text.length-index})), item => item.place, item => html`<span data-digit-place=${item.place} style="display:inline-block">${item.char}</span>`)}</span>`;
}
