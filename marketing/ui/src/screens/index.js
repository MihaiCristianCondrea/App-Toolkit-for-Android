// Screen registry. Each screen module exports { id, title, tier, description, defaults, render(state) }.
import numbers from "./numbers.js";
import quickTools from "./quick-tools.js";
import play from "./play.js";

export const SCREENS = [quickTools, numbers, play];
export const byId = (id) => SCREENS.find((s) => s.id === id);
