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

const int numSamples = 5;
const float SKY_RADIUS = 10;
const float SUN_RADIUS = 10.25f;
const float K_rfactor = 0.0025f;
const float K_mfactor = 0.0010f;

//Turn the Sun_Intense into the vec3;
const float Sun_Intense = 20.0f;

//const vec3 waveLength = ;


varying vec4 worldPos;

//need to be normalized in Vertex shader;
varying vec3 sunPositon;
varying vec3 sunDir;


// for Seascapge
vec2 resolution = vec2(800, 600);

// sea config


const int NUM_STEPS = 8;
const float EPSILON = 1e-3;
float EPSILON_NRM   = 0.1 / resolution.x;


const int ITER_GEOMETRY = 3;
const int ITER_FRAGMENT = 5;
const float SEA_HEIGHT = 0.6;
const float SEA_CHOPPY = 4.0;
const float SEA_SPEED = 0.8;
const float SEA_FREQ = 0.16;
const vec3 SEA_BASE = vec3(0.1,0.19,0.22);
const vec3 SEA_WATER_COLOR = vec3(0.8,0.9,0.6);
float SEA_TIME = time * SEA_SPEED;
mat2 octave_m = mat2(1.6,1.2,-1.2,1.6);

// A 2D hash function for use in noise generation that returns range [0 .. 1].
float hash( vec2 p ) {
    float h = dot(p,vec2(127.1,311.7)); 
    return fract(sin(h)*43758.5453123);
}

// A 2D psuedo-random wave / terrain function.
float noise( in vec2 p ) {
    vec2 i = floor( p );
    vec2 f = fract( p );
    vec2 u = f*f*(3.0-2.0*f);
    return -1.0+2.0*mix( 
                mix( hash( i + vec2(0.0,0.0) ), 
                     hash( i + vec2(1.0,0.0) ), 
                        u.x),
                mix( hash( i + vec2(0.0,1.0) ), 
                     hash( i + vec2(1.0,1.0) ), 
                        u.x), 
                u.y);
}

// lighting
float diffuse(vec3 n,vec3 l,float p) {
    return pow(dot(n,l) * 0.4 + 0.6,p);
}

float specular(vec3 n,vec3 l,vec3 e,float s) {    
    float nrm = (s + 8.0) / (3.1415 * 8.0);
    return pow(max(dot(reflect(e,n),l),0.0),s) * nrm;
}

// sea
float sea_octave(vec2 uv, float choppy) {
    uv += noise(uv);
    vec2 wv = 1.0-abs(sin(uv)); 
    vec2 swv = abs(cos(uv));  
  
    wv = mix(wv,swv,wv);
    return pow(1.0-pow(wv.x * wv.y,0.65),choppy);
}

// Compute the distance along Y axis of a point to the surface of the ocean
// using a low(er) resolution ocean height composition function (less iterations).
float map(vec3 p) {
    float freq = SEA_FREQ;
    float amp = SEA_HEIGHT;
    float choppy = SEA_CHOPPY;
    vec2 uv = p.xz; uv.x *= 0.75;
    float d, h = 0.0;    
    for(int i = 0; i < ITER_GEOMETRY; i++) {
        // start out with our 2D symmetric wave at the current frequency
        d = sea_octave((uv+SEA_TIME)*freq,choppy);
        // stack wave ontop of itself at an offset that varies over time for more height and wave pattern variance
        d += sea_octave((uv-SEA_TIME)*freq,choppy);

        h += d * amp; // Bump our height by the current wave function
        
        // "Twist" our domain input into a different space based on a permutation matrix
        uv *= octave_m;
        
        freq *= 1.9;
        amp *= 0.22;
        choppy = mix(choppy,1.0,0.2);
    }
    return p.y - h;
}

