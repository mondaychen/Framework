#version 120

// You May Use The Following Variables As RenderMaterial Input
//uniform vec4 colDiffuse;
//uniform vec4 colSpecular;

// Lighting Information
const int MAX_LIGHTS = 16;
uniform int numLights;
uniform vec3 lightIntensity[MAX_LIGHTS];
uniform vec3 lightPosition[MAX_LIGHTS];
uniform vec3 ambientLightIntensity;


// Camera Information
uniform vec3 worldCam;
uniform float exposure;

// RenderMaterial Information
uniform float shininess;

varying vec3 fN; // Interpolated normal in world-space coordinates
varying vec4 worldPos; // Interpolated position in world-space coordinates

void main() {
  // TODO#PPA2 SOLUTION START

  // Iterate through the lights and add their intensity relative to its
  // diffuse and specular component
    vec3 N = normalize(fN);
    vec3 V = normalize(worldCam - worldPos.xyz);
    
    vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);
    
    for (int i = 0; i < numLights; i++) {
        float r = length(lightPosition[i] - worldPos.xyz);
        vec3 L = normalize(lightPosition[i] - worldPos.xyz);
        vec3 H = normalize(L + V);
        
        // calculate diffuse term
        vec4 Idiff = colDiffuse * max(dot(N, L), 0.0);
        
        // calculate specular term
        vec4 Ispec = colSpecular * pow(max(dot(N, H), 0.0), shininess);
        
        finalColor += vec4(lightIntensity[i], 0.0) * (Idiff + Ispec) / (r*r);
    }
    
    // calculate ambient term
    vec4 Iamb = colDiffuse;
    
    gl_FragColor = (finalColor + vec4(ambientLightIntensity, 0.0) * Iamb) * exposure;
    


    

    
}

