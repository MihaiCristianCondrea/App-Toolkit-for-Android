import React,{useRef} from "react";
import {LitScreen} from "./LitScreen";
import {SCREENS} from "./screens";
import type {Panel} from "./composition/panels";

/** A crop of the real converter fields. No controls, values or labels are recreated. */
export const NativePanel:React.FC<{panel:Panel}>=({panel:p})=>{
 const inner=useRef<HTMLDivElement>(null);
 const measure=(host:HTMLElement)=>{
  const screen=host.querySelector("mnw-android-screen") as HTMLElement;
  const from=host.querySelector(".converter-card")!,last=host.querySelector(p.screen==="data"?".converter-body > p:not(.converter-hint)":".converter-card--result")!;
  const sr=screen.getBoundingClientRect(),factor=sr.width/430,fr=from.getBoundingClientRect(),lr=last.getBoundingClientRect();
  const actual={left:(fr.left-sr.left)/factor,top:(fr.top-sr.top)/factor,width:fr.width/factor,height:(lr.bottom-fr.top)/factor};
  if(Math.abs(actual.width-p.crop.width)>1||Math.abs(actual.height-p.crop.height)>1)throw Error("Native panel changed: "+JSON.stringify(actual));
  inner.current!.style.left=-actual.left*p.scale+"px";
  inner.current!.style.top=-actual.top*p.scale+"px";
 };
 return <div style={{position:"absolute",left:p.box.left,top:p.box.top,width:p.width,height:p.height,overflow:"hidden",borderRadius:p.screen==="data"?0:26*p.scale,boxShadow:"0 18px 34px rgba(21,63,60,.13)",zIndex:3}}>
  <div ref={inner} style={{position:"absolute",width:430,height:957,transform:`scale(${p.scale})`,transformOrigin:"0 0"}}>
   <LitScreen id={SCREENS[p.screen][0]} state={SCREENS[p.screen][1]} frame={false} style={{width:430,height:957}} measure={measure}/>
  </div>
 </div>;
};