float map_detailed(vec3 p) {
    float freq = SEA_FREQ;
    float amp = SEA_HEIGHT;
    float choppy = SEA_CHOPPY;
    vec2 uv = p.xz; uv.x *= 0.75;
    
    float d, h = 0.0;    
    for(int i = 0; i < ITER_FRAGMENT; i++) {
        d = sea_octave((uv+SEA_TIME)*freq,choppy);
        d += sea_octave((uv-SEA_TIME)*freq,choppy);
        
        h += d * amp;
        uv *= octave_m;
        
        freq *= 1.9;
        amp *= 0.22;
        choppy = mix(choppy,1.0,0.2);
    }
    return p.y - h;
}

vec3 getNormal(vec3 p, float eps) {
    vec3 n;
    n.y = map_detailed(p);
    n.x = map_detailed(vec3(p.x+eps,p.y,p.z)) - n.y;
    n.z = map_detailed(vec3(p.x,p.y,p.z+eps)) - n.y;
    n.y = eps; 
    return normalize(n);
}

float heightMapTracing(vec3 ori, vec3 dir, out vec3 p) {  
    float tm = 0.0;
    float tx = 1000.0;

    float hx = map(ori + dir * tx);
    
    if(hx > 0.0) return tx;   

    float hm = map(ori + dir * tm); 
   
    float tmid = 0.0;
    for(int i = 0; i < NUM_STEPS; i++) {
        tmid = mix(tm,tx, hm/(hm-hx));
        p = ori + dir * tmid; 
                  
        float hmid = map(p);

        if(hmid < 0.0) {
            tx = tmid;
            hx = hmid;
        } else {
            tm = tmid;
            hm = hmid;
        }
    }

    return tmid;
}



// Gejun's part start

float phase(float q, float g) {
    //vec4 backColor = vec4(0.678, 0.847, 0.902, 0.6);
    float g2 = g * g;
    
    //calculate the attenuate phase;
    float phasepart1 = 3 * (1 - g2) * (1 + q * q);
    float phasepart2 = 2 * (2 + g2) * pow((1 + g2 - 2 * g * q), 1.5);
    return phasepart1 / phasepart2;
}

//Used to scatter the sun reflection;
float getMiePhase(float q, float q2, float g2, float g) {
    
    float phasepart1 = 3 * (1 - g2) * (1 + q2);
    float phasepart2 = 2 * (2 + g2) * pow((1 + g2 - 2 * g * q), 1.5);
    return phasepart1 / phasepart2;
}


float scale(float Cos)
{
    float scaleDepth = 0.10f;
    float x = 1.0 - Cos;
    return scaleDepth * exp(-0.00287 + x*(0.459 + x*(3.83 + x*(-6.80 + x*5.25))));
}


vec3 getSkyColor(vec3 waveLength, vec3 dir, vec3 position) {
   
    vec3 backColor = vec3(0, 0, 0);
    
    
    float fscale = 1.0f / (10.25f - length(worldCam));
    float fscaleOverscaledepth = fscale / 0.23f;
    
    vec3 camera2point = dir;
    float lengthCamera = length(worldCam);
    
    camera2point = camera2point / length(camera2point);
    
    //calculate the sample ray;
    float sampleLength = length(camera2point) / numSamples;
    float scaledLength = sampleLength * fscale;
    vec3 sampleRay = camera2point * sampleLength;
    vec3 samplepoint = worldCam + sampleRay * 0.5;
    
    //Loop through the sample rays;
    
    for(int i = 0; i < 5; i++) {
        
        float sampleLength = length(samplepoint);
        float depth = exp(fscaleOverscaledepth * (10.0f - sampleLength));
        
        float sunlength = length(position);
        float sunAngle = dot(position, samplepoint) / (sampleLength * sunlength);
        float cameraAngle = dot(camera2point, samplepoint) / sampleLength;
        float scatterlight = depth * (scale(sunAngle) - scale(cameraAngle));
        
        //Calculate the attenuation phase;
        vec3 attenuate = exp(-scatterlight * 4 * PI * (waveLength * K_rfactor + K_mfactor));
        backColor = backColor + attenuate * depth * scaledLength;
        samplepoint = samplepoint + sampleRay;
    }
    
    return backColor;
    
}


