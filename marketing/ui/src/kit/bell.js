import { html, LitElement } from "lit";
import { shapeMaskStyle, shapeUrl } from "./assets.js";
import bellStrikeUrl from "./bell-strike.mp3?url";

const MAX_VOICES = 3;
const AUDIO_LEAD_SECONDS = 0.025;
const IMPACTS = [
  { time: 0.006, angle: 20 },
  { time: 0.10, angle: -23 },
  { time: 0.24, angle: 21 },
  { time: 0.38, angle: -17 },
  { time: 0.54, angle: 15 },
  { time: 0.84, angle: -12 },
];
let strikeBytes;

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

  stop = () => {
    clearTimeout(this.timer);
    this.motion.forEach(animation => animation.cancel());
    this.motion = [];
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
    const initial = getComputedStyle(icon).transform;
    this.motion.forEach(animation => animation.cancel());
    this.motion = [];
    if (matchMedia("(prefers-reduced-motion: reduce)").matches) return;

    const timing = { duration, delay, fill: "backwards" };
    const swing = [
      { offset: 0, transform: initial },
      ...IMPACTS.map(({ time, angle }) => ({
        offset: (time + 0.028) / sampleSeconds,
        transform: `rotate(${angle * direction}deg)`,
        easing: "ease-in-out",
      })),
      { offset: 1.0 / sampleSeconds, transform: `rotate(${5 * direction}deg)`, easing: "ease-out" },
      { offset: 1.17 / sampleSeconds, transform: `rotate(${-2 * direction}deg)`, easing: "ease-out" },
      { offset: 1, transform: "rotate(0deg)" },
    ];
    const animation = icon.animate(swing, timing);
    animation.id = "bell-swing";
    this.motion.push(animation);

    const waves = this.querySelector(".tk-bell__waves");
    const pulses = [{ offset: 0, opacity: 0, transform: "scale(.92)" }];
    IMPACTS.forEach(({ time, angle }, index) => {
      const until = IMPACTS[index + 1]?.time ?? 1.2;
      pulses.push(
        { offset: (time + 0.018) / sampleSeconds, opacity: Math.abs(angle) / 23, transform: "scale(.96)", easing: "ease-out" },
        { offset: (until - 0.013) / sampleSeconds, opacity: 0, transform: "scale(1.04)" },
      );
    });
    pulses.push({ offset: 1, opacity: 0 });
    const waveAnimation = waves.animate(pulses, timing);
    waveAnimation.id = "bell-wave";
    this.motion.push(waveAnimation);
  }

  render() {
    const ringing = this.ringing || this.capture;
    return html`
      <div class="tk-bell" data-ringing=${Boolean(ringing)} data-capture=${Boolean(this.capture)}>
        <div class="tk-bell__stage">
          <div class="tk-bell__visual">
            <div class="tk-bell__face" aria-hidden="true">
              <div class="tk-bell__mask" style=${shapeMaskStyle(shapeUrl("cookie_9sided"))}></div>
              <span class="tk-bell__waves"></span>
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
