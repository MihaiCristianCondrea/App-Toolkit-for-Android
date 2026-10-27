/**
 * Base URL for fonts, illustrations and logos. Vite serves public/ at "/";
 * other hosts (e.g. Remotion's staticFile) call setAssetBase() before rendering.
 */
let base = "/";
export const setAssetBase = (b) => {
  base = b.endsWith("/") ? b : b + "/";
};
export const asset = (path) => base + (path.startsWith("/") ? path.slice(1) : path);

export const shapeUrl = (name) => asset(`shapes/${name}.svg`);
export const shapeMaskStyle = (url) =>
  `-webkit-mask:url("${url}") center / contain no-repeat;mask:url("${url}") center / contain no-repeat;`;

/** Guide artwork (hosted on i.ibb.co) is synced into public/images; other remote images stay remote. */
export const resolveImageUrl = (url) => {
  if (!url) return null;
  if (/^https?:/.test(url) && !/^https:\/\/i\.ibb\.co\//.test(url)) return url;
  const match = url.match(/\/([^/?#]+\.(png|jpg|jpeg|webp))$/i);
  if (match) return asset(`images/${match[1]}`);
  return url;
};
