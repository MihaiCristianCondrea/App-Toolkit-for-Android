import React from "react";
import { LitScreen } from "../LitScreen";
import { Shape } from "../Shape";
import { shapeUrl } from "../../../../../../ui/src/kit/assets.js";
import { FRAME, measure } from "./geometry";
import { scatterConfetti } from "./confetti";
import type { Confetto } from "./confetti";
import { clamp, enter, mix, ramp, pop, standard } from "../lib/anim";
import { FONT } from "../theme";

// ---------------------------------------------------------------------------------------------------
// The phone: the real kit screen in Material-Next's 3D Pixel, placed by its frame's centre.

export type PhonePose = { x: number; y: number; scale: number; roll?: number; yaw?: number; pitch?: number };
export type Screen = [string, Record<string, unknown>];

export const Phone: React.FC<{ screen: Screen; pose: PhonePose; scroll?: number; dx?: number; dy?: number; grow?: number; opacity?: number; z?: number }> = ({ screen, pose, scroll, dx = 0, dy = 0, grow = 1, opacity = 1, z = 2 }) => (
  <div style={{ position: "absolute", left: pose.x - FRAME.width / 2, top: pose.y - FRAME.height / 2, width: FRAME.width, height: FRAME.height, zIndex: z, opacity, transformOrigin: "50% 50%", transform: `translate(${dx}px, ${dy}px) rotate(${pose.roll ?? 0}deg) scale(${pose.scale * grow})`, filter: "drop-shadow(0 4px 6px rgba(30,24,16,.10)) drop-shadow(0 30px 40px rgba(30,24,16,.16))" }}>
    <LitScreen id={screen[0]} state={screen[1]} pose={{ yaw: pose.yaw ?? 0, pitch: pose.pitch ?? 0, perspective: 3000 }} style={{ width: FRAME.width }} scroll={scroll} />
  </div>
);

/** Where the phone's speaker holes are, and which way sound leaves them, in this pose. */
export const speakers = (pose: PhonePose) =>
  measure({ pitch: pose.pitch ?? 0, yaw: pose.yaw ?? 0, roll: pose.roll ?? 0, perspective: 3000, scale: pose.scale, x: pose.x, y: pose.y }, { critical: {}, secondary: {} }).speakers;

// ---------------------------------------------------------------------------------------------------
// Type: lines rise out of a mask, one per beat.

export const Headline: React.FC<{ t: number; at: number[]; title: string; sub?: string; x: number; y: number; color: string; subColor?: string; size?: number; align?: "left" | "center"; width?: number }> = ({ t, at, title, sub, x, y, color, subColor, size = 104, align = "left", width = 900 }) => {
  const line = (text: string, start: number, fontSize: number, weight: number, tone: string, spacing: number) => {
    const p = ramp(t, start, 0.55);
    return (
      <div style={{ overflow: "hidden", paddingBottom: fontSize * 0.12 }}>
        <div style={{ transform: `translateY(${(1 - p) * 110}%)`, opacity: clamp(p * 1.6), fontSize, fontWeight: weight, lineHeight: 1.04, letterSpacing: spacing, color: tone, whiteSpace: "pre-line" }}>{text}</div>
      </div>
    );
  };
  return (
    <div style={{ position: "absolute", left: align === "center" ? x - width / 2 : x, top: y, width, textAlign: align, fontFamily: FONT, zIndex: 6 }}>
      {line(title, at[0], size, 650, color, -size * 0.035)}
      {sub && <div style={{ marginTop: size * 0.14 }}>{line(sub, at[1] ?? at[0] + 0.3, size * 0.4, 450, subColor ?? color, -0.4)}</div>}
    </div>
  );
};

// ---------------------------------------------------------------------------------------------------
// Water: on every note a sound wave leaves both groups of speaker holes. Material's expressive style, not
// hairlines: each wave is a thick rounded band in one of the world's tones that thins as it travels, and the
// bubbles it pushes are flat tonal circles with one highlight, which drift up, wobble and pop.

