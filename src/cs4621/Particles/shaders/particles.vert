#version 120

uniform mat4 mModelViewProjection;

attribute vec3 vVertex; // Sem (POSITION 0)
attribute vec3 vColor; // Sem (COLOR 0)
attribute vec2 vUV; // Sem (TEXCOORD 0)

varying vec2 fUV;
varying vec3 fColor;

void main() {
    vec4 position = mModelViewProjection * vec4(vVertex, 1.0);
    fColor = vColor;
    fUV = vUV;
    gl_Position = vec4(position);
}
