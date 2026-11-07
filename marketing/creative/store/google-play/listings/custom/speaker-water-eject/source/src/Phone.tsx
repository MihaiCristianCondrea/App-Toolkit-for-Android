import React, { useLayoutEffect, useRef } from "react";
import { AbsoluteFill, cancelRender, continueRender, delayRender } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { STRINGS } from "./strings";
import { Glass } from "./Glass";
import { Hero } from "./Hero";
import { CAPTION_BAND } from "./heroGeometry";
import { FONT, INK, ON_PILL, PILL } from "./theme";

export const WIDTH=1080,HEIGHT=1920;
const CAPTION={left:120,right:960,top:72,bottom:312};
/** The hero's caption stands at the foot of its image, in the band its geometry defines. */
const CAPTION_LOW=CAPTION_BAND;
/** The image whose phone is solved (src/Hero.tsx) rather than placed, with its caption at the foot. */
const HERO=0;
const ICON_DROP="-.1em";
// Local CSS pixels, before the renderer's 2x output scale. Every visible subject is checked after layout.
/** Nothing may enter the caption band; phones may run off the bottom. `inOwnImage` keeps them off the dividers. */
const CLEAR={left:-1e5,right:1e5,top:340,bottom:1e5};
const FRAME_W=465.23;

/**
 * A phone placed in one image: `x` and `top` place the middle of its top edge in the strip, inside a
 * single screenshot, so every image stands on its own. `roll` tilts it about that point, the way phones
 * lie on a table in a product shot, so a rolled phone's `x` is set off to keep its middle on the image's
 * centre line; `yaw` turns it to show its side; `pitch` leans the top away. Phones run off the bottom
 * edge only, away from the caption. The hero is not placed here: src/Hero.tsx draws it in a solved pose.
 */
type Device={screen:string;x:number;top:number;scale:number;roll?:number;yaw?:number;pitch?:number};

// One strip, five images, each with its own composition: the solved hero (src/Hero.tsx); a close crop on
// the countdown; the whole phone turned to show its side, the Stop button in view; two toolbox phones
// tilted apart; the level.
const DEVICES:Device[]=[
 {screen:"water-running",x:1080+580,top:420,scale:1.8,roll:3,yaw:8},
 {screen:"water-stop",x:2160+560,top:400,scale:1.3,roll:3,yaw:-22},
 {screen:"unit-converter",x:3240+300,top:560,scale:1.08,roll:-7},
 {screen:"quick-tools",x:3240+790,top:660,scale:1.08,roll:6},
 {screen:"level",x:4320+560,top:500,scale:1.5,roll:-5},
];

/** The Pixel 9 Pro XL frame's height, in CSS px: 430 px of display is 70.8 mm. */
const FRAME_H=162.8*430/70.8;
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
 const xs=edges.flatMap(([a,b])=>Array.from({length:41},(_,k)=>place(d,[a[0]+(b[0]-a[0])*k/40,a[1]+(b[1]-a[1])*k/40]))).filter(([,y])=>y>=0&&y<=HEIGHT).map(([x])=>x);
 if(Math.min(...xs)<i*WIDTH||Math.max(...xs)>(i+1)*WIDTH)throw new Error(`The ${d.screen} phone crosses the edge of image ${i+1}`);
};
DEVICES.forEach(inOwnImage);

export const PhonePanorama:React.FC<{locale:string;only?:number;debug?:boolean}>=({locale,only,debug})=>{
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
  {(only===undefined||only===HERO)&&<div style={{position:"absolute",left:HERO*WIDTH,top:0,width:WIDTH,height:HEIGHT}}><Hero debug={debug}/></div>}
  <div data-shot="strip" style={{position:"absolute",inset:0}}>
   {visible.map((d,k)=>(
    <div key={d.screen} style={{position:"absolute",left:d.x,top:d.top,width:FRAME_W,zIndex:k+1,transform:`translateX(-50%) rotate(${d.roll??0}deg) scale(${d.scale})`,transformOrigin:"50% 0",filter:"drop-shadow(0 34px 44px rgba(0,40,48,.26))"}}>
     <LitScreen id={SCREENS[d.screen][0]} state={SCREENS[d.screen][1]} safeBounds={CLEAR} pose={{yaw:d.yaw??0,pitch:d.pitch??0}} style={{width:FRAME_W}}/>
    </div>
   ))}
  </div>
  {strings.screenshots.map((s,i)=>(
   <div key={s.id} style={{position:"absolute",left:i*WIDTH,top:0,width:WIDTH,height:HEIGHT,pointerEvents:"none",zIndex:20}}>
    <Caption lines={s.lines} highlight={s.highlight} icon={s.icon} low={i===HERO} check={only===i}/>
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
const Caption: React.FC<{ lines: string[]; highlight: string; icon: string; low: boolean; check: boolean }> = ({ lines, highlight, icon, low, check }) => {
  const band = low ? CAPTION_LOW : CAPTION;
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
    <h1 ref={ref} style={{ position: "absolute", left: "50%", ...(low ? { bottom: HEIGHT - band.bottom } : { top: band.top }), transform: "translateX(-50%)", width: "max-content", maxWidth: CAPTION.right - CAPTION.left, margin: 0, zIndex: 2, color: INK, fontSize: 76, lineHeight: 1.36, letterSpacing: -1.5, fontWeight: 560, textAlign: "center" }}>
      {lines.map((line, i) => {
        if (i !== found[0]) return <span key={i} style={{ display: "block" }}>{line}</span>;
        const [before, after] = line.split(highlight);
        return <span key={i} style={{ display: "block" }}>{before}{pill}{after}</span>;
      })}
    </h1>
  );
};

/** One phone screenshot: the panorama, moved so screenshot `index` fills the frame. `debug` overlays the hero's geometry. */
export const PhoneShot: React.FC<{ locale: string; index: number; debug?: boolean }> = ({ locale, index, debug }) => (
  <AbsoluteFill style={{ overflow: "hidden" }}>
    <div style={{ position: "absolute", left: -index * WIDTH, top: 0, height: HEIGHT }}>
      <PhonePanorama locale={locale} only={index} debug={debug} />
    </div>
  </AbsoluteFill>
);
