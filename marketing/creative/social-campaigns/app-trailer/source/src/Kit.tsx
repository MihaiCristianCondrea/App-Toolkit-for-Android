import React, {useLayoutEffect, useRef, useState} from 'react';
import {cancelRender, staticFile, useDelayRender} from 'remotion';
import * as kit from '../../../../../ui/src/embed.js';
import device from './generated/device.json';

export const PHONE_WIDTH=430;
export const PHONE_HEIGHT=PHONE_WIDTH*device.display.height/device.display.width;
export const SKIN_SCALE=PHONE_WIDTH/device.display.width;
export const SKIN_WIDTH=device.width*SKIN_SCALE;
export const SKIN_HEIGHT=device.height*SKIN_SCALE;
export const DISPLAY={x:device.display.x*SKIN_SCALE,y:device.display.y*SKIN_SCALE,width:PHONE_WIDTH,height:PHONE_HEIGHT,radius:device.display.radius*SKIN_SCALE};
export type Rect={x:number;y:number;width:number;height:number;radii?:number[]};
export type Geometry=Record<string,Rect>;
export const WIDGETS=[
  {id:'compass',selector:'.qt-live[aria-label^="Compass,"]',query:''},
  {id:'level',selector:'.qt-live[aria-label^="Bubble level,"]',query:''},
  {id:'flash',selector:'.qt-live--signal[data-accent="gold"]',query:''},
  {id:'sos',selector:'.qt-live[data-accent="magenta"]',query:''},
  {id:'water',selector:'.qt-live[data-tool="water_eject"]',query:'Water Eject'},
  {id:'breathe',selector:'.qt-breathe',query:'Breathe'},
] as const;

type Embed={setup:(options:Record<string,unknown>)=>Promise<void>;mount:(el:HTMLElement,id:string,state:Record<string,unknown>,options:Record<string,unknown>)=>Promise<void>};
const embed=kit as unknown as Embed;
let ready:Promise<void>|undefined;
const boot=()=>ready??=(async()=>{
  const base=staticFile('fonts/GoogleSansFlex.ttf').replace('fonts/GoogleSansFlex.ttf','');
  await embed.setup({assetBase:base,marketingAssetBase:base+'material-next/',theme:'light',palette:'blue'});
  await document.fonts.ready;
  await document.fonts.load('700 120px "Google Sans Flex"');
})();
const frame=()=>new Promise<void>(resolve=>requestAnimationFrame(()=>resolve()));

/** Invert the rendered axes, so Studio zoom and the film's card rotations never distort measurements. */
const localRect=(node:HTMLElement,root:HTMLElement):Rect=>{
  const rootPosition=root.style.position,nodePosition=node.style.position;
  if(getComputedStyle(root).position==='static')root.style.position='relative';
  if(getComputedStyle(node).position==='static')node.style.position='relative';
  const marker=(parent:HTMLElement,x:string,y:string)=>{
    const point=document.createElement('i');
    point.style.cssText=`position:absolute;left:${x};top:${y};width:0;height:0;margin:0;padding:0;border:0;pointer-events:none`;
    parent.append(point);return point;
  };
  const points=[marker(root,'0px','0px'),marker(root,'100px','0px'),marker(root,'0px','100px'),marker(node,'0px','0px'),marker(node,'100%','100%')];
  const [o,xAxis,yAxis,start,end]=points.map(point=>point.getBoundingClientRect());
  const a=(xAxis.x-o.x)/100,b=(xAxis.y-o.y)/100,c=(yAxis.x-o.x)/100,d=(yAxis.y-o.y)/100;
  const determinant=a*d-b*c;
  const invert=(point:DOMRect)=>({x:(d*(point.x-o.x)-c*(point.y-o.y))/determinant,y:(a*(point.y-o.y)-b*(point.x-o.x))/determinant});
  const p=invert(start),q=invert(end);
  points.forEach(point=>point.remove());root.style.position=rootPosition;node.style.position=nodePosition;
  return {x:p.x,y:p.y,width:q.x-p.x,height:q.y-p.y};
};

