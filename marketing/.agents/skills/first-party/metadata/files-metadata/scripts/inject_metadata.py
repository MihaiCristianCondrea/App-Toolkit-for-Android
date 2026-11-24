#!/usr/bin/env python3
"""Write SEO and rights metadata into finished marketing files, then verify nothing else changed.

Metadata comes from three layers, later ones winning:

1. the project block in `marketing/README.md` (between `<!-- project-metadata -->` and the next ```json fence),
2. the campaign manifest `<campaign-slug>-metadata.json`,
3. the manifest's `files` entry for one file (matched by its path relative to the manifest, or its name).

    python3 inject_metadata.py --manifest marketing/social-campaigns/<slug>/<slug>-metadata.json output/**/*
    python3 inject_metadata.py --manifest ... --jsonld output/schema.jsonld output/*   # also write a JSON-LD graph
    python3 inject_metadata.py --manifest ... --dry-run output/*                        # show what would be written
    python3 inject_metadata.py --manifest ... --check output/*                          # only report what is missing

Formats and how they are written:

    PNG                          iTXt text chunks and an XMP packet (pure Python)
    JPEG WebP GIF TIFF AVIF HEIC  EXIF, IPTC and XMP with exiftool
    PDF                          Info dictionary and XMP with exiftool
    SVG                          <title>, <desc> and RDF <metadata>
    HTML                         title, meta description, Open Graph, Twitter card, canonical, JSON-LD
    WAV                          RIFF LIST INFO (pure Python) and an ID3 chunk (mutagen)
    MP3 AIFF                     ID3v2.4 (mutagen)
    FLAC OGG Opus                Vorbis comments (mutagen)
    M4A AAC                      MP4 atoms (mutagen)
    MP4 MOV M4V WebM MKV         container tags with ffmpeg -c copy (no re-encode)
    anything else (SRT VTT ZIP)  an XMP sidecar next to the file (<file>.xmp)

Every write is checked: the decoded pixels, the stream packets or the visible document must be identical before and
after. A file that fails the check is restored from its backup and reported.
"""
from __future__ import annotations

import argparse
import datetime as dt
import hashlib
import html
import json
import re
import shutil
import struct
import subprocess
import sys
import tempfile
import zlib
from pathlib import Path
from xml.sax.saxutils import escape as xml_escape

MARKER = "<!-- project-metadata -->"
DEFAULT_CREDITS = {
    "creator": "Mihai-Cristian Condrea",
    "copyrightHolder": "Mihai-Cristian Condrea",
    "country": "Romania",
    "profiles": {
        "GitHub": "https://github.com/MihaiCristianCondrea",
        "X": "https://x.com/MihaiCristianC",
        "Facebook": "https://www.facebook.com/dev.mihaicristiancondrea",
        "TikTok": "https://www.tiktok.com/@dev.mihaicristiancondrea",
        "Instagram": "https://www.instagram.com/dev.mihaicristiancondrea",
    },
}

IMAGE_EXIFTOOL = {".jpg", ".jpeg", ".webp", ".gif", ".tif", ".tiff", ".avif", ".heic", ".heif"}
VIDEO = {".mp4", ".mov", ".m4v", ".webm", ".mkv"}
MUTAGEN_AUDIO = {".mp3", ".aiff", ".aif", ".flac", ".ogg", ".oga", ".opus", ".m4a", ".aac"}


# ---------------------------------------------------------------------------------------------------------------------
# Loading and merging


def find_readme(start: Path) -> Path | None:
    for d in [start, *start.parents]:
        if d.name == "marketing" and (d / "README.md").exists():
            return d / "README.md"
        if (d / "marketing" / "README.md").exists():
            return d / "marketing" / "README.md"
    return None


def read_project_block(readme: Path | None) -> dict:
    if not readme:
        return {}
    text = readme.read_text(encoding="utf-8")
    at = text.find(MARKER)
    if at < 0:
        return {}
    m = re.search(r"```json\s*\n(.*?)\n```", text[at:], re.S)
    return json.loads(m.group(1)) if m else {}


def deep_merge(base: dict, over: dict) -> dict:
    out = dict(base)
    for k, v in over.items():
        out[k] = deep_merge(out[k], v) if isinstance(v, dict) and isinstance(out.get(k), dict) else v
    return out


