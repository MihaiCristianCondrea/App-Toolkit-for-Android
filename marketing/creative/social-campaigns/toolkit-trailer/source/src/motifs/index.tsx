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

export const Headline: React.FC<{ t: number; at: number[]; title: string; sub?: string; x: number; y: number; color: string; subColor?: string; size?: number; subScale?: number; align?: "left" | "center"; width?: number }> = ({ t, at, title, sub, x, y, color, subColor, size = 104, subScale = 0.4, align = "left", width = 900 }) => {
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
      {sub && <div style={{ marginTop: size * 0.14 }}>{line(sub, at[1] ?? at[0] + 0.3, size * subScale, 480, subColor ?? color, -0.4)}</div>}
    </div>
  );
};

// ---------------------------------------------------------------------------------------------------
// Water, as the Water Eject listing draws it: on every note a set of three thin arcs with a soft glow leaves
// the speaker edge, glass bubbles (a clear centre, a tinted rim, a highlight and a crescent) rise off the
// fronts, and on the strong notes droplets are thrown out of the grille and fall.

/** Material elevation, used by every lifted surface in the film (phones, receipt, tiles, chips). */
export const ELEVATION = {
  1: "0 1px 2px rgba(30,24,16,.10), 0 4px 10px rgba(30,24,16,.08)",
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

/** The listing's glass bubble; `id` names the shared gradient in the enclosing svg. */
export const Bubble: React.FC<{ x: number; y: number; r: number; rim: string; id?: string; opacity?: number }> = ({ x, y, r, rim, id = "tt-glass", opacity = 1 }) => (
  <g opacity={opacity}>
    <circle cx={x} cy={y} r={r} fill={`url(#${id})`} stroke={rim} strokeOpacity=".65" strokeWidth={Math.max(1.6, r * 0.09)} />
    <ellipse cx={x - r * 0.36} cy={y - r * 0.4} rx={r * 0.26} ry={r * 0.16} fill="#FFFFFF" transform={`rotate(-35 ${x - r * 0.36} ${y - r * 0.4})`} />
    <path d={`M ${x + r * 0.55} ${y + r * 0.2} A ${r * 0.7} ${r * 0.7} 0 0 1 ${x + r * 0.1} ${y + r * 0.62}`} fill="none" stroke="#FFFFFF" strokeWidth={Math.max(1.2, r * 0.08)} strokeOpacity=".85" strokeLinecap="round" />
  </g>
);
export const GlassDefs: React.FC<{ tint: string; id?: string }> = ({ tint, id = "tt-glass" }) => (
  <defs>
    <radialGradient id={id} cx=".5" cy=".5" r=".5">
      <stop offset=".62" stopColor="#FFFFFF" stopOpacity=".18" />
      <stop offset=".9" stopColor={tint} stopOpacity=".6" />
      <stop offset="1" stopColor="#FFFFFF" stopOpacity=".95" />
    </radialGradient>
    <filter id={`${id}-glow`} x="-30%" y="-30%" width="160%" height="160%"><feGaussianBlur stdDeviation="7" /></filter>
  </defs>
);

const hash = (n: number) => { const x = Math.sin(n * 127.1 + 311.7) * 43758.5453; return x - Math.floor(x); };

export const SoundWaves: React.FC<{ t: number; onsets: number[]; from: { at: [number, number]; direction: [number, number] }; line: string; glow: string; tint: string; reach?: number; spread?: number; drops?: number[] }> = ({ t, onsets, from, line, glow, tint, reach = 520, spread = 42, drops = [] }) => {
  const { at, direction: d } = from, n: [number, number] = [-d[1], d[0]];
  const along = (r: number, deg: number): [number, number] => { const a = (deg * Math.PI) / 180; return [at[0] + r * (d[0] * Math.cos(a) + n[0] * Math.sin(a)), at[1] + r * (d[1] * Math.cos(a) + n[1] * Math.sin(a))]; };
  return (
    <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, pointerEvents: "none", overflow: "visible" }}>
      <GlassDefs tint={tint} />
      {onsets.flatMap((o, k) => {
        if (t < o || t > o + 3) return [];
        const p = enter(clamp((t - o) / 1.4)), fade = 1 - clamp((t - o - 0.5) / 0.9);
        // Three rings per note, on the listing's spacing.
        const rings = fade > 0 ? [0, 34, 68].map((gap, i) => {
          const r = 30 + gap * (0.4 + 0.6 * p) + p * reach * 0.8, path = arcPath(at, d, r, spread - i * 4);
          return (
            <g key={`w${k}-${i}`} opacity={fade * (1 - i * 0.18)}>
              <path d={path} fill="none" stroke={glow} strokeWidth={16} strokeLinecap="round" opacity=".28" filter="url(#tt-glass-glow)" />
              <path d={path} fill="none" stroke={line} strokeWidth={Math.max(2.5, 5 - i)} strokeLinecap="round" opacity=".75" />
            </g>
          );
        }) : [];
        // Bubbles leave the front and rise on their own, wobbling, then pop.
        const bubbles = [0, 1, 2].map((b) => {
          const deg = (hash(k * 3 + b) * 2 - 1) * spread * 0.9, out = 40 + enter(clamp((t - o) / 1.1)) * reach * (0.35 + 0.45 * hash(k * 7 + b));
          const life = t - o, rise = clamp((life - 0.3) / 2.2), popAt = 1.7 + hash(k + b * 5) * 0.9, popP = clamp((life - popAt) / 0.16);
          if (popP >= 1) return null;
          const [x0, y0] = along(out, deg), r = (8 + hash(k * 11 + b) * 18) * (0.4 + 0.6 * pop(t, o + 0.04 * b, 11, 180)) * (1 + popP * 0.6);
          return <Bubble key={`b${k}-${b}`} x={x0 + Math.sin(life * 4.2 + k + b) * 10 * rise} y={y0 - rise * rise * 260} r={r} rim={glow} opacity={1 - popP} />;
        });
        return [...rings, ...bubbles];
      })}
      {drops.flatMap((o, k) => {
        if (t < o || t > o + 1.6) return [];
        return [0, 1, 2, 3, 4].map((j) => {
          const life = t - o, deg = (hash(k * 13 + j) * 2 - 1) * spread, v = 700 + hash(j * 3 + k) * 600;
          const [ux, uy] = along(1, deg).map((c, i) => c - at[i]) as [number, number];
          const x = at[0] + ux * v * life, y = at[1] + uy * v * life + 900 * life * life, size = 9 + hash(k + j * 17) * 9;
          const vx = ux * v, vy = uy * v + 1800 * life, angle = (Math.atan2(vy, vx) * 180) / Math.PI - 90, fade = 1 - clamp((life - 0.9) / 0.6);
          return <path key={`d${k}-${j}`} d={`M 0 ${-size * 1.5} C ${size * 0.55} ${-size * 0.4} ${size} 0 ${size} ${size * 0.35} A ${size} ${size} 0 0 1 ${-size} ${size * 0.35} C ${-size} 0 ${-size * 0.55} ${-size * 0.4} 0 ${-size * 1.5} Z`} fill={line} opacity={0.85 * fade} transform={`translate(${x.toFixed(1)} ${y.toFixed(1)}) rotate(${(angle + 180).toFixed(1)})`} />;
        });
      })}
    </svg>
  );
};

