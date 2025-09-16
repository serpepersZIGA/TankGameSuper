attribute vec2 a_position;
attribute vec2 a_texCoord;
attribute vec3 a_particleData; // x: lifetime, y: size, z: type

uniform mat4 u_projection;
uniform mat4 u_view;
uniform float u_time;

varying vec2 v_texCoord;
varying float v_lifetime;
varying float v_size;
varying float v_type;

void main() {
    v_texCoord = a_texCoord;
    v_lifetime = a_particleData.x;
    v_size = a_particleData.y;
    v_type = a_particleData.z;

    // Анимация частиц
    vec2 position = a_position;

    // Движение брызг в зависимости от типа
    if (v_type < 0.5) {
        // Основные брызги - расширяются
        float progress = mod(u_time + v_lifetime, 1.0);
        position += normalize(a_position) * progress * v_size * 0.5;
    }

    gl_Position = u_projection * u_view * vec4(position, 0.0, 1.0);
    gl_PointSize = v_size * (1.0 - abs(2.0 * mod(u_time + v_lifetime, 1.0) - 1.0)) * 20.0;
}