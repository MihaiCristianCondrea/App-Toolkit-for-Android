import React, { useMemo } from "react";
import { AbsoluteFill, Audio, Img, staticFile, useCurrentFrame } from "remotion";
import { useFonts } from "./LitScreen";
import { Shape } from "./Shape";
import timeline from "./timeline.json";
import icon from "./assets/icon.png";
import { B, FPS, clamp, drift, enter, mix, pop, ramp, standard } from "./lib/anim";
import { CONFETTI, FONT, NEUTRAL, WORLDS } from "./theme";
import type { World } from "./theme";
import { Bubble, Chip, ConfettiBurst, Decor, ELEVATION, GlassDefs, Headline, Phone, Receipt, Ripples, Ruler, ShapeWipe, SoundWaves, UnitPair, Wheel, burstPieces, decorPieces, speakers } from "./motifs";
import type { PhonePose, Screen } from "./motifs";

/**
 * One grid for every tool scene (Remotion's video-layout rules: one thing to notice per zone, key content in a
 * generous safe area, type sized for a phone screen): the title block top left, the tool's motif in the zone
 * under it, the phones in the right column, everything inside the safe area.
 */
const GRID = { left: 144, right: 1776, top: 108, bottom: 972, column: 940, phone: 1380, motifTop: 560 };
const TITLE = { size: 132, sub: 0.45 };

// The real screens, in the states the store listings show.
const SCREENS: Record<string, Screen> = {
  water: ["quick-tools", { activeToolId: "water_eject" }],
  protractor: ["numbers", { activeSheet: "protractor", protractorAngle: 45 }],
  length: ["numbers", { converterCategory: "Length", converterFrom: "Metre", converterTo: "Centimetre", converterInput: "1", activeSheet: "unit_converter" }],
  bill: ["numbers", { activeSheet: "bill_splitter", billAmount: "52.00", billCurrency: "EUR", billTipEnabled: true, tipPercent: 10, splitPeople: 2 }],
  wheel: ["play", { activeToolId: "decision_wheel", wheelText: "Walk\nRead\nCook\nDraw\nMusic\nRest", wheelRotation: 0, wheelWinner: "" }],
  result: ["play", { activeToolId: "decision_wheel", wheelText: "Walk\nRead\nCook\nDraw\nMusic\nRest", wheelRotation: 1530, wheelWinner: "Music" }],
};
/** Water Eject opens from its card in Quick Tools: the list is scrolled to it, as on the listing. */
const WATER_SCROLL = 672;

const Ground: React.FC<{ color: string }> = ({ color }) => <AbsoluteFill style={{ background: color }} />;

/** A large tonal shape that breathes slowly behind the phone. */
const Behind: React.FC<{ t: number; name: string; x: number; y: number; size: number; color: string; seed: number; start: number }> = ({ t, name, x, y, size, color, seed, start }) => {
  const p = pop(t, start, 14, 120), s = size * (0.7 + 0.3 * p);
  return <div style={{ position: "absolute", inset: 0, zIndex: 1 }}><Shape name={name} x={x - s / 2 + drift(t, seed) * 10} y={y - s / 2 + drift(t, seed + 1) * 10} size={s} color={color} rotate={(t - start) * 6 + seed * 20} /></div>;
};

/**
 * A phone's entrance: it flies in from `from` (an offset and a turn) and turns to its pose on a Material
 * spring, so it overshoots a little and settles, then floats. The real 3D Pixel is redrawn for every pose
 * the turn passes through (yaw and pitch are kept to quarter degrees, so a settled phone is drawn once).
 */
type From = { yaw: number; pitch: number; roll: number; dx: number; dy: number };
const fly = (t: number, start: number, pose: PhonePose, from: From) => {
  const r = pop(t, start, 15, 120), q = ramp(t, start, 0.85), k = 1 - r, qz = (v: number) => Math.round(v * 4) / 4;
  return {
    pose: { ...pose, yaw: qz((pose.yaw ?? 0) + k * from.yaw), pitch: qz((pose.pitch ?? 0) + k * from.pitch), roll: (pose.roll ?? 0) + k * from.roll },
    dx: (1 - q) * from.dx + drift(t, start) * 6, dy: (1 - q) * from.dy + drift(t, start + 3) * 6, grow: 0.8 + 0.2 * q,
  };
};

