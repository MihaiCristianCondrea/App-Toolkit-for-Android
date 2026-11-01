// Generates App Toolkit's icon resources from the toolbox artwork in .idea/icon.svg (Android Studio's project icon):
// the launcher foreground and themed (monochrome) layer, the drawer logo, the widget preview mark, the library's
// animated splash and the web logos in marketing/assets/logos. Run after changing the artwork: npm install && npm run icons
import fs from 'node:fs';
import svgpath from 'svgpath';
import {fileURLToPath} from 'node:url';

const ROOT = fileURLToPath(new URL('../../../../', import.meta.url));
const HEADER = fs.readFileSync(`${ROOT}sample/app/src/main/res/drawable/app_logo.xml`, 'utf8').split('-->')[0] + '-->\n';

// The artwork's parts, in the source SVG's coordinates (viewBox 66.9 176.1 478.2 435.1).
const BOX = {x: 66.9, y: 176.1, w: 478.2, h: 435.1};
const P = {
  handle: 'M167.3,274.1v-43c0-30.4,24.6-55,55-55h167.3c30.4,0,55,24.6,55,55v43h-45.4v-40.6c0-10.5-8.6-19.1-19.1-19.1H231.9c-10.5,0-19.1,8.6-19.1,19.1v40.6H167.3z',
  lid: 'M129.1,266.9h353.8c34.3,0,62.2,27.9,62.2,62.2v22.7c0,7.9-6.5,14.3-14.3,14.3H81.3c-7.9,0-14.3-6.5-14.3-14.3v-22.7C66.9,294.8,94.8,266.9,129.1,266.9z',
  body: 'M81.3,375.7h449.4c7.9,0,14.3,6.5,14.3,14.3v159c0,34.3-27.9,62.2-62.2,62.2H129.1c-34.3,0-62.2-27.9-62.2-62.2V390C66.9,382.1,73.4,375.7,81.3,375.7z',
  latchTop: 'M277.3,323.1h57.4c10.5,0,19.1,8.6,19.1,19.1v23.9h-95.6v-23.9C258.2,331.7,266.8,323.1,277.3,323.1z',
  latchBar: 'M258.2,366.1h95.6v9.6h-95.6z',
  latchBottom: 'M273.7,375.7h64.5V408c0,9.2-7.5,16.7-16.7,16.7h-31.1c-9.2,0-16.7-7.5-16.7-16.7V375.7z',
  wrench: 'M-95.2,86.1l0,38.2c0,9.9,8,17.9,17.9,17.9s17.9-8,17.9-17.9l0-38.2c0.7-1.9,2.8-2.8,4.6-2.1c0.5,0.2,1,0.5,1.4,0.9c23.4,10.8,33.6,38.4,22.9,61.8c-5.2,11.3-14.7,20.1-26.5,24.4l0,80c0,11.2-9.1,20.3-20.3,20.3c-11.2,0-20.3-9.1-20.3-20.3l0-80c-24.2-8.8-36.7-35.5-27.9-59.7c4.3-11.7,13-21.3,24.4-26.5c1.4-1.4,3.6-1.5,5.1-0.2C-95.7,85.2-95.4,85.6-95.2,86.1z',
  grip: 'M151,16.3h19.1c9.9,0,17.9,8,17.9,17.9v64.5c0,9.9-8,17.9-17.9,17.9H151c-9.9,0-17.9-8-17.9-17.9V34.2C133,24.3,141.1,16.3,151,16.3z',
  gripLine: 'M158.1,36.6h4.8c2.6,0,4.8,2.1,4.8,4.8v50.2c0,2.6-2.1,4.8-4.8,4.8h-4.8c-2.6,0-4.8-2.1-4.8-4.8V41.4C153.4,38.8,155.5,36.6,158.1,36.6z',
  shaft: 'M149.8,9.1h21.5v-32.3l10.2-16.7c1.8-3,2-6.6,0.6-9.8l-9-32c-1.1-3.6-4.4-6-8.1-6h-8.8c-3.7,0-7.1,2.4-8.1,6l-9,32c-1.4,3.2-1.2,6.8,0.6,9.8l10.2,16.7V9.1z',
};
// The themed icon is one colour: the handle stops short of the lid by the lid's own gap above the body (9.6), so it
// reads as a handle and not as a bump on the box.
const MONO_HANDLE = 'M167.3,257.3v-26.2c0-30.4,24.6-55,55-55h167.3c30.4,0,55,24.6,55,55v26.2h-45.4v-23.8c0-10.5-8.6-19.1-19.1-19.1H231.9c-10.5,0-19.1,8.6-19.1,19.1v23.8H167.3z';
const C = {handle: '#BF7A00', lid: '#FFF2C6', body: '#FDE7A2', latchTop: '#F6A800', latchBar: '#B36E00', latchBottom: '#C67D05', tool: '#EE9B00', gripLine: '#FFD03B'};
// The tools' own frames (SVG: translate(...) rotate(...)); pivots for the splash animation, in those frames.
const WRENCH = {x: 168, y: 304, r: -38, pivot: [-77.3, 200]};
const DRIVER = {x: 302, y: 358, r: 45, pivot: [160.5, 66]};
const cx = BOX.x + BOX.w / 2, cy = BOX.y + BOX.h / 2;

