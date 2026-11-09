import React, { useMemo } from "react";
import { AbsoluteFill, Audio, Img, staticFile, useCurrentFrame } from "remotion";
import { useFonts } from "./LitScreen";
import { Shape } from "./Shape";
import timeline from "./timeline.json";
import icon from "./assets/icon.png";
// Google's official badge, unaltered (marketing/assets/badges/google-play): moved and scaled whole, never redrawn.
import badge from "../../../../../assets/badges/google-play/en_badge_web_generic.png";
import { B, FPS, clamp, drift, enter, leave, mix, pop, pullOut, pushIn, ramp, standard } from "./lib/anim";
import { CONFETTI, FONT, NEUTRAL, WORLDS } from "./theme";
import type { World } from "./theme";
import { Chip, ConfettiBurst, ShareCard, Waveform, Decor, ELEVATION, Headline, MorphPair, Phone, Receipt, Ripples, Ruler, ShapeWipe, SoundWaves, Wheel, burstPieces, speakers } from "./motifs";
import type { PhonePose, Screen } from "./motifs";

/**
 * One grid for every tool scene (Remotion's video-layout rules: one thing to notice per zone, key content in a
 * generous safe area, type sized for a phone screen): the title block top left, the tool's motif in the zone
 * under it, the phones in the right column, everything inside the safe area.
 */
const GRID = { left: 144, right: 1776, top: 108, bottom: 972, column: 940, phone: 1380, motifTop: 560 };
const TITLE = { size: 132, sub: 0.45 };
/** The title block's box, kept clear of decoration and confetti in every tool scene. */
const TITLE_BOX = { left: GRID.left, top: GRID.top, right: 900, bottom: 500 };
/** The Surface Ruler's reading for a marker position: the sheet's own sum (window height over 160 dpi in mm). */
const rulerMm = (marker: number) => (marker * 957) / (160 / 25.4);

/**
 * Decoration placed by hand, as on the listing screenshots: a few MaterialShapes ([x, y, size]) around the
 * phone and at the frame's edges, never in a row, never touching the type, the motif or the phone.
 */
const DECOR_NAMES = ["sunny", "flower", "gem", "clover_4leaf", "soft_burst", "cookie_6sided", "very_sunny", "clover_8leaf"];
const place = (spots: [number, number, number][], colors: string[], seed: number) =>
  spots.map(([x, y, size], i) => ({ x, y, size, name: DECOR_NAMES[(i * 3 + seed) % DECOR_NAMES.length], color: colors[(i + seed) % colors.length], spin: ((i % 2) * 2 - 1) * (8 + i * 2), delay: i * 0.07 }));

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

// ------------------------------------------------------------------------------------------------- camera
/**
 * One camera through the film, so the worlds flow into each other instead of cutting like slides. Every tool
 * scene opens pushed into its hero phone (straight on, centred, PEAK times its size) and pulls back to its
 * layout while the phone twists into its pose on a spring; it ends pushing into its exit phone while the ground
 * blends to the next world's colour. Both scenes hold the same straight pose at the cut, so the cut is only the
 * screen changing, as if one phone travelled through every world.
 */
