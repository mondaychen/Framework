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


vec4 getWaterColor() {
    vec3 outgoing = - worldPos.xyz + worldCam;
    vec3 normal = vec3(0, 1, 0);
    vec3 relected = normal * 2 * dot(normal, outgoing) - outgoing;
    
    return vec4(0.2, 0.8, 1, 1) * 0.5 + getEnvironmentColor(relected) * 0.5;
}

vec4 getSkyColor() {
    return getEnvironmentColor(worldPos.xyz - worldCam);
}

void main() {
  gl_FragColor = worldPos.y < 0 ? getWaterColor() : getSkyColor();
}