/** Material elevation, used by every lifted surface in the film (phones, receipt, tiles). */
export const ELEVATION = {
  2: "0 1px 3px rgba(30,24,16,.10), 0 10px 24px rgba(30,24,16,.10)",
  3: "0 2px 6px rgba(30,24,16,.10), 0 22px 44px rgba(30,24,16,.14)",
};

const arcPath = (at: [number, number], d: [number, number], r: number, spread: number) => {
  const n = [-d[1], d[0]];
  const pts = Array.from({ length: 41 }, (_, i) => {
    const a = ((-spread + (2 * spread * i) / 40) * Math.PI) / 180;
    return [at[0] + r * (d[0] * Math.cos(a) + n[0] * Math.sin(a)), at[1] + r * (d[1] * Math.cos(a) + n[1] * Math.sin(a))];
  });
  return `M ${pts.map((q) => q.map((v) => v.toFixed(1)).join(" ")).join(" L ")}`;
};

export const Bubble: React.FC<{ x: number; y: number; r: number; fill: string; rim: string; opacity?: number }> = ({ x, y, r, fill, rim, opacity = 1 }) => (
  <g opacity={opacity}>
    <circle cx={x} cy={y} r={r} fill={fill} />
    <circle cx={x} cy={y} r={r - Math.max(2, r * 0.12) / 2} fill="none" stroke={rim} strokeWidth={Math.max(2, r * 0.12)} opacity=".55" />
    <circle cx={x - r * 0.34} cy={y - r * 0.34} r={r * 0.2} fill="#FFFFFF" />
  </g>
);

export const SoundWaves: React.FC<{ t: number; onsets: number[]; from: { at: [number, number]; direction: [number, number] }[]; tones: string[]; rim: string; bubble: string; reach?: number; spread?: number }> = ({ t, onsets, from, tones, rim, bubble, reach = 420, spread = 58 }) => (
  <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, pointerEvents: "none", overflow: "visible" }}>
    {onsets.flatMap((o, k) => {
      if (t < o || t > o + 2.2) return [];
      const p = enter(clamp((t - o) / 1.3)), r = 24 + p * reach, fade = 1 - clamp((t - o - 0.55) / 0.75), tone = tones[k % tones.length];
      return from.flatMap(({ at, direction: d }, s) => {
        const n = [-d[1], d[0]];
        // Two bubbles per side ride the front out, then rise on their own and pop.
        const bubbles = [-22, 26].map((deg, b) => {
          const a = ((deg + (s ? -10 : 10) + ((k * 23) % 14)) * Math.PI) / 180, rise = clamp((t - o - 0.35) / 1.6), rr = r * 0.92 + b * 14;
          const out = 46 + enter(clamp((t - o) / 0.9)) * reach * 0.85 + b * 18;
          const x = at[0] + out * (d[0] * Math.cos(a) + n[0] * Math.sin(a)) + Math.sin((t - o) * 5 + k + b) * 8 * rise;
          const y = at[1] + out * (d[1] * Math.cos(a) + n[1] * Math.sin(a)) - rise * rise * 220;
          const popAt = 1.55 + b * 0.2, popP = clamp((t - o - popAt) / 0.18), size = (b ? 16 : 24) * (0.5 + 0.5 * pop(t, o + 0.05, 10, 200)) * (1 + popP * 0.5);
          return { x, y, size, opacity: 1 - popP, key: `${rr}` };
        });
        return [
          fade > 0 && <path key={`w${k}-${s}`} d={arcPath(at, d, r, spread)} fill="none" stroke={tone} strokeWidth={mix(34, 10, p)} strokeLinecap="round" opacity={fade} />,
          ...bubbles.map((q, b) => q.opacity > 0 && <Bubble key={`b${k}-${s}-${b}`} x={q.x} y={q.y} r={q.size} fill={bubble} rim={rim} opacity={q.opacity} />),
        ];
      });
    })}
  </svg>
);

/**
 * The worlds gathering: a Material ripple per onset, a tonal disc in a world's container colour that grows out
 * of the icon and fades as it spreads, the way a pressed surface's state layer does.
 */
