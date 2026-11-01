import { asset } from "./assets.js";

export const RECOMMENDATION_SHAPES = ["arch", "arrow", "boom", "bun", "burst", "circle", "clam_shell", "clover_4leaf", "clover_8leaf", "cookie_12sided", "cookie_4sided", "cookie_6sided", "cookie_7sided", "cookie_9sided", "diamond", "fan", "flower", "gem", "ghostish", "heart", "oval", "pentagon", "pill", "pixel_circle", "pixel_triangle", "puffy", "puffy_diamond", "semi_circle", "slanted", "soft_boom", "soft_burst", "square", "sunny", "triangle", "very_sunny"];
export const recommendationMask = value => asset(`assets/shapes/${RECOMMENDATION_SHAPES.includes(value) ? value : "square"}.svg`);
