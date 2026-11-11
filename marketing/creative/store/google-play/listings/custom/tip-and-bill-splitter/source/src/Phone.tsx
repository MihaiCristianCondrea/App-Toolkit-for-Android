import React, { useLayoutEffect, useRef } from "react";
import { AbsoluteFill, cancelRender, continueRender, delayRender } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { STRINGS } from "./strings";
import { Glass } from "./Glass";
import { Shot } from "./Shot";
import { CAPTION, FRAME, fitPose, measure } from "./composition/geometry";
import type { ScreenUi } from "./composition/geometry";
import ui from "./composition/ui.json";
import { Shape } from "./Shape";
import { SCREENS } from "./screens";
import { SHOTS } from "./composition/shots";
import { FONT, INK, ON_PILL, PILL, CYAN, BUTTER } from "./theme";

export const WIDTH=1080,HEIGHT=1920;
const ICON_DROP="-.1em";

/**
 * The whole strip of phone screenshots: the glass ground across all of them, each screenshot's solved
 * composition (src/Shot.tsx, from src/composition/), then the captions. `only` draws one screenshot's
 * foreground; the ground is always drawn whole, so the seams meet. The ribbons fade behind every caption's
 * text block. `captions={false}` leaves the text out (the verifier measures the background under it);
 * `debug` overlays each composition's measurements.
 */
export const PhonePanorama:React.FC<{locale:string;only?:number;debug?:boolean;captions?:boolean;exploration?:boolean}>=({locale,only,debug,captions=true,exploration=false})=>{
 useFonts();
 if(exploration)return <AlternativeHero locale={locale}/>;
 const strings=STRINGS[locale],n=strings.screenshots.length;
 if(n!==SHOTS.length)throw new Error(`${locale} has ${n} screenshots; the layout has ${SHOTS.length}`);
 const text=SHOTS.map((spec,i)=>{const b=CAPTION[spec.caption].block;return {left:b.left+i*WIDTH,top:b.top,right:b.right+i*WIDTH,bottom:b.bottom};});
 return <AbsoluteFill style={{width:n*WIDTH,fontFamily:FONT,overflow:"hidden"}}>
  <Glass width={n*WIDTH} height={HEIGHT} panels={n} text={text} variant={exploration?"flow":"paired"}/>
  {SHOTS.map((spec,i)=>(only===undefined||only===i)&&(
   <div key={spec.id} style={{position:"absolute",left:i*WIDTH,top:0,width:WIDTH,height:HEIGHT}}><Shot index={i} panels={n} debug={debug} exploration={exploration}/></div>
  ))}
  {captions&&strings.screenshots.map((s,i)=>(
   <div key={s.id} style={{position:"absolute",left:i*WIDTH,top:0,width:WIDTH,height:HEIGHT,pointerEvents:"none",zIndex:20}}>
    <Caption lines={s.lines} highlight={s.highlight} icon={s.icon} at={SHOTS[i].caption} check={only===i}/>
   </div>
  ))}
 </AbsoluteFill>;
};

/**
 * Two short lines, centred in the caption box. One word or short phrase of them sits on a pill with the Material Symbol
 * of the screen it describes, the way Google's own apps caption their store screenshots. No commas, full stops or
 * dashes: the line break carries the pause. The render fails if the highlight is not in the lines exactly once, if a line has
 * punctuation or a dash, if the caption leaves its box, or if it covers over 20% of the image, Play's limit.
 */
