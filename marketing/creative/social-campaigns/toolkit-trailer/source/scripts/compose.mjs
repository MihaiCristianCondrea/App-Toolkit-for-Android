// Composes the trailer's soundtrack (node scripts/compose.mjs) into ../soundtrack.mp3. Everything is synthesised
// here, from nothing but sine waves and seeded noise, so the music is ours and can be changed with the film.
// It reads src/timeline.json: the tempo and bars give the groove and the chords, and the beats place the sound
// of each motif on the frame it happens: drops for Water Eject's waves, ticks for the protractor's arm, prints
// for the receipt's lines, a ratchet that follows the wheel's real rotation, and a sparkle for the confetti.
//
// 120 BPM in F major, one chord per bar (F, C, Dm, Bb). The intro is pad and mallet; the five tools ride a
// four-on-the-floor groove; the wheel drops the kick and builds into the confetti; the end card resolves on F.
import { execFileSync } from "node:child_process";
import { readFileSync, rmSync, writeFileSync } from "node:fs";
import { fileURLToPath } from "node:url";

const tl = JSON.parse(readFileSync(new URL("../src/timeline.json", import.meta.url), "utf8"));
const B = tl.beats, RATE = 48000, BEAT = 60 / tl.tempo, BAR = 4 * BEAT, DUR = tl.durationSeconds, N = Math.ceil(DUR * RATE);
const scene = Object.fromEntries(tl.scenes.map((s) => [s.id, s]));
const L = new Float32Array(N), R = new Float32Array(N), SL = new Float32Array(N), SR = new Float32Array(N);

// ------------------------------------------------------------------------------------------------ helpers
let seed = 0x5eed;
const rand = () => ((seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0) / 4294967296);
const noise = () => rand() * 2 - 1;
const hz = (midi) => 440 * 2 ** ((midi - 69) / 12);
const TAU = 2 * Math.PI;

/** Adds a voice f(seconds since start) at `t0` for `len` seconds, panned (-1..1), with a reverb send. */
const voice = (t0, len, f, { gain = 1, pan = 0, send = 0, duck = false } = {}) => {
  const i0 = Math.round(t0 * RATE), n = Math.round(len * RATE);
  const gl = gain * Math.cos(((pan + 1) * Math.PI) / 4), gr = gain * Math.sin(((pan + 1) * Math.PI) / 4);
  for (let i = 0; i < n; i++) {
    const j = i0 + i;
    if (j < 0) continue;
    if (j >= N) break;
    const v = f(i / RATE) * (duck ? pump(j / RATE) : 1);
    L[j] += v * gl; R[j] += v * gr;
    if (send) { SL[j] += v * gl * send; SR[j] += v * gr * send; }
  }
};
/** A state-variable filter; returns a function that filters one sample and gives { lp, bp, hp }. */
const svf = () => {
  let low = 0, band = 0;
  return (x, cutoff, q = 0.7) => {
    const f = 2 * Math.sin((Math.PI * Math.min(cutoff, RATE / 6)) / RATE), high = x - low - band / q;
    band += f * high; low += f * band;
    return { lp: low, bp: band, hp: high };
  };
};
const env = (tau, attack, decay) => (tau < attack ? tau / attack : Math.exp(-(tau - attack) * decay));

// Where the groove plays: the kick ducks the pad and bass (a light sidechain pump).
const grooving = (t) => (t >= scene.water.start && t < scene.wheel.start) || (t >= B.wheelBurst && t < scene.end.start);
const pump = (t) => (grooving(t) ? 1 - 0.45 * Math.exp(-((t % BEAT) / 0.09)) : 1);

// ------------------------------------------------------------------------------------------------ instruments
const kick = (t, gain = 0.9) => voice(t, 0.5, (u) => {
  const phase = TAU * (48 * u + (120 * (1 - Math.exp(-u * 32))) / 32);
  return Math.sin(phase) * Math.exp(-u * 6.5) + noise() * Math.exp(-u * 400) * 0.25;
}, { gain });

const clap = (t, gain = 0.3) => { const f = svf(); voice(t, 0.3, (u) => {
  const bursts = [0, 0.011, 0.022].reduce((a, o) => a + (u >= o ? Math.exp(-(u - o) * (o < 0.02 ? 180 : 22)) : 0), 0);
  return f(noise(), 1500, 1.4).bp * bursts * 1.6;
}, { gain, send: 0.25 }); };

const hat = (t, gain = 0.08, pan = 0.25, open = false) => { const f = svf(); voice(t, open ? 0.3 : 0.08, (u) => f(noise(), 9000, 0.8).hp * Math.exp(-u * (open ? 14 : 60)), { gain, pan }); };

