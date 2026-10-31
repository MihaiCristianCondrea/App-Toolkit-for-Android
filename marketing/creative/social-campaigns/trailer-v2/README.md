# App Toolkit: Trailer V2

A 16:9 product trailer for App Toolkit based on the owner's toolbox sketches and the 29.88-second soundtrack.
The mark opens, four live widgets leave its mouth into a connected 2x2 bento, the screen forms into a Pixel 9 Pro phone, then Measure and Play & Decide open from their tabs before a 3D fan of three devices ends in a golden burst.

| | |
| --- | --- |
| Status | Polished preview, ready for owner playback |
| Sound | The complete supplied `soundtrack.mp3`, 29.88 seconds, unchanged speed and volume |
| Design size | 1920 x 1080, 90 fps |
| Preview | 960 x 540 |
| Master | 2560 x 1440, rendered after creative review |
| Output | `output/video/` and `output/images/`, generated and ignored by Git |

## Scenes & Choreography

| Time | Picture |
| --- | --- |
| 0.00 to 1.76 | Corner shapes arrive one per motif note around an open focal centre. |
| 1.76 to 3.32 | The mark pops, its lid swings about the right hinge like a chest lid (handle attached) and the box settles low as the launcher. |
| 3.32 to 6.76 | Compass, Bubble level, Flash Dimmer and SOS rise out of the box, one per note, and lock into a bento whose inner corners tighten to 8 px. Starts smaller to fit above the box. |
| 6.76 to 10.08 | A teal ground grows from the bento; it moves right and grows beside "One place.". |
| 10.08 to 17.14 | The bento becomes the phone. Each Quick Tools widget lifts out of the screen on its note while its name lands. |
| 17.14 to 21.98 | A gold ground and the Measure screen grow from the Measure tab; Protractor, Unit Converter, Bill Splitter and Counter push through the left column with live values. |
| 21.98 to 26.26 | A pink ground and Play & Decide grow from their tab. The phone springs to the left half with a short swing, and "Let chance decide.", the spinning wheel and the result cards take the right side at a larger scale. |
| 26.26 to 29.88 | The three destinations fan in beside the mark, title and Google Play badge; a gold burst from the mark takes the frame. |

## Build

Requires Node 22+, Python 3 with Pillow and FFmpeg/ffprobe on PATH.

```sh
cd marketing/ui
npm ci && npm run assets
cd ../creative/social-campaigns/trailer-v2/source
npm ci
npm run assets      # copies assets, extracts SVG layers, builds device and media metadata
npm run lint        # type-check
npm run preview     # renders 960 x 540 preview video with full soundtrack
npm run sketchbook  # renders all 6 act sketchbook sheets and master storyboards
npm run render      # renders 2K master once approved
```
