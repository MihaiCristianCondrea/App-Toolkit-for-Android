import React from "react";
import {TEAL} from "./theme";
import type {Arc} from "./composition/effects";
export const ConversionArrows:React.FC<{arcs:Arc[]}>=({arcs})=><svg width={1080} height={1920} aria-hidden style={{position:"absolute",inset:0}}>
 {arcs.map((a,k)=>{const end=a.pts[a.pts.length-1],pre=a.pts[a.pts.length-3],angle=Math.atan2(end[1]-pre[1],end[0]-pre[0])*180/Math.PI;
 return <g key={k} fill="none" stroke={TEAL} strokeWidth={7} strokeLinecap="round" strokeLinejoin="round">
 <polyline points={a.pts.map(p=>p.join(",")).join(" ")}/>
 <path d="M -14 -10 L 0 0 L -14 10" transform={`translate(${end[0]} ${end[1]}) rotate(${angle})`}/>
 </g>})}
</svg>;
