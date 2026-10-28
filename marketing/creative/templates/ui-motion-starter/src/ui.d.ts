declare module "*/embed.js" {
  export const setup: (o?: { assetBase?: string; marketingAssetBase?: string; palette?: string; theme?: string }) => Promise<void>;
  export const mount: (el: HTMLElement, id: string, state?: Record<string, unknown>, o?: { frame?: boolean }) => Promise<void>;
  export const SCREENS: { id: string; title: string }[];
}
