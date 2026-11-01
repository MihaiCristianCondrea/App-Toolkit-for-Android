"""Detect the soundtrack's onsets so every shot can start on a note.

Usage: analyze.py <soundtrack> <output.json>

The track is decoded with FFmpeg to 8 kHz mono. A spectral flux (half-wave rectified
change in log magnitude per 20 ms hop, weighted to the bass (under 200 Hz, a little up to 800 Hz) and timed at the window centre) is peak-picked
against a moving median, with at least 0.2 s between onsets. The BPM is an estimate from
the flux autocorrelation, not a claim about the song.
"""
import json, subprocess, sys
import numpy as np

source, output = sys.argv[1], sys.argv[2]
RATE, HOP, WINDOW = 8000, 160, 1024
raw = subprocess.run(['ffmpeg', '-v', 'error', '-i', source, '-ac', '1', '-ar', str(RATE), '-f', 'f32le', '-'],
                     check=True, capture_output=True).stdout
audio = np.frombuffer(raw, dtype=np.float32)
duration = round(len(audio) / RATE, 2)

frames = 1 + max(0, len(audio) - WINDOW) // HOP
window = np.hanning(WINDOW)
spectrum = np.abs(np.fft.rfft(np.stack([audio[i * HOP:i * HOP + WINDOW] * window for i in range(frames)]), axis=1))
freqs = np.fft.rfftfreq(WINDOW, 1 / RATE)
weight = np.where(freqs < 200, 1.0, np.where(freqs < 800, 0.3, 0.0))
log = np.log1p(100 * spectrum)
flux = np.concatenate([[0], (np.maximum(0, np.diff(log, axis=0)) * weight).sum(axis=1)])
flux /= flux.max() or 1

span = int(0.5 / (HOP / RATE))
onsets, last = [], -1.0
for i in range(1, len(flux) - 1):
    lo, hi = max(0, i - span), min(len(flux), i + span)
    threshold = np.median(flux[lo:hi]) + 0.08
    t = (i * HOP + WINDOW / 2) / RATE
    if flux[i] > threshold and flux[i] >= flux[i - 1] and flux[i] >= flux[i + 1] and t - last >= 0.2:
        onsets.append(round(t, 2))
        last = t

centered = flux - flux.mean()
ac = np.correlate(centered, centered, 'full')[len(centered) - 1:]
lags = np.arange(len(ac)) * HOP / RATE
band = (lags > 60 / 180) & (lags < 60 / 70)
bpm = round(60 / lags[band][np.argmax(ac[band])], 2) if band.any() else None

json.dump({'durationSeconds': duration, 'sampleRate': RATE, 'analysisHopSeconds': HOP / RATE,
           'estimatedBpm': bpm, 'onsetsSeconds': onsets}, open(output, 'w'), indent=2)
print(f'{len(onsets)} onsets over {duration}s, about {bpm} BPM -> {output}')