// ---- Vector drawables ----
const path = (d, color, extra = '') => `<path android:fillColor="${color}" android:pathData="${d}"${extra} />`;
const artwork = (ind, names = false) => {
  const i = ' '.repeat(ind);
  const tool = (t, name, body) => names
    ? `${i}<group android:translateX="${t.x}" android:translateY="${t.y}" android:rotation="${t.r}">\n${i}    <group android:name="${name}" android:pivotX="${t.pivot[0]}" android:pivotY="${t.pivot[1]}" android:rotation="0">\n${body.map((b) => `${i}        ${b}`).join('\n')}\n${i}    </group>\n${i}</group>`
    : `${i}<group android:translateX="${t.x}" android:translateY="${t.y}" android:rotation="${t.r}">\n${body.map((b) => `${i}    ${b}`).join('\n')}\n${i}</group>`;
  return [
    `${i}${path(P.handle, C.handle)}`,
    `${i}${path(P.lid, C.lid)}`,
    `${i}${path(P.body, C.body)}`,
    `${i}${path(P.latchTop, C.latchTop)}`,
    `${i}${path(P.latchBar, C.latchBar)}`,
    `${i}${path(P.latchBottom, C.latchBottom)}`,
    tool(WRENCH, 'wrench', [path(P.wrench, C.tool)]),
    tool(DRIVER, 'screwdriver', [path(P.grip, C.tool), path(P.gripLine, C.gripLine), path(P.shaft, C.tool)]),
  ].join('\n');
};
// Fits the artwork's width to `width` units, centred on (ox, oy).
const fit = (width, ox, oy) => {
  const s = width / BOX.w;
  return {s: +s.toFixed(5), tx: +(ox - cx * s).toFixed(3), ty: +(oy - cy * s).toFixed(3)};
};
const vector = ({size, viewport, comment, body}) => `${HEADER}
${comment ? `<!-- ${comment} -->\n` : ''}<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:width="${size}dp"
    android:height="${size}dp"
    android:viewportWidth="${viewport}"
    android:viewportHeight="${viewport}"
    tools:ignore="VectorRaster,VectorPath">

${body}
</vector>
`;
const fitted = (f, ind = 4) => {
  const i = ' '.repeat(ind);
  return `${i}<group\n${i}    android:scaleX="${f.s}"\n${i}    android:scaleY="${f.s}"\n${i}    android:translateX="${f.tx}"\n${i}    android:translateY="${f.ty}">\n${artwork(ind + 4)}\n${i}</group>`;
};

// Launcher foreground: 108 dp over a 432 viewport; the toolbox 200 units (50 dp) wide, inside the 66 dp safe zone.
const LAUNCHER = fit(200, 216, 216);
const foreground = vector({size: 432, viewport: 432, body: fitted(LAUNCHER)});

// Monochrome: one even-odd shape, so the tools read as cut-outs when the launcher tints the icon.
const inTool = (t, d) => svgpath(d).rotate(t.r).translate(t.x, t.y).round(2).toString();
const mono = [P.lid, P.body, inTool(WRENCH, P.wrench), inTool(DRIVER, P.grip), inTool(DRIVER, P.shaft)].join(' ');
const monochrome = vector({
  size: 432, viewport: 432,
  comment: 'The themed icon: the handle, clear of the lid, and the box with the wrench and screwdriver cut out of it.',
  body: `    <group\n        android:scaleX="${LAUNCHER.s}"\n        android:scaleY="${LAUNCHER.s}"\n        android:translateX="${LAUNCHER.tx}"\n        android:translateY="${LAUNCHER.ty}">\n        <path\n            android:fillColor="#FFFFFFFF"\n            android:pathData="${MONO_HANDLE}" />\n        <path\n            android:fillColor="#FFFFFFFF"\n            android:fillType="evenOdd"\n            android:pathData="${mono}" />\n    </group>`,
});

// The mark beside the name (drawer, about): a square viewport cropped to the artwork, so it fills its slot.
const LOGO = fit(BOX.w, BOX.w / 2, BOX.w / 2);
const logo = vector({
  size: 24, viewport: BOX.w,
  comment: 'The launcher foreground draws the mark inside the adaptive-icon safe zone. This viewport is cropped to the artwork, so the mark fills the slot it is given.',
  body: fitted(LOGO),
});