/** Additive saw (band-limited), `harmonics` partials rolled off by `bright`. */
const saw = (freq, u, harmonics = 10, bright = 0.25) => {
  let v = 0;
  for (let k = 1; k <= harmonics && k * freq < 16000; k++) v += (Math.sin(TAU * k * freq * u) / k) * Math.exp(-(k - 1) * bright);
  return v;
};

const bass = (t, midi, len, gain = 0.32) => voice(t, len, (u) => {
  const e = Math.min(1, u / 0.004) * Math.exp(-u * 3) * Math.min(1, (len - u) / 0.02);
  return (0.7 * Math.sin(TAU * hz(midi) * u) + 0.35 * saw(hz(midi), u, 6, 0.6)) * e;
}, { gain, duck: true });

const pad = (t, notes, len, gain = 0.05) => notes.forEach((m, i) => [-1, 1].forEach((side) => voice(t, len + 0.6, (u) => {
  const e = Math.min(1, u / 0.35) * Math.min(1, Math.max(0, (len + 0.6 - u) / 0.6));
  return saw(hz(m) * 2 ** ((side * 7) / 1200), u, 8, 0.45) * e;
}, { gain, pan: side * (0.35 + i * 0.1), send: 0.5, duck: true })));

/** A soft FM mallet, the film's lead voice. */
const mallet = (t, midi, gain = 0.16, pan = 0, decay = 5) => voice(t, 1.4, (u) => {
  const f = hz(midi);
  return Math.sin(TAU * f * u + 1.6 * Math.exp(-u * 14) * Math.sin(TAU * 2 * f * u)) * env(u, 0.003, decay);
}, { gain, pan, send: 0.35 });

/** A bell (FM, inharmonic ratio), for the icon and the end. */
const bell = (t, midi, gain = 0.12, pan = 0, len = 3.5) => voice(t, len, (u) => {
  const f = hz(midi);
  return Math.sin(TAU * f * u + 2.2 * Math.exp(-u * 3) * Math.sin(TAU * 3.5 * f * u)) * env(u, 0.002, 1.6);
}, { gain, pan, send: 0.55 });

/** A water drop: a sine that sweeps up as it rings. */
const drop = (t, from, to, gain = 0.22, pan = 0) => voice(t, 0.25, (u) => {
  const k = 1 - Math.exp(-u * 40), phase = TAU * (from * u + ((to - from) * (u - k / 40)));
  return Math.sin(phase) * env(u, 0.002, 22);
}, { gain, pan, send: 0.4 });

const tick = (t, gain = 0.14, freq = 2200, pan = 0) => { const f = svf(); voice(t, 0.06, (u) => (Math.sin(TAU * freq * u) * 0.6 + f(noise(), freq * 1.5, 3).bp) * Math.exp(-u * 120), { gain, pan, send: 0.15 }); };

/** Filtered noise rising into a downbeat; the shape wipes open on it. */
const riser = (end, len = 0.75, gain = 0.12) => { const f = svf(); voice(end - len, len + 0.12, (u) => {
  const p = Math.min(1, u / len), tail = u > len ? Math.exp(-(u - len) * 40) : 1;
  return f(noise(), 300 + 7000 * p * p, 1.2).bp * p * p * tail;
}, { gain, send: 0.3 }); };

const crash = (t, gain = 0.1) => { const f = svf(); voice(t, 2.2, (u) => f(noise(), 6000, 0.6).hp * Math.exp(-u * 2.2), { gain, send: 0.4 }); };

// ------------------------------------------------------------------------------------------------ harmony
const CHORDS = [
  { root: 41, pad: [53, 57, 60, 64], lead: [77, 72, 74, 72, 69] }, // Fmaj7
  { root: 36, pad: [52, 55, 60, 62], lead: [79, 76, 74, 72, 74] }, // Cadd9
  { root: 38, pad: [50, 53, 57, 60], lead: [74, 77, 81, 77, 74] }, // Dm7
  { root: 34, pad: [53, 57, 58, 62], lead: [74, 72, 70, 72, 77] }, // Bbmaj7
];
const bars = Math.round(DUR / BAR), endBar = Math.round(scene.end.start / BAR);
const chordAt = (bar) => (bar >= endBar ? CHORDS[0] : CHORDS[bar % 4]);
const LEAD_RHYTHM = [0, 3, 4, 6, 7]; // in eighths

