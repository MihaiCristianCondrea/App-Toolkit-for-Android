import React from "react";
import { AbsoluteFill } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { Glass } from "./Glass";
import { STRINGS } from "./strings";
import { FONT, INK } from "./theme";

export const FeatureGraphic: React.FC<{locale:string}> = ({locale})=>{
 useFonts();
 const {title,tagline}=STRINGS[locale].featureGraphic;
 return <AbsoluteFill style={{fontFamily:FONT,color:INK,overflow:"hidden"}}>
  <Glass id="feature" width={1024} height={500} panels={1}/>
  <div style={{position:"absolute",left:180,top:140,width:505,textAlign:"center",zIndex:2}}>
   <h1 style={{margin:0,fontSize:65,lineHeight:1.1,letterSpacing:-2,fontWeight:700}}>{title}</h1>
   <p style={{margin:"22px 0 0",fontSize:25,lineHeight:1.35,whiteSpace:"pre-line",fontWeight:500}}>{tagline}</p>
  </div>
  <div style={{position:"absolute",left:735,top:-205,width:430,height:957,transform:"scale(.76)",transformOrigin:"0 0",borderRadius:28,overflow:"hidden",boxShadow:"0 20px 35px #007c8f26"}}>
   <LitScreen id={SCREENS["water-eject"][0]} state={SCREENS["water-eject"][1]} frame={false} style={{width:430,height:957}}/>
  </div>
 </AbsoluteFill>;
};
