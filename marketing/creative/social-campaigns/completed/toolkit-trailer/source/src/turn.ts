import { smooth } from "./lib/anim";

/**
 * A phone turning through edge-on into another screen, as one continuous angle on one curve: from yaw `from`
 * round to half a turn past `to`. Past edge-on the second screen faces us, so the yaw shown is 180 less; the
 * angular velocity is the same either side of the swap. Nothing is rounded. scripts/verify-wheel.mjs samples it.
 */
export const throughEdge = (u: number, from: number, to: number) => {
  const theta = from + (to + 180 - from) * smooth(u), second = theta > 90;
  return { yaw: second ? theta - 180 : theta, second };
};
