/**
 *  Fire Shader Copyright (C) 2018 Logan McCandless
 *  MIT License: https://opensource.org/licenses/MIT
 */

#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

uniform sampler2D texture;
uniform vec2 texOffset;
uniform vec2 resolution;
uniform float time;

varying vec4 vertColor;
varying vec4 vertTexCoord;

float decode(vec4 color) {
    return (color.r + color.g + color.b)/3.0;
}

void main(void) {
	float f =  decode(texture2D(texture, vertTexCoord.st));
	vec3 col = vec3(1.6*f, 1.6*pow(f,3), pow(f,5));
    gl_FragColor = vec4(col,1.0);
}