import { Config } from "@remotion/cli/config";

// 2K output from a 1080p layout: 1920x1080 renders at 2560x1440 ("Render quality" in marketing/README.md).
Config.setScale(4 / 3);
// Lossless frames: JPEG frames soften the UI kit's text and edges before the video encoder sees them.
Config.setVideoImageFormat("png");
Config.setOverwriteOutput(true);
