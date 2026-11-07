// Offline optical water study. Fixed inputs, CPU evaluation, no clock, pointer events or animation loop.
// Surface normals bend the depth colour; the refraction Jacobian concentrates light into caustics.
import {deflateSync} from 'node:zlib';
import {mkdir,writeFile} from 'node:fs/promises';
import {fileURLToPath} from 'node:url';
import {createHash} from 'node:crypto';
const out=fileURLToPath(new URL('../public/water/',import.meta.url));
await mkdir(out,{recursive:true});
let seed=73821;
const random=()=>{seed=(Math.imul(seed,1664525)+1013904223)>>>0;return seed/4294967296;};
const waves=Array.from({length:12},(_,i)=>{
 const angle=random()*Math.PI*2,f=9+random()*24;
 return {kx:Math.cos(angle)*f,ky:Math.sin(angle)*f,a:.0025+random()*.004,phase:random()*Math.PI*2};
});
const clamp=(x,a=0,b=1)=>Math.max(a,Math.min(b,x));
const smooth=(a,b,x)=>{const t=clamp((x-a)/(b-a));return t*t*(3-2*t);};
const crcTable=Array.from({length:256},(_,i)=>{let c=i;for(let k=0;k<8;k++)c=c&1?0xedb88320^(c>>>1):c>>>1;return c>>>0;});
const chunk=(name,bytes)=>{
 const type=Buffer.from(name),data=Buffer.concat([type,bytes]);let crc=0xffffffff;
 for(const x of data)crc=crcTable[(crc^x)&255]^(crc>>>8);
 const n=Buffer.alloc(4),c=Buffer.alloc(4);n.writeUInt32BE(bytes.length);c.writeUInt32BE((crc^0xffffffff)>>>0);
 return Buffer.concat([n,data,c]);
};
function png(width,height,pixels){
 const head=Buffer.alloc(13);head.writeUInt32BE(width);head.writeUInt32BE(height,4);head[8]=8;head[9]=2;
 const rows=Buffer.alloc(height*(width*3+1));
 for(let y=0;y<height;y++)pixels.copy(rows,y*(width*3+1)+1,y*width*3,(y+1)*width*3);
 return Buffer.concat([Buffer.from([137,80,78,71,13,10,26,10]),chunk('IHDR',head),chunk('IDAT',deflateSync(rows,{level:9})),chunk('IEND',Buffer.alloc(0))]);
}
function render(width,height){
 const pixels=Buffer.alloc(width*height*3);
 for(let y=0;y<height;y++)for(let x=0;x<width;x++){
  const u=x/height,v=y/height;
  let hx=0,hy=0,hxx=0,hxy=0,hyy=0;
  for(const w of waves){
   const p=u*w.kx+v*w.ky+w.phase,s=Math.sin(p),c=Math.cos(p),a=w.a;
   hx+=a*w.kx*c;hy+=a*w.ky*c;
   hxx-=a*w.kx*w.kx*s;hxy-=a*w.kx*w.ky*s;hyy-=a*w.ky*w.ky*s;
  }
  // Three fixed wave packets interrupt the cross swell. These are light on water, not drawn rings.
  for(const [cx,cy,radius] of [[.09,.68,.24],[2.15,.79,.33],[3.82,.53,.21]]){
   const dx=u-cx,dy=v-cy,dist=Math.hypot(dx,dy)+1e-6,q=dist-radius,inv=1/(.11*.11),k=66;
   const env=.004*Math.exp(-q*q*inv),c=Math.cos(k*q),s=Math.sin(k*q);
   const first=env*(-2*q*inv*c-k*s),second=env*((4*q*q*inv*inv-2*inv-k*k)*c+4*q*inv*k*s);
   const nx=dx/dist,ny=dy/dist;
   hx+=first*nx;hy+=first*ny;
   hxx+=second*nx*nx+first/dist*(1-nx*nx);
   hyy+=second*ny*ny+first/dist*(1-ny*ny);
   hxy+=(second-first/dist)*nx*ny;
  }
  const det=(1+.32*hxx)*(1+.32*hyy)-(.32*hxy)**2;
  const focus=Math.exp(-Math.abs(det)*7);
  const normal=Math.sqrt(hx*hx+hy*hy+.8*.8);
  const spec=Math.pow(clamp((-.32*hx-.38*hy+.86*.8)/normal),34);
  const depth=clamp(.16+v*.55+.11*Math.sin((u+hx*.4)*3.8+(v+hy*.4)*2));
  const top=[166,234,218],bottom=[5,153,171];
  const calm=smooth(.17,.45,v);
  const light=.75+(.8/normal)*.25;
  const i=(y*width+x)*3;
  for(let c=0;c<3;c++){
   const water=(top[c]*(1-depth)+bottom[c]*depth)*light+(focus*.26+spec*.12)*[185,224,207][c];
   pixels[i+c]=Math.round(clamp([221,249,242][c]*(1-calm)+water*calm,0,255));
  }
 }
 return png(width,height,pixels);
}
const checks={};
for(const [name,w,h] of [['phone',4320,960],['wide',3840,540],['feature',1024,500]]){
 const bytes=render(w,h);
 await writeFile(out+name+'.png',bytes);
 checks[name]={width:w,height:h,sha256:createHash('sha256').update(bytes).digest('hex')};
}
await writeFile(out+'generation.json',JSON.stringify(checks,null,2)+'\n');
console.log('Generated deterministic water textures:',checks);
