import {html} from "lit";
import "material-next-web-components/app-shell.js";

export const hubNavigation = (active) => html`
  <mnw-navigation-item href="./" icon="grid_view" ?active=${active === "screens"}>Screens</mnw-navigation-item>
`;
