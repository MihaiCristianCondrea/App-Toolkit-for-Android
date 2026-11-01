---
name: motion-video
description: >
  Plan, choreograph, sound and render code-built product videos (promos, app
  trailers, shorts) with Remotion, React, Python audio synthesis and one
  timeline.json, in the Pixel and Material You motion style. Use for any new
  or revised marketing video, from the beat sheet to the final 2K master.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-28'
  keywords:
  - remotion
  - video-promo
  - app-trailer
  - motion-design
  - pixel-motion
  - material-you
  - kinetic-typography
  - python-audio
---

# Motion video

Everything is code: React for the picture, Python for the sound, and one JSON timeline that keeps them in sync. The style is the Pixel Feature Drop and Material You motion language. Copy in the film follows [`writing-style`](../writing-style/SKILL.md).

Where this skill sits in a campaign: [`marketing-startup`](../marketing-startup/SKILL.md) sets up the campaign folder, [`marketing-ui`](../marketing-ui/SKILL.md) provides the app's screens, this skill makes the film, [`metadata`](../metadata/SKILL.md) tags every delivered file, and [`social-captions`](../social-captions/SKILL.md) writes the post text.

| Read | When |
| --- | --- |
| [`references/pixel-style.md`](references/pixel-style.md) | Choosing the look: shape play, kinetic type, layouts, palettes, transitions |
| [`references/index.md`](references/index.md) | Finding a reference moment: films, stills and storyboards by timestamp |
| [`references/technical-rules.md`](references/technical-rules.md) | Before building scenes and before the master: fidelity gate, choreography and rendering rules |
| [`references/sound.md`](references/sound.md) | Writing the soundtrack and SFX |
| [`references/review.md`](references/review.md) | Reviewing cuts and rendering the delivery |

## 1. Install once

| Tool | Why | Check |
| --- | --- | --- |
| **Node.js 20+** (22 recommended) | Remotion and the Studio | `node -v` |
| **Python 3.10+** with `numpy scipy pillow` | Soundtrack synthesis and review helpers | `pip install numpy scipy pillow` |
| **ffmpeg** (optional) | Storyboards of reference films, loudness checks | `ffmpeg -version` |

Remotion downloads its own headless browser and ships its own ffmpeg (`npx remotion ffmpeg ...`).

Install Remotion's official agent skills, so the agent follows current Remotion APIs instead of guessing:

```bash
npx skills add remotion-dev/skills
```

## 2. Start a new film

Every film lives in its campaign: `marketing/creative/social-campaigns/<campaign-slug>/source/` (see [`marketing-startup`](../marketing-startup/SKILL.md)). Published films move to `creative/social-campaigns/completed/` and are a record: don't re-render or edit them unless the owner asks. Start each new film in a new campaign, from a copy of `marketing/creative/templates/ui-motion-starter/` when the repository has one (its `LitScreen` already mounts the kit's screens frame by frame).

Every film renders at 2K and 90 fps: lay out at 1920x1080, scale with `Config.setScale()` in `remotion.config.ts`, set `fps={90}` in `Root.tsx`, and write all timing in seconds (frames = seconds x fps), so a 60 fps delivery cut is the same film.

**Option A: copy a finished film.** Copy a completed campaign's `source/` into the new campaign, delete `out/`, and work only in the copy. An older film may keep its beats in frames; move them to seconds before rendering.

**Option B: fresh scaffold.**

```bash
npx create-video@latest --yes --blank --no-tailwind source
cd source && npm i
npx remotion add @remotion/fonts @remotion/media @remotion/layout-utils
```

> The blank template may still pull in Tailwind. If you don't use it, remove `@remotion/tailwind-v4`, `tailwindcss` and the `enableTailwind` line in `remotion.config.ts`.

## 3. Project anatomy

