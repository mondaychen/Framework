#version 120

uniform sampler2D particleTexture;

varying vec2 fUV;
varying vec3 fColor;

void main() {
    vec4 particleColor = texture2D(particleTexture, fUV);
    gl_FragColor = particleColor * vec4(fColor, 1.0);
}
