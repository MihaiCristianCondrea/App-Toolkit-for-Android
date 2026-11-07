import React, { useLayoutEffect, useRef } from "react";
import { AbsoluteFill, cancelRender, continueRender, delayRender } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { STRINGS } from "./strings";
import { WaterArt, type Ripple } from "./WaterArt";
import { FONT, INK, ON_PILL, PILL } from "./theme";

export const WIDTH=1080,HEIGHT=1920;
const CAPTION={left:120,right:960,top:72,bottom:312};
const ICON_DROP="-.1em";
// Local CSS pixels, before the renderer's 2x output scale. Every visible subject is checked after layout.
const SAFE={left:40,right:1040,top:360,bottom:1880};
// A phone may run off the bottom or a side on purpose (a close view); nothing may enter the caption band.
const BLEED={left:-1200,right:2280,top:360,bottom:3000};
const FRAME_W=465.23;

/** The one phone in a shot: where it stands, how large, its turn, and whether it may run off the edge. */
type Device={x:number;top:number;scale:number;yaw?:number;pitch?:number;bleed?:boolean};
type Shot={device:Device;ripples:Ripple[]};

// One phone and one screen per image. The set varies through framing, the way a designer sequences a story: a whole
// hero, then closer and closer views that run off the bottom or a side, each with its rings in a new place. The Water
// Eject story (1 to 5) carries the ripples, each set spilling into its neighbours; the toolbox (6 to 8) keeps only the
// last faint rings. Poses are turns a person would make to show a phone, never a roll.
const SHOTS:Shot[]=[
 // 1 Hero: the whole phone, straight on, rings rising from its speaker.
 {device:{x:540,top:420,scale:1.36},ripples:[{x:540,y:1780,reach:1250,strength:1}]},
 // 2 The tone: turned to its edge, set right so the rings travel on the left.
 {device:{x:600,top:440,scale:1.4,yaw:-24,pitch:3},ripples:[{x:280,y:1380,reach:1150,strength:.95}]},
 // 3 Up to 30 seconds: close and centred, the bezel running off the bottom, the countdown at the heart of the image.
 {device:{x:540,top:400,scale:1.55,bleed:true},ripples:[{x:540,y:1560,reach:900,strength:.55}]},
 // 4 Control: turned the other way and running off the left edge, Stop in full view.
 {device:{x:280,top:440,scale:1.5,yaw:16,pitch:2,bleed:true},ripples:[{x:900,y:1700,reach:1000,strength:.8}]},
 // 5 Guidance: running off the right edge, its guidance in full view at reading size.
 {device:{x:800,top:400,scale:1.5,yaw:-10,bleed:true},ripples:[{x:150,y:1850,reach:900,strength:.6}]},
 // 6 The toolbox: the whole phone again, a breath after the close views.
 {device:{x:540,top:430,scale:1.34,yaw:-10},ripples:[{x:-40,y:1820,reach:900,strength:.4}]},
 // 7 One everyday tool, close and running off the left edge.
 {device:{x:280,top:440,scale:1.5,yaw:14,pitch:2,bleed:true},ripples:[]},
 // 8 Bookend: whole and straight on, the last faint rings.
 {device:{x:540,top:430,scale:1.34},ripples:[{x:540,y:1880,reach:900,strength:.4}]},
];
export const PhonePanorama:React.FC<{locale:string;only?:number}>=({locale,only})=>{
 useFonts();
 const strings=STRINGS[locale],n=strings.screenshots.length;
 if(SHOTS.length!==n) throw new Error(`${n} captions for ${SHOTS.length} compositions`);
 const ripples=SHOTS.flatMap((shot,i)=>shot.ripples.map(r=>({...r,x:r.x+i*WIDTH})));
 return <AbsoluteFill style={{width:n*WIDTH,fontFamily:FONT,overflow:"hidden"}}>
  <WaterArt width={n*WIDTH} height={HEIGHT} ripples={ripples}/>
  {strings.screenshots.map((s,i)=>(
   <div key={s.id} data-shot={i} style={{position:"absolute",left:i*WIDTH,top:0,width:WIDTH,height:HEIGHT}}>
    <Caption lines={s.lines} highlight={s.highlight} icon={s.icon} check={only===i}/>
    {(only===undefined||only===i)&&(()=>{
     const d=SHOTS[i].device;
     return <div style={{position:"absolute",left:d.x,top:d.top,width:FRAME_W,zIndex:1,transform:`translateX(-50%) scale(${d.scale})`,transformOrigin:"50% 0",filter:"drop-shadow(0 26px 30px rgba(0,66,77,.12))"}}>
      <LitScreen id={SCREENS[s.id][0]} state={SCREENS[s.id][1]} safeBounds={d.bleed?BLEED:SAFE} pose={{yaw:d.yaw??0,pitch:d.pitch??0}} style={{width:FRAME_W}}/>
     </div>;
    })()}
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
const Caption: React.FC<{ lines: string[]; highlight: string; icon: string; check: boolean }> = ({ lines, highlight, icon, check }) => {
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
      const inside = r.left - box.left >= CAPTION.left && r.right - box.left <= CAPTION.right && r.top - box.top >= CAPTION.top && r.bottom - box.top <= CAPTION.bottom;
      if (share > 0.2) cancelRender(new Error(`Caption covers ${(share * 100).toFixed(1)}% of the image; Play allows 20%`));
      else if (!inside) cancelRender(new Error(`The caption "${lines.join(" ")}" leaves its box; shorten it`));
      else continueRender(handle);
    });
  }, [check, total, highlight, lines]);
  // An inline block shares the line's baseline, so the highlighted word sits level with the words around it; the
  // padding, not an offset, sizes the pill around it.
  const pill = (
    <b style={{ display: "inline-block", margin: "0 .08em", padding: ".02em .3em .1em .2em", lineHeight: 1.04, borderRadius: 999, background: PILL, color: ON_PILL, fontWeight: 600, whiteSpace: "nowrap" }}>
      <span aria-hidden style={{ display: "inline-block", marginRight: ".16em", fontFamily: "'Material Symbols Rounded'", fontWeight: 400, fontSize: ".72em", lineHeight: 1, verticalAlign: ICON_DROP, fontVariationSettings: "'FILL' 1, 'wght' 500, 'opsz' 48" }}>{icon}</span>
      {highlight}
    </b>
  );
  return (
    <h1 ref={ref} style={{ position: "absolute", left: "50%", top: CAPTION.top, transform: "translateX(-50%)", width: "max-content", maxWidth: CAPTION.right - CAPTION.left, margin: 0, zIndex: 2, color: INK, fontSize: 76, lineHeight: 1.36, letterSpacing: -1.5, fontWeight: 560, textAlign: "center" }}>
      {lines.map((line, i) => {
        if (i !== found[0]) return <span key={i} style={{ display: "block" }}>{line}</span>;
        const [before, after] = line.split(highlight);
        return <span key={i} style={{ display: "block" }}>{before}{pill}{after}</span>;
      })}
    </h1>
  );
};

/** One phone screenshot: the panorama, moved so screenshot `index` fills the frame. */
export const PhoneShot: React.FC<{ locale: string; index: number }> = ({ locale, index }) => (
  <AbsoluteFill style={{ overflow: "hidden" }}>
    <div style={{ position: "absolute", left: -index * WIDTH, top: 0, height: HEIGHT }}>
      <PhonePanorama locale={locale} only={index} />
    </div>
  </AbsoluteFill>
);