// ------------------------------------------------------------------------------------------------- intro
const INTRO_SHAPES: { world: World; name: string; x: number; y: number; size: number }[] = [
  { world: WORLDS.water, name: "cookie_9sided", x: 1540, y: 300, size: 250 },
  { world: WORLDS.ruler, name: "soft_burst", x: 380, y: 790, size: 270 },
  { world: WORLDS.unit, name: "cookie_6sided", x: 1500, y: 810, size: 230 },
  { world: WORLDS.bill, name: "pill", x: 400, y: 270, size: 240 },
  { world: WORLDS.wheel, name: "flower", x: 700, y: 950, size: 220 },
];
const Intro: React.FC<{ t: number }> = ({ t }) => {
  const swing = ramp(t, B.swing, 0.8, enter), iconP = pop(t, B.icon, 10, 190);
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} />
      {INTRO_SHAPES.map((s, i) => {
        const p = pop(t, B.introShapes[i], 12, 200), a = (swing * 22 * Math.PI) / 180, dx = s.x - 960, dy = s.y - 540, k = 1 - 0.08 * swing;
        const x = 960 + k * (dx * Math.cos(a) - dy * Math.sin(a)), y = 540 + k * (dx * Math.sin(a) + dy * Math.cos(a)), size = s.size * p;
        return <Shape key={i} name={s.name} x={x - size / 2} y={y - size / 2} size={size} color={s.world.shape} rotate={i * 23 + t * 10} />;
      })}
      <Img src={icon} style={{ position: "absolute", left: 960 - 150, top: 410 - 150 - (1 - iconP) * 30, width: 300, height: 300, transform: `scale(${iconP})`, zIndex: 4 }} />
      <Headline t={t} at={[B.title, B.tagline]} title="App Toolkit" sub="Everyday tools in one app" x={960} y={590} width={1200} align="center" color={NEUTRAL.ink} subColor={NEUTRAL.accent} size={150} subScale={0.42} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- water
const WATER_POSE: PhonePose = { x: 1420, y: 560, scale: 0.78, pitch: 8, yaw: -12, roll: 58 };
const Water: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.water, m = fly(t, start, WATER_POSE, { yaw: 75, pitch: 30, roll: -24, dx: 520, dy: 420 });
  // The waves leave the middle of the speaker edge (between the two groups of holes) of the pose as it is now.
  const sp = speakers(m.pose), dir = sp.reduce((a, s) => [a[0] + s.direction[0], a[1] + s.direction[1]], [0, 0]), len = Math.hypot(dir[0], dir[1]) || 1;
  const from = { at: [sp.reduce((a, s) => a + s.at[0], 0) / sp.length + m.dx, sp.reduce((a, s) => a + s.at[1], 0) / sp.length + m.dy] as [number, number], direction: [dir[0] / len, dir[1] / len] as [number, number] };
  const decor = useMemo(() => decorPieces(11, 14, [{ left: 120, top: 80, right: 760, bottom: 570 }, { left: 1040, top: 150, right: 1880, bottom: 990 }, { left: 200, top: 520, right: 1100, bottom: 1080 }], [w.tones[0], w.tones[1], w.tones[2], w.container]), []);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="cookie_12sided" x={1420} y={560} size={880} color={w.shape} seed={1} start={start} />
      <Decor t={t} start={start} pieces={decor} />
      <Phone screen={SCREENS.water} scroll={WATER_SCROLL} pose={m.pose} dx={m.dx} dy={m.dy} grow={m.grow} />
      <SoundWaves t={t} onsets={B.waterWaves} drops={B.waterDrops} from={from} line={w.accent} glow={w.bright} tint={w.tones[0]} reach={600} />
      <Headline t={t} at={B.waterHeadline} title={"Water\nEject"} sub={"A short tone\nfor wet speakers"} x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- ruler