```
src/
  timeline.json        ← every beat, in seconds (frames = seconds × fps). Single source of truth for picture AND sound
  theme.ts             ← brand tokens: colours, font family, content data (categories, sizes, …)
  Root.tsx             ← registers the full promo + each scene as its own composition
  Promo.tsx            ← backdrop + scenes as overlapping <Sequence>s + <Audio>
  lib/anim.ts          ← ramp(), sp() (spring), drift() noise, easings, deterministic rnd()
  lib/project.ts       ← CSS-3D rig transform + matching projection math (for handoffs)
  components/
    Kinetic.tsx        ← FontGate (wait for font), MaskedWords (masked, spring-in type), vf() axes
    Logo.tsx           ← brand logo as separately animatable SVG parts + particle targets
    Backdrop.tsx       ← continuous procedural blobs / brand line behind every scene
    Phone.tsx, Photo.tsx, Icon.tsx, Gauge.tsx, CategoryTile.tsx   ← product-specific UI
  scenes/*.tsx         ← one file per scene; each reads absolute frames from timeline.json
scripts/
  soundtrack.py        ← original score + SFX synthesised from timeline.json
  copy-font.mjs        ← puts the brand font in public/
  contact_sheet.py     ← stills → one labelled review image
  inspect_audio.py     ← spectrogram + loudness report (for checking a mix without speakers)
```

**To re-brand for another product**, work through these in order:

1. **`theme.ts`**: set the palette from the app's real theme and screenshots, and point `loadFont` at the brand font in `public/`.
2. **`Logo.tsx`**: paste the logo's SVG or Android-vector `pathData`, one entry per coloured part, each with an `origin` to rotate and scale around.
3. **Product UI components**: rebuild the key screens as real React/CSS (not screenshots), so the elements can animate, split apart and leave the device.
4. **`timeline.json` + scenes**: write the story beats first (see §4), then build each scene against them.
5. **`soundtrack.py`**: change the `CHORDS`/`BASS` harmony and remap the SFX calls to your event names.

## 4. The creative method

### Establish the motion system before polishing scenes

Public Pixel and Made by Google production case studies repeatedly show the same useful pattern: establish the visual and motion language, work against music early, test ideas in rough form, then polish the film.

For a flagship trailer, do this before building finished scenes:

1. **Define the central visual concept.** Reduce the whole film to one transformation or metaphor.
2. **Define 4 to 6 motion rules.** Decide how type behaves, how shapes morph, how the camera moves, what easing feels like, and how scenes hand objects to each other.
3. **Choose the music grid.** Establish BPM and the major hits before detailed animation.
4. **Write the timed beat sheet.** Every scene and transition gets a duration and purpose.
5. **Make style frames.** Produce one representative frame for every major beat.
6. **Build a boardomatic or rough animatic.** Time those frames against the music and validate pacing before spending effort on polish.
7. **Review the transitions.** Ask whether each scene naturally hands an object, word, shape, or UI element to the next.
8. **Only then build the final scenes.**

The goal is a coherent campaign system, not a collection of individually nice shots.

### Feature behavior first

A feature should be understandable from its visual behavior before the viewer reads a technical explanation. Start with the product truth, then exaggerate the motion around it.

```text
real feature behavior
  -> one clear visual metaphor
  -> expressive motion
  -> object-driven transition
```

The useful target is **realistic UI journey + embellished motion**. Keep the UI, data model, states, and claims truthful. Let cards, photos, rings, words, and other UI pieces leave the device or gain depth when it makes the feature easier to understand.

### Build the whole frame

Watch for the easy composition:

```text
headline

phone

decorative background
```

It can work as a quiet beat, but it should not become the film's default grammar. Prefer frames where typography, UI, shapes, depth, and transitions all participate in communicating the feature.

### Scene-building rules

1. **Research first.** Read the app's README, theme, launcher icon, fonts and store screenshots. Use them as the design language, not as footage to animate.
2. **One core metaphor.** For a storage cleaner it might be "clutter becoming space": heavy, dense, dark, then light, spacious, organised. Every scene expresses it.
3. **Beat sheet on a musical grid.** Pick a BPM (120 BPM is a beat every 0.5 s: 45 frames at 90 fps). Put scene changes and big hits on downbeats, and list them in `timeline.json`.
4. **Transitions caused by objects.** No crossfades. Examples:
   - A headline's full stop becomes a shockwave that reveals the next world.
   - A storage ring's segments peel off into floating cards.
   - The camera dives into one card, which becomes the next page's header (a match cut).
   - A meter bursts into photos.
   - Swiped cards reveal text behind their trailing edge.
   - The leftover clutter spirals into the logo.
