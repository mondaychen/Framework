#version 120

#define M_PI 3.1415926536897932
#define ncellulose 1.55  //refractive index of cellulose
// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv)
// vec4 getNormalColor(vec2 uv)
// vec4 getSpecularColor(vec2 uv)
// vec4 getFiberColorColor(fUV)
// vec4 getFiberDirectionColor(fUV)

// Lighting Information
const int MAX_LIGHTS = 16;
uniform int numLights;
uniform vec3 lightIntensity[MAX_LIGHTS];
uniform vec3 lightPosition[MAX_LIGHTS];
uniform vec3 ambientLightIntensity;

// Camera Information
uniform vec3 worldCam;
uniform float exposure;

// Shading Information
uniform float shininess;

varying vec2 fUV;
varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world coordinates
varying mat3 mTNB; // tangent-normal-binormal frame (local->world)

void main() {
	// Renormalize and orient the coordinate system 
	// to match the wood texture convention
	mat3 tnb = mat3(
	-normalize(mTNB[0]),
	normalize(mTNB[1]),
    -normalize(mTNB[2])
		);

	// Start Solution TODO#PPA2

	// Interpolating normals will change the length of the normal (renormalize the normal)
	// Calculate the light vector
	// Get pixel data from textures
	// Read the fiber vector from the fiber map and convert it to [-1,1] range
	// Convert fiber direction to world space
    // Compute refracted incident and reflection angles
    // subsurface gaussian component
    // Geometric factor
    // Add material contributions

	gl_FragColor = vec4(1,1,1,1);

	// End Solution TODO#PPA2
}