// ---------------------------------------------------------------------------------------------------
// Decoration, as every listing screenshot has it: small MaterialShapes in the world's tones scattered over
// the open ground, each popping in on its own spring, then turning and floating slowly.

const DECOR_SHAPES = ["sunny", "flower", "gem", "clover_4leaf", "soft_burst", "cookie_6sided", "very_sunny", "clover_8leaf", "pill", "cookie_4sided", "burst", "bun"];
export type Box = { left: number; top: number; right: number; bottom: number };
export const decorPieces = (seed: number, count: number, avoid: Box[], colors: string[]) => {
  let s = seed >>> 0 || 1;
  const rand = () => ((s = (Math.imul(s, 1664525) + 1013904223) >>> 0) / 4294967296);
  const out: { x: number; y: number; size: number; name: string; color: string; spin: number; delay: number }[] = [];
  for (let tries = 0; out.length < count && tries < count * 60; tries++) {
    const big = out.length < 3, size = big ? 110 + rand() * 70 : 40 + rand() * 46, r = size / 2;
    let best: { x: number; y: number; d: number } | undefined;
    for (let c = 0; c < 16; c++) {
      const x = 40 + r + rand() * (1920 - 80 - 2 * r), y = 40 + r + rand() * (1080 - 80 - 2 * r);
      if (avoid.some((a) => x + r > a.left && x - r < a.right && y + r > a.top && y - r < a.bottom)) continue;
      const d = Math.min(Infinity, ...out.map((p) => Math.hypot(p.x - x, p.y - y) - p.size / 2 - r));
      if (!best || d > best.d) best = { x, y, d };
    }
    if (!best || best.d < 40) continue;
    out.push({ x: best.x, y: best.y, size, name: DECOR_SHAPES[Math.floor(rand() * DECOR_SHAPES.length)], color: colors[Math.floor(rand() * colors.length)], spin: (rand() * 2 - 1) * 14, delay: rand() * 0.5 });
  }
  return out;
};
export const Decor: React.FC<{ t: number; start: number; pieces: ReturnType<typeof decorPieces>; z?: number }> = ({ t, start, pieces, z = 1 }) => (
  <div style={{ position: "absolute", inset: 0, zIndex: z, pointerEvents: "none" }}>
    {pieces.map((q, i) => {
      const p = pop(t, start + 0.15 + q.delay, 10, 170), size = q.size * p;
      if (size <= 0.5) return null;
      const fy = Math.sin((t + i) * 1.3) * 6, fx = Math.cos((t + i * 2) * 0.9) * 4;
      return <Shape key={i} name={q.name} x={q.x - size / 2 + fx} y={q.y - size / 2 + fy} size={size} color={q.color} rotate={i * 37 + (t - start) * q.spin + (1 - p) * -90} />;
    })}
  </div>
);

