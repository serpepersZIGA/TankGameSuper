#ifdef GL_ES
precision highp float;
#endif

#define PROCESSING_COLOR_SHADER

uniform float time;
uniform vec2 mouse;
uniform float dissipation;
uniform vec2 resolution;
uniform sampler2D buffer1;
uniform sampler2D buffer2;
uniform sampler2D cooling;

vec3 mod289(vec3 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec2 mod289(vec2 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec3 permute(vec3 x) { return mod289(((x*34.0)+1.0)*x); }

float decode(vec4 color) {
    return (color.r + color.g + color.b)/3.0;
}

vec4 encode(float r){
	int k = int(floor(r*255));
	int d = int(round(k % 3));
	float up = 1.0/255.0;

	if (d==0) return vec4(r,r,r,1.0);
	if (d==1) return vec4(r,r,r+up,1.0);
	if (d==2) return vec4(r,r+up,r+up,1.0);
}

// Description : GLSL 2D simplex noise function
//      Author : Ian McEwan, Ashima Arts
//  Maintainer : ijm
//     Lastmod : 20110822 (ijm)
//     License :
//  Copyright (C) 2011 Ashima Arts. All rights reserved.
//  Distributed under the MIT License. See LICENSE file.
//  https://github.com/ashima/webgl-noise
float snoise(vec2 v) {
    // Precompute values for skewed triangular grid
    const vec4 C = vec4(0.211324865405187,
                        // (3.0-sqrt(3.0))/6.0
                        0.366025403784439,
                        // 0.5*(sqrt(3.0)-1.0)
                        -0.577350269189626,
                        // -1.0 + 2.0 * C.x
                        0.024390243902439);
                        // 1.0 / 41.0
    // First corner (x0)
    vec2 i  = floor(v + dot(v, C.yy));
    vec2 x0 = v - i + dot(i, C.xx);

    // Other two corners (x1, x2)
    vec2 i1 = vec2(0.0);
    i1 = (x0.x > x0.y)? vec2(1.0, 0.0):vec2(0.0, 1.0);
    vec2 x1 = x0.xy + C.xx - i1;
    vec2 x2 = x0.xy + C.zz;

    // Do some permutations to avoid
    // truncation effects in permutation
    i = mod289(i);
    vec3 p = permute(
            permute( i.y + vec3(0.0, i1.y, 1.0))
                + i.x + vec3(0.0, i1.x, 1.0 ));

    vec3 m = max(0.5 - vec3(
                        dot(x0,x0),
                        dot(x1,x1),
                        dot(x2,x2)
                        ), 0.0);
    m = m*m ;
    m = m*m ;

    // Gradients:
    //  41 pts uniformly over a line, mapped onto a diamond
    //  The ring size 17*17 = 289 is close to a multiple
    //      of 41 (41*7 = 287)

    vec3 x = 2.0 * fract(p * C.www) - 1.0;
    vec3 h = abs(x) - 0.5;
    vec3 ox = floor(x + 0.5);
    vec3 a0 = x - ox;

    // Normalise gradients implicitly by scaling m
    // Approximation of: m *= inversesqrt(a0*a0 + h*h);
    m *= 1.79284291400159 - 0.85373472095314 * (a0*a0+h*h);

    // Compute final noise value at P
    vec3 g = vec3(0.0);
    g.x  = a0.x  * x0.x  + h.x  * x0.y;
    g.yz = a0.yz * vec2(x1.x,x2.x) + h.yz * vec2(x1.y,x2.y);
    return 130.0 * dot(m, g);
}

void main( void ) {
	vec2 position = ( gl_FragCoord.xy / resolution.xy );
	vec2 pixel = 1./resolution;
		float sum = 0.0;
		float lvl = decode(texture2D(buffer2, position + pixel * vec2(0.0, 0.0)));
		float k = ((1.0-lvl)*2.0);
		k = k*-k;

		float right = decode(texture2D(buffer1, position + pixel * vec2(1., k)));
		float left = decode(texture2D(buffer1, position + pixel * vec2(-1, k)));
		float d = (-3 * (k-0.5))*pow((right-left),3) ;

		float r = (snoise(position*8 + vec2(0,time)))*3.1456;
		vec2 warp = vec2(cos(r),sin(r))*0.00068*k;// + vec2(1.,1.);

		sum += decode(texture2D(buffer1, position + pixel * vec2(d+1., k) +  warp));
		sum += decode(texture2D(buffer1, position + pixel * vec2(d-1, k)+  warp));
		sum += decode(texture2D(buffer1, position + pixel * vec2(d, 1+k) +  warp));
		sum += decode(texture2D(buffer1, position + pixel * vec2(d, -1+k) +  warp));

		sum *= 0.25;
		position.y = 1-position.y;
		position.y += time;
		if (position.y > 1.0) position.y = (position.y - 1.0);

		sum -= pow(decode(texture2D(cooling, position + pixel * vec2(d, k)  +  warp)),3)*50.0;///(4.0);
		gl_FragColor = encode(sum);
}