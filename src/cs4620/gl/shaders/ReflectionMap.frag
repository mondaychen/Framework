#version 120

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv)
// vec4 getNormalColor(vec2 uv)
// vec4 getSpecularColor(vec2 uv)
// vec4 getEnvironmentColor(vec3 dir)

// Lighting Information

// Camera Information
uniform vec3 worldCam;

varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world coordinates

void main() {
  // TODO A4: Implement reflection mapping fragment shader
  vec3 N = normalize(fN);
  vec3 V = normalize(worldPos.xyz - worldCam);

  vec3 R = V - 2*N*(dot(V,N));

  gl_FragColor = getEnvironmentColor(R);
}