export const Ripples: React.FC<{ t: number; onsets: number[]; at: [number, number]; colors: string[]; from: number; reach: number; z?: number }> = ({ t, onsets, at, colors, from, reach, z = 2 }) => (
  <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: z, pointerEvents: "none" }}>
    {onsets.map((o, k) => {
      if (t < o) return null;
      const p = enter(clamp((t - o) / 1.1)), fade = 1 - standard(clamp((t - o - 0.15) / 0.95));
      return fade > 0 && <circle key={k} cx={at[0]} cy={at[1]} r={from + p * reach} fill={colors[k % colors.length]} opacity={0.9 * fade} />;
    })}
  </svg>
);

// ---------------------------------------------------------------------------------------------------
// Ruler and Protractor: the listing's artwork, with a slide and a live arm.

export const Ruler: React.FC<{ x: number; y: number; width: number; angle: number; ticks?: number }> = ({ x, y, width, angle, ticks = 1 }) => {
  const n = Math.floor((width - 48) / 12), shown = Math.floor(n * clamp(ticks));
  return (
    <g transform={`translate(${x} ${y}) rotate(${angle})`}>
      <rect x="0" y="10" width={width} height="128" rx="34" fill="#B56D00" opacity=".16" />
      <rect width={width} height="128" rx="34" fill="#F6B91D" />
      <path d={`M 30 98 H ${width - 30}`} stroke="#8F5700" opacity=".25" />
      {Array.from({ length: shown }, (_, i) => <line key={i} x1={28 + i * 12} x2={28 + i * 12} y1="0" y2={i % 10 === 0 ? 64 : i % 5 === 0 ? 44 : 24} stroke="#654000" strokeWidth={i % 5 ? 1.6 : 2.8} />)}
    </g>
  );
};

export const Protractor: React.FC<{ x: number; y: number; r: number; tilt?: number; arm: number }> = ({ x, y, r, tilt = 0, arm }) => {
  const a = (-arm * Math.PI) / 180;
  return (
    <g transform={`translate(${x} ${y}) rotate(${tilt})`}>
      <path d={`M ${-r} 0 A ${r} ${r} 0 0 1 ${r} 0 Z`} transform="translate(0 10)" fill="#8F5700" opacity=".12" />
      <path d={`M ${-r} 0 A ${r} ${r} 0 0 1 ${r} 0 Z`} fill="#FDE7A2" />
      <path d={`M ${-r * 0.65} 0 A ${r * 0.65} ${r * 0.65} 0 0 1 ${r * 0.65} 0`} stroke="#C48B24" strokeWidth="2" fill="none" />
      {Array.from({ length: 37 }, (_, i) => { const t = Math.PI + (i * Math.PI) / 36, o = r - 14, n = o - (i % 3 ? 12 : 28); return <line key={i} x1={Math.cos(t) * o} y1={Math.sin(t) * o} x2={Math.cos(t) * n} y2={Math.sin(t) * n} stroke="#8F5700" strokeWidth={i % 3 ? 1.6 : 2.8} />; })}
      <path d={`M ${r * 0.8} 0 H 0 L ${r * 0.8 * Math.cos(a)} ${r * 0.8 * Math.sin(a)}`} fill="none" stroke="#8F5700" strokeWidth="6" strokeLinecap="round" strokeLinejoin="round" />
      <path d={`M ${r * 0.3} 0 A ${r * 0.3} ${r * 0.3} 0 0 0 ${r * 0.3 * Math.cos(a)} ${r * 0.3 * Math.sin(a)}`} fill="none" stroke="#F6A800" strokeWidth="5" />
      <circle r="11" fill="#8F5700" />
      <text x={r * 0.36 * Math.cos(a / 2)} y={r * 0.36 * Math.sin(a / 2) - 12} fill="#8F5700" fontFamily={FONT} fontWeight={650} fontSize={r * 0.16}>{Math.round(arm)}°</text>
    </g>
  );
};

// ---------------------------------------------------------------------------------------------------
// Unit Converter: two units on MaterialShapes with swap arrows between them.