class Meta:
    """The merged metadata for one file, with the field names every writer uses."""

    def __init__(self, data: dict, path: Path):
        self.d = data
        p, c, cr, cp = data.get("project", {}), data.get("campaign", {}), data.get("credits", {}), data.get("copy", {})
        self.title = cp.get("title") or c.get("title") or p.get("name", path.stem)
        self.headline = cp.get("headline") or self.title
        self.description = cp.get("description") or p.get("description", "")
        self.long_description = cp.get("longDescription") or self.description
        self.alt = cp.get("altText") or self.description
        self.keywords = list(dict.fromkeys(cp.get("keywords", []) + p.get("keywords", [])))
        self.hashtags = cp.get("hashtags", [])
        self.creator = cr.get("creator", DEFAULT_CREDITS["creator"])
        self.holder = cr.get("copyrightHolder", self.creator)
        self.year = str(cr.get("copyrightYear") or c.get("releaseDate", "")[:4] or dt.date.today().year)
        self.copyright = cr.get("copyright") or f"Copyright {self.year} {self.holder}. All rights reserved."
        self.license = cr.get("license", "")
        self.contact = cr.get("contact", "")
        self.country = cr.get("country", "")
        self.profiles = cr.get("profiles", {})
        self.publisher = p.get("publisher") or p.get("name") or self.creator
        self.project = p.get("name", "")
        self.url = cp.get("url") or c.get("url") or p.get("url", "")
        self.store_url = p.get("storeUrl", "")
        self.repository = p.get("repository", "")
        self.category = p.get("category", "")
        self.genre = cp.get("genre") or c.get("genre") or "Promotional"
        self.language = p.get("language", "en")
        self.date = c.get("releaseDate") or dt.date.today().isoformat()
        self.album = c.get("title") or self.project
        self.series = c.get("seriesCode", "")
        self.software = data.get("software", "")
        self.image = cp.get("image", "")

    def comment(self) -> str:
        parts = [self.description]
        if self.store_url:
            parts.append(self.store_url)
        if self.hashtags:
            parts.append(" ".join(self.hashtags))
        return "\n\n".join(x for x in parts if x)


def load_meta(manifest: Path, readme: Path | None, file: Path) -> Meta:
    data = deep_merge({"credits": DEFAULT_CREDITS}, read_project_block(readme))
    camp = json.loads(manifest.read_text(encoding="utf-8"))
    files = camp.pop("files", {})
    data = deep_merge(data, camp)
    try:
        rel = file.resolve().relative_to(manifest.parent.resolve()).as_posix()
    except ValueError:
        rel = file.name
    over = files.get(rel) or files.get(file.name) or {}
    if over:
        data = deep_merge(data, {"copy": over})
    return Meta(data, file)


# ---------------------------------------------------------------------------------------------------------------------
# XMP


def xmp_packet(m: Meta) -> str:
    def alt(v):
        return f'<rdf:Alt><rdf:li xml:lang="x-default">{xml_escape(v)}</rdf:li></rdf:Alt>'

    bag = "".join(f"<rdf:li>{xml_escape(k)}</rdf:li>" for k in m.keywords)
    web = f"<xmpRights:WebStatement>{xml_escape(m.url)}</xmpRights:WebStatement>" if m.url else ""
    lic = f"<cc:license rdf:resource=\"{xml_escape(m.license)}\"/>" if m.license.startswith("http") else ""
    return f"""<?xpacket begin="﻿" id="W5M0MpCehiHzreSzNTczkc9d"?>
<x:xmpmeta xmlns:x="adobe:ns:meta/">
 <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about=""
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:xmp="http://ns.adobe.com/xap/1.0/"
    xmlns:xmpRights="http://ns.adobe.com/xap/1.0/rights/"
    xmlns:photoshop="http://ns.adobe.com/photoshop/1.0/"
    xmlns:Iptc4xmpCore="http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/"
    xmlns:cc="http://creativecommons.org/ns#">
   <dc:title>{alt(m.title)}</dc:title>
   <dc:description>{alt(m.description)}</dc:description>
   <dc:creator><rdf:Seq><rdf:li>{xml_escape(m.creator)}</rdf:li></rdf:Seq></dc:creator>
   <dc:publisher><rdf:Bag><rdf:li>{xml_escape(m.publisher)}</rdf:li></rdf:Bag></dc:publisher>
   <dc:rights>{alt(m.copyright)}</dc:rights>
   <dc:subject><rdf:Bag>{bag}</rdf:Bag></dc:subject>
   <dc:language><rdf:Bag><rdf:li>{xml_escape(m.language)}</rdf:li></rdf:Bag></dc:language>
   <xmp:CreateDate>{m.date}</xmp:CreateDate>
   <xmp:MetadataDate>{dt.date.today().isoformat()}</xmp:MetadataDate>
   <xmp:CreatorTool>{xml_escape(m.software)}</xmp:CreatorTool>
   <xmpRights:Marked>True</xmpRights:Marked>
   <xmpRights:Owner><rdf:Bag><rdf:li>{xml_escape(m.holder)}</rdf:li></rdf:Bag></xmpRights:Owner>
   {web}
   <photoshop:Headline>{xml_escape(m.headline)}</photoshop:Headline>
   <photoshop:Credit>{xml_escape(m.creator)}</photoshop:Credit>
   <photoshop:Source>{xml_escape(m.project)}</photoshop:Source>
   <photoshop:Country>{xml_escape(m.country)}</photoshop:Country>
   <Iptc4xmpCore:AltTextAccessibility>{alt(m.alt)}</Iptc4xmpCore:AltTextAccessibility>
   {lic}
  </rdf:Description>
 </rdf:RDF>
</x:xmpmeta>
<?xpacket end="w"?>"""