// ---- SVG for the web (UI kit, marketing assets): the same crop ----
const svgArt = () => [
  `  <path fill="${C.handle}" d="${P.handle}" />`,
  `  <path fill="${C.lid}" d="${P.lid}" />`,
  `  <path fill="${C.body}" d="${P.body}" />`,
  `  <path fill="${C.latchTop}" d="${P.latchTop}" />`,
  `  <path fill="${C.latchBar}" d="${P.latchBar}" />`,
  `  <path fill="${C.latchBottom}" d="${P.latchBottom}" />`,
  `  <path fill="${C.tool}" transform="translate(${WRENCH.x} ${WRENCH.y}) rotate(${WRENCH.r})" d="${P.wrench}" />`,
  `  <g transform="translate(${DRIVER.x} ${DRIVER.y}) rotate(${DRIVER.r})">`,
  `    <path fill="${C.tool}" d="${P.grip}" />`,
  `    <path fill="${C.gripLine}" d="${P.gripLine}" />`,
  `    <path fill="${C.tool}" d="${P.shaft}" />`,
  `  </g>`,
].join('\n');
const pad = (BOX.w - BOX.h) / 2;
const webSvg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="${BOX.x} ${+(BOX.y - pad).toFixed(2)} ${BOX.w} ${BOX.w}">\n${svgArt()}\n</svg>\n`;
const monoSvg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="${BOX.x} ${+(BOX.y - pad).toFixed(2)} ${BOX.w} ${BOX.w}">\n  <path fill="#000" d="${MONO_HANDLE}" />\n  <path fill="#000" fill-rule="evenodd" d="${mono}" />\n</svg>\n`;

// ---- Splash: 288 dp over a 393 viewport, the toolbox inside the 192 dp circle; it hops, the tools rock ----
const SPLASH = fit(200, 196.5, 196.5);
const bottom = +(196.5 + (BOX.h * SPLASH.s) / 2).toFixed(2);
const keyframes = (prop, frames) => frames.map(([f, v]) => `                        <keyframe android:fraction="${f}" android:value="${v}" />`).join('\n');
const animator = (prop, frames) => `                <objectAnimator
                    android:duration="1000"
                    android:interpolator="@android:interpolator/linear"
                    android:propertyName="${prop}"
                    android:repeatCount="infinite"
                    android:valueType="floatType">
                    <propertyValuesHolder android:propertyName="${prop}">
${keyframes(prop, frames)}
                    </propertyValuesHolder>
                </objectAnimator>`;
const target = (name, animators) => `    <target android:name="${name}">
        <aapt:attr name="android:animation">
            <set android:ordering="together">
${animators.join('\n')}
            </set>
        </aapt:attr>
    </target>`;
const rock = (sign) => [[0, 0], [0.3, 0], [0.45, sign * 10], [0.6, sign * -6], [0.72, sign * 3], [0.84, 0], [1, 0]];
const splash = `${HEADER}
<!-- App Toolkit's splash: the toolbox hops once a second while the wrench and screwdriver rock. -->
<animated-vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt">

    <aapt:attr name="android:drawable">
        <vector
            android:width="288dp"
            android:height="288dp"
            android:viewportWidth="393"
            android:viewportHeight="393">

            <!-- Scales from the box's bottom edge, so it squashes on the ground it lands on. -->
            <group
                android:name="toolbox"
                android:pivotX="196.5"
                android:pivotY="${bottom}"
                android:scaleY="1"
                android:translateY="0">
                <group
                    android:scaleX="${SPLASH.s}"
                    android:scaleY="${SPLASH.s}"
                    android:translateX="${SPLASH.tx}"
                    android:translateY="${SPLASH.ty}">
${artwork(20, true)}
                </group>
            </group>
        </vector>
    </aapt:attr>

${target('toolbox', [
  animator('translateY', [[0, 0], [0.3, 0], [0.4, -2], [0.64, -7], [0.72, -7], [0.82, -4], [0.92, -1], [1, 0]]),
  animator('scaleY', [[0, 1], [0.3, 1], [0.4, 1.01], [0.64, 1.02], [0.72, 1.02], [0.92, 0.98], [1, 1]]),
])}

${target('wrench', [animator('rotation', rock(-1))])}

${target('screwdriver', [animator('rotation', rock(1))])}
</animated-vector>
`;

const write = (rel, text) => { fs.writeFileSync(`${ROOT}${rel}`, text); console.log('wrote', rel); };
const res = 'sample/app/src/main/res';
write(`${res}/drawable-anydpi/ic_launcher_foreground.xml`, foreground);
write(`${res}/drawable-anydpi/ic_launcher_monochrome.xml`, monochrome);
write(`${res}/drawable/app_logo.xml`, logo);
write('sample/widget/src/main/res/drawable/widget_preview_app_mark.xml',
  foreground.replace('<vector', "<!-- The app's mark for the widget's preview. A copy of :sample:app's ic_launcher_foreground: the widget module cannot use the app's resources. -->\n<vector"));
write('library/apptoolkit/src/main/res/drawable/anim_splash.xml', splash);
for (const dir of ['marketing/assets/logos']) {
  write(`${dir}/icon.svg`, webSvg);
  write(`${dir}/app_logo.svg`, webSvg);
}
