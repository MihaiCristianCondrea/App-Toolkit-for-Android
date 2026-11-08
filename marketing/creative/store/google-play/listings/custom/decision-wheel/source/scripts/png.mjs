// Minimal PNG reading and writing on Node's zlib, for the verifier's pixel checks and contact sheets:
// 8-bit RGB or RGBA, non-interlaced, as Remotion writes them. No dependency.
import { deflateSync, inflateSync } from "node:zlib";
import { readFile, writeFile } from "node:fs/promises";

const CRC = Array.from({ length: 256 }, (_, n) => { let c = n; for (let k = 0; k < 8; k++) c = c & 1 ? 0xedb88320 ^ (c >>> 1) : c >>> 1; return c >>> 0; });
const crc = (buf) => { let c = 0xffffffff; for (const b of buf) c = CRC[(c ^ b) & 0xff] ^ (c >>> 8); return (c ^ 0xffffffff) >>> 0; };

/** Decodes to { width, height, data } with 4 bytes (RGBA) per pixel. */
export const readPng = async (file) => {
  const buf = await readFile(file);
  let pos = 8, width = 0, height = 0, type = 0;
  const idat = [];
  while (pos < buf.length) {
    const len = buf.readUInt32BE(pos), kind = buf.toString("ascii", pos + 4, pos + 8), body = buf.subarray(pos + 8, pos + 8 + len);
    if (kind === "IHDR") {
      [width, height, type] = [body.readUInt32BE(0), body.readUInt32BE(4), body[9]];
      if (body[8] !== 8 || body[12] !== 0 || (type !== 2 && type !== 6)) throw new Error(`${file}: only 8-bit RGB(A), non-interlaced PNGs are supported`);
    }
    if (kind === "IDAT") idat.push(body);
    pos += 12 + len;
  }
  const bpp = type === 6 ? 4 : 3, stride = width * bpp, raw = inflateSync(Buffer.concat(idat)), px = Buffer.alloc(height * stride);
  for (let y = 0; y < height; y++) {
    const f = raw[y * (stride + 1)], line = raw.subarray(y * (stride + 1) + 1, (y + 1) * (stride + 1)), out = px.subarray(y * stride, (y + 1) * stride), up = y ? px.subarray((y - 1) * stride, y * stride) : null;
    for (let x = 0; x < stride; x++) {
      const a = x >= bpp ? out[x - bpp] : 0, b = up ? up[x] : 0, c = up && x >= bpp ? up[x - bpp] : 0;
      const p = a + b - c, pa = Math.abs(p - a), pb = Math.abs(p - b), pc = Math.abs(p - c);
      out[x] = (line[x] + [0, a, b, (a + b) >> 1, pa <= pb && pa <= pc ? a : pb <= pc ? b : c][f]) & 0xff;
    }
  }
  const data = Buffer.alloc(width * height * 4);
  for (let i = 0; i < width * height; i++) { data[i * 4] = px[i * bpp]; data[i * 4 + 1] = px[i * bpp + 1]; data[i * 4 + 2] = px[i * bpp + 2]; data[i * 4 + 3] = bpp === 4 ? px[i * bpp + 3] : 255; }
  return { width, height, data };
};

export const writePng = async (file, { width, height, data }) => {
  const raw = Buffer.alloc(height * (width * 4 + 1));
  for (let y = 0; y < height; y++) data.copy(raw, y * (width * 4 + 1) + 1, y * width * 4, (y + 1) * width * 4);
  const chunk = (kind, body) => { const len = Buffer.alloc(4); len.writeUInt32BE(body.length); const kb = Buffer.concat([Buffer.from(kind, "ascii"), body]); const c = Buffer.alloc(4); c.writeUInt32BE(crc(kb)); return Buffer.concat([len, kb, c]); };
  const ihdr = Buffer.alloc(13); ihdr.writeUInt32BE(width, 0); ihdr.writeUInt32BE(height, 4); ihdr[8] = 8; ihdr[9] = 6;
  await writeFile(file, Buffer.concat([Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]), chunk("IHDR", ihdr), chunk("IDAT", deflateSync(raw, { level: 6 })), chunk("IEND", Buffer.alloc(0))]));
};

/** Box-filter downscale by an integer factor. */
export const shrink = (img, k) => {
  const width = Math.floor(img.width / k), height = Math.floor(img.height / k), data = Buffer.alloc(width * height * 4);
  for (let y = 0; y < height; y++) for (let x = 0; x < width; x++) for (let c = 0; c < 4; c++) {
    let s = 0; for (let j = 0; j < k; j++) for (let i = 0; i < k; i++) s += img.data[((y * k + j) * img.width + x * k + i) * 4 + c];
    data[(y * width + x) * 4 + c] = Math.round(s / (k * k));
  }
  return { width, height, data };
};

/** Images side by side with a gap, on a white sheet. */
export const sheet = (imgs, gap) => {
  const width = imgs.reduce((a, i) => a + i.width, 0) + gap * (imgs.length + 1), height = Math.max(...imgs.map((i) => i.height)) + 2 * gap, data = Buffer.alloc(width * height * 4, 255);
  let x0 = gap;
  for (const img of imgs) { for (let y = 0; y < img.height; y++) img.data.copy(data, ((y + gap) * width + x0) * 4, y * img.width * 4, (y + 1) * img.width * 4); x0 += img.width + gap; }
  return { width, height, data };
};