// ---------------------------------------------------------------------------------------------------
// Material chips: filter chips (selected shows a check) and assist chips, drawn as the app draws them.

export const Chip: React.FC<{ label: string; selected?: boolean; color: string; container: string; outline: string; size?: number; style?: React.CSSProperties }> = ({ label, selected, color, container, outline, size = 30, style }) => (
  <div style={{ display: "inline-flex", alignItems: "center", gap: size * 0.3, height: size * 1.9, padding: `0 ${size * 0.62}px 0 ${selected ? size * 0.42 : size * 0.62}px`, borderRadius: size * 0.5, boxSizing: "border-box", background: selected ? container : "transparent", border: selected ? "none" : `2px solid ${outline}`, color, fontFamily: FONT, fontSize: size, fontWeight: 560, whiteSpace: "nowrap", ...style }}>
    {selected && <svg width={size * 0.9} height={size * 0.9} viewBox="0 0 24 24"><path d="M5 12.5 l4.5 4.5 L19 7.5" fill="none" stroke={color} strokeWidth="2.6" strokeLinecap="round" strokeLinejoin="round" /></svg>}
    {label}
  </div>
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

export const Ruler: React.FC<{ x: number; y: number; width: number; angle: number; ticks?: number; band?: number; accent?: string }> = ({ x, y, width, angle, ticks = 1, band = 0, accent = "#8F5700" }) => {
  const n = Math.floor((width - 56) / 12), shown = Math.floor((n + 1) * clamp(ticks));
  return (
    <g transform={`translate(${x} ${y}) rotate(${angle})`}>
      <rect x="0" y="12" width={width} height="136" rx="36" fill="#B56D00" opacity=".16" />
      <rect width={width} height="136" rx="36" fill="#F6B91D" />
      {band > 0 && <rect x="28" y="0" width={band * (width - 56)} height="136" fill={accent} opacity=".16" />}
      {band > 0 && <rect x={28 + band * (width - 56) - 3} y="-14" width="6" height="164" rx="3" fill={accent} />}
      {Array.from({ length: shown }, (_, i) => <line key={i} x1={28 + i * 12} x2={28 + i * 12} y1="0" y2={i % 10 === 0 ? 66 : i % 5 === 0 ? 46 : 26} stroke="#654000" strokeWidth={i % 5 ? 1.8 : 3} strokeLinecap="round" />)}
      {Array.from({ length: Math.floor(shown / 10) + 1 }, (_, k) => k > 0 && <text key={k} x={28 + k * 120} y="98" textAnchor="middle" fontFamily={FONT} fontWeight={600} fontSize="26" fill="#654000">{k}</text>)}
    </g>
  );
};

// ---------------------------------------------------------------------------------------------------
// Unit Converter: two units on MaterialShapes with the swap arrows between them; on each category change both
// cards flip over (the content changes at the edge-on moment) and take the next pair's shapes.

export type Unit = { value: string; label: string; shape: string; color: string };
export const UnitPair: React.FC<{ cx: number; cy: number; gap: number; a: Unit; b: Unit; flip: number; ink: string; arrows: string; turn: number; size?: number }> = ({ cx, cy, gap, a, b, flip, ink, arrows, turn, size = 300 }) => {
  const unit = (u: Unit, x: number, k: string, lag: number) => {
    const f = clamp(flip - lag), sx = Math.abs(Math.cos(f * Math.PI));
    return (
      <div key={k} style={{ position: "absolute", left: x - size / 2, top: cy - size / 2, width: size, height: size, zIndex: 4, transform: `scaleX(${Math.max(0.02, sx).toFixed(3)}) rotate(${(f * 30).toFixed(1)}deg)` }}>
        <Shape name={u.shape} x={0} y={0} size={size} color={u.color} rotate={12} />
        <div style={{ position: "absolute", inset: 0, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", fontFamily: FONT, color: ink }}>
          <div style={{ fontSize: size * (u.value.length > 4 ? 0.2 : 0.26), fontWeight: 650, letterSpacing: -2, lineHeight: 1 }}>{u.value}</div>
          <div style={{ fontSize: size * 0.14, fontWeight: 560, marginTop: 8 }}>{u.label}</div>
        </div>
      </div>
    );
  };
  return (
    <>
      {unit(a, cx - gap / 2, "a", 0)}
      {unit(b, cx + gap / 2, "b", 0.12)}
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5 }}>
        <g transform={`translate(${cx} ${cy}) rotate(${turn})`}>
          <circle r="62" fill="#FFFFFF" />
          <g fill="none" stroke={arrows} strokeWidth="9" strokeLinecap="round" strokeLinejoin="round">
            <path d="M -30 -12 H 28 l -14 -14" /><path d="M 30 12 H -28 l 14 14" />
          </g>
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

/**
 * The Decision Wheel with its choices: each segment fills and takes its label on its own beat (`shown`, 0..1
 * per segment); once it settles, the winning segment lifts out and the others step back.
 */
export const Wheel: React.FC<{ cx: number; cy: number; r: number; rotation: number; choices: string[]; shown: number[]; colors: string[]; ground: string; ink: string; accent: string; winner?: number; lift?: number }> = ({ cx, cy, r, rotation, choices, shown, colors, ground, ink, accent, winner = -1, lift = 0 }) => {
  const n = choices.length, seg = 360 / n;
  return (
    <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 3, overflow: "visible" }}>
      <circle cx={cx} cy={cy + 14} r={r + 18} fill="#3A2240" opacity=".08" />
      <circle cx={cx} cy={cy} r={r + 18} fill={ground} />
      <g transform={`translate(${cx} ${cy}) rotate(${rotation})`}>
        {choices.map((label, i) => {
          const a = ((i * seg - 90) * Math.PI) / 180, b = (((i + 1) * seg - 90) * Math.PI) / 180, mid = (i + 0.5) * seg - 90, m = (mid * Math.PI) / 180;
          const p = clamp(shown[i] ?? 1), win = i === winner, out = win ? lift * 26 : 0, rr = r * (0.55 + 0.45 * p) + (win ? lift * 14 : 0);
          return (
            <g key={i} transform={`translate(${(Math.cos(m) * out).toFixed(1)} ${(Math.sin(m) * out).toFixed(1)})`} opacity={winner >= 0 && !win ? 1 - lift * 0.45 : 1}>
              <path d={`M 0 0 L ${rr * Math.cos(a)} ${rr * Math.sin(a)} A ${rr} ${rr} 0 0 1 ${rr * Math.cos(b)} ${rr * Math.sin(b)} Z`} fill={win && lift > 0 ? accent : colors[i % colors.length]} opacity={0.25 + 0.75 * p} stroke={ground} strokeWidth="8" strokeLinejoin="round" />
              <text transform={`rotate(${mid}) translate(${r * 0.62} 0)`} textAnchor="middle" dominantBaseline="central" fontFamily={FONT} fontWeight={650} fontSize={r * 0.13} fill={win && lift > 0 ? "#FFFFFF" : ink} opacity={clamp(p * 2 - 1)}>{label}</text>
            </g>
          );
        })}
        <circle r={r * 0.15} fill={ground} />
        <circle r={r * 0.06} fill={accent} />
      </g>
      <path d={`M ${cx - 30} ${cy - r - 34} h 60 l -30 58 Z`} fill={accent} stroke={accent} strokeWidth="16" strokeLinejoin="round" />
    </svg>
  );
};

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
