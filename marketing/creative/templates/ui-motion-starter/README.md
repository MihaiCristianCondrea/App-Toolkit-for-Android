# UI motion starter

A minimal Remotion project, copied from Smart Cleaner's, that animates the live HTML screens from [`marketing/ui`](../../../ui). Each frame computes a state (progress, scroll, swipe drag) and mounts the screen with it, so the video uses the real translated UI, not screenshots.

## Run

**Windows:** double-click `open-motion-studio.cmd` in this folder. It prepares the UI kit, installs Remotion on the first run, and opens Remotion Studio.

**By hand:**

```bash
cd ../../../ui && npm install && npm run assets   # the kit and its assets come first
cd ../creative/templates/ui-motion-starter && npm install
npm run dev       # Remotion Studio
npm run render    # out/ui-screens-demo.mp4, 2560x1440 at 90 fps
```

## Quality

New films start from here, so this project renders at the marketing standard ("Render quality" in [`marketing/README.md`](../../../README.md#render-quality)): 2K at 90 fps.

- The composition is laid out at 1920x1080 and rendered at 2560x1440 by `Config.setScale(4 / 3)` in `remotion.config.ts`. Frames are captured as PNG, so text and edges reach the encoder unblurred.
- `FPS` in `src/Root.tsx` is the only frame rate. `UiScreensDemo` times everything in seconds (`frame / fps`), so a different frame rate changes smoothness, not pacing. Keep that when you copy it.

## How it works

`src/LitScreen.tsx` is the whole bridge:

```tsx
<LitScreen id="numbers" state={{ activeSheet: "counter", counterValue: 24 + Math.floor(frame / 36) }} />
```

- `setup()` from `marketing/ui/src/embed.js` registers the fonts and Material Web, and points asset URLs at Remotion's `staticFile`.
- `mount(el, id, state)` renders the screen and resolves once every Material Web element has finished updating. `LitScreen` holds the frame with `delayRender` until then, so every captured frame is complete.

`src/UiScreensDemo.tsx` shows the pattern with three phones: the Compass sheet turns from 312° to 45°, the Counter sheet counts up one tap at a time, and the Tip & Bill Splitter adds a person to the split three times.

For a full promo, combine these screens with the techniques in the [`motion-video`](../../../skills/built-in/motion-video/SKILL.md) skill: camera rigs, object-driven transitions, and a synthesized soundtrack.
