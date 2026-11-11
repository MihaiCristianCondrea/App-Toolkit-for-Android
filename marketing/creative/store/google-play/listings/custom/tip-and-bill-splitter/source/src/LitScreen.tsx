import React, {useLayoutEffect,useRef} from "react";
import {cancelRender,continueRender,delayRender,staticFile} from "remotion";
import {mount,setup} from "../../../../../../../../ui/src/embed.js";
import {PALETTE} from "./theme";
import {EXPECTED_BILLS} from "./screens";
import {billSummary} from "../../../../../../../../ui/src/kit/measure.js";

const assetBase=staticFile("fonts/GoogleSansFlex.ttf").replace("fonts/GoogleSansFlex.ttf","");
export const ready=setup({assetBase,marketingAssetBase:assetBase+"material-next/",palette:PALETTE,theme:"light"})
 .then(()=>document.fonts.ready).then(()=>{
  for(const family of ["Google Sans Flex","Material Symbols Outlined","Material Symbols Rounded"])
   if(![...document.fonts].some(f=>f.family.replace(/['"]/g,"")===family&&f.status==="loaded"))
    throw Error(family+" failed to load");
 });
export const useFonts=()=>useLayoutEffect(()=>{
 const h=delayRender("Loading fonts");
 ready.then(()=>new Promise<void>(r=>requestAnimationFrame(()=>requestAnimationFrame(()=>r())))).then(()=>continueRender(h),cancelRender);
},[]);
export const LitScreen:React.FC<{id:string;state?:Record<string,unknown>;frame?:boolean;window?:string;pose?:{yaw:number;pitch:number;perspective?:number};style?:React.CSSProperties;measure?:(host:HTMLElement)=>void;scroll?:number}>=({id,state={},frame=true,window="phone",pose={yaw:0,pitch:0},style,measure})=>{
 const ref=useRef<HTMLDivElement>(null),key=JSON.stringify(state);
 useLayoutEffect(()=>{
  const h=delayRender("Rendering "+id);
  ready.then(()=>mount(ref.current!,id,{go:()=>{},set:()=>{},...JSON.parse(key)},{frame,window,showAds:false}))
   .then(async()=>{
    ref.current!.querySelectorAll("mnw-android-screen").forEach(s=>s.removeAttribute("interactive"));
    await document.fonts.ready;
    await Promise.all([...ref.current!.querySelectorAll("mnw-device-frame")].map(async(f:any)=>{
     f.yaw=pose.yaw;f.pitch=pose.pitch;f.perspective=pose.perspective??3000;f.finish="porcelain";
     await f.updateComplete;
     if(f.device==="pixel_9_pro_xl"&&!f.shadowRoot?.querySelector("img.phone"))throw Error("The 3D Pixel picture is unavailable");
    }));
    await Promise.all([...ref.current!.querySelectorAll("img")].map(i=>i.decode()));
    // Reveal the selected category by scrolling the real horizontal chip row.
    const selected=ref.current!.querySelector(".measure-categories md-filter-chip[selected]") as HTMLElement;
    const row=ref.current!.querySelector(".measure-categories") as HTMLElement;
    if(selected&&row)row.scrollLeft=Math.max(0,selected.offsetLeft-row.clientWidth/2+selected.offsetWidth/2);
    await new Promise<void>(r=>requestAnimationFrame(()=>requestAnimationFrame(()=>r())));
    const host=ref.current!;
    let content:Element|null=host.querySelector(".measure-overview");
    while(content&&!((["auto","scroll"].includes(getComputedStyle(content).overflowY))&&content.scrollHeight>content.clientHeight))
     content=(content as HTMLElement).assignedSlot??content.parentElement??((content.getRootNode() as ShadowRoot).host??null);
    if(content&&state.activeSheet==="bill_splitter")content.scrollTop=window==="phone"?300:200;
    await new Promise<void>(r=>requestAnimationFrame(()=>requestAnimationFrame(()=>r())));
    // Scrolling starts the app bar's real colour transition inside its shadow root.
    // Finish finite animations at their end state before capture rather than sampling wall-clock time.
    const roots:(Document|ShadowRoot)[]=[document];
    const visit=(root:Document|ShadowRoot)=>root.querySelectorAll("*").forEach(e=>{if(e.shadowRoot){roots.push(e.shadowRoot);visit(e.shadowRoot);}});
    visit(document);
    for(const root of roots)for(const animation of root.getAnimations()){
     const duration=animation.effect?.getComputedTiming().endTime;
     if(duration!==Infinity)animation.finish();
    }
    await new Promise<void>(r=>requestAnimationFrame(()=>requestAnimationFrame(()=>r())));
    const fonts=[...document.fonts].filter(f=>f.status==="loaded").map(f=>f.family);
    if(state.activeSheet==="bill_splitter"){
     const result=billSummary(state);
     const expected=EXPECTED_BILLS[[state.billAmount,state.tipPercent,state.splitPeople,state.billCurrency].join(":")];
     const fields=[...host.querySelectorAll(".bill-input-row md-outlined-text-field,.measure-people md-outlined-text-field")] as any[];
     const share=host.querySelector(".measure-bill > strong")?.textContent?.trim();
     const currency=host.querySelector(".bill-currency")?.textContent?.trim();
     if(!expected||!result.valid||String(result.tip)!==expected.tip||String(result.total)!==expected.total||String(result.share)!==expected.share||
      share!==expected.formatted||currency!==state.billCurrency||String(fields[0]?.value)!==String(state.billAmount)||String(fields[1]?.value)!==String(state.splitPeople))
      throw Error("The bill capture does not match the verified calculation");
     console.log("BILL "+JSON.stringify({bill:state.billAmount,tip:state.tipPercent,people:state.splitPeople,currency:state.billCurrency,share,total:String(result.total),fonts}));
    }else{
     const tools=[...host.querySelectorAll(".numbers-card")].map(e=>e.getAttribute("data-tool"));
     if(!["ruler","protractor","unit_converter","bill_splitter"].every(t=>tools.includes(t)))throw Error("The Measure workspace is incomplete");
     console.log("TOOLKIT "+JSON.stringify({tools,fonts}));
    }
    measure?.(host);
   }).then(()=>continueRender(h),cancelRender);
 },[id,key,frame,window,pose.yaw,pose.pitch,pose.perspective]);
 return <div ref={ref} style={style}/>;
};
