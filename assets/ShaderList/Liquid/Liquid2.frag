#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;
varying vec2 v_pos;

uniform float u_time;
uniform vec2 u_resolution;
//uniform vec2 u_blobCenter; // центр кляксы в пикселях
uniform float u_blobRadius; // радиус кляксы
uniform vec3 u_waterColor; // цвет воды (например, 0.0, 0.5, 1.0)

// Волновые параметры
const float waveSpeed = 3.0;
const float waveFreq = 8.0;
const float waveAmp = 0.02;

void main() {
    vec2 uv = v_texCoord;
    vec2 fragCoord = uv * u_resolution;

    // Расстояние от центра кляксы
    float dist = length(fragCoord - vec2(0.5,0.5));
    float normalizedDist = dist / u_blobRadius;

    // Обрезка за пределами кляксы
    if (normalizedDist > 1.0) {
        discard;
    }

    // Волновое искажение (рябь)
    float wavePhase = u_time * waveSpeed + dist * waveFreq;
    vec2 waveOffset = vec2(
    sin(wavePhase) * waveAmp,
    cos(wavePhase * 1.3) * waveAmp
    ) * (1.0 - normalizedDist); // сильнее в центре

    vec2 distortedUV = uv + waveOffset;

    // Мягкий край кляксы (антиалиасинг)
    float edge = 1.0 - smoothstep(0.8, 1.0, normalizedDist);

    // Внутренние блики (имитация преломления)
    float highlight = pow(1.0 - normalizedDist, 3.0) * 0.6;
    highlight += sin(wavePhase * 2.0) * 0.1 * (1.0 - normalizedDist);

    // Цвет
    vec3 color = u_waterColor;
    color += vec3(highlight);

    // Прозрачность
    float alpha = edge * 0.7; // полупрозрачная

    gl_FragColor = vec4(color, alpha);
}
