import { IDENTITY } from "../config/identity.js";
import { MATERIAL_SHAPES } from "./material-shapes.js";

/**
 * Base URL for fonts, illustrations and logos: the identity's, so its logo and fonts move with everything else.
 * Vite serves public/ at "/"; other hosts (e.g. Remotion's staticFile) call setAssetBase() before rendering.
 */
export const setAssetBase = (b) => IDENTITY.setAssetBase(b);
export const asset = (path) => IDENTITY.asset(path);

/**
 * MaterialShapes as data-URI masks, from the outlines Compose draws (material-shapes.js), so a tool's shape in
 * the kit is the shape the app cuts its face from. An unknown name falls back to the nine-sided cookie.
 */
const cache = new Map();
export const shapeUrl = (name) => {
  if (cache.has(name)) return cache.get(name);
  const d = MATERIAL_SHAPES[name] || MATERIAL_SHAPES.cookie_9sided;
  const url = 'data:image/svg+xml,' + encodeURIComponent('<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><path d="' + d + '"/></svg>');
  cache.set(name, url);
  return url;
};
export const shapeMaskStyle = (url) =>
  `-webkit-mask:url("${url}") center / contain no-repeat;mask:url("${url}") center / contain no-repeat;`;

