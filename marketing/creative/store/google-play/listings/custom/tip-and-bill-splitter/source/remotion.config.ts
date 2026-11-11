import {Config} from "@remotion/cli/config";
import {inlineFonts} from "./scripts/bundle.mjs";
Config.setStillImageFormat("png");
Config.setOverwriteOutput(true);
Config.overrideWebpackConfig(inlineFonts);
