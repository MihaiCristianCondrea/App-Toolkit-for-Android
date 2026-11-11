import enUS from "../strings/en-US.json";

export type Strings = typeof enUS;
// One source for every language: add strings/<locale>.json and list it here.
export const STRINGS: Record<string, Strings> = { "en-US": enUS };
export const LOCALES = Object.keys(STRINGS);