# ---------------------------------------------------------------------------------------------------------------------
# PNG (pure Python)

PNG_SIG = b"\x89PNG\r\n\x1a\n"
PNG_TEXT = {b"tEXt", b"zTXt", b"iTXt"}


def png_chunks(data: bytes):
    if not data.startswith(PNG_SIG):
        raise ValueError("not a PNG")
    i = 8
    while i < len(data):
        (n,) = struct.unpack(">I", data[i:i + 4])
        yield data[i + 4:i + 8], data[i + 8:i + 8 + n]
        i += 12 + n


def png_chunk(kind: bytes, body: bytes) -> bytes:
    return struct.pack(">I", len(body)) + kind + body + struct.pack(">I", zlib.crc32(kind + body) & 0xFFFFFFFF)


def itxt(key: str, value: str) -> bytes:
    return png_chunk(b"iTXt", key.encode("latin-1") + b"\0\0\0\0\0" + value.encode("utf-8"))


def write_png(path: Path, m: Meta) -> None:
    fields = {
        "Title": m.title, "Author": m.creator, "Description": m.description, "Copyright": m.copyright,
        "Creation Time": m.date, "Software": m.software, "Source": m.project, "Comment": m.comment(),
        "Keywords": ", ".join(m.keywords), "Disclaimer": m.license, "URL": m.url,
        "XML:com.adobe.xmp": xmp_packet(m),
    }
    out = bytearray(PNG_SIG)
    for kind, body in png_chunks(path.read_bytes()):
        if kind in PNG_TEXT:
            continue  # replaced below; keeps the write idempotent
        if kind == b"IDAT" and fields:
            for k, v in fields.items():
                if v:
                    out += itxt(k, v)
            fields = {}
        out += png_chunk(kind, body)
    path.write_bytes(bytes(out))


# ---------------------------------------------------------------------------------------------------------------------
# WAV RIFF LIST INFO (pure Python) + ID3 (mutagen)


def write_wav(path: Path, m: Meta) -> None:
    data = path.read_bytes()
    if data[:4] != b"RIFF" or data[8:12] != b"WAVE":
        raise ValueError("not a RIFF WAVE file")
    chunks, i = [], 12
    while i + 8 <= len(data):
        cid, n = data[i:i + 4], struct.unpack("<I", data[i + 4:i + 8])[0]
        body = data[i + 8:i + 8 + n]
        if not (cid == b"LIST" and body[:4] == b"INFO"):
            chunks.append((cid, body))
        i += 8 + n + (n & 1)
    info = {b"INAM": m.title, b"IART": m.creator, b"ICMT": m.comment(), b"ICOP": m.copyright, b"ICRD": m.date,
            b"IGNR": m.genre, b"IKEY": "; ".join(m.keywords), b"IPRD": m.album, b"ISFT": m.software,
            b"ISBJ": m.description, b"ICNT": m.country}
    lst = b"INFO"
    for k, v in info.items():
        if v:
            b = v.encode("utf-8") + b"\0"
            lst += k + struct.pack("<I", len(b)) + b + (b"\0" if len(b) & 1 else b"")
    chunks.append((b"LIST", lst))
    body = b"WAVE" + b"".join(c + struct.pack("<I", len(x)) + x + (b"\0" if len(x) & 1 else b"") for c, x in chunks)
    path.write_bytes(b"RIFF" + struct.pack("<I", len(body)) + body)
    from mutagen.wave import WAVE

    f = WAVE(path)
    if f.tags is None:
        f.add_tags()
    id3_fill(f.tags, m)
    f.save()


