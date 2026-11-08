import { Config } from "@remotion/cli/config";

// Stills are PNG so text and edges stay sharp; scripts/render.mjs flattens the ones Play wants without alpha.
Config.setStillImageFormat("png");
Config.setOverwriteOutput(true);
