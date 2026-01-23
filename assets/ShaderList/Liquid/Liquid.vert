#version 300 es
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

out vec4 v_color;
out vec2 v_texCoord;
out vec2 v_worldPos;

void main() {
    v_color = a_color;
    v_texCoord = a_texCoord0;
    v_worldPos = a_position.xy;
    gl_Position = u_projTrans * a_position;
}