// Two phones: the Surface Ruler measuring as the drawn ruler does, and the Protractor sweeping to 45 degrees.
const RULER_A: PhonePose = { x: 1230, y: 545, scale: 0.7, yaw: 16, roll: -5 };
const RULER_B: PhonePose = { x: 1575, y: 535, scale: 0.72, yaw: -16, roll: 5 };
const RulerScene: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.ruler;
  const a = fly(t, start, RULER_A, { yaw: -80, pitch: 30, roll: -20, dx: -600, dy: 500 }), b = fly(t, start + 0.5, RULER_B, { yaw: 80, pitch: 30, roll: 20, dx: 600, dy: 500 });
  const [m0, m1] = B.rulerMeasure, measure = ramp(t, m0, m1 - m0, standard);
  const marker = Math.round((0.12 + 0.38 * measure) * 100) / 100;
  const s = B.protractorSweep, angle = Math.round(45 * s.reduce((acc, at, i) => acc + ramp(t, at, 0.35) * [0.3, 0.25, 0.25, 0.2][i], 0));
  const decor = useMemo(() => decorPieces(23, 12, [{ left: 120, top: 80, right: 820, bottom: 500 }, { left: 110, top: 600, right: 980, bottom: 940 }, { left: 1040, top: 150, right: 1790, bottom: 990 }], [w.tones[0], w.tones[1], w.container, "#EE9B00"]), []);
  const slide = ramp(t, B.rulerSlide, 0.7);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="soft_burst" x={1400} y={540} size={940} color={w.shape} seed={2} start={start} />
      <Decor t={t} start={start} pieces={decor} />
      <Phone screen={["numbers", { activeSheet: "ruler", rulerAdvanced: false, rulerMarker: marker }]} pose={a.pose} dx={a.dx} dy={a.dy} grow={a.grow} z={3} />
      <Phone screen={["numbers", { activeSheet: "protractor", protractorAngle: angle }]} pose={b.pose} dx={b.dx} dy={b.dy} grow={b.grow} z={4} />
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, overflow: "visible" }}>
        <Ruler x={mix(-1100, GRID.left, slide)} y={700} width={GRID.column - GRID.left} angle={-4} ticks={ramp(t, B.rulerSlide + 0.15, 0.8)} band={0.12 + 0.38 * measure} accent={w.accent} />
      </svg>
      <Headline t={t} at={B.rulerHeadline} title={"Ruler &\nProtractor"} sub="Measure on your screen" x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- unit
// Five categories, one per beat: the chips select it, the unit cards flip to its pair and the phone follows.
const CATEGORIES = [
  { name: "Length", from: "Metre", to: "Centimetre", input: "1", a: ["1", "m"], b: ["100", "cm"], shapes: ["cookie_9sided", "cookie_6sided"] },
  { name: "Mass", from: "Gram", to: "Kilogram", input: "500", a: ["500", "g"], b: ["0.5", "kg"], shapes: ["clover_4leaf", "soft_burst"] },
  { name: "Temperature", from: "Celsius", to: "Fahrenheit", input: "20", a: ["20", "°C"], b: ["68", "°F"], shapes: ["sunny", "flower"] },
  { name: "Volume", from: "Litre", to: "Millilitre", input: "2", a: ["2", "L"], b: ["2000", "mL"], shapes: ["gem", "pill"] },
  { name: "Data", from: "Gigabyte", to: "Megabyte", input: "1", a: ["1", "GB"], b: ["1000", "MB"], shapes: ["cookie_12sided", "clover_8leaf"] },
];
const UNIT_POSE: PhonePose = { x: GRID.phone, y: 540, scale: 0.8, yaw: -12, roll: -3 };
const Unit: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.unit, m = fly(t, start, UNIT_POSE, { yaw: -70, pitch: 40, roll: 18, dx: 300, dy: 700 });
  const at = B.unitCategories, i = Math.max(0, at.filter((c) => t >= c + 0.2).length - 1), shown = Math.max(0, at.filter((c) => t >= c).length - 1);
  const last = at[shown], flip = shown > 0 ? clamp((t - last) / 0.4) : 0, c = CATEGORIES[i];
  const count = i === 0 ? String(Math.max(1, Math.round(100 * ramp(t, at[0], 0.5, (x) => x)))) : c.b[0];
  const enterP = pop(t, at[0] - 0.2, 13, 160), turn = shown * 180 + (shown > 0 ? enter(clamp((t - last) / 0.5)) * 0 : 0);
  const decor = useMemo(() => decorPieces(37, 12, [{ left: 120, top: 80, right: 820, bottom: 560 }, { left: 120, top: 560, right: 980, bottom: 960 }, { left: 1150, top: 120, right: 1620, bottom: 990 }], [w.tones[0], w.tones[1], w.tones[2], w.container]), []);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="clover_4leaf" x={GRID.phone} y={500} size={860} color={w.tones[2]} seed={3} start={start} />
      <Decor t={t} start={start} pieces={decor} />
      <Phone screen={["numbers", { activeSheet: "unit_converter", converterCategory: c.name, converterFrom: c.from, converterTo: c.to, converterInput: c.input, converterEditingLeft: true }]} pose={m.pose} dx={m.dx} dy={m.dy} grow={m.grow} />
      <div style={{ position: "absolute", left: GRID.left, top: 575, display: "flex", gap: 10, zIndex: 5 }}>
        {CATEGORIES.map((k, j) => { const p = pop(t, start + 0.4 + j * 0.06, 12, 200); return <div key={k.name} style={{ transform: `scale(${p})`, opacity: clamp(p * 2) }}><Chip label={k.name} selected={j === shown} color={w.ink} container={w.container} outline={w.tones[2]} size={28} /></div>; })}
      </div>
      <div style={{ position: "absolute", inset: 0, transform: `scale(${0.6 + 0.4 * enterP})`, transformOrigin: "540px 820px", opacity: clamp(enterP * 2), zIndex: 4 }}>
        <UnitPair cx={540} cy={820} gap={390} flip={flip} turn={(shown + flip) * 180} ink={w.ink} arrows={w.accent} size={250}
          a={{ value: c.a[0], label: c.a[1], shape: c.shapes[0], color: w.tones[0] }} b={{ value: count, label: c.b[1], shape: c.shapes[1], color: w.tones[1] }} />
      </div>
      <Headline t={t} at={B.unitHeadline} title={"Unit\nConverter"} sub="Convert units both ways" x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- bill
