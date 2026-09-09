#version 300 es
#ifdef GL_ES
precision mediump float;
#endif

// same solid-fill approach as Rain.frag, just a near-white tint instead of
// blue - every snow particle quad gets painted this flat color
const vec4 u_color = vec4(0.92, 0.94, 0.98, 0.55);
// lets WeatherMainSystem cross-fade snow in/out smoothly across a biome
// boundary instead of switching it fully on/off at some line
uniform float u_alphaScale;

out vec4 fragColor;

void main() {
    fragColor = vec4(u_color.rgb, u_color.a*u_alphaScale);
}
