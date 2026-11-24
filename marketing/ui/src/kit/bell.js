import { html, LitElement } from "lit";
import { shapeMaskStyle, shapeUrl } from "./assets.js";
import bellStrikeUrl from "./bell-strike.mp3?url";

const MAX_VOICES = 3;
const AUDIO_LEAD_SECONDS = 0.025;
const MAX_IMPACT_ANGLE = 23;
const COOKIE_PULSE_SCALE = 0.032;
const ATTACK_EASING = "cubic-bezier(0.2, 0.8, 0.2, 1)";
const DECAY_EASING = "cubic-bezier(0.22, 1, 0.36, 1)";
const IMPACTS = [
  { time: 0.006, angle: 20 },
  { time: 0.10, angle: -23 },
  { time: 0.24, angle: 21 },
  { time: 0.38, angle: -17 },
  { time: 0.54, angle: 15 },
  { time: 0.84, angle: -12 },
];
const WAVE_RINGS = {
  inner: {
    stagger: 0,
    attack: 0.026,
    duration: 0.19,
    finalDuration: 0.21,
    peakOpacity: 0.8,
    startScale: 0.91,
    peakScale: 0.967,
    endScale: 1.07,
    startStroke: 3.4,
    peakStroke: 3.2,
    endStroke: 2.0,
  },
  outer: {
    stagger: 0.048,
    attack: 0.034,
    duration: 0.22,
    finalDuration: 0.294,
    peakOpacity: 0.48,
    startScale: 0.93,
    peakScale: 0.993,
    endScale: 1.09,
    startStroke: 2.6,
    peakStroke: 2.4,
    endStroke: 1.5,
  },
};
const INNER_LEFT_ARC = "M 42.56 121.55 A 91 60 0 0 1 42.56 54.45";
const INNER_RIGHT_ARC = "M 193.44 54.45 A 91 60 0 0 1 193.44 121.55";
const OUTER_LEFT_ARC = "M 38.33 133.64 A 104 71 0 0 1 38.33 42.36";
const OUTER_RIGHT_ARC = "M 197.67 42.36 A 104 71 0 0 1 197.67 133.64";
let strikeBytes;

function clampOffset(offset) {
  return Math.min(Math.max(offset, 0), 0.999);
}

function buildWaveKeyframes(sampleSeconds, spec, slot) {
  const frames = [
    { offset: 0, opacity: 0, transform: `scale(${spec.startScale})`, strokeWidth: `${spec.startStroke}px` },
  ];
  IMPACTS.forEach(({ time, angle }, index) => {
    if (index % 2 !== slot) return;
    const isFinal = index === IMPACTS.length - 1;
    const start = time + spec.stagger;
    const peak = start + spec.attack;
    const end = start + (isFinal ? spec.finalDuration : spec.duration);
    const energy = Math.abs(angle) / MAX_IMPACT_ANGLE;
    frames.push(
      {
        offset: clampOffset(start / sampleSeconds),
        opacity: 0,
        transform: `scale(${spec.startScale})`,
        strokeWidth: `${spec.startStroke}px`,
        easing: ATTACK_EASING,
      },
      {
        offset: clampOffset(peak / sampleSeconds),
        opacity: Number((spec.peakOpacity * energy).toFixed(3)),
        transform: `scale(${spec.peakScale})`,
        strokeWidth: `${spec.peakStroke}px`,
        easing: DECAY_EASING,
      },
      {
        offset: clampOffset(end / sampleSeconds),
        opacity: 0,
        transform: `scale(${spec.endScale})`,
        strokeWidth: `${spec.endStroke}px`,
      },
    );
  });
  frames.push({
    offset: 1,
    opacity: 0,
    transform: `scale(${spec.endScale})`,
    strokeWidth: `${spec.endStroke}px`,
  });
  return frames;
}

function loadStrike() {
  strikeBytes ??= fetch(bellStrikeUrl)
    .then(response => {
      if (!response.ok) throw new Error("Bell audio could not be loaded.");
      return response.arrayBuffer();
    })
    .catch(error => {
      strikeBytes = undefined;
      throw error;
    });
  return strikeBytes;
}

class Bell extends LitElement {
  static properties = {
    motionAvailable: { type: Boolean },
    capture: { type: Boolean },
    shake: { type: Boolean },
    onShakeHandled: { attribute: false },
    ringing: { state: true },
    audioUnavailable: { state: true },
  };

  voices = [];
  motion = [];
  strike = 0;

  constructor() {
    super();
    this.ringing = false;
    this.audioUnavailable = false;
  }

