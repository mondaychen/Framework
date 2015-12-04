#version 120

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv)
// vec4 getNormalColor(vec2 uv)
// vec4 getSpecularColor(vec2 uv)
// vec4 getEnvironmentColor(vec3 dir)

// Lighting Information

// Camera Information
uniform vec3 worldCam;

varying vec4 worldPos;


vec4 getWaterColor(vec3 V) {
    V.y = -V.y;
    return vec4(0.2, 0.8, 1, 1) * 0.5 + getEnvironmentColor(V) * 0.5;
}

void main() {
  vec3 V = worldPos.xyz - worldCam;
  gl_FragColor = worldPos.y < 0 ? getWaterColor(V) : getEnvironmentColor(V);
}