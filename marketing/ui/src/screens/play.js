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
};
export default {
 id:'play',title:'Play & Decide',category:'play',categoryLabel:'Play & Decide',tier:1,palette:'google',description:'A decision wheel, random numbers, dice and coin flips for everyday choices.',
 defaults:{searchQuery:'',activeToolId:'none',randomMin:'1',randomMax:'100',randomResult:73,diceResult:5,coinSide:'Heads',wheelText:'Option 1\nOption 2',wheelRotation:0,wheelWinner:'',wheelSpinning:false},
 controls:[{type:'presets',label:'Play & Decide states',options:[['Overview',{activeToolId:'none'}],...Object.values(TOOLS).map(t=>[t.title+' sheet',{activeToolId:t.id}])]}],
 render(s){const open=id=>s.set?.({activeToolId:id});const activate=id=>e=>{if(e.target===e.currentTarget&&['Enter',' '].includes(e.key)){e.preventDefault();open(id);}};
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
 return previewScreen(APP,s,{tab:'play',search:{placeholder:'Search tools',value:s.searchQuery||'',onInput:v=>s.set?.({searchQuery:v})},content:html`<div class="tk-screen-pad"><section class="qt-section"><h2 class="qt-section__title"><md-icon class="sc-rounded">casino</md-icon>Chance</h2><div class="play-chance-group">${ids.includes("decision_wheel")?live.decision_wheel():nothing}<div class="qt-live-row qt-live-row--chance">${ids.filter(id=>id!=="decision_wheel").map(id=>live[id]())}</div></div>${!ids.length?html`<p role="status">No tools found.</p>`:nothing}</section>${s.showAds && !query && ids.length ? nativeAdPreview() : nothing}</div>`,overlay:toolOverlay({tool:TOOLS[s.activeToolId],state:s,onState:s.set,onClose:()=>s.set?.({activeToolId:'none',wheelSpinning:false})})});
 }
};