def id3_fill(tags, m: Meta) -> None:
    from mutagen import id3

    for key in ("TIT2", "TPE1", "TPE2", "TALB", "TCOM", "TCOP", "TDRC", "TCON", "COMM", "WOAR", "WCOP", "TPUB",
                "TLAN", "TXXX:Keywords", "TXXX:Description", "WOAF"):
        tags.delall(key)
    tags.add(id3.TIT2(encoding=3, text=m.title))
    tags.add(id3.TPE1(encoding=3, text=m.creator))
    tags.add(id3.TPE2(encoding=3, text=m.creator))
    tags.add(id3.TALB(encoding=3, text=m.album))
    tags.add(id3.TCOM(encoding=3, text=m.creator))
    tags.add(id3.TCOP(encoding=3, text=f"{m.year} {m.holder}"))
    tags.add(id3.TDRC(encoding=3, text=m.date))
    tags.add(id3.TCON(encoding=3, text=m.genre))
    tags.add(id3.TPUB(encoding=3, text=m.publisher))
    tags.add(id3.TLAN(encoding=3, text="eng" if m.language.startswith("en") else m.language))
    tags.add(id3.COMM(encoding=3, lang="eng", desc="", text=m.comment()))
    tags.add(id3.TXXX(encoding=3, desc="Keywords", text="; ".join(m.keywords)))
    tags.add(id3.TXXX(encoding=3, desc="Description", text=m.description))
    if m.url:
        tags.add(id3.WOAF(url=m.url))
    if m.profiles:
        tags.add(id3.WOAR(url=next(iter(m.profiles.values()))))


def write_audio(path: Path, m: Meta) -> None:
    import mutagen

    f = mutagen.File(path)
    if f is None:
        raise ValueError("mutagen cannot read this file")
    ext = path.suffix.lower()
    if ext in {".mp3", ".aiff", ".aif"}:
        if f.tags is None:
            f.add_tags()
        id3_fill(f.tags, m)
    elif ext in {".m4a", ".aac"}:
        if f.tags is None:
            f.add_tags()
        t = f.tags
        t["\xa9nam"], t["\xa9ART"], t["aART"], t["\xa9alb"] = m.title, m.creator, m.creator, m.album
        t["\xa9wrt"], t["cprt"], t["\xa9day"], t["\xa9gen"] = m.creator, m.copyright, m.date, m.genre
        t["\xa9cmt"], t["desc"] = m.comment(), m.description
        t["----:com.apple.iTunes:KEYWORDS"] = "; ".join(m.keywords).encode()
    else:  # Vorbis comments
        if f.tags is None:
            f.add_tags()
        vals = {"TITLE": m.title, "ARTIST": m.creator, "ALBUMARTIST": m.creator, "ALBUM": m.album,
                "COMPOSER": m.creator, "COPYRIGHT": m.copyright, "DATE": m.date, "GENRE": m.genre,
                "DESCRIPTION": m.description, "COMMENT": m.comment(), "KEYWORDS": "; ".join(m.keywords),
                "ORGANIZATION": m.publisher, "CONTACT": m.contact or m.url, "LICENSE": m.license}
        for k, v in vals.items():
            if v:
                f.tags[k] = v
    f.save()


# ---------------------------------------------------------------------------------------------------------------------
# exiftool (JPEG, WebP, GIF, TIFF, AVIF, HEIC, PDF)


def write_exiftool(path: Path, m: Meta) -> None:
    if not shutil.which("exiftool"):
        raise RuntimeError("exiftool is not installed (https://exiftool.org)")
    a = [
        f"-XMP-dc:Title={m.title}", f"-XMP-dc:Description={m.description}", f"-XMP-dc:Creator={m.creator}",
        f"-XMP-dc:Rights={m.copyright}", f"-XMP-dc:Publisher={m.publisher}",
        f"-XMP-xmpRights:Marked=True", f"-XMP-xmpRights:Owner={m.holder}", f"-XMP-xmp:CreateDate={m.date}",
        f"-XMP-photoshop:Headline={m.headline}", f"-XMP-photoshop:Credit={m.creator}",
        f"-XMP-photoshop:Source={m.project}", f"-XMP-iptcCore:AltTextAccessibility={m.alt}",
        f"-XMP-xmp:CreatorTool={m.software}",
    ]
    a += [f"-XMP-dc:Subject={k}" for k in m.keywords]  # repeated '=' replaces the whole list
    if m.url:
        a.append(f"-XMP-xmpRights:WebStatement={m.url}")
    ext = path.suffix.lower()
    if ext == ".pdf":
        a += [f"-PDF:Title={m.title}", f"-PDF:Author={m.creator}", f"-PDF:Subject={m.description}",
              f"-PDF:Keywords={', '.join(m.keywords)}", f"-PDF:Creator={m.software}"]
    elif ext in {".jpg", ".jpeg", ".tif", ".tiff", ".webp", ".heic", ".heif", ".avif"}:
        a += [f"-EXIF:Artist={m.creator}", f"-EXIF:Copyright={m.copyright}",
              f"-EXIF:ImageDescription={m.description}", f"-EXIF:Software={m.software}",
              # Windows Explorer's Details tab: Title, Subject, Tags, Comments, Authors.
              f"-EXIF:XPTitle={m.title}", f"-EXIF:XPSubject={m.headline}", f"-EXIF:XPKeywords={'; '.join(m.keywords)}",
              f"-EXIF:XPComment={m.description}", f"-EXIF:XPAuthor={m.creator}"]
        if ext in {".jpg", ".jpeg", ".tif", ".tiff"}:
            a += ["-IPTC:CodedCharacterSet=UTF8", f"-IPTC:ObjectName={m.title[:64]}",
                  f"-IPTC:By-line={m.creator}", f"-IPTC:CopyrightNotice={m.copyright[:128]}",
                  f"-IPTC:Caption-Abstract={m.description[:2000]}", f"-IPTC:Headline={m.headline[:256]}",
                  f"-IPTC:Credit={m.creator}"]
            a += [f"-IPTC:Keywords={k[:64]}" for k in m.keywords]
    r = subprocess.run(["exiftool", "-q", "-overwrite_original", "-charset", "iptc=UTF8", *a, str(path)],
                       capture_output=True, text=True)
    if r.returncode:
        raise RuntimeError(r.stderr.strip() or "exiftool failed")


