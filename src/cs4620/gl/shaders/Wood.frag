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

float gaussian(float beta, float psiH) {
  return exp(-psiH*psiH/(2*beta*beta))/sqrt(2*M_PI)/beta;
}

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

  vec3 N = normalize(fN);
  vec3 V = normalize(worldCam - worldPos.xyz);

  vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);

  vec3 localFiberDir = normalize(getFiberDirectionColor(fUV).xyz * 2.0 - 1.0);
  vec3 U = normalize(tnb * localFiberDir);
  vec4 fiberColor = getFiberColorColor(fUV);


  for (int i = 0; i < numLights; i++) {
    float r = length(lightPosition[i] - worldPos.xyz);
    vec3 L = normalize(lightPosition[i] - worldPos.xyz);
    vec3 H = normalize(L + V);

    // calculate diffuse term
    vec4 Idiff = getDiffuseColor(fUV) * max(dot(N, L), 0.0);

    // calculate specular term
    vec4 Ispec = getSpecularColor(fUV) * pow(max(dot(N, H), 0.0), shininess);

    // subsurface
    float psiI = asin(dot(V, U)/ncellulose);
    float psiR = asin(dot(L, U)/ncellulose);
    float psiD = psiR - psiI;
    float psiH = psiR + psiI;
    vec4 Isub = fiberColor * gaussian(length(getSpecularColor(fUV)), psiH) / (cos(psiD/2) * cos(psiD/2));

    finalColor += vec4(lightIntensity[i], 0.0) * (Idiff + Ispec + Isub);

    if (dot(N, L) <=0) {
      finalColor = vec4(0,0,0,1);
    }

  }

  // calculate ambient term
  vec4 Iamb = getDiffuseColor(fUV);

  finalColor += vec4(ambientLightIntensity, 0.0) * Iamb;

  gl_FragColor = finalColor * exposure;
	// End Solution TODO#PPA2
}
