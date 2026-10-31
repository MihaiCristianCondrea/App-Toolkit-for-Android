# App Toolkit: trailer v2 (film v3)

A new trailer on a new beat. The film (`AppTrailerV3`) reuses the trailer v2 choreography from
`social-campaigns/app-trailer`, but every shot is timed from the onsets of the soundtrack in this
folder, so a different track changes the cut without editing the scenes.

| | |
| --- | --- |
| Status | Waiting for the soundtrack |
| Sound | `soundtrack.mp3` in this folder (owner-supplied, used whole and unchanged) |
| Design size | 1920 x 1080, 90 fps |
| Preview | 960 x 540 |
| Master | 2560 x 1440, rendered after creative review |
| Reference | `reference/app-trailer-v1-v2.zip`: the complete `app-trailer` campaign (v1 and v2) at the time this one started |

## Build

1. Put the new beat in this folder as `soundtrack.mp3`.
2. Requires Node 22+, Python 3 with NumPy and Pillow, and FFmpeg/ffprobe on PATH:

```sh
cd marketing/ui && npm ci && npm run assets
cd ../creative/social-campaigns/trailer-v2/source && npm ci
npm run assets    # copies assets, measures the track and detects its onsets
npm run lint
npm run preview   # 960 x 540 with the full soundtrack
npm run sheet     # quarter-scale review stills (BROWSER_EXECUTABLE for a local Chrome)
```

`npm run render` makes the 2K master once the preview is approved.

## How the timing follows the beat

`scripts/analyze.py` decodes the track to 8 kHz mono and peak-picks a bass-weighted spectral flux (at least 0.2 s
between onsets). It writes `src/generated/music-analysis.json` on every `npm run assets`. The BPM it prints is an
estimate.

`src/Trailer.tsx` keeps the v2 shot list as times on its 29.88 s track. Each time is scaled to the new track's
length and moved to the nearest detected onset; lists stay in order, and the gold burst always lands at least
0.5 s before the end. The live tools also change on every onset.

## Shots

Same story as trailer v2:
1. Corner shapes arrive on the motif notes.
2. The mark pops and opens like a chest.
3. Four live tools rise out of it into a bento.
4. A teal ground and "One place." follow.
5. The bento becomes the phone, and each Quick Tools widget lifts out on its note.
6. Measure grows from its tab.
7. Play & Decide grows from its tab, with the phone springing to the left half.
8. Three destinations fan in.
9. A gold burst from the mark ends the film.

Once the new beat is in, retime or restage shots in `src/Trailer.tsx` to suit its phrases.
