#version 120

uniform float time;

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv)
// vec4 getNormalColor(vec2 uv)
// vec4 getSpecularColor(vec2 uv)
// vec4 getEnvironmentColor(vec3 dir)

// Lighting Information

// Camera Information
uniform vec3 worldCam;
const float PI = 3.1415926;

//const int numSamples = 5;
//const g = -0.80f;
//const SKY_RADIUS = 10;
//const SUN_RADIUS = 1000;
//const K_rfactor = 0.0025f;
//const K_mfactor = 0.0010f;
//const Sun_Intense = 20.0f

//const vec3 waveLength = ;


varying vec4 worldPos;

//need to be normalized in Vertex shader;
varying vec3 sunPositon;




vec4 getWaterColor() {
    vec3 outgoing = - worldPos.xyz + worldCam;
    vec3 normal = vec3(0, 1, 0);
    vec3 relected = normal * 2 * dot(normal, outgoing) - outgoing;
    
    return vec4(0.2, 0.8, 1, 1) * 0.5 + getEnvironmentColor(relected) * 0.5;
}

//vec4 getMieColor(vec3 outgoing) {
//    //vec4 backColor = vec4(0.678, 0.847, 0.902, 0.6);
//    
//    float g2 = g * g;
//    float cosangle = dot(outgoing, sunPositon) / (length(outgoing) * length(sunPositon));
//    
//    //calculate the attenuate phase;
//    float phasepart1 = 3 * (1 - g2) * (1 + cosangle * cosangle);
//    float phasepart2 = 2 * (2 + g2) * pow((1 + g2 - 2 * g * cosangle), 1.5);
//    float miePhase = phasepart1 / phasepart2;
//    
//    return
//}

float scale(float fCos)
{
    float fScaleDepth = 0.25f;
    float x = 1.0 - fCos;
    return fScaleDepth * exp(-0.00287 + x*(0.459 + x*(3.83 + x*(-6.80 + x*5.25))));
}


vec4 getSkyColor() {
    
    float K_rfactor = 0.0025f;
    float K_mfactor = 0.0010f;
    float Sun_Intense = 20.0f;
    int numSamples = 5;
    float fscale = 1.0f / (10 - length(worldCam));
    float fscaleOverscaledepth = fscale / 0.25f;
    
    vec3 camera2point = worldPos.xyz - worldCam;
    float lengthCamera = length(worldCam);
    
    camera2point = camera2point / length(camera2point);
    
    //calculate the sample ray;
    float sampleLength = length(camera2point) / numSamples;
    float scaledLength = sampleLength * fscale;
    vec3 sampleRay = camera2point * sampleLength;
    vec3 samplepoint = worldCam + sampleRay * 0.5;
    
    //Loop through the sample rays;
    vec3 backColor = vec3(0.678, 0.847, 0.902);
    
    for(int i = 0; i < 5; i++) {
        
        float sampleLength = length(samplepoint);
        float depth = exp(fscaleOverscaledepth * (lengthCamera - sampleLength));
        float sunAngle = dot(sunPositon, samplepoint) / sampleLength;
        float cameraAngle = dot(camera2point, samplepoint) / sampleLength;
        float scatterlight = depth * (scale(sunAngle) - scale(cameraAngle));
        
        //Calculate the attenuation phase;
        float attenuate = exp(-scatterlight * 4 * PI * (K_rfactor + K_mfactor));
        backColor = backColor + attenuate * depth * scaledLength;
        samplepoint = samplepoint + sampleRay;
    }
    
    return vec4(backColor * Sun_Intense * K_rfactor, 1);
    
}


void main() {
    
    //Set the raydirection to the inersect point;
  
    gl_FragColor = worldPos.y < 0 ? getWaterColor() : getSkyColor();
}