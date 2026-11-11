import {CAPTION,GOLDEN,H,SPACE,W,centre,fitPose,measure} from "./geometry.ts";
import type {CaptionAt,Measured,Pose,ScreenUi} from "./geometry.ts";
import type {ShapePlan} from "./effects.ts";
export type Terms=Record<string,number>;
export type ShotSpec={id:string;intent:string;caption:CaptionAt;screens:string[];shapes:ShapePlan;candidates:(ui:Record<string,ScreenUi>)=>Iterable<Pose[]>;hard?:(ms:Measured[])=>string[];score:(ms:Measured[])=>Terms};
const sq=(n:number)=>n*n;
const directions=[
 {id:"dinner",caption:"top" as CaptionAt,width:680,pitch:1,yaw:-4,roll:-2,focus:"share",crop:false,x:540,y:1070,intent:"Readable dinner bill and equal shares in one complete Pixel"},
 {id:"tip",caption:"bottom" as CaptionAt,width:820,pitch:2,yaw:5,roll:2,focus:"tip",crop:true,x:520,y:1460,intent:"Closer view of the native percentage choices and updated total"},
 {id:"friends",caption:"top" as CaptionAt,width:660,pitch:-2,yaw:6,roll:2,focus:"people",crop:false,x:575,y:1090,intent:"Four-person dinner, with every input and the share visible"},
 {id:"currency",caption:"bottom" as CaptionAt,width:680,pitch:1,yaw:-6,roll:-2,focus:"share",crop:false,x:530,y:775,intent:"Whole Pixel with JPY denomination and whole-yen rounding"},
 {id:"toolkit",caption:"top" as CaptionAt,width:650,pitch:-3,yaw:8,roll:2,focus:"bill",crop:false,x:550,y:1080,intent:"The broader Measure workspace after the bill story"}
];
export const SHOTS:ShotSpec[]=directions.map((d,index)=>({
 id:d.id,intent:d.intent,caption:d.caption,screens:[d.id],shapes:{seed:29+index*41,offset:index*2,small:5,size:[75,180],contained:true},
 *candidates(ui){
  for(const pitch of [d.pitch-1,d.pitch,d.pitch+1])for(const yaw of [d.yaw-2,d.yaw,d.yaw+2])
   for(const width of [d.width-21,d.width,d.width+21])for(const dx of [-13,0,13])
    yield [fitPose({pitch,yaw,roll:d.roll,perspective:3000},ui[d.id],{
     width,x:m=>m.centroid[0],xTarget:d.x+dx,y:m=>d.crop?m.full.bottom:(m.full.top+m.full.bottom)/2,yTarget:d.y
    })];
 },
 hard:ms=>ms.flatMap(m=>[
  ...(!d.crop&&(m.full.top<SPACE.m||m.full.bottom>H-SPACE.m)?["Show the whole Pixel"]:[]),
  ...(d.crop&&m.full.top>=0?["The close shot must extend naturally beyond the top image edge"]:[]),
  ...(d.caption==="bottom"&&m.full.bottom>CAPTION.bottom.block.top-SPACE.xl?["Keep the device clear of the lower caption"]:[])
 ]),
 score:(ms):Terms=>{
  const m=ms[0];
  return {
   size:sq(((m.full.right-m.full.left)-d.width)/34),
   balance:sq((m.centroid[0]-d.x)/34),
   focus:.025*sq((centre(m.critical[d.focus])[1]-GOLDEN.y[1])/144),
   pose:(sq(m.pose.pitch-d.pitch)+sq(m.pose.yaw-d.yaw))/9
  };
 }
}));
export const measureAll=(poses:Pose[],screens:string[],ui:Record<string,ScreenUi>)=>poses.map((p,i)=>measure(p,ui[screens[i]]));
