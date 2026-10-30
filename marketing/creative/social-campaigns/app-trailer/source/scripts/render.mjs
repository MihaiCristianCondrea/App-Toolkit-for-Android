import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {execFileSync} from 'node:child_process';

const source=fileURLToPath(new URL('../',import.meta.url));
process.chdir(source);
const run=(cmd,args)=>execFileSync(cmd,args,{cwd:source,stdio:'inherit'});
run(process.execPath,['scripts/prepare.mjs']);
run(process.execPath,['scripts/check.mjs']);
const mode=process.argv[2]??'preview';
if(!['preview','master','frames'].includes(mode))throw new Error('Use preview, master or frames');
const cli=path.join(source,'node_modules/@remotion/cli/remotion-cli.js');
const output=path.resolve(source,'../output');
fs.mkdirSync(output+'/video',{recursive:true});fs.mkdirSync(output+'/images',{recursive:true});
const render=args=>run(process.execPath,[cli,...args]);
if(mode!=='frames'){
  const raw=`${output}/video/app-toolkit-${mode}-raw.mp4`;
  const final=`${output}/video/app-toolkit-${mode}.mp4`;
  render(['render','src/index.ts','AppTrailer',raw,'--scale',mode==='master'?String(4/3):'.5','--codec','h264','--crf',mode==='master'?'16':'20','--audio-codec','aac','--audio-bitrate','320k','--concurrency','2','--log','info']);
  const manifest=JSON.parse(fs.readFileSync('../app-trailer-metadata.json'));
  run(process.env.FFMPEG_PATH||'ffmpeg',['-y','-v','error','-i',raw,'-map','0','-c','copy','-metadata',`title=${manifest.copy.title}`,'-metadata','artist=Mihai-Cristian Condrea','-metadata',`description=${manifest.copy.description}`,'-metadata',`comment=${manifest.copy.longDescription}`,'-metadata',`keywords=${manifest.copy.keywords.join(', ')}`,'-movflags','+faststart',final]);
  run(process.execPath,['scripts/check.mjs',final]);
  fs.unlinkSync(raw);
}
if(mode==='frames'||!fs.existsSync(`${output}/images/contact-sheet.png`)){
  const shots=[1,2.6,5.9,8.8,15.5,20.5,25.5,28.8];
  for(let i=0;i<shots.length;i++)render(['still','src/index.ts','AppTrailer',`${output}/images/scene-${String(i+1).padStart(2,'0')}.png`,'--frame',String(Math.round(shots[i]*90)),'--scale','.5','--log','error']);
  run(process.env.PYTHON_PATH||'python',['scripts/review.py']);
}
const files=['images','video'].flatMap(dir=>fs.readdirSync(`${output}/${dir}`).filter(file=>/\.(png|mp4)$/.test(file)).map(file=>`${output}/${dir}/${file}`));
run(process.env.PYTHON_PATH||'python',[path.resolve(source,'../../../../skills/built-in/metadata/scripts/inject_metadata.py'),'--manifest',path.resolve(source,'../app-trailer-metadata.json'),'--jsonld',`${output}/metadata.jsonld`,...files]);