const C = { x: 960, y: 540 }, PEAK = 1.5, PULL = 1, PUSH = 0.7, CUT = 0.06;
type Cam = { k: number; hero: PhonePose; tilt: number; out: boolean };
const camera = (t: number, start: number, end: number, enterHero: PhonePose, exitHero: PhonePose, tilt: number, exits = true): Cam => {
  const kout = exits ? ramp(t, end - PUSH, PUSH, pushIn) : 0;
  return kout > 0 ? { k: kout, hero: exitHero, tilt, out: true } : { k: 1 - ramp(t, start, PULL, pullOut), hero: enterHero, tilt: -tilt, out: false };
};
/** The camera as a CSS transform: the hero's point travels to the frame's centre while everything scales round it. */
const camTransform = (cam: Cam, depth = 1) => {
  const { k, hero: P } = cam, S = 1 + (PEAK / P.scale - 1) * k * depth;
  return `translate(${mix(P.x, C.x, k)}px, ${mix(P.y, C.y, k)}px) rotate(${Math.sin(k * Math.PI) * cam.tilt}deg) scale(${S}) translate(${-P.x}px, ${-P.y}px)`;
};
const qz = (v: number) => Math.round(v * 4) / 4;
/** The hero phone's pose: straight at the cut, twisting out to its layout pose on a spring, straightening on the way in. */
const heroPose = (t: number, start: number, cam: Cam, layout: PhonePose, twist: number) => {
  const r = cam.out ? 1 - cam.k : pop(t, start + 0.03, 13, 110), extra = cam.out ? 0 : Math.sin(Math.PI * clamp(r)) * twist;
  return { pose: { ...layout, yaw: qz((layout.yaw ?? 0) * r + extra), pitch: qz((layout.pitch ?? 0) * r), roll: (layout.roll ?? 0) * r - extra * 0.3 }, dx: drift(t, start) * 6 * (1 - cam.k), dy: drift(t, start + 3) * 6 * (1 - cam.k), grow: 1 };
};
const hex = (c: string) => [1, 3, 5].map((i) => parseInt(c.slice(i, i + 2), 16));
const mixColor = (a: string, b: string, p: number) => `rgb(${hex(a).map((v, i) => Math.round(v + (hex(b)[i] - v) * p)).join(",")})`;

/**
 * A scene on the camera: the ground (blending to the next world on the way out), a far layer for the large
 * shape and the decoration (it moves less, for depth), and the near layer; everything but the hero phone
 * fades as the camera closes in, so the cut holds only the ground and the phone.
 */
const Stage: React.FC<{ cam: Cam; ground: string; next: string; back?: React.ReactNode; under?: React.ReactNode; hero: React.ReactNode; over?: React.ReactNode }> = ({ cam, ground, next, back, under, hero, over }) => {
  const veil = 1 - clamp((cam.k - 0.45) / 0.45);
  const layer = (depth: number, opacity: number, z: number, child: React.ReactNode) => <div style={{ position: "absolute", inset: 0, transformOrigin: "0 0", transform: camTransform(cam, depth), opacity, zIndex: z }}>{child}</div>;
  return (
    <AbsoluteFill>
      <AbsoluteFill style={{ background: cam.out ? mixColor(ground, next, cam.k) : ground }} />
      {back && layer(0.75, veil, 1, back)}
      {under && layer(1, veil, 2, under)}
      {layer(1, 1, 3, hero)}
      {over && layer(1, veil, 4, over)}
    </AbsoluteFill>
  );
};
type SceneProps = { t: number; start: number; end: number; next: string };

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
  // Four corners of the lockup and one above it, all clear of the icon, the name and the line.
  { world: WORLDS.water, name: "cookie_9sided", x: 1590, y: 270, size: 240 },
  { world: WORLDS.ruler, name: "soft_burst", x: 330, y: 810, size: 260 },
  { world: WORLDS.unit, name: "cookie_6sided", x: 1600, y: 820, size: 230 },
  { world: WORLDS.bill, name: "pill", x: 340, y: 280, size: 230 },
  { world: WORLDS.wheel, name: "flower", x: 960, y: 120, size: 150 },
];
const Intro: React.FC<{ t: number }> = ({ t }) => {
  const swing = ramp(t, B.swing, 0.8, enter), iconP = pop(t, B.icon, 10, 190);
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} />
      {INTRO_SHAPES.map((s, i) => {
        // On the swing beat each shape breathes out and back, turning in place.
        const p = pop(t, B.introShapes[i], 12, 200), k = 1 + 0.05 * Math.sin(swing * Math.PI), size = s.size * p * k;
        const x = s.x + drift(t, i) * 8, y = s.y + drift(t, i + 5) * 8;
        return <Shape key={i} name={s.name} x={x - size / 2} y={y - size / 2} size={size} color={s.world.shape} rotate={i * 23 + t * 10 + swing * 40} />;
      })}
      <Img src={icon} style={{ position: "absolute", left: 960 - 150, top: 410 - 150 - (1 - iconP) * 30, width: 300, height: 300, transform: `scale(${iconP})`, zIndex: 4 }} />
      <Headline t={t} at={[B.title, B.tagline]} title="App Toolkit" sub="Everyday tools in one app" x={960} y={590} width={1200} align="center" color={NEUTRAL.ink} subColor={NEUTRAL.accent} size={150} subScale={0.42} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- water
