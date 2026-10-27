import {html} from 'lit';
import 'material-next-web-components/app-shell.js';
import {installHubStyles} from 'material-next-web-components/marketing.js';

installHubStyles();

/** Reference pages: the shared top app bar with a way back to the gallery, no drawer or footer. */
export const referenceShell = ({heading}, content) => html`
  <mnw-app-shell heading=${heading} navigation-mode="none" leading="back"
    @back-request=${() => location.assign('./')}>
    ${content}
  </mnw-app-shell>`;
