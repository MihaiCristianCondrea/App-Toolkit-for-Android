import fs from 'node:fs';
import assert from 'node:assert/strict';
import {fileURLToPath} from 'node:url';

const source=fileURLToPath(new URL('../',import.meta.url));
const timeline=JSON.parse(fs.readFileSync(source+'src/timeline.json'));
const music=JSON.parse(fs.readFileSync(source+'src/music-analysis.json'));
const code=['OpeningJourney','FeatureJourney','ClosingScene','visuals'].map(name=>fs.readFileSync(source+`src/${name}.tsx`,'utf8')).join('\n');
const read=(events,ref)=>ref.split('/').slice(1).reduce((value,key)=>value?.[key],events);
function audit(t){
  assert.deepEqual(t.events.beats.map(b=>b.at),music.onsetsSeconds,'Every onset needs an ordered beat entry');
  const big=[];
  assert.equal(t.events.shots[0].at,0);
  assert.equal(t.events.shots.at(-1).end,t.durationSeconds);
  for(let i=0;i<t.events.shots.length;i++){
    const shot=t.events.shots[i],next=t.events.shots[i+1];
    assert.ok(shot.framing&&shot.transition,'Every shot names its framing and transition');
    assert.ok(shot.at===0||shot.at===t.events.finalAnticipation||music.onsetsSeconds.includes(shot.at),'A shot starts away from its musical cue');
    if(next){
      assert.equal(shot.end,next.at,'Shot list must be contiguous');
      assert.notEqual(shot.composition,next.composition,'Consecutive shots repeat a composition');
    }
  }
  assert.deepEqual(t.events.phrases,t.events.shots.map(s=>s.at),'Each phrase needs a camera shot');
  for(const hit of t.events.beats){
    assert.ok(hit.responses?.length,`No visible event at ${hit.at}`);
    for(const r of hit.responses){
      assert.equal(read(t.events,r.ref),hit.at,`Unresolved event ${r.ref} at ${hit.at}`);
      const root=r.ref.split('/')[1];
      assert.ok(code.includes(`e.${root}`)||code.includes(`events.${root}`),`Unused timeline reference: ${r.ref}`);
      if(r.kind==='nudge'){
        const e=t.events,windows={'intro-stage':[[0,e.logo]],toolbox:[[e.logo,e.launch]],hero:[[e.launch,e.gather],[e.quickStart,e.quickReturnEnd],[e.measure,e.close]],stage:[[e.gather,e.quickStart]],phone:[[e.quickReturnEnd,e.measure],[e.close,e.closingStage]],'closing-stage':[[e.closingStage,t.durationSeconds]]};
        assert.equal(read(t.events,r.ref.replace(/\/at$/,'/target')),r.target,'Accent target differs from its visible cue');
        assert.ok(windows[r.target]?.some(([start,end])=>hit.at>=start&&hit.at<end),`Accent target is absent at ${hit.at}: ${r.target}`);
      }
      if(r.big)big.push({at:hit.at,label:r.label});
      if(r.kind==='camera')assert.ok(t.events.phrases.includes(hit.at),'Camera move outside a phrase boundary');
    }
  }
  big.sort((a,b)=>a.at-b.at);
  for(let i=1;i<big.length;i++)assert.ok(big[i].at-big[i-1].at>=.15-1e-9,`Big gestures within 0.15s: ${big[i-1].label} / ${big[i].label}`);
  return big.length;
}
const count=audit(timeline);
// Regression checks ensure this audit rejects missing responses and colliding gestures.
const missing=structuredClone(timeline);missing.events.beats[1].responses=[];
assert.throws(()=>audit(missing),/No visible event/);
const absent=structuredClone(timeline);absent.events.beats[1].responses[0].target='hero';
assert.throws(()=>audit(absent),/Accent target differs/);
const collision=structuredClone(timeline),hit=collision.events.beats.find(b=>b.responses.some(r=>r.big));
hit.responses.push({...hit.responses.find(r=>r.big),label:'collision fixture'});
assert.throws(()=>audit(collision),/Big gestures within/);
for(const hit of timeline.events.beats)console.log(`${hit.at.toFixed(2)}  ${hit.responses.map(r=>`${r.label} (${r.ref})`).join('; ')}`);
console.log(`Beat audit passed: ${timeline.events.beats.length} onsets, ${count} big gestures, no gaps or 0.15s collisions; camera moves only on phrase boundaries.`);
