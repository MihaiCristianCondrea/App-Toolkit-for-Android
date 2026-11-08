import {CAPTION,GOLDEN,H,SPACE,W,centre,fitPose,measure,bbox} from "./geometry.ts";
import type {CaptionAt,Measured,Pose,ScreenUi} from "./geometry.ts";
import type {ShapePlan} from "./effects.ts";
export type Terms=Record<string,number>;
export type ShotSpec={id:string;intent:string;caption:CaptionAt;screens:string[];shapes:ShapePlan;candidates:(ui:Record<string,ScreenUi>)=>Iterable<Pose[]>;hard?:(ms:Measured[])=>string[];score:(ms:Measured[])=>Terms};
const sq=(n:number)=>n*n;
export const DIRECTIONS=[
 {id:"convert",screen:"length",caption:"bottom" as CaptionAt,width:900,pitch:3,yaw:-6,roll:-2,focus:"from",cropTop:true,x:540},
 {id:"everyday",screen:null,caption:"top" as CaptionAt},
 {id:"temperature-data",screen:null,caption:"bottom" as CaptionAt},
 {id:"toolkit",screen:"toolkit",caption:"top" as CaptionAt,width:650,pitch:-4,yaw:12,roll:3,focus:"converter",cropTop:false,x:580}
];
export const SHOTS:ShotSpec[]=DIRECTIONS.map((d,index)=>({
 id:d.id,intent:index===0?"Large real converter with an intentional top crop":"The broader Measure workspace",
 caption:d.caption,screens:d.screen?[d.screen]:[],shapes:{seed:11+index*17,small:5,size:[70,180],contained:true},
 *candidates(ui){
  if(!d.screen){yield [];return;}
  const top=d.caption==="top"?CAPTION.top.block.bottom+SPACE.xl:SPACE.xl;
  const bottom=d.caption==="bottom"?CAPTION.bottom.block.top-SPACE.xxl:H-SPACE.xl;
  for(const pitch of [d.pitch!-1,d.pitch!,d.pitch!+1])for(const yaw of [d.yaw!-2,d.yaw!,d.yaw!+2])
   for(const width of [d.width!-21,d.width!,d.width!+21])
    yield [fitPose({pitch,yaw,roll:d.roll!,perspective:3000},ui[d.screen],{
     width,x:m=>m.centroid[0],xTarget:d.x!,
     y:m=>d.cropTop?m.full.bottom:(m.full.top+m.full.bottom)/2,yTarget:d.cropTop?1446:(top+bottom)/2
    })];
 },
 hard:ms=>ms.flatMap(m=>[
  ...(!d.cropTop&&m.full.top<SPACE.m?["Show the whole phone"]:[]),
  ...(d.cropTop&&m.full.top>=0?["The hero must extend naturally beyond the top edge"]:[]),
  ...(m.full.bottom>H-SPACE.m?["The phone leaves the bottom edge"]:[]),
  ...(d.caption==="bottom"&&m.full.bottom>CAPTION.bottom.block.top-SPACE.xxl?["Keep the phone shadow clear of the caption"]:[])
 ]),
 score:(ms):Terms=>ms.length?{
  size:sq(((ms[0].box.right-ms[0].box.left)-d.width!)/SPACE.m),
  framing:sq((ms[0].centroid[0]-d.x!)/SPACE.m),
  goldenY:.05*sq((centre(ms[0].critical[d.focus!])[1]-GOLDEN.y[1])/SPACE.xxl),
  pose:(sq(ms[0].pose.pitch-d.pitch!)+sq(ms[0].pose.yaw-d.yaw!))/9
 }:{}
}));
type PairDirection={index:number;screens:[string,string];intent:string;tops:[number[],number[]];stagger:number;guides:[number,number];facing:number};
// Search both devices together, preserving their measured controls and a real gap between silhouettes.
const pairedShot=(d:PairDirection):ShotSpec=>({
 id:DIRECTIONS[d.index].id,intent:d.intent,caption:DIRECTIONS[d.index].caption,screens:d.screens,
 shapes:{seed:11+d.index*17,offset:d.index*2,small:5,size:[70,180],contained:true},
 *candidates(ui){
  for(const width of [460,480,500])for(const delta of [-13,0,13])
   for(const separation of [490,510,530])
    for(const rolls of [[-2,2],[2,-2]])
     for(const yaw of [6,9])
      for(const topLeft of d.tops[0])
       for(const topRight of d.tops[1]){
        const make=(screen:string,w:number,x:number,top:number,roll:number,yaw:number,pitch:number)=>fitPose(
         {pitch,yaw,roll,perspective:3000},ui[screen],
         {width:w,x:m=>m.centroid[0],xTarget:x,y:m=>m.full.top,yTarget:top});
        yield [
         make(d.screens[0],width,540-separation/2,topLeft,rolls[0],-yaw*d.facing,2*d.facing),
         make(d.screens[1],width+delta,540+separation/2,topRight,rolls[1],yaw*d.facing,-2*d.facing)
        ];
       }
 },
 hard:ms=>[
  ...ms.flatMap(m=>[
   ...(m.full.top<(DIRECTIONS[d.index].caption==="top"?400:SPACE.xl)||
       m.full.bottom>(DIRECTIONS[d.index].caption==="bottom"?CAPTION.bottom.block.top-SPACE.xl:H-SPACE.xl)?
       ["Keep both whole devices inside their body safe bounds"]:[]),
   ...(m.pose.scale<.94?["The conversion fields are too small"]:[]),
   ...(bbox(m.critical.from).right-bbox(m.critical.from).left<350?["The native conversion card is too narrow"]:[])
  ]),
  ...(ms[1].full.left-ms[0].full.right<SPACE.s?["Keep a measured gap between the two silhouettes"]:[])
 ],
 score:ms=>{
  const a=ms[0],b=ms[1],cx=(a.centroid[0]*a.area+b.centroid[0]*b.area)/(a.area+b.area);
  return {
   size:sq(((a.full.right-a.full.left)-480)/34)+sq(((b.full.right-b.full.left)-480)/34),
   pairBalance:2*sq((cx-540)/21),
   stagger:sq(((b.full.top-a.full.top)-d.stagger)/55),
   leftGuide:.12*sq((centre(a.critical.to)[1]-d.guides[0])/144),
   rightGuide:.12*sq((centre(b.critical.to)[1]-d.guides[1])/144),
   pose:(sq(a.pose.yaw+6*d.facing)+sq(b.pose.yaw-6*d.facing))/36
  };
 }
});
SHOTS[1]=pairedShot({index:1,screens:["mass","volume"],intent:"Mass and volume on complementary Pixel poses with readable fields and a balanced combined silhouette",tops:[[420,455,490],[640,695,750]],stagger:240,guides:[GOLDEN.y[1],GOLDEN.yLow],facing:1});
SHOTS[2]=pairedShot({index:2,screens:["temperature","data"],intent:"Temperature and decimal data on two whole Pixels with a reversed stagger and clear native guidance",tops:[[400,435,470],[110,145,180]],stagger:-290,guides:[GOLDEN.yLow,GOLDEN.y[1]],facing:-1});
export const measureAll=(poses:Pose[],screens:string[],ui:Record<string,ScreenUi>)=>poses.map((p,i)=>measure(p,ui[screens[i]]));