# ---------------------------------------------------------------------------------------------------------------------
# Video (ffmpeg, stream copy)


def write_video(path: Path, m: Meta) -> None:
    if not shutil.which("ffmpeg"):
        raise RuntimeError("ffmpeg is not installed")
    tags = {"title": m.title, "artist": m.creator, "album_artist": m.creator, "author": m.creator,
            "composer": m.creator, "album": m.album, "copyright": m.copyright, "date": m.date, "genre": m.genre,
            "description": m.description, "synopsis": m.long_description, "comment": m.comment(),
            "keywords": ", ".join(m.keywords), "publisher": m.publisher, "language": m.language}
    if m.url:
        tags["url"] = m.url
    tmp = path.with_name(path.stem + ".meta-tmp" + path.suffix)
    cmd = ["ffmpeg", "-v", "error", "-y", "-i", str(path), "-map", "0", "-c", "copy", "-map_metadata", "-1"]
    for k, v in tags.items():
        cmd += ["-metadata", f"{k}={v}"]
    if path.suffix.lower() in {".mp4", ".mov", ".m4v"}:
        cmd += ["-movflags", "+faststart"]
    r = subprocess.run([*cmd, str(tmp)], capture_output=True, text=True)
    if r.returncode:
        tmp.unlink(missing_ok=True)
        raise RuntimeError(r.stderr.strip() or "ffmpeg failed")
    tmp.replace(path)
    if path.suffix.lower() in {".mp4", ".mov", ".m4v"}:
        write_mp4_fields(path, m)


def write_mp4_fields(path: Path, m: Meta) -> None:
    """Fields ffmpeg cannot write: the iTunes item list, Microsoft's Xtra atom (what Windows Explorer shows under
    Details: Subtitle, Tags, Directors, Producers, Writers, Publisher, Content provider, Encoded by, Author URL,
    Promotion URL, Copyright) and an XMP packet. exiftool rewrites only the metadata atoms, never the media data."""
    if not shutil.which("exiftool"):
        raise RuntimeError("exiftool is not installed (https://exiftool.org); it writes the MP4 fields Windows reads")
    author_url = next(iter(m.profiles.values()), "") or m.url
    a = [
        f"-ItemList:Title={m.title}", f"-ItemList:Artist={m.creator}", f"-ItemList:AlbumArtist={m.creator}",
        f"-ItemList:Album={m.album}", f"-ItemList:Composer={m.creator}", f"-ItemList:Copyright={m.copyright}",
        f"-ItemList:Genre={m.genre}", f"-ItemList:Comment={m.comment()}", f"-ItemList:Description={m.description}",
        f"-ItemList:LongDescription={m.long_description}", f"-ItemList:ContentCreateDate={m.year}",
        f"-ItemList:Keyword={', '.join(m.keywords)}", f"-ItemList:EncodedBy={m.creator}",
        f"-Microsoft:SubTitle={m.headline}", f"-Microsoft:Category={'; '.join(m.keywords)}",
        f"-Microsoft:Director={m.creator}", f"-Microsoft:Producer={m.creator}", f"-Microsoft:Writer={m.creator}",
        f"-Microsoft:Publisher={m.publisher}", f"-Microsoft:Provider={m.publisher}",
        f"-Microsoft:EncodedBy={m.software or m.creator}",
        f"-XMP-dc:Title={m.title}", f"-XMP-dc:Description={m.description}", f"-XMP-dc:Creator={m.creator}",
        f"-XMP-dc:Rights={m.copyright}", f"-XMP-dc:Publisher={m.publisher}", f"-XMP-xmpRights:Owner={m.holder}",
        "-XMP-xmpRights:Marked=True", f"-XMP-xmpDM:Artist={m.creator}", f"-XMP-xmpDM:Genre={m.genre}",
    ]
    a += [f"-XMP-dc:Subject={k}" for k in m.keywords]
    if m.software:
        a += [f"-ItemList:Encoder={m.software}", f"-Microsoft:EncodedBy={m.software}", f"-XMP-xmp:CreatorTool={m.software}"]
    if author_url:
        a.append(f"-Microsoft:AuthorURL={author_url}")
    if m.store_url or m.url:
        a.append(f"-Microsoft:PromotionURL={m.store_url or m.url}")
    if m.url:
        a.append(f"-XMP-xmpRights:WebStatement={m.url}")
    r = subprocess.run(["exiftool", "-q", "-overwrite_original", *a, str(path)], capture_output=True, text=True)
    if r.returncode:
        raise RuntimeError(r.stderr.strip() or "exiftool failed")


