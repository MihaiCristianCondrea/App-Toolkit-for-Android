import React from "react";
import {AbsoluteFill} from "remotion";
import {Shape} from "./Shape";
import {useFonts} from "./LitScreen";
import {FIELD,FONT,INK,PILL,CYAN,TEAL,BUTTER,PALE} from "./theme";
export type Concept="transformation"|"connected"|"editorial";
const Bill:React.FC<{x:number;y:number;angle:number}>=({x,y,angle})=><div style={{position:"absolute",left:x,top:y,width:292,height:360,transform:`rotate(${angle}deg)`,borderRadius:28,background:"#FFFBF5",boxShadow:"0 14px 24px #733B2422",padding:"27px 30px",boxSizing:"border-box",color:INK}}>
 <div style={{fontSize:20,fontWeight:550}}>Dinner for two</div>
 <div style={{display:"flex",justifyContent:"space-between",fontSize:22,marginTop:24}}><span>Bill</span><b>€52.00</b></div>
 <div style={{display:"flex",justifyContent:"space-between",fontSize:22,marginTop:14}}><span>Tip 10%</span><b>€5.20</b></div>
 <div style={{borderTop:"3px dotted #D9A08C",marginTop:22,paddingTop:16,fontSize:17}}>Per person</div>
 <div style={{fontSize:52,fontWeight:650,letterSpacing:-2,marginTop:3}}>€28.60</div>
 <div style={{fontSize:17,marginTop:9,color:PILL}}>Total €57.20</div>
</div>;
export const FeatureGraphic:React.FC<{locale:string;concept?:Concept}>=({concept="connected"})=>{
 useFonts();
 return <AbsoluteFill style={{background:FIELD[0],fontFamily:FONT,color:INK,overflow:"hidden"}}>
 {concept==="connected"&&<>
  <Shape name="soft_burst" x={600} y={-4} size={500} color={CYAN} rotate={-12}/>
  <Shape name="pill" x={-100} y={348} size={300} color={BUTTER} rotate={25}/>
  <Bill x={648} y={73} angle={7}/>
  <div style={{position:"absolute",left:80,top:102,fontSize:26,fontWeight:560,color:PILL}}>App Toolkit</div>
  <div style={{position:"absolute",left:76,top:159,fontSize:74,lineHeight:1.04,fontWeight:650,letterSpacing:-2.7}}>Tip &amp; Bill<br/>Splitter</div>
 </>}
 {concept==="transformation"&&<>
  <Shape name="pill" x={-132} y={-155} size={630} color={CYAN} rotate={-22}/>
  <Bill x={80} y={71} angle={-8}/>
  <Shape name="clover_4leaf" x={851} y={310} size={240} color={BUTTER} rotate={16}/>
  <div style={{position:"absolute",left:447,top:129,fontSize:25,color:PILL,fontWeight:550}}>App Toolkit</div>
  <div style={{position:"absolute",left:443,top:185,fontSize:67,lineHeight:1.1,fontWeight:650,letterSpacing:-2}}>Tip &amp; Bill<br/>Splitter</div>
 </>}
 {concept==="editorial"&&<>
  <Shape name="clover_4leaf" x={650} y={185} size={350} color={PALE} rotate={10}/>
  <div style={{position:"absolute",left:80,top:55,fontSize:25,fontWeight:550,color:PILL}}>App Toolkit</div>
  <div style={{position:"absolute",left:76,top:106,fontSize:69,fontWeight:650,letterSpacing:-2.3}}>Tip &amp; Bill Splitter</div>
  <div style={{position:"absolute",left:81,top:251,fontSize:36,fontWeight:550}}>(€52 + €5.20) ÷ 2</div>
  <div style={{position:"absolute",left:636,top:245,fontSize:75,fontWeight:640,letterSpacing:-2}}>€28.60</div>
  <div style={{position:"absolute",left:643,top:334,fontSize:22}}>per person</div>
 </>}
 </AbsoluteFill>;
};
