#version 300 es
#ifdef GL_ES
precision mediump float;
#endif
precision highp float;

in vec2 v_texCoord;
in vec4 v_color;

out vec4 fragColor;

uniform float u_time;  // Время для анимации
uniform sampler2D u_texture;
uniform float lightTotal;

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
    float dist = length(st - center);
    float distInvert = 1.0-dist;// Радиальное расстояние от центра

    // Анимация основного огня
    float time = u_time *2.0;  // Ускоряем для живости
    //float s = sin(abs(u_time))/12.0;
    vec2 noiseUV = st * 5.0+ time;  // Масштаб шума
    //noiseUV += normalize((center)) + time;  // Движение наружу
    float n = fbm(noiseUV);

    // Интенсивность огня с радиальным градиентом
    float intensity = n * distInvert + 0.35;  // Мягче затухание

    // Цвета огня: добавляем синий у основания
    vec3 fireCol;
    float alpha;
    if(dist < 0.5){

        if (intensity < 0.3) {
            fireCol = vec3(0.0, 0.2, 0.0);// Синий у основания
        } else if (intensity < 0.5) {
            fireCol = mix(vec3(0.5, 0.2, 0.2), vec3(0.8, 0.3, 0.2), (intensity - 0.3) *4.0);// Переход к оранжевому
        } else if (intensity < 0.74) {
            fireCol = mix(vec3(0.3, 0.1, 0.1), vec3(0.9, 0.3, 0.1), (intensity - 0.5)*5.0);// Оранжевый
        } else {
            fireCol = mix(vec3(0.7, 0.2, 0.3), vec3(0.6, 0.2, 0.2), (intensity - 0.7) *3.3);// Жёлтый/белый
        }
        //alpha = clamp(intensity, 0.0, 1.0) * (1.0 - dist * 1.8);
    }
    else{
        //gl_FragColor = vec4(0,0,0,0);
        return;
    }

    // Альфа для основного огня
    //alpha = clamp(intensity, 0.0, 1.0) * (1.0 - dist * 1.8);

    // Частицы (искры)
    vec2 particleUV = center;  // Мелкий масштаб для частиц
    //particleUV.y*=s;
    // Быстрое движение вверх
    float particle = particleNoise(particleUV);
    float particleIntensity = smoothstep(0.5, 1.0, particle) * distInvert;  // Искры только в центре
    vec3 particleCol = vec3(1.0, 0.9, 0.6) * particleIntensity;  // Яркие жёлтые искры
    //    float particleAlpha = particleIntensity;
    //float col2 = smoothstep(particleIntensity, dist,particle);
    // Комбинируем огонь и частицы
    vec3 finalCol = mix(fireCol, particleCol, particleIntensity);
    //float finalColor = smoothstep(0.2,1.0,texColor);
    //alpha = clamp(alpha + particleIntensity, 0.0, 1.0);

    fragColor = (vec4(finalCol.rgb*lightTotal, 0.4* distInvert));
}
