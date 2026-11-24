# Review and render

Part of the [`motion-video`](../SKILL.md) skill.

## Review loop (don't settle for the first cut)

```bash
# quick stills around a transition
npx remotion render Promo out/stills --frames=400,410,420,430 --image-format=jpeg --muted
python3 scripts/contact_sheet.py out/stills out/sheet.jpg

# example pacing strip for a 15 s, 30 fps composition at 20% size
# adapt the end frame and step to the composition's duration and frame rate
npx remotion render Promo out/strip --frames=$(python3 -c "print(','.join(map(str,range(0,450,5))))") --scale=0.2 --image-format=jpeg --muted
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

Use [Remotion Render](../../../official/remotion/remotion-render/SKILL.md) for the render command and encoder options. If the renderer cannot download Chromium in a sandbox, pass `--browser-executable` with an installed Chromium headless shell.

Inspect the encoded file's dimensions, frame rate, duration and audio track. Review it at delivery speed, including scene boundaries, audio sync, caption and text legibility, and crops across each aspect ratio. Return to [Motion Video's finish steps](../SKILL.md#7-finish) for publication assets.