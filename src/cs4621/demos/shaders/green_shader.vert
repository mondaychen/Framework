#version 120

uniform mat4 VP;

attribute vec3 vPos;

void main()
{
    gl_Position = VP * vec4(vPos,1); 
}
