import React from "react";
import {AbsoluteFill} from "remotion";
import {useFonts} from "./LitScreen";
import {STRINGS} from "./strings";
import {Shape} from "./Shape";
import {FONT,INK} from "./theme";
export type Concept="ripple"|"ribbon";
export const FeatureGraphic:React.FC<{locale:string;concept?:Concept}>=({locale})=>{
 useFonts();const s=STRINGS[locale].featureGraphic;
 return <AbsoluteFill style={{background:"#FFF4D9",fontFamily:FONT,color:INK,overflow:"hidden"}}>
 <Shape name="cookie_12sided" x={-130} y={190} size={490} color="#FFD03B" rotate={12}/>
 <Shape name="clover_4leaf" x={810} y={-100} size={370} color="#FDE7A2" rotate={-16}/>
 <Shape name="soft_burst" x={850} y={380} size={150} color="#F6A800" rotate={18}/>
 <svg width="1024" height="500" style={{position:"absolute",inset:0}} fill="none" stroke="#8F5700" strokeWidth="2">
 <path d="M 0 420 H 350"/>{Array.from({length:15},(_,i)=><path key={i} d={`M ${i*24} 420 v ${i%4?-12:-25}`}/>)}
 <path d="M 760 30 A 230 230 0 0 1 990 260"/><path d="M 825 60 V 210 L 935 100" strokeWidth="3"/>
 </svg>
 <div style={{position:"absolute",left:110,right:110,top:142,textAlign:"center"}}><div style={{fontSize:22,fontWeight:600,color:"#8F5700"}}>{s.brand}</div><div style={{fontSize:64,fontWeight:650,letterSpacing:-2,marginTop:14}}>{s.title}</div><div style={{fontSize:29,marginTop:16}}>{s.tagline}</div></div>
 </AbsoluteFill>
};