5. **Kinetic type with variable axes.** Animate `wdth`/`wght` through `fontVariationSettings`, so words can be cramped, then "make room", or widen to say "more space". Words rise through masks with springs.
6. **Depth.** Build one CSS-3D rig per scene (`perspective` + `transform-style: preserve-3d`) and give it a continuous drift or orbit. Spread elements across z for parallax, add fog and depth blur only at the extremes, and let foreground elements pass in front of text.
7. **Determinism.** Only use `useCurrentFrame()`, `interpolate`, `spring` and `random(seed)`. Never `Math.random`, CSS transitions or timers.

## 5. Use the app's real UI

Never rebuild app screens inside a video project. Import them from the project's UI kit (`marketing/ui`, built with the [`marketing-ui`](../marketing-ui/SKILL.md) skill), where every Compose screen is translated to HTML with Material Web and renders from a state object:

```tsx
<LitScreen id="gallery-cleaner" state={{ drag: interpolate(frame, [0, 20], [0, -1]) }} />
```

When a video needs a screen the kit doesn't have yet, add it to the kit first, so the next video gets it for free.

A film doesn't need whole screens to tell its story. A film can tell it only with single pieces rendered by `mountPart()`: a Quick Scan card, a row, an arc, a grid card, a metadata card. The whole screen appears once, on the end card, where it presents the product rather than carrying a beat: in the kit's device frame (`mount(el, id, state, { frame: true })`), or mounted bare inside a real device skin from `marketing/assets/device-frames` (body, display clipped to its corner radius with the screen at the display's zoom, then the foreground mask with the camera hole). When a piece isn't exposed yet, factor it out of its screen into a function the screen also calls, add it to `PARTS`, and check that the screen still renders pixel-identically.

Show a screen in a state the app would really show. The app hides a card with nothing to act on (an empty clipboard, no empty folders), so a Dashboard after a cleanup has fewer cards, not cards that read "0" or blank. When a state looks odd on screen, read the app's show conditions and fix the kit screen to match, rather than styling around it.

## 6. Production references

These public case studies are useful references for process and motion-system thinking:

- [Google Pixel Feature Drop, Buff Motion](https://www.buffmotion.com/projects/google-fd): kinetic typography, music-led editing, a boardomatic before final animation, and sound design as part of the piece.
- [Google Pixel 6 Launch, Buff Motion](https://www.buffmotion.com/projects/google-pixel-6): R&D before production, feature-led type behavior, and a balance between realistic user journeys and embellished UI motion.
- [Google Pixel Feature Drop Animation, Edelman Maker Studio](https://www.behance.net/gallery/136871337/Google-Pixel-Feature-Drop-Animation): shared timing rules, pronounced easing, and shape exploration to make separate features feel like one animated universe.
- [Google Pixel 4, BUCK](https://www.buck.co/work/google-pixel-4): a design exploration developed into a reusable visual system that scaled from 2D into 3D and physical media.
- [Made by Google 2024, Modicum](https://www.modicum.agency/work/made-by-google-24): a custom motion language built around expressive typography and then scaled across the wider launch.

Use these to study process, not to imitate Google branding. The films themselves, with timestamped stills and storyboards, are in [`references/`](references/index.md).

## 7. Finish

1. Run the review loop and the fidelity gate ([`references/review.md`](references/review.md), [`references/technical-rules.md`](references/technical-rules.md)).
2. Get the owner's approval on the animatic, then render the master and the delivery cut.
3. Write metadata into every output file with [`metadata`](../metadata/SKILL.md).
4. Write the post text with [`social-captions`](../social-captions/SKILL.md).

## 8. Prompt template for the next product

> Create a polished 15-second motion-design promo for **<PRODUCT>**, laid out at 1920×1080 and rendered at 2K (2560×1440) and 90 fps, with Remotion and its official skills, following the `motion-video` skill. First inspect **<repo/assets>** for the real UI, colours, icon and fonts. Core idea: **<metaphor>**. Headlines: **<lines>**. End on **<tagline>**. Before final animation, define the motion rules, build a timed beat sheet, make representative style frames, and assemble a rough boardomatic against the music. Then build the final scenes using feature-first storytelling, object-driven transitions, 2.5D depth, springs and kinetic type, with an original synthesised soundtrack synced through `timeline.json`. Review the stills and pacing strips, fix the weak spots, then render the MP4.
