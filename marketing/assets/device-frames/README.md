# Device frames

Android Studio's device art, one folder per device: `back`, `mask` and the `layout` file that places the
display inside the frame.

The devices hubs draw are not kept here, because
[Material Next Web Components](https://github.com/MihaiCristianCondrea/material-next-web-components)
ships them, where `<mnw-device-frame>` loads them:

| Device | In Material Next | Still here |
| --- | --- | --- |
| Pixel 9 Pro XL | Drawn in 3D by Material Next (`marketing/ui/pixel-model.js`); the host passes the back `logo` mask | `pixel_9_pro_xl/layout`, `display-mask.png` |
| Pixel 9 Pro Fold, open | `assets/marketing/device-frames/pixel_9_pro_fold/back.webp`, `mask.webp` | `pixel_9_pro_fold/default/layout` (and the whole `closed` frame) |
| Pixel Tablet | Not currently a `<mnw-device-frame>` device preset; composite from the asset | `pixel_tablet/back.webp`, `mask.webp`, `layout` |

A hub copies them, with the Google Play badge and the Pixelbook Go frame, using
`npx mnw-marketing-assets public/material-next`.

For Google Play screenshot production, use the Material Next 3D Pixel 9 Pro XL for phones
(`logo` is a host-supplied white-on-transparent Google mark), the Pixel Tablet art above for
tablets, and genuine posture-specific captures for foldables. Play requires 1:1 square
unfolded, 4:3 landscape unfolded, and 10:16 portrait folded screenshots when a dedicated
foldable set is supplied. The full exported image, not only its embedded display, must have
the requested aspect ratio. See the [Play Store Media skill](../../.agents/skills/first-party/play-store/play-store-media/SKILL.md).
