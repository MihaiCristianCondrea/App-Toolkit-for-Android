import React, { useLayoutEffect, useRef } from "react";
import { AbsoluteFill, cancelRender, continueRender, delayRender } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { STRINGS } from "./strings";
import { Glass, Wave } from "./Glass";
import { FONT, INK, ON_PILL, PILL } from "./theme";

export const WIDTH=1080,HEIGHT=1920;
const CAPTION={left:120,right:960,top:72,bottom:312};
const ICON_DROP="-.1em";
// Local CSS pixels, before the renderer's 2x output scale. Every visible subject is checked after layout.
/** Nothing may enter the caption band. Phones may run off the bottom; `inOwnImage` keeps them off the dividers. */
const CLEAR={left:-1e5,right:1e5,top:340,bottom:1e5};
const FRAME_W=465.23;

/**
 * A phone placed in one image: `x` is its centre across the strip, inside a single screenshot, so every
 * image stands on its own. `roll` tilts it in the picture plane, the way phones lie on a table in a
 * product shot; `yaw` turns it to show its side; `pitch` leans the top away to show the bottom edge.
 * Phones may run off the bottom edge, never off the top, so no corner is ever cut.
 */
type Device={screen:string;x:number;top:number;scale:number;roll?:number;yaw?:number;pitch?:number;speaker?:boolean};

// One strip, five images, each with its own composition: the hero shows the whole phone leaning back so
// its speaker grille faces the viewer, with water thrown out of it; a close crop on the countdown; the
// whole phone turned to show its side, the Stop button in view; two toolbox phones tilted apart; the level.
const DEVICES:Device[]=[
 {screen:"water-eject",x:640,top:385,scale:1.16,roll:-6,pitch:26,speaker:true},
 {screen:"water-running",x:1080+580,top:420,scale:1.8,roll:3,yaw:8},
 {screen:"water-stop",x:2160+560,top:400,scale:1.3,roll:3,yaw:-22},
 {screen:"unit-converter",x:3240+300,top:560,scale:1.08,roll:-7},
 {screen:"quick-tools",x:3240+790,top:660,scale:1.08,roll:6},
 {screen:"level",x:4320+560,top:500,scale:1.5,roll:-5},
];

/** The Pixel 9 Pro XL frame, in CSS px: 430 px of display is 70.8 mm. */
const MM=430/70.8,FRAME_H=162.8*MM,DEPTH=8.5*MM,FACE=(8.5/2+0.1)*MM,PERSPECTIVE=2400;
/** A point on the phone's bottom edge, `px` across from the centre and `pz` towards the screen, as drawn at `pitch`. */
const bottomEdge=(px:number,pz:number,pitch:number)=>{
 const p=pitch*Math.PI/180,y=(FRAME_H/2)*Math.cos(p)-pz*Math.sin(p),z=(FRAME_H/2)*Math.sin(p)+pz*Math.cos(p)-FACE,k=PERSPECTIVE/(PERSPECTIVE-z);
 return [FRAME_W/2+px*k,FRAME_H/2+y*k];
};
/** The speaker grille's centre, left of the USB-C port as on the phone. */
const SPEAKER_X=-108;
/** Where a point of a phone's frame lands in the strip: the phone's box is scaled and rolled about its top centre. */
const place=(d:Device,[lx,ly]:number[])=>{
 const r=(d.roll??0)*Math.PI/180,u=(lx-FRAME_W/2)*d.scale,v=ly*d.scale;
 return [d.x+u*Math.cos(r)-v*Math.sin(r),d.top+u*Math.sin(r)+v*Math.cos(r)];
};

/**
 * Fails the render when the visible part of a phone, above the image's bottom edge, crosses into the next
 * image: every screenshot has to stand on its own.
 */
