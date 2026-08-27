#version 300 es
#ifdef GL_ES
    precision mediump float;
#endif

#define MAX 100

uniform vec2 u_resolution;

out vec4 fragColor;

uniform sampler2D u_texture;
uniform vec4 u_ambientColor;
uniform float u_minLightness;
uniform float u_gamma;
uniform float u_brightness;
uniform float u_contrast;

struct Light {
    vec2 position;
    vec4 color;
    float intensity;
    float radius;
    float transparency;
};

uniform int u_activeLights;
uniform Light u_lights[MAX];

in vec4 v_color;
in vec2 v_texCoords;
in vec2 v_worldPos;


void main() {
    vec4 color;
    vec4 texColor = texture(u_texture, v_texCoords) * v_color;
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
        // the old (radius/dist)*0.05 glow term diverges to infinity as dist
        // approaches 0 - any pixel sitting right on/near a light's own
        // position got an unbounded additive blowout, which is why a lamp's
        // own texture always read as a flat white blob no matter the
        // brightness setting. Clamping dist to a floor keeps the same
        // "brighter close up" shape without the singularity.
        lightEffect = (light.color * light.intensity * attenuation) + ((light.radius / max(dist, light.radius*0.2)) * 0.05);
        accumulatedLight.rgb += lightEffect.rgb * lightEffect.a;
        accumulatedLight.a *= (1.0 - lightEffect.a * attenuation);
    }
    finalColor = texColor;
    if ((finalColor.r + finalColor.g + finalColor.b) * 0.3333 < 0.1)
        finalColor.rgb += ((((accumulatedLight.r+texColor.r)*0.1) + (accumulatedLight.g+texColor.g)*0.5 + (accumulatedLight.b+texColor.b)*0.5) * 0.3333) * 0.25;
    finalColor.rgb *= max(accumulatedLight.rgb, vec3(u_minLightness));
    // soft highlight rolloff instead of a hard 1.0 clip: below the knee
    // nothing changes, above it the value eases toward white asymptotically
    // instead of getting flattened the instant it crosses 1.0 - keeps some
    // texture/detail visible even in a very bright spot instead of a flat
    // white patch.
    const float knee = 0.8;
    vec3 excess = max(finalColor.rgb-knee, 0.0);
    finalColor.rgb = min(finalColor.rgb, vec3(knee)) + (1.0-knee)*(1.0-exp(-excess/(1.0-knee)));
    finalColor.rgb *= u_brightness;
    finalColor.rgb = (finalColor.rgb-0.5)*u_contrast+0.5;
    finalColor.rgb = clamp(finalColor.rgb, 0.0, 1.0);
    finalColor.rgb = pow(finalColor.rgb, vec3(1.0/u_gamma));
    finalColor.rgb = clamp(finalColor.rgb, 0.0, 1.0);
    finalColor.a = clamp(finalColor.a, 0.0, 1.0);
    fragColor = finalColor;
}