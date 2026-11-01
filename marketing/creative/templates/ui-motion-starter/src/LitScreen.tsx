import React, { useLayoutEffect, useRef } from "react";
import { staticFile, useDelayRender } from "remotion";
import { mount, setup } from "../../../../ui/src/embed.js";

// One-time setup: fonts, Material Web, and the asset base Remotion serves public/ from.
// The device frames are in public/material-next/, which `npm run assets` in marketing/ui fills.
const assetBase = staticFile("fonts/GoogleSansFlex.ttf").replace("fonts/GoogleSansFlex.ttf", "");
const ready = setup({ assetBase, marketingAssetBase: `${assetBase}material-next/` });

/**
 * Renders a marketing/ui screen as a pure function of `state`. Each frame waits for
 * Material Web to finish updating before Remotion captures it.
 */
export const LitScreen: React.FC<{
  id: string;
  state?: Record<string, unknown>;
  frame?: boolean;
  style?: React.CSSProperties;
}> = ({ id, state = {}, frame = true, style }) => {
  const ref = useRef<HTMLDivElement>(null);
  const { delayRender, continueRender } = useDelayRender();
  const key = JSON.stringify(state);
  useLayoutEffect(() => {
    const handle = delayRender(`Rendering ${id}`);
    ready
      .then(() => mount(ref.current!, id, JSON.parse(key), { frame }))
      .then(() => continueRender(handle));
  }, [id, key, frame, delayRender, continueRender]);
  return <div ref={ref} style={style} />;
};
