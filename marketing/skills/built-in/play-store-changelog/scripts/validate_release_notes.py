#!/usr/bin/env python3
"""Validate Google Play What's new notes and locale blocks.

Standard library only. Python 3.9+.

Default limit is intentionally 499 Unicode code points, strictly under the
published Google Play Console limit of 500 Unicode characters per language.

Usage:
    python scripts/validate_release_notes.py notes.txt --mode auto
    python scripts/validate_release_notes.py notes.txt --mode console --required-locales en-US,ro
    python scripts/validate_release_notes.py notes.txt --mode plain --json
"""

from __future__ import annotations

import argparse
from dataclasses import asdict, dataclass
import json
from pathlib import Path
import re
import sys
from typing import List, Optional, Sequence, Tuple

OPEN_TAG = re.compile(r"^<([a-z]{2,3}(?:-[A-Za-z0-9]{2,8})*)>$")
CLOSE_TAG = re.compile(r"^</([a-z]{2,3}(?:-[A-Za-z0-9]{2,8})*)>$")
TAG_LIKE = re.compile(r"^</?[A-Za-z][A-Za-z0-9\- ]*>$")


@dataclass
class Note:
    locale: str
    text: str
    line: int


@dataclass
class Result:
    locale: str
    chars: int
    maximum: int
    status: str
    line: int
    errors: List[str]
    warnings: List[str]


def normalize_newlines(text: str) -> str:
    return text.replace("\r\n", "\n").replace("\r", "\n")


def infer_mode(text: str) -> str:
    if any(OPEN_TAG.fullmatch(line) or CLOSE_TAG.fullmatch(line)
           for line in text.split("\n")):
        return "console"
    return "plain"


def parse_console(text: str) -> Tuple[List[Note], List[str]]:
    lines = text.split("\n")
    notes: List[Note] = []
    errors: List[str] = []
    locale: Optional[str] = None
    opened_line = 0
    inner_lines: List[str] = []
    seen: set = set()

    for lineno, line in enumerate(lines, start=1):
        opening = OPEN_TAG.fullmatch(line)
        closing = CLOSE_TAG.fullmatch(line)
        if locale is None:
            if opening:
                candidate = opening.group(1)
                if candidate.lower() in seen:
                    errors.append(f"line {lineno}: duplicate locale <{candidate}>")
                seen.add(candidate.lower())
                locale = candidate
                opened_line = lineno
                inner_lines = []
            elif closing:
                errors.append(f"line {lineno}: unexpected closing tag {line}")
            elif line.strip():
                if TAG_LIKE.fullmatch(line):
                    errors.append(f"line {lineno}: invalid or unsupported locale tag {line}")
                else:
                    errors.append(f"line {lineno}: content outside a locale block")
            continue

        if opening:
            errors.append(
                f"line {lineno}: nested locale <{opening.group(1)}> inside <{locale}>"
            )
            inner_lines.append(line)
        elif closing:
            if closing.group(1) != locale:
                errors.append(
                    f"line {lineno}: closing tag {line} does not match <{locale}>"
                )
                # Keep current block open so a subsequent matching close can recover.
                inner_lines.append(line)
            else:
                notes.append(Note(locale, "\n".join(inner_lines), opened_line))
                locale = None
                opened_line = 0
                inner_lines = []
        else:
            if TAG_LIKE.fullmatch(line) and not (opening or closing):
                errors.append(f"line {lineno}: invalid or unsupported locale tag {line}")
            inner_lines.append(line)

    if locale is not None:
        errors.append(f"line {opened_line}: missing </{locale}> closing tag")
    if not notes:
        errors.append("no complete locale blocks found")
    return notes, errors


def parse_plain(text: str) -> Tuple[List[Note], List[str]]:
    # Remove one terminal newline conventionally added by text editors; other
    # deliberate blank lines are retained and counted. No whitespace stripping.
    if text.endswith("\n"):
        text = text[:-1]
    errors = []
    for lineno, line in enumerate(text.split("\n"), start=1):
        if OPEN_TAG.fullmatch(line) or CLOSE_TAG.fullmatch(line):
            errors.append(
                f"line {lineno}: locale tags found in plain mode; use --mode console "
                "or remove tags (Fastlane files should not contain them)"
            )
    return [Note("plain", text, 1)], errors


