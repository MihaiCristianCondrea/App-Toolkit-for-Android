import {CAPTION,H,SPACE,W,fitPose,measure} from "./geometry.ts";
import type {CaptionAt,Measured,Pose,ScreenUi} from "./geometry.ts";
import type {ShapePlan} from "./effects.ts";
export type Terms=Record<string,number>;
export type ShotSpec={id:string;intent:string;caption:CaptionAt;screens:string[];effects?:"sound";shapes:ShapePlan;candidates:(ui:Record<string,ScreenUi>)=>Iterable<Pose[]>;hard?:(ms:Measured[])=>string[];score:(ms:Measured[])=>Terms};
const specs=[
 {id:"ruler",width:660,yaw:-4,roll:-3,x:505,top:390},
 {id:"protractor",width:620,yaw:0,roll:2,x:570,top:455},
 {id:"advanced",width:650,yaw:3,roll:-2,x:510,top:385},
 {id:"converter",width:590,yaw:-8,roll:5,x:565,top:420},
 {id:"measure",width:630,yaw:6,roll:-4,x:525,top:400}
];
export const SHOTS:ShotSpec[]=specs.map(({id,width:target,yaw:turn,roll:lean,x,top},i)=>({
 id,intent:id,caption:"top",screens:[id],
 shapes:{seed:11+i*7,offset:i,small:3,big:{at:()=>[i%2?410:680,1170],max:980,min:700}},
 *candidates(ui){
 for(const pitch of [-2,0,2])for(const yaw of [turn-2,turn,turn+2])for(const roll of [lean-1,lean,lean+1])for(const width of [target-30,target,target+30])
 yield [fitPose({pitch,yaw,roll,perspective:3000},ui[id],{width,x:m=>m.centroid[0],xTarget:x,y:m=>m.full.top,yTarget:Math.max(top,CAPTION.top.block.bottom+SPACE.l)})];
 },
 hard:([m])=>m.full.bottom>H-SPACE.m?["Show the whole device"]:[],
 score:([m])=>({size:Math.pow(((m.box.right-m.box.left)-target)/30,2),pose:(m.pose.pitch*m.pose.pitch+Math.pow(m.pose.yaw-turn,2)+Math.pow(m.pose.roll-lean,2))/100})
}));
export const measureAll=(poses:Pose[],screens:string[],ui:Record<string,ScreenUi>)=>poses.map((p,i)=>measure(p,ui[screens[i]]));
