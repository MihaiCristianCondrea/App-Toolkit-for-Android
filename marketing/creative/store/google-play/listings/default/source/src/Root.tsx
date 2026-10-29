import React from "react";
import { Still } from "remotion";
import { FeatureGraphic } from "./FeatureGraphic";
import { Icon } from "./Icon";
import { HEIGHT, PhonePanorama, PhoneShot, WIDTH } from "./Phone";
import { STRINGS } from "./strings";
import { CANVAS, WideShot, WideStrip } from "./Wide";

const en = STRINGS["en-US"];

// Every Google Play graphic as a still. scripts/render.mjs renders each one per language; Remotion Studio previews them.
export const RemotionRoot: React.FC = () => (
  <>
    <Still id="PhoneShot" component={PhoneShot} width={WIDTH} height={HEIGHT} defaultProps={{ locale: "en-US", index: 0 }} />
    <Still id="PhonePanorama" component={PhonePanorama} width={WIDTH * en.screenshots.length} height={HEIGHT} defaultProps={{ locale: "en-US" }} />
    <Still id="TabletShot" component={WideShot} width={CANVAS.w} height={CANVAS.h} defaultProps={{ locale: "en-US", kind: "tablet" as const, index: 0 }} />
    <Still id="TabletStrip" component={WideStrip} width={CANVAS.w * en.tabletScreenshots.length} height={CANVAS.h} defaultProps={{ locale: "en-US", kind: "tablet" as const }} />
    <Still id="DesktopShot" component={WideShot} width={CANVAS.w} height={CANVAS.h} defaultProps={{ locale: "en-US", kind: "desktop" as const, index: 0 }} />
    <Still id="DesktopStrip" component={WideStrip} width={CANVAS.w * en.desktopScreenshots.length} height={CANVAS.h} defaultProps={{ locale: "en-US", kind: "desktop" as const }} />
    <Still id="FeatureGraphic" component={FeatureGraphic} width={1024} height={500} defaultProps={{ locale: "en-US" }} />
    <Still id="Icon" component={Icon} width={512} height={512} />
  </>
);
