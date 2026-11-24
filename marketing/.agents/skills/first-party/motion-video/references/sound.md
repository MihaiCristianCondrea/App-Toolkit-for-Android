# Sound

Part of the [`motion-video`](../SKILL.md) skill.

`scripts/soundtrack.py` reads `src/timeline.json` and writes `public/audio/soundtrack.wav` (48 kHz stereo) with:

- **Music:** detuned additive-saw pads, 8th-note bass, 16th-note pluck arpeggio, soft kick/clap/hat, and kick sidechain on the pads.
- **SFX:** ticks (a sine blip plus high-passed noise), swishes (noise through a swept state-variable filter, auto-panned), low hits (a pitch-dropping sine plus a thump), bells (inharmonic partials), and risers that cut about 35 ms before a drop for impact.
- **Sync:** per-object sounds are computed from the same maths as the visuals (e.g. one pluck per fragment landing on the ring, panned by its angle). When that maths lives in TypeScript, bundle the module with esbuild and write the frames to a JSON file the Python reads (`scripts/events.mjs` in the flagship trailer).
- **Master:** convolution reverb (a generated impulse response), gentle tanh saturation, fades, and normalisation to −2 to −2.4 dBFS. AAC encoding adds about 0.7 to 1 dB of true peak over the WAV's sample peak, so that lands at about −1 dBTP in the MP4 (measure it with ffmpeg's `ebur128=peak=true`, and encode the delivery's audio once, from the WAV). The shape to aim for is a quiet intro (about −20 dBFS RMS), hits around −13, and a feature bed around −17 to −18.

Regenerate after any timing change with `npm run audio`. Use `python3 scripts/inspect_audio.py` to check the loudness per section and that the hits line up with the event markers.