# ---------------------------------------------------------------------------------------------------------------------
# SVG and HTML


def write_svg(path: Path, m: Meta) -> None:
    s = path.read_text(encoding="utf-8")
    s = re.sub(r"\s*<(title|desc|metadata)\b[^>]*>.*?</\1>", "", s, flags=re.S)
    rdf = xmp_packet(m)
    rdf = rdf[rdf.index("<rdf:RDF"):rdf.index("</rdf:RDF>") + len("</rdf:RDF>")]
    block = (f"\n  <title>{xml_escape(m.title)}</title>\n  <desc>{xml_escape(m.alt)}</desc>\n"
             f"  <metadata>{rdf}</metadata>")
    s, n = re.subn(r"(<svg\b[^>]*>)", lambda x: x.group(1) + block, s, count=1)
    if not n:
        raise ValueError("no <svg> element")
    s = re.sub(r"(<svg\b)(?![^>]*\brole=)", r'\1 role="img"', s, count=1)
    path.write_text(s, encoding="utf-8")


def jsonld_for(path: Path, m: Meta) -> dict:
    ext = path.suffix.lower()
    kind = ("VideoObject" if ext in VIDEO else "AudioObject" if ext in MUTAGEN_AUDIO | {".wav"}
            else "ImageObject" if ext in IMAGE_EXIFTOOL | {".png", ".svg"} else "WebPage" if ext in {".html", ".htm"}
            else "CreativeWork")
    person = {"@type": "Person", "name": m.creator, "sameAs": list(m.profiles.values())}
    if m.country:
        person["nationality"] = {"@type": "Country", "name": m.country}
    person["@id"] = (next(iter(m.profiles.values()), "") or "#creator") + "#person"
    ref = {"@id": person["@id"]}
    d = {"@type": kind, "name": m.title, "headline": m.headline, "description": m.description,
         "keywords": ", ".join(m.keywords), "inLanguage": m.language, "creator": person, "author": ref,
         "copyrightHolder": ref if m.holder == m.creator else {"@type": "Organization", "name": m.holder}, "copyrightYear": m.year, "copyrightNotice": m.copyright,
         "datePublished": m.date, "contentUrl": path.name, "encodingFormat": ext.lstrip(".")}
    if m.url:
        d["url"] = m.url
    if m.license:
        d["license"] = m.license
    if kind == "ImageObject":
        d["caption"] = m.alt
    if kind == "VideoObject" and m.image:
        d["thumbnailUrl"] = m.image
    if m.project:
        app = {"@type": "SoftwareApplication", "name": m.project, "operatingSystem": "Android",
               "applicationCategory": m.category or "Application"}
        if m.store_url:
            app["url"] = m.store_url
        d["about"] = app
    return d