const BILL_POSE: PhonePose = { x: GRID.phone, y: 540, scale: 0.8, yaw: -10, roll: 3 };
const Bill: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.bill, m = fly(t, start, BILL_POSE, { yaw: 80, pitch: -30, roll: -16, dx: 400, dy: -800 }), out = ramp(t, B.billReceipt, 0.7);
  const decor = useMemo(() => decorPieces(53, 13, [{ left: 120, top: 80, right: 820, bottom: 560 }, { left: 300, top: 540, right: 800, bottom: 1000 }, { left: 1150, top: 120, right: 1620, bottom: 990 }], [w.tones[0], w.tones[1], w.tones[2], w.container]), []);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="soft_burst" x={GRID.phone} y={500} size={860} color={w.shape} seed={4} start={start} />
      <Decor t={t} start={start} pieces={decor} />
      {t >= B.billReceipt && <Receipt x={mix(1240, 404, out)} y={588} angle={mix(10, -5, out)} scale={1.05} t={t} lines={B.billLines} ink={w.ink} accent={w.accent} />}
      <Phone screen={SCREENS.bill} pose={m.pose} dx={m.dx} dy={m.dy} grow={m.grow} z={5} />
      <Headline t={t} at={B.billHeadline} title={"Tip & Bill\nSplitter"} sub="The tip and each share" x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- wheel
// The choices arrive on the wheel one by one, it spins, settles on Music, and the pick lifts out.
const CHOICES = ["Walk", "Read", "Cook", "Draw", "Music", "Rest"];
const WHEEL_POSE: PhonePose = { x: GRID.phone + 40, y: 540, scale: 0.8, yaw: -12, roll: 2 };
const WheelScene: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.wheel, m = fly(t, start, WHEEL_POSE, { yaw: -75, pitch: 35, roll: 20, dx: 500, dy: 600 });
  const [s0, s1] = B.wheelSpin, spin = 1 - (1 - clamp((t - s0) / (s1 - s0))) ** 3, rot = spin * timeline.wheelTurn;
  const pieces = useMemo(() => burstPieces({ seed: 41, count: 46, from: [520, 540], colors: w.tones.concat(["#D9C67D", "#9ABDA9"]), avoid: [{ x: 520, y: 765, r: 250 }, { x: 1420, y: 540, r: 260 }, { x: 420, y: 300, r: 300 }, { x: 860, y: 640, r: 120 }] }), []);
  const decor = useMemo(() => decorPieces(67, 12, [{ left: 120, top: 80, right: 820, bottom: 500 }, { left: 280, top: 500, right: 760, bottom: 1000 }, { left: 740, top: 560, right: 1000, bottom: 720 }, { left: 1190, top: 120, right: 1660, bottom: 990 }], [w.tones[0], w.tones[1], w.tones[2], w.container]), []);
  const pick = clamp((t - B.wheelPick) / 0.3), lift = pop(t, B.wheelBurst, 9, 200), chip = pop(t, B.wheelPick, 10, 210);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="flower" x={520} y={765} size={640} color={w.container} seed={6} start={start} />
      <Decor t={t} start={start} pieces={decor} />
      <Wheel cx={520} cy={765} r={205} rotation={rot} choices={CHOICES} shown={B.wheelChoices.map((c) => pop(t, c, 11, 200))} colors={w.tones} ground={w.field} ink={w.ink} accent={w.accent} winner={t >= B.wheelBurst ? 4 : -1} lift={lift} />
      <Phone screen={SCREENS.wheel} pose={m.pose} dx={m.dx} dy={m.dy} grow={m.grow} opacity={1 - pick} />
      <Phone screen={SCREENS.result} pose={m.pose} dx={m.dx} dy={m.dy} opacity={pick} grow={m.grow} z={3} />
      <div style={{ position: "absolute", left: 860, top: 640, transform: `translate(-50%, -50%) scale(${chip})`, opacity: clamp(chip * 2), zIndex: 6, boxShadow: ELEVATION[2], borderRadius: 32 }}>
        <Chip label="Music" selected color="#FFFFFF" container={w.accent} outline={w.accent} size={40} />
      </div>
      <ConfettiBurst t={t} at={B.wheelBurst} from={[520, 540]} pieces={pieces} />
      <Headline t={t} at={B.wheelHeadline} title={"Decision\nWheel"} sub="Let chance decide" x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- montage