/** The actual screen is mounted from frame state. Fonts, images and Web Components finish before capture. */
export const LiveScreen:React.FC<{id:string;state?:Record<string,unknown>;onReady?:(root:HTMLDivElement)=>void;style?:React.CSSProperties}>=({id,state={},onReady,style})=>{
  const root=useRef<HTMLDivElement>(null);
  const {delayRender,continueRender}=useDelayRender();
  const key=JSON.stringify(state);
  const callback=useRef(onReady);callback.current=onReady;
  useLayoutEffect(()=>{
    const element=root.current!;
    const handle=delayRender(`App Toolkit ${id}`,{timeoutInMilliseconds:60000});
    let live=true;
    (async()=>{
      await boot();
      if(!live)return;
      await embed.mount(element,id,{...JSON.parse(key),showAds:false,moreApps:[]},{frame:false,palette:'blue',window:{width:PHONE_WIDTH,height:PHONE_HEIGHT,system:'android'}});
      await Promise.all([...element.querySelectorAll('img')].map(img=>img.decode()));
      await frame();
      // Interactive digit motion has no wall clock in a film. The supplied frame owns the value.
      for(const animation of element.getAnimations({subtree:true}))animation.finish();
      if(live)callback.current?.(element);
      await frame();
    })().catch(cancelRender).finally(()=>continueRender(handle));
    return()=>{live=false;continueRender(handle);};
  },[id,key,delayRender,continueRender]);
  return <div ref={root} data-theme="light" data-palette="blue" style={{width:PHONE_WIDTH,height:PHONE_HEIGHT,...style}}/>;
};

/** Geometry comes from the same screen the viewer will see after the assembly. */
export const GeometryProbe:React.FC<{onGeometry:(g:Geometry)=>void}>=({onGeometry})=><LiveScreen id="quick-tools" state={{compassHeading:312,levelRoll:0,levelPitch:0}} style={{position:'absolute',left:-10000,top:0,opacity:0}} onReady={root=>{
  const geometry:Geometry={};
  for(const widget of WIDGETS){
    const node=root.querySelector<HTMLElement>(widget.selector);
    if(!node)throw new Error(`Missing real widget: ${widget.id}`);
    const box=localRect(node,root);
    const style=getComputedStyle(node);
    geometry[widget.id]={...box,radii:[style.borderTopLeftRadius,style.borderTopRightRadius,style.borderBottomRightRadius,style.borderBottomLeftRadius].map(parseFloat)};
  }
  const shell=root.querySelector('mnw-android-screen')?.shadowRoot;
  for(const button of shell?.querySelectorAll<HTMLButtonElement>('.navbar__item')??[]){
    const label=button.querySelector('.navbar__label')?.textContent?.trim();
    if(!label)continue;
    geometry[label]=localRect(button,root);
  }
  onGeometry(geometry);
}}/>;

/** Crop the real rendered widget, retaining its Material components, fonts, shape and theme context. */
export const LiveCrop:React.FC<{screen:string;selector:string;state?:Record<string,unknown>;style?:React.CSSProperties}>=({screen,selector,state={},style})=>{
  const [box,setBox]=useState<Rect|null>(null);
  return <div data-kit-crop={selector} style={{position:'relative',width:box?.width??0,height:box?.height??0,overflow:'hidden',...style}}>
    <LiveScreen id={screen} state={state} style={{position:'absolute',left:0,top:0,transform:`translate(${-Number(box?.x??0)}px,${-Number(box?.y??0)}px)`}} onReady={root=>{
      const node=root.querySelector<HTMLElement>(selector);
      if(!node)throw new Error(`No ${selector} in real ${screen} screen`);
      const next=localRect(node,root);
      setBox(old=>old&&JSON.stringify(old)===JSON.stringify(next)?old:next);
    }}/>
  </div>;
};
