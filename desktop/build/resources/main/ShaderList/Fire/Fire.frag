#version 300 es
precision highp float;

varying vec2 v_texCoord;

uniform float u_time;  // Время для анимации

// Простая функция шума
float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float random(vec2 st) {
    return fract(sin(dot(st, vec2(12.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);  // Smoothstep
    return mix(mix(hash(i + vec2(0.0, 0.0)), hash(i + vec2(1.0, 0.0)), u.x),
    mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), u.x), u.y);
}

// FBM с большим числом октав для детализации
float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    float frequency = 1.0;
    for (int i = 0; i < 8; ++i) {  // Увеличено до 8 октав
        value += amplitude * noise(p * frequency);;
        amplitude *= 0.5;
        frequency *= 2.01;  // Лёгкое смещение для хаотичности
    }
    return value;
}

// Шум для частиц
float particleNoise(vec2 p) {
    return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec2 st = v_texCoord;  // UV координаты
    vec2 center = vec2(0.5, 0.5);  // Центр пламени
    float dist = length(st - center);  // Радиальное расстояние от центра

    // Анимация основного огня
    float time = u_time *12.0;  // Ускоряем для живости
    //float s = sin(abs(u_time))/12.0;
    vec2 noiseUV = st * 5.0+ time;  // Масштаб шума
    //noiseUV += normalize((center)) + time;  // Движение наружу
    float n = fbm(noiseUV);

    // Интенсивность огня с радиальным градиентом
    float intensity = n * (1.0 - dist * 1.8) + 0.4;  // Мягче затухание

    // Цвета огня: добавляем синий у основания
    vec3 fireCol;
    if (intensity < 0.3) {
        fireCol = vec3(0.0, 0.0, 0.2);  // Синий у основания
    } else if (intensity < 0.5) {
        fireCol = mix(vec3(0.0, 0.0, 0.2), vec3(1.0, 0.3, 0.0), (intensity - 0.3) / 0.2);  // Переход к оранжевому
    } else if (intensity < 0.7) {
        fireCol = mix(vec3(1.0, 0.3, 0.0), vec3(1.0, 0.6, 0.0), (intensity - 0.5) / 0.2);  // Оранжевый
    } else {
        fireCol = mix(vec3(1.0, 0.6, 0.0), vec3(1.0, 1.0, 0.5), (intensity - 0.7) / 0.3);  // Жёлтый/белый
    }

    // Альфа для основного огня
    float alpha = clamp(intensity, 0.0, 1.0) * (1.0 - dist * 1.8);

    // Частицы (искры)
    vec2 particleUV = center;  // Мелкий масштаб для частиц
    //particleUV.y*=s;
    // Быстрое движение вверх
    float particle = particleNoise(particleUV);
    float particleIntensity = smoothstep(0.99, 1.0, particle) * (1.0 - dist);  // Искры только в центре
    vec3 particleCol = vec3(1.0, 0.9, 0.6) * particleIntensity;  // Яркие жёлтые искры
//    float particleAlpha = particleIntensity;

    // Комбинируем огонь и частицы
    vec3 finalCol = mix(fireCol, particleCol, particleIntensity);
    alpha = clamp(alpha + particleIntensity, 0.0, 1.0);

    gl_FragColor = vec4(finalCol, alpha*1.9);
}