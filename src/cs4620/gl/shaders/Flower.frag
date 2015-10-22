#version 120

// You May Use The Following Variables As RenderMaterial Input
// uniform vec4 colDiffuse;
// uniform vec4 colSpecular;

// Lighting Information
const int MAX_LIGHTS = 16;
uniform int numLights;
uniform vec3 lightIntensity[MAX_LIGHTS];
uniform vec3 lightPosition[MAX_LIGHTS];
uniform vec3 ambientLightIntensity;

// Camera Information
uniform vec3 worldCam;

// RenderMaterial Information
uniform float shininess;

varying vec3 fN; // Interpolated normal in world-space coordinates
varying vec4 worldPos; // Interpolated position in world-space coordinates

void main() {
  // TODO#PPA2 SOLUTION START

  // Iterate through the lights and add their intensity relative to its
  // diffuse and specular component

  gl_FragColor = vec4(1,1,1,1);

  // SOLUTION END
}

