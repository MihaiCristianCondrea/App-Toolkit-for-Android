#!/usr/bin/env python3
"""Build a storyboard (one labelled tile every N seconds) for each reference film.

Reads references/films/<film>.<ext> and writes references/storyboards/<film>.jpg.
Needs ffmpeg on PATH (or --ffmpeg) and Pillow.

    python3 scripts/storyboards.py [--every 2] [--width 320] [--cols 8] [--ffmpeg ffmpeg] [film ...]
"""
import argparse
import subprocess
import tempfile
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

REFS = Path(__file__).resolve().parent.parent / "references"


def build(film: Path, out: Path, every: float, width: int, cols: int, ffmpeg: str) -> None:
    with tempfile.TemporaryDirectory() as tmp:
        subprocess.run(
            [ffmpeg, "-v", "error", "-i", str(film), "-vf", f"fps=1/{every},scale={width}:-2",
             str(Path(tmp) / "f_%04d.png")],
            check=True,
        )
        frames = sorted(Path(tmp).glob("f_*.png"))
        if not frames:
            raise SystemExit(f"no frames from {film}")
        first = Image.open(frames[0])
        w, h = first.size
        rows = (len(frames) + cols - 1) // cols
        sheet = Image.new("RGB", (cols * w, rows * h), "black")
        draw = ImageDraw.Draw(sheet)
        font = ImageFont.load_default()
        for i, f in enumerate(frames):
            x, y = (i % cols) * w, (i // cols) * h
            sheet.paste(Image.open(f).convert("RGB"), (x, y))
            t = int(i * every)
            label = f"{t // 60}:{t % 60:02d}"
            draw.rectangle((x, y, x + 8 * len(label) + 8, y + 16), fill="black")
            draw.text((x + 4, y + 2), label, fill="white", font=font)
        out.parent.mkdir(parents=True, exist_ok=True)
        sheet.save(out, quality=85)
        print(f"{out.relative_to(REFS)}  {len(frames)} tiles")


def main() -> None:
    p = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    p.add_argument("films", nargs="*", help="film slugs (default: every film in references/films)")
    p.add_argument("--every", type=float, default=2.0, help="seconds between tiles")
    p.add_argument("--width", type=int, default=320, help="tile width in pixels")
    p.add_argument("--cols", type=int, default=8, help="tiles per row")
    p.add_argument("--ffmpeg", default="ffmpeg", help="ffmpeg executable")
    a = p.parse_args()
    films = sorted((REFS / "films").glob("*.*"))
    if a.films:
        films = [f for f in films if f.stem in a.films]
    for film in films:
        build(film, REFS / "storyboards" / f"{film.stem}.jpg", a.every, a.width, a.cols, a.ffmpeg)


if __name__ == "__main__":
    main()
