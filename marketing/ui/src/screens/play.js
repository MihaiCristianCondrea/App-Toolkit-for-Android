import {nativeAdPreview} from '../kit/native-ad.js';
import {animatedNumber} from '../kit/animated-number.js';
import {wheelVisual,wheelItems} from '../kit/decision-wheel.js';
import {html,nothing} from 'lit';
import {previewScreen} from 'material-next-web-components/marketing.js';
import {toolOverlay,shapeBadge} from '../kit/components.js';
import {diceVisual,coinVisual,playChance} from '../kit/chance.js';
import {APP} from '../config/identity.js';
const TOOLS={
  decision_wheel:{id:"decision_wheel",title:"Decision Wheel",subtitle:"Spin to choose an option",icon:"rotate_right",shape:"cookie_12sided",palette:"gold",supportsTile:false},
  random_number: { id: "random_number", title: "Random Number", subtitle: "Pick a number within a range", icon: "casino", shape: "gem", palette: "gold", kindLabel: "Quick tool", supportsTile: false },
  coin_flip: { id: "coin_flip", title: "Coin Flip", subtitle: "Flip heads or tails instantly", icon: "monetization_on", shape: "cookie_12sided", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
  dice_roll: { id: "dice_roll", title: "Dice Roll", subtitle: "Roll a six-sided die", icon: "casino", shape: "cookie_12sided", palette: "gold", kindLabel: "Quick tool", supportsTile: true },
  bell: { id: "bell", title: "Bell", subtitle: "Shake your phone to ring the bell.", icon: "notifications", shape: "cookie_9sided", palette: "gold", supportsTile: false },
};
const BELL_READY = {bellMotionAvailable: true, bellCapture: false, bellShake: false};

export default {
 id:'play',title:'Play & Decide',category:'play',categoryLabel:'Play & Decide',tier:1,palette:'google',description:'A decision wheel, random numbers, dice, coin flips and a shake-to-ring bell.',
 defaults:{searchQuery:'',activeToolId:'none',randomMin:'1',randomMax:'100',randomResult:73,diceResult:5,coinSide:'Heads',wheelText:'Option 1\nOption 2',wheelRotation:0,wheelWinner:'',wheelSpinning:false,...BELL_READY},
 controls: [
   {type: 'presets', label: 'Play & Decide states', options: [
     ['Overview', {activeToolId: 'none', ...BELL_READY}],
     ...Object.values(TOOLS).map(tool => [tool.title + ' sheet', {activeToolId: tool.id, ...BELL_READY}]),
     ['Bell ringing (silent capture)', {activeToolId: 'bell', ...BELL_READY, bellCapture: true}],
     ['Bell without motion', {activeToolId: 'bell', ...BELL_READY, bellMotionAvailable: false}],
   ]},
   {key: 'bellShake', label: 'Simulate shake', type: 'toggle'},
 ],
 render(s){const open=id=>s.set?.({activeToolId:id,...BELL_READY});const activate=id=>e=>{if(e.target===e.currentTarget&&['Enter',' '].includes(e.key)){e.preventDefault();open(id);}};
 const query=(s.searchQuery||'').trim().toLowerCase();
    const chance = (id, value, note, visual) => html`<div class="qt-live qt-chance" data-tool=${id} role="button" tabindex="0" @click=${() => open(id)} @keydown=${activate(id)} aria-label=${TOOLS[id].title}>
      <span class="qt-live__label">${TOOLS[id].title}</span><div class="qt-chance__visual">${id==='random_number'?visual:html`<button class="qt-chance__play" aria-label=${id==='dice_roll'?'Roll die':'Flip coin'} @click=${e=>playChance(e,id,s,s.set)} @keydown=${e=>e.stopPropagation()}>${visual}</button>`}</div><span class="qt-live__value">${value}</span><span class="qt-live__note">${note}</span></div>`;

 const live={
 decision_wheel:()=>html`<button type="button" class="play-wheel-card" data-tool="decision_wheel" @click=${()=>open("decision_wheel")}><span class="play-wheel-copy"><span class="qt-live__label">Decision Wheel</span><strong>${s.wheelWinner||'Let chance choose'}</strong><span class="play-wheel-options">${wheelItems(s).length} choices · Open wheel</span></span>${wheelVisual(wheelItems(s),s.wheelRotation??0)}</button>`,
      random_number: () => chance("random_number", animatedNumber(s.randomResult ?? "?"), `${s.randomMin}-${s.randomMax}`, shapeBadge({shape:"gem",icon:"numbers",palette:"gold",size:88,iconSize:40})),
      dice_roll: () => chance("dice_roll", "Roll a die", "Tap die to roll", diceVisual(s.diceResult, true)),
      coin_flip: () => chance("coin_flip", s.coinSide, "Tap coin to flip", coinVisual(s.coinSide, true)),

 };
 const ids=Object.keys(TOOLS).filter(id=>!query||(TOOLS[id].title+' '+TOOLS[id].subtitle).toLowerCase().includes(query));
 const chanceIds = ids.filter(id => id !== 'bell');
 const bell = TOOLS.bell;
 if (s.bellShake && s.activeToolId !== 'bell' && s.set) {
   queueMicrotask(() => s.set({bellShake: false}));
 }

 return previewScreen(APP, s, {
   tab: 'play',
   search: {placeholder: 'Search tools', value: s.searchQuery || '', onInput: value => s.set?.({searchQuery: value})},
   content: html`<div class="tk-screen-pad">
     ${chanceIds.length ? html`<section class="qt-section">
       <h2 class="qt-section__title"><md-icon class="sc-rounded" aria-hidden="true">casino</md-icon>Chance</h2>
       <div class="play-chance-group">
         ${chanceIds.includes('decision_wheel') ? live.decision_wheel() : nothing}
         <div class="qt-live-row qt-live-row--chance">${chanceIds.filter(id => id !== 'decision_wheel').map(id => live[id]())}</div>
       </div>
     </section>` : nothing}
     ${s.showAds && !query ? nativeAdPreview() : nothing}
     ${ids.includes('bell') ? html`<section class="qt-section">
       <h2 class="qt-section__title"><md-icon class="sc-rounded" aria-hidden="true">toys</md-icon>Toys</h2>
       <button type="button" class="play-bell-card qt-live" data-accent="gold" data-tool="bell" @click=${() => open('bell')}>
         <span class="qt-live__label">${bell.title}</span>
         ${shapeBadge({shape: bell.shape, icon: bell.icon, palette: bell.palette, size: 88, iconSize: 40})}
         <span class="qt-live__note">Shake to ring</span>
       </button>
     </section>` : nothing}
     ${ids.length ? nothing : html`<p role="status">No tools found.</p>`}
   </div>`,
   overlay: toolOverlay({
     tool: s.activeToolId === 'bell' && s.bellMotionAvailable === false
       ? {...bell, subtitle: 'Shake detection is unavailable on this device.'}
       : TOOLS[s.activeToolId],
     state: s, onState: s.set,
     onClose: () => s.set?.({activeToolId: 'none', wheelSpinning: false, ...BELL_READY}),
   }),
 });
 }
};
