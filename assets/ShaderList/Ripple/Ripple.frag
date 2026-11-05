#version 300 es
#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution; // Разрешение экрана
uniform float u_time; // Время для анимации

out vec4 fragColor;

uniform sampler2D u_texture; // Текстура для наложения

in vec2 v_texCoord;
// UV от вершинного шейдера

float random(vec2 st) {
    return fract(sin(dot(st, vec2(12.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(random(i + vec2(0.0, 0.0)), random(i + vec2(1.0, 0.0)), u.x),
    mix(random(i + vec2(0.0, 1.0)), random(i + vec2(1.0, 1.0)), u.x), u.y);
}

void main() {
    vec2 uv = v_texCoord;// Используем UV-координаты
    vec4 color = vec4(0.0);
    vec4 colorWave = vec4(0.0, 0.2, 0.5, 0.35);

    // Параметры волны
    float waveSpeed = 2.0;// Скорость анимации волны
    float waveSize = 0.2;// Максимальный радиус волны
    float waveStrength = 1.2;// Интенсивность волны

    // Получение цвета текстуры
    vec4 textureColor = vec4(0.0, 0.0, 0.0, 0.0);

    // Вычисление текущего цикла волны
    float waveAge = u_time * waveSpeed;// Возраст текущей волны

    // Случайная позиция волны для текущего цикла
    //float cycleId = floor(u_time * waveSpeed / waveDuration);// Уникальный ID цикла
    //vec2 seed = vec2(cycleId * 0.1, cycleId * 0.2);
    vec2 waveCenter = vec2(
    0.5,
    0.5
    );

    // Создание волны
    float wave = 0.0;
    float dist = length(uv - waveCenter);
    wave = sin(dist * 50.0 - waveAge * 10.0) * waveStrength;
    wave *= smoothstep(waveSize, 0.0, dist);// Ограничение радиуса
    wave *= smoothstep(0.0, 0.2, waveAge);// Плавное появление/исчезновение

    // Комбинирование текстуры с волной
    color = textureColor + wave*colorWave;// Добавляем волну к текстуре

    // Вывод
    fragColor = color;
}