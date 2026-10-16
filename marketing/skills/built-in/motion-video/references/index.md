# Pixel Motion Reference Index

Reference films and stills for the Pixel and Material You motion language described in
[`pixel-style.md`](pixel-style.md). A still is named
`<mm>-<ss>-<what-it-shows>.png` inside its film's folder, so it leads straight back to its moment in the film.

- `films/<film>.<ext>` the films themselves (reference only, never cut into our videos). App repositories that copy this skill may leave `films/` out to stay small; the films are in [android-skills](https://github.com/MihaiCristianCondrea/android-skills/tree/main/marketing/skills/built-in/motion-video/references/films).
- `stills/<film>/<mm>-<ss>-<slug>.png` full-resolution stills.
- `storyboards/<film>.jpg` one tile every 2 s, each labelled `m:ss`. Rebuild with
  `python3 ../scripts/storyboards.py` (options: `--every`, `--width`, `--ffmpeg`).

To look at a moment in motion, open the film at the still's timestamp, for example
`ffplay -ss 37 films/pixel-feature-drop-2022-03.mp4`, or pull the frames around it with
`ffmpeg -ss 36 -t 3 -i films/pixel-feature-drop-2022-03.mp4 -vf fps=10 /tmp/f_%03d.png`.

## Films

| Slug | File | Length | Picture | Subject |
|---|---|---|---|---|
| `pixel-feature-drop-2021-12` | `films/pixel-feature-drop-2021-12.mp4` | 1:41 | 1920×1080, 60 fps | December 2021 drop: Now Playing, Digital car key, Nearby Share, Quick Tap to Snap, Wallpaper and Style |
| `pixel-feature-drop-2022-03` | `films/pixel-feature-drop-2022-03.mp4` | 1:27 | 1920×1080, 60 fps | March 2022 drop: Night Sight in Snapchat, Live Caption for calls, battery widget, At a Glance, custom text stickers, Live Translate |
| `pixel-feature-drop-2022-06` | `films/pixel-feature-drop-2022-06.webm` | 1:36 | 2560×1440, 23.98 fps (VP9) | June 2022 drop: Pocket Operator, vaccine card, Car Crash Detection, At a Glance doorbell and flashlight, Live Translate, Pride wallpapers |
| `material-you-brand` | (film not in the repo; stills only) | 0:45 | 1920×1080 | Material You launch: shape clusters, clock widgets, pastel palettes |

## Stills

### material-you-brand

| Still | Time | What it shows |
|---|---|---|
| `stills/material-you-brand/00-05-pattern-lock-dot-grid.png` | 0:05 | Pattern-lock dot grid on butter yellow, a blue stroke trails into one dot |
| `stills/material-you-brand/00-10-floating-pill-toolbar.png` | 0:10 | Floating lilac pill toolbar, one filled square button and four line icons |
| `stills/material-you-brand/00-15-four-shape-icon-badges.png` | 0:15 | Four icon badges in four shapes (teardrop, scallop, 12-cookie, circle) in a 2×2 cluster |
| `stills/material-you-brand/00-20-cookie-clock-widget.png` | 0:20 | 12-sided cookie clock widget over a flower photo, tonal to the photo |
| `stills/material-you-brand/00-25-dark-home-screen-widgets.png` | 0:25 | Dark phone home screen with themed widgets, flush on a matching photo |
| `stills/material-you-brand/00-30-phone-on-sage-giant-clock.png` | 0:30 | Phone on sage with a soft same-tone drop shadow, giant clock digits |
| `stills/material-you-brand/00-35-phone-rising-from-bottom.png` | 0:35 | Phone rising from the bottom edge on terracotta, cropped by the frame |
| `stills/material-you-brand/00-40-phone-bleeding-off-edges.png` | 0:40 | Phone bleeding off the top and bottom edges, conversation widgets on a photo |

### pixel-feature-drop-2021-12

| Still | Time | What it shows |
|---|---|---|
| `stills/pixel-feature-drop-2021-12/00-11-footage-with-headline.png` | 0:11 | Live footage with a big white line over it ("Hears a song") |
| `stills/pixel-feature-drop-2021-12/00-22-footage-two-line-question.png` | 0:22 | Footage, two-line question set right of the subject ("Got tunes?") |
| `stills/pixel-feature-drop-2021-12/00-34-clover-badge-car-key.png` | 0:34 | 4-lobe clover badge with a line icon (car key), centred on denim |
| `stills/pixel-feature-drop-2021-12/00-45-tilted-squircle-badge.png` | 0:45 | Squircle badge tilted, crop-corners icon, on rose |
| `stills/pixel-feature-drop-2021-12/00-56-phone-between-two-words.png` | 0:56 | "Share · phone · easier": phone centred between two words, two phones leaning in from the corners |
| `stills/pixel-feature-drop-2021-12/01-07-cream-squircle-badge.png` | 1:07 | Big cream squircle badge with an icon, centred on apricot |
| `stills/pixel-feature-drop-2021-12/01-19-badge-shrinking-transition.png` | 1:19 | Tiny squircle mid-transition (a badge shrinking before the next scene) |
| `stills/pixel-feature-drop-2021-12/01-30-three-phones-fanned.png` | 1:30 | Three phones fanned, wallpapers, on pale pink |

### pixel-feature-drop-2022-03

| Still | Time | What it shows |
|---|---|---|
| `stills/pixel-feature-drop-2022-03/00-10-low-light-blob-entering.png` | 0:10 | "low-light", grey type on navy, one blob entering from the right edge |
| `stills/pixel-feature-drop-2022-03/00-19-can-see-googly-eyes.png` | 0:19 | "can see" with the "ee" turned into googly eyes (a letter becomes an object) |
| `stills/pixel-feature-drop-2022-03/00-29-phone-rising-from-below.png` | 0:29 | Phone keyboard close-up rising from below, blobs on pink |
| `stills/pixel-feature-drop-2022-03/00-39-stacked-title-badge-phone.png` | 0:39 | Pattern 2: stacked type left ("with a battery widget"), squircle badge under it, phone right |
| `stills/pixel-feature-drop-2022-03/00-48-single-word-corner-blobs.png` | 0:48 | One word ("your") on purple, corner blobs top-left and bottom-right |
| `stills/pixel-feature-drop-2022-03/00-58-push-in-on-stacked-type.png` | 0:58 | Close push-in on stacked type ("custom text stickers") with the phone cropped at right |
| `stills/pixel-feature-drop-2022-03/01-08-badge-title-pills.png` | 1:08 | Circle badge + title ("Recorder") with small pills under it, wavy blobs on both edges |
| `stills/pixel-feature-drop-2022-03/01-17-single-word-cropped-blobs.png` | 1:17 | One word ("better") on purple, blobs cropped in two corners |

### pixel-feature-drop-2022-06

| Still | Time | What it shows |
|---|---|---|
| `stills/pixel-feature-drop-2022-06/00-11-footage-with-headline.png` | 0:11 | Footage with a white line over it ("turns your videos") |
| `stills/pixel-feature-drop-2022-06/00-21-can-add-corner-blobs.png` | 0:21 | Pattern 1: "can add" on coral, a butter blob top-left, blush flower right, red blob bottom |
| `stills/pixel-feature-drop-2022-06/00-32-in-canada-huge-type.png` | 0:32 | "in Canada" huge on sage, pale blob top-right, butter and charcoal blobs at the bottom corners |
| `stills/pixel-feature-drop-2022-06/00-43-the-front-door-blobs.png` | 0:43 | "the front door" on blush, charcoal blob top-left, white blob bottom-left |
| `stills/pixel-feature-drop-2022-06/00-53-blob-wipe-transition.png` | 0:53 | Transition: big sage circle and pink scallop sweeping across butter, olive blob tumbling |
| `stills/pixel-feature-drop-2022-06/01-04-stacked-word-list.png` | 1:04 | A stacked word list ("English … Simplified Chinese") centred on lilac |
| `stills/pixel-feature-drop-2022-06/01-15-three-phones-blob-wipe.png` | 1:15 | Three phones fanned left, stacked title right, violet blob wipe crossing |
| `stills/pixel-feature-drop-2022-06/01-26-end-screen-shape-pile.png` | 1:26 | End screen: a pile of pastel shapes filling the left half on lilac |

## Moments worth studying (from full viewings)

Times are `m:ss` in the film. Storyboards show them at 2 s resolution.

### Shape rain and piles
- 2022-03 0:07 "have": pastel shapes scattered across the frame in front of and behind the word.
- 2022-03 0:08 "dropped": the shapes have fallen and piled along the bottom edge under the word.
- 2022-03 0:16 and 0:46: an icon badge centred over a pile of same-tone shapes filling the lower third.
- 2022-03 1:04 and 1:12: full-screen rain of tonal shapes (whole frame covered, then the next word).
- 2022-06 0:06 "dropped": multicolour shapes (red, sky, charcoal, blush) piled at the bottom edge.
- 2022-06 1:17 and 1:21 to 1:35: scattered shapes, then a dense heap of pastel shapes (end screen).
- 2021-12 0:03 to 0:04 and 1:34 to 1:36: shape and icon grids (feature recap).

### Type as the actor
- 2022-03 0:18 to 0:19 "can see" → the "ee" become googly eyes.
- 2022-03 0:37 "your [widget] battery": the UI widget sits inline in the sentence.
- 2022-03 0:41 "your l[lock icon]ck screen": a lock icon replaces the "o" of "lock".
- 2022-03 0:49 "more more are more more more": stacked repetition.
- 2022-03 0:26 "will say" in a speech bubble; 0:27 to 0:28 "what you type" typed on with a caret.
- 2022-06 0:12 "into [badge] music", 0:24 "home [badge] screen", 1:11 "can [badge] express": a badge between words.
- 2022-06 0:56 to 0:58 words inside chat bubbles ("chat", "in", "5 new").
- 2022-06 1:08 "celebrate" with confetti shapes around it.

### Badge + title + pills
- 2021-12 0:07 to 0:08 Now Playing, 0:33 to 0:35 Digital car key, 0:45 to 0:47 Nearby Share, 0:57 to 0:59 Quick Tap to Snap, 1:18 to 1:20 Wallpaper and Style: a clover/squircle badge pops in centred, then slides left beside its title.
- 2022-03 1:06 to 1:08 "Live Translate" / "Recorder": badge + title with a row of small pills (languages) underneath.
- 2022-06 0:30 to 0:31 "safe [badge]": the badge's icon changes state (car → car with alert).

### Phone staging
- 2022-03 0:14 to 0:15 "with Night Sight … in Snapchat": phone centred, words either side, badge low-left.
- 2022-03 0:38 to 0:39, 0:44 to 0:45, 0:58 to 1:00; 2022-06 0:17 to 0:18, 0:35 to 0:36, 0:50 to 0:52, 1:05 to 1:06: Pattern 2, stacked title + badge on one side, one phone on the other.
- 2022-03 0:21 to 0:24, 0:52 to 0:57: phone rising from the bottom edge, cropped, over a shape behind it.
- 2022-06 0:09 and 0:28: a phone tilted diagonally behind a giant word ("Pixel").
- 2021-12 1:30, 2022-03 1:20 to 1:22, 2022-06 1:15 to 1:16: three phones fanned.

### Transitions
- 2022-06 0:19, 0:26, 0:37, 0:53: colour-blob wipes, large shapes sweep across and become the next background.
- 2022-03 0:33 to 0:34, 1:01: split screen, two colour panels each with a badge.
- 2021-12 1:19: the badge shrinks to a point before the cut.

### End
- 2021-12 1:37 to 1:39, 2022-03 1:25 to 1:26, 2022-06 1:18 to 1:20: the Google G assembles on white.
