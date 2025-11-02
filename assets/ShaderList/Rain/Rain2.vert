// vertex.glsl
#version 300 es
precision mediump float;

layout(location = 0) in vec2 a_position;

uniform vec2 u_resolution;  // Разрешение экрана (например, 800, 600)

out vec2 v_uv;  // Передаём нормализованные координаты во фрагментный шейдер

void main() {
    // Преобразуем позицию из пикселей в clip space (-1..1)
    vec2 clipSpace = (a_position / u_resolution) * 2.0 - 1.0;
    gl_Position = vec4(clipSpace, 0.0, 1.0);

    // Передаём UV в диапазоне [0,1]
    v_uv = a_position / u_resolution;
}