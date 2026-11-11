import React from "react";
import { AbsoluteFill } from "remotion";
import { LitScreen } from "./LitScreen";
import { SCREENS, SCROLL } from "./screens";
import { STRINGS } from "./strings";
import { Glass } from "./Glass";
import { Confetti } from "./Confetti";
import { Shape } from "./Shape";
import ui from "./composition/ui.json";
import type { Pt, ScreenUi } from "./composition/geometry";
import { CANVAS, WIDE_GOLDEN, WIDE_SETS, composeWide } from "./composition/wide";
import type { Kind } from "./composition/wide";

export { CANVAS };

/**
 * The tablet or desktop strip: the glass ground across every screenshot, and in each one its device at true
 * size, centred, with MaterialShapes placed around it (src/composition/wide.ts). No captions, as Play asks
 * of large-screen screenshots. `only` draws one screenshot's foreground; `debug` overlays its measurements.
 */
export const WideStrip: React.FC<{ locale: string; kind: Kind; only?: number; debug?: boolean }> = ({ locale, kind, only, debug }) => {
  const set = WIDE_SETS[kind], list = STRINGS[locale][set.key], screens = list.map((s) => s.id);
  return (
    <AbsoluteFill style={{ width: list.length * CANVAS.w, overflow: "hidden" }}>
      <Glass id="wide" width={list.length * CANVAS.w} height={CANVAS.h} panels={list.length} />
      {list.map((s, i) => {
        if (only !== undefined && only !== i) return null;
        const g = composeWide(kind, i, screens, ui as Record<string, ScreenUi>);
        if (g.report.hard.length && !debug) throw new Error(`${kind} screenshot ${i + 1} (${s.id}) breaks its layout rules: ${g.report.hard.join("; ")}`);
        return (
          <div key={s.id} style={{ position: "absolute", left: i * CANVAS.w, top: 0, width: CANVAS.w, height: CANVAS.h }}>
            {g.shapes.map((p, k) => <Shape key={k} name={p.name} x={p.x} y={p.y} size={p.size} color={p.color} rotate={p.rotate} />)}
            <Confetti pieces={g.confetti} width={CANVAS.w} height={CANVAS.h} />
            <div style={{ position: "absolute", left: g.box.left, top: g.box.top, zIndex: 1 }}>
              <LitScreen id={SCREENS[s.id][0]} state={SCREENS[s.id][1]} window={set.window} scroll={SCROLL[s.id]} />
            </div>
            {debug && <WideOverlay g={g} />}
          </div>
        );
      })}
    </AbsoluteFill>
  );
};

const poly = (pts: Pt[]) => pts.map((p) => p.map((n) => n.toFixed(1)).join(",")).join(" ");

/** Golden guides, the device box and its centre, critical UI (solid) and secondary UI (dashed), shapes, the visual centroid and the report. */
const WideOverlay: React.FC<{ g: ReturnType<typeof composeWide> }> = ({ g }) => {
  const label = (x: number, y: number, text: string, fill: string) => <text x={x} y={y} fill={fill} fontSize="20" fontFamily="monospace" stroke="#fff" strokeWidth="5" paintOrder="stroke">{text}</text>;
  const lines = [...g.report.hard.map((t) => ["#D50000", `HARD ${t}`]), ...g.report.preferences.map((t) => ["#E65100", `PREF ${t}`]), ...g.report.intentional.map((t) => ["#2E7D32", `OK ${t}`])];
  const b = g.report.balance;
  return (
    <svg width={CANVAS.w} height={CANVAS.h} style={{ position: "absolute", left: 0, top: 0, zIndex: 40, overflow: "visible" }}>
      {WIDE_GOLDEN.x.map((x) => <line key={x} x1={x} y1={0} x2={x} y2={CANVAS.h} stroke="#E6A100" strokeWidth="2" strokeDasharray="12 8" />)}
      {WIDE_GOLDEN.y.map((y) => <line key={y} x1={0} y1={y} x2={CANVAS.w} y2={y} stroke="#E6A100" strokeWidth="2" strokeDasharray="12 8" />)}
      <line x1={CANVAS.w / 2} y1={0} x2={CANVAS.w / 2} y2={CANVAS.h} stroke="#555" strokeWidth="1.5" />
      <polygon points={poly(g.device)} fill="none" stroke="#C2185B" strokeWidth="3" />
      {Object.entries(g.critical).map(([k, q]) => <polygon key={k} points={poly(q)} fill="#1565C0" fillOpacity=".1" stroke="#1565C0" strokeWidth="2" />)}
      {Object.entries(g.secondary).map(([k, q]) => <polygon key={k} points={poly(q)} fill="none" stroke="#78909C" strokeWidth="2" strokeDasharray="6 5" />)}
      {g.shapes.map((p, i) => <circle key={i} cx={p.x + p.size / 2} cy={p.y + p.size / 2} r={p.size / 2} fill="none" stroke="#00897B" strokeWidth="2" />)}
      <circle cx={b[0]} cy={b[1]} r="16" fill="none" stroke="#000" strokeWidth="3" />
      <circle cx={b[0]} cy={b[1]} r="4" fill="#000" />
      {label(b[0] + 22, b[1] + 30, `visual centroid ${(b[0] / CANVAS.w).toFixed(3)} W, ${(b[1] / CANVAS.h).toFixed(3)} H`, "#000")}
      {label(g.box.left + 8, g.box.top - 10, `${g.screen} · device ${(g.box.right - g.box.left).toFixed(0)} x ${(g.box.bottom - g.box.top).toFixed(0)}, centred`, "#C2185B")}
      {lines.map(([fill, t], i) => label(21, CANVAS.h - 21 - (lines.length - 1 - i) * 26, t, fill))}
    </svg>
  );
};

export const WideShot: React.FC<{ locale: string; kind: Kind; index: number; debug?: boolean }> = ({ locale, kind, index, debug }) => (
  <AbsoluteFill style={{ overflow: "hidden" }}>
    <div style={{ position: "absolute", left: -index * CANVAS.w, top: 0, height: CANVAS.h }}>
      <WideStrip locale={locale} kind={kind} only={index} debug={debug} />
    </div>
  </AbsoluteFill>
);
