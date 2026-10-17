# Technical rules

Part of the [`motion-video`](../SKILL.md) skill. Each rule here was learned on a real film. Read the whole list before building scenes and again before the final render.

## Fidelity: the film shows the app as it is

Animation cannot fix an inaccurate picture of the app. Before any scene is animated, and again before the master render, the film passes this gate.

1. **Static parity.** Put the UI kit's screens (the ones the film mounts) next to the app's own renders (Compose previews, Robolectric or screenshot tests) and its sources. Make one sheet per screen, app left, kit right. Fix the kit, not the film.
2. **Geometry check.** Render frames with a QA flag. The film reports where it drew each cue (a ring, a finger, a container start, a flying chip) and the measured box of the control the cue belongs to. A script compares them within half a pixel and checks that text stays inside its container at sampled frames. A frame-difference check finds jumps; only a geometry check finds a dot in the wrong place that moves smoothly, or a doubled sentence that holds still.
3. **Low-resolution animatic** at full playback speed (540p is enough), reviewed for readability, camera, focal point and rhythm.
4. **Approval.** The owner approves the creative direction. Only then render the master and the delivery, and run the geometry and motion checks again on the delivery.

Keep the result in a `FIDELITY.md` beside the film: an acceptance table (requirement, how it is met, what verifies it) and the known deviations with their reasons.

More fidelity rules:

