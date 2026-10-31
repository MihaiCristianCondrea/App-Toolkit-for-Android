import {Easing,spring} from 'remotion';

export const lerp=(a:number,b:number,p:number)=>a+(b-a)*p;
export const clamp=(n:number)=>Math.max(0,Math.min(1,n));
const emphasized=Easing.bezier(.2,0,0,1),accelerate=Easing.bezier(.3,0,.8,.15);
/** Material emphasized: a quick start and a long, exact settle. */
export const ease=(t:number,start:number,end:number)=>emphasized(clamp((t-start)/(end-start)));
/** Exits accelerate away instead of drifting. */
export const easeIn=(t:number,start:number,end:number)=>accelerate(clamp((t-start)/(end-start)));
/** A lightly damped spring (one small overshoot) whose tail blends to exact rest. */
export const pop=(t:number,start:number,duration=.7)=>{
  if(t<=start)return 0;
  const frame=(t-start)*90,length=duration*90;
  if(frame>=length)return 1;
  return lerp(spring({frame,fps:90,config:{damping:15,stiffness:170,mass:.85}}),1,clamp((frame-length*.7)/(length*.3)));
};