export const UnitPair: React.FC<{ a: { x: number; y: number; label: string; value: string; color: string; shape: string }; b: { x: number; y: number; label: string; value: string; color: string; shape: string }; ink: string; arrows: string; turn: number; size?: number }> = ({ a, b, ink, arrows, turn, size = 300 }) => {
  const unit = (u: typeof a, k: string) => (
    <div key={k} style={{ position: "absolute", left: u.x - size / 2, top: u.y - size / 2, width: size, height: size, zIndex: 4 }}>
      <Shape name={u.shape} x={0} y={0} size={size} color={u.color} rotate={12} />
      <div style={{ position: "absolute", inset: 0, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", fontFamily: FONT, color: ink }}>
        <div style={{ fontSize: size * 0.26, fontWeight: 650, letterSpacing: -2, lineHeight: 1 }}>{u.value}</div>
        <div style={{ fontSize: size * 0.15, fontWeight: 560, marginTop: 8 }}>{u.label}</div>
      </div>
    </div>
  );
  const cx = (a.x + b.x) / 2, cy = (a.y + b.y) / 2;
  return (
    <>
      {unit(a, "a")}
      {unit(b, "b")}
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 3 }}>
        <g transform={`translate(${cx} ${cy}) rotate(${turn})`} fill="none" stroke={arrows} strokeWidth="14" strokeLinecap="round" strokeLinejoin="round">
          <path d="M -110 -26 C -60 -78 60 -78 110 -26" /><path d="M 110 -26 l -40 -6 M 110 -26 l 4 -40" />
          <path d="M 110 26 C 60 78 -60 78 -110 26" /><path d="M -110 26 l 40 6 M -110 26 l -4 40" />
        </g>
      </svg>
    </>
  );
};

// ---------------------------------------------------------------------------------------------------
// Tip & Bill Splitter: the listing's receipt, printing line by line.

export const Receipt: React.FC<{ x: number; y: number; angle: number; t: number; lines: number[]; ink: string; accent: string; scale?: number }> = ({ x, y, angle, t, lines, ink, accent, scale = 1.35 }) => {
  const show = (i: number) => { const p = ramp(t, lines[i], 0.45); return { opacity: p, transform: `translateY(${(1 - p) * 18}px)` }; };
  const big = pop(t, lines[3], 9, 200);
  return (
    <div style={{ position: "absolute", left: x, top: y, width: 292, height: 372, transform: `rotate(${angle}deg) scale(${scale})`, transformOrigin: "50% 50%", borderRadius: 28, background: "#FFFBF5", boxShadow: ELEVATION[3], padding: "28px 30px", boxSizing: "border-box", color: ink, fontFamily: FONT, zIndex: 4 }}>
      <div style={{ fontSize: 20, fontWeight: 550 }}>Dinner for two</div>
      <div style={{ ...show(0), display: "flex", justifyContent: "space-between", fontSize: 22, marginTop: 24 }}><span>Bill</span><b>€52.00</b></div>
      <div style={{ ...show(1), display: "flex", justifyContent: "space-between", fontSize: 22, marginTop: 14 }}><span>Tip 10%</span><b>€5.20</b></div>
      <div style={{ ...show(2), borderTop: "3px dotted #D9A08C", marginTop: 22, paddingTop: 16, fontSize: 17 }}>Per person</div>
      <div style={{ opacity: clamp(big * 2), transform: `scale(${0.6 + 0.4 * big})`, transformOrigin: "0 50%", fontSize: 54, fontWeight: 650, letterSpacing: -2, marginTop: 3 }}>€28.60</div>
      <div style={{ ...show(4), fontSize: 17, marginTop: 9, color: accent }}>Total €57.20</div>
    </div>
  );
};

// ---------------------------------------------------------------------------------------------------
// Decision Wheel: the listing's drawn wheel, turning.