def check_notes(
    notes: Sequence[Note],
    max_chars: int = 499,
    target_min: int = 250,
    target_max: int = 450,
) -> List[Result]:
    result = []
    for note in notes:
        count = len(note.text)
        errors = []
        warnings = []
        if not note.text.strip():
            errors.append("note body is empty")
        if count > max_chars:
            errors.append(f"{count} characters exceeds {max_chars} by {count - max_chars}")
        elif count < target_min:
            warnings.append(
                f"below the {target_min}-{target_max} editorial target; "
                "this is fine for a short release, do not pad"
            )
        elif count > target_max:
            warnings.append(
                f"above the {target_min}-{target_max} editorial target but within the hard limit"
            )
        if "```" in note.text:
            warnings.append("contains code fences; paste only their contents into Play Console")
        if re.search(r"(?m)^#{1,6} ", note.text):
            warnings.append("contains Markdown headings; prefer plain text headings")
        result.append(Result(
            locale=note.locale,
            chars=count,
            maximum=max_chars,
            status="error" if errors else "ok",
            line=note.line,
            errors=errors,
            warnings=warnings,
        ))
    return result


def validate(
    text: str,
    mode: str = "auto",
    max_chars: int = 499,
    target_min: int = 250,
    target_max: int = 450,
    required_locales: Optional[Sequence[str]] = None,
) -> Tuple[str, List[Result], List[str]]:
    text = normalize_newlines(text)
    actual_mode = infer_mode(text) if mode == "auto" else mode
    if actual_mode == "console":
        notes, structural_errors = parse_console(text)
    elif actual_mode == "plain":
        notes, structural_errors = parse_plain(text)
    else:
        raise ValueError(f"unknown mode {actual_mode}")

    if required_locales:
        if actual_mode != "console":
            structural_errors.append("--required-locales requires console mode")
        else:
            actual = {note.locale.lower() for note in notes}
            missing = [code for code in required_locales if code.lower() not in actual]
            if missing:
                structural_errors.append("missing locales: " + ", ".join(missing))
    return (actual_mode, check_notes(notes, max_chars, target_min, target_max),
            structural_errors)


def main(argv: Optional[Sequence[str]] = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("path", help="UTF-8 notes file, or '-' for standard input")
    parser.add_argument("--mode", choices=("auto", "console", "plain"), default="auto")
    parser.add_argument("--max-chars", type=int, default=499,
                        help="hard maximum Unicode code points per language (default: 499)")
    parser.add_argument("--target-min", type=int, default=250)
    parser.add_argument("--target-max", type=int, default=450)
    parser.add_argument("--required-locales", default="",
                        help="comma-separated required locales, e.g. en-US,ro")
    parser.add_argument("--json", action="store_true", help="emit machine-readable results")
    args = parser.parse_args(argv)
    if args.max_chars < 1 or args.target_min < 0 or args.target_max < args.target_min:
        parser.error("limits must satisfy: max-chars >= 1, target-min >= 0, target-max >= target-min")

    try:
        if args.path == "-":
            original = sys.stdin.read()
        else:
            original = Path(args.path).read_text(encoding="utf-8-sig")
    except (OSError, UnicodeError) as exc:
        print(f"Cannot read {args.path}: {exc}", file=sys.stderr)
        return 2

    requested = [x.strip() for x in args.required_locales.split(",") if x.strip()]
    actual_mode, results, structural_errors = validate(
        original,
        mode=args.mode,
        max_chars=args.max_chars,
        target_min=args.target_min,
        target_max=args.target_max,
        required_locales=requested,
    )
    succeeded = not structural_errors and all(item.status == "ok" for item in results)
    if args.json:
        print(json.dumps({
            "valid": succeeded,
            "mode": actual_mode,
            "structural_errors": structural_errors,
            "results": [asdict(item) for item in results],
        }, indent=2, ensure_ascii=False))
    else:
        for error in structural_errors:
            print(f"ERROR: {error}")
        for item in results:
            status = "OK" if item.status == "ok" else "ERROR"
            print(f"{status} {item.locale}: {item.chars}/{item.maximum} characters "
                  f"(starts on line {item.line})")
            for error in item.errors:
                print(f"  ERROR: {error}")
            for warning in item.warnings:
                print(f"  NOTE: {warning}")
        if succeeded:
            print("VALID: all release-note bodies are within the configured hard limit.")
    return 0 if succeeded else 1


if __name__ == "__main__":
    sys.exit(main())