const Caption: React.FC<{ lines: string[]; highlight: string; icon: string; at: "top" | "bottom"; check: boolean }> = ({ lines, highlight, icon, at, check }) => {
  const band = CAPTION[at].band, low = at === "bottom";
  const ref = useRef<HTMLHeadingElement>(null);
  const found = lines.flatMap((line, i) => (line.split(highlight).length - 1 === 1 ? [i] : []));
  const total = lines.reduce((sum, line) => sum + line.split(highlight).length - 1, 0);
  useLayoutEffect(() => {
    if (!check) return;
    const handle = delayRender("Measuring the caption");
    if (total !== 1) {
      cancelRender(new Error(`The highlight "${highlight}" must appear exactly once in the caption "${lines.join(" ")}"`));
      return;
    }
    if (/[,.;:\u2010-\u2015-]/.test(lines.join(""))) {
      cancelRender(new Error(`The caption "${lines.join(" ")}" has punctuation or a dash; the line break carries the pause`));
      return;
    }
    document.fonts.ready.then(() => {
      const r = ref.current!.getBoundingClientRect();
      const share = (r.width * r.height) / (WIDTH * HEIGHT);
      const box = ref.current!.parentElement!.getBoundingClientRect();
      const inside = r.left - box.left >= band.left && r.right - box.left <= band.right && r.top - box.top >= band.top && r.bottom - box.top <= band.bottom;
      if (share > 0.2) cancelRender(new Error(`Caption covers ${(share * 100).toFixed(1)}% of the image; Play allows 20%`));
      else if (!inside) cancelRender(new Error(`The caption "${lines.join(" ")}" leaves its box; shorten it`));
      else continueRender(handle);
    });
  }, [check, total, highlight, lines, band]);
  // An inline block shares the line's baseline, so the highlighted word sits level with the words around it; the
  // padding, not an offset, sizes the pill around it.
  const pill = (
    <b style={{ display: "inline-block", margin: "0 .08em", padding: ".02em .3em .1em .2em", lineHeight: 1.04, borderRadius: 999, background: PILL, color: ON_PILL, fontWeight: 600, whiteSpace: "nowrap" }}>
      <span aria-hidden style={{ display: "inline-block", marginRight: ".16em", fontFamily: "'Material Symbols Rounded'", fontWeight: 400, fontSize: ".72em", lineHeight: 1, verticalAlign: ICON_DROP, fontVariationSettings: "'FILL' 1, 'wght' 500, 'opsz' 48" }}>{icon}</span>
      {highlight}
    </b>
  );
  return (
    <h1 ref={ref} style={{ position: "absolute", left: "50%", ...(low ? { bottom: HEIGHT - band.bottom } : { top: band.top }), transform: "translateX(-50%)", width: "max-content", maxWidth: band.right - band.left, margin: 0, zIndex: 2, color: INK, fontSize: 76, lineHeight: 1.36, letterSpacing: -1.5, fontWeight: 560, textAlign: "center" }}>
      {lines.map((line, i) => {
        if (i !== found[0]) return <span key={i} style={{ display: "block" }}>{line}</span>;
        const [before, after] = line.split(highlight);
        return <span key={i} style={{ display: "block" }}>{before}{pill}{after}</span>;
      })}
    </h1>
  );
};

/** One phone screenshot: the panorama, moved so screenshot `index` fills the frame. `debug` overlays the hero's geometry. */
export const PhoneShot: React.FC<{ locale: string; index: number; debug?: boolean; captions?: boolean; exploration?: boolean }> = ({ locale, index, debug, captions, exploration }) => (
  <AbsoluteFill style={{ overflow: "hidden" }}>
    <div style={{ position: "absolute", left: -index * WIDTH, top: 0, height: HEIGHT }}>
      <PhonePanorama locale={locale} only={index} debug={debug} captions={captions} exploration={exploration} />
    </div>
  </AbsoluteFill>
);
/** An alternate, lower-caption study with a larger tonal surface and a contrasting device direction. */
const AlternativeHero:React.FC<{locale:string}>=({locale})=>{
 const s=STRINGS[locale].screenshots[0],screen=(ui as Record<string,ScreenUi>).dinner;
 const pose=fitPose({pitch:4,yaw:8,roll:2,perspective:3000},screen,{width:650,x:m=>m.centroid[0],xTarget:540,y:m=>(m.full.top+m.full.bottom)/2,yTarget:(89+CAPTION.bottom.block.top-144)/2});
 const m=measure(pose,screen);
 if(m.full.left<34||m.full.right>1046||m.full.top<34||m.full.bottom>CAPTION.bottom.block.top-89)throw Error("Alternate hero leaves safe bounds");
 return <AbsoluteFill style={{width:WIDTH,background:"#FFEBDD",fontFamily:FONT,overflow:"hidden"}}>
  <Shape name="pill" x={30} y={410} size={700} color={CYAN} rotate={-25}/>
  <Shape name="cookie_6sided" x={674} y={1050} size={340} color={BUTTER} rotate={12}/>
  <div style={{position:"absolute",left:pose.x-FRAME.width/2,top:pose.y-FRAME.height/2,width:FRAME.width,height:FRAME.height,transform:`rotate(${pose.roll}deg) scale(${pose.scale})`,filter:"drop-shadow(0 21px 21px rgba(21,63,60,.16))"}}>
   <LitScreen id={SCREENS.dinner[0]} state={SCREENS.dinner[1]} pose={pose} style={{width:FRAME.width}}/>
  </div>
  <Caption lines={s.lines} highlight={s.highlight} icon={s.icon} at="bottom" check={true}/>
 </AbsoluteFill>;
};
