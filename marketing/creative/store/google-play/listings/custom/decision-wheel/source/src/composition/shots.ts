import {CAPTION,GOLDEN,H,SPACE,W,centre,fitPose,measure} from "./geometry.ts";
import type {CaptionAt,Measured,Pose,ScreenUi} from "./geometry.ts";
import type {ShapePlan} from "./effects.ts";
export type Terms=Record<string,number>;
export type ShotSpec={id:string;intent:string;caption:CaptionAt;screens:string[];effects?:"sound";shapes:ShapePlan;candidates:(ui:Record<string,ScreenUi>)=>Iterable<Pose[]>;hard?:(ms:Measured[])=>string[];score:(ms:Measured[])=>Terms};
const sq=(n:number)=>n*n;
type Direction={id:string;caption:CaptionAt;width:number;pitch:number;yaw:number;roll:number;focus:string;guideY:number;shapes:ShapePlan};
export const DIRECTIONS:Direction[]=[
 {id:"wheel",caption:"top",width:690,pitch:0,yaw:-6,roll:-1,focus:"wheel",guideY:GOLDEN.y[1],shapes:{seed:11,small:1,big:{at:()=>[660,1140],max:900,min:700,name:"flower",rotate:-12,color:"#E3C4DF"}}},
 {id:"result",caption:"bottom",width:690,pitch:6,yaw:14,roll:2,focus:"result",guideY:GOLDEN.y[1],shapes:{seed:23,offset:3,small:2,big:{at:()=>[310,GOLDEN.y[0]],max:620,min:500,name:"sunny",rotate:18,color:"#DDA795"}}},
 {id:"choices",caption:"top",width:650,pitch:0,yaw:0,roll:0,focus:"wheel",guideY:GOLDEN.y[1],shapes:{seed:37,offset:2,small:2}},
 {id:"number",caption:"top",width:640,pitch:-2,yaw:-10,roll:-1,focus:"result",guideY:GOLDEN.y[1],shapes:{seed:47,offset:4,small:2}},
 {id:"coin",caption:"top",width:660,pitch:3,yaw:12,roll:-2,focus:"coin",guideY:GOLDEN.y[1],shapes:{seed:59,offset:5,small:1,big:{at:()=>[430,1230],max:700,min:550,name:"pill",rotate:-20,color:"#E7D9EF"}}},
 {id:"dice",caption:"bottom",width:640,pitch:6,yaw:-8,roll:1,focus:"dice",guideY:GOLDEN.y[0],shapes:{seed:71,offset:6,small:3}}
];
export const SHOTS:ShotSpec[]=DIRECTIONS.map(d=>({
 id:d.id,intent:`Focus ${d.focus} on its golden horizontal guide; use the full projected silhouette as the primary placement anchor.`,caption:d.caption,screens:[d.id],shapes:d.shapes,
 *candidates(ui){
  const top=d.caption==="top"?CAPTION.top.block.bottom+SPACE.xl:SPACE.xl;
  const bottom=d.caption==="bottom"?CAPTION.bottom.block.top-SPACE.xxl:H-SPACE.xl;
  for(const pitch of [d.pitch-2,d.pitch,d.pitch+2])for(const yaw of [d.yaw-2,d.yaw,d.yaw+2])for(const roll of [d.roll-1,d.roll,d.roll+1])
   for(const width of [d.width-21,d.width,d.width+21])for(const dy of [-SPACE.s,0,SPACE.s])
    yield [fitPose({pitch,yaw,roll,perspective:3000},ui[d.id],{width,x:m=>m.centroid[0],xTarget:W/2,y:m=>(m.full.top+m.full.bottom)/2,yTarget:(top+bottom)/2+dy})];
 },
 hard:([m])=>[
  ...(m.full.top<SPACE.m||m.full.bottom>H-SPACE.m?["Show the whole device inside the composition"]:[]),
  ...(d.caption==="bottom"&&m.full.bottom>CAPTION.bottom.block.top-SPACE.xxl?["Keep the device shadow clear of the lower caption"]:[])
 ],
 score:([m])=>{
  const top=d.caption==="top"?CAPTION.top.block.bottom+SPACE.xl:SPACE.xl;
  const bottom=d.caption==="bottom"?CAPTION.bottom.block.top-SPACE.xxl:H-SPACE.xl;
  return {
   size:sq(((m.box.right-m.box.left)-d.width)/SPACE.m),
   framing:2*sq(((m.full.top+m.full.bottom)/2-(top+bottom)/2)/SPACE.l),
   goldenY:.15*sq((centre(m.critical[d.focus])[1]-d.guideY)/SPACE.xxl),
   pose:(sq(m.pose.pitch-d.pitch)+sq(m.pose.yaw-d.yaw)+sq(m.pose.roll-d.roll))/9,
   balance:4*sq((m.centroid[0]-W/2)/SPACE.m)
  };
 }
}));
export const measureAll=(poses:Pose[],screens:string[],ui:Record<string,ScreenUi>)=>poses.map((p,i)=>measure(p,ui[screens[i]]));
