import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import {fileURLToPath} from 'node:url';
import {createHash} from 'node:crypto';
import {execFileSync} from 'node:child_process';

const source=fileURLToPath(new URL('../',import.meta.url));
const timeline=JSON.parse(fs.readFileSync(path.join(source,'src/timeline.json')));
const media=JSON.parse(fs.readFileSync(path.join(source,'src/generated/media.json')));
assert.equal(timeline.scenes.length,8);
assert.equal(timeline.scenes[0].start,0);
for(let i=1;i<timeline.scenes.length;i++)assert.equal(timeline.scenes[i].start,timeline.scenes[i-1].end);
assert.equal(timeline.scenes.at(-1).end,media.durationSeconds);
const hash=file=>createHash('sha256').update(fs.readFileSync(file)).digest('hex');
assert.equal(hash(path.join(source,'../soundtrack.mp3')),media.soundtrackSha256);
assert.equal(hash(path.join(source,'public/audio/soundtrack.mp3')),media.soundtrackSha256);
const device=JSON.parse(fs.readFileSync(path.join(source,'src/generated/device.json')));
assert.equal(device.display.width,1280);assert.equal(device.display.height,2856);
assert.equal(device.display.x,60);assert.equal(device.display.y,61);
const video=process.argv[2];
if(video){
  const result=JSON.parse(execFileSync(process.env.FFPROBE_PATH||'ffprobe',['-v','error','-show_entries','format=duration:stream=codec_type,width,height,r_frame_rate,sample_rate,channels','-of','json',video],{encoding:'utf8'}));
  assert.ok(Number(result.format.duration)>=media.durationSeconds-.01,'Full soundtrack duration must be covered');
  assert.ok(Number(result.format.duration)<=media.durationSeconds+.1,'Unexpected empty end');
  assert.ok(result.streams.some(stream=>stream.codec_type==='audio'),'Soundtrack missing');
  assert.ok(result.streams.some(stream=>stream.codec_type==='video'&&stream.r_frame_rate==='90/1'),'Expected 90 fps');
}
console.log('Trailer checks passed: eight contiguous scenes, unchanged full soundtrack, original device aperture'+(video?', encoded duration and audio.':'.'));
