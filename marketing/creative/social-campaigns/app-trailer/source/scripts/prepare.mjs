import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {createHash} from 'node:crypto';
import {execFileSync} from 'node:child_process';

const source=fileURLToPath(new URL('../',import.meta.url));
const campaign=path.resolve(source,'..');
const marketing=path.resolve(source,'../../../..');
const publicDir=path.join(source,'public');
const generated=path.join(source,'src/generated');
fs.mkdirSync(publicDir,{recursive:true}); fs.mkdirSync(generated,{recursive:true});
for (const name of ['fonts','logos','shapes']) {
  const from=path.join(marketing,'ui/public',name);
  if (!fs.existsSync(from)) throw new Error(`Missing ${from}. Run npm run assets in marketing/ui first.`);
  fs.cpSync(from,path.join(publicDir,name),{recursive:true});
}
fs.mkdirSync(path.join(publicDir,'device'),{recursive:true});
const skin=path.join(marketing,'assets/device-frames/pixel_9_pro');
for(const name of ['back.webp','mask.webp'])fs.copyFileSync(path.join(skin,name),path.join(publicDir,'device',name));
const layout=fs.readFileSync(path.join(skin,'layout'),'utf8');
const block=(name)=>layout.match(new RegExp(`${name} \\{([^}]+)\\}`))[1];
const number=(text,key)=>Number(text.match(new RegExp(`\\b${key} (\\d+)`))[1]);
const display=block('display'), portrait=layout.slice(layout.indexOf('layouts {'));
const part=portrait.match(/part2 \{([^}]+)\}/)[1];
const device={width:number(portrait,'width'),height:number(portrait,'height'),display:{x:number(part,'x'),y:number(part,'y'),width:number(display,'width'),height:number(display,'height'),radius:number(display,'corner_radius')}};
fs.writeFileSync(path.join(generated,'device.json'),JSON.stringify(device,null,2)+'\n');
const logo=fs.readFileSync(path.join(marketing,'assets/logos/icon.svg'),'utf8');
const nodes=logo.match(/<path\b[^>]*\/>|<g\b[\s\S]*?<\/g>/g);
if(nodes?.length!==8)throw new Error('Logo structure changed. Review its lid/body grouping before rendering.');
const viewBox=logo.match(/viewBox="([^"]+)"/)[1];
for(const [name,indices] of [['toolbox-base',[2,4,5,6,7]],['toolbox-lid',[0,1,3]]]) {
  fs.writeFileSync(path.join(publicDir,`${name}.svg`),`<svg xmlns="http://www.w3.org/2000/svg" viewBox="${viewBox}">${indices.map(i=>nodes[i]).join('')}</svg>`);
}
fs.copyFileSync(path.join(marketing,'assets/badges/google-play/en_badge_web_generic.png'),path.join(publicDir,'google-play.png'));
fs.mkdirSync(path.join(publicDir,'audio'),{recursive:true});
const soundtrack=path.join(campaign,'soundtrack.mp3');
fs.copyFileSync(soundtrack,path.join(publicDir,'audio/soundtrack.mp3'));
const probe=JSON.parse(execFileSync(process.env.FFPROBE_PATH||'ffprobe',['-v','error','-show_entries','format=duration','-of','json',soundtrack],{encoding:'utf8'}));
const durationSeconds=Number(probe.format.duration);
const timeline=JSON.parse(fs.readFileSync(path.join(source,'src/timeline.json')));
if(Math.abs(durationSeconds-timeline.durationSeconds)>.03)throw new Error('Soundtrack duration changed. Adapt the timeline; never trim the music to fit.');
const media={durationSeconds,soundtrackSha256:createHash('sha256').update(fs.readFileSync(soundtrack)).digest('hex'),logoSource:'marketing/assets/logos/icon.svg',deviceSource:'marketing/assets/device-frames/pixel_9_pro/layout'};
fs.writeFileSync(path.join(generated,'media.json'),JSON.stringify(media,null,2)+'\n');
console.log(`Prepared original logo layers, fonts, shapes, Pixel skin, Play badge and all ${durationSeconds}s of the soundtrack.`);
