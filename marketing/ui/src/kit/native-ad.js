import {html} from 'lit';
import {shapeMaskStyle, shapeUrl} from './assets.js';

/** Static native-ad layout for the prototype's Show ads switch; no network ad request. */
export const nativeAdPreview = () => html`
  <section class="qt-section" aria-label="Sponsored content" data-native-ad>
    <h2 class="qt-section__title"><md-icon class="sc-rounded" aria-hidden="true">campaign</md-icon>Sponsored content</h2>
    <div class="qt-ad">
      <div class="qt-ad__face" style=${shapeMaskStyle(shapeUrl('cookie_9sided'))}><md-icon>apps</md-icon></div>
      <div class="qt-ad__text">
        <h3 class="qt-ad__headline">Advertiser app name</h3>
        <p class="qt-ad__body">One short line from the advertiser about what the app does.</p>
        <div class="qt-ad__meta"><md-icon>star</md-icon>4.6 · Free</div>
      </div>
      <md-filled-tonal-button class="qt-ad__cta">Install</md-filled-tonal-button>
    </div>
  </section>`;