// The phone stands upright with its speaker pointing down, as the sheet asks; the waves, bubbles and droplets
// leave the speaker below it, and the left zone holds the tone itself, 165 Hz, as a live waveform.
const WATER_POSE: PhonePose = { x: GRID.phone, y: 470, scale: 0.72, pitch: 14, yaw: -12, roll: 5 };
const Water: React.FC<SceneProps> = ({ t, start, end, next }) => {
  const w = WORLDS.water, cam = camera(t, start, end, WATER_POSE, WATER_POSE, 4), m = heroPose(t, start, cam, WATER_POSE, 28);
  // The waves leave the middle of the speaker edge (between the two groups of holes) of the pose as it is now.
  const sp = speakers(m.pose), dir = sp.reduce((a, s) => [a[0] + s.direction[0], a[1] + s.direction[1]], [0, 0]), len = Math.hypot(dir[0], dir[1]) || 1;
  const from = { at: [sp.reduce((a, s) => a + s.at[0], 0) / sp.length + m.dx, sp.reduce((a, s) => a + s.at[1], 0) / sp.length + m.dy] as [number, number], direction: [dir[0] / len, dir[1] / len] as [number, number] };
  const decor = useMemo(() => place([[1080, 180, 104], [1820, 150, 58], [1800, 560, 46], [975, 110, 40], [96, 1000, 64]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  return (
    <Stage cam={cam} ground={w.field} next={next}
      back={<><Behind t={t} name="cookie_12sided" x={GRID.phone} y={470} size={820} color={w.shape} seed={1} start={start} /><Decor t={t} start={start} pieces={decor} /></>}
      hero={<Phone screen={SCREENS.water} scroll={WATER_SCROLL} pose={m.pose} dx={m.dx} dy={m.dy} />}
      over={<>
        <SoundWaves t={t} onsets={B.waterWaves} drops={B.waterDrops} from={from} line={w.accent} glow={w.bright} tint={w.tones[0]} reach={420} spread={52} rise={150} />
        <Waveform t={t} start={B.waterHeadline[1] + 0.25} onsets={B.waterWaves} x={GRID.left} y={850} width={GRID.column - GRID.left} label="165 Hz" line={w.accent} glow={w.bright} ink={w.accent} />
        <Headline t={t} at={B.waterHeadline} title={"Water\nEject"} sub={"A short tone\nfor wet speakers"} x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
      </>} />
  );
};

// ------------------------------------------------------------------------------------------------- ruler
// Two phones: the Surface Ruler measuring as the drawn ruler does, and the Protractor sweeping to 45 degrees.
const RULER_A: PhonePose = { x: 1230, y: 545, scale: 0.7, yaw: 16, roll: -5 };
const RULER_B: PhonePose = { x: 1575, y: 535, scale: 0.72, yaw: -16, roll: 5 };
const RulerScene: React.FC<SceneProps> = ({ t, start, end, next }) => {
  const w = WORLDS.ruler, cam = camera(t, start, end, RULER_A, RULER_B, -5);
  // The camera pulls out of the Surface Ruler and later pushes into the Protractor, which joins on the half bar.
  const a = cam.out ? { pose: RULER_A, dx: 0, dy: 0, grow: 1 } : heroPose(t, start, cam, RULER_A, -30);
  const b = cam.out ? heroPose(t, start, cam, RULER_B, 0) : fly(t, start + 0.5, RULER_B, { yaw: 80, pitch: 30, roll: 20, dx: 600, dy: 500 });
  const [m0, m1] = B.rulerMeasure, measure = ramp(t, m0, m1 - m0, standard);
  const marker = Math.round((0.12 + 0.38 * measure) * 100) / 100;
  const s = B.protractorSweep, angle = Math.round(45 * s.reduce((acc, at, i) => acc + ramp(t, at, 0.35) * [0.3, 0.25, 0.25, 0.2][i], 0));
  const decor = useMemo(() => place([[975, 130, 84], [1840, 160, 56], [1850, 900, 62], [1000, 990, 70], [90, 990, 50]], [w.tones[0], w.tones[1], w.container, "#EE9B00"], 2), []);
  const slide = ramp(t, B.rulerSlide, 0.7);
  const ruler = <Phone screen={["numbers", { activeSheet: "ruler", rulerAdvanced: false, rulerMarker: marker }]} pose={a.pose} dx={a.dx} dy={a.dy} z={3} />;
  const protractor = <Phone screen={["numbers", { activeSheet: "protractor", protractorAngle: angle }]} pose={b.pose} dx={b.dx} dy={b.dy} grow={b.grow} z={4} />;
  return (
    <Stage cam={cam} ground={w.field} next={next}
      back={<><Behind t={t} name="soft_burst" x={1400} y={540} size={940} color={w.shape} seed={2} start={start} /><Decor t={t} start={start} pieces={decor} /></>}
      under={cam.out ? ruler : undefined}
      hero={cam.out ? protractor : ruler}
      over={<>
        {!cam.out && protractor}
        <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, overflow: "visible" }}>
          <Ruler x={mix(-1100, GRID.left, slide)} y={690} width={GRID.column - GRID.left} angle={-2} ticks={ramp(t, B.rulerSlide + 0.15, 0.8)} mm={t >= m0 ? rulerMm(marker) : 0} accent={w.accent} />
        </svg>
        <Headline t={t} at={B.rulerHeadline} title={"Ruler &\nProtractor"} sub="Measure on your screen" x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
      </>} />
  );
};

// ------------------------------------------------------------------------------------------------- unit
// Five categories, one per beat: the chip selects it, the two unit shapes spin on and morph into the next pair
// as the values swap (Material 3 Expressive's loading indicator), and the phone shows the same conversion.
const CATEGORIES = [
  { name: "Length", from: "Metre", to: "Centimetre", input: "1", a: ["1", "m"], b: ["100", "cm"], shapes: ["soft_burst", "cookie_6sided"] as [string, string] },
  { name: "Mass", from: "Gram", to: "Kilogram", input: "500", a: ["500", "g"], b: ["0.5", "kg"], shapes: ["cookie_9sided", "flower"] as [string, string] },
  { name: "Temperature", from: "Celsius", to: "Fahrenheit", input: "20", a: ["20", "°C"], b: ["68", "°F"], shapes: ["pentagon", "clover_4leaf"] as [string, string] },
  { name: "Volume", from: "Litre", to: "Millilitre", input: "2", a: ["2", "L"], b: ["2000", "mL"], shapes: ["pill", "gem"] as [string, string] },
  { name: "Data", from: "Gigabyte", to: "Megabyte", input: "1", a: ["1", "GB"], b: ["1000", "MB"], shapes: ["sunny", "very_sunny"] as [string, string] },
];
const UNIT_POSE: PhonePose = { x: GRID.phone, y: 540, scale: 0.8, yaw: -12, roll: -3 };
const Unit: React.FC<SceneProps> = ({ t, start, end, next }) => {
  const w = WORLDS.unit, cam = camera(t, start, end, UNIT_POSE, UNIT_POSE, 6), m = heroPose(t, start, cam, UNIT_POSE, -30);
  const at = B.unitCategories, i = Math.max(0, at.filter((c) => t >= c + 0.2).length - 1), shown = Math.max(0, at.filter((c) => t >= c).length - 1), c = CATEGORIES[i];
  const count = String(Math.max(1, Math.round(100 * ramp(t, at[0], 0.5, (x) => x))));
  const decor = useMemo(() => place([[1060, 170, 120], [1840, 110, 50], [1730, 690, 64], [1790, 1000, 54], [1010, 960, 58]], [w.tones[0], w.tones[1], w.tones[2], w.container], 2), []);
  return (
    <Stage cam={cam} ground={w.field} next={next}
      back={<><Behind t={t} name="clover_4leaf" x={GRID.phone} y={500} size={860} color={w.tones[2]} seed={3} start={start} /><Decor t={t} start={start} pieces={decor} /></>}
      hero={<Phone screen={["numbers", { activeSheet: "unit_converter", converterCategory: c.name, converterFrom: c.from, converterTo: c.to, converterInput: c.input, converterEditingLeft: true }]} pose={m.pose} dx={m.dx} dy={m.dy} />}
      over={<>
        <div style={{ position: "absolute", left: GRID.left, top: 575, display: "flex", gap: 10, zIndex: 5 }}>
          {CATEGORIES.map((k, j) => { const p = pop(t, start + 0.4 + j * 0.06, 12, 200); return <div key={k.name} style={{ transform: `scale(${p})`, opacity: clamp(p * 2) }}><Chip label={k.name} selected={j === shown} color={w.ink} container={w.container} outline={w.tones[2]} size={28} /></div>; })}
        </div>
        <MorphPair t={t} cx={464} cy={820} gap={390} size={250} changes={at} shapes={CATEGORIES.map((k) => k.shapes)} colors={[w.tones[0], w.tones[1]]} ink={w.ink} arrows={w.accent}
          faces={CATEGORIES.map((k, j) => [{ value: k.a[0], label: k.a[1] }, { value: j === 0 ? count : k.b[0], label: k.b[1] }])} />
        <Headline t={t} at={B.unitHeadline} title={"Unit\nConverter"} sub="Convert units both ways" x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
      </>} />
  );
};

// ------------------------------------------------------------------------------------------------- bill
const BILL_POSE: PhonePose = { x: GRID.phone, y: 540, scale: 0.8, yaw: -10, roll: 3 };
const Bill: React.FC<SceneProps> = ({ t, start, end, next }) => {
  const w = WORLDS.bill, cam = camera(t, start, end, BILL_POSE, BILL_POSE, -4), m = heroPose(t, start, cam, BILL_POSE, 30), out = ramp(t, B.billReceipt, 0.7);
  const decor = useMemo(() => place([[1000, 200, 80], [1790, 120, 92], [1830, 700, 58], [1700, 980, 70], [80, 1000, 54]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  return (
    <Stage cam={cam} ground={w.field} next={next}
      back={<><Behind t={t} name="soft_burst" x={GRID.phone} y={500} size={860} color={w.shape} seed={4} start={start} /><Decor t={t} start={start} pieces={decor} /></>}
      under={<>
        {t >= B.billReceipt && <Receipt x={mix(1240, 158, out)} y={584} angle={mix(10, -3, out)} scale={1.05} t={t} lines={B.billLines} ink={w.ink} accent={w.accent} />}
        {[0, 1].map((k) => { const p = pop(t, B.billLines[3] + k * 0.25, 12, 190); return p > 0 && <div key={k} style={{ position: "absolute", inset: 0, zIndex: 4, opacity: clamp(p * 2), transform: `translateX(${(1 - p) * -160}px)` }}><ShareCard x={540} y={640 + k * 160} who={String(k + 1)} amount="€28.60" ink={w.ink} container={w.container} avatar={w.tones[k]} shape={k ? "cookie_6sided" : "sunny"} /></div>; })}
      </>}
      hero={<Phone screen={SCREENS.bill} pose={m.pose} dx={m.dx} dy={m.dy} z={5} />}
      over={<Headline t={t} at={B.billHeadline} title={"Tip & Bill\nSplitter"} sub="The tip and each share" x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />} />
  );
};

// ------------------------------------------------------------------------------------------------- wheel
// The choices arrive on the wheel one by one, it spins, settles on Music, and the pick lifts out.
const CHOICES = ["Walk", "Read", "Cook", "Draw", "Music", "Rest"];
const WHEEL_POSE: PhonePose = { x: GRID.phone + 40, y: 540, scale: 0.8, yaw: -12, roll: 2 };
const WheelScene: React.FC<SceneProps> = ({ t, start, end, next }) => {
  const w = WORLDS.wheel, cam = camera(t, start, end, WHEEL_POSE, WHEEL_POSE, 5), m = heroPose(t, start, cam, WHEEL_POSE, -28);
  const [s0, s1] = B.wheelSpin, spin = 1 - (1 - clamp((t - s0) / (s1 - s0))) ** 3, rot = spin * timeline.wheelTurn;
  const pieces = useMemo(() => burstPieces({ seed: 41, count: 40, from: [370, 540], colors: w.tones.concat(["#D9C67D", "#9ABDA9"]), avoid: [{ x: 370, y: 770, r: 235 }], boxes: [{ left: 0, top: 0, right: 920, bottom: 520 }, { left: 1200, top: 90, right: 1650, bottom: 990 }, { left: 750, top: 590, right: 970, bottom: 690 }] }), []);
  const decor = useMemo(() => place([[1070, 160, 88], [1830, 220, 70], [1810, 840, 56], [760, 980, 62]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  const pick = clamp((t - B.wheelPick) / 0.3), lift = pop(t, B.wheelBurst, 9, 200), chip = pop(t, B.wheelPick, 10, 210);
  return (
    <Stage cam={cam} ground={w.field} next={next}
      back={<Decor t={t} start={start} pieces={decor} />}
      under={<Wheel cx={370} cy={770} r={205} rotation={rot} choices={CHOICES} shown={B.wheelChoices.map((c) => pop(t, c, 11, 200))} colors={w.tones} ground={w.field} ink={w.ink} accent={w.accent} winner={t >= B.wheelBurst ? 4 : -1} lift={lift} />}
      hero={<>
        <Phone screen={SCREENS.wheel} pose={m.pose} dx={m.dx} dy={m.dy} opacity={1 - pick} />
        <Phone screen={SCREENS.result} pose={m.pose} dx={m.dx} dy={m.dy} opacity={pick} z={3} />
      </>}
      over={<>
        <div style={{ position: "absolute", left: 860, top: 640, transform: `translate(-50%, -50%) scale(${chip})`, opacity: clamp(chip * 2), zIndex: 6, boxShadow: ELEVATION[2], borderRadius: 32 }}>
          <Chip label="Music" selected color="#FFFFFF" container={w.accent} outline={w.accent} size={40} />
        </div>
        <ConfettiBurst t={t} at={B.wheelBurst} from={[370, 540]} pieces={pieces} />
        <Headline t={t} at={B.wheelHeadline} title={"Decision\nWheel"} sub="Let chance decide" x={GRID.left} y={GRID.top} color={w.ink} subColor={w.accent} size={TITLE.size} subScale={TITLE.sub} />
      </>} />
  );
};

// ------------------------------------------------------------------------------------------------- montage
// The camera pulls back from the wheel's pick, the same screen it pushed into, and reveals it as the last of a
// row of five phones, one per world, each in the state its scene ended on. The phones bob on their notes, the
// line rises and more tools stream in; then the row gathers into the App Toolkit icon.
const ROW: { screen: Screen; scroll?: number; world: World }[] = [
  { screen: SCREENS.water, scroll: WATER_SCROLL, world: WORLDS.water }, { screen: SCREENS.protractor, world: WORLDS.ruler },
  { screen: SCREENS.length, world: WORLDS.unit }, { screen: SCREENS.bill, world: WORLDS.bill }, { screen: SCREENS.result, world: WORLDS.wheel },
];
const rowPose = (i: number): PhonePose => ({ x: 960 + (i - 2) * 300, y: 320 + Math.abs(i - 2) * 16, scale: 0.42, roll: (i - 2) * 3 });
const GATHER = { at: 27.5, land: 28, x: 960, y: 330 };
const MORE = ["Compass", "Bubble Level", "Flash Dimmer", "Morse & SOS", "Counter", "Coin Flip", "Dice Roll", "Random number", "Reaction Test", "Breathe"];
const Montage: React.FC<SceneProps> = ({ t, start }) => {
  const hero = rowPose(4), cam = camera(t, start, 0, hero, hero, 3, false);
  // Each phone leaves in turn and they all land in the icon's place on the downbeat it pops.
  const gone = (i: number) => { const s0 = GATHER.at + (4 - i) * 0.05; return leave(clamp((t - s0) / (GATHER.land - s0))); };
  const phone = (i: number) => {
    const p = rowPose(i), g = gone(i), bob = B.montageTiles.reduce((a, b, j) => (j === i && t >= b ? a + Math.sin(clamp((t - b) / 0.5) * Math.PI) * 22 : a), 0);
    const arrive = i === 4 ? 1 : pop(t, start + 0.25 + (3 - i) * 0.09, 12, 150);
    return <Phone key={i} screen={ROW[i].screen} scroll={ROW[i].scroll} pose={{ ...p, roll: (p.roll ?? 0) * (i === 4 ? 1 - cam.k : 1) * (1 - g) }} dx={(GATHER.x - p.x) * g + (1 - arrive) * 120 * (i - 4)} dy={(GATHER.y - p.y) * g - bob} grow={(0.85 + 0.15 * arrive) * (1 - 0.8 * g)} opacity={(i === 4 ? 1 : clamp(arrive * 1.5)) * (1 - clamp((g - 0.85) / 0.15))} z={2} />;
  };
  const leaveT = clamp((t - GATHER.at) / 0.4);
  return (
    <Stage cam={cam} ground={NEUTRAL.field} next={NEUTRAL.field}
      under={[0, 1, 2, 3].map(phone)}
      hero={phone(4)}
      over={<div style={{ position: "absolute", inset: 0, opacity: 1 - leaveT, transform: `translateY(${leaveT * 40}px)` }}>
        <Headline t={t} at={[B.montageHeadline]} title="Everyday tools, one app" x={960} y={600} width={1600} align="center" color={NEUTRAL.ink} size={112} />
        <div style={{ position: "absolute", left: 160, right: 160, top: 790, display: "flex", flexWrap: "wrap", justifyContent: "center", gap: 18, zIndex: 4 }}>
          {MORE.map((name, i) => {
            // A stream, not a stack of pops: each chip rises in a beat behind the last, on a soft spring.
            const p = pop(t, B.montageChips[0] + i * 0.11, 14, 160), c = Object.values(WORLDS)[i % 5];
            return <div key={name} style={{ transform: `translateY(${(1 - p) * 60}px)`, opacity: clamp(p * 1.6), padding: "16px 30px", borderRadius: 999, background: c.container, color: c.ink, fontFamily: FONT, fontSize: 32, fontWeight: 560 }}>{name}</div>;
          })}
        </div>
      </div>} />
  );
};

// ------------------------------------------------------------------------------------------------- end
// The row lands in the icon: it pops with a ripple in each world's colour and confetti; the name rises and the
// official Get it on Google Play badge comes in whole, with Google's trademark line under it.
const End: React.FC<SceneProps> = ({ t, start }) => {
  const iconP = pop(t, B.endBurst, 10, 190), badgeP = pop(t, B.endPlay, 13, 170), legal = ramp(t, B.endPlay + 0.4, 0.6);
  const pieces = useMemo(() => burstPieces({ seed: 7, count: 56, from: [GATHER.x, GATHER.y], colors: CONFETTI, avoid: [{ x: GATHER.x, y: GATHER.y, r: 190 }], boxes: [{ left: 480, top: 440, right: 1440, bottom: 1010 }] }), []);
  return (
    <AbsoluteFill>
      {/* The montage's ground is the same colour and still under this for its last frames. */}
      <AbsoluteFill style={{ background: NEUTRAL.field, opacity: clamp((t - start - 0.3) / 0.1) }} />
      <Ripples t={t} onsets={B.endRings} at={[GATHER.x, GATHER.y]} colors={[WORLDS.water.container, WORLDS.ruler.container, WORLDS.unit.container, WORLDS.bill.container, WORLDS.wheel.container]} from={160} reach={720} />
      <ConfettiBurst t={t} at={B.endBurst} from={[GATHER.x, GATHER.y]} pieces={pieces} z={3} />
      <Img src={icon} style={{ position: "absolute", left: GATHER.x - 150, top: GATHER.y - 150, width: 300, height: 300, transform: `scale(${iconP})`, zIndex: 5 }} />
      <Headline t={t} at={[B.endTitle]} title="App Toolkit" x={960} y={515} width={1300} align="center" color={NEUTRAL.ink} size={150} />
      <Img src={badge} style={{ position: "absolute", left: 960 - 210, top: 730, width: 420, transform: `scale(${0.85 + 0.15 * badgeP})`, opacity: clamp(badgeP * 1.6), zIndex: 6 }} />
      <div style={{ position: "absolute", left: 0, right: 0, top: 985, textAlign: "center", fontFamily: FONT, fontSize: 22, color: NEUTRAL.accent, opacity: legal * 0.8, zIndex: 6 }}>Google Play and the Google Play logo are trademarks of Google LLC.</div>
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- film
const SCENES: Record<string, React.FC<SceneProps>> = {
  intro: ({ t }) => <Intro t={t} />, water: Water, ruler: RulerScene, unit: Unit, bill: Bill, wheel: WheelScene, montage: Montage, end: End,
};
/** How each scene arrives: the water world grows out of its shape in the intro; the end lands on the gathered row; the rest are camera cuts. */
const ARRIVAL: Record<string, "wipe" | "cut" | "over"> = { water: "wipe", end: "over" };

/**
 * The film: the intro, then one world per tool joined by the camera (a push into the phone, a cut on the
 * screen, a pull back into the next world), the montage pulled back from the last phone, and the end card the
 * row gathers into, over the soundtrack scripts/compose.mjs writes from the same timeline. Every time comes
 * from timeline.json.
 */
export const Trailer: React.FC = () => {
  useFonts();
  const t = useCurrentFrame() / FPS, wipe = timeline.wipeSeconds, scenes = timeline.scenes;
  return (
    <AbsoluteFill style={{ background: NEUTRAL.field, fontFamily: FONT, overflow: "hidden" }}>
      {scenes.map((s, i) => {
        const nextScene = scenes[i + 1], arrival = ARRIVAL[s.id] ?? "cut", leaves = nextScene ? ARRIVAL[nextScene.id] ?? "cut" : "cut";
        const from = arrival === "cut" && i > 0 ? s.start - CUT : s.start, until = leaves === "wipe" ? s.end + wipe : leaves === "over" ? s.end + 0.4 : s.end + CUT;
        if (t < from || t >= until) return null;
        const next = nextScene ? (WORLDS[nextScene.id as keyof typeof WORLDS]?.field ?? NEUTRAL.field) : NEUTRAL.field;
        const Scene = SCENES[s.id], body = <Scene t={t} start={s.start} end={s.end} next={next} />;
        if (arrival === "wipe") return <div key={s.id} style={{ position: "absolute", inset: 0, zIndex: i }}><ShapeWipe t={t} start={s.start} dur={wipe} at={s.from as [number, number]} shape={s.shape!}>{body}</ShapeWipe></div>;
        // A camera cut: the next scene dissolves in over the 0.12 s where both hold the same straight phone.
        const opacity = arrival === "cut" && i > 0 ? clamp((t - (s.start - CUT)) / (2 * CUT)) : 1;
        return <div key={s.id} style={{ position: "absolute", inset: 0, zIndex: i, opacity }}>{body}</div>;
      })}
      <Audio src={staticFile("soundtrack.mp3")} />
    </AbsoluteFill>
  );
};