for (let bar = 0; bar < bars; bar++) {
  const t0 = bar * BAR, c = chordAt(bar);
  if (bar < endBar) pad(t0, c.pad, BAR);
  for (let e = 0; e < 8; e++) {
    const t = t0 + (e * BEAT) / 2;
    if (grooving(t)) {
      if (e % 2 === 0) kick(t);
      if (e === 2 || e === 6) clap(t);
      hat(t, e % 2 ? 0.09 : 0.05, e % 4 < 2 ? 0.3 : -0.3, e === 7);
      bass(t, c.root + (e % 4 === 3 ? 12 : 0), BEAT / 2 - 0.01);
      const n = LEAD_RHYTHM.indexOf(e);
      if (n >= 0 && t >= scene.ruler.start) mallet(t, c.lead[n], 0.07, n % 2 ? 0.4 : -0.4);
    }
  }
}

// ------------------------------------------------------------------------------------------------ the film's moments
// Intro: one mallet note per arriving shape, rising through F major; a bell for the icon; the name lands.
[65, 69, 72, 76, 77].forEach((m, i) => mallet(B.introShapes[i], m, 0.2, (i - 2) * 0.3, 3.5));
bell(B.icon, 77, 0.16); bell(B.icon, 84, 0.07, 0.3);
mallet(B.title, 72, 0.12); mallet(B.tagline, 74, 0.1);
for (let e = 0; e < 8; e++) hat(B.icon + (e * BEAT) / 2, 0.03 + e * 0.006, e % 2 ? 0.3 : -0.3);

// Every scene opens on a riser into its downbeat and a crash as the shape wipes.
for (const s of tl.scenes.slice(1)) { riser(s.start); if (s.id !== "wheel") crash(s.start, s.id === "end" ? 0.14 : 0.06); }

// Water Eject: a drop on every wave, from each speaker side.
B.waterWaves.forEach((t, i) => drop(t, 380 + (i % 3) * 90, 1300 + (i % 4) * 160, 0.2, i % 2 ? 0.45 : -0.45));

// Ruler and Protractor: ticks as the ruler draws its scale, a click on each sweep of the arm.
for (let k = 0; k < 14; k++) tick(B.rulerSlide + 0.2 + k * 0.065, 0.05, 3000, -0.5 + k / 14);
B.protractorSweep.forEach((t, i) => { tick(t, 0.16, 1800 + i * 200); mallet(t, [72, 74, 77, 81][i], 0.1, 0.2); });

// Unit Converter: the count runs up in ticks, the swap whooshes, the temperature lands on a mallet pair.
for (let k = 0; k < 10; k++) tick(B.unitCount[0] + (k * (B.unitCount[1] - B.unitCount[0])) / 10, 0.05, 2400 + k * 120, 0.3);
riser(B.unitSwap + 0.3, 0.45, 0.08); mallet(B.unitSwap + 0.3, 81, 0.12, -0.3);
mallet(B.unitTemperature, 77, 0.12, -0.2); mallet(B.unitTemperature + 0.12, 84, 0.1, 0.2);

// Tip and Bill Splitter: the receipt prints each line; the share lands on a bell.
B.billLines.forEach((t, i) => { for (let k = 0; k < 4; k++) tick(t + k * 0.035, 0.05, 1400, 0.4); if (i === 3) bell(t, 81, 0.1, 0.2, 2); });

// Decision Wheel: a ratchet click each time a segment passes the pointer, following the wheel's own easing
// (Trailer.tsx: spin = 1 - (1 - p)^3 over 1530 degrees), a snare build, then the confetti.
{
  const [s0, s1] = B.wheelSpin, rot = (t) => (1 - (1 - Math.min(1, Math.max(0, (t - s0) / (s1 - s0)))) ** 3) * 1530 + 15;
  let last = Math.floor(rot(s0) / 60);
  for (let t = s0; t < s1; t += 1 / RATE * 48) { const seg = Math.floor(rot(t) / 60); if (seg !== last) { tick(t, 0.13, 1600, -0.2); last = seg; } }
  for (let k = 0; k < 16; k++) { const t = s0 + BAR / 2 + (k * (s1 - s0 - BAR / 2)) / 16; clap(t, 0.04 + k * 0.012); }
  for (let k = 0; k < (s1 - s0) / (BEAT / 4); k++) hat(s0 + (k * BEAT) / 4, 0.03 + 0.07 * (k / ((s1 - s0) / (BEAT / 4))), k % 2 ? 0.35 : -0.35);
  bass(s0, 38, s1 - s0, 0.18);
  riser(s1, 1.6, 0.14);
  kick(s1, 1); crash(s1, 0.16);
  // Confetti: a sparkle of mallets falling through the pentatonic.
  [96, 93, 91, 89, 86, 84, 81, 79, 77].forEach((m, i) => mallet(s1 + 0.03 + i * 0.05 + rand() * 0.02, m, 0.07, rand() * 1.4 - 0.7, 9));
  bell(B.wheelPick, 77, 0.12, -0.2, 2.5);
}

