import {ScreenHost as MaterialNextScreenHost, createScreenRegistry} from "material-next-web-components/marketing.js";
import {SCREENS} from "./screens/index.js";

const registry = createScreenRegistry(SCREENS);

/** Product registry for the motion page's live previews. */
export class ScreenHost extends MaterialNextScreenHost {
  constructor(el, options) {
    super(el, {registry, ...options});
  }
}
