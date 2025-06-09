#ifdef GL_ES
    precision mediump float;
#endif

#define MAX 160

uniform sampler2D u_texture;
uniform vec4 u_ambientColor;
uniform float u_minLightness;

struct Light {
    vec2 position;
    vec4 color;
    float intensity;
    float radius;
    float transparency;
};

uniform int u_activeLights;
uniform Light u_lights[MAX];

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_worldPos;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords) * v_color;
    float dist;
    float attenuation;
    vec4 lightEffect;
    vec4 finalColor;
    int i;
    if (texColor.a <= 0.0) discard;
    vec4 accumulatedLight = u_ambientColor;
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
    finalColor = texColor;
    finalColor.rgb *= max(accumulatedLight.rgb, vec3(u_minLightness));
    finalColor.rgb = clamp(finalColor.rgb, 0.0, 1.0);
    finalColor.a = clamp(finalColor.a, 0.0, 1.0);
    gl_FragColor = finalColor;
}