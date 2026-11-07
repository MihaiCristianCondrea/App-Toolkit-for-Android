import React from "react";
import { AbsoluteFill } from "remotion";
import { useFonts } from "./LitScreen";
import { STRINGS } from "./strings";
import { FONT } from "./theme";
export type Concept="ripple"|"ribbon";
export const FeatureGraphic:React.FC<{locale:string;concept?:Concept}>=({locale,concept="ribbon"})=>{
 useFonts();const s=STRINGS[locale].featureGraphic;
 return <AbsoluteFill style={{background:"#D8E8F9",fontFamily:FONT,color:"#102A50",overflow:"hidden"}}>
 <svg width="1024" height="500" style={{position:"absolute",inset:0}}>
 {concept==="ribbon"?<g fill="none" stroke="#769ED0" strokeWidth="3"><path d="M -30 400 H 1054"/>{Array.from({length:32},(_,i)=><path key={i} d={`M ${i*34} 400 v ${i%5===0?-35:-16}`}/>)}<path d="M 760 500 A 280 280 0 0 0 1040 220"/></g>:<g fill="none" stroke="#769ED0" strokeWidth="4"><path d="M -140 500 A 350 350 0 0 1 210 150"/><path d="M 820 500 L 1024 296"/><path d="M 820 500 H 1100"/></g>}
 </svg>
 <div style={{position:"absolute",left:100,right:100,top:145,textAlign:"center"}}><div style={{fontSize:22,fontWeight:600,color:"#27588F"}}>{s.brand}</div><div style={{fontSize:64,fontWeight:650,letterSpacing:-2,marginTop:14}}>{s.title}</div><div style={{fontSize:29,marginTop:16}}>{s.tagline}</div></div>
 </AbsoluteFill>
};
