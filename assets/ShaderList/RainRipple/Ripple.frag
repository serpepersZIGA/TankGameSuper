precision mediump float;

uniform float u_time;
uniform vec2 u_resolution;
uniform sampler2D u_splashTexture;
uniform vec3 u_waterColor;
uniform float u_intensity;

varying vec2 v_texCoord;
varying float v_lifetime;
varying float v_size;
varying float v_type;

// Шум для текстуры брызг
float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    return mix(
    mix(hash(i), hash(i + vec2(1.0, 0.0)), f.x),
    mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), f.x),
    f.y
    );
}

// Форма круга с мягкими краями
float circle(vec2 uv, vec2 center, float radius, float softness) {
    float dist = length(uv - center);
    return 1.0 - smoothstep(radius - softness, radius + softness, dist);
}

void main() {
    // Координаты внутри point sprite
    vec2 uv = gl_PointCoord;
    vec2 center = vec2(0.5);

    float alpha = 0.0;
    vec3 color = u_waterColor;

    // Разные типы брызг
    if (v_type < 0.3) {
        // Основное пятно брызг
        float time = mod(u_time + v_lifetime, 1.0);
        float life = 1.0 - abs(2.0 * time - 1.0); // Плавное появление и исчезновение

        // Форма круга
        float circle_shape = circle(uv, center, 0.4, 0.2);

        // Добавляем шум для естественности
        float n = noise(uv * 8.0 + v_lifetime);
        circle_shape *= mix(0.8, 1.2, n);

        alpha = circle_shape * life * u_intensity;
        color = mix(u_waterColor, vec3(1.0), 0.8);

    } else if (v_type < 0.6) {
        // Мелкие капли вокруг основного пятна
        vec2 offset = (uv - center) * 2.0;
        float dist = length(offset);

        if (dist < 1.0) {
            float angle = atan(offset.y, offset.x);
            vec2 droplet_pos = vec2(cos(angle * 3.0), sin(angle * 2.5)) * 0.3 + center;

            float droplet = circle(uv, droplet_pos, 0.1, 0.05);
            float time = mod(u_time + v_lifetime * 0.5, 1.0);
            alpha = droplet * (1.0 - time) * u_intensity * 0.7;
            color = vec3(1.0);
        }

    } else {
        // Волны/рябь
        float time = mod(u_time + v_lifetime, 2.0) / 2.0;
        if (time < 0.8) {
            float wave = sin(length(uv - center) * 20.0 - time * 10.0) *
            exp(-time * 3.0);
            wave = abs(wave) * (1.0 - smoothstep(0.0, 0.8, time));

            alpha = wave * 0.3 * u_intensity;
            color = mix(u_waterColor, vec3(0.9, 0.95, 1.0), 0.5);
        }
    }

    // Свечение брызг
    float glow = circle(uv, center, 0.6, 0.3);
    color += glow * 0.2 * u_intensity;

    // Final color
    gl_FragColor = vec4(color, alpha);

    // Альфа тестинг для прозрачности
    if (alpha < 0.01) discard;
}
