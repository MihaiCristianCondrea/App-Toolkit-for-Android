import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {execFileSync} from 'node:child_process';

const source=fileURLToPath(new URL('../',import.meta.url));
process.chdir(source);
const defaults=[.3,.9,1.8,2.4,3,3.8,5,8,9.6,11.2,12.4,15.8,18.9,21,23.5,25.9,27.5,29.6,29.85];
const seconds=process.argv.length>2?process.argv.slice(2).map(Number):defaults;
const timeline=JSON.parse(fs.readFileSync('src/timeline.json'));
if(seconds.some(t=>!Number.isFinite(t)||t<0||t>=timeline.durationSeconds))throw new Error('Use seconds between 0 and the end of the film.');
const output=path.resolve('../output/images');
const frames=path.join(output,'review-frames');
fs.mkdirSync(frames,{recursive:true});
for(const [i,t] of seconds.entries()){
  const args=[path.resolve('node_modules/@remotion/cli/remotion-cli.js'),'still','src/index.ts','AppTrailer',path.join(frames,`${i+1}.png`),'--frame',String(Math.round(t*90)),'--scale','.2','--log','error'];
  if(process.env.BROWSER_EXECUTABLE)args.push('--browser-executable',process.env.BROWSER_EXECUTABLE);
  execFileSync(process.execPath,args,{stdio:'inherit'});
  console.log(`${i+1}/${seconds.length}: ${t}s`);
}
execFileSync(process.env.PYTHON_PATH||'python',['-c',`
import json,sys,math
from pathlib import Path
from PIL import Image,ImageDraw,ImageFont,PngImagePlugin
data=json.load(sys.stdin); out=Path(data['output']); times=data['seconds']
cols=min(5,len(times)); sheet=Image.new('RGB',(cols*384,math.ceil(len(times)/cols)*248),'#FFF4D9')
draw=ImageDraw.Draw(sheet); font=ImageFont.truetype('public/fonts/GoogleSansFlex.ttf',18)
for i,t in enumerate(times):
 x=(i%cols)*384; y=(i//cols)*248
 sheet.paste(Image.open(out/'review-frames'/f'{i+1}.png'),(x,y+32))
 draw.text((x+10,y+5),f'{i+1:02}. {t:g} s',font=font,fill='#3B2D14')
meta=PngImagePlugin.PngInfo(); meta.add_itxt('Title','App Toolkit: trailer review'); meta.add_itxt('Author','Mihai-Cristian Condrea'); meta.add_itxt('Description','Numbered trailer samples labelled with their time in seconds.')
sheet.save(out/'review-sheet.png',pnginfo=meta)
`],{input:JSON.stringify({output,seconds}),stdio:['pipe','inherit','inherit']});
console.log(path.join(output,'review-sheet.png'));
