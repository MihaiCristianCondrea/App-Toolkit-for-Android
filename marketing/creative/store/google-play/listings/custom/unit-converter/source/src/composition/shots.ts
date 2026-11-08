import {CAPTION,GOLDEN,H,SPACE,W,centre,fitPose,measure,bbox} from "./geometry.ts";
import type {CaptionAt,Measured,Pose,ScreenUi} from "./geometry.ts";
import type {ShapePlan} from "./effects.ts";
import type {PanelPlan} from "./panels.ts";
export type Terms=Record<string,number>;
export type ShotSpec={id:string;intent:string;caption:CaptionAt;screens:string[];panels:PanelPlan[];shapes:ShapePlan;candidates:(ui:Record<string,ScreenUi>)=>Iterable<Pose[]>;hard?:(ms:Measured[])=>string[];score:(ms:Measured[])=>Terms};
const sq=(n:number)=>n*n;
export const DIRECTIONS=[
 {id:"convert",screen:"length",caption:"bottom" as CaptionAt,width:900,pitch:3,yaw:-6,roll:-2,focus:"from",cropTop:true,x:540},
 {id:"everyday",screen:null,caption:"top" as CaptionAt},
 {id:"temperature-data",screen:null,caption:"bottom" as CaptionAt},
 {id:"toolkit",screen:"toolkit",caption:"top" as CaptionAt,width:650,pitch:-4,yaw:12,roll:3,focus:"converter",cropTop:false,x:580}
];
const panelPlans:PanelPlan[][]=[
 [],
 [],
 [{screen:"temperature",label:"Temperature",x:84,y:210,width:600},{screen:"data",label:"Data storage",x:436,y:865,width:560}],
 []
];
export const SHOTS:ShotSpec[]=DIRECTIONS.map((d,index)=>({
 id:d.id,intent:index===0?"Large real converter with an intentional top crop":index===3?"The broader Measure workspace":"Related conversion categories grouped as real UI details",
 caption:d.caption,screens:d.screen?[d.screen]:[],panels:panelPlans[index],shapes:{seed:11+index*17,small:0},
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
// Search both devices as one composition. Their fields, rather than their anchors, determine fit.
SHOTS[1]={
 id:"everyday",intent:"Mass and volume on complementary Pixel poses with readable fields and a balanced combined silhouette",
 caption:"top",screens:["mass","volume"],panels:[],shapes:{seed:28,small:0},
 *candidates(ui){
  for(const width of [460,480,500])for(const delta of [-13,0,13])
   for(const separation of [490,510,530])
    for(const rolls of [[-2,2],[2,-2]])
     for(const yaw of [6,9])
      for(const topLeft of [420,455,490])
       for(const topRight of [640,695,750]){
        const make=(screen:string,w:number,x:number,top:number,roll:number,yaw:number,pitch:number)=>fitPose(
         {pitch,yaw,roll,perspective:3000},ui[screen],
         {width:w,x:m=>m.centroid[0],xTarget:x,y:m=>m.full.top,yTarget:top});
        yield [
         make("mass",width,540-separation/2,topLeft,rolls[0],-yaw,2),
         make("volume",width+delta,540+separation/2,topRight,rolls[1],yaw,-2)
        ];
       }
 },
 hard:ms=>[
  ...ms.flatMap(m=>[
   ...(m.full.top<400||m.full.bottom>H-SPACE.xl?["Keep both whole devices inside their body safe bounds"]:[]),
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
   stagger:sq(((b.full.top-a.full.top)-240)/55),
   massGuide:.12*sq((centre(a.critical.to)[1]-GOLDEN.y[1])/144),
   volumeGuide:.12*sq((centre(b.critical.to)[1]-GOLDEN.yLow)/144),
   pose:(sq(a.pose.yaw+6)+sq(b.pose.yaw-6))/36
  };
 }
};
export const measureAll=(poses:Pose[],screens:string[],ui:Record<string,ScreenUi>)=>poses.map((p,i)=>measure(p,ui[screens[i]]));
