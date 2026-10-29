// Android TV banner (drawable-xhdpi/tv_banner.png, 320 x 180): the toolbox and the name over MaterialShapes in its own
// browns and ambers. Run after npm run icons: npm run banner (CHROME_PATH picks a local Chromium). Shapes use the UI kit's polar MaterialShapes curves (marketing/ui/src/kit/assets.js).
import {chromium} from '../../../ui/node_modules/playwright-core/index.mjs';
import {fileURLToPath} from 'node:url';
import fs from 'node:fs';
const R=fileURLToPath(new URL('../../../../', import.meta.url));
const icon=fs.readFileSync(R+'marketing/assets/logos/icon.svg','utf8').replace('<svg','<svg width="100%" height="100%"');
const font=fs.readFileSync(R+'marketing/ui/public/fonts/GoogleSansFlex.ttf').toString('base64');
const CURVES={cookie12:t=>0.9+0.1*Math.cos(12*t),cookie9:t=>0.9+0.1*Math.cos(9*t),flower:t=>0.8+0.2*Math.cos(8*t),sunny:t=>0.82+0.18*Math.cos(12*t),
  clover:t=>0.62+0.38*Math.pow(Math.abs(Math.cos(2*t)),0.8),burst:t=>0.86+0.14*Math.cos(10*t),circle:()=>1};
const shape=(name,{x,y,size,fill,rot=0,stroke})=>{const r=CURVES[name];const pts=[];for(let i=0;i<240;i++){const t=i/240*2*Math.PI;pts.push([r(t)*Math.cos(t),r(t)*Math.sin(t)]);}
  const max=Math.max(...pts.map(([a,b])=>Math.max(Math.abs(a),Math.abs(b))));const d=pts.map(([a,b],i)=>(i?'L':'M')+(50+a/max*49).toFixed(2)+' '+(50+b/max*49).toFixed(2)).join('')+'Z';
  return `<svg style="position:absolute;left:${x}px;top:${y}px;width:${size}px;height:${size}px;transform:rotate(${rot}deg)" viewBox="0 0 100 100"><path d="${d}" ${stroke?`fill="none" stroke="${stroke}" stroke-width="${stroke && 400/size}"`:`fill="${fill}"`}/></svg>`;};
const dots=(x,y,c)=>`<svg style="position:absolute;left:${x}px;top:${y}px" width="34" height="22" viewBox="0 0 34 22">${[0,1].flatMap(r=>[0,1,2,3].map(k=>`<circle cx="${3+k*9.3}" cy="${3+r*9}" r="2.2" fill="${c}"/>`)).join('')}</svg>`;
const wave=(x,y,w,c,rot)=>`<svg style="position:absolute;left:${x}px;top:${y}px;width:${w}px;transform:rotate(${rot}deg)" viewBox="0 0 200 30" fill="none"><path d="M3 15 q12.5 -12 25 0 t25 0 t25 0 t25 0 t25 0 t25 0 t25 0" stroke="${c}" stroke-width="5" stroke-linecap="round"/></svg>`;
const html=`<style>@font-face{font-family:GSF;src:url(data:font/ttf;base64,${font});font-weight:1 1000;font-stretch:25% 151%}body{margin:0}</style>
<div id=b style="width:320px;height:180px;position:relative;overflow:hidden;background:radial-gradient(120% 140% at 22% 50%,#5A3800 0%,#3A2400 55%,#241600 100%)">
${shape('flower',{x:196,y:-64,size:200,fill:'#4A2F00',rot:8})}
${shape('cookie12',{x:250,y:104,size:120,fill:'#F6A800',rot:12})}
${shape('clover',{x:-34,y:118,size:96,fill:'#6E4600',rot:20})}
${shape('sunny',{x:272,y:20,size:30,fill:'#FFD03B',rot:0})}
${dots(14,14,'#8F5D00')}
${wave(146,10,64,'#7F5200',-6)}
${shape('cookie12',{x:6,y:22,size:136,fill:'#4C3000',rot:-6})}
<div style="position:absolute;left:30px;top:46px;width:88px;height:88px;filter:drop-shadow(0 5px 6px rgba(20,10,0,.45))">${icon}</div>
<div style="position:absolute;left:138px;top:50px;font:650 31px/0.98 GSF;font-stretch:112%;color:#FFF2C6;letter-spacing:-0.4px">App<br>Toolkit</div>
<div style="position:absolute;left:139px;top:118px;font:500 10.5px/1 GSF;font-stretch:100%;letter-spacing:1.6px;color:#FFD03B">EVERYDAY TOOLS</div>
</div>`;
const b=await chromium.launch(process.env.CHROME_PATH?{executablePath:process.env.CHROME_PATH}:{});
for(const [scale,file] of [[1,R+'sample/app/src/main/res/drawable-xhdpi/tv_banner.png']]){
  const p=await b.newPage({viewport:{width:320,height:180},deviceScaleFactor:scale});await p.setContent(html);await p.evaluate(()=>document.fonts.ready);
  await p.locator('#b').screenshot({path:file});await p.close();}
await b.close();