export const Wheel: React.FC<{ cx: number; cy: number; r: number; rotation: number; colors: string[]; ground: string; accent: string }> = ({ cx, cy, r, rotation, colors, ground, accent }) => (
  <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 3 }}>
    <g transform={`translate(${cx} ${cy}) rotate(${rotation})`}>
      {Array.from({ length: 6 }, (_, i) => {
        const a = ((i * 60 - 90) * Math.PI) / 180, b = (((i + 1) * 60 - 90) * Math.PI) / 180;
        return <path key={i} d={`M 0 0 L ${r * Math.cos(a)} ${r * Math.sin(a)} A ${r} ${r} 0 0 1 ${r * Math.cos(b)} ${r * Math.sin(b)} Z`} fill={colors[i % colors.length]} stroke={ground} strokeWidth="6" />;
      })}
      <circle r={r * 0.14} fill={ground} />
      <circle r={r * 0.045} fill={accent} />
    </g>
    <path d={`M ${cx - 30} ${cy - r - 26} h 60 l -30 56 Z`} fill={accent} stroke={accent} strokeWidth="14" strokeLinejoin="round" />
  </svg>
);

// ---------------------------------------------------------------------------------------------------
// Confetti: the Decision Wheel listing's blue-noise scatter, thrown out from a point and settling.

export const burstPieces = (o: { seed: number; count: number; from: [number, number]; colors: string[]; avoid?: { x: number; y: number; r: number }[] }): Confetto[] =>
  scatterConfetti({
    width: 1920, height: 1080, seed: o.seed, count: o.count, margin: 30, colors: o.colors,
    focus: { left: o.from[0] - 80, top: o.from[1] - 80, right: o.from[0] + 80, bottom: o.from[1] + 80 },
    free: (x, y, r) => (o.avoid ?? []).every((a) => Math.hypot(a.x - x, a.y - y) > a.r + r * 1.6),
  }).map((q) => ({ ...q, w: q.w * 1.6, h: q.h * 1.6 }));

export const ConfettiBurst: React.FC<{ t: number; at: number; from: [number, number]; pieces: Confetto[]; z?: number }> = ({ t, at, from, pieces, z = 7 }) => {
  if (t < at) return null;
  return (
    <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: z, pointerEvents: "none" }}>
      {pieces.map((q, i) => {
        // Each piece leaves on its own spring, then flutters: a slow fall, a sway and a turn that never stops.
        const delay = (i % 7) * 0.012, p = enter(clamp((t - at - delay) / 0.95)), after = Math.max(0, t - at - 0.7);
        const x = mix(from[0], q.x, p) + Math.sin(after * 2.4 + i) * 10 * clamp(after), y = mix(from[1], q.y, p) + after * 22;
        const flip = Math.cos(after * (2 + (i % 5) * 0.4) + i), rot = q.rotate + (1 - p) * 420 + after * 50 * ((i % 2) * 2 - 1);
        const k = 0.35 + 0.65 * p;
        return q.kind === "dot"
          ? <circle key={i} cx={x} cy={y} r={(q.w / 2) * k} fill={q.color} />
          : <rect key={i} x={x - (q.w * k) / 2} y={y - (q.h * k * (0.45 + 0.55 * Math.abs(flip))) / 2} width={q.w * k} height={q.h * k * (0.45 + 0.55 * Math.abs(flip))} rx={(Math.min(q.w, q.h) * k) / 2} fill={q.color} transform={`rotate(${rot.toFixed(1)} ${x.toFixed(1)} ${y.toFixed(1)})`} />;
      })}
    </svg>
  );
};

// ---------------------------------------------------------------------------------------------------
// The hand-off: the next world grows out of a MaterialShape until it fills the frame.

export const ShapeWipe: React.FC<{ t: number; start: number; dur: number; at: [number, number]; shape: string; children: React.ReactNode }> = ({ t, start, dur, at, shape, children }) => {
  const p = clamp((t - start) / dur);
  if (p >= 1) return <div style={{ position: "absolute", inset: 0 }}>{children}</div>;
  const size = 20 + enter(p) * 5200, mask = `url("${shapeUrl(shape)}") ${at[0] - size / 2}px ${at[1] - size / 2}px / ${size}px ${size}px no-repeat`;
  return <div style={{ position: "absolute", inset: 0, WebkitMask: mask, mask }}>{children}</div>;
};
