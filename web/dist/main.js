"use strict";
var frame = document.getElementById('frame');
var fpsEl = document.getElementById('fps');
var resEl = document.getElementById('res');
// Replace the sampleBase64 with an actual base64 exported from Android run
var sampleBase64 = "data:image/png;base64,REPLACE_WITH_BASE64";
frame.src = sampleBase64;
frame.onload = function () {
    fpsEl.textContent = "FPS: 0 (sample)";
    resEl.textContent = "Resolution: ".concat(frame.naturalWidth, "x").concat(frame.naturalHeight);
};
