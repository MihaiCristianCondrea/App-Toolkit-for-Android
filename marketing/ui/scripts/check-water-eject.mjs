import assert from 'node:assert/strict';
import {createServer} from 'vite';
import {chromium} from 'playwright-core';
import {fileURLToPath} from 'node:url';
const root=fileURLToPath(new URL('../',import.meta.url));
const server=await createServer({root,server:{host:'127.0.0.1',port:0}});
await server.listen();
const browser=await chromium.launch({executablePath:process.env.CHROMIUM_PATH});
try {
 const page=await browser.newPage();
 await page.goto('http://127.0.0.1:'+server.httpServer.address().port+'/screen.html?id=quick-tools');
 await page.locator('mnw-android-screen').waitFor();
 const result=await page.evaluate(async()=>{
  const {waterBody}=await import('/src/kit/water-eject.js');
  const {render}=await import('/node_modules/lit/index.js');
  let audioStarts=0;
  window.AudioContext=class {constructor(){audioStarts++;throw Error('Capture must not start audio');}};
  const host=document.createElement('div');document.body.append(host);
  let updates=0;
  render(waterBody({waterCapture:true,waterRunning:false},()=>updates++),host);
  const idle=host.textContent;
  render(waterBody({waterCapture:true,waterRunning:true,waterSeconds:20},()=>updates++),host);
  await new Promise(resolve=>requestAnimationFrame(()=>requestAnimationFrame(resolve)));
  const running=host.textContent;
  host.querySelector('md-filled-button').click();
  await new Promise(resolve=>setTimeout(resolve,1100));
  return {idle,running,after:host.textContent,audioStarts,updates};
 });
 assert.match(result.idle,/165 Hz/);
 assert.match(result.idle,/Start 30-second cycle/);
 assert.doesNotMatch(result.idle,/Ready|Playing|30 s/);
 assert.match(result.running,/20 s/);
 assert.match(result.running,/Stop cycle/);
 assert.equal(result.after,result.running);
 assert.equal(result.audioStarts,0);
 assert.equal(result.updates,0);
 console.log('Water Eject idle, running, capture silence and countdown stability passed.');
} finally {await browser.close();await server.close();}
