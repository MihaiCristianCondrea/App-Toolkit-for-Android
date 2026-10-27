# Pixelbook Go

Google's device art for the Pixelbook Go, from the Android Developers Device Art Generator
(`https://developer.android.com/distribute/marketing-tools/device-art-resources/pixelbook_go/`), downloaded 2026-10-16.

| File | Size | Layer |
| --- | --- | --- |
| `land_shadow.png` | 2188 x 1459 | The shadow under the laptop, drawn first |
| `land_back.png` | 2188 x 1459 | The laptop, with the screen left transparent |
| `land_fore.png` | 2188 x 1459 | The screen glare, drawn last (the UI kit leaves it out) |
| `thumb.png` | 230 x 153 | The generator's thumbnail |

The screen is the transparent 1826 x 1079 px rectangle at 181, 159 in `land_back.png` (measured from its alpha). The
UI kit draws it at 1218 / 1826, so the screen is the 1218 x 720 dp `desktop` window (`marketing/ui/src/kit/layout.js`).
