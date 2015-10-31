#version 120

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv)
// vec4 getNormalColor(vec2 uv)
// vec4 getSpecularColor(vec2 uv)

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
// 0 : smooth, 1: rough
uniform float roughness;


varying vec2 fUV;
varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world coordinates

void main()
{

    // TODO A4: Implement reflection mapping fragment shader
	vec3 N = normalize(fN);
	vec3 V = normalize(worldCam - worldPos.xyz);
	
	
	float Fo = 0.04;
	float pi = 3.14159265358;
    vec4 ks = getSpecularColor(fUV);
    vec4 kd = getDiffuseColor(fUV);
    ks = clamp(ks, 0.0,1.0);
    kd = clamp(kd, 0.0, 1.0);
  
    
	vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);
	for (int i = 0; i < numLights; i++) {
	
	  vec3 L = normalize(lightPosition[i] - worldPos.xyz); 
	  vec3 H = normalize(L + V);
	  
	  float vdoth = dot(V,H);
	  float ndoth = dot(N,H);
	  float ndotv = dot(N,V);
	  float ndotl = dot(N,L);
	
	  float fresnalTerm = Fo + (1.0 - Fo) * pow((1.0 - vdoth),5);
	  float microfacetDist = (1.0/(pow(roughness,2) * pow(ndoth,4))) * exp((pow(ndoth,2) - 1.0)/(pow(roughness,2) * pow(ndoth, 2)));
	  
	  float geomAtt1 = 2.0 * ndoth * ndotv / vdoth;
	  float geomAtt2 = 2.0 * ndoth * ndotl / vdoth;
	  float geomAtt = min(1.0, min(geomAtt1, geomAtt2));
	
	  float r = length(lightPosition[i] - worldPos.xyz);
	  finalColor += (ks * (fresnalTerm / pi) * ((microfacetDist * geomAtt) / (ndotv * ndotl)) + kd)
	  				* max(ndotl,0.0) * (vec4(lightIntensity[i],0.0) / pow(r,2)) + kd * vec4(ambientLightIntensity,0.0);
	}
    
    
	gl_FragColor = finalColor * exposure; 
}