const TILES: { key: keyof typeof WORLDS; draw: (c: World) => React.ReactNode }[] = [
  { key: "water", draw: (c) => <g><GlassDefs tint={c.tones[0]} id="tt-tile" />{[44, 78, 112].map((r, i) => { const d = `M ${150 - r * 0.8} ${96 + r * 0.6} A ${r} ${r} 0 0 0 ${150 + r * 0.8} ${96 + r * 0.6}`; return <g key={i} opacity={1 - i * 0.18}><path d={d} fill="none" stroke={c.bright} strokeWidth="12" strokeLinecap="round" opacity=".28" filter="url(#tt-tile-glow)" /><path d={d} fill="none" stroke={c.accent} strokeWidth={5 - i} strokeLinecap="round" opacity=".8" /></g>; })}<Bubble x={226} y={200} r={20} rim={c.bright} id="tt-tile" /><Bubble x={84} y={232} r={13} rim={c.bright} id="tt-tile" /></g> },
  { key: "ruler", draw: (c) => <g transform="translate(36 140) rotate(-10)"><rect width="230" height="70" rx="20" fill="#F6B91D" />{Array.from({ length: 17 }, (_, i) => <line key={i} x1={20 + i * 12} x2={20 + i * 12} y1="0" y2={i % 5 ? 18 : 34} stroke="#654000" strokeWidth="2.2" />)}</g> },
  { key: "unit", draw: (c) => <g fontFamily={FONT} fontWeight={650} fill={c.ink} textAnchor="middle"><circle cx="90" cy="160" r="62" fill={c.tones[0]} /><circle cx="210" cy="160" r="62" fill={c.tones[1]} /><text x="90" y="178" fontSize="52">m</text><text x="210" y="176" fontSize="46">cm</text></g> },
  { key: "bill", draw: (c) => <g fontFamily={FONT}><rect x="70" y="60" width="160" height="200" rx="22" fill="#FFFBF5" /><rect x="94" y="96" width="112" height="10" rx="5" fill={c.tones[2]} /><rect x="94" y="122" width="80" height="10" rx="5" fill={c.tones[2]} /><text x="150" y="214" fontSize="40" fontWeight={650} fill={c.ink} textAnchor="middle">€28.60</text></g> },
  { key: "wheel", draw: (c) => <g transform="translate(150 165)">{Array.from({ length: 6 }, (_, i) => { const a = ((i * 60 - 90) * Math.PI) / 180, b = (((i + 1) * 60 - 90) * Math.PI) / 180, r = 100; return <path key={i} d={`M 0 0 L ${r * Math.cos(a)} ${r * Math.sin(a)} A ${r} ${r} 0 0 1 ${r * Math.cos(b)} ${r * Math.sin(b)} Z`} fill={c.tones[i % 3]} stroke={c.field} strokeWidth="4" />; })}<path d="M -13 -118 h 26 l -13 26 Z" fill={c.accent} /></g> },
];
const MORE = ["Compass", "Bubble Level", "Flash Dimmer", "Morse & SOS", "Counter", "Coin Flip", "Dice Roll", "Random number", "Reaction Test", "Breathe"];
const Montage: React.FC<{ t: number }> = ({ t }) => {
  const rise = ramp(t, B.montageHeadline, 0.6);
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} />
      {TILES.map((tile, i) => {
        const c = WORLDS[tile.key], p = pop(t, B.montageTiles[i], 12, 190), x = 960 + (i - 2) * 350 - 160, y = 170 - rise * 70;
        return (
          <div key={tile.key} style={{ position: "absolute", left: x, top: y, width: 320, height: 380, borderRadius: 48, background: c.field, boxShadow: ELEVATION[2], transform: `scale(${p}) rotate(${(i - 2) * 2.5 * (1 - rise)}deg)`, transformOrigin: "50% 60%", overflow: "hidden", zIndex: 3 }}>
            <svg width="320" height="300" viewBox="0 0 300 300" style={{ position: "absolute", left: 10, top: 6 }}>{tile.draw(c)}</svg>
            <div style={{ position: "absolute", left: 0, right: 0, bottom: 34, textAlign: "center", fontFamily: FONT, fontSize: 30, fontWeight: 600, color: c.ink }}>{c.name}</div>
          </div>
        );
      })}
      <Headline t={t} at={[B.montageHeadline]} title="Everyday tools, one app" x={960} y={590} width={1600} align="center" color={NEUTRAL.ink} size={112} />
      <div style={{ position: "absolute", left: 160, right: 160, top: 790, display: "flex", flexWrap: "wrap", justifyContent: "center", gap: 18, zIndex: 4 }}>
        {MORE.map((name, i) => {
          const p = pop(t, B.montageChips[Math.min(Math.floor(i / 2), B.montageChips.length - 1)] + (i % 2) * 0.12, 12, 220);
          const c = Object.values(WORLDS)[i % 5];
          return <div key={name} style={{ transform: `scale(${p})`, opacity: clamp(p * 2), padding: "16px 30px", borderRadius: 999, background: c.container, color: c.ink, fontFamily: FONT, fontSize: 32, fontWeight: 560 }}>{name}</div>;
        })}
      </div>
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- end
const End: React.FC<{ t: number }> = ({ t }) => {
  const iconP = pop(t, B.endBurst, 10, 190);
  const pieces = useMemo(() => burstPieces({ seed: 7, count: 56, from: [960, 380], colors: CONFETTI, avoid: [{ x: 960, y: 380, r: 190 }, { x: 960, y: 700, r: 330 }] }), []);
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} />
      <Ripples t={t} onsets={B.endRings} at={[960, 380]} colors={[WORLDS.water.container, WORLDS.ruler.container, WORLDS.unit.container, WORLDS.bill.container, WORLDS.wheel.container]} from={160} reach={720} />
      <ConfettiBurst t={t} at={B.endBurst} from={[960, 380]} pieces={pieces} z={3} />
      <Img src={icon} style={{ position: "absolute", left: 960 - 160, top: 380 - 160, width: 320, height: 320, transform: `scale(${iconP})`, zIndex: 5 }} />
      <Headline t={t} at={[B.endTitle, B.endPlay]} title="App Toolkit" sub="Available on Google Play" x={960} y={590} width={1300} align="center" color={NEUTRAL.ink} subColor={NEUTRAL.accent} size={150} subScale={0.42} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- film