const inOwnImage=(d:Device)=>{
 const i=Math.floor(d.x/WIDTH),edges:number[][][]=[[[0,0],[FRAME_W,0]],[[0,FRAME_H],[FRAME_W,FRAME_H]],[[0,0],[0,FRAME_H]],[[FRAME_W,0],[FRAME_W,FRAME_H]]];
 const xs=edges.flatMap(([a,b])=>Array.from({length:41},(_,k)=>place(d,[a[0]+(b[0]-a[0])*k/40,a[1]+(b[1]-a[1])*k/40]))).filter(([,y])=>y<=HEIGHT).map(([x])=>x);
 if(Math.min(...xs)<i*WIDTH||Math.max(...xs)>(i+1)*WIDTH)throw new Error(`The ${d.screen} phone crosses the edge of image ${i+1}`);
};
DEVICES.forEach(inOwnImage);

/** The bottom edge's openings, drawn on the phone's own edge: the speaker grille, the USB-C port and a microphone. */
const SpeakerEdge:React.FC<{pitch:number}>=({pitch})=>{
 const slot=(cx:number,w:number,h:number)=>[[cx-w/2,-h/2],[cx+w/2,-h/2],[cx+w/2,h/2],[cx-w/2,h/2]].map(([x,z])=>bottomEdge(x,z,pitch).map(n=>n.toFixed(2)).join(",")).join(" ");
 const holes=Array.from({length:8},(_,i)=>SPEAKER_X-38+i*10.8);
 return <svg aria-hidden width={FRAME_W} height={FRAME_H} style={{position:"absolute",left:0,top:0,overflow:"visible",pointerEvents:"none"}}>
  {holes.map(x=><polygon key={x} points={slot(x,3.2,DEPTH*0.22)} fill="#1B2B30" stroke="#1B2B30" strokeWidth="2.2" strokeLinejoin="round"/>)}
  <polygon points={slot(0,50,DEPTH*0.32)} fill="#1F3035" stroke="#2C3E43" strokeWidth="3" strokeLinejoin="round"/>
  <polygon points={slot(0,36,DEPTH*0.1)} fill="#4B5C61"/>
  <polygon points={slot(96,6,DEPTH*0.14)} fill="#16262B" stroke="#16262B" strokeWidth="2" strokeLinejoin="round"/>
 </svg>;
};

export const PhonePanorama:React.FC<{locale:string;only?:number}>=({locale,only})=>{
 useFonts();
 const strings=STRINGS[locale],n=strings.screenshots.length;
 // Draw only the phones that fall in the image being rendered.
 const visible=DEVICES.filter(d=>{
  if(only===undefined)return true;
  const half=FRAME_W*d.scale*0.62;
  return d.x+half>only*WIDTH&&d.x-half<(only+1)*WIDTH;
 });
 return <AbsoluteFill style={{width:n*WIDTH,fontFamily:FONT,overflow:"hidden"}}>
  <Glass width={n*WIDTH} height={HEIGHT} panels={n}/>
  <div data-shot="strip" style={{position:"absolute",inset:0}}>
   {visible.map((d,k)=>(
    <div key={d.screen} style={{position:"absolute",left:d.x,top:d.top,width:FRAME_W,zIndex:k+1,transform:`translateX(-50%) rotate(${d.roll??0}deg) scale(${d.scale})`,transformOrigin:"50% 0",filter:"drop-shadow(0 34px 44px rgba(0,40,48,.26))"}}>
     <LitScreen id={SCREENS[d.screen][0]} state={SCREENS[d.screen][1]} safeBounds={CLEAR} pose={{yaw:d.yaw??0,pitch:d.pitch??0}} style={{width:FRAME_W}}/>
     {d.speaker&&<SpeakerEdge pitch={d.pitch??0}/>}
    </div>
   ))}
  </div>
  {visible.filter(d=>d.speaker).map(d=>{
   const [x,y]=place(d,bottomEdge(SPEAKER_X,0,d.pitch??0));
   return <div key={`wave-${d.screen}`} style={{position:"absolute",inset:0,zIndex:10}}><Wave x={x} y={y} angle={d.roll??0} size={1.1} width={n*WIDTH} height={HEIGHT}/></div>;
  })}
  {strings.screenshots.map((s,i)=>(
   <div key={s.id} style={{position:"absolute",left:i*WIDTH,top:0,width:WIDTH,height:HEIGHT,pointerEvents:"none",zIndex:20}}>
    <Caption lines={s.lines} highlight={s.highlight} icon={s.icon} check={only===i}/>
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
