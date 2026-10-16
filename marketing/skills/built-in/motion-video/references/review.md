# Review and render

Part of the [`motion-video`](../SKILL.md) skill.

## Review loop (don't settle for the first cut)

```bash
# quick stills around a transition
npx remotion render Promo out/stills --frames=400,410,420,430 --image-format=jpeg --muted
python3 scripts/contact_sheet.py out/stills out/sheet.jpg

# whole-film pacing strip: every 15th frame (6 a second at 90 fps) at 20 % size
npx remotion render Promo out/strip --frames=$(python3 -c "print(','.join(map(str,range(0,1350,15))))") --scale=0.2 --image-format=jpeg --muted
python3 scripts/contact_sheet.py out/strip out/strip.jpg 6

npm run dev   # Remotion Studio: scrub the timeline, preview each scene composition on its own
```

Look for these problems:
- A scene with more objects than the one before it, when the film is about making space. Count them.
- A static layout, such as a phone sitting beside text.
- A repeated headline + centered phone + decorative background composition.
- A feature that is only visible because the viewer already knows what the UI means.
- Typography that decorates rather than participates in the idea.
- A transition that could be replaced with a crossfade without changing the story.
- A scene where nothing on screen naturally motivates the next scene.
- Dead pauses longer than about 0.5 s.
- Elements clipped at the frame edge.
- Text overlapped by moving objects.
- Repeated procedural assets.
- Jumps at scene handoffs.

Fix them, re-render the strip, and repeat. Then run the fidelity gate in [`technical-rules.md`](technical-rules.md).

## Final render

```bash
npm run render
# = remotion render Promo out/promo-1440p90.mp4 --codec=h264 --crf=16 --audio-codec=aac --audio-bitrate=320k
#   at 2K and 90 fps: Config.setScale(4 / 3) in remotion.config.ts, fps={90} in Root.tsx
```

A 15 s promo, 900 frames at 1080p60, took about 85 s on a 4-core machine. The same 15 s at 2K and 90 fps is 1,350 frames with 1.8 times the pixels, so expect about two and a half to three times as long. Where Remotion can't download its browser (a sandbox), pass `--browser-executable` pointing at an installed Chromium headless shell. For a vertical social cut, register a second `<Composition>` at 1080×1920 (rendered at 1440×2560) and adapt the layouts. The timeline, sound and components carry over unchanged.

After the render, write the metadata into every delivered file with the [`metadata`](../../metadata/SKILL.md) skill, and the post text with [`social-captions`](../../social-captions/SKILL.md).
