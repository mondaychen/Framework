#version 120

// Note: We multiply a vector with a matrix from the left side (M * v)!
// mProj * mView * mWorld * pos

// RenderCamera Input
uniform mat4 mViewProjection;

// RenderObject Input
uniform mat4 mWorld;
uniform mat3 mWorldIT;

// RenderMesh Input
attribute vec4 vPosition; // Sem (POSITION 0)
attribute vec3 vNormal; // Sem (NORMAL 0)
attribute vec2 vUV; // Sem (TEXCOORD 0)

// Shading Information
uniform float dispMagnitude;

varying vec2 fUV;
varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world-space coordinates

void main() {
	// TODO A4: Implement displacement mapping vertex shader
	// Calculate Point In World Space
	// Calculate Projected Point

	// We have to use the inverse transpose of the world transformation matrix for the normal
	fN = normalize((mWorldIT * vNormal).xyz);
	
	vec4 dispColor = getNormalColor(vUV);
	float dispMag = (dispColor.x + dispColor.y + dispColor.z)/3;
	
	worldPos = mWorld * (vPosition + vec4(dispMag * dispMagnitude * vNormal, 0.0));
	gl_Position = mViewProjection * worldPos;
	
	fUV = vUV;
}
