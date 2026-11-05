#version 300 es
#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution;  // Разрешение экрана (width, height)
const vec2 u_rectSize =vec2(0.35,0.85);
const vec4 u_color = vec4(0.07,0.3,0.5,0.45);
const vec2 u_rectPos = vec2(0.5,0.5);

out vec4 fragColor;

void main() {
    // Нормализуем координаты: от (0,0) в левом-нижнем углу до (1,1) в правом-верхнем
    vec2 uv = gl_FragCoord.xy / u_resolution;

    // Центр прямоугольника в нормализованных координатах
    vec2 rectCenter = u_rectPos / u_resolution;
    vec2 rectHalfSize = u_rectSize / u_resolution * 0.5;

    // SDF для прямоугольника (axis-aligned)
    vec2 d = abs(uv - rectCenter) - rectHalfSize;

    // Расстояние до границы (отрицательное внутри)
    float dist = length(max(d, 0.0)) + min(max(d.x, d.y), 0.0);

    // Граница (anti-aliased)
    //float edge = 0.001;  // Толщина сглаживания
    //float alpha = 1.0 - smoothstep(0.0, edge, dist);

    // Вывод цвета
    //vec3 finalColor = u_color.rgb;
    fragColor = vec4(u_color);
}