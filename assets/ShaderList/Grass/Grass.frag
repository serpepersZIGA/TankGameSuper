#version 300 es
#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution; // Разрешение экрана
uniform float u_time; // Время для анимации
uniform sampler2D u_texture; // Базовая текстура (например, земля)
uniform vec3 u_grassColor; // Цвет травы (RGB)
uniform float u_grassDensity; // Плотность травы (0.0–1.0)
uniform float u_grassHeight; // Высота травы (влияет на интенсивность)
uniform float u_windSpeed; // Скорость ветра
uniform float u_windStrength; // Сила ветра

varying vec2 v_texCoord; // UV от вершинного шейдера

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
    vec2 uv = v_texCoord; // UV-координаты
    vec3 color = vec3(0.0);

    // Получение цвета базовой текстуры
    vec3 baseColor = texture2D(u_texture, uv).rgb;

    // Параметры травы
    float grassScale = 20.0; // Масштаб травы (плотность пятен)
    vec2 grassGrid = uv * grassScale;
    vec2 gridId = floor(grassGrid);
    vec2 gridUv = fract(grassGrid);

    // Шум для имитации ветра
    float wind = noise(gridId + vec2(u_time * u_windSpeed, 0.0)) * u_windStrength;
    vec2 distortedUv = uv + vec2(wind * 0.02, 0.0); // Смещение UV для колыхания

    // Шум для формы травы
    float grassShape = noise(gridId + vec2(0.5, 0.3));
    float grass = smoothstep(u_grassDensity - 0.3, u_grassDensity, grassShape);

    // Высота травы (имитация вертикального роста)
    float grassHeightFactor = smoothstep(0.0, u_grassHeight, uv.y);

    // Прозрачность травы
    float grassAlpha = grass * grassHeightFactor * 0.8; // Прозрачность для естественного вида

    // Комбинирование цветов
    vec3 grassFinalColor = u_grassColor * grassAlpha;
    color = mix(baseColor, grassFinalColor, grassAlpha); // Смешивание с базовой текстурой

    // Вывод
    gl_FragColor = vec4(color, 1.0);
}