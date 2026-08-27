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
    vec4 texColor = texture(u_texture, v_texCoords) * v_color;
    float dist;
    float attenuation;
    vec4 finalColor;
    int i;
    if (texColor.a <= 0.0) discard;
    vec4 accumulatedLight = u_ambientColor;
    for (i = 0; i < MAX; i++) {
        if (i >= u_activeLights) break;
        Light light = u_lights[i];
        dist = distance(v_worldPos, light.position);
        if (dist > light.radius) continue;

        // was: smoothstep with an inner plateau + a colorless "glow" term
        // multiplied AGAIN by the color's own alpha - that's why a lamp read
        // as a flat tinted disc instead of actual light. Just add the
        // light's own color, scaled by intensity and a plain smooth falloff.
        attenuation = 1.0 - smoothstep(0.0, light.radius, dist);
        attenuation = pow(attenuation, 2.0);
        attenuation *= (1.0 - light.transparency);
        accumulatedLight.rgb += light.color.rgb * light.intensity * attenuation;
    }
    // a safety ceiling so a pile of overlapping lights (a dense flamethrower
    // stream, several lamps close together) degrades to "very bright" rather
    // than growing without bound - the soft-knee rolloff below only has so
    // much headroom to work with before everything above it is equally
    // indistinguishable-white anyway.
    accumulatedLight.rgb = min(accumulatedLight.rgb, vec3(3.0));
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