#version 300 es
#ifdef GL_ES
precision mediump float;
#endif

#define MAX 100

uniform vec2 u_resolution;
uniform vec4 u_ambientColor;
uniform float u_minLightness;

uniform int u_activeLights;

varying vec2 v_worldPos;

struct Light {
    vec2 position;
    vec4 color;
    float intensity;
    float radius;
    float transparency;
};
uniform Light u_lights[MAX];

void main() {
    vec4 accumulatedLight = u_ambientColor;
    float dist, attenuation;
    vec4 lightEffect;
    int i;

    for (i = 0; i < MAX; i++) {
        if (i >= u_activeLights) break;
        Light light = u_lights[i];
        dist = distance(v_worldPos, light.position);
        if (dist > light.radius) continue;

        attenuation = 1.0 - smoothstep(light.radius
        * 0.15 /* 0.1 - это обратно пропорациональная сила рассеивания. Чем больше тем жестче */, light.radius, dist);
        attenuation *= (1.0 - light.transparency);
        attenuation = pow(attenuation, 1.5);
        lightEffect = (light.color * light.intensity * attenuation) + ((light.radius / dist) * 0.05);
        accumulatedLight.rgb += lightEffect.rgb * lightEffect.a;
        accumulatedLight.a *= (1.0 - lightEffect.a * attenuation);
    }

    // Только свет! Без текстуры!

    vec3 finalLight = max(accumulatedLight.rgb, vec3(u_minLightness));
    gl_FragColor = vec4(finalLight, accumulatedLight.a);
}