const SCENES: Record<string, React.FC<{ t: number; start: number }>> = {
  intro: ({ t }) => <Intro t={t} />, water: Water, ruler: RulerScene, unit: Unit, bill: Bill, wheel: WheelScene, montage: ({ t }) => <Montage t={t} />, end: ({ t }) => <End t={t} />,
};

/**
 * The film: one scene per world, each opening out of a MaterialShape on its first beat (the previous world
 * stays underneath until it is covered), over the soundtrack scripts/compose.mjs writes from the same timeline. Every time comes from timeline.json.
 */
export const Trailer: React.FC = () => {
  useFonts();
  const t = useCurrentFrame() / FPS, wipe = timeline.wipeSeconds;
  return (
    <AbsoluteFill style={{ background: NEUTRAL.field, fontFamily: FONT, overflow: "hidden" }}>
      {timeline.scenes.map((s, i) => {
        if (t < s.start || t >= s.end + wipe) return null;
        const Scene = SCENES[s.id], body = <Scene t={t} start={s.start} />;
        return <div key={s.id} style={{ position: "absolute", inset: 0, zIndex: i }}>{i === 0 ? body : <ShapeWipe t={t} start={s.start} dur={wipe} at={s.from as [number, number]} shape={s.shape!}>{body}</ShapeWipe>}</div>;
      })}
      <Audio src={staticFile("soundtrack.mp3")} />
    </AbsoluteFill>
  );
};