def write_html(path: Path, m: Meta) -> None:
    s = path.read_text(encoding="utf-8")
    if "<head" not in s:
        raise ValueError("no <head> element")
    s = re.sub(r"\s*<!-- metadata:start -->.*?<!-- metadata:end -->", "", s, flags=re.S)
    s = re.sub(r"<title>.*?</title>", f"<title>{html.escape(m.title)}</title>", s, count=1, flags=re.S)
    if "<title>" not in s:
        s = re.sub(r"(<head\b[^>]*>)", rf"\1\n<title>{html.escape(m.title)}</title>", s, count=1)
    e = lambda v: html.escape(v, quote=True)  # noqa: E731
    metas = [("name", "description", m.description), ("name", "keywords", ", ".join(m.keywords)),
             ("name", "author", m.creator), ("name", "copyright", m.copyright), ("name", "robots", "index, follow"),
             ("property", "og:type", "website"), ("property", "og:title", m.title),
             ("property", "og:description", m.description), ("property", "og:site_name", m.publisher),
             ("property", "og:locale", {"en": "en_US"}.get(m.language, m.language.replace("-", "_"))), ("name", "twitter:card", "summary_large_image"),
             ("name", "twitter:title", m.title), ("name", "twitter:description", m.description)]
    if m.url:
        metas.append(("property", "og:url", m.url))
    if m.image:
        metas += [("property", "og:image", m.image), ("property", "og:image:alt", m.alt),
                  ("name", "twitter:image", m.image)]
    x = next((u for k, u in m.profiles.items() if k.lower() in {"x", "twitter"}), "")
    if x:
        metas.append(("name", "twitter:creator", "@" + x.rstrip("/").rsplit("/", 1)[-1]))
    for attr, key, _ in metas:  # drop the page's own copies so each tag appears once
        s = re.sub(rf'\s*<meta\s+{attr}="{re.escape(key)}"[^>]*>', "", s)
    lines = [f'<meta {a}="{k}" content="{e(v)}">' for a, k, v in metas if v]
    if m.url:
        s = re.sub(r'\s*<link\s+rel="canonical"[^>]*>', "", s)
        lines.append(f'<link rel="canonical" href="{e(m.url)}">')
    ld = json.dumps({"@context": "https://schema.org", **jsonld_for(path, m)}, ensure_ascii=False)
    ld = ld.replace("</", "<\\/")
    lines.append(f'<script type="application/ld+json">{ld}</script>')
    block = "\n<!-- metadata:start -->\n" + "\n".join(lines) + "\n<!-- metadata:end -->"
    s = re.sub(r"(</title>)", lambda g: g.group(1) + block, s, count=1)
    path.write_text(s, encoding="utf-8")


def write_sidecar(path: Path, m: Meta) -> Path:
    side = path.with_name(path.name + ".xmp")
    side.write_text(xmp_packet(m), encoding="utf-8")
    return side


# ---------------------------------------------------------------------------------------------------------------------
# Verification: what the viewer sees or hears must not change


def fingerprint(path: Path) -> str:
    ext = path.suffix.lower()
    if ext == ".png" or ext in IMAGE_EXIFTOOL:
        from PIL import Image, ImageSequence

        h = hashlib.sha256()
        with Image.open(path) as im:
            for frame in ImageSequence.Iterator(im):
                h.update(frame.convert("RGBA").tobytes())
        return h.hexdigest()
    if ext in VIDEO or ext in MUTAGEN_AUDIO or ext == ".wav":
        r = subprocess.run(["ffmpeg", "-v", "error", "-i", str(path), "-map", "0", "-c", "copy", "-f", "streamhash",
                            "-hash", "sha256", "-"], capture_output=True, text=True)
        if r.returncode:
            raise RuntimeError(r.stderr.strip())
        return r.stdout
    if ext == ".svg":
        s = path.read_text(encoding="utf-8")
        s = re.sub(r"\s*<(title|desc|metadata)\b[^>]*>.*?</\1>", "", s, flags=re.S)
        return hashlib.sha256(re.sub(r'(<svg\b[^>]*?) role="img"', r"\1", s, count=1).encode()).hexdigest()
    if ext in {".html", ".htm"}:
        s = path.read_text(encoding="utf-8")
        s = re.sub(r"<head\b.*?</head>", "", s, flags=re.S)  # the head is ours to change; the body must not move
        return hashlib.sha256(s.encode()).hexdigest()
    if ext == ".pdf":
        # exiftool writes PDF changes as an incremental update; reverting it gives back the untouched document
        with tempfile.TemporaryDirectory() as tmp:
            copy = Path(tmp) / "doc.pdf"
            shutil.copy2(path, copy)
            subprocess.run(["exiftool", "-q", "-overwrite_original", "-PDF-update:all=", str(copy)], capture_output=True)
            return hashlib.sha256(copy.read_bytes()).hexdigest()
    return hashlib.sha256(path.read_bytes()).hexdigest()


