#ifdef GL_ES
precision mediump float;
#endif

#define MAX 60

uniform sampler2D u_texture;

struct Rain {
    vec2 position;
    float width;
    float height;
};

uniform Rain u_rain[MAX];

uniform vec2 u_resolution;

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_worldPos;



vec2 rotate(vec2 p,vec2 size, float a) {
    return vec2(
    (p.x * cos(a)) - (p.y * sin(a))+p.x,
    (p.x * sin(a)) + (p.y * cos(a))+p.y
    );
}

// Прямоугольник с закругленными углами
bool roundedRect(vec2 uv, vec2 center, vec2 size) {

//    if(uv.x>center.x && uv.x<center.x+size.x){
//        if(uv.y>center.y && uv.y<center.y+size.y){
//
//            return true;
//        }
//    }

    vec2 uv_rot = rotate(vec2(uv.x/3.4+120,uv.y/3.4),size,45);

    if (abs(uv_rot.x-center.x) < size.x/3 && abs(uv_rot.y-center.y) < size.y/3) {
        return true;
    }
    return false;

}

// Шум для создания естественного вида капель

void main() {
    vec2 uv = gl_FragCoord.xy;
    vec4 texColor = texture2D(u_texture, v_texCoords) * v_color;
    vec4 color_r = vec4(0.2,0.2,0.9,0.8);

    float dist;
    float attenuation;
    vec4 finalColor;
    int i;
    finalColor = texColor;

    for(i = 0;MAX>i;i++){
        Rain rain = u_rain[i];
        vec2 center = rain.position;
        float rotation = 45;
        vec2 size = vec2(rain.width,rain.height);
        float roundness = 50;

        // Поворачиваем координаты

        bool conf = roundedRect(uv, center, size);




        if (conf) {
            finalColor = vec4(0.2,0.2,0.8,0.5);

        }


    }



    gl_FragColor = finalColor;
}