import {CAPTION,GOLDEN,H,SPACE,W,centre,fitPose,measure} from "./geometry.ts";
import type {CaptionAt,Measured,Pose,ScreenUi} from "./geometry.ts";
import type {ShapePlan} from "./effects.ts";
export type Terms=Record<string,number>;
export type ShotSpec={id:string;intent:string;caption:CaptionAt;screens:string[];effects?:"sound";shapes:ShapePlan;candidates:(ui:Record<string,ScreenUi>)=>Iterable<Pose[]>;hard?:(ms:Measured[])=>string[];score:(ms:Measured[])=>Terms};
const sq=(n:number)=>n*n;
type Direction={id:string;caption:CaptionAt;width:number;pitch:number;yaw:number;roll:number;focus:string;target:[number,number];shapes:ShapePlan};
export const DIRECTIONS:Direction[]=[
 {id:"wheel",caption:"top",width:700,pitch:-6,yaw:-10,roll:-4,focus:"wheel",target:[GOLDEN.x[0]+SPACE.l,GOLDEN.y[1]],shapes:{seed:11,small:1,big:{at:()=>[660,1140],max:900,min:700,name:"flower",rotate:-12,color:"#E3C4DF"}}},
 {id:"result",caption:"bottom",width:630,pitch:14,yaw:24,roll:6,focus:"result",target:[GOLDEN.x[1]-SPACE.l,GOLDEN.y[1]],shapes:{seed:23,offset:3,small:2,big:{at:()=>[310,GOLDEN.y[0]],max:620,min:500,name:"sunny",rotate:18,color:"#DDA795"}}},
 {id:"choices",caption:"top",width:650,pitch:4,yaw:-16,roll:-2,focus:"wheel",target:[W/2,GOLDEN.y[1]],shapes:{seed:37,offset:2,small:2}},
 {id:"number",caption:"top",width:620,pitch:-10,yaw:18,roll:4,focus:"result",target:[W/2+SPACE.m,GOLDEN.y[1]],shapes:{seed:47,offset:4,small:2}},
 {id:"coin",caption:"top",width:640,pitch:10,yaw:-22,roll:-5,focus:"coin",target:[W/2-SPACE.m,GOLDEN.y[1]],shapes:{seed:59,offset:5,small:1,big:{at:()=>[430,1230],max:700,min:550,name:"pill",rotate:-20,color:"#E7D9EF"}}},
 {id:"dice",caption:"bottom",width:640,pitch:16,yaw:16,roll:3,focus:"dice",target:[W/2+SPACE.m,GOLDEN.y[0]],shapes:{seed:71,offset:6,small:3}}
];
export const SHOTS:ShotSpec[]=DIRECTIONS.map(d=>({
 id:d.id,intent:`Focus ${d.focus} on its golden horizontal guide; independently fit the actual projected UI.`,caption:d.caption,screens:[d.id],shapes:d.shapes,
 *candidates(ui){
  for(const pitch of [d.pitch-4,d.pitch,d.pitch+4])for(const yaw of [d.yaw-4,d.yaw,d.yaw+4])for(const roll of [d.roll-1,d.roll,d.roll+1])for(const width of [d.width-55,d.width-21,d.width,d.width+21])
   yield [fitPose({pitch,yaw,roll,perspective:3000},ui[d.id],{width,x:m=>centre(m.critical[d.focus])[0],xTarget:d.target[0],y:m=>centre(m.critical[d.focus])[1],yTarget:d.target[1],minTop:d.caption==="top"?CAPTION.top.block.bottom+SPACE.xl:SPACE.l})];
 },
 hard:([m])=>m.full.top<SPACE.m||m.full.bottom>H-SPACE.m?["Show the whole device inside the composition"]:[],
 score:([m])=>({
  size:sq(((m.box.right-m.box.left)-d.width)/SPACE.m),
  goldenX:sq((centre(m.critical[d.focus])[0]-d.target[0])/SPACE.l),
  goldenY:sq((centre(m.critical[d.focus])[1]-d.target[1])/SPACE.xl),
  pose:(sq(m.pose.pitch-d.pitch)+sq(m.pose.yaw-d.yaw)+sq(m.pose.roll-d.roll))/100,
  balance:sq((m.centroid[0]-W/2)/(W*.08))
 })
}));
export const measureAll=(poses:Pose[],screens:string[],ui:Record<string,ScreenUi>)=>poses.map((p,i)=>measure(p,ui[screens[i]]));
