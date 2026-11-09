// Reviews a rendered film by measurement, as a person would watch it for jumps and listen for late sounds
// (node scripts/playback.mjs [video], default output/video/app-toolkit-trailer-review.mp4).
//
// Picture: the mean change between consecutive frames over the whole film, at 480 x 270. A frame that changes
// more than 3.5 times as much as the frames around it, and by more than 1.5 grey levels, is a jump: something
// popped in, stepped or flashed. The planned hard cuts (scenes that come in by "cut") are expected; anything else
// fails. A slow fade of a pale shape still steps a grey level at a time in 8-bit video; that stays below the bar.
// Sound: the film's audio against the composed soundtrack, by cross-correlation, so a late or early mux shows as
// a lag in samples; and the loudness.
//
// Writes output/playback.json; exits 1 on an unplanned jump or a lag of a millisecond or more.
import { execFileSync, spawnSync } from "node:child_process";
import { readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = fileURLToPath(new URL("../", import.meta.url)), out = path.resolve(root, "../output");
const video = path.resolve(process.argv[2] ?? path.join(out, "video/app-toolkit-trailer-review.mp4"));
const tl = JSON.parse(await readFile(path.join(root, "src/timeline.json"), "utf8")), fps = tl.fps;
const run = (args, opts = {}) => execFileSync("ffmpeg", ["-hide_banner", "-loglevel", "error", ...args], { maxBuffer: 1 << 30, ...opts });

// ------------------------------------------------------------------------------------------------ picture
const W = 480, H = 270, px = W * H, raw = run(["-i", video, "-vf", `scale=${W}:${H}`, "-f", "rawvideo", "-pix_fmt", "gray", "-"]);
const frames = raw.length / px, diff = [];
for (let i = 0; i + 1 < frames; i++) {
  let s = 0; const a = i * px, b = (i + 1) * px;
  for (let k = 0; k < px; k++) s += Math.abs(raw[b + k] - raw[a + k]);
  diff.push(s / px);
}
const median = (xs) => { const s = [...xs].sort((x, y) => x - y); return s.length ? s[Math.floor(s.length / 2)] : 0; };
const cuts = tl.scenes.filter((s) => s.in === "cut").map((s) => s.start), jumps = [];
diff.forEach((d, i) => {
  const around = median([...diff.slice(Math.max(0, i - 6), i), ...diff.slice(i + 1, i + 7)]);
  if (d > 1.5 && d > 3.5 * Math.max(around, 0.2)) {
    const t = (i + 1) / fps;
    jumps.push({ seconds: Number(t.toFixed(3)), change: Number(d.toFixed(2)), around: Number(around.toFixed(2)), planned: cuts.some((c) => Math.abs(t - c) < 1.5 / fps) });
  }
});
const unplanned = jumps.filter((j) => !j.planned);
console.log(`${frames} frames; median change ${median(diff).toFixed(2)} grey levels a frame`);
jumps.forEach((j) => console.log(`  ${j.planned ? "cut " : "JUMP"} at ${j.seconds} s: ${j.change} against ${j.around} around it`));

// ------------------------------------------------------------------------------------------------ sound
const pcm = (file) => { const b = run(["-i", file, "-vn", "-ac", "1", "-ar", "48000", "-f", "f32le", "-"]); return new Float32Array(b.buffer, b.byteOffset, b.length / 4); };
const ref = pcm(path.resolve(root, "../soundtrack.mp3")), film = pcm(video), R = 48000, [w0, w1] = [Math.round(16.5 * R), Math.round(23.5 * R)];
const corr = (lag) => { let ab = 0, aa = 0, bb = 0; for (let i = w0; i < w1; i++) { const a = ref[i], b = film[i + lag] ?? 0; ab += a * b; aa += a * a; bb += b * b; } return ab / Math.sqrt(aa * bb); };
let best = { lag: 0, c: -1 };
for (let lag = -480; lag <= 4800; lag += 16) { const c = corr(lag); if (c > best.c) best = { lag, c }; }
for (let lag = best.lag - 16; lag <= best.lag + 16; lag++) { const c = corr(lag); if (c > best.c) best = { lag, c }; }
const ebur = spawnSync("ffmpeg", ["-hide_banner", "-i", video, "-af", "ebur128=peak=true", "-f", "null", "-"], { encoding: "utf8", maxBuffer: 1 << 26 }).stderr;
const lufs = Number([...ebur.matchAll(/I:\s+(-?[\d.]+) LUFS/g)].pop()?.[1]), peak = Number([...ebur.matchAll(/Peak:\s+(-?[\d.]+) dBFS/g)].pop()?.[1]);
console.log(`sound: ${best.lag} samples (${((1000 * best.lag) / R).toFixed(1)} ms) from the soundtrack, correlation ${best.c.toFixed(3)}; ${lufs} LUFS, true peak ${peak} dBFS`);

const ok = unplanned.length === 0 && Math.abs(best.lag) < R / 1000;
await writeFile(path.join(out, "playback.json"), JSON.stringify({ video: path.basename(video), frames, medianChange: median(diff), jumps, sound: { lagSamples: best.lag, lagMs: (1000 * best.lag) / R, correlation: best.c, lufs, truePeak: peak }, ok }, null, 2) + "\n");
console.log(ok ? "pass: no unplanned jumps, sound in sync" : "FAIL");
process.exit(ok ? 0 : 1);
