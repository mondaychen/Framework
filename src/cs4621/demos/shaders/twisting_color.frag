#version 110

uniform vec3 color;
varying vec2 sc;

void main()
{
    gl_FragColor = vec4(vec3(sc,1)*color, 1);
}