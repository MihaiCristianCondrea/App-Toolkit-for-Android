import {html} from 'lit';
import {ref} from 'lit/directives/ref.js';
const runs=new WeakMap();
function stop(root,set,complete=false){const run=runs.get(root);if(!run)return;clearInterval(run.timer);run.observer.disconnect();document.removeEventListener('visibilitychange',run.hidden);window.removeEventListener('pagehide',run.exit);try{run.osc.stop();}catch{}void run.audio.close().catch(()=>{});runs.delete(root);if(root.isConnected)set?.({waterRunning:false,waterSeconds:complete?0:run.seconds});}
function start(e,set){const root=e.currentTarget.closest('.water-eject');if(runs.has(root)){stop(root,set);return;}let audio;
 try{const Audio=window.AudioContext||window.webkitAudioContext;audio=new Audio();const osc=audio.createOscillator(),gain=audio.createGain();osc.type='sine';osc.frequency.value=165;gain.gain.value=0.12;osc.connect(gain);gain.connect(audio.destination);const run={audio,osc,seconds:30};runs.set(root,run);run.hidden=()=>{if(document.hidden||!root.isConnected)stop(root,set);};run.observer=new MutationObserver(()=>{if(!root.isConnected)stop(root,set);});run.observer.observe(document,{childList:true,subtree:true});document.addEventListener('visibilitychange',run.hidden);run.exit=()=>stop(root,set);window.addEventListener('pagehide',run.exit);osc.start();set?.({waterRunning:true,waterSeconds:30,waterError:''});void audio.resume().catch(()=>{stop(root,set);set?.({waterError:'Audio could not start. Try again.'});});const end=performance.now()+30000;run.timer=setInterval(()=>{const seconds=Math.max(0,Math.ceil((end-performance.now())/1000));if(seconds===run.seconds)return;run.seconds=seconds;if(!run.seconds)stop(root,set,true);else set?.({waterSeconds:run.seconds});},200);
 }catch{if(audio)void audio.close().catch(()=>{});set?.({waterRunning:false,waterError:'Audio is unavailable in this browser.'});}}
// waterCapture freezes a supplied state without starting audio or a timer.
export function waterBody(s,set){return html`<div class="water-eject" ${ref(el=>{if(el&&set&&s.waterRunning&&!s.waterCapture)requestAnimationFrame(()=>{if(el.isConnected&&!runs.has(el))set({waterRunning:false,waterSeconds:30});});})} data-running=${Boolean(s.waterRunning)}>
<svg class="water-visual" width="200" height="200" viewBox="0 0 200 200" aria-hidden="true">
<circle cx="100" cy="100" r="60" fill="none" stroke="var(--md-sys-color-primary)" stroke-opacity="${s.waterRunning?.4:.12}" stroke-width="2"/>
<circle cx="100" cy="100" r="75" fill="none" stroke="var(--md-sys-color-primary)" stroke-opacity="${s.waterRunning?.2:.12}" stroke-width="2"/>
<circle cx="100" cy="100" r="60" fill="var(--md-sys-color-primary-container)"/>
<foreignObject x="68" y="68" width="64" height="64"><md-icon style="--md-icon-size:64px;color:var(--md-sys-color-on-primary-container)">water_drop</md-icon></foreignObject></svg>
<div class="water-readout"><strong>${s.waterRunning?s.waterSeconds+' s':'165 Hz'}</strong></div>
<p>Point the speaker downwards. Start at a comfortable volume.</p>
<md-filled-button @click=${e=>!s.waterCapture&&set&&start(e,set)}>${s.waterRunning?'Stop cycle':'Start 30-second cycle'}</md-filled-button>
${s.waterError?html`<p role="alert">${s.waterError}</p>`:''}
<p class="water-note">May help dislodge droplets. Does not repair water damage or make your phone waterproof.</p>
</div>`;}