// Montage: a mallet per returning tile, a pop per chip.
B.montageTiles.forEach((t, i) => mallet(t, [65, 69, 72, 77, 81][i], 0.14, (i - 2) * 0.35));
B.montageChips.forEach((t, i) => [0, 0.12].forEach((d, k) => drop(t + d, 500 + i * 60, 1100 + i * 80 + k * 200, 0.08, k ? 0.5 : -0.5)));

// End: the groove stops on the downbeat; F major in bells over a low F, a ripple of drops, and the name.
{
  const t = scene.end.start;
  kick(t, 1); bass(t, 29, 3.4, 0.3); pad(t, [53, 57, 60, 65, 69], DUR - t - 0.6, 0.06);
  [65, 69, 72, 77].forEach((m, i) => bell(t + i * 0.02, m, 0.1, (i - 1.5) * 0.4));
  B.endRings.forEach((r, i) => drop(r, 420 + i * 70, 1400 + i * 140, 0.14, (i - 2) * 0.35));
  [96, 91, 89, 84, 81, 77].forEach((m, i) => mallet(t + 0.04 + i * 0.06, m, 0.05, rand() * 1.4 - 0.7, 9));
  mallet(B.endTitle, 77, 0.14); mallet(B.endPlay, 81, 0.12); mallet(B.endPlay + BEAT, 84, 0.1);
}

// ------------------------------------------------------------------------------------------------ mix
// A small Schroeder reverb on the sends.
const reverb = (input, offset) => {
  const out = new Float32Array(N);
  for (const [d, g] of [[1557, 0.82], [1617, 0.81], [1491, 0.83], [1422, 0.8]]) {
    const n = Math.round((d + offset) * 1.088), buf = new Float32Array(n);
    let k = 0, lp = 0;
    for (let i = 0; i < N; i++) { const y = buf[k]; lp = y * 0.7 + lp * 0.3; buf[k] = input[i] + lp * g; out[i] += y * 0.25; k = (k + 1) % n; }
  }
  for (const d of [556, 441]) {
    const n = Math.round((d + offset) * 1.088), buf = new Float32Array(n);
    let k = 0;
    for (let i = 0; i < N; i++) { const b = buf[k], y = -out[i] + b; buf[k] = out[i] + b * 0.5; out[i] = y; k = (k + 1) % n; }
  }
  return out;
};
const [wl, wr] = [reverb(SL, 0), reverb(SR, 23)];
let peak = 0;
for (let i = 0; i < N; i++) {
  const t = i / RATE, fade = Math.min(1, t / 0.01, (DUR - t) / 1.6);
  L[i] = Math.tanh((L[i] + wl[i] * 0.32) * 1.1) * fade; R[i] = Math.tanh((R[i] + wr[i] * 0.32) * 1.1) * fade;
  peak = Math.max(peak, Math.abs(L[i]), Math.abs(R[i]));
}
const k = 10 ** (-1 / 20) / peak, pcm = Buffer.alloc(44 + N * 4);
pcm.write("RIFF", 0); pcm.writeUInt32LE(36 + N * 4, 4); pcm.write("WAVEfmt ", 8); pcm.writeUInt32LE(16, 16);
pcm.writeUInt16LE(1, 20); pcm.writeUInt16LE(2, 22); pcm.writeUInt32LE(RATE, 24); pcm.writeUInt32LE(RATE * 4, 28);
pcm.writeUInt16LE(4, 32); pcm.writeUInt16LE(16, 34); pcm.write("data", 36); pcm.writeUInt32LE(N * 4, 40);
for (let i = 0; i < N; i++) { pcm.writeInt16LE(Math.round(L[i] * k * 32767), 44 + i * 4); pcm.writeInt16LE(Math.round(R[i] * k * 32767), 46 + i * 4); }

const wav = fileURLToPath(new URL("../../soundtrack.wav", import.meta.url)), mp3 = fileURLToPath(new URL("../../soundtrack.mp3", import.meta.url));
writeFileSync(wav, pcm);
execFileSync("ffmpeg", ["-hide_banner", "-loglevel", "error", "-y", "-i", wav, "-codec:a", "libmp3lame", "-b:a", "320k", "-metadata", "title=App Toolkit trailer", mp3]);
rmSync(wav);
console.log(`${mp3}: ${DUR} s, ${tl.tempo} BPM, peak -1 dBFS`);
