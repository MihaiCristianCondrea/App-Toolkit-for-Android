// Screen registry. Each screen module exports { id, title, tier, source, description, defaults, render(state) }.
import numbers from "./numbers.js";
import quickTools from "./quick-tools.js";
import apps from "./apps.js";
import components from "./components.js";

export const SCREENS = [numbers, quickTools, apps, components];
export const byId = (id) => SCREENS.find((s) => s.id === id);
