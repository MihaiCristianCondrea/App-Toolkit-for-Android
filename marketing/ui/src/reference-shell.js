import {html} from 'lit';
import 'material-next-web-components/site-footer.js';
import {installHubStyles} from 'material-next-web-components/marketing.js';
import {PRODUCT_CONFIG} from './config/product.js';
import {hubNavigation} from './hub-navigation.js';

installHubStyles();

/** Reference pages share the app shell without adding an empty screen catalog. */
export const referenceShell = ({heading, active, theme, onTheme}, content) => html`
  <mnw-app-shell heading=${heading} navigation-mode="modal" active-path=${location.pathname}>
    <a slot="brand" href="./" class="reference-brand"><img src=${PRODUCT_CONFIG.icon} width="32" height="32" alt="" />${PRODUCT_CONFIG.name}</a>
    <div slot="navigation">${hubNavigation(active)}</div>
    <mnw-app-shell-footer slot="navigation-footer" .theme=${theme}
      privacy-href=${PRODUCT_CONFIG.privacyUrl} code-of-conduct-href=${PRODUCT_CONFIG.codeOfConductUrl}
      @theme-change=${e => onTheme(e.detail.theme)}></mnw-app-shell-footer>
    ${content}
    <mnw-site-footer slot="footer"></mnw-site-footer>
  </mnw-app-shell>`;
