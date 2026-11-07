import { CAPTION, H, SPACE, W, fitPose, measure } from "./geometry.ts";
import type { CaptionAt, Measured, Pose, ScreenUi } from "./geometry.ts";
import type { ShapePlan } from "./effects.ts";
export type Terms = Record<string, number>;
export type ShotSpec = { id:string; intent:string; caption:CaptionAt; screens:string[]; effects?:"sound"; shapes:ShapePlan; candidates:(ui:Record<string,ScreenUi>)=>Iterable<Pose[]>; hard?:(ms:Measured[])=>string[]; score:(ms:Measured[])=>Terms };
const specs=[
 {id:"ruler", width:660, yaw:0, roll:0},
 {id:"protractor", width:630, yaw:0, roll:0},
 {id:"advanced", width:660, yaw:0, roll:0},
 {id:"converter", width:570, yaw:-4, roll:-2},
 {id:"measure", width:600, yaw:4, roll:2}
];
export const SHOTS:ShotSpec[]=specs.map(({id,width:target,yaw:turn,roll:lean},i)=>({
 id, intent:id, caption:"top", screens:[id], shapes:{seed:11+i*7,small:1},
 *candidates(ui) {
   for (const pitch of [-2,0,2]) for (const yaw of [-4,0,4]) for (const roll of [-2,0,2]) for (const width of [540,570,600,630,660])
     yield [fitPose({pitch,yaw,roll,perspective:3000},ui[id],{width,x:m=>m.centroid[0],xTarget:W/2,y:m=>m.full.top,yTarget:CAPTION.top.block.bottom+SPACE.xl})];
 },
 hard:([m])=>m.full.bottom>H-SPACE.m?["Show the whole device"]:[],
 score:([m])=>({width:Math.pow(((m.box.right-m.box.left)-target)/60,2),perspective:(m.pose.pitch*m.pose.pitch+Math.pow(m.pose.yaw-turn,2)+Math.pow(m.pose.roll-lean,2))/100})
}));
export const measureAll=(poses:Pose[],screens:string[],ui:Record<string,ScreenUi>)=>poses.map((p,i)=>measure(p,ui[screens[i]]));
