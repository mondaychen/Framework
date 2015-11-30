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

void main() {
  // TODO A4
  vec3 V = worldPos.xyz - worldCam;
  gl_FragColor = worldPos.y <= 0.1 ? vec4(0.2, 0.8, 1, 1) : getEnvironmentColor(V);
}