- **Interactions happen on their control.** A tap, ring or container transform starts from the measured box of the button in the frame it is drawn, after scrolling and layout. Never type coordinates in.
- **Text stays inside its container at every frame.** Lay a typed answer out at full size from the first character and reveal it by hiding the rest; wrap long words the way the platform does (`overflow-wrap: anywhere`).
- **Show real content and the real state.** Use the app's real data, in a state the app would really show. When a screen looks odd, read the app's show conditions and fix the kit screen.
- **Use the real device.** Mount the screen inside an official device skin (Android Studio's skins: body, display aperture, corner radius, camera cut-out), laid out at the display's exact aperture with no scaling of the UI. Derive every number from the skin's `layout` file.
- **Third-party artwork needs a rights decision** before release. Download it at build time, never commit it, and keep one place in the code that chooses what is shown.

## Choreography

- **Every transition needs a stated purpose.** Smooth animation alone does not justify a transformation. A change of ground that goes light, dark, light again tells the viewer nothing; cut it.
- **One foreground action at a time.** A ground never changes while the UI does. Let the tab switch or the container transform finish, then move the background together with the camera, as one gesture.
- **A ground grows from the control that caused the change.** The next scene's background expands from the tab just tapped or the button just pressed, starting behind the device so it emerges around it.
- **Headlines hold.** Each headline stays at least 1.5 s after its last word settles, with nothing else moving and nothing over it.
- **Settle before you lift.** An element finishes its landing before the next move starts on it. A move that has to land exactly uses an exact easing curve (a cubic Bezier or a critically damped spring), not an underdamped one that overshoots the target.
- **Shadows move with their object.** A shadow that trails behind reads as lag. Compute it from the same transform in the same frame.
- **Long scrolls ease in and out and carry motion blur.** A fling that starts and stops at full speed looks mechanical; a fast scroll without blur strobes.
- **Per-letter springs fade their tail to exact rest.** A spring's last sub-pixel wobble reads as shaking on still type. Blend each letter's spring into its rest pose over the last few frames.
- **Diagnose shake before changing easing.** Track the element's position across frames of the render. Shake usually comes from pixel snapping, a camera with noise on it, or two motions fighting, not from the curve.
- **An entry is a gesture, not a slide.** The device enters with a spring on position, a small rotation that settles, and scale, timed to a beat.

## Rendering and layout

- **Crisp zooms.** Chrome rasterises 3D-transformed layers at their original size, so a 10× `scale()` goes blurry. For hero zooms, lift the element out of the rig into screen space and grow its *dimensions*  instead of scaling it.
- **Seamless handoffs between scenes.** Compute where an element is on screen (`projectPoint` in `lib/project.ts`, which mirrors the CSS rig order translate → rotateX → rotateY → scale). Then start the next scene's element at exactly that position, size and rotation. Unmount the old one on the same frame.
- **Measure text only after the font loads.** Wrap scenes in `<FontGate>` and then use `measureText` from `@remotion/layout-utils`.
- **Measure variable-axis type in the DOM.** Canvas measurement ignores `font-variation-settings`, and `wdth` changes a word's width a lot. Measure a hidden span at the current axes (cached per text and axes), and render scenes only once the font has loaded so the first frame is measured in the real font. This is what lets a word be split by a button, aligned to a card's edge, or fitted to the frame.
- **Keep 3D rotations under about ±70°,** or backfaces show mirrored text. Give story props (warnings, key cards) small, slow rotations.
- **Avoid `filter` on a `preserve-3d` container.** It flattens the 3D.
- **Watch overlapping sequences.** An element from the previous scene can bleed through while the next scene's background fades in.
- **Check every mount boundary.** Handoffs break where one component unmounts and the next mounts. Extract the frame before and after each boundary from the encoded file and compare them.
- **A crop on a part's box also clips the part's own transforms.** A swipe card moves itself by its drag inside its box; clip it only once it is back inside.
- **Set the stacking order when objects converge.** When many pieces land on one spot, give them a z-index in landing order. DOM order is the layout's order, not the story's.
- **A morph that fades both ends leaves a hole.** Carry one visible element through it (the arc's tip, a number, a card edge).
- **`<Freeze>` clamps to its composition's duration.** Render style frames as stills of the film itself, rather than from a short composition that freezes it.
- **Position by transform, render at 2x.** Chrome snaps layout positions (`left`/`top`) and text to whole pixels, so slow motion steps a pixel at a time and reads as shaking. Move things with `transform: translate()`, render at twice the output size and downscale with Lanczos: the steps drop to about a third of a pixel. For a 2K master laid out at 1080p, that is `Config.setScale(8 / 3)`, downscaled to 2560×1440. Diagnose shake by tracking an element across frames of the render before changing any easing.
- **Stack objects with a skyline, not a physics engine.** Drop each outline onto what is already there (lowest supported rest, a few degrees and pixels of freedom), animate the fall with constant gravity plus a drift, a spin, one bounce and one rock. Nothing overlaps, nothing jitters, and it is deterministic.
- **Never teleport a push.** When type or a room pushes other pieces aside, spring the push over about 0.4 s and bring the type in at the same time; a push in a few frames reads as pieces jumping.
- **A `clip-path` also clips the element's own shadow.** Put the shadow on a parent and ramp it in as the clip opens.
- **Brand shapes as masks.** The project's brand shapes (`marketing/assets/shapes`) work as CSS `mask-image` (the files are black; only their alpha counts, and the colour comes from the theme); give non-square view boxes their aspect. To turn a round thumbnail into a cookie, open a circular clip over the cookie mask, from the shape's inner radius to past its corners.
- **Reserve the hero's room at layout time.** If clutter must frame something, keep a clear rectangle out of the layout instead of pushing pieces out at runtime: pushed pieces pile up on each other at the edge of the push.
- **A collage: place side by side, don't scatter.** Scattered pieces land on each other. Give each piece a turned-rectangle footprint and keep it clear of everything placed with a separating-axis test and a small gap; of many seeded tries, take the free place closest to the hero's room with a little randomness in the score, so it still looks random. Hand out names and icons from lists and leave a piece out rather than repeat one.
- **Per-glyph type has no kerning.** When each glyph is its own span, a glyph's place is the sum of the widths before it, not a share of the measured line; and a full stop sits on the baseline, below the middle of the line box. Find anything you attach to a glyph the same way the line is laid out.
- **Dash patterns repeat.** With `pathLength` 1, keep `stroke-dasharray` summing to 1 (`p, 1 - p` clamped), or a progress past 1 wraps and blanks part of the stroke.
- **A part that hands a piece of itself on must stop drawing it.** Give the kit part an option that keeps the piece's place empty (for example a `button: false` state), or the film shows two.
- **Measure brand shapes; don't approximate them.** Rasterise each SVG once and keep its outline and the centre and radius of its largest inner circle in a JSON file. Stack the real silhouettes, and put an icon on the inner circle's centre, not the box's: in a triangle the box centre looks off.
- **A pile on a rope: solve it on the loaded curve.** Start the skyline as the rope's curve under the full load, then move every item with the rope as it sags; the rest poses are exact once the last item is in, and nothing floats or sinks while it loads.
- **A masked line needs room for descenders.** When glyphs rise through a line's clip, pad the clip below the baseline (about a third of the size) or "g" and "p" lose their tails.
- **A translucent kit part needs its surface.** Some parts  are translucent on purpose; over other objects, give the part the surface it sits on in the app.

