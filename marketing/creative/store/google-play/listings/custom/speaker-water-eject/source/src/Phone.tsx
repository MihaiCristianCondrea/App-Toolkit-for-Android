import React, { useLayoutEffect, useRef } from "react";
import { AbsoluteFill, cancelRender, continueRender, delayRender } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { STRINGS } from "./strings";
import { WaterArt, type Ripple } from "./WaterArt";
import { Wave } from "./Wave";
import { FONT, INK, ON_PILL, PILL } from "./theme";

export const WIDTH=1080,HEIGHT=1920;
const CAPTION={left:120,right:960,top:72,bottom:312};
const ICON_DROP="-.1em";
// Local CSS pixels, before the renderer's 2x output scale. Every visible subject is checked after layout.
const SAFE={left:40,right:1040,top:360,bottom:1880};
// A phone may run off the bottom or a side on purpose (a close view); nothing may enter the caption band.
const BLEED={left:-1200,right:2280,top:360,bottom:3000};
const FRAME_W=465.23;

/** A device in a shot: its screen (a key of SCREENS, the shot's own by default), where it stands, how large, its turn. */
type Device={screen?:string;x:number;top:number;scale:number;yaw?:number;pitch?:number;bleed?:boolean;detail?:"guidance"};
/** A shot: one or two devices, never the same screen twice, its ripples, and an illustrated wave from a speaker. */
type Shot={devices:Device[];ripples:Ripple[];wave?:{x:number;y:number}};

// Five shots, each with its own job. The hero sells the metaphor (a wave rising from the speaker); the product shot
// shows the 165 Hz screen up close; two phones show the cycle ready and running; the guidance sits at reading size;
// two tools show the rest of the toolbox. Ripples link neighbouring shots. Poses are turns, never a roll.
const SHOTS:Shot[]=[
 // 1 Hero: a large turned phone, the wave rising from its speaker.
 {devices:[{x:560,top:410,scale:1.2,yaw:-22,pitch:4}],ripples:[{x:1020,y:1500,reach:900,strength:.5}],wave:{x:540,y:1590}},
 // 2 Product: close and straight, the 165 Hz screen at the heart of the image, rings from the speaker.
 {devices:[{x:540,top:400,scale:1.55,bleed:true}],ripples:[{x:540,y:1940,reach:1300,strength:1}]},
 // 3 The cycle: ready behind on the left, running in front with Stop under the thumb.
 {devices:[{screen:"water-eject",x:330,top:620,scale:1.0,yaw:20},{screen:"water-running",x:690,top:450,scale:1.24,yaw:-8}],ripples:[{x:500,y:1880,reach:1050,strength:.8}]},
 // 4 Guidance: the guidance card itself, lifted out of the screen at reading size.
 {devices:[{detail:"guidance",x:560,top:900,scale:2.15}],ripples:[{x:150,y:1850,reach:1000,strength:.7}]},
 // 5 The toolbox: Quick Tools behind, Unit Converter in front, a breath after the Water Eject story.
 {devices:[{screen:"quick-tools",x:330,top:600,scale:1.02,yaw:18},{screen:"unit-converter",x:690,top:450,scale:1.24,yaw:-8}],ripples:[{x:-60,y:1820,reach:900,strength:.4}]},
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
    {(only===undefined||only===i)&&SHOTS[i].wave&&<Wave id={`wave-${i}`} x={SHOTS[i].wave!.x} y={SHOTS[i].wave!.y} width={WIDTH} height={HEIGHT}/>}
    {(only===undefined||only===i)&&SHOTS[i].devices.map((d,k)=>{
     const id=d.screen??s.id;
     const w=d.detail?430:FRAME_W;
     return <div key={k} style={{position:"absolute",left:d.x,top:d.top,width:w,zIndex:k+1,transform:`translateX(-50%) scale(${d.scale})`,transformOrigin:"50% 0",filter:"drop-shadow(0 26px 30px rgba(0,66,77,.14))"}}>
      <LitScreen id={SCREENS[id][0]} state={SCREENS[id][1]} frame={!d.detail} detail={d.detail} safeBounds={d.bleed?BLEED:SAFE} pose={{yaw:d.yaw??0,pitch:d.pitch??0}} style={{width:w}}/>
     </div>;
    })}
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