  createRenderRoot() {
    return this;
  }

  connectedCallback() {
    super.connectedCallback();
    document.addEventListener("visibilitychange", this.stopWhenHidden);
    window.addEventListener("pagehide", this.stop);
  }

  disconnectedCallback() {
    document.removeEventListener("visibilitychange", this.stopWhenHidden);
    window.removeEventListener("pagehide", this.stop);
    this.stop();
    super.disconnectedCallback();
  }

  updated(changed) {
    if (changed.has("capture") && this.capture) this.stop();
    if ((changed.has("capture") || changed.has("onShakeHandled")) && !this.capture && this.onShakeHandled) {
      void loadStrike().catch(() => {});
    }
    if (changed.has("shake") && this.shake && this.onShakeHandled) {
      if (this.motionAvailable) this.ring();
      this.onShakeHandled();
    }
  }

  stopWhenHidden = () => {
    if (document.hidden) this.stop();
  };

  clearWaveDirection() {
    this.querySelectorAll(".tk-bell__wave").forEach(wave => {
      delete wave.dataset.lead;
    });
  }

  stop = () => {
    clearTimeout(this.timer);
    this.motion.forEach(animation => animation.cancel());
    this.motion = [];
    this.clearWaveDirection();
    const audio = this.audio;
    this.audio = undefined;
    this.sample = undefined;
    this.voices.length = 0;
    if (audio && audio.state !== "closed") void audio.close().catch(() => {});
    this.ringing = false;
  };

  ring = async () => {
    if (this.capture || document.hidden) return;
    this.audioUnavailable = false;

    let audio = this.audio;
    try {
      if (!audio) {
        const Audio = window.AudioContext || window.webkitAudioContext;
        audio = new Audio();
        this.audio = audio;
        this.sample = loadStrike().then(bytes => audio.decodeAudioData(bytes.slice(0)));
      }
      const [buffer] = await Promise.all([this.sample, audio.resume()]);
      if (this.audio !== audio || this.capture || document.hidden) return;

      const start = audio.currentTime + AUDIO_LEAD_SECONDS;
      const nextStrike = this.strike + 1;
      const source = audio.createBufferSource();
      const gain = audio.createGain();
      source.buffer = buffer;
      source.detune.value = nextStrike % 2 ? -9 : 9;
      gain.gain.value = nextStrike % 2 ? 0.32 : 0.36;
      source.connect(gain);
      gain.connect(audio.destination);

      const voice = { source, gain };
      this.voices.push(voice);
      source.onended = () => {
        const index = this.voices.indexOf(voice);
        if (index !== -1) this.voices.splice(index, 1);
        source.disconnect();
        gain.disconnect();
      };
      if (this.voices.length > MAX_VOICES) {
        const oldest = this.voices.shift();
        oldest.gain.gain.setTargetAtTime(0, start, 0.004);
        oldest.source.stop(start + 0.016);
      }

      const timestamp = audio.getOutputTimestamp?.();
      const delay = timestamp?.performanceTime
        ? Math.max(0, timestamp.performanceTime + (start - timestamp.contextTime) * 1000 - performance.now())
        : AUDIO_LEAD_SECONDS * 1000;
      const duration = buffer.duration * 1000 / 2 ** (source.detune.value / 1200);
      source.start(start);
      this.animateStrike(buffer.duration, duration, delay, nextStrike % 2 ? 1 : -1);
      this.strike = nextStrike;
      this.ringing = true;
      clearTimeout(this.timer);
      this.timer = setTimeout(() => { this.ringing = false; }, duration + delay);
    } catch {
      if (this.audio === audio) {
        this.stop();
        this.audioUnavailable = true;
      }
    }
  };

