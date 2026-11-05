#version 300 es
#ifdef GL_ES
    precision mediump float;
#endif

in vec4 v_color;
in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    fragColor = v_color * texColor;
}