vec3 getSkyColorFull(vec3 dir, out vec3 specularColor) {
    //Initialize the parameter;
    
    float alpha = dot(dir, sunPositon) / length(sunPositon);
    
    //Loop through the sample rays;
   // vec3 backColor = vec3(0.678, 0.847, 0.902);
    
    //Set waveLength;
    float redLength = pow(0.65f, 4.0f);
    float greenLength = pow(0.57f, 4.0f);
    float blueLength = pow(0.45f, 4.0f);
    vec3 waveLength = vec3(1 / redLength, 1 / greenLength, 1 / blueLength);
    
    vec3 fakesunPosition = vec3(0, 0, 1);
    
    vec3 newcolor1 = getSkyColor(waveLength, dir, fakesunPosition) * waveLength * K_rfactor * Sun_Intense;
    
    
    vec3 newcolor2 = getSkyColor(waveLength, dir, sunPositon) * K_mfactor * Sun_Intense;

    //vec3 skyColor = getSkyColor() * waveLength * K_rfactor;
    
    
    //Controling the blue part color of the sky, the parameter is not sure;
    vec3 Color1 = newcolor1 * phase(alpha, 0);
    
    //Controling the sun part of the sky, the parameter is not sure;
    vec3 Color2 = newcolor2 * getMiePhase(alpha, alpha * alpha, -0.990* -0.990, -0.990);
    
    vec3 skyColor = Color1 + Color2;

    specularColor = Color2;
    
    //+ Color2;
    
    //getSkyColorSimple(dir)
    
    
    //newcolor1 * phase(alpha, 0) + newcolor2 * phase(alpha, -0.80f);
    //skyColor.a = skyColor.b;
    return skyColor;
}


// p: point on ocean surface to get color for
// n: normal on ocean surface at <p>
// l: light (sun) direction
// eye: ray direction from camera position for this pixel
// dist: distance from camera to point <p> on ocean surface
vec3 getSeaColor(vec3 p, vec3 n, vec3 l, vec3 eye, vec3 dist) {  
    // bteitler: Fresnel is an exponential that gets bigger when the angle between ocean
    // surface normal and eye ray is smaller
    float fresnel = 1.0 - max(dot(n,-eye),0.0);
    fresnel = pow(fresnel,3.0) * 0.65;

    vec3 specularColor;
        
    vec3 reflected = getSkyColorFull(reflect(eye,n), specularColor);    
    
    vec3 refracted = SEA_BASE + diffuse(n,l,80.0) * SEA_WATER_COLOR * 0.12; 
    
    vec3 color = mix(refracted,reflected,fresnel);
    
    float atten = max(1.0 - dot(dist,dist) * 0.001, 0.0);
    color += SEA_WATER_COLOR * (p.y - SEA_HEIGHT) * 0.18 * atten;
    
    color += specularColor * specular(n,l,eye,60.0);
    
    return color;
}



// Gejun's part end

void main() {
    vec4 fragCoord = gl_FragCoord;
    
    vec2 uv = fragCoord.xy / resolution.xy;

    
    uv = uv * 2.0 - 1.0; // Shift pixel coordinates from 0 to 1 to between -1 and 1
    uv.x *= resolution.x / resolution.y; // Aspect ratio correction - if you don't do this your rays will be distorted
        
    // ray
    vec3 dir = normalize(worldPos.xyz - worldCam);

    
    // tracing

    vec3 pointOnSea;
    heightMapTracing(worldCam, dir, pointOnSea);

    vec3 dist = pointOnSea - worldCam;

    vec3 normal = getNormal(pointOnSea,
             dot(dist,dist)  
                * EPSILON_NRM
           );

    vec3 sunlight = normalize(worldCam - sunPositon);
    
    
    vec3 specularColor;
    vec3 skyColor = getSkyColorFull(dir, specularColor);

    vec3 color = mix(
        skyColor, //getSkyColorSimple(dir),
        getSeaColor(pointOnSea, normal, sunlight, dir, dist),
        pow(smoothstep(0.0,-0.05,dir.y), 0.5) // bteitler: Can be thought of as "fog" that gets thicker in the distance
    );
    

    gl_FragColor = vec4(pow(color,vec3(0.75)), 1.0);
}