  animateStrike(sampleSeconds, duration, delay, direction) {
    const icon = this.querySelector(".tk-bell__face md-icon");
    const mask = this.querySelector(".tk-bell__mask");
    const initial = getComputedStyle(icon).transform;
    const initialMask = getComputedStyle(mask).transform;
    this.motion.forEach(animation => animation.cancel());
    this.motion = [];
    if (matchMedia("(prefers-reduced-motion: reduce)").matches) {
      this.clearWaveDirection();
      return;
    }

    const timing = { duration, delay, fill: "backwards" };
    const swing = [
      { offset: 0, transform: initial },
      ...IMPACTS.map(({ time, angle }) => ({
        offset: clampOffset((time + 0.028) / sampleSeconds),
        transform: `rotate(${angle * direction}deg)`,
        easing: "ease-in-out",
      })),
      { offset: clampOffset(1.0 / sampleSeconds), transform: `rotate(${5 * direction}deg)`, easing: "ease-out" },
      { offset: clampOffset(1.17 / sampleSeconds), transform: `rotate(${-2 * direction}deg)`, easing: "ease-out" },
      { offset: 1, transform: "rotate(0deg)" },
    ];
    const animation = icon.animate(swing, timing);
    animation.id = "bell-swing";
    this.motion.push(animation);

    const maskPulse = [
      {
        offset: 0,
        transform: initialMask === "none" ? "scale(1)" : initialMask,
        easing: ATTACK_EASING,
      },
    ];
    IMPACTS.forEach(({ time, angle }, index) => {
      const nextTime = IMPACTS[index + 1]?.time ?? 1.15;
      const energy = Math.abs(angle) / MAX_IMPACT_ANGLE;
      const peak = (1 + COOKIE_PULSE_SCALE * energy).toFixed(4);
      const settle = Math.min(time + 0.135, nextTime - 0.008);
      if (index > 0) {
        maskPulse.push({
          offset: clampOffset(time / sampleSeconds),
          transform: "scale(1)",
          easing: ATTACK_EASING,
        });
      }
      maskPulse.push(
        { offset: clampOffset((time + 0.026) / sampleSeconds), transform: `scale(${peak})`, easing: DECAY_EASING },
        { offset: clampOffset(settle / sampleSeconds), transform: "scale(1)" },
      );
    });
    maskPulse.push({ offset: 1, transform: "scale(1)" });
    const maskAnimation = mask.animate(maskPulse, timing);
    maskAnimation.id = "bell-mask";
    this.motion.push(maskAnimation);

    this.querySelectorAll(".tk-bell__wave").forEach(wave => {
      const ring = wave.dataset.ring;
      const slot = Number(wave.dataset.slot);
      wave.dataset.lead = (slot === 0) === (direction > 0) ? "left" : "right";
      const waveAnimation = wave.animate(
        buildWaveKeyframes(sampleSeconds, WAVE_RINGS[ring], slot),
        timing,
      );
      waveAnimation.id = `bell-wave-${ring}-${slot}`;
      this.motion.push(waveAnimation);
    });
  }

  render() {
    const ringing = this.ringing || this.capture;
    return html`
      <div class="tk-bell" data-ringing=${Boolean(ringing)} data-capture=${Boolean(this.capture)}>
        <div class="tk-bell__stage">
          <div class="tk-bell__visual">
            <div class="tk-bell__face" aria-hidden="true">
              <div class="tk-bell__mask" style=${shapeMaskStyle(shapeUrl("cookie_9sided"))}></div>
              <svg class="tk-bell__waves" viewBox="0 0 236 176" fill="none" aria-hidden="true">
                <g class="tk-bell__wave tk-bell__wave--outer" data-ring="outer" data-slot="0">
                  <path class="tk-bell__arc tk-bell__arc--left" d=${OUTER_LEFT_ARC} />
                  <path class="tk-bell__arc tk-bell__arc--right" d=${OUTER_RIGHT_ARC} />
                </g>
                <g class="tk-bell__wave tk-bell__wave--outer" data-ring="outer" data-slot="1">
                  <path class="tk-bell__arc tk-bell__arc--left" d=${OUTER_LEFT_ARC} />
                  <path class="tk-bell__arc tk-bell__arc--right" d=${OUTER_RIGHT_ARC} />
                </g>
                <g class="tk-bell__wave tk-bell__wave--inner" data-ring="inner" data-slot="0">
                  <path class="tk-bell__arc tk-bell__arc--left" d=${INNER_LEFT_ARC} />
                  <path class="tk-bell__arc tk-bell__arc--right" d=${INNER_RIGHT_ARC} />
                </g>
                <g class="tk-bell__wave tk-bell__wave--inner" data-ring="inner" data-slot="1">
                  <path class="tk-bell__arc tk-bell__arc--left" d=${INNER_LEFT_ARC} />
                  <path class="tk-bell__arc tk-bell__arc--right" d=${INNER_RIGHT_ARC} />
                </g>
              </svg>
              <md-icon class="sc-filled">notifications</md-icon>
            </div>
            <p class="label-large tk-bell__status" data-visible=${Boolean(this.audioUnavailable)} role="status">${this.audioUnavailable
              ? "Audio is unavailable."
              : ringing ? "Ringing" : ""}</p>
          </div>
          <md-filled-button @click=${this.ring}>Ring bell</md-filled-button>
        </div>
      </div>
    `;
  }
}

customElements.define("tk-bell", Bell);
