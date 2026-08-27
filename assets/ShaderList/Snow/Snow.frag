#version 300 es
#ifdef GL_ES
precision mediump float;
#endif

// same solid-fill approach as Rain.frag, just a near-white tint instead of
// blue - every snow particle quad gets painted this flat color
const vec4 u_color = vec4(0.92, 0.94, 0.98, 0.55);

out vec4 fragColor;

void main() {
    fragColor = u_color;
}
