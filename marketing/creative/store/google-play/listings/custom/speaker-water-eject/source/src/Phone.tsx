import React, { useLayoutEffect, useRef } from "react";
import { AbsoluteFill, cancelRender, continueRender, delayRender } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { STRINGS } from "./strings";
import { Bubbles, Glass } from "./Glass";
import { FONT, INK, ON_PILL, PILL } from "./theme";

export const WIDTH=1080,HEIGHT=1920;
const CAPTION={left:120,right:960,top:72,bottom:312};
const ICON_DROP="-.1em";
// Local CSS pixels, before the renderer's 2x output scale. Every visible subject is checked after layout.
/** Phones may run off the bottom and across the dividers; nothing may enter the caption band. */
const CLEAR={left:-1e5,right:1e5,top:340,bottom:1e5};
const FRAME_W=465.23;

/**
 * A phone placed on the whole strip: `x` is its centre across all the screenshots, so a phone may sit
 * across a divider and appear in both images. `roll` tilts it in the picture plane, the way phones lie
 * on a table in a product shot; `yaw` adds a little depth. Phones are large and run off the bottom
 * edge, never off the top, so no corner is ever cut.
 */
type Device={screen:string;x:number;top:number;scale:number;roll?:number;yaw?:number;pitch?:number};

// One strip, five images. Each phone shows its own screen; the ready 165 Hz screen appears once. The
// toolbox phones are side by side, tilted apart, Quick Tools straddling the last divider.
const DEVICES:Device[]=[
 {screen:"water-eject",x:540,top:470,scale:1.72,roll:-7,yaw:-6},
 {screen:"water-running",x:1080+540,top:500,scale:1.72,roll:4,yaw:6},
 {screen:"water-stop",x:2160+530,top:450,scale:1.6,roll:-3},
 {screen:"unit-converter",x:3240+360,top:540,scale:1.5,roll:-6},
 {screen:"quick-tools",x:4320,top:700,scale:1.5,roll:7},
 {screen:"level",x:4320+700,top:520,scale:1.5,roll:-5},
];
/** Bubbles rising from the hero's speaker, just below the edge of the image. */
const BUBBLES={x:470,y:1920};

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
  {(only===undefined||only===0)&&<Bubbles x={BUBBLES.x} y={BUBBLES.y} width={n*WIDTH} height={HEIGHT}/>}
  <div data-shot="strip" style={{position:"absolute",inset:0}}>
   {visible.map((d,k)=>(
    <div key={d.screen} style={{position:"absolute",left:d.x,top:d.top,width:FRAME_W,zIndex:k+1,transform:`translateX(-50%) rotate(${d.roll??0}deg) scale(${d.scale})`,transformOrigin:"50% 0",filter:"drop-shadow(0 34px 44px rgba(0,40,48,.26))"}}>
     <LitScreen id={SCREENS[d.screen][0]} state={SCREENS[d.screen][1]} safeBounds={CLEAR} pose={{yaw:d.yaw??0,pitch:d.pitch??0}} style={{width:FRAME_W}}/>
    </div>
   ))}
  </div>
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
