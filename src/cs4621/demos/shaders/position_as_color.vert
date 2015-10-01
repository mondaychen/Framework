#version 120

varying vec3 color;
attribute vec3 vPos;
uniform mat4 VP;

void main()
{
	gl_Position = VP * vec4(vPos,1);
	//color = (vec3(vPos) + vec3(1,1,1)) * 0.5;
	color = (vec3(gl_Position.xyz) + vec3(1,1,1)) * 0.5;
}