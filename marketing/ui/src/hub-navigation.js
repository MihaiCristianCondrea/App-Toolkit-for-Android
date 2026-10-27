import {html} from "lit";
import "material-next-web-components/app-shell.js";

export const hubNavigation = (active) => html`
  <mnw-navigation-item href="./" icon="grid_view" ?active=${active === "screens"}>Screens</mnw-navigation-item>
  <mnw-navigation-item href="motion.html" icon="animation" ?active=${active === "motion"}>Motion</mnw-navigation-item>
  <mnw-navigation-item href="specs.html" icon="palette" ?active=${active === "specs"}>Specifications</mnw-navigation-item>
`;
