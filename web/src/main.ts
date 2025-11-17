const frame = document.getElementById('frame') as HTMLImageElement;
const fpsEl = document.getElementById('fps')!;
const resEl = document.getElementById('res')!;

// Replace the sampleBase64 with an actual base64 exported from Android run
const sampleBase64 = "data:image/png;base64,REPLACE_WITH_BASE64";

frame.src = sampleBase64;

frame.onload = () => {
  fpsEl.textContent = "FPS: 0 (sample)"
  resEl.textContent = `Resolution: ${frame.naturalWidth}x${frame.naturalHeight}`
}
