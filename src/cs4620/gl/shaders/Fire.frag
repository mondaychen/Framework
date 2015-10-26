#version 120

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv) // samples fire.png
// vec4 getNormalColor(vec2 uv)  // samples noise.png

uniform float time;

const vec3 texture_scales = vec3(1.0, 2.0, 3.0);
const vec3 scroll_speeds = vec3(1.0, 1.0, 1.0);

varying vec2 fUV;
varying vec3 fPos;

void main() {
  // TODO#PPA2 SOLUTION START
    
	gl_FragColor = vec4(1,1,1,1);
  // SOLUTION END
}