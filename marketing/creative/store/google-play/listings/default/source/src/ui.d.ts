declare module "*/embed.js" {
  export const setup: (o?: { assetBase?: string; marketingAssetBase?: string; palette?: string; theme?: string }) => Promise<void>;
  export const mount: (el: HTMLElement, id: string, state?: Record<string, unknown>, o?: { frame?: boolean; window?: string; palette?: string; showAds?: boolean }) => Promise<void>;
}
declare module "*/kit/assets.js" {
  export const shapeUrl: (name: string) => string;
}
