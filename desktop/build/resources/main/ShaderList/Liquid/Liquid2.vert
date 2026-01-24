in vec4 a_position;
in vec2 a_texCoord;

uniform mat4 u_projTrans;

out vec2 v_texCoord;
out vec2 v_pos;

void main() {
    v_texCoord = a_texCoord;
    v_pos = a_position.xy;
    gl_Position = u_projTrans * a_position;
}