def read_back(path: Path) -> str:
    """Return the title as stored in the file, read with an independent tool."""
    ext = path.suffix.lower()
    if ext in VIDEO:
        r = subprocess.run(["ffprobe", "-v", "error", "-show_entries", "format_tags=title", "-of", "default=nw=1:nk=1",
                            str(path)], capture_output=True, text=True)
        return r.stdout.strip()
    if ext in {".svg"}:
        mm = re.search(r"<title>(.*?)</title>", path.read_text(encoding="utf-8"), re.S)
        return html.unescape(mm.group(1)) if mm else ""
    if ext in {".html", ".htm"}:
        mm = re.search(r"<title>(.*?)</title>", path.read_text(encoding="utf-8"), re.S)
        return html.unescape(mm.group(1)) if mm else ""
    if ext in MUTAGEN_AUDIO or ext == ".wav":
        import mutagen

        f = mutagen.File(path)
        if f is None or f.tags is None:
            return ""
        for key in ("TIT2", "\xa9nam", "title"):
            try:
                v = f.tags.get(key)
            except (KeyError, ValueError):  # Vorbis comments reject keys that aren't valid field names
                continue
            if v:
                return str(v.text[0] if hasattr(v, "text") else v[0])
        return ""
    if shutil.which("exiftool"):
        r = subprocess.run(["exiftool", "-s3", "-XMP-dc:Title", str(path)], capture_output=True, text=True)
        return r.stdout.strip()
    return ""


def writer_for(path: Path):
    ext = path.suffix.lower()
    writer = {".png": write_png, ".wav": write_wav, ".svg": write_svg, ".html": write_html, ".htm": write_html,
              ".pdf": write_exiftool}.get(ext)
    if writer is None:
        writer = (write_exiftool if ext in IMAGE_EXIFTOOL else write_video if ext in VIDEO
                  else write_audio if ext in MUTAGEN_AUDIO else None)
    return writer


def process(path: Path, m: Meta, dry: bool) -> str:
    writer = writer_for(path)
    if writer is None:
        if dry:
            return f"sidecar   {path.name}.xmp"
        return f"sidecar   {write_sidecar(path, m).name}"
    if dry:
        return f"would tag {path.name} ({writer.__name__.removeprefix('write_')})"
    before = fingerprint(path)
    with tempfile.TemporaryDirectory() as tmp:
        backup = Path(tmp) / path.name
        shutil.copy2(path, backup)
        try:
            writer(path, m)
            after = fingerprint(path)
            if after != before:
                raise RuntimeError("content changed while writing metadata")
            got = read_back(path)
            if got and got != m.title:
                raise RuntimeError(f"title read back as {got!r}")
        except Exception:
            shutil.copy2(backup, path)
            raise
    return f"tagged    {path.name}"


def check(path: Path, m: Meta) -> str:
    if writer_for(path) is None:
        side = path.with_name(path.name + ".xmp")
        return f"{'ok' if side.exists() else 'missing sidecar':<20} {path.name}"
    got = read_back(path) if path.suffix.lower() not in {".png"} else ""
    if path.suffix.lower() == ".png":
        keys = {b.split(b"\0", 1)[0].decode("latin-1") for k, b in png_chunks(path.read_bytes()) if k in PNG_TEXT}
        missing = {"Title", "Author", "Copyright", "Description", "XML:com.adobe.xmp"} - keys
        return f"{'ok' if not missing else 'missing ' + ', '.join(sorted(missing)):<20} {path.name}"
    return f"{'ok' if got == m.title else 'missing title':<20} {path.name}"


def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("files", nargs="+", type=Path)
    ap.add_argument("--manifest", type=Path, required=True, help="<campaign-slug>-metadata.json")
    ap.add_argument("--readme", type=Path, help="marketing/README.md with the project block (found by default)")
    ap.add_argument("--jsonld", type=Path, help="also write a schema.org JSON-LD graph of the files")
    ap.add_argument("--dry-run", action="store_true")
    ap.add_argument("--check", action="store_true", help="only report files whose metadata is missing")
    a = ap.parse_args()
    readme = a.readme or find_readme(a.manifest.resolve().parent)
    if not read_project_block(readme):
        print(f"note: no project block in {readme or 'marketing/README.md'}; using the manifest and default credits",
              file=sys.stderr)
    skip = (".xmp", ".jsonld", "-metadata.json")
    files = [f for f in a.files if f.is_file() and not f.name.endswith(skip) and ".meta-tmp" not in f.name]
    failed, graph = 0, []
    for f in files:
        m = load_meta(a.manifest, readme, f)
        try:
            print(check(f, m) if a.check else process(f, m, a.dry_run))
            graph.append(jsonld_for(f, m))
        except Exception as ex:  # noqa: BLE001 - report every file, then fail
            failed += 1
            print(f"FAILED    {f.name}: {ex}", file=sys.stderr)
    if a.jsonld and not a.dry_run and not a.check:
        a.jsonld.write_text(json.dumps({"@context": "https://schema.org", "@graph": graph}, indent=2,
                                       ensure_ascii=False) + "\n", encoding="utf-8")
        print(f"wrote     {a.jsonld}")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
