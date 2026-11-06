# Phone screenshot captions

The caption style for every phone screenshot, taken from the way Google captions its own apps on Google Play (Chrome,
Gemini, Photos): a short centred sentence with one word or phrase lifted onto a pill that carries the screen's own
icon. One rule for every app, so their listings read as a family.

## The caption

- **Two short lines, centred** in the top band, in the ink colour of the listing's ground. The whole caption is one
  sentence about the benefit, in the user's words ([`writing-style`](../../writing-style/SKILL.md)): "Handy tools"
  over "one tap away", "Let the wheel" over "decide".
- **No punctuation and no dashes**: no commas, full stops, colons, semicolons, hyphens, en dashes or em dashes, as
  Google's own captions do. The line break carries the pause; reword a caption that needs one ("Split the bill" over
  "with the tip", not "Split the bill," over "tip included").
- **One highlight per caption**: a single word or a short phrase from the caption itself, never added text. Pick the
  word that names what the screen shows: the feature ("tools", "wheel"), its object ("speaker") or its action
  ("Split", "Measure"). It stays in its place in the sentence, so it can sit at the start, the middle or the end of either line.
- **The highlight is a pill**: fully rounded, filled with the listing's strong accent, the text in its on-colour, at
  the caption's own size and a little bolder, with a sliver of space on each side.
- **The highlight sits on the line's baseline**: the pill is an inline block, so its word shares the baseline of the
  words around it, and the padding (never a vertical offset) sizes the pill around the word. Check a render at full
  size: the pill's word and its neighbours must stand on one line.
- **The pill starts with an icon**: a Material Symbol, rounded and filled, about 0.7 of the text size, centred in the
  pill's height. Use the icon the app itself shows for that screen: its tab, tool or section icon (from the UI kit's
  config), never a new one. A caption whose screen has no icon of its own uses the icon of the tab it lives in.
- **Size and weight**: large and readable on a phone (76 px on a 1080 x 1920 image), medium-bold (about 560), slightly
  tight tracking; a line height of about 1.35, so the pill of one line never touches the other line.
- **Strings**: each caption is `lines` (two strings), `highlight` (the exact words on the pill) and `icon` (the
  Material Symbol name). A translation keeps the highlight as words of its own lines; the icon stays the same.

## Checks the renderer runs

The render fails, so a bad caption can never be uploaded, when:

1. The highlight does not appear exactly once in the caption's lines.
2. A line has a comma, a full stop, a colon, a semicolon, a hyphen or a dash.
3. The caption leaves its box: a fixed rectangle centred in the top band (on a 1080 x 1920 image: 120 px in from each
   side, from 72 to 312 px down). Shorten the caption or the highlight.
4. The caption covers more than 20% of the image, Google Play's limit.

## Shapes around the caption

The caption box is reserved: nothing else is drawn in it.

- **No shape overlaps another shape or a caption box.** Every placed piece (the seam shapes, waves, dot grids and the
  scatter) is a circle around its centre, and none may come within a margin (40 px) of another or of any caption
  box. The layout code asserts this for the fixed pieces and fails the render with the two pieces it found; the
  seeded scatter skips any position that breaks it.
- **The top band stays calm**: only the small shapes centred on the seams sit there, between two captions, small
  enough to keep the margin to both boxes (about 150 px at most on 1080 px images).
- **Waves and dot grids sit in the gutters between the phones**, under the captions, centred on a seam.
- Large shapes may still run behind the phones and off the edges, as the panorama rules in the skill say.
